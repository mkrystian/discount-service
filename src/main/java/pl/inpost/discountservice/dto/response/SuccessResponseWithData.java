package pl.inpost.discountservice.dto.response;

import static pl.inpost.discountservice.dto.response.Response.ResponseType.SUCCESS_WITH_DATA;

public record SuccessResponseWithData<T>(
        T data
) implements Response<T> {
    @Override
    public ResponseType type() {
        return SUCCESS_WITH_DATA;
    }
}
