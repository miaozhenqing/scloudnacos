package com.zq.msg;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface MsgChannels {
    @Input("myInput")
    SubscribableChannel myInput();
}
