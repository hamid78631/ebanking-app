# e-banking — Digital Banking Platform

A full-stack digital banking platform built with **Spring Boot** (backend) and **Angular** (frontend), designed to manage customers, bank accounts, and financial operations in a clean and modern interface.

---

## Features

- **Dashboard** — Real-time overview of customers, accounts, total balance, and account type distribution
- **Customer Management** — Create, search, edit, and delete customers; view their accounts in a side panel
- **Account Management** — Create current (with overdraft) and saving (with interest rate) accounts per customer
- **Banking Operations** — Debit, credit, and transfer between accounts with full transaction history
- **AI Chatbot** — Integrated assistant powered by Google Gemini (Spring AI)
- **Responsive UI** — Light theme with a professional red palette, horizontal navigation, and Plus Jakarta Sans typography

---

## Tech Stack

### Backend
- Java 21 (Temurin)
- Spring Boot 3.4.1
- Spring Data JPA (H2 in-memory database)
- Spring Security (CORS, stateless)
- Spring AI — Vertex AI Gemini

### Frontend
- Angular 15+
- TypeScript
- RxJS (`forkJoin`, `Observable`)
- Reactive Forms
- Bootstrap Icons
- Google Fonts — Plus Jakarta Sans

---

## Getting Started

### Backend

```bash
cd ebanking-backend
./mvnw spring-boot:run
```

The API is available at `http://localhost:8085`.

### Frontend

```bash
cd ebanking-frontend
npm install
ng serve
```

The app is available at `http://localhost:4200`.

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/customers` | List all customers |
| GET | `/customers/search?keyword=` | Search customers |
| POST | `/customers` | Create a customer |
| PUT | `/customers/{id}` | Update a customer |
| DELETE | `/customers/{id}` | Delete a customer |
| GET | `/accounts` | List all accounts |
| GET | `/accounts/{id}/pageOperations` | Paginated transaction history |
| POST | `/accounts/current` | Create a current account |
| POST | `/accounts/saving` | Create a saving account |
| POST | `/accounts/debit` | Debit an account |
| POST | `/accounts/credit` | Credit an account |
| POST | `/accounts/transfer` | Transfer between accounts |

---

## Author

Hamid — [hamidoumamoudou125@icloud.com](mailto:hamidoumamoudou125@icloud.com)