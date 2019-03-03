package com.example.springbootdemo.TreadsBySpring;

import java.util.ArrayList;
import java.util.List;

public class PsrList {

    public List<BookStatistic> getPsrList() {
        List<BookStatistic> psrList = new ArrayList<BookStatistic>();
        for (int i = 0; i < 20; i++) {
            BookStatistic book = new BookStatistic();
            book.setPno("zxl" + i);
            psrList.add(book);
        }
        return psrList;
    }
}
