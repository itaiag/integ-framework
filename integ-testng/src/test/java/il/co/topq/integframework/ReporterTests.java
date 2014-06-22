package il.co.topq.integframework;

import static il.co.topq.integframework.reporting.Reporter.log;
import static il.co.topq.integframework.reporting.Reporter.logFile;
import static il.co.topq.integframework.reporting.Reporter.logImage;
import static il.co.topq.integframework.reporting.Reporter.logToFile;
import static il.co.topq.integframework.reporting.Reporter.startLogToggle;
import static il.co.topq.integframework.reporting.Reporter.step;
import static il.co.topq.integframework.reporting.Reporter.stopLogToggle;
import static il.co.topq.integframework.reporting.Reporter.Color.BLUE;
import static il.co.topq.integframework.reporting.Reporter.Color.GREEN;
import static il.co.topq.integframework.reporting.Reporter.Color.RED;
import static il.co.topq.integframework.reporting.Reporter.Color.YELLOW;
import static il.co.topq.integframework.reporting.Reporter.Style.BOLD;
import static il.co.topq.integframework.reporting.Reporter.Style.ITALIC;
import static il.co.topq.integframework.reporting.Reporter.Style.REGULAR;
import static org.testng.Reporter.getCurrentTestResult;
import il.co.topq.integframework.reporting.Reporter.Color;
import il.co.topq.integframework.reporting.Reporter.Style;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.testng.ITestResult;
import org.testng.annotations.Test;

import com.google.common.collect.Sets;

public class ReporterTests {

	@Test
	public void testManyLines() {
		log("This is the first line");
		log("This is the second line");
		log("This is the third line");
	}

	@Test
	public void testReportOutput() throws IOException {
		File myFile = new File("myFile.txt");
		myFile.createNewFile();
		logFile("Link to my file", myFile);
	}

	@Test
	public void testReportColors() {
		log("In red", REGULAR, RED);
		log("In blue", REGULAR, BLUE);
		log("In yellow", REGULAR, YELLOW);
		log("In green", REGULAR, GREEN);

		log("In red", BOLD, RED);
		log("In blue", BOLD, BLUE);
		log("In yellow", BOLD, YELLOW);
		log("In green", BOLD, GREEN);

		log("In red", ITALIC, RED);
		log("In blue", ITALIC, BLUE);
		log("In yellow", ITALIC, YELLOW);
		log("In green", ITALIC, GREEN);

		step("All colors and styles in a loop:");
		for (Color color : Color.values()) {
			for (Style style : Style.values()) {
				log(color.name() + " log with a " + style.name() + " style", style, color);
			}
		}
		step("****** All colors in a loop ******");
	}

	@Test
	public void testLogImage() {
		logImage("my title", new File(this.getClass().getResource("/screenshot.png").getPath()));
	}

	@Test
	public void testException() {
		log(new RuntimeException());
		log(new RuntimeException("with message"));
		log("with my own title", new RuntimeException());
		log("with my own title", new RuntimeException("with message"));

		log(new RuntimeException("fail"), ITestResult.FAILURE);
		log(new RuntimeException("success"), ITestResult.SUCCESS);
		getCurrentTestResult().setStatus(0);
	}

	@Test
	public void warning() {
		log(new RuntimeException("warning"), ITestResult.SUCCESS_PERCENTAGE_FAILURE);
	}

	@Test
	public void testToggle() {
		log("Simple toggle", generateLines(50, 10));
		startLogToggle("Toggle on some lines - No color");
		log(generateLines(1, 40));
		log(generateLines(1, 40));
		log(generateLines(1, 40));
		log(generateLines(1, 40));
		log(generateLines(1, 40));
		stopLogToggle();

		startLogToggle("Toggle on some lines - Green title", GREEN);
		log(generateLines(1, 40));
		log(generateLines(1, 40));
		log(generateLines(1, 40));
		log(generateLines(1, 40));
		log(generateLines(1, 40));
		stopLogToggle();

		log("Toggle with empty body and color blue", "", BLUE);

		log("About to test toggle with null title. The title should be changed to 'title' automatically");
		log(null, "Some body", BLUE);
	}

	@Test
	public void testToggleFile() {
		logToFile("Simple toggle", generateLines(50, 10), GREEN);
		logToFile("Toggle with empty body and color blue", "", BLUE);
		log("About to test toggle with null title. The title should be changed to 'file' automatically");
		logToFile(null, "Some body", BLUE);
	}

	private String generateLines(int numOfLines, int lengthOfLine) {
		StringBuilder body = new StringBuilder();
		for (int i = 0; i < numOfLines; i++) {
			body.append("Line ").append(i).append(" ");
			Random r = new Random();
			for (int j = 0; j < lengthOfLine; j++) {
				body.append((char) (r.nextInt(26) + 'a'));
			}
			body.append("\n");
		}
		return body.toString();
	}

	@Test
	public void testToggleWithColor() {
		final String body = "Body with one <b>bold</b> element";
		log("The title should appear in GREEN", body, GREEN);
		log("The title should appear in RED", body, RED);
		log("The title should appear in YELLOW", body, YELLOW);
	}

	@Test
	public void testStyle() {
		for (Style style : Style.values()) {
			log("Style is " + style.name(), style);
		}
	}

	@Test
	public void testFilterException() {
		Exception e = new RuntimeException("Exception with small stackTrace");
		log(e, Sets.newHashSet("sun.reflect", "java.lang.reflect", "org.testng", "org.apache.maven.surefire"));
		e = new RuntimeException("Exception with large stackTrace");
		log(e);

	}

}
