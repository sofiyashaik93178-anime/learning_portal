# Learn Hub — Online Learning Portal

This project contains a React + Vite frontend and a Java Spring Boot backend.

## Prerequisites
- Node.js 20 or newer and npm
- JDK 25 or newer
- Apache Maven 3.9 or newer
- MySQL 8 only if using the `mysql` Spring profile; local development defaults to file-based H2

## Run on Windows

### 1. Start the backend
Open a terminal in `backend` and run:

```cmd
mvn clean test
mvn spring-boot:run
```

The backend API listens on `http://localhost:8080` by default. The default `h2` profile stores local data in `backend/data/learnhub`.

### 2. Start the frontend
Open a second terminal in `frontend` and run:

```cmd
npm install
npm run build
npm run dev
```

Open `http://localhost:5173`.

The Vite development proxy forwards `/api` calls to the backend. Start the backend first; otherwise the browser may show connection-refused errors.

## MySQL profile
Set actual local database credentials in the same terminal before starting the backend. Never commit real passwords or JWT secrets.

```cmd
set SPRING_PROFILES_ACTIVE=mysql
set DB_URL=jdbc:mysql://localhost:3306/learn_hub?createDatabaseIfNotExist=true^&useSSL=false^&allowPublicKeyRetrieval=true^&serverTimezone=UTC
set DB_USERNAME=your_mysql_username
set DB_PASSWORD=your_actual_mysql_password
set JWT_SECRET=replace_with_a_long_random_secret
mvn spring-boot:run
```

The application configuration accepts `DB_USERNAME`, `DB_PASSWORD`, `DB_URL`, and `JWT_SECRET` environment variables. The fallback values are for local development only and must not be used in a public deployment.

## Seeded demo accounts
On a fresh database, the seeder creates:
- `student@learnhub.com` / `Student@123`
- `instructor@learnhub.com` / `Instructor@123`
- `admin@learnhub.com` / `Admin@123`

Passwords are stored as BCrypt hashes. Current seed data is initialized only when the database has no users. The programming catalog includes C, C++, Java, Python, JavaScript, HTML & CSS, SQL & MySQL, Data Structures & Algorithms, DBMS, OOP, Operating Systems, Computer Networks, Computer Organization and Architecture, Software Engineering, Backend Development, React.js, Spring Boot, Git and GitHub, and Data Science Fundamentals.

## Current scope and verification
The existing application provides student, instructor, and admin routes, course/module/lesson data, quizzes, authentication, and progress-related APIs. Course content in this starter remains introductory and must be expanded before it can be described as exhaustive A-to-Z training. A secure isolated code execution service and real video content are not bundled by default; configure a trusted provider before enabling those capabilities.

Build verification is environment-dependent. Run the commands above on your computer and review the terminal results. A ZIP package alone does not mean the application has passed runtime or integration tests.
