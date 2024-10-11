package com.zq.service;

import com.zq.msg.MsgChannels;
import org.example.dto.MsgDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

@EnableBinding(MsgChannels.class)
public class MsgProducerService {
    @Autowired
    private MsgChannels msgChannels;

    public void send(String msg) {
        msgChannels.myOutput().send(MessageBuilder.withPayload(new MsgDTO(msg)).build());
    }
}
