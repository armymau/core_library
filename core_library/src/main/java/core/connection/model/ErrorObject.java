package core.connection.model;

import java.io.Serializable;

public class ErrorObject implements Serializable {

    private static final long serialVersionUID = 1L;
    private String message;
    private String description;

    public ErrorObject(String message, String description) {
        this.message = message;
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
