package com.example.springbootdemo.TreadsBySpring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

@Component
public class SyncBookHandler {
    @Autowired
    private TrXXOO trXXOO;


    private static final Logger LOG = LoggerFactory.getLogger(SyncBookHandler.class);

    /**
     * syncMargePsr:(多线程同步处理数据方法). <br/>
     *
     * @param bookList  一段数据集合
     * @param pageIndex 段数
     * @return Future<String> future对象
     * @author LW
     * @since JDK 1.8
     */
    @Async
    public Future<String> syncMargePsr(List<BookStatistic> bookList, int pageIndex) {


        LOG.info(String.format("此批数据的段数为:%s 此段数据的数据条数为:%s", pageIndex, new PsrList().getPsrList().size()));
        //声明future对象
        Future<String> result = new AsyncResult<String>("");
        //循环遍历该段旅客集合
        if (null != bookList && bookList.size() > 0) {
            for (BookStatistic book : bookList) {
                trXXOO.ccoo(book);
            }
        }
        return result;

    }
}