# Learn Hub backend

## Requirements
- JDK 25 (latest LTS) or later
- Apache Maven 3.9+
- MySQL 8 only if you choose the `mysql` profile; the default local profile is file-based H2 so the app can be started without MySQL.

## Start locally (default H2 database)
From this folder, run:

```cmd
mvn spring-boot:run
```

The API listens on `http://127.0.0.1:8080`. The H2 data file is stored under `data/learnhub`. Registration data is stored in that H2 database, not MySQL, while the default profile is active.

## Use MySQL instead
1. Create a MySQL database and user with permissions for `learn_hub`. Do not use a guessed password.
2. Set environment variables in the same Windows Command Prompt before starting the backend:

```cmd
set SPRING_PROFILES_ACTIVE=mysql
set DB_URL=jdbc:mysql://localhost:3306/learn_hub?createDatabaseIfNotExist=true^&useSSL=false^&allowPublicKeyRetrieval=true^&serverTimezone=UTC
set DB_USERNAME=your_mysql_username
set DB_PASSWORD=your_actual_mysql_password
mvn spring-boot:run
```

Use your actual database credentials. Do not commit passwords to source control.

## Seeded demo accounts
When the selected database is empty, the seeder creates these local demo users:
- `student@learnhub.com` / `Student@123`
- `instructor@learnhub.com` / `Instructor@123`
- `admin@learnhub.com` / `Admin@123`

Passwords are stored as BCrypt hashes. If the database already contains any users, the current seeder does not add the demo accounts.
