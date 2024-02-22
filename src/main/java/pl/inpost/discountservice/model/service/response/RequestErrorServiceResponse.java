package pl.inpost.discountservice.model.service.response;

public record RequestErrorServiceResponse(
        String message
) implements ServiceResponse {
}
