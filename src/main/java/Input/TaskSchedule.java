package Input;

import GUI.MainWindow;
import Model.Graph;
import Model.State;
import Output.Output;
import Output.OutputHandler;
import Parser.DotFileParser;
import Schedule.Algorithm;
import Schedule.RuntimeMonitor;
import javafx.application.Application;

import java.io.FileNotFoundException;

public class TaskSchedule {
	private static Graph graph;
	private Boolean Done = true ;
	private static State _finalSchedule;
	private static InputHandler input;
	private static RuntimeMonitor _runtimeMonitor;
	//Singleton pattern
	private static TaskSchedule _instance = null;
	private TaskSchedule(){
		if(_instance == null){
			_instance = this;
		}
	}
	public static TaskSchedule getInstance() {
		return _instance;
	}

	public State getSchedule(){
		return _finalSchedule;
	}


	public static void main(String[] args) {
		new TaskSchedule(); //Create a new instance
		input = new InputHandler(args);

		try {
			if(input.toVisualize()) {
				Application.launch(MainWindow.class);
			} else {
				runAlgorithm(input);
			}
			//TODO:transfer from final state to output
		} catch (Exception e1) {
			e1.printStackTrace();
		}}

		public static void runAlgorithm(InputHandler input) throws FileNotFoundException {
			DotFileParser parser = new DotFileParser();
			Graph g = parser.parseDotFile(System.getProperty("user.dir")+"/"+ input.getFilePath());
			graph = g;
			Algorithm sch = input.getAlgorithm();
			State finalState =  sch.schedule();
			System.out.println("The scheduled node of finalState: "+finalState.getscheduledNodes().size());
			_finalSchedule = finalState;
			System.out.println("The cost of the final state is " +finalState.getCost());
			Output output = new Output(input);
			OutputHandler outputHandler = new OutputHandler(finalState,g);
			String finalOutput = outputHandler.getFinalOutput();
			output.generateGraph(finalOutput, "dot");
		}

		public static InputHandler getInput(){
			return input;
		}
		public static Graph getGraph(){ return graph; }

    public static String getGraphName() {
        String filePath = input.getFilePath();
        int index = filePath.lastIndexOf("/");
        String fileName = filePath.substring(index + 1);
        return fileName;

    }
		public static void toggleAlgorithm(Boolean isAstar){
			input.setAlgorithm(isAstar);
		}

	public String getPath() {
		return System.getProperty("user.dir")+"/"+ input.getFilePath();
	}
}



