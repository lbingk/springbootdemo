package com.example.springbootdemo.TreadsBySpring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@Transactional
public class TrXXOO {
    private static final Logger LOG = LoggerFactory.getLogger(SyncBookHandler.class);

    public void ccoo(BookStatistic book) {

//        try {
        //数据入库操作
        LOG.info("正在执行入库单据号::" + book.getPno());
        if (Objects.equals(book.getPno(), "zxl2")) {
            LOG.info("正在执行入库单据号::" + book.getPno() + "即将失败");
            int i = 1 / 0;
            LOG.info("正在执行入库单据号::" + book.getPno() + "失败了");
        }
//        } catch (Exception e) {
//            LOG.info("正在执行入库单据号::" + book.getPno() + "失败了");
    }
//    }
}
