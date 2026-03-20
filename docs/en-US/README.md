# AI Novel Writing Assistant

A powerful AI-assisted novel writing platform with memory persistence, multiple AI model integration, and a user-friendly interface.

<div align="center">
### 🌐 语言切换
  <a href="../zh-CN/README.md">中文版</a> | <a href="../en-US/README.md">English</a>
</div>

## Features

### Core Features
1. **Novel Character & Setting Planning**
   - Visual character profile creation
   - Worldview construction
   - Plot outline development
   - AI-powered creative inspiration preservation

2. **Custom Skills & Agents**
   - Accumulate creative experience
   - Build personalized creative agents
   - Adapt to individual creative workflows

3. **Conversation as Creation**
   - Natural dialogue-based instructions
   - AI content generation
   - Character development
   - Plot continuation

### Technical Features
- **Memory Persistence**: Prevents AI from losing context after server restart
- **Multiple AI Model Support**: Integrates mainstream AI models with web-based configuration
- **Frontend-Backend Separation**: Modern Vue3 frontend with Java Spring Boot backend
- **Local Usage**: Designed for personal local use without complex authentication

## Technology Stack

### Frontend
- **Framework**: Vue 3
- **State Management**: Pinia
- **Router**: Vue Router
- **Build Tool**: Vite
- **Styling**: CSS3

### Backend
- **Framework**: Java Spring Boot
- **Database**: MySQL
- **ORM**: JPA/Hibernate
- **API**: RESTful

### AI Models
- **Free Models**: Ollama (local), Groq, Together AI, Qwen, Doubao, DeepSeek
- **Paid Models**: OpenAI GPT, Anthropic Claude

## Quick Start

### Prerequisites
- Java 11+ 
- Maven 3.6+
- Node.js 16+
- MySQL 5.7+
- (Optional) Ollama for local models

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/lguchen/novel.git
   cd novel
   ```

2. **Setup MySQL Database**
   ```sql
   CREATE DATABASE novel CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **Backend Setup**
   ```bash
   cd novel_serve
   mvn clean install
   ```

4. **Frontend Setup**
   ```bash
   cd novel_web
   npm install
   ```

### Running the Application

1. **Start Backend**
   ```bash
   cd novel_serve
   mvn spring-boot:run
   ```

2. **Start Frontend**
   ```bash
   cd novel_web
   npm run dev
   ```

3. **Access the Application**
   - Frontend: http://localhost:5173
   - Backend API: http://localhost:8080

## Project Structure

```
novel/
├── docs/                # Documentation
│   ├── en-US/           # English documentation
│   ├── zh-CN/           # Chinese documentation
├── novel_serve/         # Backend
│   ├── src/main/java/   # Java source code
│   ├── src/main/resources/ # Configuration files
├── novel_web/           # Frontend
│   ├── src/             # Vue source code
│   ├── public/          # Static assets
├── sql/                 # SQL files
├── start.bat            # Quick start script
├── README.md            # This file
```

## AI Model Configuration

For detailed information about AI model configuration, please refer to the [AI Model Configuration Guide](AI_MODEL_CONFIGURATION_GUIDE.md).

## Usage Guide

1. **Create a New Novel**
   - Click "Novel Management" in the navigation bar
   - Click "Add Novel"
   - Enter novel title and basic information

2. **Configure Characters**
   - Click "Character Management"
   - Add character profiles with detailed information

3. **Build World Settings**
   - Click "World Setting Management"
   - Create and organize world settings

4. **Configure AI Models**
   - Click "AI Model Configuration"
   - Enter API keys for your preferred models

5. **Create Agents**
   - Click "Agent Configuration"
   - Create specialized agents for different writing tasks

6. **Start Writing**
   - Click "Conversation Creation"
   - Use natural dialogue to generate content

## Key Modules

### Backend Modules
- **AIModelController**: Manage AI model configurations
- **AgentController**: Handle agent creation and management
- **CharacterController**: Manage character profiles
- **ConversationController**: Handle AI conversations
- **NovelController**: Manage novel data
- **WorldSettingController**: Manage world settings

### Frontend Pages
- **Home.vue**: Dashboard
- **NovelList.vue**: Novel management
- **NovelEdit.vue**: Novel editing
- **CharacterManage.vue**: Character management
- **WorldSettingManage.vue**: World setting management
- **AIModelConfig.vue**: AI model configuration
- **AgentConfig.vue**: Agent configuration
- **Conversation.vue**: AI conversation interface

## Troubleshooting

### Common Issues
1. **Database Connection Error**
   - Check MySQL service is running
   - Verify database credentials in application.yml
   - Ensure database "novel" exists

2. **AI Model Call Failure**
   - Check API key is correct
   - Verify network connection
   - Check model endpoint URL

3. **Frontend Not Loading**
   - Ensure frontend dependencies are installed
   - Check frontend server is running
   - Verify backend API is accessible

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License.

## Acknowledgements

- Vue 3 - Frontend framework
- Spring Boot - Backend framework
- MySQL - Database
- Various AI model providers for their APIs

## Support

If you encounter any issues, please check the documentation or open an issue on GitHub.