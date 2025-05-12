# README.md - Content Management API

## Overview

This project is a Spring Boot-based RESTful API for managing content articles. It allows users to create, update, and search content by tags and authors. The API is designed to be scalable and extensible, supporting common content management operations. The project includes unit and integration tests for comprehensive verification.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Project Setup](#project-setup)
3. [Running the Application](#running-the-application)
4. [Testing](#testing)
5. [API Endpoints](#api-endpoints)
6. [Error Handling](#error-handling)
7. [Common Issues](#common-issues)
8. [License](#license)
9. [Contributors](#contributors)

---

## Prerequisites

Before running the project, ensure that you have the following installed:

- **Java 17+**: Ensure you have the Java 17 JDK installed.
- **Maven**: For project builds and dependency management. Alternatively, you can use **Gradle** if you prefer.
- **IDE**: An Integrated Development Environment (IDE) such as IntelliJ IDEA, Eclipse, or Visual Studio Code.
- **Postman** or any REST client for testing the API endpoints.
- **Git**: To clone the project from the repository.

---

## Project Setup

Follow these steps to set up the project locally.

### Clone the Repository

Clone the repository to your local machine using Git.

```bash
git clone https://github.com/your-repository/content-management-api.git
cd content-management-api


Install Dependencies
If you're using Maven,run to install all project dependencies:
```bash
mvn clean install

For Gradle, you can use the following:
```bash
gradle build

Running the Application

To run the Spring Boot application, execute the following command in your terminal:
```bash
mvn spring-boot:run

Testing

The project includes tests that can be run via Maven or Gradle.

Unit Tests
To run unit tests for your project, execute the following command:

mvn test
For Gradle:

gradle test
The test suite includes:

Valid and invalid content creation
Search functionality (tag-based search)
Error handling for edge cases like missing parameters or invalid data
Controller tests using MockMvc
API Endpoints

POST /content
Creates new content.

Request Body: ContentDTO (JSON)
{
  "title": "Test Article",
  "body": "This is the article content.",
  "author": "Admin",
  "tags": ["news", "tech"],
  "createdAt": "2025-05-12T10:00:00"
}
Responses:
201 Created – On success
400 Bad Request – If validation fails
PATCH /content/{id}
Updates existing content by its id.

Request Body: ContentDTO (JSON)
Path Variable: id – ID of the content to update
Responses:
200 OK – On success
400 Bad Request – If validation fails
404 Not Found – If the content does not exist
GET /content/search
Search for content by tags (and optionally, authors).

Query Parameters:
tag (required)
author (optional)
Responses:
200 OK – Returns the list of matching content
404 Not Found – If no matching content is found
Error Handling

This application uses standard HTTP status codes for error handling:

400 Bad Request: Validation errors, such as missing required fields.
404 Not Found: When a requested resource is not found.
500 Internal Server Error: General server-side errors or unexpected issues.
Example Error Response:
{
  "timestamp": "2025-05-12T08:29:26",
  "status": 500,
  "error": "Server Error",
  "message": "An unexpected error occurred",
  "details": ["Name for argument of type [java.lang.String] not specified."]
}
Common Issues

1. 500 Error on GET /content/search
Issue: The error may be related to missing query parameters or issues in the service layer.
Solution: Ensure that the controller method handles the query parameters correctly. Also, ensure that the service layer (ContentService) returns data as expected.
2. JSON Parse Error
Issue: Invalid or incomplete JSON data in the request body.
Solution: Make sure that the ContentDTO fields are correctly populated and the required fields are not null when sending the request.
3. Missing Path Parameter in PATCH /content/{id}
Issue: The ID parameter is required in the path, but it's missing.
Solution: Make sure the URL is correctly formatted, including the id path variable.
4. Validation Failure
Issue: When required fields like title, body, or author are empty, validation fails.
Solution: Ensure all required fields are filled in the request body before submitting.
License

This project is open-source and licensed under the MIT License. See the LICENSE file for more details.

Contributors

Your Name (Developer / Maintainer)
Other Contributors (if applicable)

This **HELP.md** provides detailed documentation for setting up, running, and troubleshooting




