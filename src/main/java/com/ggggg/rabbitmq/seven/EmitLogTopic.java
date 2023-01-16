/*-----------------------------------------------------------------------
 * Copyright(c) 2022 Acer Inc. All Rights Reserved.
 * This software is proprietary to and embodies the confidential technology
 * of Acer Inc.. Possession, use or copying of this software
 * and media is authorized only pursuant to a valid written license from Acer
 * Inc. or an authorized sublicensor.
-----------------------------------------------------------------------*/

/*-----------------------------------------------------------------------
 * ProductName      : 工廠登記與管理系統再造
 * File Code        : EmitLogTopic
 * File Name        : EmitLogTopic
 * Description      : 
 * Dev Ver          : JDK 11
 * Author           : Frank Huang
 * Create Date      : 2023/01/11
-----------------------------------------------------------------------*/
package com.ggggg.rabbitmq.seven;

import com.ggggg.rabbitmq.util.RabbitUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class EmitLogTopic {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();

        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit", "Q1Q2");
        bindingKeyMap.put("lazy.orange.elephant", "Q1Q2");
        bindingKeyMap.put("quick.orange.fox", "Q1");
        bindingKeyMap.put("lazy.brown.fox", "Q2");
        bindingKeyMap.put("lazy.pink.rabbit", "Q2");
        bindingKeyMap.put("quick.brown.fox", "N");
        bindingKeyMap.put("quick.orange.male.rabbit", "N");
        bindingKeyMap.put("lazy.orange.male.rabbit", "Q2");

        for (Map.Entry<String, String> bindingKeysEntry: bindingKeyMap.entrySet()) {
            String routinKey = bindingKeysEntry.getKey();
            String message = bindingKeysEntry.getValue();

            channel.basicPublish(EXCHANGE_NAME, routinKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生閃者發出信息: " + message);
        }
    }
}
