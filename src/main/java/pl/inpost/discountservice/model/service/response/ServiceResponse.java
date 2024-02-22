package pl.inpost.discountservice.model.service.response;

public sealed interface ServiceResponse permits RequestErrorServiceResponse, SuccessServiceResponse {
}
