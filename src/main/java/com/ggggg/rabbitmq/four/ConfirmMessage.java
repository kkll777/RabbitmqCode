/*-----------------------------------------------------------------------
 * Copyright(c) 2022 Acer Inc. All Rights Reserved.
 * This software is proprietary to and embodies the confidential technology
 * of Acer Inc.. Possession, use or copying of this software
 * and media is authorized only pursuant to a valid written license from Acer
 * Inc. or an authorized sublicensor.
-----------------------------------------------------------------------*/

/*-----------------------------------------------------------------------
 * ProductName      : 工廠登記與管理系統再造
 * File Code        : ConfirmMessage
 * File Name        : ConfirmMessage
 * Description      : 
 * Dev Ver          : JDK 11
 * Author           : Frank Huang
 * Create Date      : 2023/01/07
-----------------------------------------------------------------------*/
package com.ggggg.rabbitmq.four;

import com.ggggg.rabbitmq.util.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * 發布確認模式
 * 1. 單個模式
 * 2. 批量確認
 * 3. 異步批量確認
 */
public class ConfirmMessage {

    //    批量發布消息個數
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
//        1. 單個確認
//        ConfirmMessage.publishMessageIndividually();
//        2. 批量確認
//        ConfirmMessage.publishMessageBatch();
//        3. 異步批量確認
        ConfirmMessage.publishMessageAsync();

    }

    /**
     * 單個發送
     *
     * @throws IOException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    public static void publishMessageIndividually() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitUtils.getChannel();
//        對列的聲明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
//        開啟發布確認
        channel.confirmSelect();
//        開始時間
        long begin = System.currentTimeMillis();
//        批量發消息
        int cnt = MESSAGE_COUNT;
        while (cnt > 0) {
            String messege = cnt + "";
            channel.basicPublish("", queueName, null, messege.getBytes(StandardCharsets.UTF_8));
//            待個消息馬上發布
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息發布成功");
            }
            cnt--;
        }
//        結束時間
        long end = System.currentTimeMillis();
        System.out.println("發布" + MESSAGE_COUNT + "個單獨訊息, 消耗 " + (end - begin) + "ms");
    }

    /**
     * 批量發送
     */
    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitUtils.getChannel();
//        對列的聲明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
//        開啟發布確認
        channel.confirmSelect();
//        開始時間
        long begin = System.currentTimeMillis();
//        批量發消息
        int cnt = MESSAGE_COUNT;
        while (cnt > 0) {
            String messege = cnt + "";
            channel.basicPublish("", queueName, null, messege.getBytes(StandardCharsets.UTF_8));

//            判斷達到100條消息時 批量確認一次
            if (cnt % MESSAGE_COUNT == 0) {
                channel.waitForConfirms();
            }
            cnt--;
        }
//        結束時間
        long end = System.currentTimeMillis();
        System.out.println("發布" + MESSAGE_COUNT + "個批量確認訊息, 消耗 " + (end - begin) + "ms");
    }

    //  異步發布確認
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitUtils.getChannel();
//        對列的聲明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
//        開啟發布確認
        channel.confirmSelect();
//        開始時間
        long begin = System.currentTimeMillis();

//        現成安全HashMap, 適用於高併發
//        1. 輕鬆將序號與消息進行關聯
//        2. 輕鬆批量師除條目
//        3, 支持高併發
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

//        監聽成功callback function
//        param1: 消息標記
//        param2: 是否為批量確認
        ConfirmCallback ackCallback = (deviveryTag, multiple) -> {
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deviveryTag);
                confirmed.clear();;
            } else {
                outstandingConfirms.remove(deviveryTag);
            }
//            ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deviveryTag);
            System.out.println("確認消息: " + deviveryTag);
        };
//        監聽失敗
        ConfirmCallback nackCallback = (deviveryTag, multiple) -> {

            System.out.println("未確認消息: " + outstandingConfirms.get(deviveryTag) + " TAG: " +deviveryTag);
        };
//      準備監聽器，監聽那些成功那些失敗
        //  param1: 監聽那些成功
//          param2: 監聽那些失敗
        int i = 0;
        channel.addConfirmListener(ackCallback, nackCallback);
        int cnt = MESSAGE_COUNT;
        while (cnt > 0) {
            String message = "消息: " + i;
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
            i++;
            cnt--;
        }


        //        結束時間
        long end = System.currentTimeMillis();
        System.out.println("發布" + MESSAGE_COUNT + "個異步確認訊息, 消耗 " + (end - begin) + "ms");
    }
}
