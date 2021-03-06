package com.virjar.vscrawler.core.processor.configurableprocessor.annotiondriven.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by virjar on 2017/12/10.<br/>
 * 标记这个模型可以处理那种类型的种子
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchSeed {
}
