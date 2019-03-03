package com.example.springbootdemo.TreadsBySpring;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class AsynReceiveBook {
    @Autowired
    private SyncBookHandler syncBookHandler;

    private static Logger log = Logger.getLogger(AsynReceiveBook.class);

    //核心线程数
    @Value("${book.core.poolsize}")
    private int threadSum;

    public void receiveBookJobRun() {
        List<BookStatistic> bookList = new PsrList().getPsrList();
        //入库开始时间
        Long inserOrUpdateBegin = System.currentTimeMillis();
        log.info("数据更新开始时间:" + inserOrUpdateBegin);

        //接收集合各段的 执行的返回结果
        List<Future<String>> futureList = new ArrayList<Future<String>>();
        //集合总条数
        if (bookList != null) {
            int listSize = bookList.size();

            int listStart, listEnd;
            //当总条数不足threadSum条时 用总条数 当做线程切分值
            if (threadSum > listSize) {
                threadSum = listSize;
            }

            //将list 切分多份 多线程执行
            for (int i = 0; i < threadSum; i++) {
                //计算切割  开始和结束
                listStart = listSize / threadSum * i;
                listEnd = listSize / threadSum * (i + 1);
                //最后一段线程会 出现与其他线程不等的情况
                if (i == threadSum - 1) {
                    listEnd = listSize;
                }
                //数据切断
                List<BookStatistic> sunList = bookList.subList(listStart, listEnd);

                //每段数据集合并行入库
                futureList.add(syncBookHandler.syncMargePsr(sunList, i));
            }

            //对各个线程段结果进行解析
            for (Future<String> future : futureList) {
                String str;
                if (null != future) {
                    try {
                        str = future.get().toString();
                        log.info("current thread id =" + Thread.currentThread().getName() + ",result=" + str);

                    } catch (InterruptedException | ExecutionException e) {

                        log.info("线程运行异常！");
                    }
                } else {
                    log.info("线程运行异常！");
                }
            }
        }

        Long inserOrUpdateEnd = System.currentTimeMillis();
        log.info("数据更新结束时间:" + inserOrUpdateEnd + "。此次更新数据花费时间为：" + (inserOrUpdateEnd - inserOrUpdateBegin));

    }

}

