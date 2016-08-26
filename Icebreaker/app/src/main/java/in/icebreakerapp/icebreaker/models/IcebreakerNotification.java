package in.icebreakerapp.icebreaker.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by anip on 24/08/16.
 */
public class IcebreakerNotification implements Serializable {
    @SerializedName("to")
    private String to;
    @SerializedName("from")
    private String from;
    @SerializedName("message")
    private String message;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
