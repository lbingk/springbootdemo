package com.example.springbootdemo.ThreadAsync;

import com.example.springbootdemo.TreadsBySpring.SyncBookHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@Component
public class OrderHandler {
    private static final Logger LOG = LoggerFactory.getLogger(OrderHandler.class);

    @Value("${config.core.poolsize}")
    private int configCorePoolSize;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private AsyncForOrdersHandler asyncForOrdersHandler;
    @Autowired
    private PlatformTransactionManager transactionManager;

    // redis key 获取
    String redisKey = RedisUtils.keyBuilder(RedisEnum.ORDER_SALORDERHEADSERVICE_AUTOCREATECUSCODE, "");

    //间隔 5min 定时任务执行
    public void saveOrderMsgInRedis() {
        LOG.info("保存数据到redis");
        int dataSize = 20000;
        for (int i = 0; i < dataSize; i++) {
//            redisTemplate.opsForSet().add(redisKey, UUID.randomUUID().toString().substring(10));
        }
        Set<Object> cusCodeList = redisTemplate.opsForSet().members(redisKey);
            LOG.info("从Redis里面获取的值:" + cusCodeList.size());
//        cusCodeList.forEach(o -> {
//            LOG.info("从Redis里面获取的值:" + o.toString());
//        });
    }


    //间隔 1s 执行
//    @Scheduled(cron = "0/1 * *  * * ? ")
    public void excuteSaveOrder() {
        //1.读取redis对应的待处理订单（），并删除Redis对应的数据
        //TODO:加锁
        for (int i = 0; i < configCorePoolSize; i++) {
            Object cusCode = redisTemplate.opsForSet().pop(redisKey);
            //2.循环调用 asyncForSaveOrders 方法更新数据库
            if (cusCode != null) {
                asyncForOrdersHandler.asyncForSaveOrders(i, cusCode.toString());
            }
        }
    }

//    @Transactional
    public void autoUpdateOrder() throws Exception {
        List<OrderDto> toSplitList = new ArrayList<>();
        int dataSize = 20001;
        for (int i = 0; i < dataSize; i++) {
            toSplitList.add(new OrderDto() {{
                this.setSeqno(UUID.randomUUID().toString().substring(20));
            }});
        }
        List<Future<Map<Integer, Object>>> allBatchResult =
                new ArrayList<Future<Map<Integer, Object>>>();
        //1.调用批量拆分工具
        List<List<OrderDto>> splitList = AsyncSpiltListUtils.splitList(toSplitList, configCorePoolSize);
        //3.多线程分批执行
        String currentBatchFlag = UUID.randomUUID().toString();
        //4.需要汇总所有的子线程执行的结果
        final CountDownLatch countDownLatch = new CountDownLatch(configCorePoolSize);

        for (int i = 0; i < splitList.size(); i++) {
            allBatchResult.add(
                    this.asyncForOrdersHandler.asyncForUpdateOrders(countDownLatch,splitList.get(i), i, currentBatchFlag)
//                    this.asyncForOrdersHandler.asyncForUpdateOrders(splitList.get(i), i, currentBatchFlag)
            );
        }
        countDownLatch.await();
        System.out.println("xxxxxoooooo");
        //4.解析返回结果
        Boolean rollBackFlag = false;
        List<TransactionStatus> statusList=new ArrayList<>();
        for (Future<Map<Integer, Object>> batchResult : allBatchResult) {
            Map<Integer, Object> map = null;
            try {
                map = map = batchResult.get();
            } catch (InterruptedException | ExecutionException e) {
                LOG.info(e.getMessage());
            } finally {
                TransactionStatus status = (TransactionStatus) map.get(AsyncForOrdersHandler.STATUS);
                statusList.add(status);
                if (status.isRollbackOnly()) {
                    rollBackFlag = true;
                }
            }
        }

        //5.处理回滚或者提交
//        for (TransactionStatus status : statusList) {
//            if(rollBackFlag){
//                transactionManager.rollback(status);
//            }else {
//                // TODO:执行其他业务逻辑
//                transactionManager.commit(status);
//            }
//        }
    }



    public void hello(){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    public void gitStashTest(){
        System.out.println("33333333333333333333333333000000000000000000000000000!!!!!!!!!");
    }
}
