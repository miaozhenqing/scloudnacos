package com.zq.service;

import com.alibaba.fastjson.JSONObject;
import com.zq.msg.MsgChannels;
import org.example.dto.MsgDTO;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(MsgChannels.class)
public class MsgConsumerService {
    @StreamListener("myInput")
    public void receive(MsgDTO msg) {
        System.out.println("receive msg:" + JSONObject.toJSONString(msg));
    }
}
