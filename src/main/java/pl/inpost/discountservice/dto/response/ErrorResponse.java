package pl.inpost.discountservice.dto.response;

import static pl.inpost.discountservice.dto.response.Response.ResponseType.ERROR;

public record ErrorResponse<T>(
        String error) implements Response<T> {
    @Override
    public ResponseType type() {
        return ERROR;
    }
}
