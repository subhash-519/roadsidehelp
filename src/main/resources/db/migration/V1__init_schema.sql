-- ========================================
-- USER ACCOUNT TABLE
-- ========================================
CREATE TABLE IF NOT EXISTS user_account (
    id CHAR(36) NOT NULL PRIMARY KEY,

    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(256) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,

    is_active TINYINT(1) DEFAULT 1,
    verification_token VARCHAR(255),
    token_expiration TIMESTAMP NULL,
    is_verified TINYINT(1) DEFAULT 0,

    reset_password_token VARCHAR(255) NULL,
    reset_password_expiry TIMESTAMP NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


-- ========================================
-- USER ROLES TABLE (ElementCollection)
-- ========================================
CREATE TABLE IF NOT EXISTS user_roles (
    user_id CHAR(36) NOT NULL,
    role VARCHAR(255) NOT NULL,

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id)
        REFERENCES user_account(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_user_roles_user ON user_roles(user_id);

-- ========================================
-- OTP TABLE
-- ========================================
CREATE TABLE IF NOT EXISTS otp_codes (
    id CHAR(36) NOT NULL PRIMARY KEY,
    user_id CHAR(36) NOT NULL,
    code VARCHAR(20) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used TINYINT(1) DEFAULT 0,

    CONSTRAINT fk_otp_user
        FOREIGN KEY (user_id)
        REFERENCES user_account(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_otp_user ON otp_codes(user_id);

-- ========================================
-- REFRESH TOKEN
-- ========================================
CREATE TABLE IF NOT EXISTS refresh_token (
    id CHAR(36) NOT NULL PRIMARY KEY,
    user_id CHAR(36) NOT NULL,
    token VARCHAR(512) NOT NULL UNIQUE,
    expires_at TIMESTAMP,
    revoked TINYINT(1) DEFAULT 0,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id)
        REFERENCES user_account(id) ON DELETE CASCADE
);

-- ========================================
-- USER ADDRESS TABLE
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

CREATE INDEX idx_user_address_user ON user_address(user_id);

-- ========================================
-- USER PROFILE TABLE
-- ========================================
CREATE TABLE IF NOT EXISTS user_profile (
    id CHAR(36) NOT NULL PRIMARY KEY,
    user_id CHAR(36) NOT NULL UNIQUE,

    gender VARCHAR(30),
    date_of_birth DATE,
    profile_image_url VARCHAR(500),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_profile_user
        FOREIGN KEY (user_id)
        REFERENCES user_account(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_user_profile_user ON user_profile(user_id);

