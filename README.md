# Guárdalo Two

## Autores

- [@fernandagarayn](https://www.github.com/fernandagarayn)

## Índice

- [Autores](#autores)
- [Descripción](#descripción)
   - [Backend](#backend)
   - [Frontend](#frontend)
   - [Clientes API para TeLoLLevo y Music Pro](#clientes-api-para-telollevo-y-music-pro)
- [Instalación](#instalación)
   - [Backend](#backend-1)
      - [Configuración de clientes API para TeLoLLevo y Music Pro](#configuración-de-clientes-api-para-telollevo-y-music-pro)
      - [Configuración de la base de datos](#configuración-de-la-base-de-datos)
      - [Notas para utilizar MySQL](#notas-para-utilizar-mysql)
   - [Frontend](#frontend-1)
      - [Estructura de Carpetas](#estructura-de-carpetas)
      - [Librerías Externas](#librerías-externas)

## Descripción

`Guárdalo Two` es un proyecto de software completo que consta de un backend y un frontend. El backend está construido con Java, utilizando el marco de trabajo Spring Boot y SQL para la gestión de datos. El frontend, por otro lado, está construido con JavaScript.

El objetivo principal de este proyecto es proporcionar una solución integral para la gestión de bodega y envio de productos a sucursales. Con una arquitectura de backend robusta y un frontend interactivo, `Guárdalo Two` ofrece una experiencia de usuario fluida y eficiente.

### Backend

El backend de `Guárdalo Two` está diseñado para manejar todas las operaciones de datos. Utiliza Java y Spring Boot para proporcionar una API RESTful que permite al frontend interactuar con la base de datos. La base de datos se gestiona con SQL, lo que permite una gestión de datos eficiente y segura.

### Frontend

El frontend de `Guárdalo Two` está diseñado para proporcionar una interfaz de usuario interactiva y fácil de usar. Está construido con JavaScript, lo que permite una interacción dinámica y una experiencia de usuario atractiva.

### Clientes API para TeLoLLevo y Music Pro

Este proyecto incluye dos clientes API para TeLoLLevo y Music Pro. Estos clientes API se utilizan para interactuar con las API de TeLoLLevo y Music Pro, respectivamente. 

De la API de Music Pro se utilizan los servicios de bodega para obtener instrumentos cuando se quiera solicitar productos a ella y posteriormente la API de solicitudes para poder realizar la solicitud de productos a la bodega de Music Pro.

Tanto para Music Pro y Te Lo Llevo, sus apis proporcionan la capacidad de solicitar un transporte para las solicitudes de productos realizadas en Guardalo Two. Estas APIs retornar un código de seguimiento que se utiliza principalmente para obtener el estado de las solitudes de transportes realizadas en desde Guardalo Two.

## Instalación

Antes de proceder con la instalación del backend debemos contar con los siguientes prerequisitos:

- Java 17 o superior
- Maven 3.6.3 o superior
- MySQL 8.0.23 o superior (Opcional)

Para instalar y ejecutar este proyecto, sigue los siguientes pasos:

1. Clona el repositorio en tu máquina local utilizando el siguiente comando en tu terminal:

    ```bash
    git clone https://github.com/fernandagarayn/guardalo-two.git
    ```

2. Navega hasta el directorio del proyecto:

    ```bash
    cd guardalo-two
    ```
3. Ahora tienes una copia local del proyecto y estás listo para instalar las dependencias necesarias para el backend y el frontend. Consulta las secciones correspondientes de este documento para obtener instrucciones detalladas.

### Backend

1. Navegar hasta el directorio del backend: Una vez que hayas clonado el repositorio, navega hasta el directorio del backend con el siguiente comando:
    
     ```bash
     cd backend
     ``` 
2. Instalar las dependencias: Una vez que estés en el directorio del backend, instala las dependencias necesarias con el siguiente comando:
    
    ```bash
    mvn clean install
    ```
   
3. Ejecutar el proyecto: Una vez que hayas instalado las dependencias, ejecuta el proyecto con el siguiente comando:
    
    ```bash
    mvn spring-boot:run
    ```
4. El backend se ejecutará en el puerto 8081. Puedes acceder a la documentación de la API en `http://localhost:8081/swagger-ui.html`. El puerto se puede cambiar en el archivo `application.yml`.

#### Configuración de clientes API para TeLoLLevo y Music Pro

La configuración de los clientes API se realiza mediante una libreria llamada `Feign`, que facilita la construcción de código para conexiones con APIs externas, cuyas propiedades se encuentran en el archivo `application.yml`. 

En la sección feign de del archivo `application.yml`, existen dos clientes Feign configurados: `musicpr`o y `telollevo`. Cada uno de estos clientes tiene una propiedad url que especifica la URL base de la API con la que el cliente Feign interactuará.  Aquí está la sección relevante de tu archivo `application.yml`:

   ```yaml
   feign:
      clients:
         musicpro:
            url: https://musicpro.bemtorres.win/api/v1
         telollevo:
            url: http://192.168.100.27:8080/api/v1
   ```

Si la url base de los clientes API cambia, se debe realizar una actualización la propiedad url correspondiente en el archivo `application.yml`.

#### Configuración de la base de datos

Este proyecto utiliza H2 como sistema de gestión de bases de datos. La configuración de la base de datos se encuentra en el archivo `application.yml` en el directorio `src/main/resources`.

Aquí hay una descripción de las propiedades más importantes:

- `spring.datasource.url`: Esta es la URL de tu base de datos. En este caso, estamos utilizando una base de datos en memoria llamada `g2backend`.
- `spring.datasource.username` y `spring.datasource.password`: Estas son las credenciales para acceder a tu base de datos. En este caso, el nombre de usuario es `sa` y no hay contraseña.
- `spring.datasource.driver-class-name`: Este es el nombre de la clase del controlador de la base de datos. En este caso, estamos utilizando el controlador H2.
- `spring.jpa.hibernate.ddl-auto`: Esta propiedad controla la generación automática del esquema de la base de datos. En este caso, está configurado para `validate`, lo que significa que Hibernate solo validará que las tablas y columnas existan, no las creará.
- `spring.sql.init.mode`: Esta propiedad controla si se deben ejecutar scripts SQL al inicio. En este caso, está configurado para `always`, lo que significa que siempre se ejecutarán los scripts.
- `spring.sql.init.schema-locations` y `spring.sql.init.data-locations`: Estas propiedades especifican la ubicación de los scripts SQL que se deben ejecutar al inicio. En este caso, los scripts están en el directorio `src/main/resources/sql_imports`.

Para configurar la base de datos:

1. No es necesario instalar H2 en tu máquina. El proyecto Spring Boot ya incluye la dependencia de H2 en el archivo `pom.xml`, por lo que se utilizará una base de datos en memoria.
2. Abre el archivo `application.yml` y revisa las propiedades de la base de datos. Asegúrate de que la URL, el nombre de usuario y la contraseña sean correctos para tu configuración.
3. Si necesitas cambiar alguna propiedad, hazlo directamente en el archivo `application.yml`.
4. Una vez que hayas configurado la base de datos, puedes ejecutar el proyecto. Los scripts SQL especificados en `spring.sql.init.schema-locations` y `spring.sql.init.data-locations` se ejecutarán automáticamente al inicio. Los scripts en sql_imports pueden estar organizados de la siguiente manera:
* `V00_00_INIT_TABLES.sql`: Este archivo contiene los comandos SQL para crear las tablas y relaciones en la base de datos. Por lo general, incluye comandos `CREATE TABLE`.
* `V00_01_INITIAL_DATA.sql`: Este archivo contiene los comandos SQL para insertar datos iniciales en las tablas. Por lo general, incluye comandos `INSERT INTO`.

## Notas para utilizar MySQL

Si prefieres utilizar MySQL en lugar de H2, deberás realizar algunos cambios en la configuración de la base de datos:

1. **Dependencia de MySQL**: Asegúrate de que la dependencia de MySQL está presente en tu archivo `pom.xml`. Si no es así, deberás agregarla. Aquí tienes un ejemplo de cómo hacerlo:

   ```xml
   <dependency>
       <groupId>mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
       <version>8.0.23</version>
   </dependency>
   ```

2. Configuración de la base de datos: Deberás actualizar las propiedades de la base de datos en el archivo application.yml para que apunten a tu instancia de MySQL. Aquí tienes un ejemplo de cómo podría ser:

   ```YAML
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/g2backend?useSSL=false&serverTimezone=UTC
       username: tu_usuario
       password: tu_contraseña
       driver-class-name: com.mysql.cj.jdbc.Driver
   ```
   
3. Creación de la base de datos: Deberás crear la base de datos g2backend en tu instancia de MySQL. Puedes hacerlo ejecutando el siguiente comando SQL en tu consola de MySQL:

   ```sql
   CREATE DATABASE g2backend;
   ```

4. Scripts SQL: Los scripts SQL que se ejecutan al inicio pueden necesitar modificaciones para ser compatibles con MySQL. Revisa los scripts en el directorio src/main/resources/sql_imports y haz los cambios necesarios. 

5. El directorio sql_imports generalmente contiene scripts SQL que se utilizan para inicializar la base de datos. Estos scripts pueden incluir la creación de tablas, la inserción de datos iniciales, la creación de índices, entre otros. 

#### APIs disponibles

El backend de `Guárdalo Two` ofrece las siguientes APIs:

* **GET /api/v1/warehouses/requests**: Esta API se utiliza para obtener todas las solicitudes de transporte
* **POST /api/v1/warehouses/requests**: Esta API se utiliza para crear una nueva solicitud de productos a la bodega seleccionada
* **GET /api/v1/warehouses/requests/count**: Esta API se utiliza para obtener el número de solicitudes de productos

* **GET /api/v1/warehouses/products**: Esta API se utiliza para obtener todos los productos de la bodega
* **POST /api/v1/warehouses/products**: Esta API se utiliza para crear un nuevo producto en la bodega
* **GET /api/v1/warehouses/products/{id}**: Esta API se utiliza para obtener un producto de la bodega
* **GET /api/v1/warehouses/products/{id}/search**: Esta API se utiliza para buscar un producto de la bodega utilizando un criterio para su sku, nombre o descripción
* **GET /api/v1/warehouses/products/music-pro**: Esta API se utiliza para obtener todos los productos de la bodega que pertenecen al grupo de productos de música profesional
* **GET /api/v1/warehouses/products/count**: Esta API se utiliza para obtener el número de productos de la bodega

* **POST /api/v1/warehouses/transport**: Esta API se utiliza para crear una nueva solicitud de transporte a la empresa seleccionada
* **GET /api/v1/warehouses/transport/{trackCode/status}**: Esta API se utiliza para obtener el estado de una solicitud de transporte a través de su código de seguimiento

Estas APIs tiene una especificación que puede ser accedida desde [la documentación Swagger](http://localhost:8081/swagger-ui/index.html)

### Frontend

El frontend de `Guárdalo Two` es un proyecto simple construido con HTML, JavaScript, Bootstrap y jQuery. No requiere ninguna instalación de dependencias. Para ejecutar el frontend, sigue estos pasos:

1. Navega hasta el directorio del frontend:

    ```bash
    cd repository/frontend
    ```

2. Abre el archivo `index.html` en el navegador web de preferencia para ver la aplicación.

#### Estructura de Carpetas

El proyecto frontend tiene la siguiente estructura de carpetas:

- `css/`: Esta carpeta contiene todos los archivos CSS para estilos.
- `js/`: Esta carpeta contiene todos los archivos JavaScript para la lógica de la aplicación.
- `img/`: Esta carpeta contiene todas las imágenes utilizadas en la aplicación.
- `pages/`: Esta carpeta contiene todas las páginas HTML de la aplicación.
- `index.html`: Este es el archivo principal de la aplicación que se abre en el navegador.
- `dashboard.html`: Esta es la página de dashboard de la aplicación. Punto de entrada principal a las funcionalidades de la aplicación.

Para el login las credenciales están _hardcodeades_ para uso local y de desarrollo, para producción se deben quitar. Estas corresponde a `user01` y `Pass1234!`

```
Cabe mencionar que el backend debe estar iniciado para que las funcionalidades del frontend estén disponibles.
```

### Librerías Externas

El proyecto utiliza las librerías externas `Bootstrap` y `jQuery`. Estas librerías están incluidas en el archivo `index.html` y en cada una de las páginas que lo necesiten, por lo tanto no requieren ninguna instalación adicional.