package com.company.validations;

import com.company.*;
import com.company.Comparator;
import com.company.element.CheckDefinition;
import com.company.utils.Assert;
import com.company.utils.CollectionUtils;
import com.company.utils.ReflectUtils;
import com.company.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by henry on 15/12/5.
 */
public class DateValidator extends AbstractValidator<Date> {

    DateComparator comparator;

    @Override
    protected Comparator<Date> getComparator() {
        if (comparator == null)
            comparator = new DateComparator();
        return comparator;
    }

    public List<Date> parseObject(Collection<Object> list) {
        return list.stream().map(o -> (Date) o).collect(Collectors.toList());
    }

    public List<Date> parseString(List<String> list) {
        List<Date> result = new ArrayList<>();
        list = CollectionUtils.anyMatchAndThen(list, s -> s.equals("/null"), (Function<AbstractValidator, Boolean>) o -> o.setCheckNull(true), this) ?
                list.stream().filter(s -> s.equals("/null")).collect(Collectors.toList()) : list;
        for (String s : list) {
            try {
                result.add(DateFormat.getInstance().parse(s));
            } catch (ParseException e) {
                Assert.illegalDefinitionException("date parse error", 0, "", e);
            }
        }

        return result;
    }

}
