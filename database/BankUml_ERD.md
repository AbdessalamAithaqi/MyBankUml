# BankUml/MyBankUML Entity-Relationship Diagram

## ERD Overview
This ERD represents the database schema for the MyBankUML banking system, with complete field definitions, keys, and SQLite-compatible data types.

## Database Schema with Keys and Types

### BANK
| Field | Type | Key | Constraints | Description |
|-------|------|-----|-------------|-------------|
| bank_id | INTEGER | PK | NOT NULL, AUTOINCREMENT | Unique bank identifier |
| name | TEXT | | NOT NULL, UNIQUE | Bank name |
| headquarters_address | TEXT | | NOT NULL | Main office address |
| swift_code | TEXT | | UNIQUE | International bank code |
| created_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Last update timestamp |

### BRANCH
| Field | Type | Key | Constraints | Description |
|-------|------|-----|-------------|-------------|
| branch_id | INTEGER | PK | NOT NULL, AUTOINCREMENT | Unique branch identifier |
| bank_id | INTEGER | FK | NOT NULL, REFERENCES BANK(bank_id) | Associated bank |
| branch_name | TEXT | | NOT NULL | Branch name |
| address | TEXT | | NOT NULL | Physical address |
| phone | TEXT | | | Contact number |
| manager_id | INTEGER | FK | REFERENCES EMPLOYEE(employee_id) | Branch manager |
| created_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Last update timestamp |

### USER
| Field | Type | Key | Constraints | Description |
|-------|------|-----|-------------|-------------|
| user_id | INTEGER | PK | NOT NULL, AUTOINCREMENT | Unique user identifier |
| username | TEXT | UK | NOT NULL, UNIQUE | Login username |
| password_hash | TEXT | | NOT NULL | Encrypted password |
| email | TEXT | UK | NOT NULL, UNIQUE | Email address |
| user_type | TEXT | | NOT NULL, CHECK(user_type IN ('CUSTOMER','TELLER','ADMIN')) | User role type |
| is_active | INTEGER | | DEFAULT 1 | Account active status (0/1) |
| last_login | DATETIME | | | Last successful login |
| failed_login_attempts | INTEGER | | DEFAULT 0 | Failed login counter |
| account_locked_until | DATETIME | | | Lockout expiration |
| created_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Last update timestamp |

### CUSTOMER
| Field | Type | Key | Constraints | Description |
|-------|------|-----|-------------|-------------|
| customer_id | INTEGER | PK | NOT NULL, AUTOINCREMENT | Unique customer identifier |
| user_id | INTEGER | FK, UK | NOT NULL, UNIQUE, REFERENCES USER(user_id) | Associated user account |
| first_name | TEXT | | NOT NULL | Customer first name |
| last_name | TEXT | | NOT NULL | Customer last name |
| date_of_birth | DATE | | NOT NULL | Birth date |
| birthplace | TEXT | | | Place of birth |
| ssn_masked | TEXT | | | Last 4 digits only (****1234) |
| phone | TEXT | | | Contact number |
| address | TEXT | | | Residential address |
| branch_id | INTEGER | FK | REFERENCES BRANCH(branch_id) | Home branch |
| created_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Last update timestamp |

### EMPLOYEE (Teller)
| Field | Type | Key | Constraints | Description |
|-------|------|-----|-------------|-------------|
| employee_id | INTEGER | PK | NOT NULL, AUTOINCREMENT | Unique employee identifier |
| user_id | INTEGER | FK, UK | NOT NULL, UNIQUE, REFERENCES USER(user_id) | Associated user account |
| branch_id | INTEGER | FK | NOT NULL, REFERENCES BRANCH(branch_id) | Work location |
| first_name | TEXT | | NOT NULL | Employee first name |
| last_name | TEXT | | NOT NULL | Employee last name |
| position | TEXT | | NOT NULL | Job title |
| department | TEXT | | | Department name |
| hire_date | DATE | | NOT NULL | Employment start date |
| is_teller | INTEGER | | DEFAULT 1 | Teller role flag (0/1) |
| is_manager | INTEGER | | DEFAULT 0 | Manager role flag (0/1) |
| created_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Last update timestamp |

### ADMINISTRATOR
| Field | Type | Key | Constraints | Description |
|-------|------|-----|-------------|-------------|
| admin_id | INTEGER | PK | NOT NULL, AUTOINCREMENT | Unique admin identifier |
| user_id | INTEGER | FK, UK | NOT NULL, UNIQUE, REFERENCES USER(user_id) | Associated user account |
| first_name | TEXT | | NOT NULL | Admin first name |
| last_name | TEXT | | NOT NULL | Admin last name |
| access_level | TEXT | | NOT NULL, CHECK(access_level IN ('FULL','LIMITED')) | Permission level |
| created_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Last update timestamp |

### ACCOUNT
| Field | Type | Key | Constraints | Description |
|-------|------|-----|-------------|-------------|
| account_id | INTEGER | PK | NOT NULL, AUTOINCREMENT | Unique account identifier |
| account_number | TEXT | UK | NOT NULL, UNIQUE | Account number |
| customer_id | INTEGER | FK | NOT NULL, REFERENCES CUSTOMER(customer_id) | Account owner |
| account_type | TEXT | | NOT NULL, CHECK(account_type IN ('CHECK','SAVING')) | Account category |
| balance | REAL | | DEFAULT 0.00 | Current balance |
| status | TEXT | | DEFAULT 'ACTIVE', CHECK(status IN ('ACTIVE','FROZEN','CLOSED')) | Account status |
| branch_id | INTEGER | FK | NOT NULL, REFERENCES BRANCH(branch_id) | Managing branch |
| opened_date | DATE | | NOT NULL | Account opening date |
| closed_date | DATE | | | Account closure date |
| created_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Last update timestamp |

### CHECK_ACCOUNT
| Field | Type | Key | Constraints | Description |
|-------|------|-----|-------------|-------------|
| check_account_id | INTEGER | PK | NOT NULL, AUTOINCREMENT | Unique check account ID |
| account_id | INTEGER | FK, UK | NOT NULL, UNIQUE, REFERENCES ACCOUNT(account_id) | Base account |
| overdraft_limit | REAL | | DEFAULT 0.00 | Overdraft protection amount |
| monthly_fee | REAL | | DEFAULT 0.00 | Monthly maintenance fee |
| created_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Last update timestamp |

### SAVING_ACCOUNT
| Field | Type | Key | Constraints | Description |
|-------|------|-----|-------------|-------------|
| saving_account_id | INTEGER | PK | NOT NULL, AUTOINCREMENT | Unique saving account ID |
| account_id | INTEGER | FK, UK | NOT NULL, UNIQUE, REFERENCES ACCOUNT(account_id) | Base account |
| interest_rate | REAL | | NOT NULL | Annual interest rate (%) |
| minimum_balance | REAL | | DEFAULT 0.00 | Minimum required balance |
| created_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Last update timestamp |

### CARD
| Field | Type | Key | Constraints | Description |
|-------|------|-----|-------------|-------------|
| card_id | INTEGER | PK | NOT NULL, AUTOINCREMENT | Unique card identifier |
| account_id | INTEGER | FK | NOT NULL, REFERENCES ACCOUNT(account_id) | Associated account |
| card_number_masked | TEXT | | NOT NULL | Masked card number (****1234) |
| card_type | TEXT | | NOT NULL, CHECK(card_type IN ('DEBIT','CREDIT')) | Card category |
| card_holder_name | TEXT | | NOT NULL | Name on card |
| expiry_date | TEXT | | NOT NULL | MM/YY format |
| cvc_hash | TEXT | | NOT NULL | Hashed CVC code |
| status | TEXT | | DEFAULT 'ACTIVE', CHECK(status IN ('ACTIVE','BLOCKED','EXPIRED')) | Card status |
| daily_limit | REAL | | DEFAULT 1000.00 | Daily transaction limit |
| created_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Last update timestamp |

### TRANSACTION
| Field | Type | Key | Constraints | Description |
|-------|------|-----|-------------|-------------|
| transaction_id | INTEGER | PK | NOT NULL, AUTOINCREMENT | Unique transaction ID |
| from_account_id | INTEGER | FK | REFERENCES ACCOUNT(account_id) | Source account |
| to_account_id | INTEGER | FK | REFERENCES ACCOUNT(account_id) | Destination account |
| amount | REAL | | NOT NULL, CHECK(amount > 0) | Transaction amount |
| transaction_type | TEXT | | NOT NULL, CHECK(transaction_type IN ('DEPOSIT','WITHDRAWAL','TRANSFER','PAYMENT')) | Transaction category |
| status | TEXT | | DEFAULT 'PENDING', CHECK(status IN ('PENDING','COMPLETED','FAILED','REVERSED')) | Transaction status |
| description | TEXT | | | Transaction notes |
| transaction_date | DATETIME | | NOT NULL | Transaction timestamp |
| created_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Last update timestamp |

### AUTHENTICATION
| Field | Type | Key | Constraints | Description |
|-------|------|-----|-------------|-------------|
| auth_id | INTEGER | PK | NOT NULL, AUTOINCREMENT | Unique session ID |
| user_id | INTEGER | FK | NOT NULL, REFERENCES USER(user_id) | Authenticated user |
| session_token | TEXT | UK | NOT NULL, UNIQUE | Session identifier |
| expires_at | DATETIME | | NOT NULL | Session expiration |
| ip_address | TEXT | | | Client IP |
| user_agent | TEXT | | | Browser/client info |
| created_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Session start time |

### ROLE
| Field | Type | Key | Constraints | Description |
|-------|------|-----|-------------|-------------|
| role_id | INTEGER | PK | NOT NULL, AUTOINCREMENT | Unique role ID |
| role_name | TEXT | UK | NOT NULL, UNIQUE | Role identifier |
| description | TEXT | | | Role description |
| created_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Last update timestamp |

### PERMISSION
| Field | Type | Key | Constraints | Description |
|-------|------|-----|-------------|-------------|
| permission_id | INTEGER | PK | NOT NULL, AUTOINCREMENT | Unique permission ID |
| role_id | INTEGER | FK | NOT NULL, REFERENCES ROLE(role_id) | Associated role |
| resource | TEXT | | NOT NULL | Resource name |
| action | TEXT | | NOT NULL, CHECK(action IN ('CREATE','READ','UPDATE','DELETE')) | Allowed action |
| created_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |
| updated_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Last update timestamp |

### AUDIT_LOG
| Field | Type | Key | Constraints | Description |
|-------|------|-----|-------------|-------------|
| audit_id | INTEGER | PK | NOT NULL, AUTOINCREMENT | Unique audit entry ID |
| user_id | INTEGER | FK | REFERENCES USER(user_id) | Acting user |
| action | TEXT | | NOT NULL | Action performed |
| resource | TEXT | | NOT NULL | Affected resource |
| resource_id | TEXT | | | Affected record ID |
| old_value | TEXT | | | Previous value (JSON) |
| new_value | TEXT | | | New value (JSON) |
| ip_address | TEXT | | | Client IP |
| created_at | DATETIME | | DEFAULT CURRENT_TIMESTAMP | Action timestamp |

## Index Definitions

```sql
-- Performance indexes
CREATE INDEX idx_customer_name ON CUSTOMER(first_name, last_name);
CREATE INDEX idx_customer_birthplace ON CUSTOMER(birthplace);
CREATE INDEX idx_account_number ON ACCOUNT(account_number);
CREATE INDEX idx_account_customer ON ACCOUNT(customer_id);
CREATE INDEX idx_account_status ON ACCOUNT(status);
CREATE INDEX idx_transaction_date ON TRANSACTION(transaction_date);
CREATE INDEX idx_transaction_from ON TRANSACTION(from_account_id);
CREATE INDEX idx_transaction_to ON TRANSACTION(to_account_id);
CREATE INDEX idx_user_email ON USER(email);
CREATE INDEX idx_user_username ON USER(username);
CREATE INDEX idx_audit_user_date ON AUDIT_LOG(user_id, created_at);
CREATE INDEX idx_auth_token ON AUTHENTICATION(session_token);
CREATE INDEX idx_auth_expiry ON AUTHENTICATION(expires_at);
```

## Key Legend
- **PK**: Primary Key
- **FK**: Foreign Key
- **UK**: Unique Key

## SQLite Data Types Used
- **INTEGER**: Whole numbers, also used for boolean flags (0/1)
- **TEXT**: Variable-length character strings
- **REAL**: Floating-point numbers for monetary values
- **DATE/DATETIME**: Stored as TEXT in ISO8601 format (YYYY-MM-DD HH:MM:SS)

## Notes
1. All monetary values use REAL type with 2 decimal precision
2. Boolean fields use INTEGER (0 = false, 1 = true)
3. Timestamps use SQLite's CURRENT_TIMESTAMP for automatic updates
4. CHECK constraints enforce data integrity at database level
5. Sensitive data (SSN, card numbers) stored in masked format only
