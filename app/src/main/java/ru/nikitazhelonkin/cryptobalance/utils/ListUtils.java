package ru.nikitazhelonkin.cryptobalance.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class ListUtils {

    public interface Criteria<T> {
        boolean check(T object);
    }

    public interface Map<T, P>{
        P map(T object);
    }

    public interface Filter<T>{
        boolean accept(T t);
    }

    public interface Reducer<R, T> {
        R reduce(R r, T t);
    }

    public static <T> T find(List<T> list, Criteria<T> criteria) {
        if (list == null) {
            return null;
        }
        for (T object : list) {
            if (criteria.check(object)) {
                return object;
            }
        }
        return null;
    }

    public static <T> int findIndex(List<T> list, Criteria<T> criteria) {
        if (list == null) {
            return -1;
        }
        for (int i=0;i<list.size();i++) {
            if (criteria.check(list.get(i))) {
                return i;
            }
        }
        return -1;
    }


    public static <T> int count(List<T> list, Criteria<T> criteria) {
        if (list == null) {
            return 0;
        }
        int count = 0;
        for (T object : list) {
            if (criteria.check(object)) {
                count++;
            }
        }
        return count;
    }

    public static <R,T> R reduce(List<T> list, R seed, Reducer<R, T> reducer) {
        for(T t:list){
            seed = reducer.reduce(seed, t);
        }
        return seed;
    }

    public static <T> List<T> filter(List<T> list, Filter<T> filter) {
        if (list == null) {
            return null;
        }
        List<T> result = new ArrayList<>();
        for (T object : list) {
            if (filter.accept(object)) {
                result.add(object);
            }
        }
        return result;
    }

    public static <T, P> List<P> map(List<T> list, Map<T, P> map) {
        if (list == null) {
            return null;
        }
        List<P> result = new ArrayList<>(list.size());
        for (T t : list) {
            result.add(map.map(t));
        }
        return result;
    }

    public static <T> List<T> distinct(List<T> list){
        return new ArrayList<>(new LinkedHashSet<>(list));
    }
}