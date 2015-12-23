package com.company.utils;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by henry on 15/11/12.
 */
public abstract class MapUtils {

    public MapUtils() {
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    @SafeVarargs
    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> type, Entry<K, V>... entries) {
        EnumMap<K, V> map = new EnumMap<>(type);
        for (Entry<K, V> entry : entries)
            map.put(entry.getKey(), entry.getValue());
        return map;
    }

    @SafeVarargs
    public static <K, V> MultiKeySetMap<K, V> newMultiKeySetMap(Entry<Set<K>, V>... entries) {
        MultiKeySetMap<K, V> map = new MultiKeySetMap<>();
        for (Entry<Set<K>, V> entry : entries)
            map.put(entry.getKey(), entry.getValue());
        return map;
    }


    public static class Entry<K, V> {
        K key;
        V value;

        public Entry(K k, V v) {
            this.key = k;
            this.value = v;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

}
