# 🚗 Concesionarios BaCa - Android

Este proyecto es una aplicación móvil para la **compra y venta de coches**. Los usuarios pueden explorar vehículos disponibles, comprar coches y gestionar su perfil. La aplicación está desarrollada en **Kotlin** con **Jetpack Compose** y usa **Strapi** como backend.

## 📌 Características Principales
✅ Autenticación de usuarios con **JWT Tokens**.  
✅ Consulta de coches disponibles mediante la API de **Strapi**.  
✅ **Compra de coches** y asignación automática del `customerId`.  
✅ **Filtrado de coches comprados**, evitando que aparezcan en el catálogo.  
✅ Manejo avanzado de **imágenes de coches** al actualizar en la API.  
✅ Integración con **Retrofit** para llamadas a la API.  

## 🛠 Tecnologías Usadas
- **Kotlin**
- **Jetpack Compose**
- **Retrofit** (para consumo de APIs)
- **Strapi** (Backend Headless CMS)
- **Room Database** (para persistencia local)
- **Hilt** (para inyección de dependencias)

## 🔧 Configuración del Proyecto
1. Clonar el repositorio:
   git clone https://github.com/usuario/Concesionarios-BaCa-Android.git
   cd Concesionarios-BaCa-Android

2. Abrir el proyecto en Android Studio.

3. Ejecutar la aplicación en un dispositivo o emulador.

---

## 🛠 Estructura del Código
📂 `data/repository/CarRepository.kt` → Manejo de coches en la API.  
📂 `data/repository/AuthRepository.kt` → Autenticación y gestión de usuarios.  
📂 `data/api/ApiService.kt` → Definición de endpoints de Strapi.  
📂 `data/mapping/CarMapping.kt` → Conversión de datos entre API y aplicación.  
📂 `ui/carDetails/CarDetailsViewModel.kt` → Lógica de compra de coches.  

---

## 🚀 Flujo de Compra de un Coche
1️⃣ El usuario selecciona un coche en el catálogo.  
2️⃣ Se verifica si el usuario tiene un `customerId` en la API.  
3️⃣ Si el coche está disponible, se actualiza el `customerId`.  
4️⃣ La imagen del coche **se mantiene** en la actualización.  
5️⃣ **El coche desaparece del catálogo**, ya que ahora tiene un dueño.  

---

## ⚠ Desafíos y Soluciones
### 🔍 Problema: La imagen del coche desaparecía al comprar
✅ Solución: Se garantizó que `picture` se mantuviera en `PictureData`.  

### 🔍 Problema: Los coches comprados aún aparecían en el catálogo
✅ Solución: Se filtraron coches en `getCars()` con `customerId == null`.  

### 🔍 Problema: Error de tipos en Retrofit al actualizar el `customerId`
✅ Solución: Se convirtió `customerId` en `{ "id": customerId }` antes de enviarlo a la API.  

---

## 📌 Ejemplo de Uso
val carRepository: CarRepository = CarRepository(apiService, carDao)
val availableCars = carRepository.getCars() // Obtiene solo coches disponibles

---

## 📞 Contacto
Si tienes alguna duda o sugerencia, puedes contactarme en **[tu email]** o abrir un **issue en GitHub**.  
¡Gracias por visitar el proyecto! 🚗✨
