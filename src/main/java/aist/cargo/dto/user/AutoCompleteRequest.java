package aist.cargo.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutoCompleteRequest {

    private String fromWhere;
    private String toWhere;

    public AutoCompleteRequest(String fromWhere, String toWhere) {
        this.fromWhere = fromWhere;
        this.toWhere = toWhere;
    }

    public String fromWhere() {
        return this.fromWhere;
    }

    public String toWhere() {
        return this.toWhere;
    }
}

