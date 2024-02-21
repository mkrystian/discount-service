package pl.inpost.discountservice.api;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.inpost.discountservice.model.ProductDiscounts;
import pl.inpost.discountservice.service.ProductDiscountService;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("product-discounts")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProductDiscountsController {

    ProductDiscountService productDiscountService;

    @GetMapping("/{productId}")
    public Mono<ProductDiscounts> getProductDiscounts(@PathVariable UUID productId) {
        return productDiscountService.getProductDiscounts(productId);
    }

    @PutMapping("/{productId}")
    public Mono<Void> updateProductDiscounts(@PathVariable UUID productId, ProductDiscounts productDiscounts) {
        return productDiscountService.addProductDiscounts(productId, productDiscounts);
    }

    @DeleteMapping("/{productId}")
    public Mono<Void> removeProductDiscounts(@PathVariable UUID productId) {
        return productDiscountService.removeProductDiscounts(productId);
    }
}
