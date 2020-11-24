# Sample project with Ktor

## Stacks

* Ktor framework

* Firebase Authentication

* Kotlin Serialization

* Dependency injection with Koin

* Database access with Doma

* Database migration with Flyway

## Get Started

### Prepare Firebase project

Create your project on [Firebase Console](https://console.firebase.google.com/).

Then, download an admin credential file into `.env/configs`.

### Setup local properties

Create `.env/application.properties` file as bellow.

```properties
# Path to the firebase admin credential file downloaded in the previous step
GOOGLE_APPLICATION_CREDENTIALS=./.env/configs/xxxxxxxxxxx.json
# URL of the firebase database which you can find the firebase console
FIREBASE_DATABASE_URL=

# CORS
KTOR_CORS_ALLOW_SCHEME=http,https
KTOR_CORS_ALLOW_ORIGIN=*

# H2 database
DATABASE_URL=jdbc:h2:./.env/work/h2/application;MODE=MySQL;AUTO_SERVER=TRUE
DATABASE_SECRET_JSON={ "username": "sa", "password": "pass" }
DATABASE_DIALECT=org.seasar.doma.jdbc.dialect.H2Dialect
```

### Run

```bash
$ ./gradlew run
```

### Run with Docker

You can also run this application on Docker.

First, build docker image.

```bash
$ ./gradlew jibDockerBuild
# or
$ ./gradlew jibBuildTar
$ docker load --input build/jib-image.tar
```

Next, edit `.env/application.properties` file.
* Comment out properties related to database in order to connect to MySQL container.
* Fix `GOOGLE_APPLICATION_CREDENTIALS` property as starting with `/etc/configs/` instead of `./.env/configs/`.

Then,

```bash
$ docker-compose up -d db
$ docker-compose up api
```
