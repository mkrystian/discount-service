package pl.inpost.discountservice.dto.response;

import static pl.inpost.discountservice.dto.response.Response.ResponseType.SUCCESS;

public record SuccessResponse<T>() implements Response<T> {
    @Override
    public ResponseType type() {
        return SUCCESS;
    }
}
