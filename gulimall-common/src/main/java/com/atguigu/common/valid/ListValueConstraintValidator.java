package com.atguigu.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @author QiaoJiancheng
 * @create 2023/4/19 19:16
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValues, Integer> {

    private Set<Integer> set = new HashSet<>();

    @Override
    public void initialize(ListValues constraintAnnotation) {
        int[] vals = constraintAnnotation.vals();
        if (vals != null && vals.length > 0) {
            for (int val : vals) {
                set.add(val);
            }
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return set.contains(value);
    }
}
