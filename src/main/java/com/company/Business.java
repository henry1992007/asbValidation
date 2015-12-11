package com.company;

import com.company.validations.ValidationChecker;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by henry on 15/11/4.
 */
@Component
public class Business {

    public void biz(BizObj bizObj) {
        ValidationChecker checker = ValidationChecker.get("couponFieldsCheck");
        System.out.println(Arrays.toString(checker.check().toArray()));
    }

}
