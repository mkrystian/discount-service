package pl.inpost.discountservice.service;

import pl.inpost.discountservice.model.ProductDiscounts;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductDiscountService {

    Mono<Void> addProductDiscounts(UUID productId, ProductDiscounts productDiscounts);

    Mono<Void> removeProductDiscounts(UUID productId);

    Mono<ProductDiscounts> getProductDiscounts(UUID productId);
}
