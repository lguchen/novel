import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import NovelList from '../views/NovelList.vue'
import NovelEdit from '../views/NovelEdit.vue'
import CharacterManage from '../views/CharacterManage.vue'
import WorldSettingManage from '../views/WorldSettingManage.vue'
import AIModelConfig from '../views/AIModelConfig.vue'
import AgentConfig from '../views/AgentConfig.vue'
import Conversation from '../views/Conversation.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/novels',
    name: 'NovelList',
    component: NovelList
  },
  {
    path: '/novels/:id/edit',
    name: 'NovelEdit',
    component: NovelEdit
  },
  {
    path: '/novels/:id/characters',
    name: 'CharacterManage',
    component: CharacterManage
  },
  {
    path: '/novels/:id/world-settings',
    name: 'WorldSettingManage',
    component: WorldSettingManage
  },
  {
    path: '/ai-models',
    name: 'AIModelConfig',
    component: AIModelConfig
  },
  {
    path: '/agents',
    name: 'AgentConfig',
    component: AgentConfig
  },
  {
    path: '/conversation/:id',
    name: 'Conversation',
    component: Conversation
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router