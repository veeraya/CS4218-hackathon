package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Paths;

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CopyTool extends ATool implements ICopyTool {
	// Constants
	private final int FILE_ALREADY_EXISTS = 2;
	private final int FILE_NOT_FOUND = 1;
	private final int FILE_COPIED = 0;
	private final int MISSING_ARGS = -1;
	private final int MISSING_DEST = -2;
	private final int SAME_FILE = -3;
	private final int NOT_A_DIRECTORY = -4;

	// Variables
	private String fileNotFound;
	private String fileAlreadyExists;

	// Constructor
	/**
	 * Creates a new CopyTool instance.
	 * 
	 * @param arguments
	 *            This represents the arguments passed to the command for
	 *            execution.
	 */
	public CopyTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
		this.fileNotFound = "";
		this.fileAlreadyExists = "";
	}

	/**
	 * Copies the Source File/Directory (from) to its new Destination
	 * File/Directory (to) and sets the command's execution statusCode to 0 if
	 * the copy was successful.
	 * 
	 * If the Source File/Directory is not found, a FileNotFoundException would
	 * be thrown and the statusCode would be set to 1.
	 * 
	 * If the Destination File/Directory already exists, a
	 * FileAlreadyExistsException would be thrown and the statusCode would set
	 * to 2.
	 * 
	 * @param from
	 *            Source File/Directory.
	 * 
	 * @param to
	 *            Destination File/Directory.
	 * 
	 * @return true if the file/directory was copied successfully, else false.
	 * 
	 */
	@Override
	public boolean copy(File from, File to) {
		// TODO Auto-generated method stub
		try {
			if (from.exists()) {
				if (to.isDirectory()) {
					final File newFile = new File(to.toString() + "/"
							+ from.getName());
					newFile.createNewFile();
					writeToDest(from, newFile);
				} else {
					if (!to.exists()) {
						to.createNewFile();
						writeToDest(from, to);
					} else {
						throw new FileAlreadyExistsException(null);
					}
				}
			} else {
				throw new FileNotFoundException();
			}

			return true;

		} catch (FileAlreadyExistsException fExists) {
			setStatusCode(FILE_ALREADY_EXISTS);
			fileAlreadyExists = Paths.get(to.getName().toString())
					.getFileName().toString();
		} catch (FileNotFoundException fNotFound) {
			setStatusCode(FILE_NOT_FOUND);
			fileNotFound = Paths.get(from.getName().toString()).getFileName()
					.toString();
		} catch (IOException ioE) {
			ioE.printStackTrace();
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
				returnMessage = copyMultipleFiles(workingDir);
				return returnMessage.trim();
			} else {
				setStatusCode(NOT_A_DIRECTORY);
			}
		} else if (args[0].equals(args[1])) {
			setStatusCode(SAME_FILE);
		} else {
			copy(createFileObject(args[0], workingDir),
					createFileObject(args[1], workingDir));
		}

		return getReturnMessage(getStatusCode()).trim();
	}

	/**
	 * Method to copy the contents of a file/directory to its designated
	 * location.
	 * 
	 * @param from
	 *            Source File/Directory.
	 * @param to
	 *            Destination File/Directory.
	 */
	private void writeToDest(File from, File to) throws IOException {

		final FileInputStream fIs = new FileInputStream(from);
		final FileOutputStream fOs = new FileOutputStream(to);

		final byte[] data = new byte[2048];

		while (fIs.read(data) != -1) {
			fOs.write(data, 0, data.length);
		}

		fIs.close();
		fOs.close();

		setStatusCode(FILE_COPIED);

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
		case FILE_COPIED:
			return System.lineSeparator();
		case FILE_NOT_FOUND:
			return "copy: '" + this.fileNotFound
					+ "': No such file or directory" + System.lineSeparator();
		case FILE_ALREADY_EXISTS:
			return "copy: '" + this.fileAlreadyExists
					+ "': File already exists";
		case NOT_A_DIRECTORY:
			return "copy: target '" + args[args.length - 1]
					+ "' is not a directory";
		case MISSING_ARGS:
			return "copy: missing file operand";
		case MISSING_DEST:
			return "copy: missing destination file operand after '" + args[0]
					+ "'";
		case SAME_FILE:
			return "copy: '" + args[0] + "' and '" + args[1]
					+ "' are the same file";
		default:
			break;
		}

		return null;
	}

	/**
	 * Method to copy multiple files to a directory. For every invocation of the
	 * move method, the appropriate return message would be concatenated.
	 * 
	 * @param workingDir
	 *            The current working directory path.
	 * 
	 * @return the concatenated return message.
	 * 
	 */
	public String copyMultipleFiles(File workingDir) {

		StringBuilder sb = new StringBuilder();
		int start = 0;
		final int end = args.length - 2;

		while (start <= end) {		
					
			copy(createFileObject(args[start], workingDir),
					createFileObject(args[args.length - 1], workingDir));
			sb.append(getReturnMessage(getStatusCode()));
			start++;
		}	
		
		return sb.toString();

	}
}
