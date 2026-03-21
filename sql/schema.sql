-- Create database
CREATE DATABASE IF NOT EXISTS novel CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE novel;

-- Create novels table
CREATE TABLE IF NOT EXISTS novels (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    genre VARCHAR(100),
    target_chapters INT,
    chapter_word_count INT,
    word_count INT DEFAULT 0,
    content LONGTEXT,
    context_memory LONGTEXT,
    style_guide TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_title (title),
    INDEX idx_genre (genre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create characters table
CREATE TABLE IF NOT EXISTS characters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(100),
    personality TEXT,
    color VARCHAR(20),
    background TEXT,
    appearance TEXT,
    relationships TEXT,
    novel_id BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_novel_id (novel_id),
    INDEX idx_name (name),
    FOREIGN KEY (novel_id) REFERENCES novels(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create world_settings table
CREATE TABLE IF NOT EXISTS world_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    description TEXT,
    rules TEXT,
    geography TEXT,
    history TEXT,
    novel_id BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_novel_id (novel_id),
    INDEX idx_category (category),
    FOREIGN KEY (novel_id) REFERENCES novels(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create ai_models table
CREATE TABLE IF NOT EXISTS ai_models (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    provider VARCHAR(100),
    model_type VARCHAR(100),
    api_key TEXT,
    api_url VARCHAR(500),
    model_id VARCHAR(255),
    max_tokens INT DEFAULT 2000,
    temperature DOUBLE DEFAULT 0.7,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_provider (provider),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create agents table
CREATE TABLE IF NOT EXISTS agents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    system_prompt TEXT,
    instructions TEXT,
    role VARCHAR(100),
    model_id BIGINT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_model_id (model_id),
    INDEX idx_is_active (is_active),
    FOREIGN KEY (model_id) REFERENCES ai_models(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create conversation_history table
CREATE TABLE IF NOT EXISTS conversation_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    novel_id BIGINT,
    agent_id BIGINT,
    role VARCHAR(50),
    content LONGTEXT,
    sequence INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_novel_id (novel_id),
    INDEX idx_agent_id (agent_id),
    INDEX idx_sequence (sequence),
    FOREIGN KEY (novel_id) REFERENCES novels(id) ON DELETE CASCADE,
    FOREIGN KEY (agent_id) REFERENCES agents(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create prompts table
CREATE TABLE IF NOT EXISTS prompts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    category VARCHAR(100),
    tags VARCHAR(255),
    usage_count INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category (category),
    INDEX idx_tags (tags),
    INDEX idx_usage_count (usage_count)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create chapters table
CREATE TABLE IF NOT EXISTS chapters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    novel_id BIGINT,
    title VARCHAR(255) NOT NULL,
    chapter_number INT,
    content LONGTEXT,
    word_count INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_novel_id (novel_id),
    INDEX idx_chapter_number (chapter_number),
    FOREIGN KEY (novel_id) REFERENCES novels(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;