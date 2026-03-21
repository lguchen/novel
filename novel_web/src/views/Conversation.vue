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
              <el-button class="action-btn" type="primary" @click="setAction('chat')" :class="{ active: currentAction === 'chat' }">
                <el-icon><ChatDotRound /></el-icon>
                对话
              </el-button>
              <el-button class="action-btn" type="success" @click="setAction('continue')" :class="{ active: currentAction === 'continue' }">
                <el-icon><DocumentAdd /></el-icon>
                续写
              </el-button>
              <el-button class="action-btn" type="warning" @click="setAction('character')" :class="{ active: currentAction === 'character' }">
                <el-icon><User /></el-icon>
                角色
              </el-button>
              <el-button class="action-btn" type="info" @click="setAction('plot')" :class="{ active: currentAction === 'plot' }">
                <el-icon><TrendCharts /></el-icon>
                剧情
              </el-button>
              <el-button class="action-btn" type="danger" @click="setAction('dialogue')" :class="{ active: currentAction === 'dialogue' }">
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
              <div class="chat-messages" ref="chatMessagesRef">
                    <div v-for="(message, index) in messages" :key="index" :class="['message', message.role]">
                      <div class="message-header">
                        <span class="message-role">{{ message.role === 'user' ? '你' : 'AI助手' }}</span>
                        <span class="message-time">{{ message.time }}</span>
                      </div>
                      <div class="message-content">
                        <div v-if="message.role === 'assistant'" class="markdown-content" v-html="renderMarkdown(message.content)"></div>
                        <div v-else>{{ message.content }}</div>
                      </div>
                    </div>
                    <div v-if="isStreaming" class="message assistant">
                      <div class="message-header">
                        <span class="message-role">AI助手</span>
                        <span class="message-time">{{ formatTime(new Date()) }}</span>
                      </div>
                      <div class="message-content">
                        <div class="markdown-content" v-html="renderMarkdown(streamingContent)"></div>
                      </div>
                    </div>
                  </div>
              
              <div class="chat-input">
                <el-input
                  v-model="userInput"
                  type="textarea"
                  :rows="4"
                  :placeholder="inputPlaceholder"
                  @keydown.enter.prevent="sendMessage"
                />
                <div class="input-actions">
                  <span class="hint">按 Enter 发送</span>
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
import MarkdownIt from 'markdown-it'

const md = new MarkdownIt()

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
const currentAction = ref('')
const selectedAgentId = ref(null)
const isStreaming = ref(false)
const streamingContent = ref('')
const chatMessagesRef = ref(null)

const inputPlaceholder = computed(() => {
  const actionHints = {
    chat: '输入你的问题或创作想法...',
    continue: '输入续写指令，如：继续写下一章...',
    character: '输入角色相关问题，如：帮我完善主角的性格...',
    plot: '输入剧情相关问题，如：帮我规划接下来的剧情发展...',
    dialogue: '输入对话生成指令，如：生成一段主角和配角的对话...'
  }
  return actionHints[currentAction.value] || '输入你的创作指令或问题...'
})

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
}

function renderMarkdown(content) {
  return md.render(content)
}

function scrollToBottom() {
  setTimeout(() => {
    if (chatMessagesRef.value) {
      chatMessagesRef.value.scrollTop = chatMessagesRef.value.scrollHeight
    }
  }, 100)
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
  
  // 自动滚动到底部
  scrollToBottom()
  
  try {
    // 模拟流式输出
    isStreaming.value = true
    streamingContent.value = ''
    
    // 先发送请求
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
      // 模拟流式输出效果
      const fullContent = response.response
      let index = 0
      const interval = setInterval(() => {
        if (index < fullContent.length) {
          // 每次添加几个字符
          const chunkSize = Math.min(5, fullContent.length - index)
          streamingContent.value += fullContent.substring(index, index + chunkSize)
          index += chunkSize
          // 自动滚动
          scrollToBottom()
        } else {
          clearInterval(interval)
          // 完成流式输出
          isStreaming.value = false
          messages.value.push({
            role: 'assistant',
            content: fullContent,
            time: formatTime(new Date())
          })
          
          if (response.novel) {
            novel.value = response.novel
            contextMemory.value = response.contextMemory
          }
          
          ElMessage.success('AI回复成功')
          // 自动滚动
          scrollToBottom()
        }
      }, 50) // 每50毫秒添加一次内容
    }
  } catch (err) {
    ElMessage.error('发送失败')
    messages.value.pop()
    isStreaming.value = false
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
  gap: 8px;
  width: 100%;
  overflow: hidden;
  box-sizing: border-box;
  padding: 0;
  margin: 0;
}

.action-btn {
  width: 100% !important;
  min-width: 100% !important;
  max-width: 100% !important;
  box-sizing: border-box !important;
  transition: all 0.3s ease !important;
  padding: 10px 14px !important;
  justify-content: center !important;
  text-align: center !important;
  margin: 0 !important;
  border-radius: 6px !important;
  font-size: 14px !important;
  height: 48px !important;
  line-height: 24px !important;
}

.action-btn .el-icon {
  margin-right: 8px !important;
  font-size: 16px !important;
}

.action-btn:hover {
  transform: scale(1.02) !important;
}

.action-btn:active {
  transform: scale(1.05) !important;
}

.action-btn.active {
  background-color: #667eea !important;
  border-color: #667eea !important;
  color: white !important;
  transform: scale(1) !important;
  width: 100% !important;
  min-width: 100% !important;
  max-width: 100% !important;
  box-sizing: border-box !important;
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
  padding: 10px 14px !important;
  border-radius: 12px;
  animation: fadeIn 0.3s ease-in;
  margin: 6px 0 !important;
  box-sizing: border-box !important;
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
  margin-bottom: 4px !important;
  font-size: 12px;
  opacity: 0.8;
  padding: 0 !important;
}

.message-content {
  line-height: 1.6 !important;
  white-space: pre-wrap;
  padding: 0 !important;
  margin: 0 !important;
  min-height: 16px;
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

.markdown-content :deep(h1),
.markdown-content :deep(h2),
.markdown-content :deep(h3),
.markdown-content :deep(h4),
.markdown-content :deep(h5),
.markdown-content :deep(h6) {
  margin-top: 16px;
  margin-bottom: 8px;
  font-weight: 600;
}

.markdown-content :deep(h1) {
  font-size: 24px;
  border-bottom: 1px solid #eee;
  padding-bottom: 8px;
}

.markdown-content :deep(h2) {
  font-size: 20px;
  border-bottom: 1px solid #eee;
  padding-bottom: 4px;
}

.markdown-content :deep(h3) {
  font-size: 18px;
}

.markdown-content :deep(p) {
  margin: 4px 0 !important;
  line-height: 1.6 !important;
  padding: 0 !important;
}

.markdown-content :deep(ul),
.markdown-content :deep(ol) {
  margin: 6px 0 !important;
  padding-left: 24px !important;
  line-height: 1.6 !important;
}

.markdown-content :deep(li) {
  margin: 2px 0 !important;
  line-height: 1.6 !important;
  padding: 0 !important;
}

.markdown-content :deep(code) {
  background-color: #f0f0f0;
  padding: 2px 6px;
  border-radius: 4px;
  font-family: monospace;
  font-size: 14px;
}

.markdown-content :deep(pre) {
  background-color: #f5f5f5;
  padding: 12px;
  border-radius: 6px;
  overflow-x: auto;
  margin: 8px 0;
}

.markdown-content :deep(pre code) {
  background-color: transparent;
  padding: 0;
}

.markdown-content :deep(blockquote) {
  border-left: 4px solid #667eea;
  padding-left: 16px;
  margin: 8px 0;
  color: #666;
  background-color: #f8f9fa;
  padding: 12px 16px;
  border-radius: 0 6px 6px 0;
}

.markdown-content :deep(a) {
  color: #667eea;
  text-decoration: none;
}

.markdown-content :deep(a:hover) {
  text-decoration: underline;
}

.markdown-content :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 8px 0;
}

.markdown-content :deep(th),
.markdown-content :deep(td) {
  border: 1px solid #ddd;
  padding: 8px 12px;
  text-align: left;
}

.markdown-content :deep(th) {
  background-color: #f5f7fa;
  font-weight: 600;
}

.markdown-content :deep(img) {
  max-width: 100%;
  border-radius: 6px;
  margin: 8px 0;
}

.markdown-content :deep(hr) {
  border: none;
  border-top: 1px solid #eee;
  margin: 16px 0;
}
</style>