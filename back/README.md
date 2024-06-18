# Yoga App - Backend Testing

## Introduction

Bienvenue dans le projet de tests pour l'application Yoga. Ce projet utilise Java et Spring Boot pour le backend et Angular pour le frontend. Les tests sont essentiels pour garantir la qualité et la fiabilité de l'application.

## Pré-requis

- JDK 11 ou supérieur
- Maven
- MySQL
- Node.js et npm (pour le frontend)
- Angular CLI (si besoin de tester le frontend)

## Installation

1. **Cloner le dépôt**
   ```bash
   git clone https://github.com/Meceline/Testing-java-angular.git
   cd Testing-java-angular
   git checkout develop

2. **Configurer la base de données**
    ```bash
   Créez une base de données MySQL nommée testdb.
   Mettez à jour les informations de connexion à la base de données dans src/main/resources/application.properties.
   

3. **Execution des tests - backend**
    ```bash
   mvn test

Les rapports de couverture seront générés accessible dans le dossier target/site/jacoco/index.html.

4. **Execution des tests - front-end**
    ```bash
   npm run test

Les rapports de couverture seront accessible depuis l'url : http://127.0.0.1:5500/front/coverage/jest/lcov-report/index.html

5. **Execution des tests end to end - front-end**
    ```bash
   ng e2e

Les rapports de test sont accessibles dans le navigateur choisi pour executer les tests.