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

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.TimerTask;

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
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.Timer;
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
	private final int JFRAME_BORDER_SIZE = 60;
	
	// refresh interval in ms to follow the nci brain iterations
//	private final int REFRESH_INTERVAL = 1000;

	// CONFIG END
	////////////////

	private MainWindow myMainWindow;
//	JPanel myPanel;
	private JLayeredPane myPanel;
	private ImageIcon myImageIcon;
	private JLabel myImageLabel;

	private int lastLayer = 1;
	
	public ImageViewer(MainWindow myMainWindow) {
			
			this.myMainWindow = myMainWindow;
//			setSize(new Dimension(JFRAME_WIDTH, JFRAME_HEIGHT));			
//			setMinimumSize(new Dimension(JFRAME_WIDTH, JFRAME_HEIGHT));			
//			setMaximumSize(new Dimension(JFRAME_WIDTH, JFRAME_HEIGHT));			
			setLayout(new BorderLayout());
			
//		    getContentPane().setLayout(null);

			// Create a JPanel containing the Image
//		    myPanel = new JPanel();
		    myPanel = new JLayeredPane();
//		    myPanel.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
//		    myPanel.setLocation(0, 0);
//			BorderLayout layout = new BorderLayout();
//		    myPanel.setLayout(layout);

		    // the swing layout "OverlayLayout" allows to arrange
		    // components over the top of each other with some 
		    // nice management.
//		    OverlayLayout layout = new OverlayLayout(myPanel); 
//		    myPanel.setLayout(layout);
		    myPanel.setLayout(null);
			
		    // Try to get access to the file - pack it into a Swing component
		    try {
		    	System.out.println("Trying to get file from: "+this.myMainWindow.getSelectedFileUrl());
		    	myImageIcon = new ImageIcon(this.myMainWindow.getSelectedFileUrl());

//		    	myPanel.setSize(new Dimension(JFRAME_WIDTH, JFRAME_HEIGHT));
//		    	myPanel.setMinimumSize(new Dimension(JFRAME_WIDTH, JFRAME_HEIGHT));
//		    	myPanel.setMaximumSize(new Dimension(JFRAME_WIDTH, JFRAME_HEIGHT));

		    	myImageLabel = new JLabel(myImageIcon);
		    	
		    	myImageLabel.setSize(new Dimension(myImageIcon.getIconWidth(), myImageIcon.getIconHeight()));
		    	
		    	myPanel.setSize(new Dimension(myImageLabel.getWidth(), myImageLabel.getHeight()));
		    	myPanel.setMinimumSize(new Dimension(myImageLabel.getWidth(), myImageLabel.getHeight()));
		    	myPanel.setMaximumSize(new Dimension(myImageLabel.getWidth(), myImageLabel.getHeight()));

		    	//		    	myPanel.setPreferredSize(new Dimension(myImageIcon.getIconWidth(), myImageIcon.getIconHeight()));
//		    	myPanel.setMinimumSize(new Dimension(myImageIcon.getIconWidth(), myImageIcon.getIconHeight()));
//		    	myPanel.setMaximumSize(new Dimension(myImageIcon.getIconWidth(), myImageIcon.getIconHeight()));
//		    	myPanel.setVisible(true);
		    	
		    	// add some test markers
//				this.markArea(myPanel, 5, 5, 20, 20);
//				this.markArea(myPanel, 50, 50, 20, 20);
//				this.markArea(myPanel, 100, 100, 20, 20);
		    	
		    	// we will make a timer here to catch the
		    	// events of the nci brain:
		    	// image chunk processing for each step will
		    	// be shown as a rectangle on the image.

		    	myPanel.add(myImageLabel, new Integer(1));

//		    	Timer timer = new Timer(REFRESH_INTERVAL, new ActionListener() {
//		    		int i = 0;
//		    		int j = 2;
//		    		public void actionPerformed(ActionEvent evt) {
//						if (true) {
//							markArea(myPanel, i, i, 20, 20, j);
//							i += 5;
//							j++;
//							repaint();
//						}
//					}
//		    		
//		    	});
//		    	timer.start();
		    	
		    	// pack the ImageIcon into a JLabel - that's the way with swing		    	
		    	
//			    myPanel.setVisible(true);

			    // add the panel containing the Image to the JFrame
//			    getContentPane().setSize(myImageIcon.getIconWidth(), myImageIcon.getIconHeight());

//		    	myImageLabel.setSize(new Dimension(JFRAME_WIDTH, JFRAME_HEIGHT));
//		    	myImageLabel.setMinimumSize(new Dimension(JFRAME_WIDTH, JFRAME_HEIGHT));
//		    	myImageLabel.setMaximumSize(new Dimension(JFRAME_WIDTH, JFRAME_HEIGHT));
//		    	myImageLabel.setLocation(new Point(0, 0));


//			    setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
			    setTitle("Image viewer");

		    	setSize(new Dimension(myPanel.getWidth() + JFRAME_BORDER_SIZE, myPanel.getHeight() + JFRAME_BORDER_SIZE));
		    	setMinimumSize(new Dimension(myPanel.getWidth() + JFRAME_BORDER_SIZE, myPanel.getHeight() + JFRAME_BORDER_SIZE));
		    	setMaximumSize(new Dimension(myPanel.getWidth() + JFRAME_BORDER_SIZE, myPanel.getHeight() + JFRAME_BORDER_SIZE));

			    getContentPane().add(myPanel, BorderLayout.CENTER);

				setResizable(false);
				
//				setLocationRelativeTo(myMainWindow); // will center the window according to the position of the MainWindow

				// move the window a little
//				setLocation(this.getX() - 100, this.getY() - 100);
				
//			    setVisible(true);

				this.setAlwaysOnTop(true);
				this.setVisible(true);
		    	
		    } catch (Exception ex) {
		    	System.err.println("Error loading Image.");
		    	JOptionPane.showMessageDialog(this.myMainWindow, "No image to display - Please select some file(s).");
		    	
		    }
		    
		  }  

	public void drawChunkMask(int posX, int posY, int sizeX, int sizeY) {
//	public void markArea(JLayeredPane myPanel, int posX, int posY, int sizeX, int sizeY, int depth) {
//		JLabel myArea = new JLabel();
//		myArea.setText(markerText);
	
		BufferedImage myMarker = new BufferedImage(sizeX+4, sizeY+4, BufferedImage.TYPE_INT_ARGB); // image with alpha (transparency)
//		BufferedImage myMarker = new BufferedImage(sizeX+4, sizeY+4, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g2d = myMarker.createGraphics();
//		g2d.setComposite(new AlphaComposite());

//		g2d.setColor(Color.RED);
//		g2d.setBackground(Color.BLACK);
		
		g2d.setColor(new Color(1f, 0f, 0f, 1f)); // red, non-transparent
		g2d.setBackground(new Color(0f, 0f, 0f, 0f)); // black, half-transparent

		g2d.drawRect(0, 0, myMarker.getWidth()-1, myMarker.getHeight()-1);
		g2d.drawRect(1, 1, myMarker.getWidth()-3, myMarker.getHeight()-3);
		
//		myArea.setLocation(posX, posY); // not working...

//		myArea.setSize(new Dimension(50, 50));
//		myArea.setMinimumSize(new Dimension(50, 50));
//		myArea.setMaximumSize(new Dimension(50, 50));

		
		
//		myArea.setAlignmentX(0f);
//		myArea.setAlignmentY(0f);
//		myArea.setLocation(posX, posY);
//		myArea.setLocation(new Point(posX, posY));
	
		ImageIcon myMarkerIcon = new ImageIcon(myMarker);
		JLabel myMarkerLabel = new JLabel(myMarkerIcon);

		myMarkerLabel.setSize(new Dimension(sizeX+4, sizeY+4));
		myMarkerLabel.setMinimumSize(new Dimension(sizeX+4, sizeY+4));
		myMarkerLabel.setMaximumSize(new Dimension(sizeX+4, sizeY+4));
		myMarkerLabel.setLocation(new Point(posX-2, posY-2));
		
		lastLayer ++;
		myPanel.add(myMarkerLabel, new Integer(lastLayer));
		myMarkerLabel.setVisible(true);

		repaint();
		
//		if (true) {
//		markArea(myPanel, i, i, 20, 20, j);
//		i += 5;
//		j++;
//		repaint();
//	}

	}

}
