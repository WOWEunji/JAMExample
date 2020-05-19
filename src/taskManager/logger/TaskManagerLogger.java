package taskManager.logger;

import kr.ac.uos.ai.arbi.agent.logger.AgentAction;
import kr.ac.uos.ai.arbi.agent.logger.LogTiming;
import kr.ac.uos.ai.arbi.agent.logger.LoggerManager;
import taskManager.TaskManager;
import taskManager.logger.*;
import taskManager.logger.argument.GoalArgument;
import taskManager.logger.argument.RelationArgument;
import uos.ai.jam.Goal;
import uos.ai.jam.IntentionStructureChangeListener;
import uos.ai.jam.WorldModelChangeListener;
import uos.ai.jam.expression.Relation;
import uos.ai.jam.plan.APLElement;


public class TaskManagerLogger implements WorldModelChangeListener, IntentionStructureChangeListener {

	private AssertFactLogger assertLogger;
	private AgentAction assertAction;
	private AgentAction retractAction;
	private AgentAction newGoalAction;
	private AgentAction unpostGoalAction;
	private AgentAction intendAction;
	private TaskManager taskManager;
	
	
	public TaskManagerLogger(TaskManager taskManager){
		this.taskManager = taskManager;
		init();
	}
	
	
	
	public void init(){
		assertAction = new AgentAction("WorldModel",new AssertFactLogger(taskManager.getMsgManager()));
		retractAction = new AgentAction("WorldModel",new RetractFactLogger(taskManager.getMsgManager()));
		newGoalAction = new AgentAction("Goal",new NewGoalLogger(taskManager.getMsgManager()));
		unpostGoalAction = new AgentAction("Goal",new UnpostGoalLogger(taskManager.getMsgManager()));
		intendAction = new AgentAction("Goal",new IntendLogger(taskManager.getMsgManager()));
		
		LoggerManager loggerManager = LoggerManager.getInstance();
		
		loggerManager.registerAction("Intend", intendAction,LogTiming.NonAction);
		loggerManager.registerAction("New", newGoalAction,LogTiming.NonAction);
		loggerManager.registerAction("Unpost", unpostGoalAction, LogTiming.NonAction);
		loggerManager.registerAction("Assert", assertAction,LogTiming.NonAction);
		loggerManager.registerAction("Retract", retractAction,LogTiming.NonAction);
		System.out.println("inited");
	}
	

	@Override
	public void worldModelChanged(Relation[] retracted, Relation asserted) {
		if (asserted != null) {
			if (!asserted.getName().equals("CURRENT_TIME") && !asserted.getName().equals("APL")) {
				//System.out.println("notified WorldModelChange asserted :" + asserted.getName());
				RelationArgument argument = generateRelationArgument(asserted);
	
				assertAction.execute(argument);
				
			
			}
		}
		if (retracted != null) {
			for (int i = 0; i < retracted.length; i++) {
				if (!retracted[i].getName().equals("CURRENT_TIME")){
					System.out.println("notified WorldModelChange Retracted : " + retracted[i].getName());
					RelationArgument argument = generateRelationArgument(retracted[i]);
					
					//retractAction.execute(argument);
				}
			}
		}
	}
	
	public RelationArgument generateRelationArgument(Relation argument){
		RelationArgument newArgument = new RelationArgument(argument.getArity());
		newArgument.setName(argument.getName());
		
		for(int i = 0; i < argument.getArity();i++){
			
			newArgument.getExpresisonList()[i] = argument.getArg(i).toString();
		}
		
		return newArgument;
	}
	
	public GoalArgument generateGoalArgument(Goal goal){
		GoalArgument newArgument = new GoalArgument();
		newArgument.setName(goal.getName());
		newArgument.setUtility(goal.getGoalAction().getUtility().toString());
		Relation goalRelation = goal.getGoalAction().getGoal();
		for(int i = 0; i < goalRelation.getArity();i++){
			newArgument.addExpression(goalRelation.getArg(i).toString());
		}
		
		return newArgument;
	}
	
	
	@Override
	public void goalAdded(Goal goal) {
		System.out.println("notified New Goal");
		
		//newGoalAction.execute(generateGoalArgument(goal));
	}

	@Override
	public void goalRemoved(Goal goal) {
		System.out.println("notified Goal Removal");

		//unpostGoalAction.execute(generateGoalArgument(goal));
	}

	


	@Override
	public void intended(APLElement goal) {
		// TODO Auto-generated method stub
		
	}

}
