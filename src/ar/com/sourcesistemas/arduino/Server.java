package ar.com.sourcesistemas.arduino;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.omg.PortableInterceptor.ObjectReferenceFactoryHolder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.sourcesistemas.arduino.constantes.MessageProtocol;

public class Server implements MessageListener {
    private static int ackMode;
    private static String messageQueueName;
    private static String messageBrokerUrl;
 
    private Session session;
    private boolean transacted = false;
    private MessageProducer replyProducer;
    private MessageProtocol messageProtocol;
    
    private Conexion conn;
 
    static {
        messageBrokerUrl = "tcp://93.188.167.193:61616";
        messageQueueName = "brazo_robot";
        ackMode = Session.AUTO_ACKNOWLEDGE;
    }
 
    public Server(Conexion conn) {
        try {
        	
        	this.conn = conn;
            //This message broker is embedded
            BrokerService broker = new BrokerService();
            broker.setPersistent(false);
            broker.setUseJmx(false);
            broker.addConnector(messageBrokerUrl);
            broker.start();
        } catch (Exception e) {
            //Handle the exception appropriately
        }
 
        //Delegating the handling of messages to another class, instantiate it before setting up JMS so it
        //is ready to handle messages
        this.messageProtocol = new MessageProtocol();
        this.setupMessageQueueConsumer();
    }
 
    private void setupMessageQueueConsumer() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(messageBrokerUrl);
        Connection connection;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            this.session = connection.createSession(this.transacted, ackMode);
            Destination adminQueue = this.session.createQueue(messageQueueName);
 
            //Setup a message producer to respond to messages from clients, we will get the destination
            //to send to from the JMSReplyTo header field from a Message
            this.replyProducer = this.session.createProducer(null);
            this.replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
 
            //Set up a consumer to consume messages off of the admin queue
            MessageConsumer consumer = this.session.createConsumer(adminQueue);
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            //Handle the exception appropriately
        }
    }

    public void onMessage(Message message) {
        try {
            TextMessage response = this.session.createTextMessage();
            if (message instanceof TextMessage) {
                TextMessage txtMsg = (TextMessage) message;
                String messageText = txtMsg.getText();
                ObjectMapper obj = new ObjectMapper();
                Command readValue =null;
				try {
					readValue = obj.readValue(messageText, Command.class);
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(readValue.getCommand() != null){
                conn.codeSender(readValue.getCommand());
                System.out.println("comando enviado a arduino con exito");
				}else{
					System.out.println("recibi un null");
					
				}
            }
 
         
        } catch (JMSException e) {
            System.out.println("tiro exeption");
        }
    }
 
   
}