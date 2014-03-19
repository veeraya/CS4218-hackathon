package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import sg.edu.nus.comp.cs4218.extended2.IWcTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class WcTool extends ATool implements IWcTool {
	
	public static final int STATUS_CODE_FILE_EXCEPTION = 1;
	public static final int STATUS_CODE_INVALID_ARGUMENTS = 2;
	public static final int STATUS_CODE_INVALID_OPTION = 3;
	public static final int STATUS_CODE_STDIN_EXPECTED = 4;
	
	public WcTool(String[] arguments) {
		super(arguments);
	}

	@Override
	public String getCharacterCount(String input) {
		if (input == null)
			return "0";
		else
			return String.valueOf(input.length());
	}

	@Override
	public String getWordCount(String input) {
		
		if (input == null)
			return "0";
		
		String inputTrim = input.trim();
		if (inputTrim.isEmpty())
			return "0";
		else {
			String[] words = inputTrim.split("\\s+");
			return String.valueOf(words.length);
		}
	}

	@Override
	public String getNewLineCount(String input) {
		if (input == null)
			return "0";
		int count = 0;
		int index = -1;
		while ((index = input.indexOf(System.lineSeparator(), index+1)) != -1) {
			count ++;
		}
		return String.valueOf(count);
	}

	@Override
	public String getHelp() {
		try {
			return new String(Files.readAllBytes(new File("help_files/wc_help").toPath())).trim();
		} catch (IOException iOE) {
			return null;
		}
	}

	@Override
	public String execute(File workingDir, String stdin) {
		String result = null;
		boolean hasEmptyArg = false;
		
		// check for empty arguments
		if (args != null) {
			for (String arg : args) {
				if (arg.trim().isEmpty()) {
					hasEmptyArg = true;
					break;
				}
			}
		}
		
		// no argument and empty arguments are invalid
		if (args == null || args.length == 0 || hasEmptyArg) {
			setStatusCode(STATUS_CODE_INVALID_ARGUMENTS);
			result = "wc: Invalid arguments given";
		}
		
		else {
			Map<String, Boolean> options = getOptions(args);
			
			if (options == null) {
				setStatusCode(STATUS_CODE_INVALID_OPTION);
				result = "wc: illegal option" + System.lineSeparator() + "usage: wc [-clmw] [file ...]";
			}
			
			else if (options.size() == 0) {
				if (args[0].equals("-"))
					result = getResultStdin(stdin, true, true, true);
				else
					result = getResultFiles(args, true, true, true);
			}
			
			// if there is a valid option
			else {
				
				if (options.get("-help") != null)
					result = getHelp();
				
				else {
					int numOfOptions = options.size();
					boolean isM = (options.get("-m") != null) ? true : false;
					boolean isW = (options.get("-w") != null) ? true : false;
					boolean isL = (options.get("-l") != null) ? true : false;
					boolean isStdin = (args.length == numOfOptions || 
									  (args.length == (numOfOptions + 1) && args[numOfOptions].equals("-")));
					
					if (isStdin) {
						result = getResultStdin(stdin, isM, isW, isL);
					}
					else {
						String[] fileNames = Arrays.copyOfRange(args, options.size(), args.length);
						result = getResultFiles(fileNames, isM, isW, isL);
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * To retrieve options for wc command if any.
	 * @return	Returns the valid options found in the form of a mapping from 
	 * 			the option's name to true if the option is found.
	 * 			If there is an argument that starts with "-" but it's not a valid option then return null.
	 */
	private Map<String, Boolean> getOptions(String[] args) {
		Map<String, Boolean> optionsMap = new HashMap<String, Boolean>();
		
		for (String arg : args) {
			
			switch (arg) {
				case "-help":
					optionsMap.put("-help", true);
					break;
				case "-m":
					optionsMap.put("-m", true);
					break;
				case "-w":
					optionsMap.put("-w", true);
					break;
				case "-l":
					optionsMap.put("-l", true);
					break;
				default:
					if (arg.startsWith("-") && arg.length() > 1)
						return null;
					else
						break;
			}
		}
		return optionsMap;
	}
	
	/**
	 * Executes the wc command for standard input
	 * @param stdin		standard input entered by user
	 * @param optionM	true to count characters in standard input, else false
	 * @param optionW	true to count words in standard input, else false
	 * @param optionL	true to count new lines in standard input, else false
	 * @return			the results
	 */
	private String getResultStdin (String stdin, boolean optionM, boolean optionW, boolean optionL) {
		StringBuilder strBuilder = new StringBuilder();
		
		if (stdin == null) {
			setStatusCode(STATUS_CODE_STDIN_EXPECTED);
			strBuilder.append("wc: A standard input is expected");
		}
		
		else {
			if (optionL)
				strBuilder.append("\t"+getNewLineCount(stdin));
			if (optionW)
				strBuilder.append("\t"+getWordCount(stdin));
			if (optionM)
				strBuilder.append("\t"+getCharacterCount(stdin));
		}
		
		return strBuilder.toString();
	}
	
	/**
	 * Executes the wc command for files
	 * @param fileNames	array of file names to process
	 * @param optionM	true to count characters in standard input, else false
	 * @param optionW	true to count words in standard input, else false
	 * @param optionL	true to count new lines in standard input, else false
	 * @return			the results
	 */
	private String getResultFiles (String[] fileNames, boolean optionM, boolean optionW, boolean optionL) {
		StringBuilder resultBuilder = new StringBuilder();
		boolean oneFile = (fileNames.length == 1);
		int totalChars = 0, totalWords = 0, totalNewLines = 0;
		int chars, words, newLines;
		
		for (String fileName : fileNames) {
			String content;
			File file = new File(fileName);
			if (file.isDirectory()) {
				resultBuilder.append("wc: "+fileName+": read: Is a directory" + System.lineSeparator());
				continue;
			}
			
			else if (!file.exists()) {
				resultBuilder.append("wc: "+fileName+": open: No such file or directory" + System.lineSeparator());
				continue;
			}
			
			else {
				content = getFileContent(fileName);
				if (content == null) {
					resultBuilder.append("wc: "+fileName+": Failed to read content");
					continue;
				}
			}
			
			if (optionL) {
				newLines = Integer.parseInt(getNewLineCount(content));
				totalNewLines += newLines;
				resultBuilder.append("\t"+newLines);
			}
			if (optionW) {
				words = Integer.parseInt(getWordCount(content));
				totalWords += words;
				resultBuilder.append("\t"+words);
			}
			if (optionM) {
				chars = Integer.parseInt(getCharacterCount(content));
				totalChars += chars;
				resultBuilder.append("\t"+chars);
			}
			
			if (oneFile)
				resultBuilder.append(" "+fileName);
			else
				resultBuilder.append(" "+fileName + System.lineSeparator());
		}
		
		if (!oneFile) {
			if (optionL)
				resultBuilder.append("\t"+totalNewLines);
			if (optionW)
				resultBuilder.append("\t"+totalWords);
			if (optionM)
				resultBuilder.append("\t"+totalChars);
			
			resultBuilder.append(" total");
		}
		
		return resultBuilder.toString();
	}
	
	private String getFileContent(String fileName) {
		StringBuilder fileContentBuilder = new StringBuilder();
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			int charValue;
			while ((charValue = reader.read()) != -1) {
				fileContentBuilder.append((char)charValue);
			}
			reader.close();
		} catch (Exception e) {
			setStatusCode(STATUS_CODE_FILE_EXCEPTION);
			return null;
		} 
		
		return fileContentBuilder.toString();
	}
	
}
