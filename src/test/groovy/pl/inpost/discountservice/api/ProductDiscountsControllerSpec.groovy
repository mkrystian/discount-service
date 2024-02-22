package pl.inpost.discountservice.api

import pl.inpost.discountservice.IntegrationTest
import pl.inpost.discountservice.dto.response.ErrorResponse
import pl.inpost.discountservice.dto.response.SuccessResponseWithData
import pl.inpost.discountservice.model.AmountDiscount
import pl.inpost.discountservice.model.ProductDiscounts

import static java.util.UUID.randomUUID
import static org.springframework.http.MediaType.APPLICATION_JSON

class ProductDiscountsControllerSpec extends IntegrationTest {

    def "should return 200 OK when getting product discounts"() {
        given:
        def productId = randomUUID()
        productDiscountsResource.addProductDiscounts(productId, new ProductDiscounts(productId, [new AmountDiscount(
                "testDiscount", Optional.of(10), Optional.of(100), BigDecimal.ONE)] as Set)).block()


        when:
        def response = webTestClient
                .get()
                .uri(buildUri("/product-discounts/{productId}", productId))
                .accept(APPLICATION_JSON)
                .header("Content-Type", APPLICATION_JSON.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(SuccessResponseWithData)
                .returnResult()

        then:
        response.responseBody.data()['productId'] == productId.toString()
    }

    def "should return 404 if not found discounts for given productId"() {
        given:
        def productId = randomUUID()

        expect:
        webTestClient
                .get()
                .uri(buildUri("/product-discounts/{productId}", productId))
                .accept(APPLICATION_JSON)
                .header("Content-Type", APPLICATION_JSON.toString())
                .exchange()
                .expectStatus().isNotFound()
    }

    def "should return 400 if productId is not valid"() {
        expect:
        webTestClient
                .get()
                .uri(buildUri("/product-discounts/{productId}", "invalid"))
                .accept(APPLICATION_JSON)
                .header("Content-Type", APPLICATION_JSON.toString())
                .exchange()
                .expectStatus()
                .isBadRequest()
    }

    def "should return 400 if there are overlapping discounts ranges"() {
        given:
        def productId = randomUUID()
        def productDiscounts = new ProductDiscounts(productId, [new AmountDiscount(
                "testDiscount", Optional.of(10), Optional.of(100), BigDecimal.ONE),
                                                                new AmountDiscount("testDiscount2", Optional.of(50), Optional.of(150), BigDecimal.ONE)] as Set)
        expect:
        def result = webTestClient
                .put()
                .uri(buildUri("/product-discounts/{productId}", productId))
                .accept(APPLICATION_JSON)
                .bodyValue(productDiscounts)
                .header("Content-Type", APPLICATION_JSON.toString())
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorResponse)
                .returnResult()

        result.responseBody.error() == "Discounts must not overlap: testDiscount and testDiscount2"
    }

    def "should add product discounts"() {
        given:
        def productId = randomUUID()
        def productDiscounts = new ProductDiscounts(productId, [new AmountDiscount(
                "testDiscount", Optional.of(10), Optional.of(100), BigDecimal.ONE)] as Set)

        when:
        webTestClient
                .put()
                .uri(buildUri("/product-discounts/{productId}", productId))
                .accept(APPLICATION_JSON)
                .bodyValue(productDiscounts)
                .header("Content-Type", APPLICATION_JSON.toString())
                .exchange()
                .expectStatus()
                .isOk()

        then:
        productDiscountsResource.getProductDiscounts(productId).block() == productDiscounts
    }

    def "should override product discounts"() {
        given:
        def productId = randomUUID()
        productDiscountsResource.addProductDiscounts(productId, new ProductDiscounts(productId, [new AmountDiscount(
                "testDiscount1", Optional.of(1), Optional.of(100), BigDecimal.TEN)] as Set)).block()
        def newProductDiscounts = new ProductDiscounts(productId, [new AmountDiscount(
                "testDiscount2", Optional.of(10), Optional.of(100), BigDecimal.ONE)] as Set)

        when:
        webTestClient
                .put()
                .uri(buildUri("/product-discounts/{productId}", productId))
                .accept(APPLICATION_JSON)
                .bodyValue(newProductDiscounts)
                .header("Content-Type", APPLICATION_JSON.toString())
                .exchange()
                .expectStatus()
                .isOk()

        then:
        productDiscountsResource.getProductDiscounts(productId).block() == newProductDiscounts
    }
}