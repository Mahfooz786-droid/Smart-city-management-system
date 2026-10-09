-- ============================================================================
-- Smart City Management System - canonical MySQL schema
-- ----------------------------------------------------------------------------
-- Apply this script to a fresh MySQL database before running the app:
--
--     mysql -u root -p < database/schema.sql
--     mysql -u root -p smart_city < database/seed.sql
--
-- The script is also loaded by the H2 in-memory test database (in MySQL
-- compatibility mode) so that unit tests run without a live MySQL instance.
-- ============================================================================

DROP TABLE IF EXISTS feedback;
DROP TABLE IF EXISTS complaints;
DROP TABLE IF EXISTS citizens;
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS admins;

-- ----------------------------------------------------------------------------
-- citizens: registered Smart City end users.
-- password_hash is a BCrypt hash; the plaintext password is never stored.
-- ----------------------------------------------------------------------------
CREATE TABLE citizens (
    citizen_id     INT          NOT NULL AUTO_INCREMENT,
    name           VARCHAR(100) NOT NULL,
    email          VARCHAR(150) NOT NULL,
    phone          VARCHAR(20)  NOT NULL,
    password_hash  VARCHAR(100) NOT NULL,
    address        VARCHAR(255) NOT NULL,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_citizens PRIMARY KEY (citizen_id),
    CONSTRAINT uq_citizens_email UNIQUE (email),
    CONSTRAINT ck_citizens_phone CHECK (CHAR_LENGTH(phone) >= 10)
);

-- ----------------------------------------------------------------------------
-- complaints: grievances filed by a citizen.
-- status is constrained to Pending / In Progress / Resolved.
-- ----------------------------------------------------------------------------
CREATE TABLE complaints (
    complaint_id   INT          NOT NULL AUTO_INCREMENT,
    citizen_id     INT          NOT NULL,
    subject        VARCHAR(200) NOT NULL,
    description    TEXT         NOT NULL,
    status         VARCHAR(20)  NOT NULL DEFAULT 'Pending',
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_complaints PRIMARY KEY (complaint_id),
    CONSTRAINT fk_complaints_citizen FOREIGN KEY (citizen_id)
        REFERENCES citizens (citizen_id) ON DELETE CASCADE,
    CONSTRAINT ck_complaints_status CHECK
        (status IN ('Pending', 'In Progress', 'Resolved'))
);

CREATE INDEX idx_complaints_citizen ON complaints (citizen_id);
CREATE INDEX idx_complaints_status  ON complaints (status);

-- ----------------------------------------------------------------------------
-- feedback: ratings + comments submitted by citizens.
-- ----------------------------------------------------------------------------
CREATE TABLE feedback (
    feedback_id   INT          NOT NULL AUTO_INCREMENT,
    citizen_id    INT          NOT NULL,
    rating        INT          NOT NULL,
    comment       TEXT         NOT NULL,
    feedback_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_feedback PRIMARY KEY (feedback_id),
    CONSTRAINT fk_feedback_citizen FOREIGN KEY (citizen_id)
        REFERENCES citizens (citizen_id) ON DELETE CASCADE,
    CONSTRAINT ck_feedback_rating CHECK (rating BETWEEN 1 AND 5)
);

CREATE INDEX idx_feedback_citizen ON feedback (citizen_id);

-- ----------------------------------------------------------------------------
-- services: catalog of municipal services offered to citizens.
-- ----------------------------------------------------------------------------
CREATE TABLE services (
    service_id   INT          NOT NULL AUTO_INCREMENT,
    service_name VARCHAR(100) NOT NULL,
    description  TEXT         NOT NULL,
    department   VARCHAR(100) NOT NULL,
    status       VARCHAR(20)  NOT NULL DEFAULT 'Active',
    CONSTRAINT pk_services PRIMARY KEY (service_id),
    CONSTRAINT ck_services_status CHECK
        (status IN ('Active', 'Maintenance', 'Deprecated'))
);

-- ----------------------------------------------------------------------------
-- admins: staff accounts (currently unused — admin/admin123 is hardcoded
-- for backward-compat with the original LoginFrame. Provided here so the
-- schema is ready when admin accounts are migrated to the DB).
-- ----------------------------------------------------------------------------
CREATE TABLE admins (
    admin_id       INT          NOT NULL AUTO_INCREMENT,
    username       VARCHAR(50)  NOT NULL,
    name           VARCHAR(100) NOT NULL,
    email          VARCHAR(150) NOT NULL,
    password_hash  VARCHAR(100) NOT NULL,
    role           VARCHAR(20)  NOT NULL DEFAULT 'ADMIN',
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_admins PRIMARY KEY (admin_id),
    CONSTRAINT uq_admins_username UNIQUE (username)
);
