/*-----------------------------------------------------------------------
 * Copyright(c) 2022 Acer Inc. All Rights Reserved.
 * This software is proprietary to and embodies the confidential technology
 * of Acer Inc.. Possession, use or copying of this software
 * and media is authorized only pursuant to a valid written license from Acer
 * Inc. or an authorized sublicensor.
-----------------------------------------------------------------------*/

/*-----------------------------------------------------------------------
 * ProductName      : 工廠登記與管理系統再造
 * File Code        : ReceiveLogs02
 * File Name        : ReceiveLogs02
 * Description      : 
 * Dev Ver          : JDK 11
 * Author           : Frank Huang
 * Create Date      : 2023/01/10
-----------------------------------------------------------------------*/
package com.ggggg.rabbitmq.five;

import com.ggggg.rabbitmq.util.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息接收
 */
public class ReceiveLogs02 {
    public static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();
//        聲明一個交換機
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
//        聲明一個臨時隊獵
        String queue = channel.queueDeclare().getQueue();
//        綁定交換機
        channel.queueBind(queue, EXCHANGE_NAME, "") ;
        System.out.println("等待接收消息.....");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs02印出接收到的消息: " + new String(message.getBody()));
        };
//        接收消息
        channel.basicConsume(queue, true, deliverCallback, consumweTag -> {});

    }
}
