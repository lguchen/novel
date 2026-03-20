<template>
  <div class="world-setting-manage">
    <div class="manage-header">
      <h3>世界观设定</h3>
      <el-button type="primary" size="small" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        添加设定
      </el-button>
    </div>
    
    <div v-if="loading" class="loading-container">
      <el-icon class="is-loading"><Loading /></el-icon>
      <span>加载中...</span>
    </div>
    
    <div v-else-if="settings.length === 0" class="empty-container">
      <el-empty description="暂无世界观设定" />
    </div>
    
    <div v-else class="settings-list">
      <div v-for="setting in settings" :key="setting.id" class="setting-card">
        <div class="setting-icon">
          <div class="icon-content">🌍</div>
        </div>
        <div class="setting-details">
          <div class="setting-header">
            <h4>{{ setting.name }}</h4>
            <el-tag size="small">{{ setting.category || '未分类' }}</el-tag>
          </div>
          <div class="setting-info">
            <p v-if="setting.description"><strong>描述:</strong> {{ setting.description }}</p>
            <p v-if="setting.rules"><strong>规则:</strong> {{ setting.rules }}</p>
            <p v-if="setting.geography"><strong>地理:</strong> {{ setting.geography }}</p>
            <p v-if="setting.history"><strong>历史:</strong> {{ setting.history }}</p>
          </div>
          <div class="setting-actions">
            <el-button size="small" @click="editSetting(setting)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="confirmDelete(setting)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </div>
        </div>
      </div>
    </div>
    
    <el-dialog v-model="showCreateDialog" :title="editingSetting ? '编辑设定' : '添加设定'" width="600px">
      <el-form :model="currentSetting" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="currentSetting.name" placeholder="请输入设定名称" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="currentSetting.category" placeholder="请选择分类">
            <el-option label="地理" value="地理" />
            <el-option label="历史" value="历史" />
            <el-option label="文化" value="文化" />
            <el-option label="魔法/科技" value="魔法/科技" />
            <el-option label="社会" value="社会" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="currentSetting.description" type="textarea" :rows="3" placeholder="请描述该设定的基本情况" />
        </el-form-item>
        <el-form-item label="规则">
          <el-input v-model="currentSetting.rules" type="textarea" :rows="3" placeholder="请描述该设定的规则或限制" />
        </el-form-item>
        <el-form-item label="地理环境">
          <el-input v-model="currentSetting.geography" type="textarea" :rows="3" placeholder="请描述地理环境（如适用）" />
        </el-form-item>
        <el-form-item label="历史背景">
          <el-input v-model="currentSetting.history" type="textarea" :rows="3" placeholder="请描述历史背景（如适用）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSaveSetting" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, defineProps } from 'vue'
import { worldSettingApi } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Loading } from '@element-plus/icons-vue'

const props = defineProps({
  novelId: {
    type: Number,
    required: true
  }
})

const settings = ref([])
const loading = ref(false)
const saving = ref(false)
const showCreateDialog = ref(false)
const editingSetting = ref(null)
const currentSetting = ref({
  name: '',
  category: '',
  description: '',
  rules: '',
  geography: '',
  history: '',
  novelId: props.novelId
})

onMounted(async () => {
  await loadSettings()
})

async function loadSettings() {
  loading.value = true
  try {
    settings.value = await worldSettingApi.getByNovelId(props.novelId)
  } catch (err) {
    ElMessage.error('加载世界观设定失败')
  } finally {
    loading.value = false
  }
}

function editSetting(setting) {
  editingSetting.value = setting
  currentSetting.value = { ...setting }
  showCreateDialog.value = true
}

async function handleSaveSetting() {
  if (!currentSetting.value.name.trim()) {
    ElMessage.warning('请输入设定名称')
    return
  }
  
  saving.value = true
  try {
    if (editingSetting.value) {
      await worldSettingApi.update(editingSetting.value.id, currentSetting.value)
      ElMessage.success('更新成功')
    } else {
      await worldSettingApi.create(currentSetting.value)
      ElMessage.success('添加成功')
    }
    showCreateDialog.value = false
    resetForm()
    await loadSettings()
  } catch (err) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

function confirmDelete(setting) {
  ElMessageBox.confirm(
    `确定要删除设定"${setting.name}"吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await worldSettingApi.delete(setting.id)
      ElMessage.success('删除成功')
      await loadSettings()
    } catch (err) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

function resetForm() {
  editingSetting.value = null
  currentSetting.value = {
    name: '',
    category: '',
    description: '',
    rules: '',
    geography: '',
    history: '',
    novelId: props.novelId
  }
}
</script>

<style scoped>
.world-setting-manage {
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

.settings-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.setting-card {
  background: white;
  border-radius: 8px;
  padding: 15px;
  display: flex;
  gap: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s;
}

.setting-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.setting-icon {
  flex-shrink: 0;
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-content {
  font-size: 30px;
}

.setting-details {
  flex: 1;
  min-width: 0;
}

.setting-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.setting-header h4 {
  margin: 0;
  color: #333;
  font-size: 16px;
}

.setting-info {
  margin-bottom: 10px;
}

.setting-info p {
  margin: 5px 0;
  color: #666;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.setting-actions {
  display: flex;
  gap: 10px;
}
</style>