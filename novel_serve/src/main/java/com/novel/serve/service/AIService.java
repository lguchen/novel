package com.novel.serve.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.novel.serve.entity.AIModel;
import com.novel.serve.repository.AIModelRepository;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AIService {
    
    @Autowired
    private AIModelRepository aiModelRepository;
    
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    
    private final Gson gson = new Gson();
    
    public String callAIModel(Long modelId, String systemPrompt, String userMessage) throws IOException {
        AIModel model = aiModelRepository.findById(modelId).orElseThrow(() -> new RuntimeException("Model not found"));
        
        String provider = model.getProvider().toLowerCase();
        
        switch (provider) {
            case "openai":
                return callOpenAI(model, systemPrompt, userMessage);
            case "anthropic":
                return callAnthropic(model, systemPrompt, userMessage);
            case "deepseek":
                return callDeepSeek(model, systemPrompt, userMessage);
            case "qwen":
                return callQwen(model, systemPrompt, userMessage);
            case "ollama":
                return callOllama(model, systemPrompt, userMessage);
            case "groq":
                return callGroq(model, systemPrompt, userMessage);
            case "together":
                return callTogether(model, systemPrompt, userMessage);
            default:
                throw new RuntimeException("Unsupported provider: " + provider);
        }
    }
    
    private String callOpenAI(AIModel model, String systemPrompt, String userMessage) throws IOException {
        List<Map<String, String>> messages = new ArrayList<>();
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            messages.add(Map.of("role", "system", "content", systemPrompt));
        }
        messages.add(Map.of("role", "user", "content", userMessage));
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", model.getModelId());
        requestBody.add("messages", gson.toJsonTree(messages));
        requestBody.addProperty("max_tokens", model.getMaxTokens() != null ? model.getMaxTokens() : 2000);
        requestBody.addProperty("temperature", model.getTemperature() != null ? model.getTemperature() : 0.7);
        
        Request request = new Request.Builder()
                .url(model.getApiUrl())
                .addHeader("Authorization", "Bearer " + model.getApiKey())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(requestBody.toString(), MediaType.parse("application/json")))
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
            return jsonResponse.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
        }
    }
    
    private String callAnthropic(AIModel model, String systemPrompt, String userMessage) throws IOException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", model.getModelId());
        requestBody.addProperty("max_tokens", model.getMaxTokens() != null ? model.getMaxTokens() : 2000);
        requestBody.addProperty("temperature", model.getTemperature() != null ? model.getTemperature() : 0.7);
        
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            requestBody.addProperty("system", systemPrompt);
        }
        
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", userMessage);
        requestBody.add("messages", gson.toJsonTree(List.of(message)));
        
        Request request = new Request.Builder()
                .url(model.getApiUrl())
                .addHeader("x-api-key", model.getApiKey())
                .addHeader("anthropic-version", "2023-06-01")
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(requestBody.toString(), MediaType.parse("application/json")))
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
            return jsonResponse.getAsJsonArray("content")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString();
        }
    }
    
    private String callDeepSeek(AIModel model, String systemPrompt, String userMessage) throws IOException {
        List<Map<String, String>> messages = new ArrayList<>();
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            messages.add(Map.of("role", "system", "content", systemPrompt));
        }
        messages.add(Map.of("role", "user", "content", userMessage));
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", model.getModelId());
        requestBody.add("messages", gson.toJsonTree(messages));
        requestBody.addProperty("max_tokens", model.getMaxTokens() != null ? model.getMaxTokens() : 2000);
        requestBody.addProperty("temperature", model.getTemperature() != null ? model.getTemperature() : 0.7);
        
        Request request = new Request.Builder()
                .url(model.getApiUrl())
                .addHeader("Authorization", "Bearer " + model.getApiKey())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(requestBody.toString(), MediaType.parse("application/json")))
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
            return jsonResponse.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
        }
    }
    
    private String callQwen(AIModel model, String systemPrompt, String userMessage) throws IOException {
        List<Map<String, String>> messages = new ArrayList<>();
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            messages.add(Map.of("role", "system", "content", systemPrompt));
        }
        messages.add(Map.of("role", "user", "content", userMessage));
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", model.getModelId());
        requestBody.add("messages", gson.toJsonTree(messages));
        requestBody.addProperty("max_tokens", model.getMaxTokens() != null ? model.getMaxTokens() : 2000);
        requestBody.addProperty("temperature", model.getTemperature() != null ? model.getTemperature() : 0.7);
        
        Request request = new Request.Builder()
                .url(model.getApiUrl())
                .addHeader("Authorization", "Bearer " + model.getApiKey())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(requestBody.toString(), MediaType.parse("application/json")))
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
            return jsonResponse.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
        }
    }
    
    private String callOllama(AIModel model, String systemPrompt, String userMessage) throws IOException {
        List<Map<String, String>> messages = new ArrayList<>();
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            messages.add(Map.of("role", "system", "content", systemPrompt));
        }
        messages.add(Map.of("role", "user", "content", userMessage));
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", model.getModelId());
        requestBody.add("messages", gson.toJsonTree(messages));
        requestBody.addProperty("stream", false);
        
        Request request = new Request.Builder()
                .url(model.getApiUrl())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(requestBody.toString(), MediaType.parse("application/json")))
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
            return jsonResponse.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
        }
    }
    
    private String callGroq(AIModel model, String systemPrompt, String userMessage) throws IOException {
        List<Map<String, String>> messages = new ArrayList<>();
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            messages.add(Map.of("role", "system", "content", systemPrompt));
        }
        messages.add(Map.of("role", "user", "content", userMessage));
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", model.getModelId());
        requestBody.add("messages", gson.toJsonTree(messages));
        requestBody.addProperty("max_tokens", model.getMaxTokens() != null ? model.getMaxTokens() : 2000);
        requestBody.addProperty("temperature", model.getTemperature() != null ? model.getTemperature() : 0.7);
        
        Request request = new Request.Builder()
                .url(model.getApiUrl())
                .addHeader("Authorization", "Bearer " + model.getApiKey())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(requestBody.toString(), MediaType.parse("application/json")))
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
            return jsonResponse.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
        }
    }
    
    private String callTogether(AIModel model, String systemPrompt, String userMessage) throws IOException {
        List<Map<String, String>> messages = new ArrayList<>();
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            messages.add(Map.of("role", "system", "content", systemPrompt));
        }
        messages.add(Map.of("role", "user", "content", userMessage));
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", model.getModelId());
        requestBody.add("messages", gson.toJsonTree(messages));
        requestBody.addProperty("max_tokens", model.getMaxTokens() != null ? model.getMaxTokens() : 2000);
        requestBody.addProperty("temperature", model.getTemperature() != null ? model.getTemperature() : 0.7);
        
        Request request = new Request.Builder()
                .url(model.getApiUrl())
                .addHeader("Authorization", "Bearer " + model.getApiKey())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(requestBody.toString(), MediaType.parse("application/json")))
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
            return jsonResponse.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
        }
    }
}