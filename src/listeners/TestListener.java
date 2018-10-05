package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.logging.Level;

import authoring.GUI;
import commands.PlayerCommand;
import enamel.ScenarioParser;

/**
 * This class is used as an action listener whenever the "Export" button is
 * clicked. It allows the user to output the settings and commands used by the
 * user.Allowing the user to save a copy of the scenario they are creating such
 * as the number of Cells and Buttons and can define what the title of the
 * scenario should be.
 *
 * @author Dilshad Khatri, Alvis Koshy, Drew Noel, Jonathan Tung
 * @version 1.0
 * @since 2017-04-02
 */
public class TestListener implements ActionListener {

	private GUI gui;
	File scenarioFile;
	public static Thread playerThread;
	String newLine = System.getProperty("line.separator");
	/**
	 * Create an export listener with a reference to the parent GUI.
	 *
	 * @param gui
	 *            A reference to the parent GUI, needed in order to properly
	 *            access the command list
	 */
	public TestListener(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		gui.logger.log(Level.INFO, "User has clicked Test Scenario button.");
		gui.counterMap.put("Test", gui.counterMap.get("Test") + 1);
		System.out.println(gui.counterMap.toString());
		StringBuilder sb = new StringBuilder();
		// Build the file header first
		sb.append("Cell " + gui.getSettingsPanel().getCellField() + newLine);
		sb.append("Button " + gui.getSettingsPanel().getButtonField() + newLine);
		sb.append(gui.getSettingsPanel().getTitleField() + newLine + newLine);

		// Get the list of commands for export
		List<PlayerCommand> list = gui.getLeftPanel().getList();
		sb.append(parseCommands(list));

		// sb now contains the export file contents
//		JFileChooser save = new JFileChooser("SampleScenarios/");
//
//		FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("text files (*.txt)", "txt");
//		save.addChoosableFileFilter(txtFilter);
//		save.setFileFilter(txtFilter);
//
//		save.showSaveDialog(null);
//
//		// Check to see if any file was set
//		file = save.getSelectedFile();
//		if (file == null) {
//			return;
//		}
//
//		// Get the file and fix the extension if it's wrong
//		if (!FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("txt")) {
//			file = new File(file.toString() + ".txt");
//		}
		//if (file.exists()) {file.delete();}
		if (gui.loadedFile != null)
		{
			scenarioFile = new File(gui.loadedFile.getPath() + System.getProperty("file.separator") + "test.txt");
		}
		else
		{
		    scenarioFile = new File(System.getProperty("user.dir") + File.separator + "test.txt");
		}
		
		try {
			scenarioFile.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		scenarioFile.deleteOnExit();
		
//		File log = new File(System.getProperty("user.dir") + File.separator + "logs");
//		log.mkdirs();
		
		gui.upd();
		
		exportFile(scenarioFile, sb.toString());

		 playerThread = new Thread("Player Thread") {
		    public void run(){    
		        ScenarioParser s = new ScenarioParser();        
				s.setScenarioFile(scenarioFile.getAbsolutePath());
		    }
		};
		playerThread.start();
	}

	/**
	 * Parse a list of commands into serialized strings. Made private once
	 * testing is completed.
	 *
	 * @param list
	 *            Generic ordered list containing all the commands that should
	 *            be serialized
	 *
	 * @return String containing newline-separated serialized commands
	 */
	public String parseCommands(List<PlayerCommand> list) {
		StringBuilder sb = new StringBuilder();

		for (PlayerCommand pc : list) {
			sb.append(pc.serialize());
			sb.append(newLine);
		}

		return sb.toString();
	}

	private void exportFile(File file, String contents) {
		try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "8859_1"));) {
			out.append(contents);
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	



}
