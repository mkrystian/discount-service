package pl.inpost.discountservice.api

import pl.inpost.discountservice.IntegrationTest
import pl.inpost.discountservice.dto.request.ProductOrder
import pl.inpost.discountservice.dto.response.SuccessResponseWithData
import pl.inpost.discountservice.model.AmountDiscount
import pl.inpost.discountservice.model.ProductDiscounts

import static java.util.UUID.randomUUID
import static org.springframework.http.MediaType.APPLICATION_JSON

class ProductPriceControllerSpec extends IntegrationTest {

    def "should return original price if no discounts set"() {
        given:
        def productId = randomUUID()
        def productOrder = new ProductOrder(productId, 10, BigDecimal.TEN)

        when:
        def response = webTestClient
                .post()
                .uri(buildUri("/product-price"))
                .accept(APPLICATION_JSON)
                .header("Content-Type", APPLICATION_JSON.toString())
                .bodyValue(productOrder)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SuccessResponseWithData)
                .returnResult()

        then:
        response.responseBody.data()['productId'] == productId.toString()
        response.responseBody.data()['discountedPrice'] == 100
    }

    def "should apply corresponding discount and return discounted price"() {
        given:
        def productId = randomUUID()
        def productOrder = new ProductOrder(productId, 10, BigDecimal.TEN)
        productDiscountsResource.addProductDiscounts(productId, new ProductDiscounts(productId, [
                new AmountDiscount("testDiscount1", Optional.empty(), Optional.of(5), BigDecimal.TEN),
                new AmountDiscount("testDiscount2", Optional.of(5), Optional.of(10), BigDecimal.ONE),
                new AmountDiscount("testDiscount3", Optional.of(10), Optional.empty(), BigDecimal.ZERO),
        ] as Set)).block()

        when:
        def response = webTestClient
                .post()
                .uri(buildUri("/product-price"))
                .accept(APPLICATION_JSON)
                .header("Content-Type", APPLICATION_JSON.toString())
                .bodyValue(productOrder)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SuccessResponseWithData)
                .returnResult()

        then:
        response.responseBody.data()['productId'] == productId.toString()
        response.responseBody.data()['discountedPrice'] == 90
        response.responseBody.data()['appliedDiscount']['name'] == "testDiscount2"
    }

    def "should return price 0 if discounted price is negative"() {
        given:
        def productId = randomUUID()
        def productOrder = new ProductOrder(productId, 1, BigDecimal.TEN)
        productDiscountsResource.addProductDiscounts(productId, new ProductDiscounts(productId, [
                new AmountDiscount("testDiscount", Optional.empty(), Optional.empty(), BigDecimal.TEN),
        ] as Set)).block()

        when:
        def response = webTestClient
                .post()
                .uri(buildUri("/product-price"))
                .accept(APPLICATION_JSON)
                .header("Content-Type", APPLICATION_JSON.toString())
                .bodyValue(productOrder)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SuccessResponseWithData)
                .returnResult()

        then:
        response.responseBody.data()['productId'] == productId.toString()
        response.responseBody.data()['discountedPrice'] == 0
        response.responseBody.data()['appliedDiscount']['name'] == "testDiscount"
    }
}