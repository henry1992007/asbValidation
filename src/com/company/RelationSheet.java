package com.company;

import java.util.HashMap;

/**
 * Created by henry on 15/11/27.
 */
public class RelationSheet<PK, SK, V> {

    HashMap<Integer, V> map = new HashMap<>();

    public V put(V v, PK pk, SK... sks) {
        for (SK sk : sks)
            map.put(pk.hashCode() ^ sk.hashCode(), v);
        return v;
    }

    public V get(PK pk, SK sk) {
        return map.get(pk.hashCode() ^ sk.hashCode());
    }
}
