# Enterprise User & Order Management System

A Spring Boot backend system for managing users, roles, products, and orders.

## 🚀 Quick Start

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- MySQL 8.0+ (or MySQL 5.7+)

### MySQL Setup

#### 1. Install MySQL (if not already installed)

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

**macOS:**
```bash
brew install mysql
brew services start mysql
```

**Windows:**
Download and install from [MySQL Downloads](https://dev.mysql.com/downloads/mysql/)

#### 2. Start MySQL Service

**Linux:**
```bash
sudo systemctl start mysql
sudo systemctl enable mysql
```

**macOS:**
```bash
brew services start mysql
```

**Windows:**
MySQL service should start automatically after installation.

#### 3. Create Database

Login to MySQL:
```bash
mysql -u root -p
```

Then create the database:
```sql
CREATE DATABASE enterprise_db;
SHOW DATABASES;
EXIT;
```

#### 4. Update Application Properties

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/enterprise_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

**Important:** Replace `YOUR_MYSQL_PASSWORD` with your actual MySQL root password.

#### 5. Verify MySQL Connection

Test connection:
```bash
mysql -u root -p -e "USE enterprise_db; SHOW TABLES;"
```

### Build and Run

```bash
# Navigate to project directory
cd enterprise-user-order-system

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will:
- ✅ Automatically create database tables (via `spring.jpa.hibernate.ddl-auto=update`)
- ✅ Create ADMIN and USER roles on startup
- ✅ Start on `http://localhost:8080`

### Access Swagger UI

Once running, open:
```
http://localhost:8080/swagger-ui/index.html
```

## 📋 API Endpoints

### Authentication (Public)
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

### Products (Requires Authentication)
- `GET /api/products` - List products (paginated)
- `GET /api/products/search?q=name` - Search products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create product (ADMIN only)
- `PUT /api/products/{id}` - Update product (ADMIN only)
- `DELETE /api/products/{id}` - Delete product (ADMIN only)

### Orders (Requires Authentication)
- `POST /api/orders` - Create order
- `GET /api/orders/me` - Get current user's orders
- `GET /api/orders` - Get all orders (ADMIN only)
- `PUT /api/orders/{id}/status` - Update order status (ADMIN only)

### Admin (ADMIN only)
- `GET /api/admin/users` - Get all users

## 🔐 Testing with Postman

1. **Register a user:**
   ```
   POST http://localhost:8080/api/auth/register
   Content-Type: application/json
   
   {
     "username": "testuser",
     "email": "test@example.com",
     "password": "password123"
   }
   ```

2. **Login:**
   ```
   POST http://localhost:8080/api/auth/login
   Content-Type: application/json
   
   {
     "usernameOrEmail": "testuser",
     "password": "password123"
   }
   ```
   
   Copy the `token` from the response.

3. **Use token in subsequent requests:**
   ```
   Authorization: Bearer <your_token_here>
   ```

## 🗄️ Database Schema

The application automatically creates these tables:
- `users` - User accounts
- `roles` - User roles (ADMIN, USER)
- `products` - Product catalog
- `orders` - Customer orders
- `order_items` - Order line items

## 🔧 Troubleshooting

### Connection Refused
- Ensure MySQL is running: `sudo systemctl status mysql`
- Check MySQL port: `netstat -an | grep 3306`

### Access Denied
- Verify username/password in `application.properties`
- Reset MySQL password if needed:
  ```sql
  ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_password';
  FLUSH PRIVILEGES;
  ```

### Database Doesn't Exist
- Create it manually: `CREATE DATABASE enterprise_db;`

### Port Already in Use
- Change port in `application.properties`: `server.port=8081`

## 📝 Notes

- Roles (ADMIN, USER) are automatically created on first startup
- JWT tokens expire after 1 hour (configurable in `application.properties`)
- Database tables are auto-created/updated via Hibernate
- For production, change `spring.jpa.hibernate.ddl-auto` to `validate` or `none`
