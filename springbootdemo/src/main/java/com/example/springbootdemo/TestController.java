package com.example.springbootdemo;

import com.example.springbootdemo.ThreadAsync.OrderHandler;
import com.example.springbootdemo.ThreadsByJDK.TestThreadPoolManager;
import com.example.springbootdemo.TreadsBySpring.AsynReceiveBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Queue;
import java.util.UUID;

@RestController
public class TestController {

    @Autowired
    TestThreadPoolManager testThreadPoolManager;
    @Autowired
    AsynReceiveBook asynReceiveBook;
    @Autowired
    OrderHandler orderHandler;
    /**
     * 测试模拟下单请求 入口
     * @return
     */
    @GetMapping("/start")
    public String start() {
        //模拟的随机数
        String orderNo = System.currentTimeMillis() + UUID.randomUUID().toString();

        testThreadPoolManager.addOrders(orderNo);

        return "Test ThreadPoolExecutor start";
    }

    /**
     * 停止服务
     * @param id
     * @return
     */
    @GetMapping("/end/{id}")
    public String end(@PathVariable Long id) {

        testThreadPoolManager.shutdown();

        Queue q = testThreadPoolManager.getMsgQueue();
        System.out.println("关闭了线程服务，还有未处理的信息条数：" + q.size());
        return "Test ThreadPoolExecutor start";
    }


    @GetMapping("asynReceiveBook")
    public void asynReceiveBook() {

        asynReceiveBook.receiveBookJobRun();

        System.err.println("我已经执行结束啦");
        System.err.println("我已经执行结束啦");
        System.err.println("我已经执行结束啦");
        System.err.println("我已经执行结束啦");
    }



    @GetMapping("asynOrder")
    public void asynOrder() {

        orderHandler.saveOrderMsgInRedis();

        System.err.println("我已经执行结束啦");
        System.err.println("我已经执行结束啦");
        System.err.println("我已经执行结束啦");
        System.err.println("我已经执行结束啦");
    }

    @GetMapping("autoUpdateOrder")
    public void autoUpdateOrder() throws Exception {

        orderHandler.autoUpdateOrder();

        System.err.println("我已经执行结束啦");
        System.err.println("我已经执行结束啦");
        System.err.println("我已经执行结束啦");
        System.err.println("我已经执行结束啦");
    }
}
