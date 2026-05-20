# Nuvora

## Descripción del Proyecto

Nuvora es un sistema de gestión de finanzas personales enfocado en ayudar a los jóvenes a mejorar la administración de su dinero de manera práctica, dinámica e interactiva.

La plataforma permite registrar ingresos y gastos, organizar las finanzas mediante categorías, establecer metas de ahorro dinámicas y generar presupuestos automáticos adaptados al estilo de vida del usuario. Además, incorpora módulos de educación financiera para fortalecer los conocimientos económicos de los usuarios y fomentar hábitos financieros saludables.

Uno de los componentes principales del sistema es su asistente virtual impulsado por inteligencia artificial mediante Gemini AI, el cual brinda recomendaciones financieras personalizadas, apoyo en la creación de presupuestos y orientación según los hábitos de gasto e ingresos registrados en la plataforma.

El proyecto está dividido en diferentes componentes de desarrollo, incluyendo frontend, backend y base de datos, permitiendo una arquitectura organizada y escalable.

## Integrantes:
- Alejandra Gonzalez
- Sara Gómez 
---

# Tecnologías Utilizadas

## Backend

El backend del sistema fue desarrollado utilizando tecnologías enfocadas en seguridad, escalabilidad y manejo eficiente de datos:

* Java
* Spring Boot
* Maven
* Spring Security
* JWT (JSON Web Token)
* Hibernate
* JPA

## Frontend

La interfaz de usuario fue desarrollada buscando una experiencia moderna, dinámica y amigable para el usuario:

* Angular
* TypeScript
* HTML5
* CSS3

## Base de Datos y Servicios Externos

* MySQL como base de datos principal
* Firebase para la integración del chat con inteligencia artificial
* Gemini AI para el funcionamiento del asistente virtual inteligente

---

## Instalación y Ejecución del Proyecto
Antes de ejecutar el proyecto es necesario instalar las siguientes herramientas:

## Backend:
- Java JDK 17 o superior
- Maven
- MySQL Server

### Clonar el proyecto:
Al realizar la instalacion de intellij es necesario darle click al boton de "Clone Repository" para pasar el proyecto de manera correcta
<img width="1179" height="144" alt="Captura de pantalla 2026-05-19 192624" src="https://github.com/user-attachments/assets/556bb37a-2f7e-4071-9eaa-e422d1b2f25e" />

Seguido a esto se ingresa la url de git hub
<img width="1187" height="262" alt="Captura de pantalla 2026-05-19 192706" src="https://github.com/user-attachments/assets/ce5b0466-0bb5-408d-b8ae-3db753bcc27a" />

### Configurar el archivo:
src/main/resources/application.properties

Ejemplo:
spring.application.name=backend-finanzas

spring.datasource.url=jdbc:mysql://localhost:3306/nuvora_finanzas?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Bogota
spring.datasource.username=root
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

### Instalar dependencias:

- mvn clean install

### Ejecutar el backend:
Para ejecutar el backend, basta con presionar el botón de reproducción (triángulo verde) ubicado junto al nombre "backendFinanzasApplication”
<img width="314" height="31" alt="Captura de pantalla 2026-05-19 193057" src="https://github.com/user-attachments/assets/f7ba1c06-d392-4112-92aa-df112af22ffc" />

### Servidor backend:

- http://localhost:8080

-- 

## Frontend:
- Node.js
- npm
- Angular CLI

## Instalación de Angular CLI:
- npm install -g @angular/cli

### Verificación:
Para corroborar la instalacion es necesario colocar los siguientes comandos en la terminal de visual studio code
- node -v
- npm -v
- ng version

<img width="1463" height="907" alt="Captura de pantalla 2026-05-19 204624" src="https://github.com/user-attachments/assets/8a73fa2c-ecec-4269-b16c-1d73ad7bb8ef" />

### Clonar el Proyecto
git clone https://github.com/alejaagf11/NuvoraProject.git

Ingresar a la carpeta del proyecto para visualizarlo:
- cd frontend
<img width="1920" height="1008" alt="Captura de pantalla 2026-05-19 164051" src="https://github.com/user-attachments/assets/f4506c5c-73bb-4207-acb1-b35142886060" />

### Ejecutar Frontend:
Luego de ingresar a la carpeta, corres el Frontend con el siguiente comando
- ng serve
<img width="1477" height="477" alt="image" src="https://github.com/user-attachments/assets/b4d6ec84-49c8-418a-84e0-4432a370ccb8" />


### Configuración de la Base de Datos
Abrir MySQL Workbench.
Crear la base de datos:
- CREATE DATABASE nuvora;
<img width="1237" height="515" alt="Captura de pantalla 2026-05-19 192216" src="https://github.com/user-attachments/assets/60b88546-9782-4915-bffd-0af9272128fb" />

Importar el script SQL correspondiente.

-- 

## Aplicación móvil
- Node.js
- npm
- Angular CLI
- Ionic CLI
- Android Studio

### Comandos principales:
- npm install -g @ionic/cli

Luego, dentro del frontend Angular:
- ng add @ionic/angular

Seguido a esto:
- npm install @capacitor/core @capacitor/cli @capacitor/android

Luego se inicializa Capacitor:

- npx cap init
Ahí va a pedir:

- app name : ingresas "Nuvora"
- app id: ingresas "com.nuvora.app"

Después agregas Android:
- npx cap add android

### Cada vez que quieras probar en Android
Primero construyes Angular:
- ng build

Luego copias a Capacitor:
- npx cap sync android

Y abres Android Studio:
- npx cap open android

<img width="1600" height="840" alt="WhatsApp Image 2026-05-13 at 9 23 29 PM" src="https://github.com/user-attachments/assets/9d3e13e4-394e-430a-9796-6416354b5803" />

# Implementación de Ramas

El proyecto utiliza un sistema de control de versiones basado en ramas para organizar el desarrollo de cada componente de la aplicación.

## Ramas utilizadas

### frontend

Contiene todo el desarrollo de la interfaz gráfica del sistema, incluyendo componentes visuales, vistas, formularios, consumo de APIs y lógica del cliente desarrollada en Angular y TypeScript.

### backend

Incluye toda la lógica del servidor, configuración de seguridad, autenticación con JWT, manejo de entidades, servicios, controladores y conexión con la base de datos desarrollada en Spring Boot.

### database

Contiene la estructura y administración de la base de datos, incluyendo scripts SQL, relaciones entre tablas y configuración necesaria para el almacenamiento de la información del sistema.

Actualmente, las ramas se encuentran separadas durante la fase de desarrollo para facilitar la organización y el trabajo modular del proyecto antes de realizar la integración final en la rama principal (main).

---

# Funcionalidades Principales

## Gestión de Usuarios

* Registro de usuarios
* Inicio de sesión seguro
* Autenticación y autorización mediante JWT

## Gestión Financiera

* Registro de ingresos
* Registro de gastos
* Organización mediante categorías financieras
* Generación automática de presupuestos
* Administración de metas de ahorro dinámicas

## Inteligencia Artificial

* Asistente virtual financiero integrado
* Recomendaciones financieras personalizadas
* Generación de presupuestos según hábitos y estilo de vida
* Interacción mediante IA utilizando Gemini

## Educación Financiera

* Módulos de aprendizaje financiero
* Lecciones educativas interactivas
* Seguimiento del progreso del usuario dentro de los cursos

---

# Base de Datos

La base de datos del sistema fue diseñada en MySQL siguiendo un modelo relacional que permite organizar la información financiera y educativa de los usuarios de manera estructurada y segura.

## Principales entidades del sistema

### Usuario

Almacena la información general de cada usuario registrado en la plataforma, incluyendo credenciales y datos básicos.

### Transaccion

Registra los movimientos financieros realizados por el usuario, tanto ingresos como gastos.

### Categoria

Permite clasificar las transacciones financieras en diferentes categorías para una mejor organización económica.

### Presupuesto

Gestiona los presupuestos automáticos generados para cada usuario según sus ingresos y hábitos financieros.

### MetasAhorro

Administra las metas de ahorro creadas por los usuarios y permite realizar seguimiento al progreso alcanzado.

### Abono

Registra los aportes realizados por los usuarios hacia sus metas de ahorro.

### ModuloAprendizaje

Contiene los módulos educativos relacionados con educación financiera.

### Leccion

Almacena las lecciones pertenecientes a cada módulo de aprendizaje.

### ProgresoLeccionUsuario

Permite realizar seguimiento al avance del usuario dentro de las lecciones y módulos educativos.

---

# API Endpoints

La API de Nuvora está organizada por módulos funcionales y cuenta con autenticación y autorización basada en JWT y roles de usuario.

## Autenticación

| Método | Endpoint             | Acceso  | Descripción                        |
| ------ | -------------------- | ------- | ---------------------------------- |
| POST   | `/api/auth/register` | Público | Registrar un nuevo usuario         |
| POST   | `/api/auth/login`    | Público | Iniciar sesión y generar token JWT |

---

# Gestión de Categorías

| Método | Endpoint                               | Acceso              | Descripción                        |
| ------ | -------------------------------------- | ------------------- | ---------------------------------- |
| POST   | `/api/categorias/register`             | Usuario autenticado | Registrar una categoría financiera |
| GET    | `/api/categorias/list`                 | Usuario autenticado | Obtener listado de categorías      |
| PUT    | `/api/categorias/update/{categoriaId}` | Usuario autenticado | Actualizar categoría               |
| DELETE | `/api/categorias/delete/{categoriaId}` | Usuario autenticado | Eliminar categoría                 |

---

# Gestión de Lecciones

| Método | Endpoint                            | Acceso              | Descripción                    |
| ------ | ----------------------------------- | ------------------- | ------------------------------ |
| POST   | `/api/lecciones/register`           | Administrador       | Registrar una lección          |
| GET    | `/api/lecciones/modulo/{moduloId}`  | Usuario autenticado | Obtener lecciones de un módulo |
| GET    | `/api/lecciones/{leccionId}`        | Usuario autenticado | Obtener una lección específica |
| PUT    | `/api/lecciones/update/{leccionId}` | Administrador       | Actualizar lección             |
| DELETE | `/api/lecciones/delete/{leccionId}` | Administrador       | Eliminar lección               |

---

# Gestión de Metas de Ahorro

| Método | Endpoint                                 | Acceso              | Descripción                 |
| ------ | ---------------------------------------- | ------------------- | --------------------------- |
| POST   | `/api/metasAhorro/register`              | Usuario autenticado | Crear meta de ahorro        |
| GET    | `/api/metasAhorro/list`                  | Usuario autenticado | Obtener metas de ahorro     |
| GET    | `/api/metasAhorro/list/{metaAhorroId}`   | Usuario autenticado | Obtener una meta específica |
| PUT    | `/api/metasAhorro/update/{metaAhorroId}` | Usuario autenticado | Actualizar meta             |
| DELETE | `/api/metasAhorro/delete/{metaAhorroId}` | Usuario autenticado | Eliminar meta               |
| POST   | `/api/metasAhorro/abonar/{metaAhorroId}` | Usuario autenticado | Realizar abono a una meta   |

---

# Gestión de Módulos Educativos

| Método | Endpoint                         | Acceso              | Descripción                   |
| ------ | -------------------------------- | ------------------- | ----------------------------- |
| POST   | `/api/modulos/register`          | Administrador       | Registrar módulo educativo    |
| GET    | `/api/modulos/list`              | Usuario autenticado | Obtener módulos educativos    |
| GET    | `/api/modulos/progreso`          | Usuario autenticado | Consultar progreso de módulos |
| GET    | `/api/modulos/{moduloId}`        | Usuario autenticado | Obtener módulo específico     |
| PUT    | `/api/modulos/update/{moduloId}` | Administrador       | Actualizar módulo             |
| DELETE | `/api/modulos/delete/{moduloId}` | Administrador       | Eliminar módulo               |

---

# Gestión de Presupuestos

| Método | Endpoint                       | Acceso              | Descripción                                |
| ------ | ------------------------------ | ------------------- | ------------------------------------------ |
| POST   | `/api/presupuesto/generate`    | Usuario autenticado | Generar presupuesto automático             |
| POST   | `/api/presupuesto/generate-ai` | Usuario autenticado | Generar presupuesto usando IA              |
| POST   | `/api/presupuesto/chat`        | Usuario autenticado | Consultar asistente financiero inteligente |

---

# Progreso Educativo

| Método | Endpoint                              | Acceso              | Descripción                     |
| ------ | ------------------------------------- | ------------------- | ------------------------------- |
| POST   | `/api/progreso/completar/{leccionId}` | Usuario autenticado | Marcar lección como completada  |
| GET    | `/api/progreso/mis-lecciones`         | Usuario autenticado | Consultar progreso de lecciones |
| GET    | `/api/progreso/modulos`               | Usuario autenticado | Consultar progreso de módulos   |

---

# Gestión de Transacciones

| Método | Endpoint                                     | Acceso              | Descripción                         |
| ------ | -------------------------------------------- | ------------------- | ----------------------------------- |
| POST   | `/api/transacciones/register`                | Usuario autenticado | Registrar transacción               |
| GET    | `/api/transacciones/list`                    | Usuario autenticado | Obtener listado de transacciones    |
| GET    | `/api/transacciones/tipo/{tipo}`             | Usuario autenticado | Filtrar transacciones por tipo      |
| GET    | `/api/transacciones/categoria/{categoriaId}` | Usuario autenticado | Filtrar transacciones por categoría |
| PUT    | `/api/transacciones/update/{transaccionId}`  | Usuario autenticado | Actualizar transacción              |
| DELETE | `/api/transacciones/delete/{transaccionId}`  | Usuario autenticado | Eliminar transacción                |
| GET    | `/api/transacciones/saldo`                   | Usuario autenticado | Consultar saldo financiero          |

---

# Gestión de Usuarios

| Método | Endpoint              | Acceso              | Descripción                                 |
| ------ | --------------------- | ------------------- | ------------------------------------------- |
| GET    | `/api/usuario/me`     | Usuario autenticado | Obtener información del usuario autenticado |
| PUT    | `/api/usuario/update` | Usuario autenticado | Actualizar información del usuario          |
| DELETE | `/api/usuario/delete` | Usuario autenticado | Eliminar cuenta del usuario                 |

---

# Administración de Usuarios

| Método | Endpoint                                | Acceso        | Descripción                  |
| ------ | --------------------------------------- | ------------- | ---------------------------- |
| GET    | `/api/usuario/admin/{usuarioId}`        | Administrador | Consultar usuario específico |
| PUT    | `/api/usuario/admin/update/{usuarioId}` | Administrador | Actualizar usuario           |
| DELETE | `/api/usuario/admin/delete/{usuarioId}` | Administrador | Eliminar usuario             |

---

# Seguridad y Control de Acceso

El sistema implementa autenticación y autorización mediante JWT utilizando Spring Security.

Los endpoints están protegidos según roles y permisos:

* Endpoints públicos para autenticación
* Endpoints protegidos para usuarios autenticados
* Endpoints exclusivos para administradores
* Control de acceso mediante roles (ADMIN y USER)
* Protección de rutas mediante filtros JWT

Esto permite garantizar la seguridad de la información financiera y educativa almacenada en la plataforma.

