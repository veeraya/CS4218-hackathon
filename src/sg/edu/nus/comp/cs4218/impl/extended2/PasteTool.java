package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class PasteTool extends ATool implements IPasteTool {
	public static final int ERR_CODE_INVALID_OPTION = 1;
	public static final String ERR_MSG_INVALID_OPTION = "paste: invalid option '%s'."
			+ System.lineSeparator()
			+ "Try `paste -help' for more information.";

	public static final int ERR_CODE_INVALID_OPTION_ARG = 2;
	public static final String ERR_MSG_INVALID_OPTION_ARG = "paste: option requires an argument -- '%s'."
			+ System.lineSeparator()
			+ "Try `paste -help' for more information.";

	public static final int ERR_CODE_FILE_NOT_EXIST = 3;
	public static final String ERR_MSG_FILE_NOT_EXIST = "paste: %s: No such file or directory";

	public static final int ERR_CODE_IO_EXCEPTION = 4;
	
	public static final String FILE_PATH_HELP = "help_files/paste_help";

	public PasteTool(String[] arguments) {
		super(arguments);
	}

	@Override
	public String pasteSerial(String[] input) {
		return pasteUseDelimiter("\t", input);
	}

	@Override
	public String pasteUseDelimiter(String delim, String[] input) {
		if (input == null) {
			return "";
		}

		if (delim == null) {
			// Default delimiter
			delim = "\t";
		}

		StringBuilder output = new StringBuilder();

		// Add the first element
		if (input[0] != null) {
			output.append(input[0]);
		}

		// backslash char is ignored by paste
		delim = delim.replace("\\", "");
		int delimIndex = 0;
		int delimLength = delim.length();

		for (int i = 1; i < input.length; i++) {
			if (input[i] == null) {
				continue;
			}

			// Add delimiter + input at n-th index
			if (delimLength > 0) {
				output.append(delim.charAt(delimIndex));
				delimIndex = (delimIndex + 1) % delimLength;
			}

			output.append(input[i]);
		}

		return output.toString();
	}

	@Override
	public String getHelp() {
		return readFile(FILE_PATH_HELP, Charset.forName("UTF-8"));
	}

	/**
	 * Options can appear anywhere. If multiple options appear, -help prevails,
	 * followed by the last -d option (if duplicate -d appears).
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		ArrayList<String> filePathList = new ArrayList<String>();

		// filesContent contains a ArrayList of lines from each file
		ArrayList<String[]> filesContent = new ArrayList<String[]>();

		boolean isGettingHelp = false;
		boolean isUsingSerial = false;
		boolean isUsingDelimiter = false;

		String delimiter = "\t";

		for (int i = 0; args != null && i < args.length; i++) {
			if (args[i].startsWith("-")) {
				switch (args[i]) {
				case "-s":
					isUsingSerial = true;
					break;

				case "-d":
					isUsingDelimiter = true;
					if (i + 1 >= args.length) {
						setStatusCode(ERR_CODE_INVALID_OPTION_ARG);
						return String.format(ERR_MSG_INVALID_OPTION_ARG,
								args[i]);
					}

					delimiter = args[i + 1];

					i += 1;
					break;

				case "-help":
					isGettingHelp = true;
					break;

				case "-":
					filePathList.add(args[i]);
					break;

				default:
					setStatusCode(ERR_CODE_INVALID_OPTION);
					return String.format(ERR_MSG_INVALID_OPTION, args[i]);
				}

			} else {
				filePathList.add(args[i]);
			}
		}

		if (isGettingHelp) {
			return getHelp();
		}

		if (filePathList.size() <= 0) {
			// No arguments, get from stdin
			filesContent.add(stdin.split("\\r?\\n"));
		}

		String[] filePaths = new String[filePathList.size()];
		filePaths = filePathList.toArray(filePaths);

		// Check that all file paths exist
		for (String filePath : filePaths) {
			if (filePath.equals("-")) {
				// Get from stdin
				continue;
			}

			File file = new File(filePath);
			if (!file.exists()) {
				setStatusCode(ERR_CODE_FILE_NOT_EXIST);
				return String.format(ERR_MSG_FILE_NOT_EXIST, filePath);
			}
		}

		try {
			filesContent.addAll(readFiles(filePaths, stdin));

		} catch (IOException e) {
			return e.getMessage();
		}

		StringBuilder output = new StringBuilder();
		if (isUsingSerial) {
			for (String[] fileContent : filesContent) {
				if (isUsingDelimiter) {
					output.append(pasteUseDelimiter(delimiter, fileContent));
				} else {
					output.append(pasteSerial(fileContent));
				}
				output.append(System.lineSeparator());
			}

		} else {
			int maxLineCount = getMaxLineCount(filesContent);
			for (int lineIndex = 0; lineIndex < maxLineCount; lineIndex++) {
				String[] contentFromSameLineNumber = getContentFromSameLineNumber(
						filesContent, lineIndex);
				output.append(pasteUseDelimiter(delimiter,
						contentFromSameLineNumber));
				output.append(System.lineSeparator());
			}
		}

		return output.toString().trim();
	}

	public static int getMaxLineCount(ArrayList<String[]> filesContent) {
		int maxLineCount = 0;

		if (filesContent == null) {
			return maxLineCount;
		}

		for (String[] fileContent : filesContent) {
			if (fileContent.length > maxLineCount) {
				maxLineCount = fileContent.length;
			}
		}
		return maxLineCount;
	}

	/**
	 * Read the contents of all files in the argument.
	 * 
	 * @param filePaths
	 *            The file paths to all the files.
	 * @return List of String array, which represent the content of each file.
	 * @throws IOException
	 */
	public ArrayList<String[]> readFiles(String[] filePaths, String stdin)
			throws IOException {
		ArrayList<String[]> filesContent = new ArrayList<String[]>();
		for (String filePath : filePaths) {
			if (filePath.equals("-")) {
				// Content from stdin
				filesContent.add(stdin.split("\\r?\\n"));
			} else {
				filesContent.add(readFile(filePath));
			}
		}
		return filesContent;
	}

	/**
	 * Get all lines from a file.
	 * 
	 * @param filePath
	 *            The location of the file.
	 * @return Array of String where each element represent a line from the
	 *         file.
	 * @throws IOException
	 */
	public String[] readFile(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		ArrayList<String> lines = new ArrayList<String>();

		String line = br.readLine();
		while (line != null) {
			lines.add(line);
			line = br.readLine();
		}
		br.close();

		return lines.toArray(new String[lines.size()]);
	}

	/**
	 * Get the content of the n-th line from each file into a String array.
	 * <p>
	 * If n > the max line no of a file, then the content n-th line content for
	 * that file will be "".
	 * 
	 * @param filesContent
	 *            ArrayList of String array, which represent the content of each
	 *            file
	 * @param lineIndex
	 *            0 based line number, must be >= 0
	 * @return
	 */
	public String[] getContentFromSameLineNumber(
			ArrayList<String[]> filesContent, int lineIndex) {
		if (filesContent == null || lineIndex < 0) {
			return null;
		}
		
		int fileCount = filesContent.size();
		String[] contentFromSameLineNumber = new String[fileCount];

		for (int i = 0; i < filesContent.size(); i++) {
			String[] fileContent = filesContent.get(i);

			if (lineIndex >= fileContent.length) {
				// Content for that file at lineIndex is empty.
				contentFromSameLineNumber[i] = "";
			} else {
				contentFromSameLineNumber[i] = fileContent[lineIndex];
			}

		}

		return contentFromSameLineNumber;
	}

	public void writeToFile(String path, String content) {
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(path), "UTF-8"));
			out.write(content);
			out.close();
		} catch (UnsupportedEncodingException e) {
			setStatusCode(ERR_CODE_IO_EXCEPTION);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			setStatusCode(ERR_CODE_IO_EXCEPTION);
			e.printStackTrace();
		} catch (IOException e) {
			setStatusCode(ERR_CODE_IO_EXCEPTION);
			e.printStackTrace();
		}
	}
	
	public String readFile(String path, Charset encoding) {
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			setStatusCode(ERR_CODE_IO_EXCEPTION);
			return e.getMessage();
		}
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}

}
