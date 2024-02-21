package pl.inpost.discountservice.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public record PercentageDiscount(
        String name,
        Optional<Integer> minQuantity,
        Optional<Integer> maxQuantity,
        BigDecimal discountPercentage
) implements Discount {

    private static final BigDecimal PERCENTAGE_DIVISOR = BigDecimal.valueOf(100);
    private static final int SCALE = 2;

    @Override
    public BigDecimal calculateDiscount(BigDecimal unitPrice, int quantity) {
        return unitPrice.multiply(discountPercentage)
                .multiply(BigDecimal.valueOf(quantity))
                .divide(PERCENTAGE_DIVISOR, SCALE, RoundingMode.HALF_UP);
    }
}
