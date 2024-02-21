package pl.inpost.discountservice.model;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductPrice(
        UUID productId,
        BigDecimal discountedPrice,
        Discount appliedDiscount
) {
}
