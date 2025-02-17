package aist.cargo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class VerifyOtpRequest {

    @Schema(description = "Код подтверждения", example = "1234")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
