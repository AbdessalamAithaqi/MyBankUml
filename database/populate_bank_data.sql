-- MyBankUML Database - Dummy Data Population Script
-- Created by Claude for Abdel
-- This script populates the database with realistic dummy data

-- ============================================
-- CLEAR EXISTING DATA (if any)
-- ============================================

DELETE FROM AUDIT_LOG;
DELETE FROM PERMISSION;
DELETE FROM ROLE;
DELETE FROM AUTHENTICATION;
DELETE FROM `TRANSACTION`;
DELETE FROM CARD;
DELETE FROM SAVING_ACCOUNT;
DELETE FROM CHECK_ACCOUNT;
DELETE FROM ACCOUNT;
DELETE FROM ADMINISTRATOR;
DELETE FROM CUSTOMER;
DELETE FROM EMPLOYEE;
DELETE FROM BRANCH;
DELETE FROM BANK;
DELETE FROM USER;

-- Reset autoincrement counters
DELETE FROM sqlite_sequence;

-- ============================================
-- INSERT BANK DATA
-- ============================================

INSERT INTO BANK (name, headquarters_address, swift_code) VALUES
('National Trust Bank', '1500 Bay Street, Toronto, ON M5J 2N8', 'NTBKCATT'),
('Unity Financial Group', '888 Dunsmuir Street, Vancouver, BC V6C 3K4', 'UNFGCAVV'),
('Premier Banking Corp', '200 Wellington Street, Ottawa, ON K1A 0H8', 'PBCCCAOT');

-- ============================================
-- INSERT BRANCHES
-- ============================================

INSERT INTO BRANCH (bank_id, branch_name, address, phone, manager_id) VALUES
-- National Trust Bank branches
(1, 'Downtown Toronto', '100 King Street West, Toronto, ON M5X 1A9', '416-555-0100', NULL),
(1, 'North York Centre', '5000 Yonge Street, North York, ON M2N 7E9', '416-555-0200', NULL),
(1, 'Mississauga Square One', '100 City Centre Drive, Mississauga, ON L5B 2C9', '905-555-0100', NULL),

-- Unity Financial Group branches
(2, 'Vancouver Downtown', '700 West Georgia Street, Vancouver, BC V7Y 1B6', '604-555-0100', NULL),
(2, 'Richmond Centre', '6551 No. 3 Road, Richmond, BC V6Y 2B6', '604-555-0200', NULL),

-- Premier Banking Corp branches
(3, 'Parliament Hill', '111 Wellington Street, Ottawa, ON K1A 0A9', '613-555-0100', NULL),
(3, 'Gatineau Plaza', '25 Rue Laurier, Gatineau, QC J8X 4C8', '819-555-0100', NULL);

-- ============================================
-- INSERT USERS
-- ============================================

-- Admin Users
INSERT INTO USER (username, password_hash, email, user_type, is_active) VALUES
('admin.master', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@nationaltrustbank.ca', 'ADMIN', 1),
('admin.support', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'support@nationaltrustbank.ca', 'ADMIN', 1);

-- Teller/Employee Users (including your specified people)
INSERT INTO USER (username, password_hash, email, user_type, is_active) VALUES
('abdel.ait', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'abdel.ait@nationaltrustbank.ca', 'TELLER', 1),
('biko.ndab', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'biko.ndab@nationaltrustbank.ca', 'TELLER', 1),
('thomas.sum', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'thomas.sum@unityfg.ca', 'TELLER', 1),
('ulysse.qqch', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ulysse.qqch@premierbanking.ca', 'TELLER', 1),
('pavneet.some', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'pavneet.some@nationaltrustbank.ca', 'TELLER', 1),
('sarah.johnson', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'sarah.johnson@nationaltrustbank.ca', 'TELLER', 1),
('michael.chen', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'michael.chen@unityfg.ca', 'TELLER', 1),
('jennifer.martinez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'jennifer.martinez@premierbanking.ca', 'TELLER', 1),
('david.kim', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'david.kim@unityfg.ca', 'TELLER', 1);

-- Customer Users
INSERT INTO USER (username, password_hash, email, user_type, is_active) VALUES
('emma.wilson', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'emma.wilson@email.com', 'CUSTOMER', 1),
('james.brown', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'james.brown@email.com', 'CUSTOMER', 1),
('olivia.davis', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'olivia.davis@email.com', 'CUSTOMER', 1),
('william.garcia', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'william.garcia@email.com', 'CUSTOMER', 1),
('sophia.rodriguez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'sophia.rodriguez@email.com', 'CUSTOMER', 1),
('liam.martinez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'liam.martinez@email.com', 'CUSTOMER', 1),
('ava.hernandez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ava.hernandez@email.com', 'CUSTOMER', 1),
('noah.lopez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'noah.lopez@email.com', 'CUSTOMER', 1),
('isabella.gonzalez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'isabella.gonzalez@email.com', 'CUSTOMER', 1),
('ethan.wilson', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ethan.wilson@email.com', 'CUSTOMER', 1),
('mia.anderson', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'mia.anderson@email.com', 'CUSTOMER', 1),
('alexander.thomas', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'alexander.thomas@email.com', 'CUSTOMER', 1),
('charlotte.taylor', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'charlotte.taylor@email.com', 'CUSTOMER', 1),
('daniel.moore', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'daniel.moore@email.com', 'CUSTOMER', 1),
('amelia.jackson', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'amelia.jackson@email.com', 'CUSTOMER', 1),
('matthew.martin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'matthew.martin@email.com', 'CUSTOMER', 1),
('harper.lee', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'harper.lee@email.com', 'CUSTOMER', 1),
('sebastian.perez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'sebastian.perez@email.com', 'CUSTOMER', 1),
('evelyn.thompson', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'evelyn.thompson@email.com', 'CUSTOMER', 1),
('jack.white', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'jack.white@email.com', 'CUSTOMER', 1);

-- ============================================
-- INSERT ADMINISTRATORS
-- ============================================

INSERT INTO ADMINISTRATOR (user_id, first_name, last_name, access_level) VALUES
(1, 'System', 'Administrator', 'FULL'),
(2, 'Support', 'Team', 'LIMITED');

-- ============================================
-- INSERT EMPLOYEES (including your people)
-- ============================================

INSERT INTO EMPLOYEE (user_id, branch_id, first_name, last_name, position, department, hire_date, is_teller, is_manager) VALUES
-- Branch 1 - Downtown Toronto
(3, 1, 'Abdel', 'Ait', 'Senior Teller', 'Customer Service', '2020-03-15', 1, 1),
(4, 1, 'Biko', 'Ndab', 'Teller', 'Customer Service', '2021-06-01', 1, 0),
(8, 1, 'Sarah', 'Johnson', 'Teller', 'Customer Service', '2022-01-10', 1, 0),

-- Branch 2 - North York Centre
(7, 2, 'Pavneet', 'Some', 'Branch Manager', 'Management', '2018-09-20', 0, 1),
(9, 2, 'Michael', 'Chen', 'Teller', 'Customer Service', '2021-11-15', 1, 0),

-- Branch 4 - Vancouver Downtown
(5, 4, 'Thomas', 'Sum', 'Senior Teller', 'Customer Service', '2019-07-12', 1, 1),
(11, 4, 'David', 'Kim', 'Teller', 'Customer Service', '2022-03-01', 1, 0),

-- Branch 6 - Parliament Hill
(6, 6, 'Ulysse', 'Qqch', 'Branch Manager', 'Management', '2017-05-08', 0, 1),
(10, 6, 'Jennifer', 'Martinez', 'Teller', 'Customer Service', '2020-08-22', 1, 0);

-- Update branch managers
UPDATE BRANCH SET manager_id = 3 WHERE branch_id = 1;  -- Abdel manages Downtown Toronto
UPDATE BRANCH SET manager_id = 7 WHERE branch_id = 2;  -- Pavneet manages North York
UPDATE BRANCH SET manager_id = 5 WHERE branch_id = 4;  -- Thomas manages Vancouver
UPDATE BRANCH SET manager_id = 6 WHERE branch_id = 6;  -- Ulysse manages Parliament Hill

-- ============================================
-- INSERT CUSTOMERS
-- ============================================

INSERT INTO CUSTOMER (user_id, first_name, last_name, date_of_birth, birthplace, ssn_masked, phone, address, branch_id) VALUES
(12, 'Emma', 'Wilson', '1992-05-14', 'Toronto', '****7842', '416-555-1001', '45 Adelaide Street East, Toronto, ON M5C 1J7', 1),
(13, 'James', 'Brown', '1988-11-23', 'Montreal', '****3921', '514-555-2001', '1234 Rue Sainte-Catherine, Montreal, QC H3B 1A1', 1),
(14, 'Olivia', 'Davis', '1995-03-07', 'Vancouver', '****5678', '604-555-3001', '789 Granville Street, Vancouver, BC V6Z 1K3', 4),
(15, 'William', 'Garcia', '1990-08-19', 'Calgary', '****9012', '403-555-4001', '321 8th Avenue SW, Calgary, AB T2P 2Y3', 1),
(16, 'Sophia', 'Rodriguez', '1993-12-02', 'Ottawa', '****2345', '613-555-5001', '567 Bank Street, Ottawa, ON K1S 3T4', 6),
(17, 'Liam', 'Martinez', '1987-06-28', 'Toronto', '****6789', '416-555-6001', '890 Queen Street West, Toronto, ON M6J 1G3', 2),
(18, 'Ava', 'Hernandez', '1994-09-15', 'Richmond', '****0123', '604-555-7001', '234 No. 3 Road, Richmond, BC V6Y 2B6', 5),
(19, 'Noah', 'Lopez', '1991-01-30', 'Mississauga', '****4567', '905-555-8001', '678 Dundas Street, Mississauga, ON L5A 1W3', 3),
(20, 'Isabella', 'Gonzalez', '1996-04-11', 'Vancouver', '****8901', '604-555-9001', '456 Robson Street, Vancouver, BC V6B 3A2', 4),
(21, 'Ethan', 'Wilson', '1989-07-25', 'Toronto', '****2346', '416-555-1002', '123 Bloor Street West, Toronto, ON M5S 1M7', 1),
(22, 'Mia', 'Anderson', '1992-10-08', 'North York', '****5679', '416-555-1003', '789 Yonge Street, North York, ON M2M 3K1', 2),
(23, 'Alexander', 'Thomas', '1986-02-17', 'Gatineau', '****9013', '819-555-1004', '345 Boulevard Maloney, Gatineau, QC J8T 3R6', 7),
(24, 'Charlotte', 'Taylor', '1993-05-22', 'Toronto', '****2347', '416-555-1005', '567 College Street, Toronto, ON M6G 1B3', 1),
(25, 'Daniel', 'Moore', '1990-08-03', 'Vancouver', '****6780', '604-555-1006', '890 Davie Street, Vancouver, BC V6Z 2Y1', 4),
(26, 'Amelia', 'Jackson', '1994-11-14', 'Ottawa', '****0124', '613-555-1007', '234 Rideau Street, Ottawa, ON K1N 5Y4', 6),
(27, 'Matthew', 'Martin', '1988-03-29', 'Mississauga', '****4568', '905-555-1008', '678 Lakeshore Road, Mississauga, ON L5G 1H9', 3),
(28, 'Harper', 'Lee', '1995-06-12', 'Richmond', '****8902', '604-555-1009', '456 Westminster Highway, Richmond, BC V6X 1A7', 5),
(29, 'Sebastian', 'Perez', '1991-09-05', 'Toronto', '****2348', '416-555-1010', '123 Dundas Street East, Toronto, ON M5B 2G8', 1),
(30, 'Evelyn', 'Thompson', '1987-12-18', 'North York', '****5681', '416-555-1011', '789 Sheppard Avenue, North York, ON M2N 3A9', 2),
(31, 'Jack', 'White', '1993-01-26', 'Vancouver', '****9014', '604-555-1012', '456 Main Street, Vancouver, BC V6A 2T7', 4);

-- ============================================
-- INSERT ACCOUNTS
-- ============================================

-- Checking Accounts
INSERT INTO ACCOUNT (account_number, customer_id, account_type, balance, status, branch_id, opened_date) VALUES
('CHK-001-2024-0001', 1, 'CHECK', 5432.18, 'ACTIVE', 1, '2024-01-15'),
('CHK-001-2024-0002', 2, 'CHECK', 12876.54, 'ACTIVE', 1, '2024-02-20'),
('CHK-004-2024-0003', 3, 'CHECK', 3201.47, 'ACTIVE', 4, '2024-03-10'),
('CHK-001-2024-0004', 4, 'CHECK', 8765.32, 'ACTIVE', 1, '2024-01-25'),
('CHK-006-2024-0005', 5, 'CHECK', 15432.89, 'ACTIVE', 6, '2024-02-14'),
('CHK-002-2024-0006', 6, 'CHECK', 6543.21, 'ACTIVE', 2, '2023-11-05'),
('CHK-005-2024-0007', 7, 'CHECK', 4321.98, 'ACTIVE', 5, '2024-01-08'),
('CHK-003-2024-0008', 8, 'CHECK', 9876.54, 'ACTIVE', 3, '2023-12-20'),
('CHK-004-2024-0009', 9, 'CHECK', 7654.32, 'ACTIVE', 4, '2024-02-28'),
('CHK-001-2024-0010', 10, 'CHECK', 11234.56, 'ACTIVE', 1, '2023-10-15'),
('CHK-002-2024-0011', 11, 'CHECK', 2345.67, 'ACTIVE', 2, '2024-03-01'),
('CHK-007-2024-0012', 12, 'CHECK', 5678.90, 'ACTIVE', 7, '2024-01-20'),
('CHK-001-2024-0013', 13, 'CHECK', 13456.78, 'ACTIVE', 1, '2023-09-10'),
('CHK-004-2024-0014', 14, 'CHECK', 8901.23, 'ACTIVE', 4, '2024-02-05'),
('CHK-006-2024-0015', 15, 'CHECK', 6789.01, 'ACTIVE', 6, '2023-11-22'),

-- Savings Accounts
('SAV-001-2024-0016', 1, 'SAVING', 25000.00, 'ACTIVE', 1, '2024-01-15'),
('SAV-001-2024-0017', 2, 'SAVING', 48750.25, 'ACTIVE', 1, '2024-02-20'),
('SAV-004-2024-0018', 3, 'SAVING', 15620.88, 'ACTIVE', 4, '2024-03-10'),
('SAV-001-2024-0019', 4, 'SAVING', 32150.40, 'ACTIVE', 1, '2024-01-25'),
('SAV-006-2024-0020', 5, 'SAVING', 67890.12, 'ACTIVE', 6, '2024-02-14'),
('SAV-002-2024-0021', 6, 'SAVING', 19876.54, 'ACTIVE', 2, '2023-11-05'),
('SAV-003-2024-0022', 8, 'SAVING', 41234.67, 'ACTIVE', 3, '2023-12-20'),
('SAV-001-2024-0023', 10, 'SAVING', 55678.90, 'ACTIVE', 1, '2023-10-15'),
('SAV-002-2024-0024', 11, 'SAVING', 12345.00, 'ACTIVE', 2, '2024-03-01'),
('SAV-001-2024-0025', 13, 'SAVING', 78901.23, 'ACTIVE', 1, '2023-09-10');

-- ============================================
-- INSERT CHECK ACCOUNTS
-- ============================================

INSERT INTO CHECK_ACCOUNT (account_id, overdraft_limit, monthly_fee) VALUES
(1, 500.00, 4.95),
(2, 1000.00, 9.95),
(3, 500.00, 4.95),
(4, 1000.00, 9.95),
(5, 2000.00, 14.95),
(6, 500.00, 4.95),
(7, 500.00, 4.95),
(8, 1000.00, 9.95),
(9, 1000.00, 9.95),
(10, 1500.00, 12.95),
(11, 500.00, 4.95),
(12, 500.00, 4.95),
(13, 2000.00, 14.95),
(14, 1000.00, 9.95),
(15, 1000.00, 9.95);

-- ============================================
-- INSERT SAVING ACCOUNTS
-- ============================================

INSERT INTO SAVING_ACCOUNT (account_id, interest_rate, minimum_balance) VALUES
(16, 2.50, 1000.00),
(17, 3.00, 5000.00),
(18, 2.50, 1000.00),
(19, 3.00, 5000.00),
(20, 3.50, 10000.00),
(21, 2.50, 1000.00),
(22, 3.00, 5000.00),
(23, 3.50, 10000.00),
(24, 2.50, 1000.00),
(25, 3.50, 10000.00);

-- ============================================
-- INSERT CARDS
-- ============================================

INSERT INTO CARD (account_id, card_number_masked, card_type, card_holder_name, expiry_date, cvc_hash, status, daily_limit) VALUES
-- Debit cards for checking accounts
(1, '****4521', 'DEBIT', 'EMMA WILSON', '12/26', '$2a$10$randomhash1', 'ACTIVE', 2000.00),
(2, '****7823', 'DEBIT', 'JAMES BROWN', '03/27', '$2a$10$randomhash2', 'ACTIVE', 3000.00),
(3, '****1234', 'DEBIT', 'OLIVIA DAVIS', '06/26', '$2a$10$randomhash3', 'ACTIVE', 2000.00),
(4, '****5678', 'DEBIT', 'WILLIAM GARCIA', '09/27', '$2a$10$randomhash4', 'ACTIVE', 3000.00),
(5, '****9012', 'DEBIT', 'SOPHIA RODRIGUEZ', '11/26', '$2a$10$randomhash5', 'ACTIVE', 5000.00),
(6, '****3456', 'DEBIT', 'LIAM MARTINEZ', '02/27', '$2a$10$randomhash6', 'ACTIVE', 2000.00),
(7, '****7890', 'DEBIT', 'AVA HERNANDEZ', '05/26', '$2a$10$randomhash7', 'ACTIVE', 2000.00),
(8, '****2345', 'DEBIT', 'NOAH LOPEZ', '08/27', '$2a$10$randomhash8', 'ACTIVE', 3000.00),
(9, '****6789', 'DEBIT', 'ISABELLA GONZALEZ', '10/26', '$2a$10$randomhash9', 'ACTIVE', 3000.00),
(10, '****0123', 'DEBIT', 'ETHAN WILSON', '01/27', '$2a$10$randomhash10', 'ACTIVE', 4000.00),

-- Credit cards for some customers
(1, '****8521', 'CREDIT', 'EMMA WILSON', '12/27', '$2a$10$randomhash11', 'ACTIVE', 5000.00),
(2, '****9823', 'CREDIT', 'JAMES BROWN', '03/28', '$2a$10$randomhash12', 'ACTIVE', 10000.00),
(4, '****1678', 'CREDIT', 'WILLIAM GARCIA', '09/28', '$2a$10$randomhash13', 'ACTIVE', 7500.00),
(5, '****2012', 'CREDIT', 'SOPHIA RODRIGUEZ', '11/27', '$2a$10$randomhash14', 'ACTIVE', 15000.00),
(10, '****3123', 'CREDIT', 'ETHAN WILSON', '01/28', '$2a$10$randomhash15', 'ACTIVE', 8000.00);

-- ============================================
-- INSERT TRANSACTIONS
-- ============================================

INSERT INTO `TRANSACTION` (from_account_id, to_account_id, amount, transaction_type, status, description, transaction_date) VALUES
-- Deposits
(NULL, 1, 1000.00, 'DEPOSIT', 'COMPLETED', 'Cash deposit at ATM', '2024-11-01 09:15:00'),
(NULL, 2, 2500.00, 'DEPOSIT', 'COMPLETED', 'Payroll direct deposit', '2024-11-01 08:00:00'),
(NULL, 3, 750.00, 'DEPOSIT', 'COMPLETED', 'Check deposit', '2024-11-02 10:30:00'),
(NULL, 16, 5000.00, 'DEPOSIT', 'COMPLETED', 'Savings deposit', '2024-11-02 14:20:00'),
(NULL, 4, 3200.00, 'DEPOSIT', 'COMPLETED', 'Payroll direct deposit', '2024-11-03 08:00:00'),

-- Withdrawals
(1, NULL, 200.00, 'WITHDRAWAL', 'COMPLETED', 'ATM withdrawal', '2024-11-05 16:45:00'),
(2, NULL, 500.00, 'WITHDRAWAL', 'COMPLETED', 'Branch withdrawal', '2024-11-06 11:20:00'),
(3, NULL, 150.00, 'WITHDRAWAL', 'COMPLETED', 'ATM withdrawal', '2024-11-07 19:30:00'),
(4, NULL, 300.00, 'WITHDRAWAL', 'COMPLETED', 'ATM withdrawal', '2024-11-08 12:15:00'),

-- Transfers
(1, 16, 500.00, 'TRANSFER', 'COMPLETED', 'Transfer to savings', '2024-11-10 10:00:00'),
(2, 17, 1000.00, 'TRANSFER', 'COMPLETED', 'Transfer to savings', '2024-11-10 14:30:00'),
(4, 19, 750.00, 'TRANSFER', 'COMPLETED', 'Transfer to savings', '2024-11-11 09:45:00'),
(2, 1, 250.00, 'TRANSFER', 'COMPLETED', 'Transfer to Emma Wilson', '2024-11-12 11:20:00'),
(4, 2, 500.00, 'TRANSFER', 'COMPLETED', 'Rent payment', '2024-11-13 08:30:00'),
(10, 1, 150.00, 'TRANSFER', 'COMPLETED', 'Birthday gift', '2024-11-14 15:00:00'),

-- Payments
(1, NULL, 125.50, 'PAYMENT', 'COMPLETED', 'Utility bill payment', '2024-11-15 10:15:00'),
(2, NULL, 89.99, 'PAYMENT', 'COMPLETED', 'Internet bill', '2024-11-15 11:30:00'),
(3, NULL, 45.00, 'PAYMENT', 'COMPLETED', 'Phone bill', '2024-11-16 09:20:00'),
(4, NULL, 156.78, 'PAYMENT', 'COMPLETED', 'Credit card payment', '2024-11-16 14:45:00'),
(5, NULL, 234.56, 'PAYMENT', 'COMPLETED', 'Insurance premium', '2024-11-17 10:00:00'),

-- Recent transactions (last few days)
(NULL, 6, 1800.00, 'DEPOSIT', 'COMPLETED', 'Payroll direct deposit', '2024-11-18 08:00:00'),
(NULL, 7, 950.00, 'DEPOSIT', 'COMPLETED', 'Freelance payment', '2024-11-19 13:20:00'),
(6, NULL, 100.00, 'WITHDRAWAL', 'COMPLETED', 'ATM withdrawal', '2024-11-20 16:30:00'),
(7, NULL, 75.00, 'WITHDRAWAL', 'COMPLETED', 'Branch withdrawal', '2024-11-20 10:15:00'),
(8, NULL, 67.89, 'PAYMENT', 'COMPLETED', 'Restaurant payment', '2024-11-21 19:45:00'),
(9, NULL, 120.00, 'PAYMENT', 'COMPLETED', 'Grocery shopping', '2024-11-21 15:30:00'),
(1, 2, 200.00, 'TRANSFER', 'COMPLETED', 'Loan repayment', '2024-11-22 09:00:00'),
(NULL, 10, 2200.00, 'DEPOSIT', 'COMPLETED', 'Payroll direct deposit', '2024-11-22 08:00:00'),
(10, NULL, 180.00, 'WITHDRAWAL', 'COMPLETED', 'ATM withdrawal', '2024-11-22 17:20:00'),
(5, NULL, 299.99, 'PAYMENT', 'COMPLETED', 'Online shopping', '2024-11-23 11:15:00');

-- ============================================
-- INSERT ROLES
-- ============================================

INSERT INTO ROLE (role_name, description) VALUES
('SUPER_ADMIN', 'Full system access with all permissions'),
('ADMIN', 'Administrative access with limited system configuration'),
('BRANCH_MANAGER', 'Branch-level management with employee oversight'),
('TELLER', 'Customer service with transaction processing'),
('CUSTOMER', 'Account holder with self-service access');

-- ============================================
-- INSERT PERMISSIONS
-- ============================================

-- Super Admin permissions
INSERT INTO PERMISSION (role_id, resource, action) VALUES
(1, 'USERS', 'CREATE'), (1, 'USERS', 'READ'), (1, 'USERS', 'UPDATE'), (1, 'USERS', 'DELETE'),
(1, 'ACCOUNTS', 'CREATE'), (1, 'ACCOUNTS', 'READ'), (1, 'ACCOUNTS', 'UPDATE'), (1, 'ACCOUNTS', 'DELETE'),
(1, 'TRANSACTIONS', 'CREATE'), (1, 'TRANSACTIONS', 'READ'), (1, 'TRANSACTIONS', 'UPDATE'), (1, 'TRANSACTIONS', 'DELETE'),
(1, 'BRANCHES', 'CREATE'), (1, 'BRANCHES', 'READ'), (1, 'BRANCHES', 'UPDATE'), (1, 'BRANCHES', 'DELETE'),
(1, 'EMPLOYEES', 'CREATE'), (1, 'EMPLOYEES', 'READ'), (1, 'EMPLOYEES', 'UPDATE'), (1, 'EMPLOYEES', 'DELETE'),

-- Admin permissions
(2, 'USERS', 'READ'), (2, 'USERS', 'UPDATE'),
(2, 'ACCOUNTS', 'READ'), (2, 'ACCOUNTS', 'UPDATE'),
(2, 'TRANSACTIONS', 'READ'),
(2, 'BRANCHES', 'READ'),
(2, 'EMPLOYEES', 'READ'),

-- Branch Manager permissions
(3, 'ACCOUNTS', 'CREATE'), (3, 'ACCOUNTS', 'READ'), (3, 'ACCOUNTS', 'UPDATE'),
(3, 'TRANSACTIONS', 'CREATE'), (3, 'TRANSACTIONS', 'READ'), (3, 'TRANSACTIONS', 'UPDATE'),
(3, 'EMPLOYEES', 'READ'), (3, 'EMPLOYEES', 'UPDATE'),
(3, 'CUSTOMERS', 'CREATE'), (3, 'CUSTOMERS', 'READ'), (3, 'CUSTOMERS', 'UPDATE'),

-- Teller permissions
(4, 'ACCOUNTS', 'READ'),
(4, 'TRANSACTIONS', 'CREATE'), (4, 'TRANSACTIONS', 'READ'),
(4, 'CUSTOMERS', 'READ'),

-- Customer permissions
(5, 'ACCOUNTS', 'READ'),
(5, 'TRANSACTIONS', 'READ');

-- ============================================
-- INSERT AUTHENTICATION SESSIONS (Recent)
-- ============================================

INSERT INTO AUTHENTICATION (user_id, session_token, expires_at, ip_address, user_agent) VALUES
(12, 'sess_emma_' || hex(randomblob(16)), datetime('now', '+1 day'), '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'),
(13, 'sess_james_' || hex(randomblob(16)), datetime('now', '+1 day'), '192.168.1.101', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36'),
(3, 'sess_abdel_' || hex(randomblob(16)), datetime('now', '+8 hours'), '10.0.0.50', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'),
(4, 'sess_biko_' || hex(randomblob(16)), datetime('now', '+8 hours'), '10.0.0.51', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36');

-- ============================================
-- INSERT AUDIT LOG (Recent activities)
-- ============================================

INSERT INTO AUDIT_LOG (user_id, action, resource, resource_id, old_value, new_value, ip_address) VALUES
(3, 'ACCOUNT_CREATED', 'ACCOUNT', '1', NULL, '{"account_number":"CHK-001-2024-0001","customer_id":1}', '10.0.0.50'),
(3, 'TRANSACTION_PROCESSED', 'TRANSACTION', '1', NULL, '{"type":"DEPOSIT","amount":1000.00}', '10.0.0.50'),
(4, 'TRANSACTION_PROCESSED', 'TRANSACTION', '2', NULL, '{"type":"DEPOSIT","amount":2500.00}', '10.0.0.51'),
(12, 'LOGIN', 'USER', '12', NULL, '{"timestamp":"2024-11-23T10:30:00"}', '192.168.1.100'),
(13, 'LOGIN', 'USER', '13', NULL, '{"timestamp":"2024-11-23T09:15:00"}', '192.168.1.101'),
(12, 'TRANSFER_INITIATED', 'TRANSACTION', '10', NULL, '{"from":1,"to":16,"amount":500.00}', '192.168.1.100'),
(7, 'ACCOUNT_UPDATED', 'ACCOUNT', '6', '{"balance":5543.21}', '{"balance":6543.21}', '10.0.0.52'),
(1, 'USER_CREATED', 'USER', '31', NULL, '{"username":"jack.white","email":"jack.white@email.com"}', '10.0.0.1');

-- ============================================
-- VERIFICATION
-- ============================================

SELECT 'Database populated successfully!' as status;
SELECT 'Total users: ' || COUNT(*) as info FROM USER;
SELECT 'Total customers: ' || COUNT(*) as info FROM CUSTOMER;
SELECT 'Total employees: ' || COUNT(*) as info FROM EMPLOYEE;
SELECT 'Total accounts: ' || COUNT(*) as info FROM ACCOUNT;
SELECT 'Total transactions: ' || COUNT(*) as info FROM `TRANSACTION`;
