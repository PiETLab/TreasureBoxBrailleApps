package authoring;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


// This class is created to reuse the code of record audio. This method is used in 3 classes, and all of these 3 classes call this class to use this method.

public class RecordAudio {
	private boolean isRecording = false;
	private boolean noRecording = true;
	public boolean recordFlag= false;
	private GUI gui;
	ThreadRunnable thread = null;
	public File file = null;

	public void recordAudio(GUI gui)
	{
		this.gui=gui;
		JDialog recordDialog = new JDialog(gui, "Record Audio");
		recordDialog.setModal(true);
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		recordDialog.setSize(200, 180);
		recordDialog.setResizable(false);
		recordDialog.setLocationRelativeTo(gui);
	//	JLabel label = new JLabel("Press Record button to start recording, Stop button to stop and save, and Cancel button to canel recording");
		JButton recordButton = new JButton("Start Recording");
		recordButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				gui.logger.log(Level.INFO, "Recording Started");
				isRecording=true;
				noRecording = false;
				recordButton.setForeground(Color.RED);
				recordButton.setText("Recording...");
				thread = new ThreadRunnable();
				thread.start();	
				
				
			}
		});
		
		JButton stopButton = new JButton("Stop Recording");
		stopButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(isRecording==true){
				gui.logger.log(Level.INFO, "Recording Stopped");
				isRecording=false;
				recordButton.setForeground(Color.BLACK);
				recordButton.setText("Start Recording");
				file = thread.stopRecording();
				//recordDialog.setVisible(false);	
				
				
				if(noRecording)
					recordDialog.setVisible(false);
				else
					{
						gui.logger.log(Level.INFO, "Recording Done");
						isRecording=false;
						thread.cancel();
						recordFlag=true;
						recordDialog.setVisible(false);	
					}
				
				
				}
			}	
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(noRecording)
					recordDialog.setVisible(false);
				else
					{
						gui.logger.log(Level.INFO, "Recording Cancelled");
						isRecording=false;
						thread.cancel();
						recordFlag=false;
						recordDialog.setVisible(false);	
					}
				}
				
		});
		
		
	/*	JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(noRecording)
					recordDialog.setVisible(false);
				else
					{
						gui.logger.log(Level.INFO, "Recording Done");
						isRecording=false;
						thread.cancel();
						recordFlag=true;
						recordDialog.setVisible(false);	
					}
				}
				
		});*/
				
		recordDialog.setLayout(new BorderLayout());
		panel.add(recordButton);
		panel.add(stopButton);
		//panel.add(okButton);
		panel.add(cancelButton);
		recordDialog.add(panel, BorderLayout.CENTER);
		recordDialog.setVisible(true);		
	}

	
	
}
