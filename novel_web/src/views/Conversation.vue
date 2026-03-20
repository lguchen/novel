<template>
  <div class="conversation">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1 class="logo">📚 AI 小说创作助手</h1>
          <nav class="nav-menu">
            <router-link to="/" class="nav-item">首页</router-link>
            <router-link to="/novels" class="nav-item">小说管理</router-link>
            <router-link to="/ai-models" class="nav-item">AI模型配置</router-link>
            <router-link to="/agents" class="nav-item">智能体配置</router-link>
          </nav>
        </div>
      </el-header>
      
      <el-main>
        <div v-if="loading" class="loading-container">
          <el-icon class="is-loading"><Loading /></el-icon>
          <span>加载中...</span>
        </div>
        
        <div v-else-if="!novel" class="error-container">
          <el-icon><Warning /></el-icon>
          <span>小说不存在</span>
        </div>
        
        <div v-else class="conversation-container">
          <div class="conversation-sidebar">
            <div class="sidebar-header">
              <h3>{{ novel.title }}</h3>
              <el-button size="small" @click="goBack">
                <el-icon><ArrowLeft /></el-icon>
                返回
              </el-button>
            </div>
            
            <div class="action-buttons">
              <el-button type="primary" @click="setAction('chat')" :class="{ active: currentAction === 'chat' }">
                <el-icon><ChatDotRound /></el-icon>
                对话
              </el-button>
              <el-button type="success" @click="setAction('continue')" :class="{ active: currentAction === 'continue' }">
                <el-icon><DocumentAdd /></el-icon>
                续写
              </el-button>
              <el-button type="warning" @click="setAction('character')" :class="{ active: currentAction === 'character' }">
                <el-icon><User /></el-icon>
                角色
              </el-button>
              <el-button type="info" @click="setAction('plot')" :class="{ active: currentAction === 'plot' }">
                <el-icon><TrendCharts /></el-icon>
                剧情
              </el-button>
              <el-button type="danger" @click="setAction('dialogue')" :class="{ active: currentAction === 'dialogue' }">
                <el-icon><ChatLineSquare /></el-icon>
                对话生成
              </el-button>
            </div>
            
            <div class="agent-selector">
              <h4>选择智能体</h4>
              <el-select v-model="selectedAgentId" placeholder="请选择智能体" style="width: 100%">
                <el-option v-for="agent in agents" :key="agent.id" :label="agent.name" :value="agent.id" />
              </el-select>
            </div>
            
            <div class="context-preview">
              <h4>上下文记忆</h4>
              <el-scrollbar height="200px">
                <div class="context-content">
                  {{ contextMemory || '暂无上下文信息' }}
                </div>
              </el-scrollbar>
            </div>
          </div>
          
          <div class="conversation-main">
            <div class="chat-container">
              <div class="chat-messages">
                <div v-for="(message, index) in messages" :key="index" :class="['message', message.role]">
                  <div class="message-header">
                    <span class="message-role">{{ message.role === 'user' ? '你' : 'AI助手' }}</span>
                    <span class="message-time">{{ message.time }}</span>
                  </div>
                  <div class="message-content">
                    {{ message.content }}
                  </div>
                </div>
              </div>
              
              <div class="chat-input">
                <el-input
                  v-model="userInput"
                  type="textarea"
                  :rows="4"
                  placeholder="输入你的创作指令或问题..."
                  @keydown.ctrl.enter="sendMessage"
                />
                <div class="input-actions">
                  <span class="hint">按 Ctrl+Enter 发送</span>
                  <el-button type="primary" @click="sendMessage" :loading="sending">
                    <el-icon><Promotion /></el-icon>
                    发送
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { novelApi, agentApi, conversationApi } from '../api'
import { ElMessage } from 'element-plus'
import { ArrowLeft, ChatDotRound, DocumentAdd, User, TrendCharts, ChatLineSquare, Promotion, Loading, Warning } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const novelId = computed(() => parseInt(route.params.id))
const novel = ref(null)
const agents = ref([])
const messages = ref([])
const contextMemory = ref('')
const loading = ref(false)
const sending = ref(false)
const userInput = ref('')
const currentAction = ref('chat')
const selectedAgentId = ref(null)

onMounted(async () => {
  await Promise.all([loadNovel(), loadAgents()])
  await loadConversationHistory()
})

async function loadNovel() {
  loading.value = true
  try {
    novel.value = await novelApi.getById(novelId.value)
    contextMemory.value = novel.value?.contextMemory || ''
  } catch (err) {
    ElMessage.error('加载小说失败')
  } finally {
    loading.value = false
  }
}

async function loadAgents() {
  try {
    agents.value = await agentApi.getActive()
    if (agents.value.length > 0) {
      selectedAgentId.value = agents.value[0].id
    }
  } catch (err) {
    console.error('加载智能体列表失败', err)
  }
}

async function loadConversationHistory() {
  try {
    const history = await conversationApi.getHistory(novelId.value)
    messages.value = history.map(h => ({
      role: h.role,
      content: h.content,
      time: formatTime(h.createdAt)
    }))
  } catch (err) {
    console.error('加载对话历史失败', err)
  }
}

function setAction(action) {
  currentAction.value = action
  const actionHints = {
    chat: '输入你的问题或创作想法',
    continue: '输入续写指令，如：继续写下一章',
    character: '输入角色相关问题，如：帮我完善主角的性格',
    plot: '输入剧情相关问题，如：帮我规划接下来的剧情发展',
    dialogue: '输入对话生成指令，如：生成一段主角和配角的对话'
  }
  userInput.value = actionHints[action] || ''
}

async function sendMessage() {
  if (!userInput.value.trim()) {
    ElMessage.warning('请输入内容')
    return
  }
  
  if (!selectedAgentId.value) {
    ElMessage.warning('请选择智能体')
    return
  }
  
  const userMessage = userInput.value
  messages.value.push({
    role: 'user',
    content: userMessage,
    time: formatTime(new Date())
  })
  
  userInput.value = ''
  sending.value = true
  
  try {
    const response = await conversationApi.chat({
      novelId: novelId.value,
      agentId: selectedAgentId.value,
      message: userMessage,
      action: currentAction.value
    })
    
    if (response.error) {
      ElMessage.error(response.error)
      messages.value.pop()
    } else {
      messages.value.push({
        role: 'assistant',
        content: response.response,
        time: formatTime(new Date())
      })
      
      if (response.novel) {
        novel.value = response.novel
        contextMemory.value = response.contextMemory
      }
      
      ElMessage.success('AI回复成功')
    }
  } catch (err) {
    ElMessage.error('发送失败')
    messages.value.pop()
  } finally {
    sending.value = false
  }
}

function formatTime(date) {
  const d = new Date(date)
  return d.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

function goBack() {
  router.push(`/novels/${novelId.value}/edit`)
}
</script>

<style scoped>
.conversation {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.el-header {
  background-color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
}

.header-content {
  max-width: 100%;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  padding: 0 20px;
}

.logo {
  font-size: 24px;
  font-weight: bold;
  color: #667eea;
  margin: 0;
}

.nav-menu {
  display: flex;
  gap: 30px;
}

.nav-item {
  color: #333;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s;
}

.nav-item:hover {
  color: #667eea;
}

.loading-container,
.error-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
}

.loading-container {
  color: #667eea;
}

.error-container {
  color: #f56c6c;
}

.conversation-container {
  display: flex;
  height: calc(100vh - 60px);
  gap: 20px;
  padding: 20px;
}

.conversation-sidebar {
  width: 300px;
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.sidebar-header h3 {
  margin: 0;
  color: #333;
  font-size: 16px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.action-buttons .el-button {
  justify-content: flex-start;
}

.action-buttons .el-button.active {
  background-color: #667eea;
  border-color: #667eea;
  color: white;
}

.agent-selector h4,
.context-preview h4 {
  margin: 0 0 10px;
  color: #333;
  font-size: 14px;
}

.context-preview {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.context-content {
  font-size: 12px;
  color: #666;
  line-height: 1.6;
  white-space: pre-wrap;
}

.conversation-main {
  flex: 1;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.message {
  max-width: 80%;
  padding: 12px 16px;
  border-radius: 12px;
  animation: fadeIn 0.3s ease-in;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.message.user {
  align-self: flex-end;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.message.assistant {
  align-self: flex-start;
  background: #f5f7fa;
  color: #333;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  font-size: 12px;
  opacity: 0.8;
}

.message-content {
  line-height: 1.6;
  white-space: pre-wrap;
}

.chat-input {
  border-top: 1px solid #eee;
  padding: 20px;
  background: white;
}

.input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.hint {
  font-size: 12px;
  color: #999;
}
</style>