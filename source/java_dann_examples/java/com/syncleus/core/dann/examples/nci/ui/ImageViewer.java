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
import java.awt.image.BufferedImage;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * Image Viewer component of the graphical user interface based on java swing
 * for the nci example application of dANN.
 * -> "nci" stands for "Neural Compressed Image".
 * 
 * This class implements a JFrame started from the MainWindow class (main window of the gui)
 * The Image(s) selected for processing are displayed, and the chunks under
 * computation are shown in real time.
 * <!-- Author: chickenf -->
 * @author chickenf
 */

public class ImageViewer extends JFrame {
		
	////////////////
	// CONFIG START

	// Size of the Window containing the Image
	private final int JFRAME_WIDTH = 800;
	private final int JFRAME_HEIGHT = 600;

	// CONFIG END
	////////////////

	private MainWindow myMainWindow;
	JPanel myPanel;
	private ImageIcon myImageIcon;
	private JLabel myImageLabel;
	
	public ImageViewer(MainWindow myMainWindow) {
			
			this.myMainWindow = myMainWindow;
			

			// Create a JPanel containing the Image
		    myPanel = new JPanel();
//		    myPanel.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
		    myPanel.setLocation(0, 0);
			BorderLayout layout = new BorderLayout();
			myPanel.setLayout(layout);

//		    myPanel.setLayout(null);
			
		    // Try to get access to the file - pack it into a Swing component
		    try {
		    	System.out.println("Trying to get file from: "+this.myMainWindow.getSelectedFileUrl());
		    	myImageIcon = new ImageIcon(this.myMainWindow.getSelectedFileUrl());

		    	// pack the ImageIcon into a JLabel - that's the way with swing
		    
		    	myImageLabel = new JLabel(myImageIcon);
			    myPanel.add(myImageLabel, BorderLayout.CENTER);
		    	
		    } catch (Exception ex) {
		    	System.err.println("Error loading Image.");
		    	myPanel.removeAll();
		    	myImageLabel = new JLabel();
		    	myImageLabel.setText("No Image available - please select one.");
			    myPanel.add(myImageLabel, BorderLayout.CENTER);

		    }
		    
		    myPanel.setVisible(true);

		    // add the panel containing the Image to the JFrame
		    getContentPane().add(myPanel);

		    setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
		    setTitle("Image viewer");

			setResizable(true);
			
			setLocationRelativeTo(myMainWindow); // will center the window according to the position of the MainWindow

			// move the window a little
//			setLocation(this.getX() - 100, this.getY() - 100);
			
		    setVisible(true);
		  }  

}
