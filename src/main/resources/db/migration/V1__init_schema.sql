-- ========================================
-- USER ACCOUNT
-- ========================================
CREATE TABLE IF NOT EXISTS user_account (
    id CHAR(36) NOT NULL PRIMARY KEY,

    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(256) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,

    user_type VARCHAR(30) NOT NULL,

    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,

    verification_token VARCHAR(255),
    token_expiration TIMESTAMP NULL,

    reset_password_token VARCHAR(255),
    reset_password_expiry TIMESTAMP NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP
    );

-- ========================================
-- USER ROLES
-- ========================================
CREATE TABLE IF NOT EXISTS user_roles (
    user_id CHAR(36) NOT NULL,
    role VARCHAR(30) NOT NULL,

    PRIMARY KEY (user_id, role),

    CONSTRAINT fk_user_roles_user
    FOREIGN KEY (user_id)
    REFERENCES user_account(id)
    ON DELETE CASCADE
    );

-- ========================================
-- OTP CODES
-- ========================================
CREATE TABLE IF NOT EXISTS otp_codes (
    id CHAR(36) NOT NULL PRIMARY KEY,
    user_id CHAR(36) NOT NULL,
    code VARCHAR(20) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_otp_user
    FOREIGN KEY (user_id)
    REFERENCES user_account(id)
    ON DELETE CASCADE
    );

-- ========================================
-- REFRESH TOKEN
-- ========================================
CREATE TABLE IF NOT EXISTS refresh_token (
    id CHAR(36) NOT NULL PRIMARY KEY,
    user_id CHAR(36) NOT NULL,
    token VARCHAR(512) NOT NULL UNIQUE,
    expires_at TIMESTAMP NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_refresh_token_user
    FOREIGN KEY (user_id)
    REFERENCES user_account(id)
    ON DELETE CASCADE
    );

-- ========================================
-- USER ADDRESS
-- ========================================
CREATE TABLE IF NOT EXISTS user_address (
    id CHAR(36) NOT NULL PRIMARY KEY,
    user_id CHAR(36) NOT NULL UNIQUE,

    address_line1 VARCHAR(120) NOT NULL,
    address_line2 VARCHAR(120),
    city VARCHAR(80) NOT NULL,
    state VARCHAR(80) NOT NULL,
    country VARCHAR(80) NOT NULL,
    postal_code VARCHAR(6) NOT NULL,

    CONSTRAINT fk_user_address_user
    FOREIGN KEY (user_id)
    REFERENCES user_account(id)
    ON DELETE CASCADE
    );

-- ========================================
-- USER PROFILE
-- ========================================
CREATE TABLE IF NOT EXISTS user_profile (
    id CHAR(36) NOT NULL PRIMARY KEY,
    user_id CHAR(36) NOT NULL UNIQUE,

    gender VARCHAR(30),
    date_of_birth DATE,
    profile_image_url VARCHAR(500),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_profile_user
    FOREIGN KEY (user_id)
    REFERENCES user_account(id)
    ON DELETE CASCADE
    );

-- ========================================
-- GARAGE
-- ========================================
CREATE TABLE IF NOT EXISTS garage (
    id CHAR(36) NOT NULL PRIMARY KEY,
    owner_id CHAR(36) NOT NULL UNIQUE,

    name VARCHAR(120) NOT NULL,
    description VARCHAR(500),
    image_url VARCHAR(500),

    address_line1 VARCHAR(120) NOT NULL,
    address_line2 VARCHAR(120),
    city VARCHAR(80) NOT NULL,
    state VARCHAR(80) NOT NULL,
    country VARCHAR(80) NOT NULL,
    postal_code VARCHAR(6) NOT NULL,

    garage_type VARCHAR(20) NOT NULL,
    opening_time VARCHAR(20),
    closing_time VARCHAR(20),

    gst_number VARCHAR(20),

    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,

    license_document_url VARCHAR(500) NOT NULL,
    gst_certificate_url VARCHAR(500) NOT NULL,
    owner_id_proof_url VARCHAR(500) NOT NULL,
    garage_photo_url VARCHAR(500) NOT NULL,
    additional_doc_url VARCHAR(500),

    garage_status VARCHAR(20) NOT NULL DEFAULT 'CLOSED',
    kyc_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    verification_reason VARCHAR(500),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_garage_owner
    FOREIGN KEY (owner_id)
    REFERENCES user_account(id)
    ON DELETE CASCADE
    );

-- ========================================
-- VEHICLE
-- ========================================
CREATE TABLE IF NOT EXISTS vehicle (
    id CHAR(36) NOT NULL PRIMARY KEY,
    user_id CHAR(36) NOT NULL,

    vehicle_number VARCHAR(20) NOT NULL UNIQUE,
    type VARCHAR(20) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    manufacturing_year INT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_vehicle_user
    FOREIGN KEY (user_id)
    REFERENCES user_account(id)
    ON DELETE CASCADE
    );

-- ========================================
-- MECHANIC (FIXED)
-- ========================================
CREATE TABLE IF NOT EXISTS mechanic (
                                        id CHAR(36) NOT NULL PRIMARY KEY,

    user_account_id CHAR(36) NOT NULL UNIQUE,

    first_login BOOLEAN NOT NULL DEFAULT TRUE,
    status VARCHAR(30) NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE,

    garage_id CHAR(36) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_mechanic_user_account
    FOREIGN KEY (user_account_id)
    REFERENCES user_account(id)
    ON DELETE CASCADE,

    CONSTRAINT fk_mechanic_garage
    FOREIGN KEY (garage_id)
    REFERENCES garage(id)
    ON DELETE CASCADE
    );

-- ========================================
-- BOOKING
-- ========================================
CREATE TABLE IF NOT EXISTS booking (
    id CHAR(36) NOT NULL PRIMARY KEY,

    user_id CHAR(36) NOT NULL,
    garage_id CHAR(36) NOT NULL,
    vehicle_id CHAR(36) NOT NULL,
    mechanic_id CHAR(36) NULL,

    booking_type VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    payment_status VARCHAR(30) NOT NULL,

    problem_description VARCHAR(500),
    estimated_price DECIMAL(10,2) NOT NULL,

    pickup_latitude DOUBLE NOT NULL,
    pickup_longitude DOUBLE NOT NULL,
    pickup_address VARCHAR(255),

    scheduled_at TIMESTAMP NOT NULL,
    accepted_at TIMESTAMP NULL,
    started_at TIMESTAMP NULL,
    completed_at TIMESTAMP NULL,

    rejection_reason VARCHAR(255),
    cancellation_reason VARCHAR(255),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_booking_user
    FOREIGN KEY (user_id)
    REFERENCES user_account(id)
    ON DELETE CASCADE,

    CONSTRAINT fk_booking_garage
    FOREIGN KEY (garage_id)
    REFERENCES garage(id)
    ON DELETE CASCADE,

    CONSTRAINT fk_booking_vehicle
    FOREIGN KEY (vehicle_id)
    REFERENCES vehicle(id)
    ON DELETE CASCADE,

    CONSTRAINT fk_booking_mechanic
    FOREIGN KEY (mechanic_id)
    REFERENCES mechanic(id)
    ON DELETE SET NULL
    );
