package sg.edu.nus.comp.cs4218.impl.extended1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.extended1.IGrepTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class GrepTool extends ATool implements IGrepTool {
	public static final int ERR_CODE_INVALID_ARG = 1;
	public static final String ERR_MSG_INVALID_ARG = "Error: Invalid arguments. See grep -help."
			+ System.lineSeparator();
	public static final String ERR_MSG_INVALID_CONTEXT_ARG = "grep: %s: invalid context length argument";

	public static final int ERR_CODE_IO = 2;
	public static final String ERR_MSG_IO = "Error: Reading or writing problem. Please retry and check that file path is correct"
			+ System.lineSeparator();

	public static final int ERR_CODE_INVALID_REGEX = 3;
	public static final String ERR_MSG_INVALID_REGEX = "Error: Regex syntax is incorrect. Please retry."
			+ System.lineSeparator();

	public static final String MATCHES_SEPARATOR = "--"
			+ System.lineSeparator();

	public static final String FILE_PATH_HELP = "help_files/grep_help";

	public static Pattern pattern;

	public GrepTool() {
		super(null);
	}
	
	public GrepTool(String[] arguments) {
		super(arguments);
	}

	@Override
	public int getCountOfMatchingLines(String pattern, String input) {
		String[] lines = getLines(input);
		Integer[] matchingIndexes = getMatchingIndexes(lines, pattern);
		return matchingIndexes.length;
	}

	/**
	 * The output contains line separator that follows the system's line
	 * separator, which may be different from the line separator format in the
	 * original input file.
	 */
	@Override
	public String getOnlyMatchingLines(String pattern, String input) {
		String[] lines = getLines(input);
		Integer[] matchingIndexes = getMatchingIndexes(lines, pattern);

		StringBuilder output = new StringBuilder();
		for (int index : matchingIndexes) {
			output.append(lines[index]);
			output.append(System.lineSeparator());
		}

		return output.toString();
	}

	@Override
	public String getMatchingLinesWithTrailingContext(int option_A,
			String pattern, String input) {
		String[] lines = getLines(input);
		Integer[] matchingIndexes = getMatchingIndexes(lines, pattern);

		StringBuilder output = new StringBuilder();
		int prevMaxIndex = -1;
		for (int index : matchingIndexes) {
			// Check if the current match has at least one line vertical margin
			// with the previous match.
			if (prevMaxIndex > -1 && index - prevMaxIndex > 0) {
				output.append(MATCHES_SEPARATOR);
			}

			// Prevent index out of bounds.
			int maxIndex = Math.min(index + option_A, lines.length - 1);
			while (index <= maxIndex) {
				if (index > prevMaxIndex) {
					output.append(lines[index]);
					output.append(System.lineSeparator());
				}
				index++;
			}
			prevMaxIndex = maxIndex;
		}

		return output.toString();
	}

	@Override
	public String getMatchingLinesWithLeadingContext(int option_B,
			String pattern, String input) {
		String[] lines = getLines(input);
		Integer[] matchingIndexes = getMatchingIndexes(lines, pattern);

		StringBuilder output = new StringBuilder();
		int prevIndex = -1;
		for (int index : matchingIndexes) {
			int minIndex = Math.max(index - option_B, 0);

			if (prevIndex > -1 && minIndex - prevIndex > 0) {
				output.append(MATCHES_SEPARATOR);
			}

			while (minIndex <= index) {
				if (minIndex > prevIndex) {
					output.append(lines[minIndex]);
					output.append(System.lineSeparator());
				}
				minIndex++;
			}

			prevIndex = index;
		}

		return output.toString();
	}

	@Override
	public String getMatchingLinesWithOutputContext(int option_C,
			String pattern, String input) {
		String[] lines = getLines(input);
		Integer[] matchingIndexes = getMatchingIndexes(lines, pattern);

		StringBuilder output = new StringBuilder();
		int prevMaxIndex = -1;

		for (int i = 0; i < matchingIndexes.length; i++) {
			int matchingIndex = matchingIndexes[i];

			// Append '--' if the first line in block of lines in context is
			// one more line than prevMaxIndex
			if (prevMaxIndex > -1) { // Make sure it's not the first match
				int smallestIndexInContext = matchingIndex - option_C;
				if (smallestIndexInContext - prevMaxIndex > 1) {
					output.append("--");
					output.append(System.lineSeparator());
				}
			}

			int currentIndexInContext = matchingIndex - option_C;
			while (currentIndexInContext <= matchingIndex + option_C
					&& currentIndexInContext < lines.length) {

				if (currentIndexInContext <= prevMaxIndex
						|| currentIndexInContext < 0) {
					currentIndexInContext++;
					continue;
				}

				output.append(lines[currentIndexInContext]);
				output.append(System.lineSeparator());

				currentIndexInContext++;
			}

			prevMaxIndex = matchingIndex + option_C;
		}

		return output.toString();
	}

	@Override
	public String getMatchingLinesOnlyMatchingPart(String pattern, String input) {
		String[] lines = getLines(input);
		Integer[] matchingIndexes = getMatchingIndexes(lines, pattern);
		GrepTool.pattern = Pattern.compile(pattern);

		StringBuilder output = new StringBuilder();
		for (int index : matchingIndexes) {
			String matchingSentence = lines[index];
			Matcher matcher = GrepTool.pattern.matcher(matchingSentence);

			while (matcher.find()) {
				String matchingPart = matcher.group();
				output.append(matchingPart);
				output.append(System.lineSeparator());
			}
		}

		return output.toString();
	}
	
	@Override
	public String getNonMatchingLines(String pattern, String input) {
		String[] lines = getLines(input);
		Integer[] matchingIndexes = getMatchingIndexes(lines, pattern);
		StringBuilder output = new StringBuilder();

		/**
		 * i is the iterating index for all the lines j is the iterating index
		 * for matching lines
		 */
		for (int i = 0, j = 0; i < lines.length; i++) {
			// Check if current index is a matching index
			if (j < matchingIndexes.length && i == matchingIndexes[j]) {
				j++;
				continue;
			}
			output.append(lines[i]);
			output.append(System.lineSeparator());
		}

		return output.toString();
	}

	@Override
	public String getHelp() {
		String output = "";

		try {
			output = new String(Files.readAllBytes(new File(FILE_PATH_HELP).toPath()));
		} catch (IOException e) {
			setStatusCode(ERR_CODE_IO);
			return ERR_MSG_IO;
		}

		return output;
	}

	/**
	 * Assumptions: [options] always comes before PATTERN and [FILE...] Only
	 * allow one option Only allow one '-'. Behaviour is undefined otherwise.
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		if (args.length < 1) {
			setStatusCode(ERR_CODE_INVALID_ARG);
			return ERR_MSG_INVALID_ARG;
		}

		StringBuilder output = new StringBuilder();

		try {
			boolean hasMultipleFiles = false;
			if (!hasOption(args)) {
				String pattern = args[0];

				if (args.length < 2) {
					if (stdin == null) {
						setStatusCode(ERR_CODE_INVALID_ARG);
						return ERR_MSG_INVALID_ARG;
					}

					output.append(getOnlyMatchingLines(pattern, stdin));
				}

				for (int i = 1; i < args.length; i++) {
					if (args.length > 2) {
						hasMultipleFiles = true;
					}
					String input = readFile(args[i], stdin, "UTF8", hasMultipleFiles);
					output.append(getOnlyMatchingLines(pattern, input));
				}
			} else {
				int lineCount;
				String pattern;

				String option = args[0];
				switch (option) {
				case "-A":
					try {
						lineCount = Integer.parseInt(args[1]);
						pattern = args[2];
						// Check if there is [FILES] arg
						if (args.length < 4) {
							output.append(getMatchingLinesWithTrailingContext(
									lineCount, pattern, stdin));
						}

						// [FILES] arg present
						for (int i = 3; i < args.length; i++) {
							if (args.length > 4) {
								hasMultipleFiles = true;
							}
							String input = readFile(args[i], stdin, "UTF8", hasMultipleFiles);
							output.append(getMatchingLinesWithTrailingContext(
									lineCount, pattern, input));
						}
					} catch (NumberFormatException e) {
						setStatusCode(ERR_CODE_INVALID_ARG);
						return String.format(ERR_MSG_INVALID_CONTEXT_ARG,
								args[1]);
					}
					break;

				case "-B":
					try {
						lineCount = Integer.parseInt(args[1]);
						pattern = args[2];
						if (args.length < 4) {
							output.append(getMatchingLinesWithLeadingContext(
									lineCount, pattern, stdin));
						}

						for (int i = 3; i < args.length; i++) {
							if (args.length > 4) {
								hasMultipleFiles = true;
							}
							String input = readFile(args[i], stdin, "UTF8", hasMultipleFiles);
							output.append(getMatchingLinesWithLeadingContext(
									lineCount, pattern, input));
						}
					} catch (NumberFormatException e) {
						setStatusCode(ERR_CODE_INVALID_ARG);
						return String.format(ERR_MSG_INVALID_CONTEXT_ARG,
								args[1]);
					}
					break;

				case "-C":
					try {
						lineCount = Integer.valueOf(args[1]);
						pattern = args[2];
						if (args.length < 4) {
							output.append(getMatchingLinesWithOutputContext(
									lineCount, pattern, stdin));
						}

						for (int i = 3; i < args.length; i++) {
							if (args.length > 4) {
								hasMultipleFiles = true;
							}
							String input = readFile(args[i], stdin, "UTF8", hasMultipleFiles);
							output.append(getMatchingLinesWithOutputContext(
									lineCount, pattern, input));
						}
					} catch (NumberFormatException e) {
						setStatusCode(ERR_CODE_INVALID_ARG);
						return String.format(ERR_MSG_INVALID_CONTEXT_ARG,
								args[1]);
					}
					break;

				case "-c":
					pattern = args[1];
					if (args.length < 3) {
						output.append(getCountOfMatchingLines(pattern, stdin));
						output.append(System.lineSeparator());
					}

					for (int i = 2; i < args.length; i++) {
						if (args.length > 3) {
							hasMultipleFiles = true;
						}
						String input = readFile(args[i], stdin, "UTF8", hasMultipleFiles);
						output.append(getCountOfMatchingLines(pattern, input));
						output.append(System.lineSeparator());
					}
					break;

				case "-o":
					pattern = args[1];
					if (args.length < 3) {
						output.append(getMatchingLinesOnlyMatchingPart(pattern,
								stdin));
						output.append(System.lineSeparator());
					}

					for (int i = 2; i < args.length; i++) {
						if (args.length > 3) {
							hasMultipleFiles = true;
						}
						String input = readFile(args[i], stdin, "UTF8", hasMultipleFiles);
						output.append(getMatchingLinesOnlyMatchingPart(pattern,
								input));
					}
					break;

				case "-v":
					pattern = args[1];
					if (args.length < 3) {
						output.append(getNonMatchingLines(pattern, stdin));
					}

					for (int i = 2; i < args.length; i++) {
						if (args.length > 3) {
							hasMultipleFiles = true;
						}
						String input = readFile(args[i], stdin, "UTF8", hasMultipleFiles);
						output.append(getNonMatchingLines(pattern, input));
					}
					break;

				case "-help":
					// If it's help option, ignore the rest
					return getHelp();

				default:
					setStatusCode(ERR_CODE_INVALID_ARG);
					return ERR_MSG_INVALID_ARG;
				}
			}
		} catch (IOException e) {
			setStatusCode(ERR_CODE_IO);
			return ERR_MSG_IO;
		}

		return output.toString();
	}

	public boolean hasOption(String[] args) {
		return args[0].startsWith("-") && args[0].length() > 1;
	}

	public Integer[] getMatchingIndexes(String[] lines, String regex) {
		pattern = Pattern.compile(regex);
		ArrayList<Integer> matchingLines = new ArrayList<>();
		Matcher matcher = null;

		int index = 0;
		for (String line : lines) {
			// Set matcher to match new line of text
			if (matcher != null) {
				matcher.reset(line);
			} else {
				matcher = pattern.matcher(line);
			}

			if (matcher.find()) {
				matchingLines.add(index);
			}

			index++;
		}
		return matchingLines.toArray(new Integer[matchingLines.size()]); 
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
	public static String[] getLines(String input) {
		Scanner sc = new Scanner(input);
		ArrayList<String> lines = new ArrayList<>();
		while (sc.hasNextLine()) {
			lines.add(sc.nextLine());
		}
		sc.close();

		return lines.toArray(new String[lines.size()]);
	}

	public String readFile(String path, String stdin, String encodingName, boolean hasMultipleFiles)
			throws IOException {
		if (path.equals("-")) {
			if (hasMultipleFiles) {
				return "(standard input):" + stdin;
			} else {
				return stdin;
			}
		}

		File file = new File(path);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), encodingName));
		StringBuilder fileContent = new StringBuilder();
		String line = in.readLine();

		
		
		while (line != null) {
			if (hasMultipleFiles) {
				fileContent.append(path + ":");
			}
			fileContent.append(line + System.lineSeparator());
			line = in.readLine();
		}
		in.close();

		return fileContent.toString();
	}
}
