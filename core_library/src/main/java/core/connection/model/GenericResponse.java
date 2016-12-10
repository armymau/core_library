package core.connection.model;

import com.google.gson.Gson;

import java.io.Serializable;

public class GenericResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private int status;
    private String status_description;
    private Object data;
    private String load_time;
    private ErrorObject error;
    private MessageObject message;
    private String user_token;

    public GenericResponse() {
        this.status = 200;
        this.status_description = null;
        this.data = null;
        this.load_time = null;
        this.error = null;
        this.message = null;
        this.user_token = null;
    }

    public GenericResponse(int status, String status_description, Gson data, String load_time, ErrorObject error, MessageObject message, String user_token) {
        this.status = status;
        this.status_description = status_description;
        this.data = data;
        this.load_time = load_time;
        this.error = error;
        this.message = message;
        this.user_token = user_token;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus_description() {
        return status_description;
    }

    public void setStatus_description(String status_description) {
        this.status_description = status_description;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getLoad_time() {
        return load_time;
    }

    public void setLoad_time(String load_time) {
        this.load_time = load_time;
    }

    public ErrorObject getError() {
        return error;
    }

    public void setError(ErrorObject error) {
        this.error = error;
    }

    public MessageObject getMessage() {
        return message;
    }

    public void setMessage(MessageObject message) {
        this.message = message;
    }

    public String toString() {
        return "\n" +
                "status = " + status +
                ", status_description = " + status_description +
                ", data = " + data +
                ", load_time = " + load_time +
                ", error = " + error +
                ", message = " + message +
                ", user_token = " + user_token +
                "\n";
    }
}
