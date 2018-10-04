package commands;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import authoring.GUI;
import authoring.RecordAudio;

/**
 * Command wrapper to represent the /~sound command in the player. Values are
 * given as a full path to the file to be played.
 *
 * @author Dilshad Khatri, Alvis Koshy, Drew Noel, Jonathan Tung
 * @version 1.0
 * @since 2017-04-01
 */
public class SoundCommand implements PlayerCommand {

	private String file = "";
	private JFrame frame;
	private GUI gui;
	/**
	 * Create a new instance with the file path pre-set
	 *
	 * @param file
	 *            Path to the file to play
	 */
	public SoundCommand(String file, GUI gui) {
		this.file = file;
		this.gui= gui;
	}

	@Override
	public String toString() {
		return "Play Sound: " + file;
	}

	@Override
	public String serialize() {
		return "/~sound:" + file;
	}

	@Override
	public String getEditLabel() {
		return "File path: ";
	}

	@Override
	public String getCurrentValue() {
		return file;
	}

	@Override
	public void setCurrentValue(String file) {
		this.file = file;
	}

	@Override
	public void editCommand() {
	
	
		
		frame = new JFrame("Edit Sound Command");
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 16, 16));
		frame.setPreferredSize(new Dimension(370,100));
		frame.setMaximumSize(new Dimension(450,100));
		frame.setMinimumSize(new Dimension(450,100));
		frame.setResizable(false);
		frame.pack();
		
		JButton btnNewButton = new JButton("Re-record Audio");
		frame.getContentPane().add(btnNewButton);
		
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RecordAudio rc= new RecordAudio(); 
				rc.recordAudio(gui); // method in RecordAudio class
				
				if (rc.file != null && rc.recordFlag==true)
				{
					
					file= rc.file.toString();
					
				}
				frame.dispose();
			}
		});
		
		JButton btnNewButton_1 = new JButton("Re-select Audio");
		frame.getContentPane().add(btnNewButton_1);
		
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File Nfile = null;
				JFileChooser load = new JFileChooser();
				FileNameExtensionFilter wavFileFilter = new FileNameExtensionFilter("wav files (*.wav)", "wav");
				load.addChoosableFileFilter(wavFileFilter);
				load.setFileFilter(wavFileFilter);				
				load.showOpenDialog(null);
				Nfile = load.getSelectedFile();
				if (Nfile != null)
				{
					file=Nfile.toString();
				}
				frame.dispose();
			}
		});
		
		frame.setVisible(true);

	}
	
}
