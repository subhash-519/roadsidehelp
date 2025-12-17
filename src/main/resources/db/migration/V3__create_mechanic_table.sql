-- ========================================
-- V3: CREATE MECHANIC TABLE
-- ========================================

CREATE TABLE IF NOT EXISTS mechanic (
                                        id CHAR(36) NOT NULL PRIMARY KEY,

    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL,
    available TINYINT(1) NOT NULL DEFAULT 1,

    garage_id CHAR(36) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_mechanic_garage
    FOREIGN KEY (garage_id)
    REFERENCES garage(id)
    ON DELETE CASCADE
    );

-- ========================================
-- INDEXES (Performance Optimized)
-- ========================================

-- Fetch mechanics by garage
CREATE INDEX idx_mechanic_garage
    ON mechanic (garage_id);

-- Fetch available mechanics per garage
CREATE INDEX idx_mechanic_garage_available
    ON mechanic (garage_id, available);
