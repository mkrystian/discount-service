package pl.inpost.discountservice.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;
import java.util.Optional;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AmountDiscount.class, name = "AMOUNT"),
        @JsonSubTypes.Type(value = PercentageDiscount.class, name = "PERCENTAGE")
})

public sealed interface Discount permits AmountDiscount, PercentageDiscount {
    String name();

    Optional<Integer> minQuantity();

    Optional<Integer> maxQuantity();

    DiscountType type();

    default boolean isApplicable(int quantity) {
        return (minQuantity().isEmpty() || minQuantity().get() <= quantity) &&
                (maxQuantity().isEmpty() || maxQuantity().get() >= quantity);
    }

    BigDecimal calculateDiscount(BigDecimal unitPrice, int quantity);

    enum DiscountType {
        AMOUNT,
        PERCENTAGE
    }
}
