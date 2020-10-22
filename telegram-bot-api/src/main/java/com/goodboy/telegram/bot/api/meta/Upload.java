package com.goodboy.telegram.bot.api.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Upload {

    /**
     * @return upload attachment name
     */
    String streamName() default "";

    /**
     * @return upload attachment header prefix
     */
    String attachmentHeaderPrefix() default "attach://";
}
