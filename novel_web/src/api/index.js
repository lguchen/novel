import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 60000
})

api.interceptors.request.use(
  config => {
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

export const novelApi = {
  getAll() {
    return api.get('/novels')
  },
  
  getById(id) {
    return api.get(`/novels/${id}`)
  },
  
  create(novel) {
    return api.post('/novels', novel)
  },
  
  update(id, novel) {
    return api.put(`/novels/${id}`, novel)
  },
  
  delete(id) {
    return api.delete(`/novels/${id}`)
  },
  
  appendContent(id, content) {
    return api.post(`/novels/${id}/content`, content, {
      headers: { 'Content-Type': 'text/plain' }
    })
  },
  
  updateMemory(id, contextMemory) {
    return api.post(`/novels/${id}/memory`, contextMemory, {
      headers: { 'Content-Type': 'text/plain' }
    })
  }
}

export const characterApi = {
  getAll() {
    return api.get('/characters')
  },
  
  getById(id) {
    return api.get(`/characters/${id}`)
  },
  
  getByNovelId(novelId) {
    return api.get(`/characters/novel/${novelId}`)
  },
  
  create(character) {
    return api.post('/characters', character)
  },
  
  update(id, character) {
    return api.put(`/characters/${id}`, character)
  },
  
  delete(id) {
    return api.delete(`/characters/${id}`)
  }
}

export const worldSettingApi = {
  getAll() {
    return api.get('/world-settings')
  },
  
  getById(id) {
    return api.get(`/world-settings/${id}`)
  },
  
  getByNovelId(novelId) {
    return api.get(`/world-settings/novel/${novelId}`)
  },
  
  create(setting) {
    return api.post('/world-settings', setting)
  },
  
  update(id, setting) {
    return api.put(`/world-settings/${id}`, setting)
  },
  
  delete(id) {
    return api.delete(`/world-settings/${id}`)
  }
}

export const aiModelApi = {
  getAll() {
    return api.get('/ai-models')
  },
  
  getActive() {
    return api.get('/ai-models/active')
  },
  
  getById(id) {
    return api.get(`/ai-models/${id}`)
  },
  
  create(model) {
    return api.post('/ai-models', model)
  },
  
  update(id, model) {
    return api.put(`/ai-models/${id}`, model)
  },
  
  delete(id) {
    return api.delete(`/ai-models/${id}`)
  },
  
  toggle(id) {
    return api.post(`/ai-models/${id}/toggle`)
  }
}

export const agentApi = {
  getAll() {
    return api.get('/agents')
  },
  
  getActive() {
    return api.get('/agents/active')
  },
  
  getById(id) {
    return api.get(`/agents/${id}`)
  },
  
  create(agent) {
    return api.post('/agents', agent)
  },
  
  update(id, agent) {
    return api.put(`/agents/${id}`, agent)
  },
  
  delete(id) {
    return api.delete(`/agents/${id}`)
  },
  
  toggle(id) {
    return api.post(`/agents/${id}/toggle`)
  }
}

export const conversationApi = {
  chat(data) {
    return api.post('/conversation/chat', data)
  },
  
  getHistory(novelId) {
    return api.get(`/conversation/history/${novelId}`)
  },
  
  clearHistory(novelIdId) {
    return api.delete(`/conversation/history/${novelId}`)
  }
}

export default api