package pl.inpost.discountservice.service;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import pl.inpost.discountservice.dto.ProductOrder;
import pl.inpost.discountservice.model.ProductPrice;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProductPriceServiceImpl implements ProductPriceService {

    ProductDiscountService productDiscountService;

    @Override
    public Mono<ProductPrice> calculatePrice(ProductOrder productOrder) {
        return productDiscountService.getProductDiscounts(productOrder.productId())
                .mapNotNull(productDiscounts ->
                        productDiscounts.findDiscount(productOrder.quantity())
                                .map(discount -> {
                                    BigDecimal discountedPrice = discount.calculateDiscount(productOrder.unitPrice(), productOrder.quantity());
                                    return new ProductPrice(productOrder.productId(),
                                            discountedPrice.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : discountedPrice,
                                            discount);
                                }).orElse(null)
                ).defaultIfEmpty(new ProductPrice(productOrder.productId(),
                        productOrder.unitPrice().multiply(BigDecimal.valueOf(productOrder.quantity())),
                        null));
    }
}
