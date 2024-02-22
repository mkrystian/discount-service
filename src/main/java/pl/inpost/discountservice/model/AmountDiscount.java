package pl.inpost.discountservice.model;

import java.math.BigDecimal;
import java.util.Optional;

import static pl.inpost.discountservice.model.Discount.DiscountType.AMOUNT;

public record AmountDiscount(
        String name,
        Optional<Integer> minQuantity,
        Optional<Integer> maxQuantity,
        BigDecimal unitPriceDiscountAmount
) implements Discount {
    @Override
    public DiscountType type() {
        return AMOUNT;
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal unitPrice, int quantity) {
        return unitPrice.subtract(unitPriceDiscountAmount).multiply(BigDecimal.valueOf(quantity));
    }
}
