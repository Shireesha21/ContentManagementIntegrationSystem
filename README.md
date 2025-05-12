# Content Management System

A REST API for managing content, simulating Adobe Experience Manager (AEM) JCR behavior.

## Features
- **Endpoints**:
  - `GET /content`: Returns a welcome message with a link to API documentation.
  - `POST /content`: Create content with title, body, author, and tags.
  - `GET /content/{id}`: Retrieve content by ID.
  - `GET /content/search?tag={tag}&author={author}`: Search content by tag or author.
  - `PATCH /content/{id}`: Update tags for a content item.
  - `DELETE /content/{id}`: Delete content by ID.
- **Validation**: Uses Jakarta Bean Validation to ensure non-empty fields.
- **In-Memory Storage**: Uses ConcurrentHashMap to simulate a JCR repository.
- **Swagger Documentation**: Available at `/swagger-ui.html` for interactive API testing.
- **AEM Simulation**: Content model and endpoints mimic JCR node structure.
- **Error Handling**: Custom JSON responses for 404 (Not Found) and 405 (Method Not Allowed) errors.

## Tech Stack

- **Java**: 17
- **Spring Boot**: 3.4.5 
- **Gradle**: 3.4.5
- **Lombok**
- **Jakarta Validation API**
- **JUnit 5 and Mockito for testing**
- **IDE**: IntelliJ IDEA or Eclipse IDE or STS Suite
- **OS**: MAC 
- **Optional**: Postman or curl


## Setup
1. **Prerequisites**:
   - Java 21 (e.g., OpenJDK 21)
   - Gradle 3.5 or higher
   - Git
2. **Clone the repository**:
   ```bash
   git clone -b master https://github.com/your-repository/content-management-api.git
   cd ContentManagementSystem
   ```
3. **Build the project**:
   ```bash
   gradle  build 
   ```
4. **Run the application**:
   ```bash
   gradle bootRun
   ```
5. **Access the API**:
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - API base URL: `http://localhost:8080/content`

## Testing
- **Unit and Integration Tests**:
  - Run tests using:
    ```bash
    gradle test
    ```
  - Tests cover `ContentService` (CRUD and search) and `ContentManagementController` (REST endpoints, validation).
  - Uses JUnit 5 and Mockito with `@Mock` for mocking dependencies.
- **Manual Testing**:
  - Use curl, Postman, or Swagger UI to test endpoints.
  - **Example Requests**:
    ```bash
    # Get message
    curl http://localhost:8080/content

    # Create content
    curl -X POST http://localhost:8080/content -H "Content-Type: application/json" -d '{"title":"Test Article","body":"This is a test.","author":"Admin","tags":["news","aem"]}'

    # Get content by ID
    curl http://localhost:8080/content/<id>

    # Search content by tag
    curl "http://localhost:8080/content/search?tag=news"

    # Update tags
    curl -X PATCH http://localhost:8080/content/<id> -H "Content-Type: application/json" -d '{"tags":["updated"]}'

    # Delete content
    curl -X DELETE http://localhost:8080/content/<id>
    ```
- **Swagger Testing**:
  - Open `http://localhost:8080/swagger-ui.html` to test endpoints interactively.

## Development Environment
- **IDE**: Visual Studio Code
- **Recommended Extensions**:
  - Java Extension Pack (Microsoft)
  - Spring Boot Extension Pack (VMware)
  - Lombok Annotations Support for VS Code (Gabriel Basto)
  - Test Runner (Microsoft)
- **Debugging**:
  - Set breakpoints in VS Code and use the Debug view (FN+F6) to inspect code to jump into next toggle point (FN+F8).

## Notes
- The application simulates AEM JCR behavior using in-memory storage and JCR-like endpoint paths.
- Custom error handling ensures clear JSON responses for invalid requests.
- All endpoints are documented in Swagger UI for easy exploration.
