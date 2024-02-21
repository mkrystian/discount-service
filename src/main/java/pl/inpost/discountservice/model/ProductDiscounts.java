package pl.inpost.discountservice.model;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public record ProductDiscounts(
        UUID productId,
        Set<Discount> discounts) {

    public Optional<Discount> findDiscount(int quantity) {
        return discounts.stream()
                .filter(discount -> discount.isApplicable(quantity))
                .findFirst();
    }
}
