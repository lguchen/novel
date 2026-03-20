<template>
  <div class="character-manage">
    <div class="manage-header">
      <h3>角色管理</h3>
      <el-button type="primary" size="small" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        添加角色
      </el-button>
    </div>
    
    <div v-if="loading" class="loading-container">
      <el-icon class="is-loading"><Loading /></el-icon>
      <span>加载中...</span>
    </div>
    
    <div v-else-if="characters.length === 0" class="empty-container">
      <el-empty description="暂无角色" />
    </div>
    
    <div v-else class="characters-list">
      <div v-for="character in characters" :key="character.id" class="character-card">
        <div class="character-avatar">
          <div class="avatar-icon">👤</div>
        </div>
        <div class="character-details">
          <div class="character-name">
            <h4>{{ character.name }}</h4>
            <el-tag size="small">{{ character.role || '未设定' }}</el-tag>
          </div>
          <div class="character-info">
            <p v-if="character.personality"><strong>性格:</strong> {{ character.personality }}</p>
            <p v-if="character.background"><strong>背景:</strong> {{ character.background }}</p>
            <p v-if="character.appearance"><strong>外貌:</strong> {{ character.appearance }}</p>
            <p v-if="character.relationships"><strong>关系:</strong> {{ character.relationships }}</p>
          </div>
          <div class="character-actions">
            <el-button size="small" @click="editCharacter(character)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="confirmDelete(character)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </div>
        </div>
      </div>
    </div>
    
    <el-dialog v-model="showCreateDialog" :title="editingCharacter ? '编辑角色' : '添加角色'" width="600px">
      <el-form :model="currentCharacter" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="currentCharacter.name" placeholder="请输入角色姓名" />
        </el-form-item>
        <el-form-item label="角色定位">
          <el-input v-model="currentCharacter.role" placeholder="如：主角、配角、反派等" />
        </el-form-item>
        <el-form-item label="性格特点">
          <el-input v-model="currentCharacter.personality" type="textarea" :rows="3" placeholder="请描述角色的性格特点" />
        </el-form-item>
        <el-form-item label="背景故事">
          <el-input v-model="currentCharacter.background" type="textarea" :rows="4" placeholder="请描述角色的背景故事" />
        </el-form-item>
        <el-form-item label="外貌特征">
          <el-input v-model="currentCharacter.appearance" type="textarea" :rows="3" placeholder="请描述角色的外貌特征" />
        </el-form-item>
        <el-form-item label="人物关系">
          <el-input v-model="currentCharacter.relationships" type="textarea" :rows="3" placeholder="请描述角色与其他人物的关系" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSaveCharacter" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, defineProps } from 'vue'
import { characterApi } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Loading } from '@element-plus/icons-vue'

const props = defineProps({
  novelId: {
    type: Number,
    required: true
  }
})

const characters = ref([])
const loading = ref(false)
const saving = ref(false)
const showCreateDialog = ref(false)
const editingCharacter = ref(null)
const currentCharacter = ref({
  name: '',
  role: '',
  personality: '',
  background: '',
  appearance: '',
  relationships: '',
  novelId: props.novelId
})

onMounted(async () => {
  await loadCharacters()
})

async function loadCharacters() {
  loading.value = true
  try {
    characters.value = await characterApi.getByNovelId(props.novelId)
  } catch (err) {
    ElMessage.error('加载角色列表失败')
  } finally {
    loading.value = false
  }
}

function editCharacter(character) {
  editingCharacter.value = character
  currentCharacter.value = { ...character }
  showCreateDialog.value = true
}

async function handleSaveCharacter() {
  if (!currentCharacter.value.name.trim()) {
    ElMessage.warning('请输入角色姓名')
    return
  }
  
  saving.value = true
  try {
    if (editingCharacter.value) {
      await characterApi.update(editingCharacter.value.id, currentCharacter.value)
      ElMessage.success('更新成功')
    } else {
      await characterApi.create(currentCharacter.value)
      ElMessage.success('添加成功')
    }
    showCreateDialog.value = false
    resetForm()
    await loadCharacters()
  } catch (err) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

function confirmDelete(character) {
  ElMessageBox.confirm(
    `确定要删除角色"${character.name}"吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await characterApi.delete(character.id)
      ElMessage.success('删除成功')
      await loadCharacters()
    } catch (err) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

function resetForm() {
  editingCharacter.value = null
  currentCharacter.value = {
    name: '',
    role: '',
    personality: '',
    background: '',
    appearance: '',
    relationships: '',
    novelId: props.novelId
  }
}
</script>

<style scoped>
.character-manage {
  padding: 20px;
}

.manage-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.manage-header h3 {
  margin: 0;
  color: #333;
}

.loading-container,
.empty-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
}

.loading-container {
  color: #667eea;
}

.characters-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.character-card {
  background: white;
  border-radius: 8px;
  padding: 15px;
  display: flex;
  gap: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s;
}

.character-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.character-avatar {
  flex-shrink: 0;
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-icon {
  font-size: 30px;
}

.character-details {
  flex: 1;
  min-width: 0;
}

.character-name {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.character-name h4 {
  margin: 0;
  color: #333;
  font-size: 16px;
}

.character-info {
  margin-bottom: 10px;
}

.character-info p {
  margin: 5px 0;
  color: #666;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.character-actions {
  display: flex;
  gap: 10px;
}
</style>