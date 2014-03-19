package sg.edu.nus.comp.cs4218.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CommTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CutTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PasteTool;
import sg.edu.nus.comp.cs4218.impl.extended2.SortTool;
import sg.edu.nus.comp.cs4218.impl.extended2.UniqTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WcTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CdTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CopyTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.DeleteTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.LsTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.MoveTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;

public class Shell implements IShell {
	
	final private static String DIR_REGEX_NO_SPACE  = "(((~|.|[a-zA-Z]:)[\\\\/])?([\\\\/]*[a-zA-Z][a-zA-Z0-9_&-]*)+[\\\\/]*)";
	final private static String DIR_REGEX_SPACE     = "(((~|.|[a-zA-Z]:)[\\\\/])?([\\\\/]*[a-zA-Z]([a-zA-Z0-9_&-]|\\s)*)+[\\\\/]*)";
	final private static String DIR_REGEX_Q_SPACE   = "\""+DIR_REGEX_SPACE+"\"";
	final private static String DIR_REGEX           = "("+DIR_REGEX_NO_SPACE+"|"+DIR_REGEX_Q_SPACE+")";
	final private static String DIR_FILE_REGEX_NO_SPACE = "("+DIR_REGEX_NO_SPACE+"(((\\.)[a-zA-Z0-9]+)+)?)";
	final private static String DIR_FILE_REGEX_Q_SPACE  = "(\""+DIR_REGEX_SPACE+"(((\\.)[a-zA-Z0-9]+)+)?\")";
	final private static String DIR_FILE_REGEX          = "("+DIR_FILE_REGEX_NO_SPACE+"|"+DIR_FILE_REGEX_Q_SPACE+")";
	
	private static boolean stdinRequired = false;
	private static String stdIn = null;
	protected static String result = null;
	protected static boolean underTest = false;
	
	/**
	 * Parses the command line and instantiates the corresponding tool.
	 * @param commandline	command line entered by the user
	 * @return				the tool object to process the command line
	 */
	@Override
	public ITool parse(String commandline) {
		String commandTrim = commandline.trim();
		String[] args;

		stdinRequired = false;
		stdIn = null;
		
		if (commandTrim.indexOf("|") != -1) {
			Vector<ITool> tools = getPipeTools(commandTrim);
			return tools == null ? null : new PipingTool(tools);
		}
		
		else if (commandTrim.startsWith("pwd"))
			return new PWDTool(); // ignore trailing arguments in pwd command
		
		else if (commandTrim.startsWith("cd")) {
			args = getBasicCommandArgs("cd", commandTrim);
			return args == null ? null : new CdTool(args);
		}
		
		else if (commandTrim.startsWith("ls")) {
			args = getBasicCommandArgs("ls", commandTrim);
			return args == null ? null : new LsTool(args);
		}
		
		else if (commandTrim.startsWith("copy")) {
			args = getBasicCommandArgs("copy", commandTrim);
			return args == null ? null : new CopyTool(args);
		}
		
		else if (commandTrim.startsWith("move")) {
			args = getBasicCommandArgs("move", commandTrim);
			return args == null ? null : new MoveTool(args);
		}
		
		else if (commandTrim.startsWith("delete")) {
			args = getBasicCommandArgs("delete", commandTrim);
			return args == null ? null : new DeleteTool(args);
		}
		
		
		else if (commandTrim.startsWith("cat")) {
			args = getBasicCommandArgs("cat", commandTrim);
			return args == null ? null : new CatTool(args);
		}
		
		else if (commandTrim.startsWith("echo")) {
			args = getBasicCommandArgs("echo", commandTrim);
			return args == null ? null : new EchoTool(args);
		}
		
		else if (commandTrim.startsWith("grep")) {
			args = getGrepArgs(commandTrim);
			return args == null ? null : new GrepTool(args);
		}
		
		else if (commandTrim.startsWith("cut")) {
			args = getTextUtilArgs("cut", commandTrim);
			return args == null ? null : new CutTool(args);
		}
		
		else if (commandTrim.startsWith("paste")) {
			args = getTextUtilArgs("paste", commandTrim);
			return args == null ? null : new PasteTool(args);
		}
		
		else if (commandTrim.startsWith("comm")) {
			args = getTextUtilArgs("comm", commandTrim);
			return args == null ? null : new CommTool(args);
		}
		
		else if (commandTrim.startsWith("sort")) {
			args = getTextUtilArgs("sort", commandTrim);
			return args == null ? null : new SortTool(args);
		}
		
		else if (commandTrim.startsWith("uniq")) {
			args = getTextUtilArgs("uniq", commandTrim);
			return args == null ? null : new UniqTool(args);
		}
		
		else if (commandTrim.startsWith("wc")) {
			args = getTextUtilArgs("wc", commandTrim);
			return args == null ? null : new WcTool(args);
		}
		
		else
			return null;
		
	}
	
	/**
	 * Retrieves the arguments for the commandline depending on the command type
	 * @param commandType	type of command such as cd, copy, delete, cat, echo
	 * @param commandline	command line entered by user
	 * @return				If the arguments are valid according to the command type, then they will be returned. 
	 * 						Else return null.
	 * 						For commands "ls", "cd" and "echo", if there are no arguments, 
	 * 						it will return a String array with 1 element of empty string.
	 * 						For "pwd" it will return null since arguments do not matter
	 */
	protected String[] getBasicCommandArgs(String commandType, String commandline) {
		
		String argsStr = null;
		String commandTrim = commandline.trim();
		
		if (commandType.equals("pwd"))
			return null;
		
		String [] splitted = commandTrim.split("\\s+",2);
		if (!splitted[0].equals(commandType))
			return null;
		
		if (splitted.length != 2) {
			if (commandType.equals("cd") || commandType.equals("ls") || commandType.equals("echo") )
				return new String[]{""};
			else if (commandType.equals("cat"))
				return new String[]{"-"};
			else
				return null;
		}
		else 
			argsStr = splitted[1];
		
		switch(commandType) {
			case "copy":
			case "move":
				return getCopyMoveArgs(argsStr);
				
			case "echo":
				Vector<String> argsVect = new Vector<String>();
				argsVect.add(argsStr);
				return cleanUpArguments(argsVect);
				
			case "delete":
			case "ls":
				return getAllDirFileArgs(argsStr);
			
			case "cd" :
				return getCdArgs(argsStr);
				
			case "cat":
				return getCatArgs(argsStr);
				
			default:
				return null;
		}
	}
	
	/**
	 * Get arguments for cd command. 
	 * Valid arguments are [directory], "-" or ".."
	 * @param argsStr	string of arguments in a "cd" command
	 * @return			an array of valid arguments
	 */
	private String[] getCdArgs(String argsStr) {
		Vector<String> argsVect = new Vector<String>();
		
		if (argsStr.equals("-") || argsStr.equals(".."))
			return new String[] {argsStr};
		else {
			String matchedStr = findDirFile(argsStr, true);
			if (matchedStr != null) {
				argsVect.add(matchedStr);
				if (!reduceString(matchedStr, argsStr).trim().isEmpty())
					return null;
			}
			else
				return null;
		}
		return cleanUpArguments(argsVect);
	}
	
	/**
	 * Get arguments for cat command
	 * Valid arguments are "-", [file] or [directory]
	 * @param argsStr	string of arguments in a "cat" command
	 * @return			an array of valid arguments
	 */
	private String[] getCatArgs(String argsStr) {
		
		String mainArgsStr = argsStr;
		Vector<String> argsVect = new Vector<String>();
		boolean catArgsDone = false;
		stdinRequired = false;
		
		if (mainArgsStr.isEmpty())
			return new String[]{"-"};
		
		while (!catArgsDone) {
			String matchedStr = findMatch("-", mainArgsStr);
			if (matchedStr != null) {
				// cannot have more than 1 "-"
				if (stdinRequired == true)
					return null;
				argsVect.add(matchedStr);
				mainArgsStr = reduceString(matchedStr, mainArgsStr);
				stdinRequired = true;
			}
			else if ((matchedStr = findDirFile(mainArgsStr, false)) != null) {
				argsVect.add(matchedStr);
				mainArgsStr = reduceString(matchedStr, mainArgsStr);
			}
			else {
				return null;	
			}
			mainArgsStr.trim();
			if (mainArgsStr.isEmpty())
				catArgsDone = true;
		}
		
		return cleanUpArguments(argsVect);
	}
	
	/**
	 * Split a string according to arguments of file/directory name
	 * @param argsStr	string of arguments 
	 * @return			an array of file/directory names
	 */
	private String[] getAllDirFileArgs(String argsStr) {
		String mainArgsStr = argsStr;
		Vector<String> argsVect = new Vector<String>();
		
		while(!mainArgsStr.isEmpty()) {
			String matchedStr = findDirFile(mainArgsStr, false);
			
			if (matchedStr == null)
				return null;
			
			argsVect.add(matchedStr);
			mainArgsStr = reduceString(matchedStr, mainArgsStr);
		}
		
		return cleanUpArguments(argsVect);
	}
	
	/**
	 * Get arguments for move, copy command.
	 * Valid arguments are [file][file] or [file]+[directory]
	 * @param argsStr	string of arguments in a "move"/"copy" command
	 * @return			an array of valid arguments
	 */
	private String[] getCopyMoveArgs(String argsStr) {
		String[] args = getAllDirFileArgs(argsStr);
		
		// if there are supposedly more than 2 file names, check if the 
		// last name could be that of a directory name
		if (args.length > 2) {
			String lastArg = args[args.length-1];
			String matchedStr = findDirFile(lastArg, true);
			if (matchedStr == null || !reduceString(matchedStr, lastArg).isEmpty())
				return null;
		}
		
		else if (args.length < 2)
			return null;
		
		return args;
	}
	
	/**
	 * Returns the arguments for Grep command.
	 * Valid arguments are [-(A|B|C|c|o|v|help)]? [pattern] [- or file+]?
	 * @param commandline	Grep command entered by user
	 * @return				If the arguments are valid, then they will be returned. 
	 * 						Else return null.
	 */
	protected String[] getGrepArgs (String commandline) {
		String argsStr = null;
		Vector<String> argsVect = new Vector<String>();
		boolean hasPattern = false, hasOption = false, hasOptionHelp = false, 
				hasStdin = false, hasFiles = false;
		
		String commandTrim = commandline.trim();
		
		String [] splitted = commandTrim.split("\\s+", 2);
		if (splitted.length < 2 || !splitted[0].equals("grep"))
			return null;
		argsStr = splitted[1];
		
		while (!argsStr.isEmpty()) {
			boolean hasDone = false;
			
			// get an option if any
			if (!hasOption) {
				String option = findMatch("-(A|B|C|c|o|v|help)", argsStr);
				if (option != null) {
					hasOption = true;
					hasDone = true;
					
					argsStr = reduceString(option, argsStr);
					argsVect.insertElementAt(option, 0);
					
					if (option.matches("-(A|B|C)")) {
						String numStr = getNum(argsStr);
						if (numStr == null)
							return null;
						else {
							argsVect.add(numStr);
							argsStr = reduceString(numStr, argsStr);
						}
					}
					else if (option.equals("-help"))
						hasOptionHelp = true;
				}
			}
			else {
				// check for multiple options
				String option = findMatch("-(A|B|C|c|o|v|help)", argsStr);
				if (option != null)
					return null;
			}
			
			if (!hasDone && !hasPattern) {
				// get pattern
				String pattern = getPatternOrDelimiter(argsStr);
				if (pattern == null)
					return null;
				else {
					argsVect.add(pattern);
					argsStr = reduceString(pattern, argsStr);
					hasPattern = true;
					hasDone = true;
				}
			}
			
			// if there is a pattern, look for "-" or filename
			if (!hasDone && hasPattern && !hasStdin) {
				if (argsStr.charAt(0) == '-') {
					argsVect.add("-");
					argsStr = argsStr.substring(1);
					stdinRequired = true;
					hasStdin = true;
					hasDone = true;
				}
				else {
					String matchedStr;
					while ((matchedStr = findDirFile(argsStr, false)) != null) {
						argsVect.add(matchedStr);
						argsStr = reduceString(matchedStr, argsStr);
						hasDone = true;
						hasFiles = true;
					}
				}
			}
			
			if (!hasDone)
				return null;
		}
		
		// if there's no pattern but if there's "-help" then having no pattern is acceptable
		if (!hasOptionHelp && !hasPattern)
			return null;
		
		else if (!hasOptionHelp && !hasStdin && !hasFiles) {
			argsVect.add("-");
			stdinRequired = true;
		}
		
		return cleanUpArguments(argsVect);
	}
	
	/**
	 * Processes the piping command and generates a set of ITools corresponding to each part
	 * of the command
	 * @param commandline
	 * @return
	 */
	protected Vector<ITool> getPipeTools(String commandline) {
		String commandTrim = commandline.trim();
		
		// check that there is no | at the end or beginning of commandline
		if (commandTrim.startsWith("|") || commandTrim.endsWith("|"))
			return null;
		
		Vector<ITool> tools = new Vector<ITool>();
		
		String[] programs = commandTrim.split("(\\s)*(\\|)(\\s)*");
		boolean stdinRequiredTemp = false;
		for (int i=0; i<programs.length; ++i) {
			String prog = programs[i];
			ITool tool = parse(prog);
			// only take stdin for the first command
			if (i == 0)
				stdinRequiredTemp = stdinRequired;
			if (tool == null)
				return null;
			tools.add(tool);
		}
		
		if (tools.size() < 2)
			return null;
		
		stdinRequired = stdinRequiredTemp;
		return tools;
	}
	
	/**
	 * Retrieves the arguments for the commandline depending on the command type
	 * @param commandType	type of command such as cut, paste, comm, sort, uniq, wc
	 * @param commandline	command line entered by user
	 * @return				If the arguments are valid according to the command type, then they will be returned. 
	 * 						Else return null.
	 */
	protected String[] getTextUtilArgs(String commandType, String commandline) {
		String argsStr = null;
		String commandTrim = commandline.trim();
		
		String [] splitted = commandTrim.split("\\s+",2);
		if (!splitted[0].equals(commandType))
			return null;
		
		// if there is no argument, assume standard input
		if (splitted.length != 2) {
			if (commandType.matches("(uniq|paste|wc)")) {
				stdinRequired = true;
				return new String[]{"-"};
			}
			else
				return null;
		}
		else 
			argsStr = splitted[1];
		
		Map<String, String> optionsMap;
		switch(commandType) {
			case "wc":
				return getMultipleOptionsAndFilesArgs(argsStr, "-(help|m|w|l)", null, true, 4, 1, -1);
			
			case "sort":
				return getMultipleOptionsAndFilesArgs(argsStr, "-(help|c)", null, false, 2, 1, -1);
				
			case "comm":
				String[] args = getMultipleOptionsAndFilesArgs(argsStr, "-(help|c|d)", null, false, 2, 2, 2);
				boolean hasOpt = false;
				if (args != null) {
					// check that -c and -d cannot be used together
					for (String arg : args) {
						if (arg.equals("-c") || arg.equals("-d")) {
							if (hasOpt)
								return null;
							else
								hasOpt = true;
						}
					}
				}
				return args;
				
			case "paste":
				optionsMap = new HashMap<String, String>();
				optionsMap.put("-d", "DELIM OPTIONAL");
				return getMultipleOptionsAndFilesArgs(argsStr, "-(help|s|d)", optionsMap, true, 3, 1, -1);
				
			case "cut":
				return getCutArgs(argsStr);
				
			case "uniq":
				optionsMap = new HashMap<String, String>();
				optionsMap.put("-f", "NUM");
				return getMultipleOptionsAndFilesArgs(argsStr, "-(help|i|f)", optionsMap, true, 3, 1, -1);
				
			default:
				return null;
		}
	}
	
	/**
	 * Split a string of arguments according to options first followed by file names 
	 * or standard input (if allowed). Also checks if the number of options is less or 
	 * equal to the stated maximum number of options and the number of files is within 
	 * the range of the stated minimum and maximum number of files.
	 * 
	 * @param argsStr		the string of arguments to be splitted
	 * @param optionsRegex	the regular expression of the options allowed
	 * @param optionsMap	the extra part to the option such as a number or characters after specified option
	 * @param allowStdin	true if standard input is allowed, else false
	 * @param maxOptions	the maximum number of options allowed (-1 if there is no limit)
	 * @param minFiles		the minimum number of file names allowed (minimum value allowed is 0)
	 * @param maxFiles		the maximum number of file names allowed (-1 if there is no limit)
	 * @return				the array of arguments if they are all valid. Else return null.
	 */
	private String[] getMultipleOptionsAndFilesArgs(String argsStr, String optionsRegex, Map<String, String> optionsMap,
													boolean allowStdin, int maxOptions, int minFiles, int maxFiles) {
		Vector<String> argsVect = new Vector<String>();
		boolean hasHelpOption = false;
		int numOfOptions = 0, numOfFiles = 0;
		
		// get options
		String option;
		while ((option = findMatch(optionsRegex, argsStr)) != null) {
			
			argsStr = reduceString(option, argsStr);
			argsVect.add(option);
			numOfOptions ++;
			
			if (option.equals("-help"))
				hasHelpOption = true;
			
			if (optionsMap != null) {
				String extraType = optionsMap.get(option);
				if (extraType != null) {
					switch (extraType) {
						case "DELIM":
						case "DELIM OPTIONAL":
							String delim = getPatternOrDelimiter(argsStr);
							if (delim == null) {
								if (extraType.equals("DELIM"))
									return null;
							}
							else {
								argsVect.add(delim);
								argsStr = reduceString(delim, argsStr);
							}
							break;
						
						case "NUM":
							String numStr = getNum(argsStr);
							if (numStr == null)
								return null;
							else {
								argsVect.add(numStr);
								argsStr = reduceString(numStr, argsStr);
							}
							break;
							
						default:
							return null;
					}
				}
			}
		}
		
		if (maxOptions != -1 && numOfOptions > maxOptions)
			return null;
		
		if (argsStr.isEmpty()) {
			if (allowStdin) {
				argsVect.add("-");
				stdinRequired = true;
			}
			else if (hasHelpOption)
				return cleanUpArguments(argsVect);
			else
				return null;
		}
		
		else if (argsStr.equals("-") && allowStdin) {
			argsVect.add("-");
			stdinRequired = true;
		}
		
		else {
			String fileName;
			// get file names
			while ((fileName = findDirFile(argsStr, false)) != null){
				argsStr = reduceString(fileName, argsStr);
				argsVect.add(fileName);
				numOfFiles ++;
			}
			if (!argsStr.isEmpty() || numOfFiles < minFiles || (maxFiles != -1 && numOfFiles > maxFiles))
				return null;
		}
		
		return cleanUpArguments(argsVect);
	}
	
	/**
	 * Get arguments for cut command
	 * @param argsStr	string of arguments in a "cut" command
	 * @return			an array of valid arguments
	 */
	private String[] getCutArgs (String argsStr) {
		Vector<String> argsVect = new Vector<String>();
		boolean hasFOption = false, hasDOption = false, hasOption = false;
		
		// get options if any
		String option, delim = null;
		int delimIndex = -1;
		while ((option = findMatch("-(help|c|d|f)", argsStr)) != null) {
			hasOption = true;
			argsStr = reduceString(option, argsStr);
			argsVect.add(option);
			
			if (option.equals("-f")) {
				hasFOption = true;
			}
			else if (option.equals("-d")) {
				hasDOption = true;
				// check for delimeter
				delim = getPatternOrDelimiter(argsStr);
				if (delim == null)
					return null;
				else {
					delimIndex = argsVect.size();
					argsVect.add(delim);
					argsStr = reduceString(delim, argsStr);
				}	
			}
			
			if (option.matches("(-c|-f)")) {
				String numList = getNumList(argsStr);
				if (numList == null)
					return null;
				else {
					argsVect.add(numList);
					argsStr = reduceString(numList, argsStr);
				}
			}
		}
		
		if (!hasOption || (hasDOption && !hasFOption))
			return null;
		
		else if (argsStr.isEmpty() || argsStr.equals("-")) {
			argsVect.add("-");
			stdinRequired = true;
		}
		
		else {
			String fileName;
			// get file names
			while ((fileName = findDirFile(argsStr, false)) != null){
				argsStr = reduceString(fileName, argsStr);
				argsVect.add(fileName);
			}
			if (!argsStr.isEmpty())
				return null;
		}
		
		String[] args = cleanUpArguments(argsVect);
		// preserve quotes for -d delim
		if (delim != null && delim.startsWith("\"") && delim.endsWith("\""))
			args[delimIndex] = delim;
		
		return args;
		
	}
	
	/**
	 * Get the pattern/delimiter at the start of a string
	 * @param str	The string to search the pattern/delimiter from.
	 * 				If the pattern/delimiter has quotes, then spaces are allowed within the quote.
	 * @return		the pattern/delimiter from the beginning of the given string
	 * 				If "str" is an empty string or there is no pattern/delimiter at the beginning, 
	 * 				then return null
	 */
	private String getPatternOrDelimiter (String str) {
		String delim = "";
		
		if (str.isEmpty())
			return null;
		
		if (str.startsWith("\"")) {
			boolean isEndIndex = false;
			int endIndex = 0;
			while (!isEndIndex) {
				endIndex = str.indexOf("\"", endIndex+1);
				if (endIndex == -1)
					return null;
				// ensure it is not quotes that is part of the pattern (\")
				else if (str.charAt(endIndex-1) != '\\')
					isEndIndex = true;
			}
			delim = str.substring(0, endIndex+1);
		}
		else {
			delim = str.split("\\s+")[0];
			if (delim == null)
				return null;
		}
		
		if (delim.isEmpty())
			return null;
		else
			return delim;
	}
	
	/**
	 * Get the number at the start of a string.
	 * @param str	the string to search the number from
	 * @return		the number at the beginning of the string. 
	 * 				If it is an empty string or there is no number at the beginning, 
	 * 				then return null
	 */
	private String getNum (String str) {
		String numStr = "";
		if (str.isEmpty())
			return null;
		else {
			for (int i=0; i<str.length(); ++i) {
				char ch = str.charAt(i);
				if (Character.isDigit(ch))
					numStr += ch;
				else
					break;
			}
			if (numStr.isEmpty())
				return null;
		}
		
		return numStr;
	}
	
	/**
	 * Get the number at the start of a string.
	 * @param str	The string to search the list of numbers from.
	 * 				It can be of the form num, num-num, num-, -num
	 * @return		The list of numbers at the beginning of the string. 
	 * 				If it is an empty string or there is no number at the beginning, 
	 * 				then return null
	 */
	private String getNumList (String str) {
		final String regexNum = "(\\d+)";
		final String regexRange = "(\\d+\\s*-\\s*\\d+)";
		final String regexRangeStart = "(\\d+\\s*-)";
		final String regexRangeEnd = "(-\\s*\\d+)";
		final String regexNumList = "(("+regexNum+"|"+regexRange+"|"+regexRangeStart+"|"+regexRangeEnd+")\\s*,\\s*)*"
									+ "("+regexNum+"|"+regexRange+"|"+regexRangeStart+"|"+regexRangeEnd+")";
		
		
		String numList = findMatch (regexNumList, str); 
		
		if (numList == null)
			return null;
			
		// matching of regular expression may not care about the "-" in "num-"
		// to ensure that the next character in the string is a space and not part of the list of numbers
		str = str.substring(numList.length());
		while (!str.isEmpty()) {
			char ch = str.charAt(0);
			if (!Character.isWhitespace(ch)) {
				numList += ch;
				str = str.substring(1);
			}
			else
				break;
		}
		return numList;
	}
	
	/**
	 * Gets the first directory/file name found at the start of the given string 
	 * @param str		the string to search the directory or file from
	 * @param dirOnly	if true, only search for directory name else search for
	 * 					directory or file name
	 * @return			the directory/file name if it is valid at the start of the given string
	 * 					else return null
	 */
	private String findDirFile (String str, boolean dirOnly) {
		
		String matchedStr;
		if (dirOnly)
			matchedStr = findMatch(DIR_REGEX, str);
		
		else
			matchedStr = findMatch(DIR_FILE_REGEX, str);
		
		// matcher does not match last " if there is a / in the beginning
		// i.e "/dir1/dir2" will get "/dir/dir instead
		// check if matched string has " in front but " at the back
		if (matchedStr != null && matchedStr.charAt(0) == '\"' && matchedStr.charAt(matchedStr.length()-1) != '\"') {
			matchedStr += "\"";
		}
		
		return matchedStr;
		
	}
	
	/**
	 * Get a string that matches the regular expression for a specified string, starting from the beginning of the string.
	 * @param regex		the regular expression
	 * @param str		the string to check on
	 * @return			if there is a match, then the string that matched the regular expression. Else return null.
	 */
	private String findMatch (String regex, String str) {
		String matchedStr = null;
		String strTrim = str.trim();
		Matcher matcher = Pattern.compile("^"+regex).matcher(strTrim);
		if (matcher.find())
			matchedStr = matcher.group().trim();
		return matchedStr;
	}
	
	/**
	 * Clean up arguments by removing front and back quotes in arguments if any
	 * @param args		Vector of arguments
	 * @return			An array of arguments without front and back quotes
	 */
	private String[] cleanUpArguments(Vector<String> args) {
		String[] argsArray = new String[args.size()];
		
		for (int i=0; i<args.size(); ++i) {
			String argument = args.get(i);
			if (argument.startsWith("\"") && argument.endsWith("\""))
				argument = argument.substring(1,argument.length()-1);
			argsArray[i] = argument;
		}
		
		return argsArray;
	}
	
	/**
	 * Checks if a string has a particular content at the front of its string and removes it 
	 * 
	 * @param content				content to remove from the front of the string 'strToReduce'
	 * @param strToReduce			the string to be reduced
	 * @return						the reduced string if the content specified is found in front of the string, else return null
	 */
	private String reduceString(String content, String strToReduce) {
		if (content == null || strToReduce == null)
			return null;
		
		String reducedStrTrim = null;
		String reducedStr = strToReduce.trim();
		
		int index = reducedStr.indexOf(content);
		if (index != 0)
			return null;
		else {
			reducedStrTrim = reducedStr.substring(index+content.length()).trim();
		}
		
		return reducedStrTrim;
	}

	@Override
	public Runnable execute(ITool tool) {
		return new ToolRunnable(tool, stdIn);
	}

	@Override
	public void stop(Runnable toolExecution) {
		//TODO Implement
	}

	
	/**
	 * Do Forever
     * 1. Wait for a user input
     * 2. Parse the user input. Separate the command and its arguments
     * 3. Create a new thread to execute the command
     * 4. Execute the command and its arguments on the newly created thread. Exit with the status code of the executed command
     * 5. In the shell, wait for the thread to complete execution
     * 6. Report the exit status of the command to the user
	 */
	public static void main(String[] args){
		Shell shell = new Shell();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		ExecutorService executorSvc = Executors.newSingleThreadExecutor();
		Future<?> taskThread = null;
		
		try {
			while(true) {
				
				if (taskThread == null || taskThread.isDone())
					System.out.print(System.getProperty("user.dir") + " >> ");
				
				String input = br.readLine();
				
				if (input == null)
					continue;
				
				input = input.trim();
				if(input.isEmpty())
					continue;
				
				if (input.equals("Ctrl-Z")) {
					if (taskThread != null && !taskThread.isDone()) {
						taskThread.cancel(true);
						taskThread = null;
						System.out.println("Task Interrupted");
					}
				}
				
				else {
					if (taskThread == null || taskThread.isDone()) {
						ITool tool = shell.parse(input);
						boolean toExecuteTool = true;
						if (tool != null) {
							
							// get standard input if needed
							if (stdinRequired) {
								stdIn = "";
								while ((input = br.readLine()) != null) {
									// do not execute task
									if (input.equals("Ctrl-Z")) {
										toExecuteTool = false;
										System.out.println("Task Interrupted");
										break;
									}
									stdIn += input + System.lineSeparator();
								}
							}
							
							if (toExecuteTool) {
								Runnable runnable = shell.execute(tool);
								taskThread = executorSvc.submit(runnable);
							}
						}
						else {
							System.err.println("Invalid Command : " + input);
						}
					}
				}
			}
		} catch(IOException e) {
			System.err.println("Error: invalid input - " + e.getMessage());
		}
		
	}
	
	/**
	 * This method is only used for testing
	 * @param commandlines	commands to be executed sequentially
	 * @return 				The 1st element is the result string.
	 * 		   				The 2nd element onwards are the resulting 
	 * 						status code from the execution of each of the commands given
	 * 						If the commandline is invalid, the result string will be "Invalid Command"
	 * 						and the status code will be 1
	 */
	public static Vector<String> shellTestExecution(String ... commandlines) {
		underTest = true;
		
		Shell shell = new Shell();
		ExecutorService executorSvc = Executors.newSingleThreadExecutor();
		Vector<String> resultArray = new Vector<String> (commandlines.length + 1);
		
		for (int i = 0; i < commandlines.length; ++i) {
			ITool tool = shell.parse(commandlines[i]);
			if (tool == null) {
				resultArray.add("Invalid Command");
				return resultArray;
			}
			Runnable runnable = shell.execute(tool);
			Future<?> taskThread = executorSvc.submit(runnable);
			while (!taskThread.isDone()) {}
			resultArray.add(Integer.toString(tool.getStatusCode()));
		}
		resultArray.add(0,result);
		
		underTest = false;
		return resultArray;
	}
}

class ToolRunnable implements Runnable {
	
	private ITool tool;
	private String stdIn;
	
	ToolRunnable(ITool tool, String stdIn) {
		this.tool = tool;
		this.stdIn = stdIn;
	}
	
	@Override
	public void run() {

		File workingDir = new File (System.getProperty("user.dir"));
		String result = tool.execute(workingDir, stdIn);
		if (!result.isEmpty()) {
			if (!Shell.underTest)
				System.out.println(result);
			Shell.result = result;
		}
		else
			Shell.result = "";
		if (!Shell.underTest)
			System.out.print(System.getProperty("user.dir") + " >> ");
	
	}
}

