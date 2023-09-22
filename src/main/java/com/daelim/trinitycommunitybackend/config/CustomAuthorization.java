package com.daelim.trinitycommunitybackend.config;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CustomAuthorization {
    boolean isAdmin() default false;
}
