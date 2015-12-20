package com.company;

import java.util.List;

/**
 * @author Jianheng He
 */
public interface ComputeOperator<T> {
    List<T> operate(List<T> var);
}
