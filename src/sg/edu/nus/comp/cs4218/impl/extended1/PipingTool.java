package sg.edu.nus.comp.cs4218.impl.extended1;

import java.io.File;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * PipingTool will execute each ITool generated from the piping command sequentially.
 */
public class PipingTool extends ATool implements IPipingTool {
	Vector<ITool> tools;
	
	/**
	 * Constructor for PipingTool
	 * @param tools		vector of ITools to execute sequentially
	 */
	public PipingTool(Vector<ITool> tools) {
		super(null);
		this.tools = tools;
	}
	
	@Override
	public String execute(File workingDir, String stdin) {
		String result = "";
		
		if (tools.size() < 2) {
			setStatusCode(1);
			return "Error: invalid pipe command";
		}
		
		// for the first 2 ITools
		ITool from = tools.get(0);
		ITool to = tools.get(1);
		if (stdin != null) {
			result = pipe(stdin, from);
			if (getStatusCode() == 0)
				result = pipe(result, to);
		}
		else
			result = pipe(from, to);
		
		// for the subsequent ITools
		for (int i=2; i<tools.size(); i++) {
			to = tools.get(i);
			result = pipe(result, to);
			
			if (getStatusCode() != 0)
				break;
		}
		
		return result;
	}
	
	/**
	 * Executes "from" ITool first followed by "to" ITool.
	 * If "from" ITool did not execute successfully, the status code will be set to 1
	 * and "to" ITool will not be executed.
	 * @param from		the ITool to start executing first
	 * @param to		the ITool to execute after from has successfully executed
	 * @return			Output of "to" ITool
	 */
	@Override
	public String pipe(ITool from, ITool to) {
		String result = from.execute(new File(System.getProperty("user.dir")), null);
	
		if (from.getStatusCode() != 0) {
			setStatusCode(1);
			return result;
		}
		
		return pipe(result, to);
	}
	
	/**
	 * Executes "to" ITool with the standard output from the previous ITool executed.
	 * If "to" ITool did not execute successfully, the status code will be set to 1.
	 * @param to		the ITool to be executed
	 * @param stdout	the standard output from the previous ITool's successful execution
	 * @return			Output of "to" ITool
	 */
	@Override
	public String pipe(String stdout, ITool to) {
		String result = to.execute(new File(System.getProperty("user.dir")), stdout);
		
		if (to.getStatusCode() != 0)
			setStatusCode(1);
		
		return result;
	}

}
