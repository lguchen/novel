# AI小说创作助手

一个功能强大的AI辅助小说创作平台，具有记忆持久化、多AI模型集成和用户友好的界面。


<div align="center">
  <a href="../zh-CN/README.md">中文版</a> | <a href="../en-US/README.md">English</a>
</div>


## 功能特性

### 核心功能
1. **小说角色与设定规划**
   - 可视化角色档案创建
   - 世界观构建
   - 剧情大纲开发
   - AI驱动的创作灵感保存

2. **自定义技能与智能体**
   - 沉淀创作经验
   - 构建个性化创作智能体
   - 适配个人创作工作流程

3. **对话即创作**
   - 自然对话式指令
   - AI内容生成
   - 角色完善
   - 剧情续写

### 技术特性
- **记忆持久化**：防止AI在服务器重启后失去上下文
- **多AI模型支持**：集成主流AI模型，支持网页配置
- **前后端分离**：现代化Vue3前端与Java Spring Boot后端
- **本地使用**：专为个人本地使用设计，无需复杂认证

## 技术栈

### 前端
- **框架**：Vue 3
- **状态管理**：Pinia
- **路由**：Vue Router
- **构建工具**：Vite
- **样式**：CSS3

### 后端
- **框架**：Java Spring Boot
- **数据库**：MySQL
- **ORM**：JPA/Hibernate
- **API**：RESTful

### AI模型
- **免费模型**：Ollama（本地）、Groq、Together AI、通义千问、豆包、DeepSeek
- **付费模型**：OpenAI GPT、Anthropic Claude

## 快速开始

### 前置条件
- Java 11+
- Maven 3.6+
- Node.js 16+
- MySQL 5.7+
- (可选) Ollama 用于本地模型

### 安装

1. **克隆仓库**
   ```bash
   git clone https://github.com/lguchen/novel.git
   cd novel
   ```

2. **设置MySQL数据库**
   ```sql
   CREATE DATABASE novel CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **后端设置**
   ```bash
   cd novel_serve
   mvn clean install
   ```

4. **前端设置**
   ```bash
   cd novel_web
   npm install
   ```

### 运行应用

1. **启动后端**
   ```bash
   cd novel_serve
   mvn spring-boot:run
   ```

2. **启动前端**
   ```bash
   cd novel_web
   npm run dev
   ```

3. **访问应用**
   - 前端：http://localhost:5173
   - 后端API：http://localhost:8080

## 项目结构

```
novel/
├── docs/                # 文档
│   ├── en-US/           # 英文文档
│   ├── zh-CN/           # 中文文档
├── novel_serve/         # 后端
│   ├── src/main/java/   # Java源代码
│   ├── src/main/resources/ # 配置文件
├── novel_web/           # 前端
│   ├── src/             # Vue源代码
│   ├── public/          # 静态资源
├── sql/                 # SQL文件
├── start.bat            # 快速启动脚本
├── README.md            # 本文档
```

## AI模型配置

有关AI模型配置的详细信息，请参考 [AI模型配置指南](AI_MODEL_CONFIGURATION_GUIDE.md)。

## 使用指南

1. **创建新小说**
   - 点击导航栏中的"小说管理"
   - 点击"添加小说"
   - 输入小说标题和基本信息

2. **配置角色**
   - 点击"角色管理"
   - 添加详细的角色档案

3. **构建世界观**
   - 点击"世界观管理"
   - 创建和组织世界观设定

4. **配置AI模型**
   - 点击"AI模型配置"
   - 为您喜欢的模型输入API密钥

5. **创建智能体**
   - 点击"智能体配置"
   - 为不同的写作任务创建专门的智能体

6. **开始创作**
   - 点击"对话创作"
   - 使用自然对话生成内容

## 核心模块

### 后端模块
- **AIModelController**：管理AI模型配置
- **AgentController**：处理智能体创建和管理
- **CharacterController**：管理角色档案
- **ConversationController**：处理AI对话
- **NovelController**：管理小说数据
- **WorldSettingController**：管理世界观设定

### 前端页面
- **Home.vue**：仪表盘
- **NovelList.vue**：小说管理
- **NovelEdit.vue**：小说编辑
- **CharacterManage.vue**：角色管理
- **WorldSettingManage.vue**：世界观管理
- **AIModelConfig.vue**：AI模型配置
- **AgentConfig.vue**：智能体配置
- **Conversation.vue**：AI对话界面

## 故障排除

### 常见问题
1. **数据库连接错误**
   - 检查MySQL服务是否运行
   - 验证application.yml中的数据库凭证
   - 确保数据库"novel"存在

2. **AI模型调用失败**
   - 检查API密钥是否正确
   - 验证网络连接
   - 检查模型端点URL

3. **前端未加载**
   - 确保前端依赖已安装
   - 检查前端服务器是否运行
   - 验证后端API是否可访问

## 贡献

欢迎贡献！请随时提交Pull Request。

## 许可证

本项目采用MIT许可证。

## 致谢

- Vue 3 - 前端框架
- Spring Boot - 后端框架
- MySQL - 数据库
- 各种AI模型提供商的API

## 支持

如果您遇到任何问题，请查看文档或在GitHub上打开issue。