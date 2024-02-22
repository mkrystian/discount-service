package pl.inpost.discountservice.dto.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ErrorResponse.class, name = "ERROR"),
        @JsonSubTypes.Type(value = SuccessResponse.class, name = "SUCCESS"),
        @JsonSubTypes.Type(value = SuccessResponseWithData.class, name = "SUCCESS_WITH_DATA")
})
public sealed interface Response<T> permits ErrorResponse, NotFoundResponse, SuccessResponse, SuccessResponseWithData {
    ResponseType type();

    enum ResponseType {
        SUCCESS,
        ERROR,
        SUCCESS_WITH_DATA,
        NOT_FOUND
    }
}
