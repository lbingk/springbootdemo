package com.example.springbootdemo.ThreadAsync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

@Component
public class AsyncForOrdersHandler {

    public static final Integer ORDERDTO = 10;
    public static final Integer STATUS = 20;

    private static final Logger LOG = LoggerFactory.getLogger(AsyncForOrdersHandler.class);
    @Autowired
    private PlatformTransactionManager txManager;

    @Async("configTask")
    @Transactional
    public void asyncForSaveOrders(int i, String cusCode) {
        Thread currentThread = Thread.currentThread();
        String str = "configThread3";
        if (currentThread.getName().equals(str)) {
//            int j = 1 / 0;
        }
        LOG.info("当前线程名为: " + currentThread.getName() + " " +
                "--正在处理-- " +
                "当前客户代码： " + cusCode);
    }

    @Async("configTask")
    public Future<Map<Integer, Object>> asyncForUpdateOrders(CountDownLatch countDownLatch, List<OrderDto> inputList, int i, String currentBatchFlag)
//    public Future<Map<Integer, Object>> asyncForUpdateOrders( List<OrderDto> inputList, int i, String currentBatchFlag)
            throws InterruptedException {
        //1.日志：当前执行的批数信息
        LOG.info(String.format(
                "此批数据的所属标志为:%s " + "此批数据的段数为:%s " + "此段数据的数据条数为:%s",
                currentBatchFlag, i, inputList.size()));

        //2.声明future对象，获取返回信息
        Future<Map<Integer, Object>> singleBatchResult = new AsyncResult<>(new HashMap<>());

        //3.获取事务
        DefaultTransactionDefinition df = new DefaultTransactionDefinition();
        df.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transactionStatus = txManager.getTransaction(df);

        //4.循环遍历集合，执行操作
        if (null != inputList && inputList.size() > 0) {
            Map<Integer, Object> mapResult = new HashMap<>();
//            TransactionStatus transactionStatus = TransactionAspectSupport.currentTransactionStatus();
            try {
                //TODO:执行业务逻辑
              int j=1/0;
            } catch (Exception e) {
                //此批数据存在异常，
            }finally {
                countDownLatch.countDown();
                mapResult.put(ORDERDTO, inputList);
                mapResult.put(STATUS, transactionStatus);
                singleBatchResult = new AsyncResult<>(mapResult);
            }
        }
        return singleBatchResult;
    }

}
