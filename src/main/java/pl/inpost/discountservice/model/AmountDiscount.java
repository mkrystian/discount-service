package pl.inpost.discountservice.model;

import java.math.BigDecimal;
import java.util.Optional;

public record AmountDiscount(
        String name,
        Optional<Integer> minQuantity,
        Optional<Integer> maxQuantity,
        BigDecimal unitPriceDiscountAmount
) implements Discount {
    @Override
    public BigDecimal calculateDiscount(BigDecimal unitPrice, int quantity) {
        return unitPrice.subtract(unitPriceDiscountAmount).multiply(BigDecimal.valueOf(quantity));
    }
}
