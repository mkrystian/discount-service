package pl.inpost.discountservice.service;

import org.junit.jupiter.api.Test;
import pl.inpost.discountservice.model.AmountDiscount;
import pl.inpost.discountservice.model.ProductDiscounts;
import pl.inpost.discountservice.model.service.response.RequestErrorServiceResponse;
import pl.inpost.discountservice.model.service.response.ServiceResponse;
import pl.inpost.discountservice.model.service.response.SuccessServiceResponse;
import pl.inpost.discountservice.resource.ProductDiscountsResource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductDiscountServiceImplTest {

    private final ProductDiscountsResource productDiscountsResource = mock(ProductDiscountsResource.class);
    private final ProductDiscountService productDiscountService = new ProductDiscountServiceImpl(productDiscountsResource);

    @Test
    void shouldGetProductDiscounts() {
        // Given
        var productId = randomUUID();
        var productDiscounts = new ProductDiscounts(productId, null);
        when(productDiscountsResource.getProductDiscounts(productId)).thenReturn(Mono.just(productDiscounts));

        // When
        Mono<ProductDiscounts> result = productDiscountService.getProductDiscounts(productId);
        // Then
        StepVerifier.create(result)
                .expectNext(productDiscounts)
                .verifyComplete();
    }

    @Test
    void shouldRemoveProductDiscounts() {
        // Given
        var productId = randomUUID();
        when(productDiscountsResource.removeProductDiscounts(productId)).thenReturn(Mono.empty());

        // When
        Mono<Void> result = productDiscountService.removeProductDiscounts(productId);
        // Then
        StepVerifier.create(result)
                .verifyComplete();
        verify(productDiscountsResource, times(1)).removeProductDiscounts(productId);
    }

    @Test
    void shouldAddProductDiscounts() {
        // Given
        var productId = randomUUID();
        var productDiscounts = new ProductDiscounts(productId, Set.of(new AmountDiscount("discount", empty(), empty(), BigDecimal.ONE)));
        when(productDiscountsResource.addProductDiscounts(productId, productDiscounts)).thenReturn(Mono.empty());

        // When
        Mono<ServiceResponse> result = productDiscountService.addProductDiscounts(productId, productDiscounts);
        // Then
        StepVerifier.create(result)
                .expectNextMatches(response -> response instanceof SuccessServiceResponse)
                .verifyComplete();
        verify(productDiscountsResource, times(1)).addProductDiscounts(productId, productDiscounts);
    }

    @Test
    void shouldNotAddProductDiscountsIfMoreThanOneDiscountWithoutLowerBand() {
        // Given
        var productId = randomUUID();
        var productDiscounts = new ProductDiscounts(productId, Set.of(new AmountDiscount("discount", empty(), Optional.of(1), BigDecimal.ONE),
                new AmountDiscount("discount2", empty(), Optional.of(10), BigDecimal.ONE)));
        when(productDiscountsResource.addProductDiscounts(productId, productDiscounts)).thenReturn(Mono.empty());

        // When
        Mono<ServiceResponse> result = productDiscountService.addProductDiscounts(productId, productDiscounts);
        // Then
        StepVerifier.create(result)
                .expectNextMatches(response -> response instanceof RequestErrorServiceResponse)
                .verifyComplete();
        verify(productDiscountsResource, never()).addProductDiscounts(productId, productDiscounts);
    }

    @Test
    void shouldNotAddProductDiscountsIfMoreThanOneDiscountWithoutUpperBand() {
        // Given
        var productId = randomUUID();
        var productDiscounts = new ProductDiscounts(productId, Set.of(new AmountDiscount("discount", Optional.of(1), empty(), BigDecimal.ONE),
                new AmountDiscount("discount2", Optional.of(10), empty(), BigDecimal.ONE)));
        when(productDiscountsResource.addProductDiscounts(productId, productDiscounts)).thenReturn(Mono.empty());

        // When
        Mono<ServiceResponse> result = productDiscountService.addProductDiscounts(productId, productDiscounts);
        // Then
        StepVerifier.create(result)
                .expectNextMatches(response -> response instanceof RequestErrorServiceResponse)
                .verifyComplete();
        verify(productDiscountsResource, never()).addProductDiscounts(productId, productDiscounts);
    }

    @Test
    void shouldNotAddProductDiscountsIfRangesAreOverlapping() {
        // Given
        var productId = randomUUID();
        var productDiscounts = new ProductDiscounts(productId, Set.of(new AmountDiscount("discount", Optional.of(1), Optional.of(10), BigDecimal.ONE),
                new AmountDiscount("discount2", Optional.of(9), Optional.of(20), BigDecimal.ONE)));
        when(productDiscountsResource.addProductDiscounts(productId, productDiscounts)).thenReturn(Mono.empty());

        // When
        Mono<ServiceResponse> result = productDiscountService.addProductDiscounts(productId, productDiscounts);
        // Then
        StepVerifier.create(result)
                .expectNextMatches(response -> response instanceof RequestErrorServiceResponse)
                .verifyComplete();
        verify(productDiscountsResource, never()).addProductDiscounts(productId, productDiscounts);
    }
}