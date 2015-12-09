package com.company;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by henry on 15/11/8.
 */
public class MultiKeySetMap<K, V> extends HashMap<Set<K>, V> {

    public V putSetKey(K key, V value) {
        for (Set<K> o : keySet()) {
            if (get(o).equals(value)) {
                o.add(key);
                return get(o);
            }
        }

        Set<K> set = new HashSet<>();
        set.add(key);
        return put(set, value);
    }

    @Override
    public V get(Object key) {
        for (Set o : keySet()) {
            if (o.contains(key))
                return super.get(o);
        }
        return null;
    }

}
