# CommitConf 2025 - CommitConf 2025 - Spring AI: IA Avanzada para desarrolladores Spring

Este es el código de ejemplo de mi charla en CommitConf 2025 https://koliseo.com/commit/commit-conf-2025/agenda/0?selected=hotjrnofx0HBWHpN7ocZ.

Las slides están disponibles en https://www.slideshare.net/slideshow/commitconf-2025-spring-ai-ia-avanzada-para-desarrolladores-spring/277630331

## Requisitos Previos
- Java 21
- Docker y Docker Compose
- Una clave de API de OpenAI
- Una clave de https://www.weatherapi.com/

## Configuración Inicial

### 1. Configuración de la Base de Datos
Para iniciar la base de datos PostgreSQL con la extensión pgvector, ejecuta:

```bash
docker-compose up -d
```

### 2. Configuración de OpenAI
Para utilizar la aplicación, necesitas configurar tu clave de API de OpenAI. Añade la siguiente variable de entorno:

```bash
export OPENAI_API_KEY=tu_clave_api
```

Es posible utilizar [Ollama](https://ollama.com/) en lugar de Open AI, para ello instala Ollama y descarga algún modelo que puedas utilizar con tu tarjeta gráfica Nvidia, como por ejemplo `ollama run llama3.2:3b`.

## Arrancar la Aplicación

Para iniciar la aplicación, ejecuta:

```bash
./gradlew bootRun
```

La aplicación estará disponible en: http://localhost:8080

## Ejemplos Disponibles

La aplicación incluye un frontend hecho en Vaadin y creado con [JetBrains Junie](https://www.jetbrains.com/junie/). Seleccionando distintos endpoints se pueden ir ejecutando todas las demos vistas durante la charla.

