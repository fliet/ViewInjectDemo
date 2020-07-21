package com.example.lib_inject_view_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 为什么注解要单独放到一个module中，而不是和lib_processor放到一起
 * 如果和lib_processor放到一起的话，因为annotationProcessor是在编译期使用的，
 * 在运行期是不会使用的
 * 所以不能使用implementation
 * 而在主项目中使用该注解，就必须使用implementation添加lib_processor依赖
 * 这样lib_processor就会在运行期使用，
 *
 * 二者冲突，所以不放到一起
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface MyBindView {
    int value();
}
