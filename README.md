# README

## Instrucciones de Construcción y Ejecución del Proyecto

### 1. Crear el archivo Jar

Para construir el archivo JAR del proyecto, ejecuta el siguiente comando en la línea de comandos:

```
mvn clean install
```

El archivo JAR generado estará en el directorio **_target_** con un nombre similar a demoSmartJob-[VERSION].jar.

### 2. Despliegue en el Servidor

Para Deplegar el servicio, ejecuta el siguente comando en la línea de comandos:

```
java -jar target/demoSmartJob-[VERSION].jar
```

**_Ejemplo:_**

```
java -jar target/demoSmartJob-0.0.1-SNAPSHOT.jar
```

### 3. Acceder a la Aplicación

Abre Postman y accede a la siguiente ruta: **_localhost:8080/v1/_**.
Aqui una [Postman Collection](src/main/resources/Demo Collection.postman_collection.json) para importar.

### 4. Endpoints Disponibles

Los endpoints disponibles son:

- auth:
    - sign-up (POST)
    - login (POST)
    - getAllUsers (GET) (ADDITIONAL)
    - getUsersByUUID (GET) (ADDITIONAL)

Los endpoints antes mencionados están detallados en el siguente
swagger: [Swagger](http://localhost:8080/swagger-ui/index.html#/) y/o en
siguente [yaml](src/main/resources/api-docs.yaml)

Cuando es requerido el token, en Postman se debe selecionar **"Authorization"**, seleccionar en Type **"Bearer Token"** y
colocar el Token generado en los endpoints **Sign-up** o **login**

## Sobre la Aplicacion:

### Base de Datos

Al momento de correr la aplicación, se crea una base de datos en memoria [h2](http://localhost:8080/h2-console), cuya
credenciales son:

```
username=sa
password=
```

Las tablas de esta aplicación son:

- **USER**
  ~~~
  id (KEY)
  UUID;
  name;
  password;
  email;
  created;
  modified;
  lastLogin;
  isActive;
  ~~~

  Tabla que contiene los usuarios de la Aplicación cuya password se guarda encriptada. el campo UUID es el que se
  devuelve como id y no mostrar el id de la BD y asi evitar ataques "sql injection"


- **Phone**
  ~~~
  id (KEY);
  number;
  citycode;
  contrycode;
  ~~~

  Tabla que contiene los telefonos de los usuarios registrados.


- **Users_Phones**
  ~~~
  user_id (OneToMany)
  phones_id (OneToMany)
  ~~~  

  Tabla que springboot genera automaticamente y solo contiene la claves foráneas de la las tablas **user** y **phone**
  para reflejar la relacion.

### Security

La aplicación tiene una capa de seguridad con Spring Security utilizando JWT.

### Diagramas de la Solución

Aqui estan los diagramas de flujos de los endpoints solicitados: 

- [Login](Documentation/SequenceDiagram_login.html)
- [Sign-up](Documentation/SequenceDiagram_sign-up.html)


## Notas Adicionales

* Se asume que el campo email de user es el username de la aplicación, segun el siguente Requerimiento :
  **_Si caso el correo conste en la base de datos, deberá retornar un error "El correo ya registrado"._**
* Se asume que los telefonos son opcional al momento de crear un User.
* SpringBoot está diseñado para ejecutar aplicaciones en forma autónoma, incluye un servidor Tomcat embebido. Se puede
  ejecutar la aplicación directamente, sin necesidad de un servidor Tomcat externo.
* La documentación Swagger se genera una vez que se ejecuta la aplicacion (Despliegue de la aplicación).
* La base de datos se pre-cargan con estos [datos](src/main/resources/data.sql) para las tablas **USER**, **PHONE** y *
  *USERS_PHONES**
* Revisa la documentación detallada de cada endpoint para comprender mejor su funcionamiento y contratos de entrada.
* Por efecto de esta Demo, la version de este es **0.0.1-SNAPSHOT**.
* La version de spring boot **3.2.2** presenta Vulnerabilidad en una
  dependencia ([CVE-2023-51074](https://devhub.checkmarx.com/cve-details/CVE-2023-51074/)), recomiendan simpre utilizar
  la versión más reciente de Spring Boot para evitar ataques maliciosos.  
