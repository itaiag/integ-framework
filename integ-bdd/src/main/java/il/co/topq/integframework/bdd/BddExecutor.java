package il.co.topq.integframework.bdd;

import java.lang.reflect.Method;

import com.topq.integ.reporting.Reporter;
import com.topq.integ.reporting.Reporter.Color;
import com.topq.integ.reporting.Reporter.Style;

public class BddExecutor {

	static public void run(BddI bdd) throws Exception {
		reportStepDescription(bdd, "given");
		bdd.given();

		reportStepDescription(bdd, "when");
		bdd.when();

		reportStepDescription(bdd, "then");
		bdd.then();
	}

	private static void reportStepDescription(BddI bdd, String stepName) {
		Class<? extends BddI> aClass = bdd.getClass();
		try {
			final Method method = aClass.getMethod(stepName, new Class[] {});
			final Step step = method.getAnnotation(il.co.topq.integframework.bdd.Step.class);
			if (null == step) {
				Reporter.log("Step description is not available", Style.BOLD, Color.RED);
			} else {
				Reporter.log(step.description(), Style.REGULAR, Color.BLUE);

			}

		} catch (Exception e) {
			Reporter.log("Step defintion is not available", Style.BOLD, Color.RED);
		}
	}

}
