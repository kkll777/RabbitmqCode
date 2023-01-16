/*-----------------------------------------------------------------------
 * Copyright(c) 2022 Acer Inc. All Rights Reserved.
 * This software is proprietary to and embodies the confidential technology
 * of Acer Inc.. Possession, use or copying of this software
 * and media is authorized only pursuant to a valid written license from Acer
 * Inc. or an authorized sublicensor.
-----------------------------------------------------------------------*/

/*-----------------------------------------------------------------------
 * ProductName      : 工廠登記與管理系統再造
 * File Code        : ReceiveLogsDirect02
 * File Name        : ReceiveLogsDirect02
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
public class ReceiveLogsDirect02 {
    public static final String EXCHANGE_NAME = "direct_logs"    ;

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

//        聲明對列 : info
        channel.queueDeclare("disk", false, false, false, null);
        channel.queueBind("disk", EXCHANGE_NAME, "error");

//      鳩收消息callback
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect02控制台接收到訊息: " + new String(message.getBody()));
        };
        channel.basicConsume("disk", true, deliverCallback, s ->  {} );
    }
}