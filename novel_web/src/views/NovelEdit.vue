<template>
  <div class="novel-edit">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1 class="logo">📚 AI 小说创作助手</h1>
          <nav class="nav-menu">
            <router-link to="/" class="nav-item">首页</router-link>
            <router-link to="/novels" class="nav-item active">小说管理</router-link>
            <router-link to="/prompts" class="nav-item">提示词广场</router-link>
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
              <el-button @click="showChapters = true">
                <el-icon><Document /></el-icon>
                章节管理
              </el-button>
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
                <span>{{ totalWordCount }} 字</span>
                <span v-if="chapters.length > 0">{{ chapters.length }} 章</span>
              </div>
            </div>
            <div v-if="loadingChapters" class="loading-container">
              <el-icon class="is-loading"><Loading /></el-icon>
              <span>加载章节中...</span>
            </div>
            <div v-else-if="chapters.length === 0" class="empty-content">
              <el-empty description="暂无章节内容，请点击章节管理添加章节" />
            </div>
            <div v-else class="document-view">
              <div v-for="chapter in chapters" :key="chapter.id" class="chapter-content">
                <h4 class="chapter-title">第{{ chapter.chapterNumber }}章 {{ chapter.title }}</h4>
                <div class="chapter-body" v-html="formatChapterContent(chapter.content)"></div>
                <hr class="chapter-divider" />
              </div>
            </div>
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
    
    <el-drawer v-model="showChapters" title="章节管理" size="80%">
      <ChapterManage :novel-id="novelId" />
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useNovelStore } from '../stores/novel'
import { ElMessage } from 'element-plus'
import { ArrowLeft, User, Grid, ChatDotRound, Check, Loading, Warning, Document } from '@element-plus/icons-vue'
import CharacterManage from './CharacterManage.vue'
import WorldSettingManage from './WorldSettingManage.vue'
import ChapterManage from './ChapterManage.vue'
import { chapterApi, characterApi } from '../api'

const router = useRouter()
const route = useRoute()
const novelStore = useNovelStore()

const novelId = computed(() => parseInt(route.params.id))
const novel = ref(null)
const chapters = ref([])
const characters = ref([])
const loading = ref(false)
const saving = ref(false)
const loadingChapters = ref(false)
const loadingCharacters = ref(false)
const showChapters = ref(false)
const showCharacters = ref(false)
const showWorldSettings = ref(false)

onMounted(async () => {
  await loadNovel()
  await loadChapters()
  await loadCharacters()
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

async function loadChapters() {
  loadingChapters.value = true
  try {
    chapters.value = await chapterApi.getAllByNovelId(novelId.value)
    // 按章节号排序
    chapters.value.sort((a, b) => a.chapterNumber - b.chapterNumber)
  } catch (err) {
    ElMessage.error('加载章节失败')
  } finally {
    loadingChapters.value = false
  }
}

async function loadCharacters() {
  loadingCharacters.value = true
  try {
    characters.value = await characterApi.getByNovelId(novelId.value)
  } catch (err) {
    ElMessage.error('加载角色失败')
  } finally {
    loadingCharacters.value = false
  }
}

// 监听章节管理抽屉关闭事件
watch(showChapters, async (newVal) => {
  if (!newVal) {
    // 抽屉关闭时重新加载章节数据
    await loadChapters()
  }
})

// 监听角色管理抽屉关闭事件
watch(showCharacters, async (newVal) => {
  if (!newVal) {
    // 抽屉关闭时重新加载角色数据
    await loadCharacters()
  }
})

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

// 计算总字数
const totalWordCount = computed(() => {
  return chapters.value.reduce((total, chapter) => total + (chapter.wordCount || 0), 0)
})

// 格式化章节内容，将换行符转换为HTML换行标签，并高亮显示角色名字
function formatChapterContent(content) {
  if (!content) return ''
  
  let formattedContent = content.replace(/\n/g, '<br>')
  
  // 高亮显示角色名字
  characters.value.forEach(character => {
    if (character.name && character.color) {
      // 使用正则表达式匹配角色名字，确保是完整的词
      const regex = new RegExp(`\\b${character.name}\\b`, 'g')
      formattedContent = formattedContent.replace(regex, `<span style="color: ${character.color}; font-weight: bold;">${character.name}</span>`)
    }
  })
  
  return formattedContent
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

.document-view {
  background-color: #f9f9f9;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 30px;
  min-height: 400px;
  max-height: 600px;
  overflow-y: auto;
  font-family: 'SimSun', '宋体', serif;
}

.chapter-content {
  margin-bottom: 30px;
}

.chapter-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

.chapter-body {
  font-size: 16px;
  line-height: 2.0;
  color: #333;
  text-align: justify;
  margin-bottom: 20px;
}

.chapter-divider {
  border: 0;
  height: 1px;
  background: linear-gradient(to right, transparent, #ccc, transparent);
  margin: 30px 0;
}

.empty-content {
  padding: 60px 20px;
  text-align: center;
}

.save-actions {
  display: flex;
  justify-content: center;
  padding: 20px;
}
</style>