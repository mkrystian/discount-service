package pl.inpost.discountservice.resource


import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Primary
@Component
class TestProductDiscountsResource extends ProductDiscountsInMemoryResource {

    public void clear() {
        super.productIdDiscounts.clear()
    }
}
