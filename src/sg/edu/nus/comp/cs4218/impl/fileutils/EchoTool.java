package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class EchoTool extends ATool implements IEchoTool {
	
	public EchoTool() {
		super(null);
	}
	
	public EchoTool(String[] args) {
		super(args);
	}

	@Override
	public String execute(File workingDir, String stdin) {
		if (args == null) {
			return System.lineSeparator();
		}
		
		String line = buildLine(args);
		String retVal = echo(line);
		
		return retVal;
	}

	@Override
	public String echo(String toEcho) {
		if (toEcho == null) {
			return "";
		}
		 
		int newLineIndex = toEcho.indexOf(System.lineSeparator());
		if (newLineIndex == 0) {
			toEcho = "";
		} else if (newLineIndex > 0) {
			toEcho = toEcho.substring(0, newLineIndex);
		}
		
		/**
		 * Defensive approach:
		 * If toEcho contains more text after a new line,
		 * trim toEcho until just before the new line.
		 * Although buildLine should expectedly have cut the original
		 * text into a line of text (excluding new line char).
		 */
		
		toEcho = toEcho.trim();
		return toEcho + System.lineSeparator();
	}
	
	/**
	 * Concatenates all the strings in the array, 
	 * stop when new line character is reached.
	 * 
	 * @param words The array of strings to be joined
	 * @return The concatenated string which represents a line of text.
	 */
	protected String buildLine(String[] words) {
		StringBuilder stringBuilder = new StringBuilder();

		for (String word : words) {
			if (word.equals(System.lineSeparator())) {
				break;
			}
			stringBuilder.append(word);
		}
		
		return stringBuilder.toString().trim();
	}

}
