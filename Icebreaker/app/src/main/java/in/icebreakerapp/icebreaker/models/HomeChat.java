package in.icebreakerapp.icebreaker.models;

import java.io.Serializable;

/**
 * Created by anip on 28/08/16.
 */
public class HomeChat {
    private String enroll;
    private int chat_id;
    private String nick_name;

    public String getEnroll() {
        return enroll;
    }

    public void setEnroll(String enroll) {
        this.enroll = enroll;
    }

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name)
    {
        this.nick_name = nick_name;
    }

}
