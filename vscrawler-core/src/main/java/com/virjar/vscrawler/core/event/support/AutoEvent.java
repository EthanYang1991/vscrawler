package com.virjar.vscrawler.core.event.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by virjar on 17/4/30.
 *
 * @author virjar
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AutoEvent {
    String topic() default "";

    boolean sync() default false;

    boolean clearExpire() default false;
}
