package spring.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListAddUtils {

    public static <T> void add(List<T> list , T t) {
        Set<T> set1 = new HashSet<>(list);
        if (set1.add(t)){
            list.add(t);
        }

    }

}