# AI Model Configuration Guide

This project has pre-configured multiple mainstream AI models, including free and paid models.


<div align="center">
### 🌐 语言切换
  <a href="../zh-CN/AI_MODEL_CONFIGURATION_GUIDE.md">中文版</a> | <a href="../en-US/AI_MODEL_CONFIGURATION_GUIDE.md">English</a>
</div>


## Pre-configured Models

### 1. Free Models (No API Key Required)

#### Ollama (Local Deployment, Completely Free) 🆓

| Model Name | Provider | Model ID | Max Tokens | Temperature | Features |
|------------|----------|-----------|-----------|-------|------|
| Ollama Llama 3.2 | ollama | llama3.2 | 4000 | 0.7 | Completely free, local deployment |
| Ollama Mistral 7B | ollama | mistral | 4000 | 0.7 | Completely free, local deployment |
| Ollama Qwen 7B | ollama | qwen | 4000 | 0.7 | Completely free, local deployment |

**How to Use:**
1. Download and install Ollama: https://ollama.com/
2. Start Ollama service: `ollama serve`
3. Default runs on `http://localhost:11434`
4. Select Ollama model on the web page to use directly, no API key configuration needed

#### Groq (Free API Quota) 🆓

| Model Name | Provider | Model ID | Max Tokens | Temperature |
|------------|----------|-----------|-----------|-------|
| Groq Llama 3 70B | groq | llama3-70b-8192 | 8000 | 0.7 |
| Groq Mixtral 8x7B | groq | mixtral-8x7b-32768 | 8000 | 0.7 |

**Get API Key:**
- Visit: https://console.groq.com/
- Register/login, then create API key in "API Keys" page
- New users have free quota

**API Endpoint:** `https://api.groq.com/openai/v1/chat/completions`

#### Together AI (Free API Quota) 🆓

| Model Name | Provider | Model ID | Max Tokens | Temperature |
|------------|----------|-----------|-----------|-------|
| Together Llama 3 70B | together | togethercomputer/Llama-3-70b-chat-hf | 4000 | 0.7 |
| Together Mistral 7B | together | mistralai/Mistral-7B-Instruct-v0.2 | 4000 | 0.7 |

**Get API Key:**
- Visit: https://api.together.xyz/
- Register/login, then create API key in "API Keys" page
- New users have free quota

**API Endpoint:** `https://api.together.xyz/v1/chat/completions`

### 2. Models with Free API Quota

#### Qwen (Tongyi Qianwen, Free API Quota) 🆓

| Model Name | Provider | Model ID | Max Tokens | Temperature |
|------------|----------|-----------|-----------|-------|
| Qwen Pro-128K | qwen | qwen-max-latest | 8000 | 0.7 |
| Qwen Plus | qwen | qwen-plus | 8000 | 0.7 |
| Qwen Turbo | qwen | qwen-turbo | 8000 | 0.7 |

**Get API Key:**
- Visit: https://dashscope.aliyuncs.com/
- Register/login, then create API key in "API-KEY Management" page
- New users have free quota

**API Endpoint:** `https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions`

#### Doubao (ByteDance, Free API Quota) 🆓

| Model Name | Provider | Model ID | Max Tokens | Temperature |
|------------|----------|-----------|-----------|-------|
| Doubao Pro | doubao | ep-20240320183046-u7p95 | 8000 | 0.7 |
| Doubao Turbo | doubao | ep-20240320183046-u7p95 | 8000 | 0.7 |

**Get API Key:**
- Visit: https://www.bytedance.com/en/ark
- Register/login, then create API key in the developer console
- New users have free quota

**API Endpoint:** `https://ark.cn-beijing.volces.com/api/v3/chat/completions`

#### DeepSeek (Free API Quota) 🚀

| Model Name | Provider | Model ID | Max Tokens | Temperature |
|------------|----------|-----------|-----------|-------|
| DeepSeek V3 | deepseek | deepseek-chat | 8000 | 0.7 |
| DeepSeek Coder | deepseek | deepseek-coder | 8000 | 0.7 |

**Get API Key:**
- Visit: https://platform.deepseek.com/
- Register/login, then create API key in "API Keys" page
- New users have free quota

**API Endpoint:** `https://api.deepseek.com/v1/chat/completions`

### 3. Paid Models

#### OpenAI (Paid) 🤖

| Model Name | Provider | Model ID | Max Tokens | Temperature |
|------------|----------|-----------|-----------|-------|
| GPT-4 | openai | gpt-4 | 8000 | 0.7 |
| GPT-4 Turbo | openai | gpt-4-turbo-preview | 8000 | 0.7 |
| GPT-3.5 Turbo | openai | gpt-3.5-turbo | 4000 | 0.7 |

**Get API Key:**
- Visit: https://platform.openai.com/
- Register/login, then create API key in "API Keys" page

**API Endpoint:** `https://api.openai.com/v1/chat/completions`

#### Anthropic Claude (Paid) 🧠

| Model Name | Provider | Model ID | Max Tokens | Temperature |
|------------|----------|-----------|-----------|-------|
| Claude 3 Opus | anthropic | claude-3-opus-20240229 | 8000 | 0.7 |
| Claude 3 Sonnet | anthropic | claude-3-sonnet-20240229 | 8000 | 0.7 |
| Claude 3 Haiku | anthropic | claude-3-haiku-20240307 | 4000 | 0.7 |

**Get API Key:**
- Visit: https://console.anthropic.com/
- Register/login, then create API key in "API Keys" page

**API Endpoint:** `https://api.anthropic.com/v1/messages`

## How to Use

### 1. Start the Project

```bash
# Start backend (automatically executes schema.sql to initialize model data)
cd novel_serve
mvn spring-boot:run

# Start frontend
cd novel_web
npm run dev
```

### 2. Configure API Keys

#### For Free Models (Ollama)
1. Ensure Ollama is installed and running
2. Visit frontend page: http://localhost:5173
3. Click "AI Model Configuration" in navigation bar
4. Find Ollama model, use directly without API key configuration

#### For Other Models
1. Visit frontend page: http://localhost:5173
2. Click "AI Model Configuration" in navigation bar
3. Find the model you want to use, click "Edit" button
4. Enter your API key in "API Key" field
5. Click "Save"

### 3. Create Agents

1. Click "Agent Configuration" in navigation bar
2. Click "Add Agent"
3. Fill in agent information:
   - Agent Name: e.g., "Plot Planner"
   - Role: e.g., "Plot Planning"
   - System Prompt: Define the agent's basic behavior
   - Instructions: Specific task instructions
   - Bind Model: Select the configured model with API key
4. Click "Save"

### 4. Start Creating

1. Click "Novel Management", create new novel
2. Improve character settings and world settings
3. Click "Conversation Creation" to start using AI for creation

## Model Recommendations

### Completely Free (Local Deployment)
- **Ollama**: Completely free, requires local installation, suitable for users with high privacy requirements

### Free API Quota
- **Groq**: New users have free quota, fast response
- **Together AI**: New users have free quota, multiple models available
- **Qwen**: New users have free quota, strong Chinese understanding
- **Doubao**: New users have free quota, good performance
- **DeepSeek**: New users have free quota, cost-effective

### Paid Models
- **Chinese Novel Creation**: 
  - **First Choice**: Qwen Pro-128K (Strong Chinese understanding)
  - **Second Choice**: DeepSeek V3 (Cost-effective)

### Plot Planning
- **First Choice**: Claude 3 Sonnet (Strong logical reasoning)
- **Second Choice**: GPT-4 (Strong comprehensive capabilities)

### Dialogue Generation
- **First Choice**: GPT-4 Turbo (Fast response)
- **Second Choice**: Qwen Plus (Natural Chinese)

## Important Notes

1. **API Key Security**: Please keep your API keys secure and don't share them with others
2. **Cost Control**: Each AI model has its own billing rules, please monitor your usage
3. **Model Selection**:
   - Ollama: Completely free, but requires local deployment
   - Groq/Together/Qwen/Doubao/DeepSeek: Free quota available, suitable for testing
   - OpenAI/Claude: Paid, but powerful features
4. **Parameter Adjustment**:
   - Temperature (Temperature): Higher values make output more random; lower values make output more deterministic
   - Max Tokens: Controls output length, adjust according to needs
5. **Character Set Issues**: If you see garbled text, ensure database character set is utf8mb4

## Troubleshooting

### Model Call Failed
1. Check if API key is correct
2. Check if network connection is normal
3. Check if API endpoint is correct
4. Check backend logs for detailed error information
5. For Ollama, check if service is running: `curl http://localhost:11434/api/tags`

### Database Connection Failed
1. Check if MySQL service is running
2. Check if database username and password are correct
3. Check if database is created (novel)
4. Check if character set is utf8mb4

### Character Set Issues
1. Ensure database character set is utf8mb4
2. Ensure connection string includes character set parameter
3. Check table character set settings

## Get Help

If you encounter issues, please check:
1. Backend logs: Console output
2. Frontend logs: Browser developer tools
3. Official documentation from each AI platform