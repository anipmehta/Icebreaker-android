package in.icebreakerapp.icebreaker.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anip on 18/10/16.
 */
public class ContactModel {
    @SerializedName("status")
    private String status;
    @SerializedName("profile")
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
