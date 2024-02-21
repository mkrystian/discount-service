package pl.inpost.discountservice.resource;

import org.springframework.stereotype.Component;
import pl.inpost.discountservice.model.ProductDiscounts;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProductDiscountsInMemoryResource implements ProductDiscountsResource {

    private final Map<UUID, ProductDiscounts> productIdDiscounts = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> addProductDiscounts(UUID productId, ProductDiscounts productDiscounts) {
        return Mono.fromCallable(() -> {
            productIdDiscounts.put(productId, productDiscounts);
            return null;
        });
    }

    @Override
    public Mono<Void> removeProductDiscounts(UUID productId) {
        return Mono.fromCallable(() -> {
            productIdDiscounts.remove(productId);
            return null;
        });
    }

    @Override
    public Mono<ProductDiscounts> getProductDiscounts(UUID productId) {
        return Mono.fromCallable(() -> productIdDiscounts.get(productId));
    }
}
