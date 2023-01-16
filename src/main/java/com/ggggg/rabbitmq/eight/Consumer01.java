/*-----------------------------------------------------------------------
 * Copyright(c) 2022 Acer Inc. All Rights Reserved.
 * This software is proprietary to and embodies the confidential technology
 * of Acer Inc.. Possession, use or copying of this software
 * and media is authorized only pursuant to a valid written license from Acer
 * Inc. or an authorized sublicensor.
-----------------------------------------------------------------------*/

/*-----------------------------------------------------------------------
 * ProductName      : 工廠登記與管理系統再造
 * File Code        : Consumer01
 * File Name        : Consumer01
 * Description      : 
 * Dev Ver          : JDK 11
 * Author           : Frank Huang
 * Create Date      : 2023/01/11
-----------------------------------------------------------------------*/
package com.ggggg.rabbitmq.eight;

import com.ggggg.rabbitmq.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 死信對列
 * 死信原因有三種:
 * 1. TTL過期
 * 2. 存取被拒
 * 3. 超過對列最大茶度
 *
 * 重點: normal queue與死信對列綁定是透過argument參數
 *架構
 *                       type=direct
 * P ------|------->    normalExchange  ------ zhangsan --------> normal-queue------    |   ----->  C1
 *         |                                                            |               |
 *         |                                                            |               |
 *         |                                                            |               |
 *         |                                                            ↓   成為死信     |
 *         |                                                        dead-exchange       |
 *         |                                                            ↓   lisi        |
 *         |                                                         dead-queue---------|----->     C2
 *         ------------------------------------------------------------------------------
 */
public class Consumer01 {
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_QUEUE = "dead_queue";
    public static final String NORMAL_QUEUE = "normal_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        //  設置死信routingKey
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "lisi");
        //  設置最大長度
        arguments.put("x-max-length", 6);
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
        //  綁定普通交換機與對列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        //  綁訂死信交換機與對列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            if ("info5".equals(msg)) {
                System.out.println("Consumer01接收到的消息為: " + msg + ", 此消息被拒絕");
                //  第二參數為是否塞回原本對列, false 跑到死信
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("接收到信息: " + msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }

        };
        //  首度拒絕需要開啟手動應答(autoAck 為 false)
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, c->{} );
    }

}
