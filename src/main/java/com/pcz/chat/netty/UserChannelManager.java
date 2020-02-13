package com.pcz.chat.netty;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author picongzhi
 */
@Slf4j
public class UserChannelManager {
    private static Map<String, Channel> manager = new ConcurrentHashMap<>();

    public static void put(String senderId, Channel channel) {
        manager.put(senderId, channel);
    }

    public static Channel get(String senderId) {
        return manager.get(senderId);
    }

    public static void output() {
        manager.forEach((senderId, channel) -> {
            log.info("userId: " + senderId + ", channelId: " + channel.id().asLongText());
        });
    }
}
