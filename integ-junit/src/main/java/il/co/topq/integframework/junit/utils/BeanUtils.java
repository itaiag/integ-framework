package il.co.topq.integframework.junit.utils;


public class BeanUtils {

	public static Object createInstanceFromString(String beanName, Class<?> superClass) {
		//TODO: Used generic method
		try {
			Class<?> clazz = Class.forName(beanName);
			if (superClass.isAssignableFrom(clazz)) {
				return clazz.newInstance();

			} else {
				System.err.println(beanName + " is not from type " + superClass.getName());
				return null;
			}
		} catch (Exception e) {
			System.err.println("Failed to create instance of class " + beanName);
			return null;
		}
	}
}
