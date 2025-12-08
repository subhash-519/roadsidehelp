package com.roadsidehelp.api.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Embeddable Money value object.
 * Stored with precision/scale defined in column properties.
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Money implements Serializable {

    @NotNull
    @Digits(integer = 18, fraction = 4)
    @Column(name = "amount", precision = 22, scale = 4, nullable = false)
    private BigDecimal amount;

    /**
     * ISO 4217 3-letter currency code, uppercase (e.g., INR).
     */
    @NotNull
    @Pattern(regexp = "^[A-Z]{3}$", message = "currency must be a 3-letter ISO code")
    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    public static Money of(BigDecimal amount, String currency) {
        return Money.builder().amount(amount).currency(currency).build();
    }

    public Money add(Money other) {
        assertSameCurrency(other);
        return Money.of(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        assertSameCurrency(other);
        return Money.of(this.amount.subtract(other.amount), this.currency);
    }

    private void assertSameCurrency(Money other) {
        if (other == null) throw new IllegalArgumentException("other must not be null");
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("currency mismatch: " + this.currency + " vs " + other.currency);
        }
    }
}
