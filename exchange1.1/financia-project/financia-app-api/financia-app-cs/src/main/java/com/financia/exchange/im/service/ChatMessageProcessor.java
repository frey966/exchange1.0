package com.financia.exchange.im.service;

import com.alibaba.fastjson.JSONObject;
import com.financia.common.redis.service.RedisService;
import org.jim.core.ImChannelContext;
import org.jim.core.packets.ChatBody;
import org.jim.core.packets.User;
import org.jim.core.utils.JsonKit;
import org.jim.server.processor.chat.BaseAsyncChatMessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ChatMessageProcessor extends BaseAsyncChatMessageProcessor {

    private static Logger logger = LoggerFactory.getLogger(ChatMessageProcessor.class);
    private RedisService redisService;
    private static final String SUFFIX = ":";
    private static final String REDIS_IM_USER = "redis_im_user";
    public ChatMessageProcessor (RedisService redisService) {
        this.redisService = redisService;
    }
    @Override
    public void doProcess(ChatBody chatBody, ImChannelContext imChannelContext){
        logger.info("默认交由业务处理聊天记录示例,ChatMessageProcessor:{}", JsonKit.toJSONString(chatBody));
        if (chatBody.getTo().equals("admin")) {
            User user = redisService.getCacheObject(REDIS_IM_USER + SUFFIX + chatBody.getFrom());
            saveRedis(chatBody, user);
        }else {
            User user = redisService.getCacheObject(REDIS_IM_USER + SUFFIX + chatBody.getTo());
            saveRedis(chatBody, user);
        }
    }

    /**
     * 单独保存用户最后一条聊天记录，方便前端展示
     * 更新最后一次聊天时间方便排序
     * @param chatBody
     * @param user
     */
    private void saveRedis(ChatBody chatBody, User user) {
        user.setCreateTime(new Date().getTime());
        JSONObject extras = user.getExtras();
        if (extras != null) {
            extras.put("lastChat", chatBody.getContent());
        } else {
            JSONObject newExtras = new JSONObject();
            newExtras.put("lastChat", chatBody.getContent());
            user.setExtras(newExtras);
        }
        redisService.setCacheObject(REDIS_IM_USER + SUFFIX + user.getUserId(), user, 5l, TimeUnit.DAYS);
    }
}
