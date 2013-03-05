package il.co.topq.integframework.jms;

import il.co.topq.integframework.reporting.Reporter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageType;


/**
 * <b>Package:</b> com.rsa.fa.blackbox.integration.jms<br/>
 * <b>Type:</b> Jms<br/>
 * <b>Description:</b>The module responsible for interacting with JMS. It
 * exposes various services for sending and asserting messages <br/>
 * 
 */
public class JmsSystemModule {

	private final QueueConnectionFactory connectionFactory;
	private JmsTemplate jmsTemplate;

	/**
	 * @param connectionFactory
	 */
	public JmsSystemModule(QueueConnectionFactory connectionFactory) {
		super();
		this.connectionFactory = connectionFactory;
	}

	@PostConstruct
	public void createConnection() throws JMSException {
		Reporter.log("Creating JMS connection", true);
		jmsTemplate = new JmsTemplate(connectionFactory);

	}

	/**
	 * Send message to the specified queue.
	 * 
	 * @param queueName
	 *            The name of the destination queue
	 * @param type
	 *            TEXT,MAP,BYTES or OBJECT
	 * @param messageObject
	 * @throws Exception
	 *             if queue is not exist of failed to create connection to queue
	 *             or to close the queue
	 * @throws IllegalArgumentException
	 *             If message type is not supported
	 */
	public void sendMessage(final String queueName, final MessageType type, final Object messageObject)
			throws Exception {
		Reporter.log("Sending message from type " + type.name() + " to queue " + queueName);
		jmsTemplate.send(queueName, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				switch (type) {
				case TEXT:
					if (messageObject instanceof String) {
						return session.createTextMessage((String) messageObject);
					} else {
						throw new IllegalArgumentException("Cannot send as text message object is not String "
								+ type.name());
					}
				case OBJECT:
					if (messageObject instanceof Serializable)
						return session.createObjectMessage((Serializable) messageObject);
					break;
				default:
					throw new IllegalArgumentException("Unsupported type " + type.name());
				}
				return null;

			};
		});

	}

	/**
	 * Receive one message from queue
	 * 
	 * @param queueName
	 * @param timeout
	 *            time to wait in millis for message. specify zero if no timeout
	 *            is required
	 * @return message from queue or null if none exist
	 * @throws JMSException
	 */
	public Message receiveMessage(final String queueName, long timeout) throws JMSException {
		if (timeout == 0) {
			timeout = 1;
		}
		Reporter.log("Receiving message from queue " + queueName);
		jmsTemplate.setReceiveTimeout(timeout);
		Message message = jmsTemplate.receive(queueName);
		if (null != message) {
			StringBuilder messageReport = new StringBuilder();
			messageReport.append("Message from type ").append(message.getClass().getSimpleName())
					.append(" was received");
			if (message instanceof TextMessage) {
				messageReport.append(" with content '");
				try {
					messageReport.append(((TextMessage) message).getText()).append("'");
				} catch (JMSException e) {
					messageReport.append("<Failed getting message content>");
				}
			}
			Reporter.log(messageReport.toString(), true);

		}
		return message;

	}

	/**
	 * Receive all the messages in a given queue.
	 * 
	 * @param queueName
	 * @return list of messages. If none exist in queue the list would be empty
	 * @throws JMSException
	 */
	public List<Message> recieveAllMessages(final String queueName) throws JMSException {
		final List<Message> messages = new ArrayList<Message>();
		Message message = null;
		while ((message = receiveMessage(queueName, 0)) != null) {
			messages.add(message);
		}
		return messages;
	}

	/**
	 * Return all the messages from specified queue without removing from queue.
	 * 
	 * @param queueName
	 * @return List of messages.
	 * @throws Exception
	 *             If queue is not exist or failed to create connection
	 */
	public List<Message> browseQueue(String queueName) throws Exception {
		Reporter.log("Browsing queue " + queueName, true);
		final List<Message> messages = jmsTemplate.browse("Blocking", new BrowserCallback<List<Message>>() {
			@Override
			public List<Message> doInJms(Session session, QueueBrowser browser) throws JMSException {
				List<Message> messages = new ArrayList<Message>();
				@SuppressWarnings("unchecked")
				Enumeration<Message> e = browser.getEnumeration();
				while (e.hasMoreElements()) {
					messages.add(e.nextElement());

				}
				return messages;
			}
		});
		return messages;
	}

	/**
	 * Cleans all messages in queue
	 * 
	 * @param queueName
	 * @throws Exception
	 */
	public void flushQueue(String queueName) throws Exception {
		Reporter.log("Flusing queue " + queueName, true);
		while (receiveMessage(queueName, 1000) != null) {
		}
	}

}
