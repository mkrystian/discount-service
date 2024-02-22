package pl.inpost.discountservice.api;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.inpost.discountservice.dto.response.ErrorResponse;
import pl.inpost.discountservice.dto.response.NotFoundResponse;
import pl.inpost.discountservice.dto.response.Response;
import pl.inpost.discountservice.dto.response.SuccessResponse;
import pl.inpost.discountservice.dto.response.SuccessResponseWithData;
import pl.inpost.discountservice.model.ProductDiscounts;
import pl.inpost.discountservice.model.service.response.RequestErrorServiceResponse;
import pl.inpost.discountservice.model.service.response.SuccessServiceResponse;
import pl.inpost.discountservice.service.ProductDiscountService;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static reactor.core.publisher.Mono.fromCallable;

@RestController
@AllArgsConstructor
@RequestMapping("product-discounts")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProductDiscountsController {

    ProductDiscountService productDiscountService;

    @GetMapping(value = "/{productId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Response<ProductDiscounts>>> getProductDiscounts(@PathVariable UUID productId) {
        return productDiscountService.getProductDiscounts(productId)
                .map(productDiscounts ->
                        ResponseEntity.ok((Response<ProductDiscounts>) new SuccessResponseWithData<>(productDiscounts)))
                .defaultIfEmpty(ResponseEntity.status(NOT_FOUND.value()).body(new NotFoundResponse<>()));
    }

    @PutMapping("/{productId}")
    public Mono<ResponseEntity<Response<Void>>> updateProductDiscounts(@PathVariable UUID productId, @RequestBody ProductDiscounts productDiscounts) {
        return productDiscountService.addProductDiscounts(productId, productDiscounts)
                .map(addProductResponse -> switch (addProductResponse) {
                    case SuccessServiceResponse ignored -> ResponseEntity.ok(new SuccessResponse<>());
                    case RequestErrorServiceResponse incorrectDiscountsAddProductResponse ->
                            ResponseEntity.badRequest().body(new ErrorResponse<>(incorrectDiscountsAddProductResponse.message()));
                });
    }

    @DeleteMapping("/{productId}")
    public Mono<ResponseEntity<Response<Void>>> removeProductDiscounts(@PathVariable UUID productId) {
        return productDiscountService.removeProductDiscounts(productId)
                .then(fromCallable(() -> ResponseEntity.ok(new SuccessResponse<>())));
    }
}
