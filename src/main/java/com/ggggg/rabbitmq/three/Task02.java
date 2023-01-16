/*-----------------------------------------------------------------------
 * Copyright(c) 2022 Acer Inc. All Rights Reserved.
 * This software is proprietary to and embodies the confidential technology
 * of Acer Inc.. Possession, use or copying of this software
 * and media is authorized only pursuant to a valid written license from Acer
 * Inc. or an authorized sublicensor.
-----------------------------------------------------------------------*/

/*-----------------------------------------------------------------------
 * ProductName      : 工廠登記與管理系統再造
 * File Code        : Task02
 * File Name        : Task02
 * Description      : 
 * Dev Ver          : JDK 11
 * Author           : Frank Huang
 * Create Date      : 2023/01/07
-----------------------------------------------------------------------*/
package com.ggggg.rabbitmq.three;

import com.ggggg.rabbitmq.util.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Task02 {

    public static final String TASK_QUEUE = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitUtils.getChannel();

        //  聲明對列
        /**
         * 生成隊列
         * @param name 名稱
         * @param durable 消息是否持久畫
         * @param consumer 該隊列是否只提供一個消費者
         * @Param autodel 使否自動刪除 最後一個消費者段開連結後 該隊列是否自動刪除
         * @Param params 其他參數
         */
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE, durable, false, false, null);

        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNext()) {
            String message = scanner.next();
            //  發送一消費
            //  發送到哪個交換機
            //  路由key，本次是對列名稱
            //  其他參數信息      決定消息實現持久化
            //  發送消息的消息體
            channel.basicPublish("", TASK_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生產者發出消息:  " + message);

        }
    }
}
