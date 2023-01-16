/*-----------------------------------------------------------------------
 * Copyright(c) 2022 Acer Inc. All Rights Reserved.
 * This software is proprietary to and embodies the confidential technology
 * of Acer Inc.. Possession, use or copying of this software
 * and media is authorized only pursuant to a valid written license from Acer
 * Inc. or an authorized sublicensor.
-----------------------------------------------------------------------*/

/*-----------------------------------------------------------------------
 * ProductName      : 工廠登記與管理系統再造
 * File Code        : Producer
 * File Name        : Producer
 * Description      : 
 * Dev Ver          : JDK 11
 * Author           : Frank Huang
 * Create Date      : 2023/01/11
-----------------------------------------------------------------------*/
package com.ggggg.rabbitmq.eight;

import com.ggggg.rabbitmq.util.RabbitUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();
        //  延遲消息，設置TTL時間
//        AMQP.BasicProperties properties = new AMQP.BasicProperties()
//                .builder().expiration("10000")
//                .build();

        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
//            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", properties, message.getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes(StandardCharsets.UTF_8));
        }

    }
}
