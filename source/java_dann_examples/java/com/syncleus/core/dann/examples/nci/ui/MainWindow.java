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
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
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
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

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
	private final String JFRAME_TITLE = "dANN-nci - Image Compression";
	private final String ICON_PATH = "./icons/";

	// default options
	private final int nbCyclesInitVal = 20; // number of training cylces
	private final int imageChunkXInitVal = 10; // size X of image chunks sent to the brain
	private final int imageChunkYInitVal = 10; // size Y of image chunks sent to the brain
	private final double compressionRateInitVal = 0.5; // compression rate: 0 - 1.0 
	// CONFIG END
	////////////////
	
	private JPanel myHeader;
	private JPanel myFileSelector;
	private JPanel myOptionSelector;
	private JPanel myStatusReporter;
	private JPanel myCommandSelector;
	
	private JComboBox selectFileBox;
	private JButton selectFileButton;
	private File selectedFile;
	
	private JButton runButton;
	private JButton trainButton;
	private JButton quitButton;
	private String myStatusText;
	private JLabel myStatusLabel;
	private int applicationStatus;
	private ImageIcon myFileOpenIcon;
	private ImageIcon myRunIcon;
	private ImageIcon myTrainIcon;
	private ImageIcon myQuitIcon;
	
	private NciBrain brain;
	private JSpinner nbCyclesSpin;
	private int nbCycles;
	private ImageIcon myMatrixIcon;
	private ImageIcon myCompressIcon;
	private JSpinner compressionRateSpin;
	private ImageIcon myCancelIcon;
	private double compressionRate;
	private JButton showBrain3dViewButton;
	private JButton showBrain3dViewButton2;
	private JButton showImageButton;
	private ImageIcon myShowBrain3dViewIcon;
	private JSpinner imageChunkXSizeSpin;
	private JSpinner imageChunkYSizeSpin;
	private Integer imageChunkXSize;
	private Integer imageChunkYSize;
	private final Random random = new Random();
	private ImageIcon myImageIcon;
	private URL selectedFileUrl;
	
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
		getContentPane().add(myCommandSelector, gbc);

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
		myText = "Please select an input image file: ";

		myLabel.setText(myText);

//		String defaultEntry = "not set";
		this.selectFileBox.setPrototypeDisplayValue("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
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
		this.myTrainIcon = new ImageIcon(this.ICON_PATH+"reload.png");
		JLabel nbCyclesSpinLabel = new JLabel();
		String nbCyclesSpinText = null;
		this.nbCyclesSpin = new JSpinner();
		
//		this.myMatrixIcon = new ImageIcon(this.ICON_PATH+"math_matrix.png"); //	ugly
		this.myMatrixIcon = new ImageIcon(this.ICON_PATH+"randr.png"); //	ugly
		JLabel imageChunkXSizeSpinLabel = new JLabel();
		String imageChunkXSizeSpinText = null;
		this.imageChunkXSizeSpin = new JSpinner();
		
		JLabel imageChunkYSizeSpinLabel = new JLabel();
		String imageChunkYSizeSpinText = null;
		this.imageChunkYSizeSpin = new JSpinner();

		this.myCompressIcon = new ImageIcon(this.ICON_PATH+"khtml_kget.png");		
		JLabel compressionRateSpinLabel = new JLabel();
		String compressionRateSpinText = null;
		this.compressionRateSpin = new JSpinner();
		
		myPanel.setLayout(new GridBagLayout());

		// number of cycles to perform for training
		nbCyclesSpinLabel.setIcon(this.myTrainIcon);
		nbCyclesSpinText = "Training cycles: ";
		nbCyclesSpinLabel.setText(nbCyclesSpinText);	
		SpinnerModel nbCycelesSpinModel =
	        new SpinnerNumberModel(nbCyclesInitVal, //initial value
	                               10, //min
	                               1000, //max
	                               10); //step
		this.nbCyclesSpin.setModel(nbCycelesSpinModel);
		((JSpinner.DefaultEditor)this.nbCyclesSpin.getEditor())
		.getTextField().setEditable(false);
		
		// brain size x option
		imageChunkXSizeSpinLabel.setIcon(this.myMatrixIcon);
		imageChunkXSizeSpinText = "Image Chunks X size (pixels):";
		imageChunkXSizeSpinLabel.setText(imageChunkXSizeSpinText);

		SpinnerModel imageChunkXSizeSpinModel =
	        new SpinnerNumberModel(imageChunkXInitVal, //initial value
	                               2, //min
	                               20, //max
	                               1); //step
		this.imageChunkXSizeSpin.setModel(imageChunkXSizeSpinModel);
		((JSpinner.DefaultEditor)this.imageChunkXSizeSpin.getEditor())
		.getTextField().setEditable(false);
		
		// brain size y option
		imageChunkYSizeSpinLabel.setIcon(this.myMatrixIcon);
		imageChunkYSizeSpinText = "Image Chunks Y size (pixels):";
		imageChunkYSizeSpinLabel.setText(imageChunkYSizeSpinText);
		SpinnerModel imageChunkYSizeSpinModel =
	        new SpinnerNumberModel(imageChunkYInitVal, //initial value
	                               2, //min
	                               20, //max
	                               1); //step
		this.imageChunkYSizeSpin.setModel(imageChunkYSizeSpinModel);
		((JSpinner.DefaultEditor)this.imageChunkYSizeSpin.getEditor())
		.getTextField().setEditable(false);
		
		// compression rate option
		compressionRateSpinLabel.setIcon(this.myCompressIcon);
		compressionRateSpinText = "Compression rate:";
		compressionRateSpinLabel.setText(compressionRateSpinText);
		SpinnerModel compressionRateSpinModel =
	        new SpinnerNumberModel(compressionRateInitVal, //initial value
	                               0.1, //min
	                               0.9, //max
	                               0.1); //step
		this.compressionRateSpin.setModel(compressionRateSpinModel);
		((JSpinner.DefaultEditor)this.compressionRateSpin.getEditor())
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

		gbc.gridx = 0;
		gbc.gridy = 1;
		myPanel.add(imageChunkXSizeSpinLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		myPanel.add(this.imageChunkXSizeSpin, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		myPanel.add(imageChunkYSizeSpinLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		myPanel.add(this.imageChunkYSizeSpin, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		myPanel.add(compressionRateSpinLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		myPanel.add(this.compressionRateSpin, gbc);

		return myPanel;
		
	}

	private JPanel createStatusReporter() {
		JPanel myPanel = new JPanel();
		this.myStatusLabel = new JLabel();
		this.myStatusText = null;
		// The button opening the 3d visualization of the brain
		// is set here, as it shows the "status" of the brain
		this.myShowBrain3dViewIcon = new ImageIcon(this.ICON_PATH+"view_multicolumn.png");
		this.myImageIcon = new ImageIcon(this.ICON_PATH+"frame_image.png");
		this.showBrain3dViewButton = new JButton();
		this.showBrain3dViewButton2 = new JButton();
		this.showImageButton = new JButton();
		myPanel.setLayout(new GridBagLayout());
		this.setApplicationStatus(0);

//		myStatusLabel.setText(myStatusText);
		
		this.showBrain3dViewButton.setIcon(this.myShowBrain3dViewIcon);
		this.showBrain3dViewButton.setText("Show Brain in 3d (Java3D)");
		this.showBrain3dViewButton.addActionListener(this);

		this.showBrain3dViewButton2.setIcon(this.myShowBrain3dViewIcon);
		this.showBrain3dViewButton2.setText("Show Brain in 3d (JOGL)");
		this.showBrain3dViewButton2.addActionListener(this);

		this.showImageButton.setIcon(this.myImageIcon);
		this.showImageButton.setText("Show Picture");
		this.showImageButton.addActionListener(this);

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

		gbc.gridx = 1;
		gbc.gridy = 1;
		myPanel.add(this.showBrain3dViewButton2, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;
		myPanel.add(this.showImageButton, gbc);

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

		this.runButton.setText(" Run Compression ");
		this.runButton.setIcon(myRunIcon);
		this.runButton.addActionListener(this);
		// run compression not available yet.
		this.runButton.setEnabled(false);
		
		this.trainButton.setText(" Train Compression ");
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
	
	public void actionPerformed(ActionEvent evt) {
		// Handling of user actions
		
		// input file selection
		if (evt.getSource().equals(this.selectFileButton)) {
			// then open the filechooser			
			JFileChooser myFileChooser = new JFileChooser();
		
			int returnVal = myFileChooser.showOpenDialog(this);
			        
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				this.selectedFile = myFileChooser.getSelectedFile();
				try {
					this.selectedFileUrl = this.selectedFile.toURL();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("FileChooser: a file was selected.");
				this.selectFileBox.addItem(selectedFile);
				//This is where a real application would open the file.
			}
			else {
				System.out.println("FileChooser: cancel pressed.");
			}
		}
		
		// commands buttons
		else if (evt.getSource().equals(this.runButton)) {
			// run or stop the compression

			if (this.getApplicationStatus() == 0) { // application was ready
				this.setApplicationStatus(1); // set to running
				if (this.getApplicationStatus() == 1) {
					this.runButton.setIcon(this.myCancelIcon);
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
					this.runButton.setIcon(this.myCancelIcon);					
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
				this.setApplicationStatus(3); // set to run training
				if (this.getApplicationStatus() == 3) {
					this.trainButton.setIcon(this.myCancelIcon);
					this.trainButton.setText("Abort training");
					
					// show the train dialog and strat the training
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
		
		// show the Brain3dView (Java3D version)
		else if (evt.getSource().equals(this.showBrain3dViewButton)) {
			Brain3dView myBrain3dView = new Brain3dView(this);
			
		}
		// show the Brain3dView (JOGL version)
		else if (evt.getSource().equals(this.showBrain3dViewButton2)) {
			Brain3dViewJogl myBrain3dViewJogl = new Brain3dViewJogl(this);
			
		}
		// show the image(s)
		else if (evt.getSource().equals(this.showImageButton)) {
			ImageViewer myImageViewer = new ImageViewer(this);
			
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


	private void train() {
		
		// start the training with the selected options
		
		this.nbCycles = (Integer) this.nbCyclesSpin.getValue();
		this.imageChunkXSize = (Integer) this.imageChunkXSizeSpin.getValue();
		this.imageChunkYSize = (Integer) this.imageChunkYSizeSpin.getValue();
		this.compressionRate = (Double) this.compressionRateSpin.getValue();

		// start the training of the brain within a thread
		// so the status can be reported in the gui
		
		if (this.myStatusReporter.getComponentCount() == 5) { // remove the last progress bar if any
			this.myStatusReporter.remove(4);
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
        gbc.gridy = 2;
		gbc.gridwidth = 3;
        this.myStatusReporter.add(myProgressBar, gbc);
 //		pack();
 		
		// start the thread
		Thread worker1 = new Thread() {
                public void run() {
                        try {
                        	// simulate the first percents progress while creating the brain
                        	myProgressBar.setValue(1);
                        	brain = new NciBrain(compressionRate, imageChunkXSize, imageChunkYSize, true);
                        	brain.setLearning(true); // a brain wants to learn, this is the nature!
                        	
                        	// for nbCycles...
                        	// repeate training...
                    			
                        	for (int i = 1; i <= nbCycles; i++) {
                        		myProgressBar.setValue(i); // update the progress bar
                    			
//                        		currentTrainImage = ImageIO.read(trainingImages[random.nextInt(trainingImages.length)]);
                        		
                        		// only one input image for now.
                        		BufferedImage currentTrainImage = ImageIO.read(selectedFile);
                        		
                    			// select a random subsection of the image of 
                        		// imageChunkXSize and imageChunkYSize dimension
                    			currentTrainImage = currentTrainImage.getSubimage(random.nextInt(currentTrainImage.getWidth() - imageChunkXSize), random.nextInt(currentTrainImage.getHeight() - imageChunkYSize), imageChunkXSize, imageChunkYSize);

                    			//now train the image
                    			brain.compress(currentTrainImage);
                    			
                        	}
                        	
                        	// end of training
                        	brain.setLearning(false);
                        	
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
		if (((statusToSet == 1) || (statusToSet == 3)) && (selectedFile == null)) {
			statusToSet = 2; // error
		}
		
		this.applicationStatus = statusToSet;

		switch (statusToSet) {
			case 0: this.myStatusText = "Ready. Wating for command."; break;
			case 1: this.myStatusText = "Running... compressing file "+this.selectedFile.getName(); break;
			case 2: this.myStatusText = "Error! No file selected."; break;
			case 3: this.myStatusText = "Running NN training on "+this.selectedFile.getName()+" for "+this.nbCyclesSpin.getValue()+" cycles."; break;
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

	// get and set methods
	public URL getSelectedFileUrl() {
		return this.selectedFileUrl;
	}
	// ...
	
	// main method: This class is the entry point of the nci GUI
	public static void main(String args[]) {
		MainWindow myMainwindow = new MainWindow();
	}

}
