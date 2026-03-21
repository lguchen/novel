# AI模型配置指南

本项目已预配置了多个主流AI模型，包括免费和付费模型。

<div align="center">
  <h2>🌐 语言切换<h2>
  <a href="../zh-CN/AI_MODEL_CONFIGURATION_GUIDE.md">中文版</a> | <a href="../en-US/AI_MODEL_CONFIGURATION_GUIDE.md">English</a>
</div>

## 预配置模型

### 1. 免费模型（无需API Key）

#### Ollama（本地部署，完全免费） 🆓

| 模型名称 | 提供商 | 模型ID | 最大Tokens | 温度 | 特点 |
|---------|--------|--------|-----------|------|------|
| Ollama Llama 3.2 | ollama | llama3.2 | 4000 | 0.7 | 完全免费，本地部署 |
| Ollama Mistral 7B | ollama | mistral | 4000 | 0.7 | 完全免费，本地部署 |
| Ollama Qwen 7B | ollama | qwen | 4000 | 0.7 | 完全免费，本地部署 |

**使用方法：**
1. 下载并安装Ollama：https://ollama.com/
2. 启动Ollama服务：`ollama serve`
3. 默认运行在 `http://localhost:11434`
4. 在网页上选择Ollama模型即可直接使用，无需配置API key

#### Groq（有免费API额度） 🆓

| 模型名称 | 提供商 | 模型ID | 最大Tokens | 温度 |
|---------|--------|--------|-----------|------|
| Groq Llama 3 70B | groq | llama3-70b-8192 | 8000 | 0.7 |
| Groq Mixtral 8x7B | groq | mixtral-8x7b-32768 | 8000 | 0.7 |

**获取API Key：**
- 访问：https://console.groq.com/
- 注册/登录，然后在"API Keys"页面创建API key
- 新用户有免费额度

**API Endpoint：** `https://api.groq.com/openai/v1/chat/completions`

#### Together AI（有免费API额度） 🆓

| 模型名称 | 提供商 | 模型ID | 最大Tokens | 温度 |
|---------|--------|--------|-----------|------|
| Together Llama 3 70B | together | togethercomputer/Llama-3-70b-chat-hf | 4000 | 0.7 |
| Together Mistral 7B | together | mistralai/Mistral-7B-Instruct-v0.2 | 4000 | 0.7 |

**获取API Key：**
- 访问：https://api.together.xyz/
- 注册/登录，然后在"API Keys"页面创建API key
- 新用户有免费额度

**API Endpoint：** `https://api.together.xyz/v1/chat/completions`

### 2. 有免费API额度的模型

#### 通义千问（阿里云，有免费额度） 🆓

| 模型名称 | 提供商 | 模型ID | 最大Tokens | 温度 |
|---------|--------|--------|-----------|------|
| 通义千问 Pro-128K | qwen | qwen-max-latest | 8000 | 0.7 |
| 通义千问 Plus | qwen | qwen-plus | 8000 | 0.7 |
| 通义千问 Turbo | qwen | qwen-turbo | 8000 | 0.7 |

**获取API Key：**
- 访问：https://dashscope.aliyuncs.com/
- 注册/登录，然后在"API-KEY管理"页面创建API key
- 新用户有免费额度

**API Endpoint：** `https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions`

#### 豆包（字节跳动，有免费额度） 🆓

| 模型名称 | 提供商 | 模型ID | 最大Tokens | 温度 |
|---------|--------|--------|-----------|------|
| 豆包 Pro | doubao | ep-20240320183046-u7p95 | 8000 | 0.7 |
| 豆包 Turbo | doubao | ep-20240320183046-u7p95 | 8000 | 0.7 |

**获取API Key：**
- 访问：https://www.bytedance.com/ark
- 注册/登录，然后在开发者控制台创建API key
- 新用户有免费额度

**API Endpoint：** `https://ark.cn-beijing.volces.com/api/v3/chat/completions`

#### DeepSeek（有免费额度） 🚀

| 模型名称 | 提供商 | 模型ID | 最大Tokens | 温度 |
|---------|--------|--------|-----------|------|
| DeepSeek V3 | deepseek | deepseek-chat | 8000 | 0.7 |
| DeepSeek Coder | deepseek | deepseek-coder | 8000 | 0.7 |

**获取API Key：**
- 访问：https://platform.deepseek.com/
- 注册/登录，然后在"API Keys"页面创建API key
- 新用户有免费额度

**API Endpoint：** `https://api.deepseek.com/v1/chat/completions`

### 3. 付费模型

#### OpenAI（付费） 🤖

| 模型名称 | 提供商 | 模型ID | 最大Tokens | 温度 |
|---------|--------|--------|-----------|------|
| GPT-4 | openai | gpt-4 | 8000 | 0.7 |
| GPT-4 Turbo | openai | gpt-4-turbo-preview | 8000 | 0.7 |
| GPT-3.5 Turbo | openai | gpt-3.5-turbo | 4000 | 0.7 |

**获取API Key：**
- 访问：https://platform.openai.com/
- 注册/登录，然后在"API Keys"页面创建API key

**API Endpoint：** `https://api.openai.com/v1/chat/completions`

#### Anthropic Claude（付费） 🧠

| 模型名称 | 提供商 | 模型ID | 最大Tokens | 温度 |
|---------|--------|--------|-----------|------|
| Claude 3 Opus | anthropic | claude-3-opus-20240229 | 8000 | 0.7 |
| Claude 3 Sonnet | anthropic | claude-3-sonnet-20240229 | 8000 | 0.7 |
| Claude 3 Haiku | anthropic | claude-3-haiku-20240307 | 4000 | 0.7 |

**获取API Key：**
- 访问：https://console.anthropic.com/
- 注册/登录，然后在"API Keys"页面创建API key

**API Endpoint：** `https://api.anthropic.com/v1/messages`

## 使用方法

### 1. 启动项目

```bash
# 启动后端（自动执行schema.sql初始化模型数据）
cd novel_serve
mvn spring-boot:run

# 启动前端
cd novel_web
npm run dev
```

### 2. 配置API Key

#### 对于免费模型（Ollama）
1. 确保Ollama已安装并运行
2. 访问前端页面：http://localhost:5173
3. 点击导航栏中的"AI模型配置"
4. 找到Ollama模型，无需配置API key即可直接使用

#### 对于其他模型
1. 访问前端页面：http://localhost:5173
2. 点击导航栏中的"AI模型配置"
3. 找到要使用的模型，点击"编辑"按钮
4. 在"API Key"字段中输入您的API key
5. 点击"保存"

### 3. 创建智能体

1. 点击导航栏中的"智能体配置"
2. 点击"添加智能体"
3. 填写智能体信息：
   - 智能体名称：例如，"情节规划师"
   - 角色：例如，"情节规划"
   - 系统提示：定义智能体的基本行为
   - 指令：具体任务指令
   - 绑定模型：选择已配置API key的模型
4. 点击"保存"

### 4. 开始创作

1. 点击"小说管理"，创建新小说
2. 完善角色设定和世界观设定
3. 点击"对话创作"开始使用AI进行创作

## 模型推荐

### 完全免费（本地部署）
- **Ollama**：完全免费，需要本地安装，适合隐私要求高的用户

### 有免费API额度
- **Groq**：新用户有免费额度，响应速度快
- **Together AI**：新用户有免费额度，多个模型可选
- **通义千问**：新用户有免费额度，中文理解能力强
- **豆包**：新用户有免费额度，性能良好
- **DeepSeek**：新用户有免费额度，性价比高

### 付费模型
- **中文小说创作**：
  - **首选**：通义千问 Pro-128K（中文理解能力强）
  - **次选**：DeepSeek V3（性价比高）

### 情节规划
- **首选**：Claude 3 Sonnet（逻辑推理能力强）
- **次选**：GPT-4（综合能力强）

### 对话生成
- **首选**：GPT-4 Turbo（响应速度快）
- **次选**：通义千问 Plus（中文自然）

## 重要注意事项

1. **API Key安全**：请妥善保管您的API key，不要与他人分享
2. **成本控制**：每个AI模型都有自己的计费规则，请监控您的使用情况
3. **模型选择**：
   - Ollama：完全免费，但需要本地部署
   - Groq/Together/通义千问/豆包/DeepSeek：有免费额度，适合测试
   - OpenAI/Claude：付费，但功能强大
4. **参数调整**：
   - 温度（Temperature）：值越高输出越随机；值越低输出越确定
   - Max Tokens：控制输出长度，根据需要调整
5. **字符集问题**：如果看到乱码，请确保数据库字符集为utf8mb4

## 故障排除

### 模型调用失败
1. 检查API key是否正确
2. 检查网络连接是否正常
3. 检查API endpoint是否正确
4. 检查后端日志获取详细错误信息
5. 对于Ollama，检查服务是否运行：`curl http://localhost:11434/api/tags`

### 数据库连接失败
1. 检查MySQL服务是否运行
2. 检查数据库用户名和密码是否正确
3. 检查数据库是否创建（novel）
4. 检查字符集是否为utf8mb4

### 字符集问题
1. 确保数据库字符集为utf8mb4
2. 确保连接字符串包含字符集参数
3. 检查表字符集设置

## 获取帮助

如果遇到问题，请检查：
1. 后端日志：控制台输出
2. 前端日志：浏览器开发者工具
3. 各AI平台的官方文档