🚀 Getting Started
🔧 Prerequisites
Ensure you have the following installed:

Java 17+

Maven or Gradle

MongoDB (running locally or via cloud service)

An IDE like IntelliJ or VS Code

A valid Gmail App Password (for email verification)

💻 Installation
Clone the repository

git clone https://github.com/AakashShah07/Spring-Security-JWT.git
cd Spring-Security-JWT
Configure the environment

Replace sensitive properties using environment variables or a .env file (not committed):


MONGO_HOST=localhost
MONGO_PORT=27017
MONGO_DB=User_Security
JWT_SECRET=your_secure_jwt_key
JWT_EXPIRATION=3600000
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
⚠️ Do not expose credentials. Use @Value to load environment vars in Spring Boot.

Run the application

./mvnw spring-boot:run
📦 Usage
Once the app is running, the following endpoints are available:

🔐 Authentication Routes
Method	Endpoint	Description
POST	/register	Register a new user
POST	/verify-email	Email verification
POST	/login	User login and JWT generation

🛡️ Secured User Endpoints
Method	Endpoint	Access Role	Description
GET	/user/data	USER	Fetch user info
GET	/vaidya/data	VAIDYA	Vaidya-only endpoint

⚠️ Access to endpoints is controlled by JWT + Role-based access.

🧪 Testing
You can test the API using:

🔸 Postman / Thunder Client

🔸 Use the included Swagger documentation (optional, if configured)

🔸 JWT should be passed in headers as:

http

Authorization: Bearer <your_token>
🧠 Features
✅ Spring Boot 3.2

✅ MongoDB integration for data storage

✅ Stateless JWT authentication

✅ Email verification via SMTP (Gmail)

✅ Role-based access control (USER, VAIDYA)

✅ Secure endpoint protection

✅ Clean codebase with modular structure

