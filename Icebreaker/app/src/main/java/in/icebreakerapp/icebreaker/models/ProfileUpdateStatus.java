package in.icebreakerapp.icebreaker.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by DravitLochan on 12-04-2017.
 */

public class ProfileUpdateStatus implements Serializable {

    @SerializedName("status")

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
