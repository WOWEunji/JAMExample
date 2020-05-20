package taskManager;

import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import javax.sound.midi.Soundbank;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.Value;
import kr.ac.uos.ai.arbi.model.parser.ParseException;
import kr.re.kist.emma.mybom.MybomConfigure;
import sun.misc.Signal;
import sun.misc.SignalHandler;
import taskManager.aplview.APLViewer;
import taskManager.utility.CommunicationUtility;
import taskManager.utility.GLMessageManager;
import taskManager.utility.JAMUtilityManager;
import taskManager.utility.RecievedMessage;
import uos.ai.jam.Interpreter;
import uos.ai.jam.JAM;
import uos.ai.jam.parser.JAMParser;

public class TaskManager extends ArbiAgent {
	private Interpreter interpreter;
	private GLMessageManager msgManager;
	private BlockingQueue<RecievedMessage> messageQueue;
	private APLViewer aplViewer;

	public static final String JMS_BROKER_URL = MybomConfigure.ArbiHost();
	
	public static final String TASKMANAGER_ADRESS = "agent://www.arbi.com/taskManager";
	public static final String CONTEXTMANAGER_ADRESS = "agent://www.arbi.com/contextManager";
	public static final String KNOWLEDGEMANAGER_ADRESS = "agent://www.arbi.com/knowledgeManager";
	public static final String BEHAVIOUR_INTERFACE_ADDRESS = "agent://www.arbi.com/BehaviourInterface";
	public static final String PERCEPTION_ADRESS = "agent://www.arbi.com/perception";
	public static final String ACTION_ADRESS = "agent://www.arbi.com/action";
	public static final String REASONER_ADRESS = "agent://www.arbi.com/taskReasoner";
	public static final String DIALOGUE_ADRESS = "agent://www.arbi.com/dialogue";
	public static final String PREFIX = "http://www.arbi.com//ontologies#";
	
	public static final String NOTIFY_SUSBSCRIBE_TEST = "(rule (fact (SubscribeTest $state)) --> (notify (SubscribeTest $state)))";
	
	private String ID_NOTIFY_SUSBSCRIBE_TEST;
	
	private TaskManagerDataSource dc;
	
	public TaskManager() {
		
		ArbiAgentExecutor.execute(JMS_BROKER_URL, TASKMANAGER_ADRESS, this);
		
		interpreter = JAM.parse(new String[] { "plan/boot.jam" });
	
		messageQueue = new LinkedBlockingQueue<RecievedMessage>();
		
		msgManager = new GLMessageManager(interpreter);
		
		aplViewer = new APLViewer(interpreter);
				
		init();
	}
	private void init() {
		msgManager.assertFact("GLUtility", msgManager);
		msgManager.assertFact("Communication", new CommunicationUtility(this, dc));
		msgManager.assertFact("ExtraUtility", new JAMUtilityManager(interpreter));
		msgManager.assertFact("TaskManager", this);
		msgManager.assertFact("CustomFunction", new CustomFunction());
		
		Signal.handle(new Signal("TERM"), signalHandler); 
		//aplViewer.init();		
		Thread t = new Thread() {
			public void run() {
				interpreter.run();
			}
		};
		
		t.start();
		
	}

	@Override
	public void onNotify(String sender, String notification) {
		System.out.println("recieved Notify from " + sender + " : " + notification);
		aplViewer.msgReceived(notification, sender);
		RecievedMessage msg = new RecievedMessage(sender, notification);
		messageQueue.add(msg);	
	}

	@Override
	public void onStart() {
		dc = new TaskManagerDataSource(this);

		dc.connect(JMS_BROKER_URL, "ds://www.arbi.com/TaskManager");
		// subscribe
		ID_NOTIFY_SUSBSCRIBE_TEST = dc.subscribe(NOTIFY_SUSBSCRIBE_TEST);
	}

	
	public boolean dequeueMessage() {

		if (messageQueue.isEmpty())
			return false;
		else {
			try {
				RecievedMessage message = messageQueue.take();
				GeneralizedList gl = null;
				String data = message.getMessage();
				String sender = message.getSender();

				aplViewer.msgReceived(data, sender);

				//String data를 GL 구조로 바꿔줌.
				gl = GLFactory.newGLFromGLString(data);

				System.out.println("message dequeued : " + gl.toString());
				msgManager.assertGL(data);	
			} catch (InterruptedException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		}
	}

	@Override
	public String onQuery(String sender, String query) {
		System.out.println("recieved query from " + sender + " : " + query);

		aplViewer.msgReceived(query, sender);

		String result = handleQuery(query);
		System.out.println(query);
		return "(result \"" + result + "\")";
	}

	private String handleQuery(String query) {
		String result = "success";
		try {
			GeneralizedList gl = GLFactory.newGLFromGLString(query);
			String name = gl.getName();
			if (name.equals("retractFact")) {
				System.out.println("retractFact called");
				msgManager.retractFact(gl.getExpression(0).toString());
			} else if (name.equals("updateFact")) {
				System.out.println("updateFact called");
				msgManager.updateFact(gl.getExpression(0).toString(), gl.getExpression(1).toString());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}


	@Override
	public void onData(String sender, String data) {
		System.out.println("recieved data from " + sender + " : " + data);
		RecievedMessage msg = new RecievedMessage(sender, data);
		messageQueue.add(msg);
	}

	@Override
	public String onRequest(String sender, String request) {
		System.out.println("recieved data from " + sender + " : " + request);
		RecievedMessage msg = new RecievedMessage(sender, request);
		messageQueue.add(msg);

		return "(ok)";
	}

	public void addMessage(String sender,String data){
		RecievedMessage msg = new RecievedMessage(sender,data);
		messageQueue.add(msg);
	}
	
	public GLMessageManager getMsgManager() {
		return msgManager;
	}
	
	public String toString() {
		return "TaskManager";
	}

	public static void main(String[] args) {
		 Signal.handle(new Signal("TERM"), new SignalHandler () {
		      public void handle(Signal sig) {
		        System.out.println(
		          "Aaarggh, a user is trying to interrupt me!!");
		        System.out.println(
		          "(throw garlic at user, say `shoo, go away')");
		      }
		    });
		new TaskManager();	
	}	
	SignalHandler signalHandler = new SignalHandler() {
		
		@Override
		public void handle(Signal signal) {
			// TODO Auto-generated method stub
			if(signal.toString().trim().equals("SIGTERM"))
			{
				dc.unsubscribe(ID_NOTIFY_SUSBSCRIBE_TEST);
				System.exit(0);
			}
		}
	};
}

