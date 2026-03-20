<template>
  <div class="novel-edit">
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
        <div v-if="loading" class="loading-container">
          <el-icon class="is-loading"><Loading /></el-icon>
          <span>加载中...</span>
        </div>
        
        <div v-else-if="!novel" class="error-container">
          <el-icon><Warning /></el-icon>
          <span>小说不存在</span>
        </div>
        
        <div v-else class="edit-container">
          <div class="edit-header">
            <el-button @click="goBack">
              <el-icon><ArrowLeft /></el-icon>
              返回
            </el-button>
            <div class="header-actions">
              <el-button @click="showCharacters = true">
                <el-icon><User /></el-icon>
                角色管理
              </el-button>
              <el-button @click="showWorldSettings = true">
                <el-icon><Grid /></el-icon>
                世界观设定
              </el-button>
              <el-button type="primary" @click="startConversation">
                <el-icon><ChatDotRound /></el-icon>
                对话创作
              </el-button>
            </div>
          </div>
          
          <el-form :model="novel" label-width="80px" class="novel-form">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="标题">
                  <el-input v-model="novel.title" placeholder="请输入小说标题" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="类型">
                  <el-select v-model="novel.genre" placeholder="请选择小说类型">
                    <el-option label="玄幻" value="玄幻" />
                    <el-option label="都市" value="都市" />
                    <el-option label="科幻" value="科幻" />
                    <el-option label="武侠" value="武侠" />
                    <el-option label="言情" value="言情" />
                    <el-option label="历史" value="历史" />
                    <el-option label="其他" value="其他" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-form-item label="描述">
              <el-input v-model="novel.description" type="textarea" :rows="3" placeholder="请输入小说描述" />
            </el-form-item>
            
            <el-form-item label="写作风格">
              <el-input v-model="novel.styleGuide" type="textarea" :rows="4" placeholder="请输入写作风格指南，如：语言风格、叙事方式、节奏控制等" />
            </el-form-item>
          </el-form>
          
          <div class="content-section">
            <div class="section-header">
              <h3>小说内容</h3>
              <div class="content-stats">
                <span>{{ novel.wordCount }} 字</span>
              </div>
            </div>
            <el-input
              v-model="novel.content"
              type="textarea"
              :rows="20"
              placeholder="开始创作你的小说..."
              class="content-editor"
            />
          </div>
          
          <div class="save-actions">
            <el-button type="primary" @click="saveNovel" :loading="saving">
              <el-icon><Check /></el-icon>
              保存
            </el-button>
          </div>
        </div>
      </el-main>
    </el-container>
    
    <el-drawer v-model="showCharacters" title="角色管理" size="40%">
      <CharacterManage :novel-id="novelId" />
    </el-drawer>
    
    <el-drawer v-model="showWorldSettings" title="世界观设定" size="40%">
      <WorldSettingManage :novel-id="novelId" />
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useNovelStore } from '../stores/novel'
import { ElMessage } from 'element-plus'
import { ArrowLeft, User, Grid, ChatDotRound, Check, Loading, Warning } from '@element-plus/icons-vue'
import CharacterManage from './CharacterManage.vue'
import WorldSettingManage from './WorldSettingManage.vue'

const router = useRouter()
const route = useRoute()
const novelStore = useNovelStore()

const novelId = computed(() => parseInt(route.params.id))
const novel = ref(null)
const loading = ref(false)
const saving = ref(false)
const showCharacters = ref(false)
const showWorldSettings = ref(false)

onMounted(async () => {
  await loadNovel()
})

async function loadNovel() {
  loading.value = true
  try {
    novel.value = await novelStore.fetchNovelById(novelId.value)
  } catch (err) {
    ElMessage.error('加载小说失败')
  } finally {
    loading.value = false
  }
}

async function saveNovel() {
  saving.value = true
  try {
    const updatedNovel = await novelStore.updateNovel(novelId.value, novel.value)
    if (updatedNovel) {
      ElMessage.success('保存成功')
    }
  } catch (err) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

function goBack() {
  router.push('/novels')
}

function startConversation() {
  router.push(`/conversation/${novelId.value}`)
}
</script>

<style scoped>
.novel-edit {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.el-header {
  background-color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
}

.header-content {
  max-width: 1400px;
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

.loading-container,
.error-container {
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

.edit-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.edit-header {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-actions {
  display: flex;
  gap: 10px;
}

.novel-form {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.content-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.section-header h3 {
  margin: 0;
  color: #333;
}

.content-stats {
  color: #999;
  font-size: 14px;
}

.content-editor {
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.8;
}

.save-actions {
  display: flex;
  justify-content: center;
  padding: 20px;
}
</style>