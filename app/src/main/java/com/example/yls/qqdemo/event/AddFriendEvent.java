package com.example.yls.qqdemo.event;

/**
 * Created by 雪无痕 on 2017/1/3.
 */

public class AddFriendEvent{

    public AddFriendEvent(String userName,String reason){
        this.userName=userName;
        this.reason=reason;
    }

        public String userName;
        public String reason;
}
