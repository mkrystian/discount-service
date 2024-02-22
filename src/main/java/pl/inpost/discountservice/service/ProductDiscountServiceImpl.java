package pl.inpost.discountservice.service;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import pl.inpost.discountservice.model.ProductDiscounts;
import pl.inpost.discountservice.model.service.response.RequestErrorServiceResponse;
import pl.inpost.discountservice.model.service.response.ServiceResponse;
import pl.inpost.discountservice.model.service.response.SuccessServiceResponse;
import pl.inpost.discountservice.resource.ProductDiscountsResource;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProductDiscountServiceImpl implements ProductDiscountService {

    ProductDiscountsResource productDiscountsResource;

    @Override
    public Mono<ServiceResponse> addProductDiscounts(UUID productId, ProductDiscounts productDiscounts) {
        return Mono.fromCallable(() -> verifyDiscounts(productDiscounts))
                .flatMap(verificationResult -> {
                    if (verificationResult.isPresent()) {
                        return Mono.fromCallable(() -> new RequestErrorServiceResponse(verificationResult.get()));
                    }
                    return productDiscountsResource.addProductDiscounts(productId, productDiscounts)
                            .then(Mono.fromCallable(SuccessServiceResponse::new));
                });
    }

    @Override
    public Mono<Void> removeProductDiscounts(UUID productId) {
        return productDiscountsResource.removeProductDiscounts(productId);
    }

    @Override
    public Mono<ProductDiscounts> getProductDiscounts(UUID productId) {
        return productDiscountsResource.getProductDiscounts(productId);
    }

    private Optional<String> verifyDiscounts(ProductDiscounts productDiscounts) {
        if (hasManyDiscountsWithoutLowerQuantityLimit(productDiscounts)) {
            return Optional.of("Only one discount can have no lower limit for quantity");
        }
        if (hasManyDiscountsWithoutUpperQuantityLimit(productDiscounts)) {
            return Optional.of("Only one discount can have no upper limit for quantity");
        }
        return verifyIfHasOverlappingRanges(productDiscounts);
    }

    private static Optional<String> verifyIfHasOverlappingRanges(ProductDiscounts productDiscounts) {
        final var sortedDiscounts = productDiscounts.discounts().stream().sorted((d1, d2) -> {
            if (d1.minQuantity().isEmpty()) {
                return -1;
            }
            if (d2.minQuantity().isEmpty()) {
                return 1;
            }
            return d1.minQuantity().get().compareTo(d2.minQuantity().get());
        }).toList();

        for (int i = 0; i < sortedDiscounts.size() - 1; i++) {
            if (sortedDiscounts.get(i + 1).maxQuantity().isEmpty())
                return empty();

            if (sortedDiscounts.get(i).maxQuantity().get() >= sortedDiscounts.get(i + 1).minQuantity().get()) {
                return Optional.of(
                        String.format("Discounts must not overlap: %s and %s", sortedDiscounts.get(i).name(), sortedDiscounts.get(i + 1).name()));
            }
        }
        return empty();
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
