package pl.inpost.discountservice


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder
import pl.inpost.discountservice.resource.TestProductDiscountsResource
import spock.lang.Specification

@AutoConfigureWebTestClient
@ContextConfiguration(classes = [DiscountServiceApplication.class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTest extends Specification {

    @Autowired
    protected TestProductDiscountsResource productDiscountsResource

    @Autowired
    protected TestRestTemplate restTemplate

    @LocalServerPort
    private int port

    @Autowired
    protected WebTestClient webTestClient

    protected String buildUri(String path, Object... args) {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path(path)
                .buildAndExpand(args)
                .toUriString()
    }

    private void cleanup() {
        productDiscountsResource.clear()
    }
}
