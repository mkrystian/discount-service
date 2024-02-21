package pl.inpost.discountservice.service;

import pl.inpost.discountservice.dto.ProductOrder;
import pl.inpost.discountservice.model.ProductPrice;
import reactor.core.publisher.Mono;

public interface ProductPriceService {

    Mono<ProductPrice> calculatePrice(ProductOrder productOrder);
}
