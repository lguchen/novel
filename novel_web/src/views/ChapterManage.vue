<template>
  <div class="chapter-manage">
    <div class="header">
      <h2>章节管理</h2>
      <el-button type="primary" @click="createChapter">
        <el-icon><Plus /></el-icon>
        新建章节
      </el-button>
    </div>
    
    <div v-if="loading" class="loading-container">
      <el-icon class="is-loading"><Loading /></el-icon>
      <span>加载中...</span>
    </div>
    
    <div v-else class="chapter-list">
      <el-empty v-if="chapters.length === 0" description="暂无章节" />
      <el-card v-else v-for="chapter in chapters" :key="chapter.id" class="chapter-card">
        <template #header>
          <div class="chapter-header">
            <span class="chapter-number">第{{ chapter.chapterNumber }}章</span>
            <span class="chapter-title">{{ chapter.title }}</span>
            <div class="chapter-actions">
              <el-button size="small" @click="editChapter(chapter)">
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-button size="small" type="danger" @click="deleteChapter(chapter.id)">
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </div>
          </div>
        </template>
        <div class="chapter-content">
          <p class="content-preview">{{ chapter.content ? chapter.content.substring(0, 100) + '...' : '无内容' }}</p>
          <div class="chapter-stats">
            <span>{{ chapter.wordCount }} 字</span>
            <span>{{ formatDate(chapter.createdAt) }}</span>
          </div>
        </div>
      </el-card>
    </div>
    
    <!-- 章节编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑章节' : '新建章节'" width="80%">
      <el-form :model="form" label-width="80px">
        <el-form-item label="章节标题">
          <el-input v-model="form.title" placeholder="请输入章节标题" />
        </el-form-item>
        <el-form-item label="章节内容">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="15"
            placeholder="请输入章节内容"
            class="content-editor"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveChapter" :loading="saving">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Loading } from '@element-plus/icons-vue'
import { chapterApi } from '../api'

const props = defineProps({
  novelId: {
    type: Number,
    required: true
  }
})

const chapters = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const saving = ref(false)
const form = ref({
  title: '',
  content: '',
  novelId: props.novelId,
  chapterNumber: 1
})

onMounted(async () => {
  await loadChapters()
})

watch(() => props.novelId, async (newNovelId) => {
  form.value.novelId = newNovelId
  await loadChapters()
})

async function loadChapters() {
  loading.value = true
  try {
    chapters.value = await chapterApi.getAllByNovelId(props.novelId)
  } catch (err) {
    ElMessage.error('加载章节失败')
  } finally {
    loading.value = false
  }
}

async function createChapter() {
  try {
    const nextChapterNumber = await chapterApi.getNextChapterNumber(props.novelId)
    form.value = {
      title: '',
      content: '',
      novelId: props.novelId,
      chapterNumber: nextChapterNumber
    }
    isEdit = false
    dialogVisible.value = true
  } catch (err) {
    ElMessage.error('获取章节编号失败')
  }
}

function editChapter(chapter) {
  form.value = { ...chapter }
  isEdit.value = true
  dialogVisible.value = true
}

async function saveChapter() {
  if (!form.value.title) {
    ElMessage.warning('请输入章节标题')
    return
  }
  
  saving.value = true
  try {
    if (isEdit.value) {
      await chapterApi.update(form.value.id, form.value)
      ElMessage.success('章节更新成功')
    } else {
      await chapterApi.create(form.value)
      ElMessage.success('章节创建成功')
    }
    dialogVisible.value = false
    await loadChapters()
  } catch (err) {
    ElMessage.error(isEdit.value ? '章节更新失败' : '章节创建失败')
  } finally {
    saving.value = false
  }
}

async function deleteChapter(id) {
  try {
    await ElMessageBox.confirm('确定要删除该章节吗？', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await chapterApi.delete(id)
    ElMessage.success('章节删除成功')
    await loadChapters()
  } catch (err) {
    if (err !== 'cancel') {
      ElMessage.error('章节删除失败')
    }
  }
}

function formatDate(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString()
}
</script>

<style scoped>
.chapter-manage {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h2 {
  margin: 0;
  color: #333;
}

.loading-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 60px 20px;
  color: #667eea;
}

.chapter-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 20px;
}

.chapter-card {
  transition: all 0.3s;
}

.chapter-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.chapter-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.chapter-number {
  font-weight: bold;
  color: #667eea;
  margin-right: 10px;
}

.chapter-title {
  flex: 1;
  font-size: 16px;
  font-weight: 500;
}

.chapter-actions {
  display: flex;
  gap: 10px;
}

.chapter-content {
  margin-top: 10px;
}

.content-preview {
  margin: 0 0 10px 0;
  color: #666;
  line-height: 1.5;
}

.chapter-stats {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: #999;
}

.content-editor {
  font-family: 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.8;
}

.dialog-footer {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>