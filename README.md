# Online Library Microservices

Proyecto semestral desarrollado con arquitectura de microservicios utilizando Spring Boot, Spring Cloud y JWT Authentication.

## Tecnologías utilizadas

* Java 17
* Spring Boot
* Spring Cloud
* Spring Security
* JWT
* Eureka Server
* API Gateway
* MySQL
* JPA / Hibernate
* Flyway
* Maven
* GitHub

---

# Arquitectura del proyecto

El sistema está dividido en múltiples microservicios independientes:

* auth-service
* book-service
* cart-service
* order-service
* inventory-service
* payment-service
* review-service
* notification-service
* recommendation-service
* user-service

Además incluye:

* Eureka Server
* API Gateway

---

# Funcionalidades principales

## Auth Service

* Registro de usuarios
* Login
* Generación de JWT
* Seguridad centralizada

## Book Service

* Gestión de libros
* CRUD de libros
* Endpoints protegidos

## Cart Service

* Gestión de carrito de compras

## Order Service

* Gestión de órdenes

## Payment Service

* Gestión de pagos

## Inventory Service

* Control de stock

## Review Service

* Gestión de reseñas

## Notification Service

* Gestión de notificaciones

## Recommendation Service

* Recomendaciones de libros

## User Service

* Gestión de usuarios

---

# Seguridad

El proyecto implementa autenticación JWT centralizada mediante API Gateway.

Flujo:

1. Usuario inicia sesión en auth-service.
2. auth-service genera JWT.
3. API Gateway valida el token.
4. Gateway permite o bloquea acceso a microservicios protegidos.

---

# Eureka Server

Eureka Server permite el descubrimiento y registro dinámico de microservicios.

Funciones:

* Registro automático
* Descubrimiento de servicios
* Escalabilidad
* Balanceo de carga

---

# API Gateway

API Gateway centraliza:

* rutas
* seguridad
* validación JWT
* acceso a microservicios

---

# Flyway

Se implementan migraciones automáticas para creación de tablas y trazabilidad de base de datos.

Ejemplo:

* V1__init.sql

---

# Validaciones y manejo de errores

El proyecto implementa:

* DTO
* Validaciones
* Exception Handler global
* Logs
* Respuestas HTTP adecuadas

---

# Cómo ejecutar el proyecto

## 1. Levantar Eureka Server

## 2. Levantar API Gateway

## 3. Levantar microservicios

## 4. Configurar MySQL

Crear bases de datos:

* auth_db
* book_db
* order_db

## 5. Ejecutar pruebas en Postman

---

# Ejemplo Login

POST:
http://localhost:8080/auth/login

Body:
{
"email":"[admin@gmail.com](mailto:admin@gmail.com)",
"password":"123456"
}

---

# Ejemplo Bearer Token

Authorization:
Bearer Token

---

# Autor

Proyecto desarrollado por:
Brayan Stiven

Asignatura:
Arquitectura de Microservicios
