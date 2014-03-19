package sg.edu.nus.comp.cs4218.extended2;

import sg.edu.nus.comp.cs4218.ITool;

/**
 * Do not modify this file
 */
/*
 * 
 * paste : writes to standard output lines consisting of sequentially corresponding 
 * lines of each given file, separated by a TAB character
 * 
 * Command Format - paste [OPTIONS] [FILE]
 * 		FILE - Name of the file, when no file is present (denoted by "-") use standard input OPTIONS
 * 		-s : paste one file at a time instead of in parallel
 * 		-d DELIM: Use characters from the DELIM instead of TAB character
 * 		-help : Brief information about supported options
 */

public interface IPasteTool extends ITool{
	/**
	 * Join all the String in input together with tab delimiter.
	 * @param input Array of strings to be joined.
	 * @return The combined string.
	 */
	String pasteSerial(String[] input);
	
	/**
	 * Join all the String in input together with the characters in delim.
	 * 
	 * @param delim
	 * @param input
	 * @return
	 */
	String pasteUseDelimiter(String delim, String[] input);
	
	/**
	 * Get the help message.
	 * @return The help message.
	 */
	String getHelp();
}
