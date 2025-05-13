# 📚 Content Management System (CMS)

A lightweight, in-memory **RESTful API** that mimics **Adobe Experience Manager (AEM)** JCR behavior for content management.

---

## 🚀 Features

- 📄 **CRUD API for Content**:
  - `GET /content`: Used to display the content
  - `POST /content`: Creates content with following parameters (`title`, `body`, `author`, `tags`)
  - `GET /content/{id}`: Fetch content by ID
  - `GET /content/search?tag=&author=`: Search by tag and/or author (`Tag` and `Author` are not mandatory)
  - `PATCH /content/{id}`: Updates tags
  - `DELETE /content/{id}`: Remove content by id

- 🛡️ **Validation**: 
  - Used basic Jakarta Bean Validations as we are making use of in-memory storage(ConcurrentHashMap) ensures non-empty fields.
  - Added Custom Annotations

- 💾 **In-Memory Repository**: Uses `ConcurrentHashMap` to simulate JCR-like storage

- 🔍 **Swagger UI**: API docs and interactive testing at `/swagger-ui.html`

- ❗ **Error Handling**: Made use of both the `Custom Exception handling mechanism` as well as the `GlobalExceptionHandlers`

---

## 🧱 Project Structure

```bash
src/
├── main/
│ ├── java/com/aems/cmis/
│ │ ├── config
│ │ ├── controllers
│ │ ├── dto
│ │ ├── execption
│ │ ├── mapper
│ │ ├── model  
│ │ ├── repository
│ │ └── service
│ └── resources/
│ ├── application.properties
│ └── static/ # (Optional)
└── test/
└── java/com/aems/cmis/
│   ├── config
│   ├── controller
└── 
```

---

## 🛠️ Tech Stack

| Technology        | Version     |
|-------------------|-------------|
| Java              | 17          |
| Spring Boot       | 3.4.5       |
| Gradle            | 8.x         |
| Jakarta Validation| ✓           |
| Lombok            | ✓           |
| JUnit + Mockito   | ✓           |
| Swagger/OpenAPI   | ✓           |
| IDE               | IntelliJ / VS Code / Eclipse |
| OS                | macOS / Windows / Linux       |

---

## ⚙️ Setup Instructions

### 1. ✅ Prerequisites
- Java 17+
- Gradle 8+
- Git

### 2. 📦 Clone the Project
```bash
git clone https://github.com/your-repo/content-management-api.git
cd content-management-api
```

### 3. 🔨 Build the Application
```bash
./gradlew build
```
### 4. ▶️ Run the App
```bash
./gradlew bootRun
```
### 5. 🌐 Access API
Swagger UI: http://localhost:8080/swagger-ui.html
Base URL: http://localhost:8080/content

### 6. 🧪 Testing

- ***🔍 Unit & Integration Tests:***
```bash
./gradlew test
```
- ***Tests cover:***

ContentService – logic & validation
ContentController – endpoints
Uses JUnit 5 + Mockito

- ***🧪 Manual Testing (Postman / curl)***
# Create content
```bash
curl -X POST http://localhost:8080/content \
  -H "Content-Type: application/json" \
  -d '{"title":"Hello","body":"World","author":"Admin","tags":["aem","news"]}'
```

# Get content by ID
```bash
curl http://localhost:8080/content/{id}
```
# Search content
```bash
curl "http://localhost:8080/content/search?tag=aem"
```
# Update tags
```bash
curl -X PATCH http://localhost:8080/content/{id} \
  -H "Content-Type: application/json" \
  -d '{"tags":["updated"]}'
```

# Delete content
```bash
curl -X DELETE http://localhost:8080/content/{id}
```
### 7. 💻 Recommended Dev Environment

***IDE:***
Visual Studio Code (or IntelliJ / Eclipse)   

***Extensions:***
- ✅ Java Extension Pack – Microsoft
- ✅ Spring Boot Extension Pack – VMware
- ✅ Lombok Support – Gabriel Basto

***Debugging:***
Set breakpoints and debug with VS Code `(FN+F6/FN+F8)`   


### 8. 📌 Notes

This project simulates a mini AEM-like CMS using Spring Boot & in-memory JCR structure
All endpoints and usage are fully documented via Swagger
Clean separation of concerns for controller, service, repository, and model

### 9. 📫 Contact

Have suggestions or want to contribute? Raise an issue or PR!
