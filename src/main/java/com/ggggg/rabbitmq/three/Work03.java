/*-----------------------------------------------------------------------
 * Copyright(c) 2022 Acer Inc. All Rights Reserved.
 * This software is proprietary to and embodies the confidential technology
 * of Acer Inc.. Possession, use or copying of this software
 * and media is authorized only pursuant to a valid written license from Acer
 * Inc. or an authorized sublicensor.
-----------------------------------------------------------------------*/

/*-----------------------------------------------------------------------
 * ProductName      : 工廠登記與管理系統再造
 * File Code        : Work03
 * File Name        : Work03
 * Description      : 
 * Dev Ver          : JDK 11
 * Author           : Frank Huang
 * Create Date      : 2023/01/07
-----------------------------------------------------------------------*/
package com.ggggg.rabbitmq.three;

import com.ggggg.rabbitmq.util.RabbitUtils;
import com.ggggg.rabbitmq.util.SleepUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 手動應答
 */
public class Work03 {
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitUtils.getChannel();
        System.out.println("C2等待接收消息處理時間較短");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            SleepUtils.sleep(1);
            System.out.println("接收到的訊息 :" + new String(message.getBody(), StandardCharsets.UTF_8));
//          手動應答
            /**
             * 1. 消息的標記
             * 2. 是否批量應答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        //        設置不公平分發
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);

//        採用手動應答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, consumeTag -> {
            System.out.println("消費者取消消費邏輯");
        });

    }
}
