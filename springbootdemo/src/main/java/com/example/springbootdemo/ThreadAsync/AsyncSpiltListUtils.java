package com.example.springbootdemo.ThreadAsync;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 批量数据拆分小批量
 * @Author luobingkai
 * @Date 2019/1/29 18:44
 * @Version 1.0
 **/
public class AsyncSpiltListUtils {

    public static <T>  List<List<T>> splitList(List<T> splitList, int threadSum) {
        List<List<T>> subList = new ArrayList<List<T>>();

        int listSize = splitList.size();
        int listStart, listEnd;
        //当总条数不足threadSum条时 用总条数 当做线程切分值
        if (threadSum > listSize) {
            threadSum = listSize;
        }

        //将list切分多份
        for (int i = 0; i < threadSum; i++) {
            //计算切割:开始和结束
            listStart = listSize / threadSum * i;
            listEnd = listSize / threadSum * (i + 1);
            //最后一段线程会出现与其他线程不等的情况
            if (i == threadSum - 1) {
                listEnd = listSize;
            }
            //数据切断
            subList.add(splitList.subList(listStart, listEnd));
        }
        return subList;
    }
}
