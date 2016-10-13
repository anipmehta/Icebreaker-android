package in.icebreakerapp.icebreaker;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Streams;

import com.google.gson.JsonObject;
import java.io.Serializable;

/**
 * Created by anip on 11/10/16.
 */
public class VerifyStatus implements Serializable {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private JsonObject profile;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public JsonObject getProfile() {
        return profile;
    }

    public void setProfile(JsonObject profile) {
        this.profile = profile;
    }
}
