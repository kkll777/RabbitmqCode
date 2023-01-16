/*-----------------------------------------------------------------------
 * Copyright(c) 2022 Acer Inc. All Rights Reserved.
 * This software is proprietary to and embodies the confidential technology
 * of Acer Inc.. Possession, use or copying of this software
 * and media is authorized only pursuant to a valid written license from Acer
 * Inc. or an authorized sublicensor.
-----------------------------------------------------------------------*/

/*-----------------------------------------------------------------------
 * ProductName      : 工廠登記與管理系統再造
 * File Code        : Task01
 * File Name        : Task01
 * Description      : 
 * Dev Ver          : JDK 11
 * Author           : Frank Huang
 * Create Date      : 2023/01/07
-----------------------------------------------------------------------*/
package com.ggggg.rabbitmq.two;

import com.ggggg.rabbitmq.util.RabbitUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Task01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //  發送一消費
            //  發送到哪個交換機
            //  路由key，本次是對列名稱
            //  其他參數信息
            //  發送消息的消息體
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("發送消息完成: " + message);
        }
    }
}
