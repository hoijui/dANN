/******************************************************************************
 *                                                                             *
 *  Copyright: (c)                                    *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by syncleus at http://www.syncleus.com.  *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact syncleus at the information below if you cannot find   *
 *  a license:                                                                 *
 *                                                                             *
 *  Syncleus                                                                   *
 *  1116 McClellan St.                                                         *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/


package com.syncleus.core.dann.examples.nci.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.syncleus.core.dann.examples.nci.NciBrain;

/**
 * A graphical user interface based on java swing
 * for the nci example application of dANN.
 * -> "nci" stands for "Neural Compressed Image".
 * 
 * This class implements the main Window of the gui.
 * 
 * <!-- Author: chickenf -->
 * @author chickenf
 */

public class MainWindow extends JFrame implements ActionListener {
	
	////////////////
	// CONFIG START
	final String JFRAME_TITLE = "dANN-nci - Image Compression";
//	final int JFRAME_WIDTH = 800;
//	final int JFRAME_HEIGHT = 600;
	final String ICON_PATH = "./icons/";
	// CONFIG END
	////////////////
	
	private JPanel myHeader;
	private JPanel myFileSelector;
	private JPanel myOptionSelector;
	private JPanel myStatusReporter;
	private JPanel myActionSelector;
	
	private JComboBox selectFileBox;
	private JButton selectFileButton;
	private File selectedFile;
	
	private JButton runButton;
	private JButton trainButton;
	private JButton quitButton;
	private String myStatusText;
	private JLabel myStatusLabel;
	private JPanel myStatusPanel;
	private int applicationStatus;
	private ImageIcon myFileOpenIcon;
	private ImageIcon myRunIcon;
	private ImageIcon myTrainIcon;
	private ImageIcon myQuitIcon;
	private JButton trainConfirmButton;
	private JButton trainCancelButton;
	
	
	public MainWindow() {

		// some basic JFrame settings
		setTitle(JFRAME_TITLE);
//		setSize(new Dimension(JFRAME_WIDTH, JFRAME_HEIGHT));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new GridBagLayout());
		
		// create and add the content of the JFrame:
		// four JPanels
		myHeader = createHeader();
		
		myFileSelector = createFileSelector();
		
		myOptionSelector = createOptionSelector();

		myStatusReporter = createStatusReporter();
		
		myActionSelector = createActionSelector();

		GridBagConstraints gbc = new GridBagConstraints();
		
		// add insets for a less densely packed gui
//		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.fill = GridBagConstraints.BOTH;

		// header on top, full width
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		getContentPane().add(myHeader, gbc);

		// file selector on the middle left
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.ipadx = 1;
		getContentPane().add(myFileSelector, gbc);

		// options on the middle right
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;		
		getContentPane().add(myOptionSelector, gbc);

		// status on the bottom left
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		getContentPane().add(myStatusReporter, gbc);
		
		// actions on the bottom right
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		getContentPane().add(myActionSelector, gbc);

		// optimize the window size
		pack();
		
		// fix the window size for now...
		setResizable(false);
		
		this.setLocationRelativeTo(null); // will center the window
		
		setApplicationStatus(0); // application is ready
		
		setVisible(true);
	}


	private JPanel createHeader() {
		JPanel myPanel = new JPanel();
		JTextArea myTextArea = new JTextArea();
		String welcomeText = null;
		
		// introduce the software
		welcomeText = "Welcome to Neural Compressed Image (nci), a software tool for image compression.\n\n";
		welcomeText += "Nci is based on the dANN library (Dynamic Artificial Neural Network).\n";

		myTextArea.setText(welcomeText);
		myTextArea.setBackground(new Color(235, 235, 235));
		myTextArea.setEditable(false);
		
		myPanel.setBorder(BorderFactory.createTitledBorder("Info"));
		myPanel.add(myTextArea);
		
		return myPanel;
	}

	private JPanel createFileSelector() {
		JPanel myPanel = new JPanel();
		JLabel myLabel = new JLabel();
		String myText = null;
		this.selectFileBox = new JComboBox();
		myFileOpenIcon = new ImageIcon(this.ICON_PATH+"fileopen.png");
		this.selectFileButton = new JButton();
		
		myPanel.setLayout(new GridBagLayout());
		myText = "Please select an image file: ";

		myLabel.setText(myText);

//		String defaultEntry = "not set";
		this.selectFileBox.setPrototypeDisplayValue("WWWWWWWWWWWWWWWWWWWWWWWWWW");
//		this.selectFileBox.setAutoscrolls(false);
//		this.selectFileBox.setEditable(true);
//		this.selectFileBox.setEnabled(false);

		this.selectFileButton.setText("Browse...");
		this.selectFileButton.setIcon(myFileOpenIcon);
		this.selectFileButton.addActionListener(this);
		
		myPanel.setBorder(BorderFactory.createTitledBorder("File selection"));

		GridBagConstraints gbc = new GridBagConstraints();
		
		// add insets for a less densely packed gui
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.BOTH;

		// header on top, full width
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipadx = 2;
		myPanel.add(myLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.ipadx = 1;
		myPanel.add(this.selectFileBox, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.ipadx = 1;
		myPanel.add(this.selectFileButton, gbc);
				
		return myPanel;
		
	}

	private JPanel createOptionSelector() {
		JPanel myPanel = new JPanel();
		JLabel myLabel = new JLabel();
		String myText = null;
		
		myPanel.setLayout(new GridBagLayout());
		myText = "Here come the options...";
		myLabel.setText(myText);
		
		myPanel.setBorder(BorderFactory.createTitledBorder("Options"));

		GridBagConstraints gbc = new GridBagConstraints();
		
		// add insets for a less densely packed gui
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.BOTH;

		// header on top, full width
		gbc.gridx = 0;
		gbc.gridy = 0;
		myPanel.add(myLabel, gbc);

//		gbc.gridx = 1;
//		gbc.gridy = 0;
//		myPanel.add(xxx, gbc);
		
		return myPanel;
		
	}

	private JPanel createStatusReporter() {
		JPanel myPanel = new JPanel();
		this.myStatusLabel = new JLabel();
		this.myStatusText = null;
		
		myPanel.setLayout(new GridBagLayout());
		this.setApplicationStatus(0);

//		myStatusLabel.setText(myStatusText);
		
		myPanel.setBorder(BorderFactory.createTitledBorder("Status"));
		GridBagConstraints gbc = new GridBagConstraints();
		
		// add insets for a less densely packed gui
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.BOTH;

		// header on top, full width
		gbc.gridx = 0;
		gbc.gridy = 0;
		myPanel.add(this.myStatusLabel, gbc);

//		gbc.gridx = 1;
//		gbc.gridy = 0;
//		myPanel.add(xxx, gbc);
		
		return myPanel;
		
	}
	
	private JPanel createActionSelector() {
		JPanel myPanel = new JPanel();
		runButton = new JButton();
		myRunIcon = new ImageIcon(this.ICON_PATH+"button_ok.png");
		trainButton = new JButton();
		myTrainIcon = new ImageIcon(this.ICON_PATH+"reload.png");
		quitButton = new JButton();
		myQuitIcon = new ImageIcon(this.ICON_PATH+"button_cancel.png");
		
		myPanel.setLayout(new GridBagLayout());

		runButton.setText(" Run Compression ");
		runButton.setIcon(myRunIcon);
		runButton.addActionListener(this);
		// run compression not available yet.
		runButton.setEnabled(false);
		
		trainButton.setText(" Train Compression ");
		trainButton.setIcon(myTrainIcon);
		trainButton.addActionListener(this);

		quitButton.setText("Quit");
		quitButton.setIcon(myQuitIcon);
		quitButton.addActionListener(this);

		myPanel.setBorder(BorderFactory.createTitledBorder("Action"));
		GridBagConstraints gbc = new GridBagConstraints();
		
		// add insets for a less densely packed gui
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.BOTH;

		// header on top, full width
		gbc.gridx = 0;
		gbc.gridy = 0;
		myPanel.add(this.runButton, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		myPanel.add(this.trainButton, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		myPanel.add(this.quitButton, gbc);

		return myPanel;
	}
	
	public void actionPerformed(ActionEvent evt) {
		// Handling of user actions
		
		if (evt.getSource().equals(this.selectFileButton)) {
			// then open the filechooser			
			JFileChooser myFileChooser = new JFileChooser();
		
			int returnVal = myFileChooser.showOpenDialog(this);
			        
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				selectedFile = myFileChooser.getSelectedFile();
				System.out.println("FileChooser: a file was selected.");
				this.selectFileBox.addItem(selectedFile);
				//This is where a real application would open the file.
			}
			else {
				System.out.println("FileChooser: cancel pressed.");
			}
		}
		else if (evt.getSource().equals(this.runButton)) {
			// run or stop the compression

			if (this.getApplicationStatus() == 0) { // application was ready
				this.setApplicationStatus(1); // set to running
				if (this.getApplicationStatus() == 1) {
					this.runButton.setIcon(this.myQuitIcon);
					this.runButton.setText("Abort compression");
				}
			}
			else if (this.getApplicationStatus() == 1) { // application was running
				this.setApplicationStatus(0); // ready
				this.runButton.setIcon(this.myRunIcon);
				this.runButton.setText(" Run compression ");
			}
			else if (this.getApplicationStatus() == 2) { // application was in error state - try to run anyway
				this.setApplicationStatus(1); // set to running
				if (this.getApplicationStatus() == 1) {
					this.runButton.setIcon(this.myQuitIcon);					
					this.runButton.setText("Abort compression");
				}
			}
			else {
				// not handled yet.
			}
		}
		
		else if (evt.getSource().equals(this.trainButton)) {
			// run or stop the compression training

			if (this.getApplicationStatus() == 0) { // application was ready
				this.setApplicationStatus(1); // set to running
				if (this.getApplicationStatus() == 1) {
					this.trainButton.setIcon(this.myQuitIcon);
					this.trainButton.setText("Abort training");
					
					// show the train dialog and strat the training
					this.train(this.trainDialog());
				}
			}
			else if (this.getApplicationStatus() == 1) { // application was running
				this.setApplicationStatus(0); // ready
				this.trainButton.setIcon(this.myTrainIcon);
				this.trainButton.setText(" Run training ");
			}
			else if (this.getApplicationStatus() == 2) { // application was in error state - try to run anyway
				this.setApplicationStatus(1); // set to running
				if (this.getApplicationStatus() == 1) {
					this.trainButton.setIcon(this.myQuitIcon);
					this.trainButton.setText("Abort training");
					// show the train dialog and strat the training
					this.train(this.trainDialog());
				}
			}
			else {
				// not handled yet.
			}
		}
		
		else if (evt.getSource().equals(this.quitButton)) {
			// quit? ask for confirmation
			Object[] options = { "Yes", "No" };
			int selectedValue = JOptionPane.showOptionDialog(null, "Do you really want to quit?", "Warning",
			             JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
			             null, options, options[1]);
			if (selectedValue ==  0) {
				// close this program
				System.out.println("Quit confirmed. Goodbye.");
				System.exit(0);
			}
			else if (selectedValue == 1) {
				// do nothing
			}
			else {
				// should not happen
			}
		}
		else {
			// should not happen
		}
		
	}

	
	private int trainDialog() {
		int nbCycles = -1;
		
		boolean inputOk = false;
		
		while (!(inputOk)) {
			String inputValue = JOptionPane.showInputDialog(this, "Please enter a number of\ntraining cycles (1 - 1000)");
			try {
				nbCycles = Integer.valueOf(inputValue);
				if ((nbCycles > 0) && (nbCycles <= 1000)) {
					inputOk = true;
				}
			}
			catch (Exception ex) {
				System.out.println("xxxx"+ex.toString());
				if (ex.toString().matches(".*null.*")) {
					// then cancel was pressed
					nbCycles = -1;
					inputOk = true;
				}
			}
		}
		
		return nbCycles;
	}


	private void train(int nbCycles) {
		// start the training only if nbCycles > 0
		if (nbCycles < 0) {
			return;
		}
		
		// if we are here, we have a valid value of nbCycles to
		// start the training
		
		// complement the status info
		this.myStatusText += " for "+nbCycles+" cycles.";
		this.myStatusLabel.setText(this.myStatusText);
		
//		NciBrain brain = new NciBrain();
//		see... NciBrain(double compression, int xSize, int ySize, boolean extraConnectivity)

//		brain.setLearning(true);
		// and so on....
	
	}


	private void setApplicationStatus(int statusToSet) {
		// statusId:
		// 0: ready
		// 1: running
		// 2: error
		
	
		// check if a file was selected
		if ((statusToSet == 1) && (selectedFile == null)) {
			statusToSet = 2; // error
		}

		this.applicationStatus = statusToSet;

		switch (statusToSet) {
			case 0: this.myStatusText = "ready."; break;
			case 1: this.myStatusText = "running... compressing file "+this.selectedFile.getName()+" (fake for now)"; break;
			case 2: this.myStatusText = "error! No file selected."; break;
			default: this.myStatusText = "unknown.";
		}
//		myStatusPanel.remove(this.myStatusLabel);
		this.myStatusLabel.setText(this.myStatusText);
//		myStatusPanel.add(this.myStatusLabel);
		
	}

	private int getApplicationStatus() {
		// statusId:
		// 0: ready
		// 1: running
		// 2: error	
		return this.applicationStatus;

	}

	public static void main(String args[]) {
		MainWindow myMainwindow = new MainWindow();
	}

}
