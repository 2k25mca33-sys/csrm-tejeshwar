# Campus Smart Resource Management System (CSRM)

A complete full-stack web application to manage shared campus resources such as Classrooms, Labs, Lockers, and Equipment. 

## Features
- JWT Authentication & Role-Based Access Control (Admin, Faculty, Student)
- Manage Users and Resources (Admin only)
- Book resources & detect overlapping bookings automatically
- Modify & Cancel Bookings
- RESTful API with layered architecture (Controller -> Service -> Repository)

## Technology Stack
- **Backend:** Java 21, Spring Boot 3.4.0, Spring Security, JWT, MySQL, Maven
- **Frontend:** React 19, TypeScript, Vite, Tailwind CSS v4, Axios, React Router

## Setup Instructions

### 1. Database Setup (MySQL)
The application requires a running MySQL instance.
Create a database in your local MySQL instance called `csrm_db`:
```sql
CREATE DATABASE csrm_db;
```
*(By default, the backend expects MySQL on localhost:3306 with root / root).* You can change these settings in `csrm-backend/src/main/resources/application.properties`.

Alternatively, if you have Docker, you can run the provided `docker-compose.yml` to spin up a MySQL container:
```bash
docker-compose up -d
```

### 2. Backend Setup
1. Open a terminal and navigate to the backend folder:
   ```bash
   cd csrm-backend
   ```
2. Run the application using the Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```
   *Note: On Windows, use `.\mvnw.cmd spring-boot:run`*

The backend API will be available at `http://localhost:8080`.
The database schema is automatically created/updated using Hibernate (`ddl-auto=update`).

### 3. Frontend Setup
1. Open a new terminal and navigate to the frontend folder:
   ```bash
   cd csrm-frontend
   ```
2. Install dependencies (if not already installed):
   ```bash
   npm install
   ```
3. Start the Vite development server:
   ```bash
   npm run dev
   ```

The frontend app will be available at `http://localhost:5173`.

### Initial Usage
- Register a new account via the frontend. 
- By default, users register as `ROLE_STUDENT`.
- To access Admin features (like adding resources), manually change a user's role to `ROLE_ADMIN` in the `users` table in your MySQL database, or modify the registration default temporarily.
