package pl.inpost.discountservice.service;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import pl.inpost.discountservice.model.ProductDiscounts;
import pl.inpost.discountservice.resource.ProductDiscountsResource;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProductDiscountServiceImpl implements ProductDiscountService {

    ProductDiscountsResource productDiscountsResource;

    @Override
    public Mono<Void> addProductDiscounts(UUID productId, ProductDiscounts productDiscounts) {
        return Mono.fromCallable(() -> {
            verifyDiscounts(productDiscounts);
            return null;
        }).flatMap(ignored -> productDiscountsResource.addProductDiscounts(productId, productDiscounts));
    }

    @Override
    public Mono<Void> removeProductDiscounts(UUID productId) {
        return productDiscountsResource.removeProductDiscounts(productId);
    }

    @Override
    public Mono<ProductDiscounts> getProductDiscounts(UUID productId) {
        return productDiscountsResource.getProductDiscounts(productId);
    }

    private void verifyDiscounts(ProductDiscounts productDiscounts) {
        if (hasManyDiscountsWithoutLowerQuantityLimit(productDiscounts)) {
            throw new IllegalArgumentException("Only one discount can have no lower limit for quantity");
        }
        if (hasManyDiscountsWithoutUpperQuantityLimit(productDiscounts)) {
            throw new IllegalArgumentException("Only one discount can have no upper limit for quantity");
        }
        final var sortedDiscounts = productDiscounts.discounts().stream().sorted((d1, d2) -> {
            if (d1.minQuantity().isEmpty() && d2.minQuantity().isPresent()) {
                return 1;
            }
            if (d1.maxQuantity().isEmpty() && d2.maxQuantity().isPresent()) {
                return 1;
            }
            return d1.minQuantity().get().compareTo(d2.minQuantity().get());
        }).toList();

        for (int i = 0; i < sortedDiscounts.size() - 2; i++) {
            if (sortedDiscounts.get(i + 2).maxQuantity().isEmpty())
                return;

            if (sortedDiscounts.get(i).maxQuantity().get() >= sortedDiscounts.get(i + 1).minQuantity().get()) {
                throw new IllegalArgumentException(
                        String.format("Discounts must not overlap: %s and %s", sortedDiscounts.get(i).name(), sortedDiscounts.get(i + 1).name()));
            }
        }
    }

    private static boolean hasManyDiscountsWithoutLowerQuantityLimit(ProductDiscounts productDiscounts) {
        return productDiscounts.discounts().stream()
                .filter(discount -> discount.minQuantity().isEmpty())
                .count() > 1;
    }

    private static boolean hasManyDiscountsWithoutUpperQuantityLimit(ProductDiscounts productDiscounts) {
        return productDiscounts.discounts().stream()
                .filter(discount -> discount.maxQuantity().isEmpty())
                .count() > 1;
    }
}
