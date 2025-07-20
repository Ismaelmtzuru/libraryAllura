📚 Library Console Application
This is a Spring Boot console application designed to interact with book and author data. It allows users to perform various operations, including searching for books, counting books by language, and finding authors who were alive in a specific year. The application leverages Spring Data JPA for database interactions and an external API (Gutendex) for fetching book information.

🚀 Features
```
View All Books: Retrieve and display a sample of books from the Gutendex API.

Search Book by Title: Search for books using a title keyword via the Gutendex API.

Search Books by Author: (Currently redirects to search by title - To be implemented for specific author search).

Count Books by Language: Get a count of books available in a specific language (requires prior data processing/storage).

Find Authors Alive in a Given Year: Search the local database for authors who were alive during a specified year, using advanced Spring Data JPA derived queries.

Data Persistence: Saves fetched book and author data from the API into a local database.
```
🛠️ Technologies Used
Spring Boot 3.x: Framework for building standalone, production-grade Spring applications.

Spring Data JPA: Simplifies database access and persistence with JPA (Java Persistence API).

Hibernate: JPA implementation used for ORM (Object-Relational Mapping).

H2 Database (or similar): An in-memory or file-based relational database for development and testing. (You might be using PostgreSQL, MySQL, etc., adjust as needed).

Jackson (fasterxml/jackson): For JSON parsing and serialization (used in ApiCall and DTO mapping).

java.util.Scanner: For handling console input from the user.

External API: Gutendex - A public domain books API.

📦 Project Structure
```
├── src/main/java
│   └── com/free/library
│       ├── LibraryApplication.java           # Main Spring Boot application entry point
│       │
│       ├── entity                            # JPA Entities (Database Models)
│       │   ├── Author.java
│       │   └── Book.java
│       │
│       ├── repository                        # Spring Data JPA Repositories
│       │   ├── AuthorRepository.java
│       │   └── BookRepository.java
│       │
│       ├── service                           # Business Logic & Console UI Orchestration
│       │   ├── ConsoleMenu.java              # Handles main menu loop and user interaction
│       │   ├── ApiCall.java                  # Service for making external API calls
│       │   ├── BookPersistenceService.java   # Service for persisting Book/Author data
│       │   └── StatisticsService.java        # Service for statistics (e.g., language count)
│       │
│       └── dto                               # Data Transfer Objects (for API/data mapping)
│           ├── AuthorDto.java
│           └── BookDto.java
│           └── BookPersistenceService.java   # (Potentially duplicated if this is the same as service/BookPersistenceService.java, clarify)
│
└── src/main/resources
    └── application.properties                # Spring Boot configuration (database, JPA, logging)
```

🚀 Getting Started
Prerequisites
Java 17 or higher

Maven (recommended) or Gradle

An IDE like IntelliJ IDEA, VS Code, or Eclipse

Setup Instructions
Clone the Repository:

Bash

git clone [URL_DEL_REPOSITORIO]
cd library
Configure application.properties:
Open src/main/resources/application.properties. Ensure your database configuration is correct.
(Example for H2 In-Memory, adjust for other databases like PostgreSQL/MySQL):

Properties

```
spring.datasource.url=jdbc:h2:mem:librarydb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update # or create, create-drop, none
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=debug
logging.level.org.hibernate.orm.jdbc.bind=trace
```
Build the Project:
Using Maven:

Bash

mvn clean install
Running the Application
Once built, you can run the Spring Boot application from your IDE or via the command line.

Using Maven:

Bash

mvn spring-boot:run
The application will start, and you will see the console menu in your terminal.

💡 How to Use
After starting the application:

The main menu will appear:

----Library app menu----
1. See all books
2. Search book by title
3. Search books by author
4. Count Books by Language
5. Find Authors lived in/by year
0. End program
Choose an option:
Enter the number corresponding to the desired operation and press Enter.

Follow the prompts for each specific function (e.g., enter a title to search, a year for authors, a language code).

To exit the application, enter 0.

➕ Extending the Project
Complete searchByAuthor(): Implement the actual logic to search for books by author name, perhaps by first finding the author in your database and then querying books associated with them.

Improve Data Persistence: The current persistenceService.saveBooksFromJson(jsonResponse) in searchByTitle saves the entire JSON response repeatedly. Consider modifying it to save only new books/authors, or to update existing ones.

Robust Input Handling: While basic validation is in place, more advanced input validation (e.g., ensuring a year is within a sensible range) and custom error messages can be added.

More Statistics: Add more statistical analysis in StatisticsService (e.g., average download count, most popular authors).

Pagination/Filtering: For large result sets, implement pagination for API calls and database queries.

User Interface: Consider using a more advanced console UI library for a richer interactive experience if the project scales.

