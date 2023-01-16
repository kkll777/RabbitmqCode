/*-----------------------------------------------------------------------
 * Copyright(c) 2022 Acer Inc. All Rights Reserved.
 * This software is proprietary to and embodies the confidential technology
 * of Acer Inc.. Possession, use or copying of this software
 * and media is authorized only pursuant to a valid written license from Acer
 * Inc. or an authorized sublicensor.
-----------------------------------------------------------------------*/

/*-----------------------------------------------------------------------
 * ProductName      : 工廠登記與管理系統再造
 * File Code        : ReceiveLogsDirect01
 * File Name        : ReceiveLogsDirect01
 * Description      : 
 * Dev Ver          : JDK 11
 * Author           : Frank Huang
 * Create Date      : 2023/01/10
-----------------------------------------------------------------------*/
package com.ggggg.rabbitmq.six;

import com.ggggg.rabbitmq.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 將不同層級的log quere 綁訂到不同的exchange, 同時console能看到所有logs
 *      To          RoutinKey
 *      console     info
 *      console     warning
 *      disk        error
 */
public class ReceiveLogsDirect01 {
    public static final String EXCHANGE_NAME = "direct_logs"    ;

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

//        聲明對列 : info
        channel.queueDeclare("console", false, false, false, null);
        channel.queueBind("console", EXCHANGE_NAME, "info");
        channel.queueBind("console", EXCHANGE_NAME, "warning");

//      鳩收消息callbackck
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect01控制台接收到訊息: " + new String(message.getBody()));
        };
        channel.basicConsume("console", true, deliverCallback, s ->  {} );
    }
}
