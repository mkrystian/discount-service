package pl.inpost.discountservice.service;

import org.junit.jupiter.api.Test;
import pl.inpost.discountservice.dto.request.ProductOrder;
import pl.inpost.discountservice.model.AmountDiscount;
import pl.inpost.discountservice.model.ProductDiscounts;
import pl.inpost.discountservice.model.ProductPrice;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductPriceServiceImplTest {
    private final ProductDiscountService productDiscountService = mock(ProductDiscountService.class);
    private final ProductPriceService productPriceService = new ProductPriceServiceImpl(productDiscountService);

    @Test
    void shouldReturnPriceWithoutDiscountIfNoDiscountSet() {
        // Given
        var productOrder = new ProductOrder(
                UUID.randomUUID(),
                10,
                BigDecimal.ONE
        );

        when(productDiscountService.getProductDiscounts(productOrder.productId()))
                .thenReturn(Mono.empty());
        // When
        Mono<ProductPrice> result = productPriceService.calculatePrice(productOrder);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(price -> price.discountedPrice().equals(BigDecimal.TEN))
                .verifyComplete();
    }

    @Test
    void shouldReturnZeroPriceIfDiscountedPriceIsNegative() {
        // Given
        var productOrder = new ProductOrder(
                UUID.randomUUID(),
                10,
                BigDecimal.ONE
        );

        when(productDiscountService.getProductDiscounts(productOrder.productId()))
                .thenReturn(Mono.just(new ProductDiscounts(productOrder.productId(), Set.of(
                        new AmountDiscount("discount", Optional.of(1), Optional.of(100), BigDecimal.TEN)
                ))));
        // When
        Mono<ProductPrice> result = productPriceService.calculatePrice(productOrder);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(price -> price.discountedPrice().equals(BigDecimal.ZERO))
                .verifyComplete();
    }

    @Test
    void shouldReturnPriceWithDiscount() {
        // Given
        var productOrder = new ProductOrder(
                UUID.randomUUID(),
                10,
                BigDecimal.TEN
        );

        when(productDiscountService.getProductDiscounts(productOrder.productId()))
                .thenReturn(Mono.just(new ProductDiscounts(productOrder.productId(), Set.of(
                        new AmountDiscount("discount", Optional.of(1), Optional.of(100), BigDecimal.ONE)
                ))));
        // When
        Mono<ProductPrice> result = productPriceService.calculatePrice(productOrder);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(price -> price.discountedPrice().equals(BigDecimal.valueOf(90)))
                .verifyComplete();
    }
}