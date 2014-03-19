package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.extended2.IUniqTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class UniqTool extends ATool implements IUniqTool {
	public static final String FILE_PATH_HELP = "help_files/paste_help";

	public static final int ERR_CODE_FILE_NOT_FOUND = 1;
	public static final String ERR_MSG_FILE_NOT_FOUND = "uniq: %s: No such file or directory";

	public static final int ERR_CODE_INVALID_NUM_OF_FIELDS = 2;
	public static final String ERR_MSG_INVALID_NUM_OF_FIELDS = "uniq: %s: invalid number of fields to skip";

	public static final int ERR_CODE_EXTRA_OPERAND = 3;
	public static final String ERR_MSG_EXTRA_OPERAND = "uniq: extra operand %s";

	public static final int ERR_CODE_INVALID_OPTION = 4;
	public static final String ERR_MSG_INVALID_OPTION = "uniq: invalid option -- %s";

	public static final int ERR_CODE_MISSING_OPTION_ARG = 5;
	public static final String ERR_MSG_MISSING_OPTION_ARG = "uniq: option requires an argument -- %s";
	
	public static final int ERR_CODE_IO_EXCEPTION = 6;

	static Pattern p = Pattern.compile("[\t ]+");

	public UniqTool() {
		super(null);
	}
	
	public UniqTool(String[] arguments) {
		super(arguments);
	}

	@Override
	public String getUnique(boolean checkCase, String input) {
		if (input == null) {
			return "";
		}

		String[] lines = getLines(input);
		StringBuilder output = new StringBuilder();

		String prevLine = null;
		for (String line : lines) {

			if (prevLine != null) {
				if ((checkCase && line.equals(prevLine))
						|| (!checkCase && line.equalsIgnoreCase(prevLine))) {
					continue;
				}
			}

			output.append(line + System.lineSeparator());
			prevLine = line;
		}

		return output.toString().trim();
	}

	@Override
	public String getUniqueSkipNum(int NUM, boolean checkCase, String input) {
		if (NUM < 0) {
			setStatusCode(ERR_CODE_INVALID_NUM_OF_FIELDS);
			return String.format(ERR_MSG_INVALID_NUM_OF_FIELDS, NUM);
		}

		if (input == null) {
			return "";
		}

		String[] lines = getLines(input);
		StringBuilder output = new StringBuilder();

		String prevLine = null;
		for (String line : lines) {
			if (prevLine != null) {
				String prevLineSkippedFields = getStringAfterSkippingFields(
						prevLine, NUM);
				String curLineSkippedFields = getStringAfterSkippingFields(
						line, NUM);

				// Check if the fields at prevLine and curLine
				// are fewer than the number of skipped fields
				if (prevLineSkippedFields == null
						&& curLineSkippedFields == null) {
					continue;
				}

				// If prevLineSkippedFields == null but curLineSkippedFields !=
				// null
				// We are appending curLine to output

				// Check if prevLine and curLine are equal (after skipping
				// fields)
				if (prevLineSkippedFields != null
						&& curLineSkippedFields != null) {
					if ((!checkCase && prevLineSkippedFields
							.equalsIgnoreCase(curLineSkippedFields))
							|| (checkCase && prevLineSkippedFields
									.equals(curLineSkippedFields)))
						continue;
				}
			}

			output.append(line + System.lineSeparator());
			prevLine = line;
		}

		return output.toString().trim();
	}

	@Override
	public String getHelp() {
		StringBuilder output = new StringBuilder();

		try {
			File helpFile = new File(FILE_PATH_HELP);
			Scanner sc = new Scanner(helpFile);
			while (sc.hasNextLine()) {
				output.append(sc.nextLine());
				output.append(System.lineSeparator());
			}
			sc.close();
		} catch (IOException e) {
			return e.getMessage();
		}

		return output.toString();
	}

	/**
	 * -help prevails other options if multiple options
	 * are provided.
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		boolean checkCase = false;
		int skippedFieldCount = 0;
		ArrayList<String> operands = new ArrayList<String>();

		if (args == null) {
			return getUnique(checkCase, stdin);
		}

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			// Check for extra operands
			if (operands.size() >= 2) {
				setStatusCode(ERR_CODE_EXTRA_OPERAND);
				return String.format(ERR_MSG_EXTRA_OPERAND, arg);
			}

			// Check for valid file
			if (!arg.startsWith("-")) {
				File file = new File(arg);
				if (operands.size() == 0 && !file.exists()) {
					setStatusCode(ERR_CODE_FILE_NOT_FOUND);
					return String.format(ERR_MSG_FILE_NOT_FOUND, arg);
				}
				operands.add(arg);
				continue;
			}

			// Check for stdin argument
			if (arg.equals("-")) {
				operands.add(arg);
				continue;
			}

			// Option argument
			switch (arg) {
			case "-i":
				checkCase = true;
				break;
			case "-f":
				if (i + 1 >= args.length) {
					setStatusCode(ERR_CODE_MISSING_OPTION_ARG);
					return String.format(ERR_MSG_MISSING_OPTION_ARG, arg);
				}

				i += 1;
				arg = args[i];

				try {
					skippedFieldCount = Integer.parseInt(arg);
				} catch (NumberFormatException e) {
					setStatusCode(ERR_CODE_INVALID_NUM_OF_FIELDS);
					return String.format(ERR_MSG_INVALID_NUM_OF_FIELDS, arg);
				}
				break;
			case "-help":
				return getHelp();
			default:
				setStatusCode(ERR_CODE_INVALID_OPTION);
				return String.format(ERR_MSG_INVALID_OPTION, arg);
			}
		}

		String input;
		if (operands.size() == 0 || operands.get(0).equals("-")) {
			input = stdin;
		} else {
			input = readFile(operands.get(0), Charset.forName("UTF8"));
		}

		String output;
		if (skippedFieldCount == 0) {
			output = getUnique(checkCase, input);
		} else {
			output = getUniqueSkipNum(skippedFieldCount, checkCase, input);
		}

		// Check if the user specify an output file
		if (operands.size() > 1) {
			String outputFilePath = operands.get(1);

			if (!outputFilePath.equals("-")) {
				// Output is redirected to file
				writeToFile(outputFilePath, output);
				return "";
			}
		}

		return output;
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

	public String getStringAfterSkippingFields(String line,
			int fieldCount) {
		if (line == null) {
			return null;
		}
		
		if (fieldCount <= 0) {
			return line;
		}

		Matcher m = p.matcher(line);
		Boolean hasMoreMatches = true;

		while (fieldCount > 0) {
			if (!m.find()) {
				hasMoreMatches = false;
				break;
			}
			fieldCount -= 1;
		}

		// Check if line has fewer than fieldCount
		if (!hasMoreMatches) {
			return null;
		}

		return line.substring(m.end());
	}

	/**
	 * Convert the input into array of lines.
	 * <p>
	 * Information about the original new line is discarded. Thus, when
	 * generating the original string from the array, the new line will follow
	 * the new line format of the system running the program e.g. /r/n for
	 * Windows and /n for Unix
	 * 
	 * @param input
	 * @return Array of lines
	 */
	public String[] getLines(String input) {
		Scanner sc = new Scanner(input);
		ArrayList<String> lines = new ArrayList<>();
		while (sc.hasNextLine()) {
			lines.add(sc.nextLine());
		}
		sc.close();

		return lines.toArray(new String[lines.size()]);
	}

}
