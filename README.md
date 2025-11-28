# 🏦 BankUml: Banking System Simulation

Welcome to **BankUml**, a Java-based banking application designed to simulate core banking operations such as account management, transactions, and receipts.

This project demonstrates the use of Object-Oriented Programming (OOP) principles, including **Inheritance**, **Encapsulation**, **Abstraction**, and **Polymorphism**, strictly following the provided UML diagram.

## 📌 Features

- **Account Management**: Create and manage multiple types of bank accounts.
- **Transaction Handling**: Simulate payments and generate receipts.

## 🚀 How to Run

Make sure you have the following installed:

- JDK 17+
- Maven

1. Clone the repository:

```bash
git clone https://github.com/AbdessalamAithaqi/MyBankUml.git
cd MyBankUml
```

2. Compile the code:

```bash
mvn -q -DskipTests compile
```
(pom.xml sets the repo root as sourceDirectory)

3. Run the program:

```bash
# Linux/MAC
java -cp "target/classes:libs/*" bank.Main
# Windows
java -cp "target/classes;libs/*" bank.Main
```
(libs/ folder contains sqlite, jbcrypt, and lombok dependencies)

To redownload the dependencies if they are missing:

```bash
mvn dependency:copy-dependencies -DoutputDirectory=./libs
```

4. How it works

- bank.Main builds a JFrame with a CardLayout container and hands it to LoginController
- LoginController shows a role chooser (customer/teller/admin) and role-specific login/registration forms (bank/GUI/*). It talks to the singleton
  Database (database/Database.java), which opens database/bank.db, ensures tables exist, and authenticates users (passwords hashed with BCrypt). Customer
  registration also creates default check and saving accounts.
- After login:
  - Customers (CustomerController) see check/saving balances, can deposit/withdraw, transfer between their two accounts, and view transaction history.
    Balance changes and transactions are persisted via Database helper methods.
  - Tellers (EmployeeController) and admins (AdminController) get a menu to search accounts with filters, load customer accounts, and open transaction/
    transfer dialogs. Admins additionally manage users (create/update/delete non-customer users), adjust balances/statuses, and register customers.
- Everything is desktop Swing; there’s no web server. The database and GUI run locally.

---

Originally developed by [@shayanaminaei](https://github.com/shayanaminaei)
