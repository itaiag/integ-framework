package il.co.topq.integframework;

import il.co.topq.integframework.bdd.BddI;
import il.co.topq.integframework.bdd.Step;
import il.co.topq.integframework.jms.JmsSystemModule;

import javax.jms.Message;

import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.support.converter.MessageType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.topq.integ.reporting.Reporter;

/**
 */
public class JmsSystemModuleTests extends AbstractIntegrationTestCase {

	private static final String QUEUE_NAME = "Blocking";

	private static final long MESSAGE_TIMEOUT = 10;

	@Autowired
	private JmsSystemModule jms;

	@BeforeMethod
	public void clearResources() throws Exception {
		Reporter.log("Clearing queue before test");
		jms.flushQueue(QUEUE_NAME);
		Reporter.log("Finished clearing queue");
	}

	@Test
	public void testFlushQueue() throws Exception {
		for (int i = 0; i < 10; i++) {
			jms.sendMessage(QUEUE_NAME, MessageType.TEXT, "testFlushQueue");
		}
		jms.flushQueue(QUEUE_NAME);
		Assert.assertNull(jms.receiveMessage(QUEUE_NAME, 1));
		Reporter.log("Success");
	}

	@Test
	public void testSendRecieveMessage() throws Exception {
		run(new BddI() {
			Message m;

			@Override
			public void given() throws Exception {

			}

			@Override
			@Step(description = "When we send message from type text")
			public void when() throws Exception {
				jms.sendMessage(QUEUE_NAME, MessageType.TEXT, "testSendRecieveMessage0");
				m = jms.receiveMessage(QUEUE_NAME, MESSAGE_TIMEOUT);

			}

			@Override
			@Step(description = "Then we receive the message from queue")
			public void then() throws Exception {
				Assert.assertNotNull(m);
			}
		});

		Reporter.log("Success");
	}

	@Test
	public void testBrowseQueue() throws Exception {
		int numOfMessages = 2;
		for (int i = 0; i < numOfMessages; i++) {
			jms.sendMessage(QUEUE_NAME, MessageType.TEXT, "testFlushQueue" + i);
		}
		Assert.assertEquals(numOfMessages, jms.browseQueue(QUEUE_NAME).size());
		Assert.assertEquals(numOfMessages, jms.browseQueue(QUEUE_NAME).size());

		Reporter.log("Success", true);

		// Cleaning queue
		for (int i = 0; i < numOfMessages; i++) {
			jms.receiveMessage(QUEUE_NAME, 500);
		}

	}

	@Test
	public void testReceiveAllMessages() throws Exception {
		for (int i = 0; i < 3; i++) {
			jms.sendMessage(QUEUE_NAME, MessageType.TEXT, "testFlushQueue" + i);
		}
		sleep(1000);
		Assert.assertEquals(3, jms.recieveAllMessages(QUEUE_NAME).size());

	}

}
