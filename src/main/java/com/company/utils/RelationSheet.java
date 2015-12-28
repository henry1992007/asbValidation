package com.company.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by henry on 15/11/27.
 */
public class RelationSheet<V, PK, SK> {

    Map<Integer, V> map = new HashMap<>();

    @SafeVarargs
    public final V put(PK pk, V v, SK... sks) {
        for (SK sk : sks)
            map.put(pk.hashCode() ^ sk.hashCode(), v);
        return v;
    }

    public V get(PK pk, SK sk) {
        return map.get(pk.hashCode() ^ sk.hashCode());
    }
}
