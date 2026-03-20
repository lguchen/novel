-- 插入常用AI模型配置
-- 设置字符集以避免乱码
SET NAMES utf8mb4;

-- 免费模型：Ollama（本地部署，完全免费）
INSERT INTO ai_models (name, provider, model_type, api_url, model_id, max_tokens, temperature, is_active, created_at, updated_at) VALUES
('Ollama Llama 3.2', 'ollama', 'chat', 'http://localhost:11434/api/chat', 'llama3.2', 4000, 0.7, true, NOW(), NOW()),
('Ollama Mistral 7B', 'ollama', 'chat', 'http://localhost:11434/api/chat', 'mistral', 4000, 0.7, true, NOW(), NOW()),
('Ollama Qwen 7B', 'ollama', 'chat', 'http://localhost:11434/api/chat', 'qwen', 4000, 0.7, true, NOW(), NOW());

-- 免费模型：Groq（有免费API额度）
INSERT INTO ai_models (name, provider, model_type, api_url, model_id, max_tokens, temperature, is_active, created_at, updated_at) VALUES
('Groq Llama 3 70B', 'groq', 'chat', 'https://api.groq.com/openai/v1/chat/completions', 'llama3-70b-8192', 8000, 0.7, true, NOW(), NOW()),
('Groq Mixtral 8x7B', 'groq', 'chat', 'https://api.groq.com/openai/v1/chat/completions', 'mixtral-8x7b-32768', 8000, 0.7, true, NOW(), NOW());

-- 免费模型：Together AI（有免费API额度）
INSERT INTO ai_models (name, provider, model_type, api_url, model_id, max_tokens, temperature, is_active, created_at, updated_at) VALUES
('Together Llama 3 70B', 'together', 'chat', 'https://api.together.xyz/v1/chat/completions', 'togethercomputer/Llama-3-70b-chat-hf', 4000, 0.7, true, NOW(), NOW()),
('Together Mistral 7B', 'together', 'chat', 'https://api.together.xyz/v1/chat/completions', 'mistralai/Mistral-7B-Instruct-v0.2', 4000, 0.7, true, NOW(), NOW());

-- 通义千问模型（阿里云，有免费额度）
INSERT INTO ai_models (name, provider, model_type, api_url, model_id, max_tokens, temperature, is_active, created_at, updated_at) VALUES
('通义千问 Pro-128K', 'qwen', 'chat', 'https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions', 'qwen-max-latest', 8000, 0.7, true, NOW(), NOW()),
('通义千问 Plus', 'qwen', 'chat', 'https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions', 'qwen-plus', 8000, 0.7, true, NOW(), NOW()),
('通义千问 Turbo', 'qwen', 'chat', 'https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions', 'qwen-turbo', 8000, 0.7, true, NOW(), NOW());

-- 豆包模型（字节跳动，有免费额度）
INSERT INTO ai_models (name, provider, model_type, api_url, model_id, max_tokens, temperature, is_active, created_at, updated_at) VALUES
('豆包 Pro', 'doubao', 'chat', 'https://ark.cn-beijing.volces.com/api/v3/chat/completions', 'ep-20240320183046-u7p95', 8000, 0.7, true, NOW(), NOW()),
('豆包 Turbo', 'doubao', 'chat', 'https://ark.cn-beijing.volces.com/api/v3/chat/completions', 'ep-20240320183046-u7p95', 8000, 0.7, true, NOW(), NOW());

-- DeepSeek 模型（有免费额度）
INSERT INTO ai_models (name, provider, model_type, api_url, model_id, max_tokens, temperature, is_active, created_at, updated_at) VALUES
('DeepSeek V3', 'deepseek', 'chat', 'https://api.deepseek.com/v1/chat/completions', 'deepseek-chat', 8000, 0.7, true, NOW(), NOW()),
('DeepSeek Coder', 'deepseek', 'chat', 'https://api.deepseek.com/v1/chat/completions', 'deepseek-coder', 8000, 0.7, true, NOW(), NOW());

-- OpenAI 模型（需要付费）
INSERT INTO ai_models (name, provider, model_type, api_url, model_id, max_tokens, temperature, is_active, created_at, updated_at) VALUES
('GPT-4', 'openai', 'chat', 'https://api.openai.com/v1/chat/completions', 'gpt-4', 8000, 0.7, true, NOW(), NOW()),
('GPT-4 Turbo', 'openai', 'chat', 'https://api.openai.com/v1/chat/completions', 'gpt-4-turbo-preview', 8000, 0.7, true, NOW(), NOW()),
('GPT-3.5 Turbo', 'openai', 'chat', 'https://api.openai.com/v1/chat/completions', 'gpt-3.5-turbo', 4000, 0.7, true, NOW(), NOW());

-- Anthropic Claude 模型（需要付费）
INSERT INTO ai_models (name, provider, model_type, api_url, model_id, max_tokens, temperature, is_active, created_at, updated_at) VALUES
('Claude 3 Opus', 'anthropic', 'chat', 'https://api.anthropic.com/v1/messages', 'claude-3-opus-20240229', 8000, 0.7, true, NOW(), NOW()),
('Claude 3 Sonnet', 'anthropic', 'chat', 'https://api.anthropic.com/v1/messages', 'claude-3-sonnet-20240229', 8000, 0.7, true, NOW(), NOW()),
('Claude 3 Haiku', 'anthropic', 'chat', 'https://api.anthropic.com/v1/messages', 'claude-3-haiku-20240307', 4000, 0.7, true, NOW(), NOW());