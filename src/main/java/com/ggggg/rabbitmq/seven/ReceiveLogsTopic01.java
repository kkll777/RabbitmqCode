/*-----------------------------------------------------------------------
 * Copyright(c) 2022 Acer Inc. All Rights Reserved.
 * This software is proprietary to and embodies the confidential technology
 * of Acer Inc.. Possession, use or copying of this software
 * and media is authorized only pursuant to a valid written license from Acer
 * Inc. or an authorized sublicensor.
-----------------------------------------------------------------------*/

/*-----------------------------------------------------------------------
 * ProductName      : 工廠登記與管理系統再造
 * File Code        : ReceiveLogsTopic01
 * File Name        : ReceiveLogsTopic01
 * Description      : 
 * Dev Ver          : JDK 11
 * Author           : Frank Huang
 * Create Date      : 2023/01/11
-----------------------------------------------------------------------*/
package com.ggggg.rabbitmq.seven;

import com.ggggg.rabbitmq.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 主題交換機及相關對列
 * 主題式交換機的routingKey類似regexp，只要有匹配到即可收到消息
 * 架構:
 *              |---    *.orange.*    --->  Q1  ------>     Consumer 1
 * P ---> X-----|
 *              |----   *.*.rabbit  ------> Q2  -------->   Consumer2
 *              |---    lazy.#      ---↑
 */
public class ReceiveLogsTopic01 {
    //  交換機名稱
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

//        聲明對列
        String quere1 = "Q1";
        channel.queueDeclare(quere1, false,false,false,null)    ;
        channel.queueBind(quere1, EXCHANGE_NAME, "*.orange.*");
        System.out.println("等待接收消息");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody(), StandardCharsets.UTF_8));
            System.out.println("接收對列: " + quere1 + ", 綁定鍵: " + message.getEnvelope().getRoutingKey());
        };

        channel.basicConsume(quere1, true, deliverCallback, (s, e) -> {});
    }
}
