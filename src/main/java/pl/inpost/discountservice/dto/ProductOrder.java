package pl.inpost.discountservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductOrder(
        UUID productId,
        int quantity,
        BigDecimal unitPrice
) {
}
