-- MyBankUML Database Schema by Abdel
-- SQLite Database Creation Script
-- Generated from BankUml_ERD.md

-- Enable foreign key constraints
PRAGMA foreign_keys = ON;

-- ============================================
-- TABLE CREATION
-- ============================================

-- BANK Table
CREATE TABLE BANK (
    bank_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    name TEXT NOT NULL UNIQUE,
    headquarters_address TEXT NOT NULL,
    swift_code TEXT UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- USER Table
CREATE TABLE USER (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    username TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    user_type TEXT NOT NULL CHECK(user_type IN ('CUSTOMER','TELLER','ADMIN')),
    is_active INTEGER DEFAULT 1,
    last_login DATETIME,
    failed_login_attempts INTEGER DEFAULT 0,
    account_locked_until DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- EMPLOYEE Table (must be created before BRANCH due to manager_id FK)
CREATE TABLE EMPLOYEE (
    employee_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    user_id INTEGER NOT NULL UNIQUE,
    branch_id INTEGER NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    position TEXT NOT NULL,
    department TEXT,
    hire_date DATE NOT NULL,
    is_teller INTEGER DEFAULT 1,
    is_manager INTEGER DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES USER(user_id)
);

-- BRANCH Table
CREATE TABLE BRANCH (
    branch_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    bank_id INTEGER NOT NULL,
    branch_name TEXT NOT NULL,
    address TEXT NOT NULL,
    phone TEXT,
    manager_id INTEGER,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (bank_id) REFERENCES BANK(bank_id),
    FOREIGN KEY (manager_id) REFERENCES EMPLOYEE(employee_id)
);

-- CUSTOMER Table
CREATE TABLE CUSTOMER (
    customer_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    user_id INTEGER NOT NULL UNIQUE,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    date_of_birth DATE NOT NULL,
    birthplace TEXT,
    ssn_masked TEXT,
    phone TEXT,
    address TEXT,
    branch_id INTEGER,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES USER(user_id),
    FOREIGN KEY (branch_id) REFERENCES BRANCH(branch_id)
);

-- ADMINISTRATOR Table
CREATE TABLE ADMINISTRATOR (
    admin_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    user_id INTEGER NOT NULL UNIQUE,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    access_level TEXT NOT NULL CHECK(access_level IN ('FULL','LIMITED')),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES USER(user_id)
);

-- ACCOUNT Table
CREATE TABLE ACCOUNT (
    account_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    account_number TEXT NOT NULL UNIQUE,
    customer_id INTEGER NOT NULL,
    account_type TEXT NOT NULL CHECK(account_type IN ('CHECK','SAVING')),
    balance REAL DEFAULT 0.00,
    status TEXT DEFAULT 'ACTIVE' CHECK(status IN ('ACTIVE','FROZEN','CLOSED')),
    branch_id INTEGER NOT NULL,
    opened_date DATE NOT NULL,
    closed_date DATE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES CUSTOMER(customer_id),
    FOREIGN KEY (branch_id) REFERENCES BRANCH(branch_id)
);

-- CHECK_ACCOUNT Table
CREATE TABLE CHECK_ACCOUNT (
    check_account_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    account_id INTEGER NOT NULL UNIQUE,
    overdraft_limit REAL DEFAULT 0.00,
    monthly_fee REAL DEFAULT 0.00,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES ACCOUNT(account_id)
);

-- SAVING_ACCOUNT Table
CREATE TABLE SAVING_ACCOUNT (
    saving_account_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    account_id INTEGER NOT NULL UNIQUE,
    interest_rate REAL NOT NULL,
    minimum_balance REAL DEFAULT 0.00,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES ACCOUNT(account_id)
);

-- CARD Table
CREATE TABLE CARD (
    card_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    account_id INTEGER NOT NULL,
    card_number_masked TEXT NOT NULL,
    card_type TEXT NOT NULL CHECK(card_type IN ('DEBIT','CREDIT')),
    card_holder_name TEXT NOT NULL,
    expiry_date TEXT NOT NULL,
    cvc_hash TEXT NOT NULL,
    status TEXT DEFAULT 'ACTIVE' CHECK(status IN ('ACTIVE','BLOCKED','EXPIRED')),
    daily_limit REAL DEFAULT 1000.00,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES ACCOUNT(account_id)
);

-- TRANSACTION Table
CREATE TABLE TRANSACTION (
    transaction_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    from_account_id INTEGER,
    to_account_id INTEGER,
    amount REAL NOT NULL CHECK(amount > 0),
    transaction_type TEXT NOT NULL CHECK(transaction_type IN ('DEPOSIT','WITHDRAWAL','TRANSFER','PAYMENT')),
    status TEXT DEFAULT 'PENDING' CHECK(status IN ('PENDING','COMPLETED','FAILED','REVERSED')),
    description TEXT,
    transaction_date DATETIME NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_account_id) REFERENCES ACCOUNT(account_id),
    FOREIGN KEY (to_account_id) REFERENCES ACCOUNT(account_id)
);

-- AUTHENTICATION Table
CREATE TABLE AUTHENTICATION (
    auth_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    user_id INTEGER NOT NULL,
    session_token TEXT NOT NULL UNIQUE,
    expires_at DATETIME NOT NULL,
    ip_address TEXT,
    user_agent TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES USER(user_id)
);

-- ROLE Table
CREATE TABLE ROLE (
    role_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    role_name TEXT NOT NULL UNIQUE,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- PERMISSION Table
CREATE TABLE PERMISSION (
    permission_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    role_id INTEGER NOT NULL,
    resource TEXT NOT NULL,
    action TEXT NOT NULL CHECK(action IN ('CREATE','READ','UPDATE','DELETE')),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES ROLE(role_id)
);

-- AUDIT_LOG Table
CREATE TABLE AUDIT_LOG (
    audit_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    user_id INTEGER,
    action TEXT NOT NULL,
    resource TEXT NOT NULL,
    resource_id TEXT,
    old_value TEXT,
    new_value TEXT,
    ip_address TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES USER(user_id)
);

-- ============================================
-- INDEX CREATION
-- ============================================

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

-- ============================================
-- TRIGGERS FOR UPDATED_AT TIMESTAMPS
-- ============================================

-- BANK update trigger
CREATE TRIGGER update_bank_timestamp 
AFTER UPDATE ON BANK
FOR EACH ROW
BEGIN
    UPDATE BANK SET updated_at = CURRENT_TIMESTAMP WHERE bank_id = NEW.bank_id;
END;

-- BRANCH update trigger
CREATE TRIGGER update_branch_timestamp 
AFTER UPDATE ON BRANCH
FOR EACH ROW
BEGIN
    UPDATE BRANCH SET updated_at = CURRENT_TIMESTAMP WHERE branch_id = NEW.branch_id;
END;

-- USER update trigger
CREATE TRIGGER update_user_timestamp 
AFTER UPDATE ON USER
FOR EACH ROW
BEGIN
    UPDATE USER SET updated_at = CURRENT_TIMESTAMP WHERE user_id = NEW.user_id;
END;

-- CUSTOMER update trigger
CREATE TRIGGER update_customer_timestamp 
AFTER UPDATE ON CUSTOMER
FOR EACH ROW
BEGIN
    UPDATE CUSTOMER SET updated_at = CURRENT_TIMESTAMP WHERE customer_id = NEW.customer_id;
END;

-- EMPLOYEE update trigger
CREATE TRIGGER update_employee_timestamp 
AFTER UPDATE ON EMPLOYEE
FOR EACH ROW
BEGIN
    UPDATE EMPLOYEE SET updated_at = CURRENT_TIMESTAMP WHERE employee_id = NEW.employee_id;
END;

-- ADMINISTRATOR update trigger
CREATE TRIGGER update_administrator_timestamp 
AFTER UPDATE ON ADMINISTRATOR
FOR EACH ROW
BEGIN
    UPDATE ADMINISTRATOR SET updated_at = CURRENT_TIMESTAMP WHERE admin_id = NEW.admin_id;
END;

-- ACCOUNT update trigger
CREATE TRIGGER update_account_timestamp 
AFTER UPDATE ON ACCOUNT
FOR EACH ROW
BEGIN
    UPDATE ACCOUNT SET updated_at = CURRENT_TIMESTAMP WHERE account_id = NEW.account_id;
END;

-- CHECK_ACCOUNT update trigger
CREATE TRIGGER update_check_account_timestamp 
AFTER UPDATE ON CHECK_ACCOUNT
FOR EACH ROW
BEGIN
    UPDATE CHECK_ACCOUNT SET updated_at = CURRENT_TIMESTAMP WHERE check_account_id = NEW.check_account_id;
END;

-- SAVING_ACCOUNT update trigger
CREATE TRIGGER update_saving_account_timestamp 
AFTER UPDATE ON SAVING_ACCOUNT
FOR EACH ROW
BEGIN
    UPDATE SAVING_ACCOUNT SET updated_at = CURRENT_TIMESTAMP WHERE saving_account_id = NEW.saving_account_id;
END;

-- CARD update trigger
CREATE TRIGGER update_card_timestamp 
AFTER UPDATE ON CARD
FOR EACH ROW
BEGIN
    UPDATE CARD SET updated_at = CURRENT_TIMESTAMP WHERE card_id = NEW.card_id;
END;

-- TRANSACTION update trigger
CREATE TRIGGER update_transaction_timestamp 
AFTER UPDATE ON TRANSACTION
FOR EACH ROW
BEGIN
    UPDATE TRANSACTION SET updated_at = CURRENT_TIMESTAMP WHERE transaction_id = NEW.transaction_id;
END;

-- ROLE update trigger
CREATE TRIGGER update_role_timestamp 
AFTER UPDATE ON ROLE
FOR EACH ROW
BEGIN
    UPDATE ROLE SET updated_at = CURRENT_TIMESTAMP WHERE role_id = NEW.role_id;
END;

-- PERMISSION update trigger
CREATE TRIGGER update_permission_timestamp 
AFTER UPDATE ON PERMISSION
FOR EACH ROW
BEGIN
    UPDATE PERMISSION SET updated_at = CURRENT_TIMESTAMP WHERE permission_id = NEW.permission_id;
END;

-- ============================================
-- VERIFICATION QUERIES
-- ============================================

-- List all tables
SELECT 'Tables created:' as status;
SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;

-- List all indexes
SELECT 'Indexes created:' as status;
SELECT name FROM sqlite_master WHERE type='index' ORDER BY name;

-- List all triggers
SELECT 'Triggers created:' as status;
SELECT name FROM sqlite_master WHERE type='trigger' ORDER BY name;
