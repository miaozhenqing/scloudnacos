package com.zq.msg;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MsgChannels {
    @Output("myOutput")
    MessageChannel myOutput();
}
