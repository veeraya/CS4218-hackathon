package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.io.FileNotFoundException;

import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class MoveTool extends ATool implements IMoveTool {
	// Constants
	private final int FILE_NOT_FOUND = 1;
	private final int FILE_MOVED = 0;
	private final int MISSING_ARGS = -1;
	private final int MISSING_DEST = -2;
	private final int NOT_A_DIRECTORY = -3;

	// Variables
	private String fileNotFound;;

	// Constructor
	/**
	 * Creates a new MoveTool instance.
	 * 
	 * @param arguments
	 *            This represents the arguments passed to the command for
	 *            execution.
	 */
	public MoveTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
		this.fileNotFound = "";
	}

	/**
	 * Moves/Renames the Source File/Directory (from) to its new Destination
	 * File/Directory (to) and sets the command's execution statusCode to 0 if
	 * the move/rename was successful.
	 * 
	 * Else, a FileNotFoundException would be thrown if the Source
	 * File/Directory is not found and the statusCode would be set to 1.
	 * 
	 * @param from
	 *            Source File/Directory.
	 * 
	 * @param to
	 *            Destination File/Directory.
	 * 
	 * @return true if the file/directory was moved/renamed successfully, else
	 *         false.
	 * 
	 */
	@Override
	public boolean move(File from, File to) {
		// TODO Auto-generated method stub

		try {
			if (from.exists()) {				
				from.renameTo(new File(to.toString()));	
				
				setStatusCode(FILE_MOVED);
				return true;
				
				
			} else {
				throw new FileNotFoundException();
			}
		} catch (FileNotFoundException fNotFound) {
			this.fileNotFound = from.getName();
			setStatusCode(FILE_NOT_FOUND);
		}

		return false;
	}

	/**
	 * Executes the tool with the arguments specified in the constructor.
	 * 
	 * @param workingDir
	 *            The current working directory path.
	 * 
	 * @param stdin
	 *            The default input that the user enters if "-" was specified as
	 *            the sole argument in the command's argument.
	 * 
	 * @return The appropriate message to be displayed on the shell. An empty
	 *         message would be returned if the executed command returns no
	 *         errors.
	 * 
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		if (args == null || args.length == 0) {
			setStatusCode(MISSING_ARGS);
		} else if (args.length == 1) {
			setStatusCode(MISSING_DEST);
		} else if (args.length > 2) {
			String returnMessage = "";
			if (isDirectory(args[args.length - 1])) {
				returnMessage = moveMultipleFiles(workingDir);
				return returnMessage.trim();
			} else {
				setStatusCode(NOT_A_DIRECTORY);
			}
		} else {
			move(createFileObject(args[0], workingDir),
					createFileObject(args[1], workingDir));
		}

		return getReturnMessage(getStatusCode()).trim();
	}

	/**
	 * Checks the current statusCode of the executed command and returns the
	 * appropriate message.
	 * 
	 * @return The appropriate message to be displayed on the shell. Null would
	 *         be returned if the statusCode is not found within any of the
	 *         cases.
	 * 
	 */
	public String getReturnMessage(int statusCode) {

		switch (statusCode) {
		case FILE_MOVED:
			return System.lineSeparator();
		case MISSING_ARGS:
			return "move: missing file operand";
		case MISSING_DEST:
			return "move: missing destination file operand after '" + args[0]
					+ "'";
		case FILE_NOT_FOUND:
			return "move: '" + this.fileNotFound
					+ "': No such file or directory" + System.lineSeparator();
		case NOT_A_DIRECTORY:
			return "move: target '" + args[args.length - 1]
					+ "' is not a directory";
		default:
			break;
		}

		return null;

	}

	/**
	 * Creates a file object to passed to the move method. If the file specified
	 * in the parameter (argument) exists within the current working directory,
	 * the argument would be concatenated with the current working directory's
	 * path, else just the argument would be used to create the file object.
	 * 
	 * @param argument
	 *            The file/directory path
	 * 
	 * @param workingDir
	 *            The current working directory path
	 * 
	 * @return a file object pointing to the specified path.
	 * 
	 */
	public File createFileObject(String argument, File workingDir) {

		File fileObj = null;
		final boolean fileInWorkingDir = new File(workingDir, argument)
				.exists();

		if (!fileInWorkingDir) {
			fileObj = new File(argument);
		} else {
			fileObj = new File(workingDir.toString() + File.separator + argument);
		}

		return fileObj;
	}

	/**
	 * Checks if the argument specified is a directory.
	 * 
	 * @param The
	 *            file/directory path.
	 * 
	 * @return true if the argument is a directory, else false.
	 * 
	 */
	public boolean isDirectory(String argument) {

		final File dir = new File(argument);

		if (dir.isDirectory()) {
			return true;
		}

		return false;
	}

	/**
	 * Method to move multiple files to a directory. For every invocation of the
	 * move method, the appropriate return message would be concatenated.
	 * 
	 * @param workingDir
	 *            The current working directory path.
	 * 
	 * @return the concatenated return message.
	 * 
	 */
	public String moveMultipleFiles(File workingDir) {

		StringBuilder sb = new StringBuilder();

		int start = 0;
		final int end = args.length - 2;

		while (start <= end) {
			move(createFileObject(args[start], workingDir),
					createFileObject(args[args.length - 1], workingDir));
			sb.append(getReturnMessage(getStatusCode()));
			start++;
		}

		return sb.toString();

	}
}