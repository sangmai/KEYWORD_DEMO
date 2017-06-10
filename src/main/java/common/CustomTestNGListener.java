package common;

import java.lang.reflect.Field;

import org.testng.TestListenerAdapter;

public class CustomTestNGListener extends TestListenerAdapter {

	// accepts test class as parameter.
	// use ITestResult#getInstance()

	static void getCurrentTestHelper(Object testClass) {
		Class<?> c = testClass.getClass();
		try {
			// get the field "h" declared in the test-class.
			// getDeclaredField() works for protected members.
			Field hField = c.getDeclaredField("h");

			// get the name and class of the field h.
			// (this is just for fun)
			String name = hField.getName();
			Object thisHelperInstance = hField.get(testClass);
			System.out.print(name + ":" + thisHelperInstance.toString() + "\n");

			// get fields inside this Helper as follows:
			Field innerField = thisHelperInstance.getClass().getDeclaredField("testcaseName");

			// get the value of the field corresponding to the above Helper
			// instance.
			System.out.println(innerField.get(thisHelperInstance).toString());

		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}