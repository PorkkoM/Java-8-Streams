package cz.bouda.streams.trigger;

import java.lang.reflect.*;

import org.apache.commons.lang3.StringUtils;

public class Trigger {

	public static void run(Class<?> clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (!StringUtils.equals("main", method.getName()) && !StringUtils.startsWith(method.getName(), "lambda")) {
				if (method.isAnnotationPresent(Description.class)) {
					Description description = method.getDeclaredAnnotation(Description.class);
					System.out.println("\n --- " + description.value());
				} else {
					System.out.println("\n --- " + method.getName());
				}

				try {
					method.invoke(clazz);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
