package cz.bouda.streams.trigger;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Description {

	String value() default "";

}
