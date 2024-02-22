package pl.inpost.discountservice.dto.response;

import static pl.inpost.discountservice.dto.response.Response.ResponseType.NOT_FOUND;

public record NotFoundResponse<T>() implements Response<T> {
    @Override
    public ResponseType type() {
        return NOT_FOUND;
    }
}
