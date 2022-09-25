package com.financia.exchange.im.service;

import org.jim.core.ImChannelContext;
import org.jim.core.packets.ChatBody;
import org.jim.core.utils.JsonKit;
import org.jim.server.processor.chat.BaseAsyncChatMessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatMessageProcessor extends BaseAsyncChatMessageProcessor {

    private static Logger logger = LoggerFactory.getLogger(ChatMessageProcessor.class);

    @Override
    public void doProcess(ChatBody chatBody, ImChannelContext imChannelContext){
        logger.info("默认交由业务处理聊天记录示例,ChatMessageProcessor:{}", JsonKit.toJSONString(chatBody));
    }
}
