package com.company;

/**
 * Created by henry on 15/12/29.
 */
public abstract class EntityAttributes {

    public EntityAttributes() {
    }


    public final static String ID = "id";


    public final static String CLASS = "class";

    public final static String REF = "ref";


    public final static String TYPE = "type";
    public final static String FIELD = "field";
    public final static String _FIELD = "_field";
    public final static String VALUE = "value";
    public final static String _VALUE = "_value";
    public final static String CMPT = "cmpt";
    public final static String _CMPT = "_cmpt";

    /**
     * 操作符前多个值是否能通过校验的结合逻辑
     */
    public final static String VAL_LOGIC = "valLogic";

    /**
     * 操作符前多个object校验结果的结合逻辑
     */
    public final static String LOGIC = "logic";

    /**
     * 操作符后多个object校验结果的结合逻辑
     */
    public final static String _LOGIC = "_logic";
    public final static String OPERATOR = "operator";
    public final static String MSG = "msg";
}
