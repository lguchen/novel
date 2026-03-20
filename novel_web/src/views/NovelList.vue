<template>
  <div class="novel-list">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1 class="logo">📚 AI 小说创作助手</h1>
          <nav class="nav-menu">
            <router-link to="/" class="nav-item">首页</router-link>
            <router-link to="/novels" class="nav-item active">小说管理</router-link>
            <router-link to="/ai-models" class="nav-item">AI模型配置</router-link>
            <router-link to="/agents" class="nav-item">智能体配置</router-link>
          </nav>
        </div>
      </el-header>
      
      <el-main>
        <div class="page-header">
          <h2>我的小说</h2>
          <el-button type="primary" @click="showCreateDialog = true">
            <el-icon><Plus /></el-icon>
            创建新小说
          </el-button>
        </div>
        
        <div v-if="loading" class="loading-container">
          <el-icon class="is-loading"><Loading /></el-icon>
          <span>加载中...</span>
        </div>
        
        <div v-else-if="error" class="error-container">
          <el-icon><Warning /></el-icon>
          <span>{{ error }}</span>
        </div>
        
        <div v-else-if="novels.length === 0" class="empty-container">
          <el-empty description="暂无小说，点击上方按钮创建新小说">
            <el-button type="primary" @click="showCreateDialog = true">创建小说</el-button>
          </el-empty>
        </div>
        
        <div v-else class="novels-grid">
          <div v-for="novel in novels" :key="novel.id" class="novel-card" @click="viewNovel(novel)">
            <div class="novel-cover">
              <div class="novel-icon">📖</div>
            </div>
            <div class="novel-info">
              <h3 class="novel-title">{{ novel.title }}</h3>
              <p class="novel-description">{{ novel.description || '暂无描述' }}</p>
              <div class="novel-meta">
                <el-tag size="small">{{ novel.genre || '未分类' }}</el-tag>
                <span class="word-count">{{ novel.wordCount }} 字</span>
              </div>
              <div class="novel-actions">
                <el-button type="primary" size="small" @click.stop="editNovel(novel)">
                  <el-icon><Edit /></el-icon>
                  编辑
                </el-button>
                <el-button type="success" size="small" @click.stop="startConversation(novel)">
                  <el-icon><ChatDotRound /></el-icon>
                  对话创作
                </el-button>
                <el-button type="danger" size="small" @click.stop="confirmDelete(novel)">
                  <el-icon><Delete /></el-icon>
                  删除
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </el-main>
    </el-container>
    
    <el-dialog v-model="showCreateDialog" title="创建新小说" width="500px">
      <el-form :model="newNovel" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="newNovel.title" placeholder="请输入小说标题" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="newNovel.genre" placeholder="请选择小说类型">
            <el-option label="玄幻" value="玄幻" />
            <el-option label="都市" value="都市" />
            <el-option label="科幻" value="科幻" />
            <el-option label="武侠" value="武侠" />
            <el-option label="言情" value="言情" />
            <el-option label="历史" value="历史" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="newNovel.description" type="textarea" :rows="4" placeholder="请输入小说描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="handleCreateNovel" :loading="creating">创建</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useNovelStore } from '../stores/novel'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, ChatDotRound, Loading, Warning } from '@element-plus/icons-vue'

const router = useRouter()
const novelStore = useNovelStore()

const novels = ref([])
const loading = ref(false)
const error = ref(null)
const showCreateDialog = ref(false)
const creating = ref(false)
const newNovel = ref({
  title: '',
  genre: '',
  description: ''
})

onMounted(async () => {
  await loadNovels()
})

async function loadNovels() {
  loading.value = true
  error.value = null
  try {
    await novelStore.fetchNovels()
    novels.value = novelStore.novels
  } catch (err) {
    error.value = err.message
    ElMessage.error('加载小说列表失败')
  } finally {
    loading.value = false
  }
}

async function handleCreateNovel() {
  if (!newNovel.value.title.trim()) {
    ElMessage.warning('请输入小说标题')
    return
  }
  
  creating.value = true
  try {
    const createdNovel = await novelStore.createNovel(newNovel.value)
    if (createdNovel) {
      ElMessage.success('小说创建成功')
      showCreateDialog.value = false
      newNovel.value = { title: '', genre: '', description: '' }
      await loadNovels()
    }
  } catch (err) {
    ElMessage.error('创建小说失败')
  } finally {
    creating.value = false
  }
}

function viewNovel(novel) {
  router.push(`/novels/${novel.id}/edit`)
}

function editNovel(novel) {
  router.push(`/novels/${novel.id}/edit`)
}

function startConversation(novel) {
  router.push(`/conversation/${novel.id}`)
}

function confirmDelete(novel) {
  ElMessageBox.confirm(
    `确定要删除小说《${novel.title}》吗？此操作不可恢复。`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    const success = await novelStore.deleteNovel(novel.id)
    if (success) {
      ElMessage.success('删除成功')
      await loadNovels()
    } else {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}
</script>

<style scoped>
.novel-list {
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
.error-container,
.empty-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 60px 20px;
}

.loading-container {
  color: #667eea;
}

.error-container {
  color: #f56c6c;
}

.novels-grid {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  padding: 0 20px 40px;
}

.novel-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
}

.novel-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.novel-cover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.novel-icon {
  font-size: 48px;
}

.novel-info {
  padding: 20px;
}

.novel-title {
  font-size: 18px;
  margin: 0 0 10px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.novel-description {
  font-size: 14px;
  color: #666;
  margin: 0 0 15px;
  height: 40px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.novel-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.word-count {
  font-size: 12px;
  color: #999;
}

.novel-actions {
  display: flex;
  gap: 10px;
}

.novel-actions .el-button {
  flex: 1;
}
</style>