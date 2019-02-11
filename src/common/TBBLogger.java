package common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TBBLogger extends Logger {

	/**
	 * Static Error Logger to be used across all instances of the logger class for TBB
	 */
	public static TBBLogger errLogger = new TBBLogger("ERROR_LOG");
	
	private static final int fileSizeLimit = 104857600; //100MB file size limit for log file
	private String fileName;
	
	
	/**
	 * 
	 * @param name - A name for the logger. This should be a dot-separated name and should normally be based on the 
	 * 				 package name or class name of the subsystem, such as java.net or javax.swing
	 */
	private TBBLogger(String name) {
		super(name, null);
		this.fileName = "ERROR_LOG.txt";
		this.fileHandlerInit();
	}
	
	/**
	 * @param name - A name for the logger. This should be a dot-separated name and should normally be based on the 
	 * 				 package name or class name of the subsystem, such as java.net or javax.swing
	 * @param fileName - The filename associated with the log file, where all logs will be stored.
	 */
	public TBBLogger(String name, String fileName) {
		super(name, null);
		this.fileHandlerInit(fileName);
	}
	
	
	/**
	 * @return Returns the filename of the associated logger
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * Private method used internally for Static Error Logger
	 */
	private void fileHandlerInit() {
        
		FileHandler fileHandler = null;
		try 
        {  
			Path path = Paths.get(System.getProperty("user.dir") + File.separator + "logs");
			if (!Files.exists(path))
				Files.createDirectory(path);
            fileHandler = new FileHandler(
					System.getProperty("user.dir") + File.separator + "logs" + File.separator + this.fileName,
					fileSizeLimit, 1, true);
        } catch (SecurityException e) {
			e.printStackTrace();
			System.err.println("An error has occurred while creating the log files, please contact an administrator." 
			+ System.getProperty("line.separator") + "Error type: SecurityException.");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("An error has occurred while creating the log files, please contact an administrator."
					+ System.getProperty("line.separator") + "Error type: IOException.");
		} 
		
		SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);  
		this.addHandler(fileHandler);
        this.setUseParentHandlers(false);
	}
	
	
	/**
	 * Private method used to initialize fileHandler for any potential logger
	 */
	private void fileHandlerInit(String fileName) {
		
		FileHandler fileHandler = null;
		this.fileName = fileName;
		
		try {
			Path path = Paths.get(System.getProperty("user.dir") + File.separator + "logs");
			if (!Files.exists(path))
				Files.createDirectory(path);
			fileHandler = new FileHandler(
					System.getProperty("user.dir") + File.separator + "logs" + File.separator + fileName,
					fileSizeLimit, 1, true);
		} catch (SecurityException e) {
			e.printStackTrace();
			System.err.println("An error has occurred while creating the log files, please contact an administrator." 
			+ System.getProperty("line.separator") + "Error type: SecurityException.");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("An error has occurred while creating the log files, please contact an administrator."
					+ System.getProperty("line.separator") + "Error type: IOException.");
		}
		fileHandler.setFormatter(new Formatter() {
			private String format = "[%1$s] [%2$s] %3$s %n";
			private SimpleDateFormat dateWithMillis = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");

			@Override
			public String format(LogRecord record) {
				return String.format(format, dateWithMillis.format(new Date()), record.getSourceClassName(),
						formatMessage(record));
			}
		});
		this.addHandler(fileHandler);
		this.setUseParentHandlers(false);
	}

}
