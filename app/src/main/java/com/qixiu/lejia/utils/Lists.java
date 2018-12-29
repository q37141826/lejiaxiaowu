package com.qixiu.lejia.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Long on 2017/11/28
 */
public final class Lists {

    private Lists() {
    }

    public static <E> List<E> as(E[] array) {
        return Arrays.asList(array);
    }

    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<>();
    }

}
