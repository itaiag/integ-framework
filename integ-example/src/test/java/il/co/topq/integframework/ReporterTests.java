package il.co.topq.integframework;

import il.co.topq.integframework.bdd.BddExecutor;
import il.co.topq.integframework.bdd.BddI;
import il.co.topq.integframework.bdd.Step;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.testng.annotations.Test;

import com.topq.integ.reporting.Reporter;
import com.topq.integ.reporting.Reporter.Color;
import com.topq.integ.reporting.Reporter.Style;

public class ReporterTests {

	@Test
	public void testManyLinesInBDD() throws Exception {
		Reporter.log("This is the first line");
		Reporter.log("This is the second line");
		Reporter.log("This is the third line");

		BddExecutor.run(new BddI() {

			@Override
			@Step(description = "Given description")
			public void given() throws Exception {
				Reporter.log("This is the first line");
				Reporter.log("This is the second line");
				Reporter.log("This is the third line");

			}

			@Override
			@Step(description = "When description")
			public void when() throws Exception {
				Reporter.log("This is the first line");
				Reporter.log("This is the second line");
				Reporter.log("This is the third line");

			}

			@Override
			@Step(description = "Then description")
			public void then() throws Exception {
				Reporter.log("This is the first line");
				Reporter.log("This is the second line");
				Reporter.log("This is the third line");

			}

		});
	}

	@Test
	public void testManyLines() {
		Reporter.log("This is the first line");
		Reporter.log("This is the second line");
		Reporter.log("This is the third line");
	}

	@Test
	public void testReportOutput() throws IOException {
		File myFile = new File("myFile.txt");
		myFile.createNewFile();
		Reporter.logFile("Link to my file", myFile);
	}

	@Test
	public void testReportColors() {
		Reporter.log("In red", Style.REGULAR, Color.RED);
		Reporter.log("In blue", Style.REGULAR, Color.BLUE);
		Reporter.log("In yellow", Style.REGULAR, Color.YELLOW);
		Reporter.log("In green", Style.REGULAR, Color.GREEN);

		Reporter.log("In red", Style.BOLD, Color.RED);
		Reporter.log("In blue", Style.BOLD, Color.BLUE);
		Reporter.log("In yellow", Style.BOLD, Color.YELLOW);
		Reporter.log("In green", Style.BOLD, Color.GREEN);

		Reporter.log("In red", Style.ITALIC, Color.RED);
		Reporter.log("In blue", Style.ITALIC, Color.BLUE);
		Reporter.log("In yellow", Style.ITALIC, Color.YELLOW);
		Reporter.log("In green", Style.ITALIC, Color.GREEN);

	}

	@Test
	public void testToggle() {
		Reporter.log("Simple toggle", generateLines(50, 10));
		Reporter.startLogToggle("Toggle on some lines - No color");
		Reporter.log(generateLines(1, 40));
		Reporter.log(generateLines(1, 40));
		Reporter.log(generateLines(1, 40));
		Reporter.log(generateLines(1, 40));
		Reporter.log(generateLines(1, 40));
		Reporter.stopLogToggle();

		Reporter.startLogToggle("Toggle on some lines - Green title", Color.GREEN);
		Reporter.log(generateLines(1, 40));
		Reporter.log(generateLines(1, 40));
		Reporter.log(generateLines(1, 40));
		Reporter.log(generateLines(1, 40));
		Reporter.log(generateLines(1, 40));
		Reporter.stopLogToggle();

		Reporter.log("Toggle with empty body and color blue", "", Color.BLUE);

		Reporter.log("About to test toggle with null title. The title should be changed to 'title' automatically");
		Reporter.log(null, "Some body", Color.BLUE);
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
		Reporter.log("The title should appear in GREEN", body, Color.GREEN);
		Reporter.log("The title should appear in RED", body, Color.RED);
		Reporter.log("The title should appear in YELLOW", body, Color.YELLOW);
	}

	@Test
	public void testStyle() {
		Reporter.log("In bold", Style.BOLD);
		Reporter.log("In italic", Style.ITALIC);
	}

}
