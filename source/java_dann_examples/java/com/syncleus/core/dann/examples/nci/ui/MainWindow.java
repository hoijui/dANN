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
	final String ICON_PATH = "./";
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
	private JButton quitButton;
	private String myStatusText;
	private JLabel myStatusLabel;
	private JPanel myStatusPanel;
	private int applicationStatus;
	
	
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
		Icon myIcon = new ImageIcon("fileopen.png");
		this.selectFileButton = new JButton("Browse...", myIcon);
		
		myPanel.setLayout(new GridBagLayout());
		myText = "Please select an image file: ";

		myLabel.setText(myText);

//		String defaultEntry = "not set";
		this.selectFileBox.setPrototypeDisplayValue("WWWWWWWWWWWWWWWWWWWWWWWWWW");
//		this.selectFileBox.setAutoscrolls(false);
//		this.selectFileBox.setEditable(true);
//		this.selectFileBox.setEnabled(false);

//		this.selectFileButton.setText("Browse...");
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
		quitButton = new JButton();
		
		myPanel.setLayout(new GridBagLayout());

		runButton.setText(" Run Compression ");
		runButton.addActionListener(this);

		quitButton.setText("Quit");
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

		gbc.gridx = 1;
		gbc.gridy = 0;
		myPanel.add(this.quitButton, gbc);

		return myPanel;
	}
	
	public void actionPerformed(ActionEvent evt) {
		// Handling of user actions
		
		if (evt.getSource().equals(selectFileButton)) {
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
		else if (evt.getSource().equals(runButton)) {
			// run or stop the compression

			if (this.getApplicationStatus() == 0) { // application was ready
				this.setApplicationStatus(1); // set to running
				if (this.getApplicationStatus() == 1) {
					this.runButton.setText("Abort compression");
				}
			}
			else if (this.getApplicationStatus() == 1) { // application was running
				this.setApplicationStatus(0); // ready
				this.runButton.setText(" Run compression ");
			}
			if (this.getApplicationStatus() == 2) { // application was in error state - try to run anyway
				this.setApplicationStatus(1); // set to running
				if (this.getApplicationStatus() == 1) {
					this.runButton.setText("Abort compression");
				}
			}
			else {
				// not handled yet.
			}

			// simulate a run for now...
			
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			
			// run finished ok.
//			this.setApplicationStatus(0);
			
		}
		else if (evt.getSource().equals(quitButton)) {
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
