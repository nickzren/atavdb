package org.atavdb.model;

/**
 *
 * @author nick
 */
public class MessageResponse {

    private int status;
    private String message;

    public MessageResponse() {
        super();
    }

    public MessageResponse(int status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorResponse [status=" + status + ", message=" + message + "]";
    }
}
