package pl.inpost.discountservice.model;

import java.math.BigDecimal;
import java.util.Optional;

public interface Discount {
    String name();

    Optional<Integer> minQuantity();

    Optional<Integer> maxQuantity();

    default boolean isApplicable(int quantity) {
        return (minQuantity().isEmpty() || minQuantity().get() <= quantity) &&
                (maxQuantity().isEmpty() || maxQuantity().get() >= quantity);
    }

    BigDecimal calculateDiscount(BigDecimal unitPrice, int quantity);
}
