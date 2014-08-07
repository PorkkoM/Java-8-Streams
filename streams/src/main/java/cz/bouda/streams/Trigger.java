package cz.bouda.streams;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

public class Trigger {

	public static void run(Class<?> clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (!StringUtils.equals("main", method.getName()) && !StringUtils.startsWith(method.getName(), "lambda")) {
				System.out.println("\n --- " + method.getName());
				try {
					method.invoke(clazz);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
