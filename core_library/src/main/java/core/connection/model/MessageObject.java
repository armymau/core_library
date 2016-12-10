package core.connection.model;

import java.io.Serializable;

public class MessageObject implements Serializable {

    private static final long serialVersionUID = 1L;
    private String msg;
    private String description;

    public MessageObject(String msg, String description) {
        this.msg = msg;
        this.description = description;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
