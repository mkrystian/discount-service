package pl.inpost.discountservice.api;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.inpost.discountservice.dto.ProductOrder;
import pl.inpost.discountservice.model.ProductPrice;
import pl.inpost.discountservice.service.ProductPriceService;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("product-price")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProductPriceController {

    ProductPriceService productPriceService;

    @PostMapping
    public Mono<ProductPrice> getProductDiscounts(@RequestBody ProductOrder productOrder) {
        return productPriceService.calculatePrice(productOrder);
    }
}
