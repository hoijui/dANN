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

package com.syncleus.core.dann.examples.xor.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
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
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import com.syncleus.core.dann.examples.xor.XorBrain;
import com.syncleus.dann.NeuronProcessingUnit;

/**
 * A graphical user interface based on java swing
 * for the xor example application of dANN.
 * -> an "xor" operation is an exclusive "or".
 * Simple example with two inputs (1=true, 0=false)
 * (0,1)->1
 * (1,0)->1
 * (1,1)->0
 * (0,0)->0
 * The xor exemple of dANN is using three inputs,
 * and the brain has to learn the expected results:
 * (1,0,0) should give 1
 * (0,0,0) should give -1
 * (1,1,0) should give -1
 * etc..
 * (outputs 0 and 1 are replaced by -1 and 1)
 * This class implements the main Window of the gui.
 * The structure of the gui is similar to the one
 * initially designed for the nci example of dANN.
 * 
 * <!-- Author: chickenf -->
 * @author chickenf
 */

public class MainWindow extends JFrame implements ActionListener {
	
	////////////////
	// CONFIG START

	private final String JFRAME_TITLE = "dANN-XOR - example application of the dANN API";
	private final String ICON_PATH = "./icons/";

	// default options
	private final int nbCyclesInitVal = 1000; // default number of training cylces

	// CONFIG END
	////////////////
	
	private JPanel myHeader;
	private JPanel myInputSelector;
	private JPanel myOptionSelector;
	private JPanel myStatusReporter;
	private JPanel myCommandSelector;
	
	private JButton runButton;
	private JButton trainButton;
	private JButton quitButton;
	private String myStatusText;
	private JLabel myStatusLabel;
	private int applicationStatus;
	private ImageIcon myRunIcon;
	private ImageIcon myTrainIcon;
	private ImageIcon myQuitIcon;
	
	private XorBrain brain;
	private JSpinner nbCyclesSpin;
	private int nbCycles;
	private ImageIcon myCancelIcon;
	private JButton showBrain3dViewButton;
	private int sumOfTrainingCycles;
	private ImageIcon showBrain3dViewIcon;
	private ImageIcon showBrainStateIcon;
	private JButton showBrainStateButton;
	
	public MainWindow() {

		////////////
		// first, create the Xor Brain.
		
		brain = new XorBrain(); // com.syncleus.core.dann.examples.xor.Main
		
		this.sumOfTrainingCycles = 0;
		
		////////////
		// initialize the gui
		
		// some basic JFrame settings
		setTitle(JFRAME_TITLE);
//		setSize(new Dimension(JFRAME_WIDTH, JFRAME_HEIGHT));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new GridBagLayout());
		
		// create and add the content of the JFrame:
		// four JPanels
		myHeader = createHeader();
		
		myInputSelector = createInputSelector();
		
		myOptionSelector = createOptionSelector();

		myStatusReporter = createStatusReporter();
		
		myCommandSelector = createCommandSelector();

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
		getContentPane().add(myInputSelector, gbc);

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
		getContentPane().add(myCommandSelector, gbc);

		// optimize the window size
		pack();
		
		// fix the window size for now...
		setResizable(false);
		
		this.setLocationRelativeTo(null); // will center the window
		
		setApplicationStatus(0); // application is ready
		
		setVisible(true);

		// the gui is up and running

	}


	private JPanel createHeader() {
		JPanel myPanel = new JPanel();
		JTextArea myTextArea = new JTextArea();
		String welcomeText = null;
		
		// introduce the software
//		welcomeText = "Welcome to Neural Compressed Image (nci), a software tool for image compression.\n\n";
//		welcomeText += "Nci is based on the dANN library (Dynamic Artificial Neural Network).\n";
		welcomeText = "Welcome to the XOR demo - a neural network learining the XOR operation.\n";
		welcomeText += "\n";
		welcomeText += "XOR is a logical operation (exclusive OR).\n";

		// unclear, could the xor brain take 0 and 1 instead
		// of -1 and 1 as setDesired() ? 
//		welcomeText += "Example: with 0 = false and 1 = true:\n";
//		welcomeText += "(1,0,0) gives 1, but (1,1,0) gives 0.\n";
		welcomeText += "\n";
		welcomeText += "This application is based on the dANN library (Dynamic Artificial Neural Network).\n";
 
		
		myTextArea.setText(welcomeText);
		myTextArea.setBackground(new Color(235, 235, 235));
		myTextArea.setEditable(false);
		
		myPanel.setBorder(BorderFactory.createTitledBorder("Info"));
		myPanel.add(myTextArea);
		
		return myPanel;
	}

	private JPanel createInputSelector() {
		JPanel myPanel = new JPanel();
		JLabel myLabel1 = new JLabel();
		String myText1 = null;
		JLabel myLabel2 = new JLabel();
		String myText2 = null;
		JLabel myLabel3 = new JLabel();
		String myText3 = null;

//		this.selectFileBox = new JComboBox();
//		myFileOpenIcon = new ImageIcon(this.ICON_PATH+"fileopen.png");
//		this.selectFileButton = new JButton();
		
		myPanel.setLayout(new GridBagLayout());

		myText1 = "(0,0,0) should give -1";
		myLabel1.setText(myText1);

		myText2 = "(1,0,0), (0,1,0) and (0,0,1) should give 1";
		myLabel2.setText(myText2);

		myText3 = "(1,1,1), (1,1,0), (1,0,1) and (0,1,1) should give -1";
		myLabel3.setText(myText3);

		myPanel.setBorder(BorderFactory.createTitledBorder("Input values for training"));

		GridBagConstraints gbc = new GridBagConstraints();
		
		// add insets for a less densely packed gui
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.BOTH;

		// header on top, full width
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipadx = 2;
		myPanel.add(myLabel1, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		myPanel.add(myLabel2, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		myPanel.add(myLabel3, gbc);
				
		return myPanel;
		
	}

	private JPanel createOptionSelector() {
		JPanel myPanel = new JPanel();
		this.myTrainIcon = new ImageIcon(this.ICON_PATH+"reload.png");
		JLabel nbCyclesSpinLabel = new JLabel();
		String nbCyclesSpinText = null;
		this.nbCyclesSpin = new JSpinner();
				
		myPanel.setLayout(new GridBagLayout());

		// number of cycles to perform for training
		nbCyclesSpinLabel.setIcon(this.myTrainIcon);
		nbCyclesSpinText = "Training cycles: ";
		nbCyclesSpinLabel.setText(nbCyclesSpinText);	
		SpinnerModel nbCycelesSpinModel =
	        new SpinnerNumberModel(nbCyclesInitVal, //initial value
	                               1000, //min
	                               100000, //max
	                               1000); //step
		this.nbCyclesSpin.setModel(nbCycelesSpinModel);
		((JSpinner.DefaultEditor)this.nbCyclesSpin.getEditor())
		.getTextField().setEditable(false);
		
		
		myPanel.setBorder(BorderFactory.createTitledBorder("Options"));

		GridBagConstraints gbc = new GridBagConstraints();
		
		// add insets for a less densely packed gui
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.BOTH;

		// header on top, full width
		gbc.gridx = 0;
		gbc.gridy = 0;
		myPanel.add(nbCyclesSpinLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		myPanel.add(this.nbCyclesSpin, gbc);

		return myPanel;
		
	}

	private JPanel createStatusReporter() {
		JPanel myPanel = new JPanel();
		this.myStatusLabel = new JLabel();
		this.myStatusText = null;
		// The button opening the 3d visualization of the brain
		// is set here, as it shows the "status" of the brain
		this.showBrain3dViewButton = new JButton();
		this.showBrain3dViewIcon = new ImageIcon(this.ICON_PATH+"view_multicolumn.png");
		this.showBrainStateButton = new JButton();
		this.showBrainStateIcon = new ImageIcon(this.ICON_PATH+"math_matrix.png");

//		this.showImageButton = new JButton();
		myPanel.setLayout(new GridBagLayout());
		this.setApplicationStatus(0);

//		myStatusLabel.setText(myStatusText);
		
		this.showBrain3dViewButton.setIcon(this.showBrain3dViewIcon);
		this.showBrain3dViewButton.setText("Show Brain in 3d (Java3D)");
		this.showBrain3dViewButton.addActionListener(this);

		this.showBrainStateButton.setIcon(this.showBrainStateIcon);
		this.showBrainStateButton.setText("Show Brain State (Values)");
		this.showBrainStateButton.addActionListener(this);

//		this.showImageButton.setIcon(this.myImageIcon);
//		this.showImageButton.setText("Show Picture");
//		this.showImageButton.addActionListener(this);

		myPanel.setBorder(BorderFactory.createTitledBorder("Status"));
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		// add insets for a less densely packed gui
		gbc.insets = new Insets(10, 10, 10, 10);
		
		gbc.fill = GridBagConstraints.BOTH;

		// status report
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		myPanel.add(this.myStatusLabel, gbc);

		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		myPanel.add(this.showBrain3dViewButton, gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		myPanel.add(this.showBrainStateButton, gbc);

//		gbc.gridx = 1;
//		gbc.gridy = 1;
//		myPanel.add(this.showImageButton, gbc);

		return myPanel;
		
	}
	
	private JPanel createCommandSelector() {
		JPanel myPanel = new JPanel();
		this.runButton = new JButton();
		this.myRunIcon = new ImageIcon(this.ICON_PATH+"button_ok.png");
		this.trainButton = new JButton();
		this.quitButton = new JButton();
		this.myQuitIcon = new ImageIcon(this.ICON_PATH+"exit.png");
		this.myCancelIcon = new ImageIcon(this.ICON_PATH+"button_cancel.png");
		
		myPanel.setLayout(new GridBagLayout());

		this.runButton.setText(" Run the XOR ");
		this.runButton.setIcon(myRunIcon);
		this.runButton.addActionListener(this);
//		this.runButton.setEnabled(false);
		
		this.trainButton.setText(" Train the XOR ");
		this.trainButton.setIcon(myTrainIcon);
		this.trainButton.addActionListener(this);

		this.quitButton.setText("Quit");
		this.quitButton.setIcon(myQuitIcon);
		this.quitButton.addActionListener(this);

		myPanel.setBorder(BorderFactory.createTitledBorder("Commands"));
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
		myPanel.add(this.quitButton, gbc);

		return myPanel;
	}

	private void showBrainState() {
		JFrame stateFrame = new JFrame();
//		  JLabel headerText = new JLabel();
		JTable stateTable = new JTable(11, 8);  // !! (ysize,  xsize)
	
		stateFrame.setTitle("Brain state (after "+this.sumOfTrainingCycles+" training cycles)");
	
		stateFrame.setLayout(new GridBagLayout());
	
//		  headerText.setText("Results:");

		stateFrame.setPreferredSize(new Dimension(1000,400));

		stateTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		stateTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		stateTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		stateTable.getColumnModel().getColumn(3).setPreferredWidth(150);
		stateTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		stateTable.getColumnModel().getColumn(5).setPreferredWidth(100);
		stateTable.getColumnModel().getColumn(6).setPreferredWidth(100);
		stateTable.getColumnModel().getColumn(7).setPreferredWidth(100);
		
        
		stateTable.getColumnModel().setColumnMargin(6);

		// table header
		stateTable.setValueAt("Neuron", 0, 0);
		stateTable.setValueAt("Neuron DeltaTrain", 0, 1);
		stateTable.setValueAt("Neuron BiasWeight", 0, 2);
		stateTable.setValueAt("Synapse to x - DeltaTrain", 0, 3);
		stateTable.setValueAt("Synapse to x - Weight", 0, 4);
		stateTable.setValueAt("Synapse to y - DeltaTrain", 0, 5);
		stateTable.setValueAt("Synapse to y - Weight", 0, 6);
		// etc.. for all synapses of this neuron
		
		// table data
//		int col = 0;
		int row = 1;

		// One table line per neuron -> 10 lines
		// WARNING: cast problem with brain.getSecondLayer().children -> ArrayList of ProcessingUnit instead of NeuronProcessingUnit
//		  for (NeuronProcessingUnit myNeuron : (NeuronProcessingUnit)this.brain.getSecondLayer().children) {
		for (int i = 0; i < this.brain.getSecondLayer().children.size(); i++) {
			double neuronDeltaTrain = ((NeuronProcessingUnit)this.brain.getSecondLayer().children.get(i)).deltaTrain;
			//double neuronBiasWeight = xxx;
			//...for each synapse...
			//double synapseDeltaTrain = xxx;
			//double synapseWeight = xxx;
			
			stateTable.setValueAt("Neuron"+String.valueOf(i+1), row, 0); // just give a number as name
			stateTable.setValueAt(String.valueOf(neuronDeltaTrain), row, 1);
//			stateTable.setValueAt(neuronBiasWeight, row, 2);
//			stateTable.setValueAt(synapseDeltaTrain, row, 3);
//			stateTable.setValueAt(synapseWeight, row, 4);
			// etc..
			
			row++;
		}
	
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10,10,10,10);
	
		gbc.anchor = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		stateFrame.add(stateTable);
	
		stateFrame.pack();
		stateFrame.setLocationRelativeTo(null); // center the window
		stateFrame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent evt) {
		// Handling of user actions
		
		// commands buttons
		if (evt.getSource().equals(this.runButton)) {
			// run the xor computation (fast, so no button refresh)
			this.run();
		}
		
		else if (evt.getSource().equals(this.trainButton)) {
			// run or stop the compression training

			if (this.getApplicationStatus() == 0) { // application was ready
				this.setApplicationStatus(3); // set to run training
				if (this.getApplicationStatus() == 3) {
					this.trainButton.setIcon(this.myCancelIcon);
					this.trainButton.setText("Abort training");
					
					// show the train dialog and start the training
					this.train();
				}
			}
			else if (this.getApplicationStatus() == 3) { // application was running
				this.setApplicationStatus(0); // ready
				this.trainButton.setIcon(this.myTrainIcon);
				this.trainButton.setText(" Run training ");
			}
			else if (this.getApplicationStatus() == 2) { // application was in error state - try to run anyway
				this.setApplicationStatus(3); // set to run training
				if (this.getApplicationStatus() == 3) {
					this.trainButton.setIcon(this.myCancelIcon);
					this.trainButton.setText("Abort training");
					// show the train dialog and strat the training
					this.train();
				}
			}
			else {
				// not handled yet.
			}
		}
		
		else if (evt.getSource().equals(this.quitButton)) {
			// quit? ask for confirmation
			Object[] options = { "Yes", "No" };
			int selectedValue = JOptionPane.showOptionDialog(this, "Do you really want to quit?", "Warning",
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
		
		// show the Brain3dView (Java3D version)
		else if (evt.getSource().equals(this.showBrain3dViewButton)) {
			Brain3dView myBrain3dView = new Brain3dView(this);
			
		}
		// show the brain state (values at synapses etc...)
		else if (evt.getSource().equals(this.showBrainStateButton)) {
			// display a window with all the current brain parameters
			this.showBrainState();
		}
		
		else {
			// should not happen
		}
		
	}

//	// deprecated
//	private int trainDialog() {
//		int nbCycles = -1;
//		
//		boolean inputOk = false;
//		
//		while (!(inputOk)) {
//			String inputValue = JOptionPane.showInputDialog(this, "Please enter a number of\ntraining cycles (1 - 1000)");
//			try {
//				nbCycles = Integer.valueOf(inputValue);
//				if ((nbCycles > 0) && (nbCycles <= 1000)) {
//					inputOk = true;
//				}
//			}
//			catch (Exception ex) {
//				System.out.println("xxxx"+ex.toString());
//				if (ex.toString().matches(".*null.*")) {
//					// then cancel was pressed
//					nbCycles = -1;
//					inputOk = true;
//				}
//			}
//		}
//		
//		return nbCycles;
//	}


	private void run() {
		
		String myResultWindowTitle = "XOR results from the brain";
		
		ArrayList<Double> myResultsList = brain.testOutput();
		
//		JOptionPane.showMessageDialog(this, myResultsText, myResultWindowTitle, JOptionPane.INFORMATION_MESSAGE);

		JFrame resultsFrame = new JFrame();
//		JLabel headerText = new JLabel();
		JTable resultsTable = new JTable(10, 4);  // !! (ysize,  xsize)
		
		resultsFrame.setTitle("Brain XOR run results (after "+this.sumOfTrainingCycles+" training cycles)");
		
		resultsFrame.setLayout(new GridBagLayout());
		
//		headerText.setText("Results:");

		resultsTable.setPreferredSize(new Dimension(600,150));

		// table header
		resultsTable.setValueAt("First Input", 0, 0);
		resultsTable.setValueAt("Second Input", 0, 1);
		resultsTable.setValueAt("Third Input", 0, 2);
		
		resultsTable.setValueAt("Ouput of the Brain", 0, 3);

		// table data
		int col = 0;
		int row = 1;
		for (double value : myResultsList) {
			resultsTable.setValueAt(value, row, col);
			col++;
			if (col >= 4) { // new row
				row++;
				col = 0;
			}
		}
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10,10,10,10);
		
//		gbc.gridx = 0;
//		gbc.gridy = 0;
//		resultsFrame.add(headerText);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		resultsFrame.add(resultsTable);
		
		resultsFrame.pack();
		resultsFrame.setLocationRelativeTo(null); // center the window
		resultsFrame.setVisible(true);
		
	}


	private void train() {
		
		// start the training with the selected options
		
		this.nbCycles = (Integer) this.nbCyclesSpin.getValue();
		this.sumOfTrainingCycles +=  this.nbCycles;
//		this.imageChunkXSize = (Integer) this.imageChunkXSizeSpin.getValue();
//		this.imageChunkYSize = (Integer) this.imageChunkYSizeSpin.getValue();
//		this.compressionRate = (Double) this.compressionRateSpin.getValue();

		// start the training of the brain within a thread
		// so the status can be reported in the gui
		
		if (this.myStatusReporter.getComponentCount() == 4) { // remove the last progress bar if any
			this.myStatusReporter.remove(3);
		}
		final JProgressBar myProgressBar = new JProgressBar();
		myProgressBar.setMaximum(this.nbCycles); // we will report the progress for each training iteration
		myProgressBar.setValue(0);
		myProgressBar.setStringPainted(true);
        
		// Add the progress bar to the status panel
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 3;
		gbc.gridwidth = 3;
        this.myStatusReporter.add(myProgressBar, gbc);
 		pack();
 		
		// start the thread
		Thread worker1 = new Thread() {
                public void run() {
                        try {
                        	                        	
                        	// for nbCycles...
                        	// repeate training...
                    			
                        	for (int i = 1; i <= nbCycles; i++) {
                        		myProgressBar.setValue(i); // update the progress bar
                    			brain.train(1);
                    			
                        	}
                        	
                        	// end of training
//                        	brain.setLearning(false);
                        	
                        	// reset the command button for the next run
                        	trainButton.setIcon(myTrainIcon);
            				trainButton.setText(" Run training ");
            				setApplicationStatus(0); // set the status back to "ready" for the next run
                        }
                        catch (Exception ex) {
                        	// nothing here
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
//                                        System.out.println("Ready!");
                                }
                        });
                } // end of Thread run
        };
        worker1.start();
	}	


	private void setApplicationStatus(int statusToSet) {
		// statusId:
		// 0: ready
		// 1: running
		// 2: error
		
	
		// check if a file was selected
//		if (((statusToSet == 1) || (statusToSet == 3)) && (selectedFile == null)) {
//			statusToSet = 2; // error
//		}
		
		this.applicationStatus = statusToSet;

		switch (statusToSet) {
			case 0: this.myStatusText = "Ready. Wating for command."; break;
			case 1: this.myStatusText = "Running the brain to produce a XOR"; break;
			case 2: this.myStatusText = "Error! No file selected."; break;
			case 3: this.myStatusText = "Running NN training to learn a XOR"; break;
			default: this.myStatusText = "Unknown.";
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

	
	// main method: This class is the entry point of the XOR example GUI
	public static void main(String args[]) {
		MainWindow myMainwindow = new MainWindow();
	}

}
