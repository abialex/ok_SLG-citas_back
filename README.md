# 🏥 SLG Citas - Sistema de Gestión de Citas Médicas

## 📋 Descripción del Proyecto

**SLG Citas** es una aplicación backend robusta desarrollada en Java para la gestión integral de citas médicas. El sistema permite administrar citas, doctores, pacientes, horarios de atención y ubicaciones de manera eficiente y segura.

## ✨ Características Principales

- 🔐 **Gestión de Usuarios y Roles**: Sistema de autenticación y autorización basado en roles
- 📅 **Gestión de Citas**: Programación, modificación y cancelación de citas médicas
- 👨‍⚕️ **Administración de Doctores**: Gestión completa de información médica y horarios
- 📍 **Gestión de Ubicaciones**: Control de lugares de atención y direcciones
- ⏰ **Horarios de Atención**: Configuración flexible de disponibilidad médica
- 📱 **API RESTful**: Interfaz moderna y documentada para integración con frontend
- 🗄️ **Base de Datos Relacional**: PostgreSQL con JPA/Hibernate para persistencia robusta

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 11** - Lenguaje de programación principal
- **Spring Boot 2.6.7** - Framework de desarrollo empresarial
- **Spring HATEOAS** - Implementación de REST con hipermedia
- **JPA/Hibernate 5.4.0** - ORM para persistencia de datos
- **Maven** - Gestión de dependencias y build

### Base de Datos
- **PostgreSQL 42.3.1** - Base de datos relacional robusta
- **JPA** - API de persistencia Java

### Utilidades
- **Gson** - Serialización/deserialización JSON
- **Retrofit2** - Cliente HTTP para APIs
- **SLF4J** - Framework de logging
- **JSON** - Procesamiento de datos JSON

## 🏗️ Arquitectura del Sistema

```
src/
├── main/
│   ├── java/
│   │   ├── Controller/          # Punto de entrada de la aplicación
│   │   ├── Entidades/           # Modelos de datos JPA
│   │   ├── EntidadesSettings/   # Configuraciones de entidades
│   │   ├── ServicioRest/        # Controladores REST API
│   │   └── Util/                # Utilidades y helpers
│   └── resources/
│       ├── application.properties # Configuración de la aplicación
│       └── META-INF/
│           └── persistence.xml   # Configuración JPA
```

## 📊 Modelo de Datos


## 📝 Logs

El sistema genera logs detallados en:
- `var/log/CitasSLG.Log.txt` - Logs de la aplicación
- Consola - Logs de Spring Boot
- Niveles configurables por paquete

