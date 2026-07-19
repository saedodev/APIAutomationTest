# API Automation Test

Project ini merupakan framework sederhana untuk melakukan **API Automation Testing** menggunakan **Java**, **Rest Assured**, **TestNG**, dan **Gradle**.

## Tech Stack

- Java
- Rest Assured
- TestNG
- Gradle
- GitHub Actions

## Project Structure

```
src
├── main
│   └── java
└── test
    └── java
        ├── tests
        ├── utils
        └── base
```

## Test Scenarios

Framework ini digunakan untuk melakukan pengujian API seperti:

- GET Request
- POST Request
- PUT Request
- DELETE Request

Setiap pengujian akan melakukan validasi terhadap:

- Status Code
- Response Body
- Response Time

## Running the Tests

Clone repository

```bash
git clone https://github.com/saedodev/APIAutomationTest.git
```

Masuk ke folder project

```bash
cd APIAutomationTest
```

Jalankan seluruh test

Windows

```bash
gradlew test
```

Linux / macOS

```bash
./gradlew test
```

## Test Report

Setelah test selesai dijalankan, laporan dapat dilihat pada:

```
build/reports/tests/test/index.html
```

## Continuous Integration

Project ini menggunakan **GitHub Actions** untuk menjalankan automation test secara otomatis ketika terdapat:

- Push ke branch `main`
- Pull Request ke branch `main`

## Author

**Sayid Ridho**

GitHub: https://github.com/saedodev