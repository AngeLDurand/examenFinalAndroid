# 📱 **LevelUp Gamer — Aplicación Móvil Android**

Aplicación móvil desarrollada en **Kotlin + Jetpack Compose**, conectada a un backend en **Spring Boot**, diseñada para simular una tienda gamer con gestión de productos, carrito de compras, direcciones, órdenes y perfil de usuario.

---

## 👥 **Integrantes**

* **Angel Durand** — Desarrollador de la app móvil y microservicios

---

## **Importante**

* **Rama main** — En rama main se encuentra la versión final con el archivo .apk 

---

# 🚀 **Funcionalidades principales**

### 🛒 **Productos**

* Listado de productos.
* Filtro por categorías.
* Agregar productos al carrito.
* Indicador visual de cantidad en carrito.

### 🧺 **Carrito de compras**

* Sumar/restar unidades.
* Mostrar total actualizado.
* Vaciar carrito al completar compra.

### 📦 **Direcciones de envío**

* Ver direcciones guardadas.
* Crear nueva dirección.
* Seleccionar dirección para compra.

### 💳 **Procesar compra**

* Resumen del pedido.
* Envío de la orden.
* Mensaje visual de compra exitosa.

### 👤 **Perfil del usuario**

* Ver foto, nombre y correo.
* Cambiar foto desde cámara, galería o API externa.
* Ver historial de compras.
* Cambiar contraseña.

### 🔐 **Autenticación**

* Login con validación.
* Persistencia del token con DataStore.

---

# 🌐 **Endpoints utilizados**

## 🟦 **Backend propio (Spring Boot)**

### 🔗 Producción (Render)

```
https://backend-android-4x09.onrender.com/api/v1
```

### 🔗 Localhost`

```
http://localhost:8080/api/v1
```

### 🔐 Registro Usuario

```
POST /users/register
```

### 🔐 Autenticación

```
POST /users/login
```

### 🔐 Cambiar Password

```
PATCH /users/cambiar-password
```


### 🛒 Productos

```
GET /products
```

### 📦 Direcciones

```
GET /addresses
POST /addresses
```

### 🧾 Órdenes

```
POST /orders
GET /orders
```

---

## 🐱 **API externa — TheCatAPI**

Usada para generar foto de perfil aleatoria.

### Obtener una imagen aleatoria

```
GET https://api.thecatapi.com/v1/images/search
```

---

# 🛠️ **Tecnologías utilizadas**

### **Frontend**

* Kotlin
* Jetpack Compose
* Material Design 3
* Navigation Compose
* MVVM + StateFlow
* DataStore
* Retrofit2 + OkHttp
* Coil

### **Backend**

* Spring Boot 3
* Spring Security + JWT
* Oracle Cloud: Autonomous AI Database
* Hibernate
* Maven

---

# 📲 **Instalación y ejecución**

## 1️⃣ Ejecutar backend

Clonar repositorio(solo para prueba en local):

```
git clone https://github.com/AngeLDurand/examenFinalSpringBackend
```

Ejecutar:

```
mvn spring-boot:run
```

## 2️⃣ Ejecutar app en Android Studio

1. Abrir proyecto en Android Studio.
2. Ejecutar en emulador o dispositivo.
3. Verificar conexión con backend.

## 3️⃣ Instalar APK firmado en un celular

Archivo APK:

```
app/release/app-release.apk
```


---

# 📁 **Código fuente incluido**

✔ Microservicios Spring Boot
✔ App móvil Android completa
✔ Lógica MVVM, repositorios, ViewModels
✔ APK firmado


---

# 🗂️ Estructura del proyecto móvil

```
app/
 └── src/main/java/com/example/levelup/
      ├── dto/
      ├── model/
      ├── navigation/
      ├── remote/
      ├── repository/
      ├── session/
      ├── ui/
      ├── viewmodel/
      └── MainActivity.kt
```

---

# 🎯 Objetivo del proyecto

Construir una app móvil moderna, modular, escalable y conectada a un backend real, aplicando buenas prácticas de arquitectura, diseño visual, persistencia y consumo de APIs externas.
