<template>
  <div class="home">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1 class="logo">📚 AI 小说创作助手</h1>
          <nav class="nav-menu">
            <router-link to="/" class="nav-item">首页</router-link>
            <router-link to="/novels" class="nav-item">小说管理</router-link>
            <router-link to="/prompts" class="nav-item">提示词广场</router-link>
            <router-link to="/ai-models" class="nav-item">AI模型配置</router-link>
            <router-link to="/agents" class="nav-item">智能体配置</router-link>
          </nav>
        </div>
      </el-header>
      
      <el-main>
        <div class="hero-section">
          <div class="hero-content">
            <h2 class="hero-title">让 AI 成为你创作路上的最佳伙伴</h2>
            <p class="hero-subtitle">基于 AI 大语言模型的小说创作智能体，帮你新建和修改文件，创作角色、设定、章节等内容</p>
            <div class="hero-buttons">
              <el-button type="primary" size="large" @click="goToNovels">
                <el-icon><Document /></el-icon>
                开始创作
              </el-button>
              <el-button size="large" @click="goToAIModels">
                <el-icon><Setting /></el-icon>
                配置AI模型
              </el-button>
            </div>
          </div>
          
          <div class="features-grid">
            <div class="feature-card">
              <div class="feature-icon">🎭</div>
              <h3>角色与设定规划</h3>
              <p>可视化搭建角色档案、世界观、剧情大纲，AI精准还原创作灵感</p>
            </div>
            
            <div class="feature-card">
              <div class="feature-icon">🤖</div>
              <h3>自定义技能与智能体</h3>
              <p>沉淀创作经验，搭建专属创作智能体，适配个性化创作工作流</p>
            </div>
            
            <div class="feature-card">
              <div class="feature-icon">💬</div>
              <h3>对话即创作</h3>
              <p>通过自然对话指令，调用大模型完成内容生成、角色完善、剧情续写等操作</p>
            </div>
            
            <div class="feature-card">
              <div class="feature-icon">🧠</div>
              <h3>智能记忆管理</h3>
              <p>自动上下文记忆，确保小说内容保持风格和逻辑的一致性</p>
            </div>
            
            <div class="feature-card">
              <div class="feature-icon">🌐</div>
              <h3>多模型支持</h3>
              <p>支持 DeepSeek、千问、OpenAI 等多种大语言模型，灵活切换使用</p>
            </div>
            
            <div class="feature-card">
              <div class="feature-icon">⚡</div>
              <h3>高效创作</h3>
              <p>全自动化创作，AI协同修改，让创作更轻松、更自由、更高效</p>
            </div>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { Document, Setting } from '@element-plus/icons-vue'

const router = useRouter()

function goToNovels() {
  router.push('/novels')
}

function goToAIModels() {
  router.push('/ai-models')
}
</script>

<style scoped>
.home {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.el-header {
  background-color: rgba(255, 255, 255, 0.95);
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

.nav-item:hover {
  color: #667eea;
}

.hero-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 60px 20px;
}

.hero-content {
  text-align: center;
  color: white;
  margin-bottom: 60px;
}

.hero-title {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 20px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
}

.hero-subtitle {
  font-size: 20px;
  margin-bottom: 40px;
  opacity: 0.9;
}

.hero-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 30px;
}

.feature-card {
  background: white;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
}

.feature-icon {
  font-size: 48px;
  margin-bottom: 15px;
}

.feature-card h3 {
  font-size: 20px;
  margin-bottom: 10px;
  color: #333;
}

.feature-card p {
  color: #666;
  line-height: 1.6;
}
</style>