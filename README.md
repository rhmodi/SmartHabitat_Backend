## Smart Habitat ##

Welcome to the `Smart Habitat Backend`! This is a Spring Boot application that provides RESTful API services for the Smart Habitat project. It utilizes a graph database for storing knowledge graphs and supports SPARQL queries.

---


## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the Backend](#running-the-backend)
- [Local Configuration](#local-configuration)
- [API Documentation](#api-documentation)
- [Deployment](#deployment)
- [Database Configuration](#database-configuration)
- [Logging](#logging)
- [Contributors](#contributors)
- [Important Dependencies](#important-dependencies)

---

## Prerequisites

Before you begin, ensure you have met the following requirements:

- **Java Development Kit (JDK) 17** or higher installed.
- **Apache Maven** version **3.9.9** installed.
- Optional: **Lombok** plugin for your IDE (e.g., Eclipse, IntelliJ IDEA). [Download Lombok](https://projectlombok.org/download)

---


## Installation

1. Clone the Repository:
    ```python
    git clone https://github.com/rhmodi/SmartHabitat_Backend
    ```
2. Go the App folder:
    ```python
    cd SmartHabitat_Backend/SmartHabitat
    ```
3. Install Dependencies and build the application:
    ```python
    mvn clean package
    ```

---

## Running the Backend

To run the application locally: 
```python
mvn spring-boot:run
```

Base URL for the application:
```python
http://localhost:3001/api/smart-Habitat/
```
---

## Local Configuration

The application runs on port `3001` by default.

You can change the port or other configurations in the `application.properties` file located in `src/main/resources/`.
To change the port:
```python
server.port=3001
```

---


## API Documentation
Swagger UI is integrated into the application for API documentation.
Once the application is running, you can access the API documentation at:
```python
http://localhost:3001/swagger-ui.html
```
Replace `localhost` and `3001` with your server's address and port if necessary.

---

## Deployment
The backend application is hosted on Azure WebApp and deployed using a CI/CD pipeline with GitHub Actions.

### Deployment Details 
- **Azure WebApp**: Hosts the Spring Boot application.
- **CI/CD Pipeline**: Configured to deploy the application automatically on changes to the `cloudDeployment` branch.
- **Branch**: Ensure the `cloudDeployment` branch is updated with the latest code for deployment.
- **Graph Database**: Hosted on AWS to store the knowledge graph and support SPARQL queries.

### Deployment Steps
1. Build the Application
    Runt the following command to clean and package the application:
    ```python
    mvn clean package
    ```
2. Automated Deployement
    - Push the changes to `cloudDeployment` branch.
    - GitHub Actions will trigger a workflow to build and deploy the application to Azure WebApp.
    - After Successful build and deploy your backend is live on server.
3. Live Deployement URL
    ```python
    https://smarthabitatfinder.azurewebsites.net/api/smart-Habitat/home
    ```
4. If everything works correctly You will see `Welcome to Smart Habitat Finder APP !!!`. This means backend is up and running

---

## Database Configuration
The application uses a graph database for storing the knowledge graph and executing SPARQL queries.
- **Database Type**: Grapdb
- **Hosted On** : Azure
- **Database URL**: `http://20.3.141.133:7200/`
- **Connection Details**: Update the database connection settings in application.properties.
The database is accessible through a web interface for easy management and query execution. Go the [GraphDB](http://20.3.141.133:7200/)to view and interact with the database

---

## Logging
Logs are stored in the logs folder within the project directory.
Logging configuration can be adjusted in `src/main/resources/log4j2.xml`.

---

## Contributors

This project was built and maintained with contributions from the following team members:

- **RakshilKumar Modi** - [GitHub Profile](https://github.com/rakshil14-2)
- **Akshata Vijay Kulkarni** - [GitHub Profile](https://github.com/akulka89)
- **Kaumudi Degekar Gulbarga** - [GitHub Profile](https://github.com/kgulbarg)
- **Shloka Pandya** - [GitHub Profile](https://github.com/shloka23)
- **Anuj Kapoor** - [GitHub Profile](https://github.com/anujkap)


---

## Important Dependencies

1. Interaction with graphDB and sparql query:
    ```python
    		<dependency>
			<groupId>org.eclipse.rdf4j</groupId>
			<artifactId>rdf4j-model</artifactId>
			<version>5.0.2</version>
		</dependency>
		<dependency>
			<groupId>org.eclipse.rdf4j</groupId>
			<artifactId>rdf4j-repository-sparql</artifactId>
			<version>5.0.2</version>
		</dependency>
		<dependency>
			<groupId>org.eclipse.rdf4j</groupId>
			<artifactId>rdf4j-query</artifactId>
			<version>5.0.2</version>
		</dependency>
    ```
2. Implementation Logs for tracing exceptions and errors:
    ```python
        <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-log4j2</artifactId>
			<version>3.3.5</version>
		</dependency>
    ```
3. Api Documentation:
    ```python
        <dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
			<version>2.6.0</version>
		</dependency>
		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-starter-webmvc-api</artifactId>
			<version>2.6.0</version>
		</dependency>
    ```







