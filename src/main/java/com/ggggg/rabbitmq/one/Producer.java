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
 * Create Date      : 2023/01/07
-----------------------------------------------------------------------*/
package com.ggggg.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("guest");
        factory.setPassword("guest");

//        創建連接
        Connection connection = factory.newConnection();
//      獲取通道
        Channel channel = connection.createChannel();

        /**
         * 生成隊列
         * @param 名稱
         * @param 消息是否持久畫
         * @param 該隊列是否只提供一個消費者
         * @Param 使否自動刪除 最後一個消費者段開連結後 該隊列是否自動刪除
         * @Param 其他參數
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //  發消息
        String message = "hello world";

        //  發送一消費
        //  發送到哪個交換機
        //  路由key，本次是對列名稱
        //  其他參數信息
        //  發送消息的消息體
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
//        發送消息完成
    }
}
