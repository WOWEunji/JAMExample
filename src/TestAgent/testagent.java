package TestAgent;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.re.kist.emma.mybom.MybomConfigure;

public class testagent extends ArbiAgent{
	DataSource dc;
	public testagent() {
		// TODO Auto-generated constructor stub
		ArbiAgentExecutor.execute(MybomConfigure.ArbiHost(), "agent://testAgent", this);
		dc = new DataSource();
		dc.connect(MybomConfigure.ArbiHost(), "dc://kist.re.kr/dc");
		Notify();
		
	}
	
	public void Notify() {
		dc.assertFact("(SubscribeTest \"start\")");
	}
	
	@Override
	public String onRequest(String sender, String request) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		send(sender, "(Robot_task \"start\" \"test\")");
		return "(ok)";
	}
	
	@Override
	public String onQuery(String sender, String query) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "(ok)";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new testagent();

	}

}
