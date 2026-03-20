import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { novelApi, characterApi, worldSettingApi } from '../api'

export const useNovelStore = defineStore('novel', () => {
  const novels = ref([])
  const currentNovel = ref(null)
  const loading = ref(false)
  const error = ref(null)

  const activeNovel = computed(() => currentNovel.value)

  async function fetchNovels() {
    loading.value = true
    error.value = null
    try {
      novels.value = await novelApi.getAll()
    } catch (err) {
      error.value = err.message
    } finally {
      loading.value = false
    }
  }

  async function fetchNovelById(id) {
    loading.value = true
    error.value = null
    try {
      currentNovel.value = await novelApi.getById(id)
      return currentNovel.value
    } catch (err) {
      error.value = err.message
      return null
    } finally {
      loading.value = false
    }
  }

  async function createNovel(novelData) {
    loading.value = true
    error.value = null
    try {
      const newNovel = await novelApi.create(novelData)
      novels.value.push(newNovel)
      return newNovel
    } catch (err) {
      error.value = err.message
      return null
    } finally {
      loading.value = false
    }
  }

  async function updateNovel(id, novelData) {
    loading.value = true
    error.value = null
    try {
      const updatedNovel = await novelApi.update(id, novelData)
      const index = novels.value.findIndex(n => n.id === id)
      if (index !== -1) {
        novels.value[index] = updatedNovel
      }
      if (currentNovel.value && currentNovel.value.id === id) {
        currentNovel.value = updatedNovel
      }
      return updatedNovel
    } catch (err) {
      error.value = err.message
      return null
    } finally {
      loading.value = false
    }
  }

  async function deleteNovel(id) {
    loading.value = true
    error.value = null
    try {
      await novelApi.delete(id)
      novels.value = novels.value.filter(n => n.id !== id)
      if (currentNovel.value && currentNovel.value.id === id) {
        currentNovel.value = null
      }
      return true
    } catch (err) {
      error.value = err.message
      return false
    } finally {
      loading.value = false
    }
  }

  async function appendContent(id, content) {
    loading.value = true
    error.value = null
    try {
      const updatedNovel = await novelApi.appendContent(id, content)
      if (currentNovel.value && currentNovel.value.id === id) {
        currentNovel.value = updatedNovel
      }
      return updatedNovel
    } catch (err) {
      error.value = err.message
      return null
    } finally {
      loading.value = false
    }
  }

  function setCurrentNovel(novel) {
    currentNovel.value = novel
  }

  function clearCurrentNovel() {
    currentNovel.value = null
  }

  return {
    novels,
    currentNovel,
    activeNovel,
    loading,
    error,
    fetchNovels,
    fetchNovelById,
    createNovel,
    updateNovel,
    deleteNovel,
    appendContent,
    setCurrentNovel,
    clearCurrentNovel
  }
})