# 🏦 Challenge Santander RHT
#### Microservicio, arquitectura REST, que expone un CRUD completo sobre entidades bancarias.

## ✨ Funcionalidades Principales

El `BankingEntityService` proporciona las siguientes operaciones, asegurando la validación de negocio y la verificación de duplicados para mantener la integridad de los datos:

* **Creación (`create`):** Registra una nueva entidad (ej. `CUSTOMER`, `SUPPLIER`, `BRANCH`) previa validación de datos y chequeo de duplicados.
* **Lectura por ID (`getById`):** Recupera una entidad específica por su identificador único.
* **Lectura por Código (`getByCode`):** Recupera una entidad por su código de negocio único.
* **Listado por Tipo (`getByType`):** Filtra y devuelve entidades según su `EntityType`.
* **Actualización (`update`):** Modifica los datos de una entidad existente, manteniendo su ID y gestionando su `updatedAt`.
* **Eliminación (`delete`):** Elimina permanentemente una entidad por su ID.

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 21
* **Compilador/Build:** Apache Maven
* **Testing:** JUnit 5, Mockito, AssertJ
* **Arquitectura:** Domain-Driven Design (DDD) con puertos y adaptadores.

## ⚙️ Requisitos Previos

Tener instalados los siguientes componentes:

1.  **Java Development Kit (JDK):** Versión 21 o superior.
2.  **Apache Maven:** Versión 3.9.1 o superior.

## 🚀 Instalación y Ejecución

### 1. Clonar el Repositorio

```bash
git clone https://github.com/dmarra854/challengeSantanderRHT.git
```

### 2. Construir el Proyecto
```
mvn clean install -DskipTests
```

### 3. Ejecutar la Aplicación
```
mvn clean install -DskipTests
```

### Pruebas de Integración y Documentación (Swagger UI)

Con el servicio corriendo (default puerto 9091), acceder a: http://localhost:9091/swagger-ui/index.html#/.