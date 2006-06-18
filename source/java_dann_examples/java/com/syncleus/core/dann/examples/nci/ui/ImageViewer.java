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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
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
//	private final int JFRAME_WIDTH = 800;
//	private final int JFRAME_HEIGHT = 600;

	// CONFIG END
	////////////////

	private MainWindow myMainWindow;
	JPanel myPanel;
	private ImageIcon myImageIcon;
	private JLabel myImageLabel;
	
	public ImageViewer(MainWindow myMainWindow) {
			
			this.myMainWindow = myMainWindow;
			
//		    getContentPane().setLayout(null);

			// Create a JPanel containing the Image
		    myPanel = new JPanel();
//		    myPanel.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
//		    myPanel.setLocation(0, 0);
//			BorderLayout layout = new BorderLayout();
//		    myPanel.setLayout(layout);

		    // the swing layout "OverlayLayout" allows to arrange
		    // components over the top of each other with some 
		    // nice management.
		    OverlayLayout layout = new OverlayLayout(myPanel); 
		    myPanel.setLayout(layout);

//		    myPanel.setLayout(null);
			
		    // Try to get access to the file - pack it into a Swing component
		    try {

				// add some test markers
				this.markArea(myPanel, 5, 5, "it1");
				this.markArea(myPanel, 10, 10, "it2");
				this.markArea(myPanel, 20, 20, "it3");

		    	
		    	System.out.println("Trying to get file from: "+this.myMainWindow.getSelectedFileUrl());
		    	myImageIcon = new ImageIcon(this.myMainWindow.getSelectedFileUrl());

		    	// pack the ImageIcon into a JLabel - that's the way with swing
		    
		    	myImageLabel = new JLabel(myImageIcon);
//		    	myPanel.setLayout(null);
		    	myImageLabel.setLocation(100, 100);
		    	myImageLabel.setVisible(true);
		    	
		    	myPanel.add(myImageLabel);
		    	
		    	
			    myPanel.setVisible(true);

			    // add the panel containing the Image to the JFrame
			    getContentPane().add(myPanel);

			    setSize(myImageIcon.getIconWidth(), myImageIcon.getIconHeight());
//			    setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
			    setTitle("Image viewer");

				setResizable(true);
				
				setLocationRelativeTo(myMainWindow); // will center the window according to the position of the MainWindow

				// move the window a little
//				setLocation(this.getX() - 100, this.getY() - 100);
				
			    setVisible(true);
		    	
		    } catch (Exception ex) {
		    	System.err.println("Error loading Image.");
		    	JOptionPane.showMessageDialog(this.myMainWindow, "No image to display - Please select some file(s).");
		    	
		    }
		    
		  }  

	public void markArea(JPanel myPanel, int posX, int posY, String markerText) {
		JLabel myArea = new JLabel();
		myArea.setText(markerText);
	
		myArea.setLocation(posX, posY);

		myPanel.add(myArea);
	}

}
