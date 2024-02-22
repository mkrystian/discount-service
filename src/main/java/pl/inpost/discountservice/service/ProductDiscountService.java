package pl.inpost.discountservice.service;

import pl.inpost.discountservice.model.ProductDiscounts;
import pl.inpost.discountservice.model.service.response.ServiceResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductDiscountService {

    Mono<ServiceResponse> addProductDiscounts(UUID productId, ProductDiscounts productDiscounts);

    Mono<Void> removeProductDiscounts(UUID productId);

    Mono<ProductDiscounts> getProductDiscounts(UUID productId);
}
