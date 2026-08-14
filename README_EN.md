# Nursing Home Management System (NHMS)

A full-stack web application for managing elderly care facilities, including resident information, care plans, incidents, operations, and reporting.

This project is separated into a frontend application and a backend API, and it can be run locally or via Docker Compose for faster setup and deployment.

## Project Overview

The system supports administrative workflows for nursing homes and care centers, such as:

- Resident management
- Care task assignment and tracking
- Incident management
- SLA and service configuration
- Reporting and operational dashboards
- User and role-based access control

## Video Demo
![alt text](image-1.png)
https://youtu.be/zI9nwtYnUxo

## Tech Stack

### Frontend
- Node.js: 24.x
- npm: latest compatible version
- TypeScript: ~6.0.2
- Vite: 8.1.0
- React: 19.2.7
- React DOM: 19.2.7
- React Router: 7.18.1
- TanStack React Query: 5.101.1
- Tailwind CSS: 4.3.1
- shadcn/ui + Radix UI
- Axios: 1.18.1
- Zod: 4.4.3
- Zustand: 5.0.14
- React Hook Form: 7.81.0
- date-fns: 4.4.0
- Lucide React: 1.23.0

### Backend
- Java: 21
- Maven: 3.9.x or higher
- Spring Boot: 4.1.0
- Spring Web
- Spring Security
- Spring Validation
- Spring Data JPA
- SQL Server JDBC Driver
- Hibernate / JPA
- Lombok: 1.18.42
- MapStruct: 1.6.3
- H2 Database: runtime (used for local/test support)

### Infrastructure and Runtime
- SQL Server 2019 / SQL Server container
- Docker Desktop or Docker Engine
- Docker Compose v2
- Git

## System Requirements

### Frontend Requirements
- Operating System: Windows 10/11, macOS, Ubuntu/Linux
- Node.js 24.x
- npm 10.x or newer
- Modern browser: Chrome, Edge, Firefox
- Minimum RAM: 4 GB
- Minimum free disk space: 5 GB

### Backend Requirements
- JDK 21
- Maven 3.9.x
- SQL Server 2019 or compatible SQL Server container
- Minimum RAM: 4 GB
- Minimum free disk space: 8 GB
- Docker (recommended when using Compose)

## Project Structure

```text
.
├── backend/
│   ├── Dockerfile
│   ├── mvnw
│   ├── pom.xml
│   ├── src/
│   └── target/
├── frontend/
│   ├── Dockerfile
│   ├── package.json
│   ├── vite.config.ts
│   ├── src/
│   └── public/
├── docker-compose.yml
├── README.md
├── README_EN.md
└── postman/
```

## Quick Start with Docker Compose

The repository includes a ready-to-use `docker-compose.yml` file at the root. This is the easiest way to run the entire system.

### Step 1: Clone the repository

```bash
git clone <repository-url>
cd MockProject_062026_Nhom1
```

### Step 2: Verify Docker is installed and running

```bash
docker --version
docker compose version
```

If you are using Docker Desktop on Windows or macOS, make sure the application is running before continuing.

### Step 3: Build and start all services

```bash
docker compose up --build -d
```

This command will:
- build the backend from `backend/Dockerfile`
- build the frontend from `frontend/Dockerfile`
- start the SQL Server database
- start the API and frontend services

### Step 4: Check the running containers

```bash
docker compose ps
```

You should see services similar to:
- `eldercare-db`
- `eldercare-backend`
- `eldercare-frontend`

### Step 5: Access the application

- Frontend: http://localhost:5173
- Backend API: http://localhost:8080
- SQL Server: localhost:1433

### Step 6: View logs if needed

```bash
docker compose logs -f backend
docker compose logs -f frontend
docker compose logs -f db
```

### Step 7: Stop the system

```bash
docker compose down
```

If you also want to remove database data:

```bash
docker compose down -v
```

## Local Development Setup

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Default local development URL:
- http://localhost:5173

### Backend

Requirements: JDK 21 and Maven installed.

```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

Backend default URL:
- http://localhost:8080

## Default Environment Configuration

The current local Compose setup uses the following settings:

- Database name: `elder_care_21_jul`
- Username: `sa`
- Password: `MatKhauCuaBan123!`
- Backend port: `8080`
- Frontend port: `5173`

> These values are intended for local/demo use. For production or shared environments, use safer credentials and environment-based configuration.

## Notes

- The frontend is configured to serve through Nginx and is exposed on port 5173.
- The backend uses Spring Boot 4.1.0 and Java 21, so the local machine must meet the required runtime environment.
- If you change the API base URL or CORS settings, update the relevant configuration in the backend and frontend configuration files.

## Contribution

Contributions are welcome. Please follow your team workflow when creating pull requests or updating environment variables.

## License

This project is distributed under the MIT license.
