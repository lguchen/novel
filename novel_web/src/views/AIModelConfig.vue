<template>
  <div class="ai-model-config">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1 class="logo">📚 AI 小说创作助手</h1>
          <nav class="nav-menu">
            <router-link to="/" class="nav-item">首页</router-link>
            <router-link to="/novels" class="nav-item">小说管理</router-link>
            <router-link to="/ai-models" class="nav-item active">AI模型配置</router-link>
            <router-link to="/agents" class="nav-item">智能体配置</router-link>
          </nav>
        </div>
      </el-header>
      
      <el-main>
        <div class="page-header">
          <h2>AI 模型配置</h2>
          <el-button type="primary" @click="showCreateDialog = true">
            <el-icon><Plus /></el-icon>
            添加模型
          </el-button>
        </div>
        
        <div v-if="loading" class="loading-container">
          <el-icon class="is-loading"><Loading /></el-icon>
          <span>加载中...</span>
        </div>
        
        <div v-else-if="models.length === 0" class="empty-container">
          <el-empty description="暂无AI模型配置">
            <el-button type="primary" @click="showCreateDialog = true">添加模型</el-button>
          </el-empty>
        </div>
        
        <div v-else class="models-grid">
          <div v-for="model in models" :key="model.id" class="model-card">
            <div class="model-header">
              <div class="model-icon">{{ getModelIcon(model.provider) }}</div>
              <el-tag :type="model.isActive ? 'success' : 'info'" size="small">
                {{ model.isActive ? '已启用' : '已禁用' }}
              </el-tag>
            </div>
            <div class="model-info">
              <h3>{{ model.name }}</h3>
              <p class="model-provider">{{ model.provider }} - {{ model.modelType }}</p>
              <p class="model-id">模型: {{ model.modelId }}</p>
              <div class="model-params">
                <span>最大Token: {{ model.maxTokens }}</span>
                <span>温度: {{ model.temperature }}</span>
              </div>
            </div>
            <div class="model-actions">
              <el-button size="small" @click="editModel(model)">
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-button :type="model.isActive ? 'warning' : 'success'" size="small" @click="toggleModel(model)">
                <el-icon><Switch /></el-icon>
                {{ model.isActive ? '禁用' : '启用' }}
              </el-button>
              <el-button type="danger" size="small" @click="confirmDelete(model)">
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </div>
          </div>
        </div>
      </el-main>
    </el-container>
    
    <el-dialog v-model="showCreateDialog" :title="editingModel ? '编辑模型' : '添加模型'" width="600px">
      <el-form :model="currentModel" label-width="100px">
        <el-form-item label="模型名称">
          <el-input v-model="currentModel.name" placeholder="如：GPT-4、DeepSeek-V3" />
        </el-form-item>
        <el-form-item label="提供商">
          <el-select v-model="currentModel.provider" placeholder="请选择提供商">
            <el-option label="OpenAI" value="openai" />
            <el-option label="Anthropic" value="anthropic" />
            <el-option label="DeepSeek" value="deepseek" />
            <el-option label="千问" value="qwen" />
          </el-select>
        </el-form-item>
        <el-form-item label="模型类型">
          <el-input v-model="currentModel.modelType" placeholder="如：chat、completion" />
        </el-form-item>
        <el-form-item label="模型ID">
          <el-input v-model="currentModel.modelId" placeholder="如：gpt-4、deepseek-chat" />
        </el-form-item>
        <el-form-item label="API地址">
          <el-input v-model="currentModel.apiUrl" placeholder="请输入API地址" />
        </el-form-item>
        <el-form-item label="API密钥">
          <el-input v-model="currentModel.apiKey" type="password" placeholder="请输入API密钥" show-password />
        </el-form-item>
        <el-form-item label="最大Token">
          <el-input-number v-model="currentModel.maxTokens" :min="100" :max="8000" />
        </el-form-item>
        <el-form-item label="温度">
          <el-slider v-model="currentModel.temperature" :min="0" :max="2" :step="0.1" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSaveModel" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { aiModelApi } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Switch, Loading } from '@element-plus/icons-vue'

const models = ref([])
const loading = ref(false)
const saving = ref(false)
const showCreateDialog = ref(false)
const editingModel = ref(null)
const currentModel = ref({
  name: '',
  provider: '',
  modelType: '',
  modelId: '',
  apiUrl: '',
  apiKey: '',
  maxTokens: 2000,
  temperature: 0.7,
  isActive: true
})

onMounted(async () => {
  await loadModels()
})

async function loadModels() {
  loading.value = true
  try {
    models.value = await aiModelApi.getAll()
  } catch (err) {
    ElMessage.error('加载AI模型列表失败')
  } finally {
    loading.value = false
  }
}

function getModelIcon(provider) {
  const icons = {
    openai: '🤖',
    anthropic: '🧠',
    deepseek: '🔮',
    qwen: '💫'
  }
  return icons[provider] || '🎯'
}

function editModel(model) {
  editingModel.value = model
  currentModel.value = { ...model }
  showCreateDialog.value = true
}

async function handleSaveModel() {
  if (!currentModel.value.name.trim() || !currentModel.value.provider || !currentModel.value.modelId) {
    ElMessage.warning('请填写必要信息')
    return
  }
  
  saving.value = true
  try {
    if (editingModel.value) {
      await aiModelApi.update(editingModel.value.id, currentModel.value)
      ElMessage.success('更新成功')
    } else {
      await aiModelApi.create(currentModel.value)
      ElMessage.success('添加成功')
    }
    showCreateDialog.value = false
    resetForm()
    await loadModels()
  } catch (err) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

async function toggleModel(model) {
  try {
    await aiModelApi.toggle(model.id)
    ElMessage.success(model.isActive ? '已禁用' : '已启用')
    await loadModels()
  } catch (err) {
    ElMessage.error('操作失败')
  }
}

function confirmDelete(model) {
  ElMessageBox.confirm(
    `确定要删除模型"${model.name}"吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await aiModelApi.delete(model.id)
      ElMessage.success('删除成功')
      await loadModels()
    } catch (err) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

function resetForm() {
  editingModel.value = null
  currentModel.value = {
    name: '',
    provider: '',
    modelType: '',
    modelId: '',
    apiUrl: '',
    apiKey: '',
    maxTokens: 2000,
    temperature: 0.7,
    isActive: true
  }
}
</script>

<style scoped>
.ai-model-config {
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

.models-grid {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
  padding: 0 20px 40px;
}

.model-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.model-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.model-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.model-icon {
  font-size: 40px;
}

.model-info {
  margin-bottom: 20px;
}

.model-info h3 {
  margin: 0 0 10px;
  color: #333;
  font-size: 18px;
}

.model-provider,
.model-id {
  margin: 5px 0;
  color: #666;
  font-size: 14px;
}

.model-params {
  display: flex;
  gap: 15px;
  margin-top: 10px;
  font-size: 13px;
  color: #999;
}

.model-actions {
  display: flex;
  gap: 10px;
}

.model-actions .el-button {
  flex: 1;
}
</style>