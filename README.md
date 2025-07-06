# VOX Code Backend

![VoxCode (1)](https://github.com/user-attachments/assets/51be6290-5d50-48bd-8a91-c89fd2208616)

A voice-controlled IDE backend service built with Quarkus that processes natural language commands and converts them into executable IDE actions. This backend is part of the VOX Code IDE project, designed to help programmers with motor disabilities through voice recognition and AI-powered command processing.

## 🚀 Quick Start

### Prerequisites

- **Java 21** 
- **Maven** 3.8+
- **PostgreSQL** 14+
- **Docker** (optional, for database setup)
- **OpenAI API Key** (required for AI functionality)

### Database Setup

1. **Start PostgreSQL container:**
   ```bash
   docker run -d \
   --name voice-ide-db \
   -e POSTGRES_USER=voice_user \
   -e POSTGRES_PASSWORD=voice_pass \
   -e POSTGRES_DB=voice_ide_db \
   -p 5432:5432 \
   postgres:14
   ```

2. **Initialize database:**
   ```bash
   # Execute the SQL script to create tables and insert initial data
   psql -h localhost -U voice_user -d voice_ide_db -f bdd/INSERTS.sql
   ```

### Environment Configuration

1. **Copy environment template:**
   ```bash
   cp .env.example .env
   ```

2. **Configure your API keys:**
   ```bash
   # Edit the .env file with your OpenAI API credentials
   nano .env
   ```
   
   Update the following variables in your `.env` file:
   ```bash
   # OpenAI API Configuration
   OPENAI_API_KEY=your-actual-openai-api-key-here
   OPENAI_API_URL=https://api.openai.com/v1/chat/completions
   
   # Database Configuration (optional - defaults are provided)
   DB_USERNAME=voice_user
   DB_PASSWORD=voice_pass
   DB_NAME=voice_ide_db
   DB_HOST=localhost
   DB_PORT=5432
   ```

**Important**: 
- Never commit your actual API keys to version control
- The `.env` file is automatically ignored by Git
- Use the `.env.example` file as a template for your configuration

### Running the Application

**Development mode:**
```bash
./mvnw quarkus:dev
```

**Production build:**
```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

**Native executable:**
```bash
./mvnw package -Dnative -Dquarkus.native.container-build=true
./target/vox_code-1.0.0-SNAPSHOT-runner
```

The application will be available at `http://localhost:8080`

## 🔗 Frontend Integration

This backend is designed to work with the VOX Code IDE frontend. The frontend repository can be found at:
[https://github.com/CamperoJose/vox_code_ide](https://github.com/CamperoJose/vox_code_ide)

## 📚 API Documentation

### Sample Endpoints

#### Chat Processing
**POST** `/chat`
Processes natural language commands and returns executable actions.

**Request:**
```json
{
  "message": "quiero generar un archivo con el nombre .env para mis credenciales"
}
```

**Response:**
```json
{
  "match": true,
  "allParams": true,
  "functionKey": "#NEW_FILE_ROOT",
  "groupKey": "#DIRECTORY_WINDOW",
  "outParams": [
    {
      "paramKey": "#FILE_NAME",
      "value": ".env"
    }
  ],
  "summary": "I'll create a new file named .env in the project root."
}
```

#### Function Management
**GET** `/function/list`
Returns all available functions.

**GET** `/function/params`
Get parameters for a specific function.

**Request:**
```json
{
  "key": "#TERMINAL_COMMAND"
}
```

#### Group Management
**GET** `/group/list`
Returns all function groups.

### Function Groups

The system organizes functions into three main groups:

#### 1. **#EDITOR_WINDOW** - Code Editor Operations
Functions for manipulating code in the editor:

- `#MOVE_CURSOR_TO_LINE` - Move cursor to specific line
- `#SELECT_LINE` - Select entire line
- `#SELECT_LINE_RANGE` - Select range of lines
- `#INSERT_CODE` - Insert AI-generated code
- `#DELETE_LINE` - Delete specific line

#### 2. **#DIRECTORY_WINDOW** - File Management
Functions for file and folder operations:

- `#NEW_FILE_ROOT` - Create new file in root
- `#NEW_FOLDER_ROOT` - Create new folder in root
- `#OPEN_FOLDER` - Open folder in explorer
- `#OPEN_FILE` - Open file in editor

#### 3. **#TERMINAL_WINDOW** - Terminal Operations
Functions for terminal commands:

- `#TERMINAL_COMMAND` - Execute shell command
- `#TERMINAL_EMPTY_ENTER` - Send empty enter key

### Example Commands

Here are some example voice commands and their responses:

#### File Operations
```bash
# Create new file
POST /chat
{
  "message": "quiero generar un nuevo archivo llamado catalog para mis pruebas y debe estar dentro de la carpeta http"
}
```

#### Code Editor Operations
```bash
# Insert code at specific line
POST /chat
{
  "message": "debes insertar codigo de una funcion para verificar si un numero es primo en python en la linea 5"
}
```

**Response:**
```json
{
  "match": true,
  "allParams": true,
  "functionKey": "#INSERT_CODE",
  "groupKey": "#EDITOR_WINDOW",
  "outParams": [
    {
      "paramKey": "#LINE_NUMBER",
      "value": "5"
    },
    {
      "paramKey": "#GENERATED_CODE",
      "value": "def is_prime(num):\n\tif num <= 1:\n\t\treturn False\n\tfor i in range(2, int(num**0.5) + 1):\n\t\tif num % i == 0:\n\t\t\treturn False\n\treturn True"
    }
  ],
  "summary": "I've inserted Python code to check if a number is prime at line 5."
}
```

#### Terminal Operations
```bash
# Execute terminal command
POST /chat
{
  "message": "quiero insertar un comando vacio en la terminal"
}
```

**Response:**
```json
{
  "match": true,
  "allParams": true,
  "functionKey": "#TERMINAL_EMPTY_ENTER",
  "groupKey": "#TERMINAL_WINDOW",
  "outParams": [],
  "summary": "I'll send an empty enter command to the terminal."
}
```

## 🏗️ Architecture

### Project Structure
```
src/main/java/ide/vox/code/
├── bl/                    # Business Logic
│   ├── ChatService.java   # Main chat processing
│   ├── FunctionService.java
│   └── GroupService.java
├── data/
│   ├── entity/           # Database entities
│   ├── repository/       # Data access layer
│   └── mapper/          # Object mapping
├── dto/                  # Data Transfer Objects
├── rest/                 # REST endpoints
└── utils/               # Utility classes
```

### Key Components

- **ChatService**: Processes natural language and matches to functions
- **FunctionService**: Manages available functions and their parameters
- **GroupService**: Handles function grouping and categorization
- **OpenAI Integration**: Uses AI to understand and process voice commands

## 🔧 Configuration

The application uses environment variables for configuration. Key settings are loaded from the `.env` file:

```properties
# Database (with environment variable fallbacks)
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=${DB_USERNAME:voice_user}
quarkus.datasource.password=${DB_PASSWORD:voice_pass}
quarkus.datasource.jdbc.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:voice_ide_db}

# OpenAI API (from environment variables)
openai.api.key=${OPENAI_API_KEY}
openai.api.url=${OPENAI_API_URL:https://api.openai.com/v1/chat/completions}

# Server
quarkus.http.port=8080
```

## Testing

Test the API endpoints using the provided `http/endpoints.http` file:

```bash
# List all groups
GET http://localhost:8080/group/list

# List all functions
GET http://localhost:8080/function/list

# Get function parameters
GET http://localhost:8080/function/params
Content-Type: application/json
{
    "key":"#TERMINAL_COMMAND"
}

# Test chat processing
POST http://localhost:8080/chat
Content-Type: application/json
{
  "message":"quiero generar un archivo con el nombre .env"
}
```

---

**Made with ❤️ for the programming community**

