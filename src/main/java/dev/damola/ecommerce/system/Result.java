package dev.damola.ecommerce.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Result {
    private boolean status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    public Result(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
    public Result(boolean status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
