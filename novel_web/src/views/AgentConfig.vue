<template>
  <div class="agent-config">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1 class="logo">📚 AI 小说创作助手</h1>
          <nav class="nav-menu">
            <router-link to="/" class="nav-item">首页</router-link>
            <router-link to="/novels" class="nav-item">小说管理</router-link>
            <router-link to="/prompts" class="nav-item">提示词广场</router-link>
            <router-link to="/ai-models" class="nav-item">AI模型配置</router-link>
            <router-link to="/agents" class="nav-item active">智能体配置</router-link>
          </nav>
        </div>
      </el-header>
      
      <el-main>
        <div class="page-header">
          <h2>智能体配置</h2>
          <el-button type="primary" @click="showCreateDialog = true">
            <el-icon><Plus /></el-icon>
            添加智能体
          </el-button>
        </div>
        
        <div v-if="loading" class="loading-container">
          <el-icon class="is-loading"><Loading /></el-icon>
          <span>加载中...</span>
        </div>
        
        <div v-else-if="agents.length === 0" class="empty-container">
          <el-empty description="暂无智能体配置">
            <el-button type="primary" @click="showCreateDialog = true">添加智能体</el-button>
          </el-empty>
        </div>
        
        <div v-else class="agents-grid">
          <div v-for="agent in agents" :key="agent.id" class="agent-card">
            <div class="agent-header">
              <div class="agent-icon">{{ getAgentIcon(agent.role) }}</div>
              <el-tag :type="agent.isActive ? 'success' : 'info'" size="small">
                {{ agent.isActive ? '已启用' : '已禁用' }}
              </el-tag>
            </div>
            <div class="agent-info">
              <h3>{{ agent.name }}</h3>
              <p class="agent-role">{{ agent.role || '未设定角色' }}</p>
              <p class="agent-description">{{ agent.description || '暂无描述' }}</p>
              <div class="agent-model">
                <el-tag size="small" type="info">{{ getModelName(agent.modelId) }}</el-tag>
              </div>
            </div>
            <div class="agent-actions">
              <el-button size="small" @click="editAgent(agent)">
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-button :type="agent.isActive ? 'warning' : 'success'" size="small" @click="toggleAgent(agent)">
                <el-icon><Switch /></el-icon>
                {{ agent.isActive ? '禁用' : '启用' }}
              </el-button>
              <el-button type="danger" size="small" @click="confirmDelete(agent)">
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </div>
          </div>
        </div>
      </el-main>
    </el-container>
    
    <el-dialog v-model="showCreateDialog" :title="editingAgent ? '编辑智能体' : '添加智能体'" width="700px">
      <el-form :model="currentAgent" label-width="100px">
        <el-form-item label="智能体名称">
          <el-input v-model="currentAgent.name" placeholder="如：剧情策划师、角色塑造专家" />
        </el-form-item>
        <el-form-item label="角色定位">
          <el-select v-model="currentAgent.role" placeholder="请选择角色定位">
            <el-option label="剧情策划" value="剧情策划" />
            <el-option label="角色塑造" value="角色塑造" />
            <el-option label="对话生成" value="对话生成" />
            <el-option label="世界观构建" value="世界观构建" />
            <el-option label="文字润色" value="文字润色" />
            <el-option label="创意顾问" value="创意顾问" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="currentAgent.description" type="textarea" :rows="2" placeholder="请描述智能体的功能" />
        </el-form-item>
        <el-form-item label="系统提示词">
          <el-input v-model="currentAgent.systemPrompt" type="textarea" :rows="4" placeholder="请输入系统提示词，定义智能体的基本行为和角色" />
        </el-form-item>
        <el-form-item label="指令">
          <el-input v-model="currentAgent.instructions" type="textarea" :rows="4" placeholder="请输入具体指令，指导智能体如何完成任务" />
        </el-form-item>
        <el-form-item label="绑定模型">
          <el-select v-model="currentAgent.modelId" placeholder="请选择AI模型">
            <el-option v-for="model in models" :key="model.id" :label="model.name" :value="model.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSaveAgent" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { agentApi, aiModelApi } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Switch, Loading } from '@element-plus/icons-vue'

const agents = ref([])
const models = ref([])
const loading = ref(false)
const saving = ref(false)
const showCreateDialog = ref(false)
const editingAgent = ref(null)
const currentAgent = ref({
  name: '',
  role: '',
  description: '',
  systemPrompt: '',
  instructions: '',
  modelId: null,
  isActive: true
})

onMounted(async () => {
  await Promise.all([loadAgents(), loadModels()])
})

async function loadAgents() {
  loading.value = true
  try {
    agents.value = await agentApi.getAll()
  } catch (err) {
    ElMessage.error('加载智能体列表失败')
  } finally {
    loading.value = false
  }
}

async function loadModels() {
  try {
    models.value = await aiModelApi.getActive()
  } catch (err) {
    console.error('加载AI模型列表失败', err)
  }
}

function getAgentIcon(role) {
  const icons = {
    '剧情策划': '📝',
    '角色塑造': '🎭',
    '对话生成': '💬',
    '世界观构建': '🌍',
    '文字润色': '✨',
    '创意顾问': '💡'
  }
  return icons[role] || '🤖'
}

function getModelName(modelId) {
  const model = models.value.find(m => m.id === modelId)
  return model ? model.name : '未绑定模型'
}

function editAgent(agent) {
  editingAgent.value = agent
  currentAgent.value = { ...agent }
  showCreateDialog.value = true
}

async function handleSaveAgent() {
  if (!currentAgent.value.name.trim()) {
    ElMessage.warning('请输入智能体名称')
    return
  }
  
  saving.value = true
  try {
    if (editingAgent.value) {
      await agentApi.update(editingAgent.value.id, currentAgent.value)
      ElMessage.success('更新成功')
    } else {
      await agentApi.create(currentAgent.value)
      ElMessage.success('添加成功')
    }
    showCreateDialog.value = false
    resetForm()
    await loadAgents()
  } catch (err) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

async function toggleAgent(agent) {
  try {
    await agentApi.toggle(agent.id)
    ElMessage.success(agent.isActive ? '已禁用' : '已启用')
    await loadAgents()
  } catch (err) {
    ElMessage.error('操作失败')
  }
}

function confirmDelete(agent) {
  ElMessageBox.confirm(
    `确定要删除智能体"${agent.name}"吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await agentApi.delete(agent.id)
      ElMessage.success('删除成功')
      await loadAgents()
    } catch (err) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

function resetForm() {
  editingAgent.value = null
  currentAgent.value = {
    name: '',
    role: '',
    description: '',
    systemPrompt: '',
    instructions: '',
    modelId: null,
    isActive: true
  }
}
</script>

<style scoped>
.agent-config {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.el-header {
  background-color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
}

.header-content {
  max-width: 1200px;
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

.nav-item:hover,
.nav-item.active {
  color: #667eea;
}

.page-header {
  max-width: 1200px;
  margin: 0 auto 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-header h2 {
  margin: 0;
  color: #333;
}

.loading-container,
.empty-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
}

.loading-container {
  color: #667eea;
}

.agents-grid {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
  padding: 0 20px 40px;
}

.agent-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.agent-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.agent-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.agent-icon {
  font-size: 40px;
}

.agent-info {
  margin-bottom: 20px;
}

.agent-info h3 {
  margin: 0 0 10px;
  color: #333;
  font-size: 18px;
}

.agent-role {
  margin: 5px 0;
  color: #667eea;
  font-size: 14px;
  font-weight: 500;
}

.agent-description {
  margin: 10px 0;
  color: #666;
  font-size: 14px;
  height: 40px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.agent-model {
  margin-top: 10px;
}

.agent-actions {
  display: flex;
  gap: 10px;
}

.agent-actions .el-button {
  flex: 1;
}
</style>