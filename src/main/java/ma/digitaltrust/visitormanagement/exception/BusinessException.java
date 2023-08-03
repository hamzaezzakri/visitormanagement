package ma.digitaltrust.visitormanagement.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {

    private String message;

    public BusinessException(String message) {
        this.message = message;
    }
}
