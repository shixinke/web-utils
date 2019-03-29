package com.shixinke.utils.web.util;

import java.util.*;

/**
 * @author jiangfangtao
 * @version 1.0
 * created 19-3-29 11:58
 */
public class Converters {
    public static <E> List<E> iteratorToList(Iterator<E> iterator) {
        List<E> list = new ArrayList<E>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public static <E> List<E> iteratorMergeToList(Iterator<E> it1, Iterator<E> it2) {
        List<E> list1 = iteratorToList(it1);
        List<E> list2 = iteratorToList(it2);
        Set<E> sets = new HashSet<E>(list1);
        sets.addAll(list2);
        return new ArrayList<E>(sets);
    }
}
