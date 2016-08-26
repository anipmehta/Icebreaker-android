package in.icebreakerapp.icebreaker.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by anip on 12/08/16.
 */
public class SignupStatus implements Serializable {
    @SerializedName("response")
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
