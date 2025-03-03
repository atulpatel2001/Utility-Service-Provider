# Utility-Service-Provider - Service Booking Platform

Utility-Service-Provider is a Spring Boot-based application that offers on-demand services like AC repair, plumbing, electrical work, and more. Similar to Urban Company, this platform connects customers with service providers efficiently.

## Features
- **Service Booking**: Users can browse and book services.
- **User Authentication**: Secure login and registration.
- **Service Provider Management**: Admin can manage service providers.
- **Order Management**: Track service requests and history.
- **Payment Integration**: Supports multiple payment options.
- **Review & Ratings**: Users can provide feedback for services.
- **PostgreSQL Database**: Stores user, service, and transaction data.

## Technologies Used
- **Spring Boot** - Backend framework
- **PostgreSQL** - Relational database
- **Spring Security** - User authentication & authorization
- **Hibernate/JPA** - ORM for database interaction
- **Thymeleaf** - Template engine for dynamic web pages
- **Spring MVC** - For handling user requests

## Installation
### Prerequisites
- Install **Java 17+**
- Install **PostgreSQL** and set up a database
- Install **Maven**

### Steps to Run the Application
1. Clone the repository:
   ```bash
   git clone <repository_url>
   cd utility-service-provider
   ```
2. Configure the database in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```
3. Install dependencies and build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
5. Open the application in your browser:
   ```
   http://localhost:8080/
   ```

## Pages & Routing (Thymeleaf)
- **Home Page (`/Book-Store`)** - Displays available services
- **Login Page (`/Book-Stor/login`)** - User authentication
- **Registration Page (`/Book-Store/register`)** - New user signup

## Contributions
Contributions are welcome! Feel free to submit issues or pull requests.
---
Happy coding! 🚀

