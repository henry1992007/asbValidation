package com.company.validations;

import com.company.Comparator;
import com.company.element.CheckDefinition;
import com.company.enums.CheckType;

import java.util.*;

/**
 * Created by henry on 15/11/5.
 */
public class BooleanValidator extends AbstractValidator<Boolean> {

    AbstractComparator<Boolean> comparator;

    @Override
    protected Comparator<Boolean> getComparator() {
        return comparator == null ? comparator = AbstractComparator.<Boolean>defaultComparator(CheckType.BOOLEAN) : comparator;
    }

    @Override
    protected Boolean parseObject(Object o) {
        return o == null ? null :
                o instanceof String ? parseString((String) o) :
                        o instanceof Boolean ? (Boolean) o : false;
    }

    @Override
    protected Boolean parseString(String s) {
        if (s.equals("null")) {
            setCheckNull(true);
            return null;
        } else {
            return s.equals("true");
        }
    }

}
