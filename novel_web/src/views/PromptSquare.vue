<template>
  <div class="prompt-square">
    <div class="header">
      <div class="header-left">
        <el-button @click="goBack">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
        <h2>提示词广场</h2>
      </div>
      <div class="search-container">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索提示词"
          prefix-icon="Search"
          @keyup.enter="searchPrompts"
        />
        <el-button type="primary" @click="searchPrompts">搜索</el-button>
      </div>
    </div>
    
    <div class="filter-container">
      <el-select v-model="selectedCategory" placeholder="按分类筛选" @change="filterPrompts">
        <el-option label="全部" value="" />
        <el-option v-for="category in categories" :key="category" :label="category" :value="category" />
      </el-select>
      <el-select v-model="selectedTag" placeholder="按标签筛选" @change="filterPrompts">
        <el-option label="全部" value="" />
        <el-option v-for="tag in tags" :key="tag" :label="tag" :value="tag" />
      </el-select>
      <el-button type="success" @click="showPopularPrompts">
        <el-icon><Star /></el-icon>
        热门提示词
      </el-button>
    </div>
    
    <div v-if="loading" class="loading-container">
      <el-icon class="is-loading"><Loading /></el-icon>
      <span>加载中...</span>
    </div>
    
    <div v-else class="prompt-list">
      <el-empty v-if="filteredPrompts.length === 0" description="暂无提示词" />
      <el-card v-else v-for="prompt in filteredPrompts" :key="prompt.id" class="prompt-card">
        <template #header>
          <div class="prompt-header">
            <span class="prompt-category">{{ prompt.category }}</span>
            <div class="prompt-usage">
              <el-icon><View /></el-icon>
              <span>{{ prompt.usageCount }} 次使用</span>
            </div>
          </div>
        </template>
        <div class="prompt-content">
          <p>{{ prompt.content }}</p>
        </div>
        <div class="prompt-footer">
          <div class="prompt-tags">
            <el-tag v-for="tag in prompt.tags.split(',')" :key="tag" size="small" effect="plain">
              {{ tag }}
            </el-tag>
          </div>
          <div class="prompt-actions">
            <el-button size="small" @click="copyPrompt(prompt.content)">
              <el-icon><DocumentCopy /></el-icon>
              复制
            </el-button>
            <el-button size="small" type="primary" @click="usePrompt(prompt)">
              <el-icon><Check /></el-icon>
              使用
            </el-button>
          </div>
        </div>
      </el-card>
    </div>
    
    <!-- 使用提示词对话框 -->
    <el-dialog v-model="useDialogVisible" title="使用提示词" width="80%">
      <div class="use-prompt-container">
        <h3>提示词内容</h3>
        <el-input
          v-model="currentPrompt"
          type="textarea"
          :rows="5"
          readonly
        />
        <h3>选择使用场景</h3>
        <el-radio-group v-model="useScene">
          <el-radio label="novel">小说创作</el-radio>
          <el-radio label="chapter">章节创作</el-radio>
          <el-radio label="character">角色设定</el-radio>
        </el-radio-group>
        <h3 v-if="useScene === 'novel'">选择小说</h3>
        <el-select v-if="useScene === 'novel'" v-model="selectedNovel" placeholder="选择小说">
          <el-option v-for="novel in novels" :key="novel.id" :label="novel.title" :value="novel.id" />
        </el-select>
        <h3 v-if="useScene === 'chapter'">选择章节</h3>
        <el-select v-if="useScene === 'chapter'" v-model="selectedChapter" placeholder="选择章节">
          <el-option v-for="chapter in chapters" :key="chapter.id" :label="`第${chapter.chapterNumber}章 ${chapter.title}`" :value="chapter.id" />
        </el-select>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="useDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmUsePrompt">确认使用</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Star, View, DocumentCopy, Check, Loading, ArrowLeft } from '@element-plus/icons-vue'
import { promptApi, novelApi, chapterApi } from '../api'

const router = useRouter()

function goBack() {
  router.back()
}

const prompts = ref([])
const loading = ref(false)
const searchKeyword = ref('')
const selectedCategory = ref('')
const selectedTag = ref('')
const useDialogVisible = ref(false)
const currentPrompt = ref('')
const useScene = ref('novel')
const selectedNovel = ref(null)
const selectedChapter = ref(null)
const novels = ref([])
const chapters = ref([])

const categories = ref([
  '情节发展',
  '人物塑造',
  '场景描写',
  '对话设计',
  '氛围营造',
  '世界观设定',
  '开头结尾',
  '悬念设置'
])

const tags = ref([
  '玄幻',
  '都市',
  '科幻',
  '武侠',
  '言情',
  '历史',
  '悬疑',
  '恐怖',
  '喜剧',
  '悲剧'
])

const filteredPrompts = computed(() => {
  let result = [...prompts.value]
  
  if (searchKeyword.value) {
    result = result.filter(prompt => 
      prompt.content.includes(searchKeyword.value)
    )
  }
  
  if (selectedCategory.value) {
    result = result.filter(prompt => 
      prompt.category === selectedCategory.value
    )
  }
  
  if (selectedTag.value) {
    result = result.filter(prompt => 
      prompt.tags.includes(selectedTag.value)
    )
  }
  
  return result
})

onMounted(async () => {
  await loadPrompts()
  await loadNovels()
})

async function loadPrompts() {
  loading.value = true
  try {
    prompts.value = await promptApi.getAll()
  } catch (err) {
    ElMessage.error('加载提示词失败')
  } finally {
    loading.value = false
  }
}

async function loadNovels() {
  try {
    novels.value = await novelApi.getAll()
  } catch (err) {
    ElMessage.error('加载小说失败')
  }
}

async function loadChapters(novelId) {
  try {
    chapters.value = await chapterApi.getAllByNovelId(novelId)
  } catch (err) {
    ElMessage.error('加载章节失败')
  }
}

async function searchPrompts() {
  loading.value = true
  try {
    if (searchKeyword.value) {
      prompts.value = await promptApi.search(searchKeyword.value)
    } else {
      await loadPrompts()
    }
  } catch (err) {
    ElMessage.error('搜索提示词失败')
  } finally {
    loading.value = false
  }
}

async function filterPrompts() {
  loading.value = true
  try {
    if (selectedCategory.value) {
      prompts.value = await promptApi.getByCategory(selectedCategory.value)
    } else if (selectedTag.value) {
      prompts.value = await promptApi.getByTag(selectedTag.value)
    } else {
      await loadPrompts()
    }
  } catch (err) {
    ElMessage.error('筛选提示词失败')
  } finally {
    loading.value = false
  }
}

async function showPopularPrompts() {
  loading.value = true
  try {
    prompts.value = await promptApi.getPopular()
  } catch (err) {
    ElMessage.error('加载热门提示词失败')
  } finally {
    loading.value = false
  }
}

function copyPrompt(content) {
  navigator.clipboard.writeText(content)
    .then(() => {
      ElMessage.success('提示词已复制到剪贴板')
    })
    .catch(() => {
      ElMessage.error('复制失败')
    })
}

function usePrompt(prompt) {
  currentPrompt.value = prompt.content
  useDialogVisible.value = true
  // 增加使用次数
  promptApi.incrementUsageCount(prompt.id)
}

async function confirmUsePrompt() {
  if (!useScene.value) {
    ElMessage.warning('请选择使用场景')
    return
  }
  
  if (useScene.value === 'novel' && !selectedNovel.value) {
    ElMessage.warning('请选择小说')
    return
  }
  
  if (useScene.value === 'chapter' && !selectedChapter.value) {
    ElMessage.warning('请选择章节')
    return
  }
  
  // 这里可以根据使用场景，将提示词应用到相应的创作中
  // 例如，将提示词添加到小说的记忆中，或用于生成章节内容
  
  ElMessage.success('提示词使用成功')
  useDialogVisible.value = false
}
</script>

<style scoped>
.prompt-square {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-left h2 {
  margin: 0;
  color: #333;
}

.search-container {
  display: flex;
  gap: 10px;
  width: 400px;
}

.filter-container {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  align-items: center;
}

.loading-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 60px 20px;
  color: #667eea;
}

.prompt-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 20px;
}

.prompt-card {
  transition: all 0.3s;
}

.prompt-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.prompt-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.prompt-category {
  font-weight: bold;
  color: #667eea;
}

.prompt-usage {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
  color: #999;
}

.prompt-content {
  margin: 15px 0;
  line-height: 1.6;
  color: #333;
}

.prompt-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.prompt-tags {
  display: flex;
  gap: 5px;
  flex-wrap: wrap;
}

.prompt-actions {
  display: flex;
  gap: 10px;
}

.use-prompt-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.use-prompt-container h3 {
  margin: 0;
  color: #333;
  font-size: 16px;
}

.dialog-footer {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>