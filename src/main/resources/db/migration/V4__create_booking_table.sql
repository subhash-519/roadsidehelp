-- ========================================
-- V4: CREATE BOOKING TABLE
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

-- ========================================
-- INDEXES (MATCH ENTITY @Table indexes)
-- ========================================
CREATE INDEX idx_booking_user ON booking(user_id);
CREATE INDEX idx_booking_garage ON booking(garage_id);
CREATE INDEX idx_booking_status ON booking(status);
CREATE INDEX idx_booking_mechanic ON booking(mechanic_id);
