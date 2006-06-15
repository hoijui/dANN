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

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

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

public class PictureViewer extends JFrame {
		
	////////////////
	// CONFIG START

	// Size of the Window containing the 3d visualization
	private final int JFRAME_WIDTH = 600;
	private final int JFRAME_HEIGHT = 800;
	private JFrame myMainWindow;

	// CONFIG END
	////////////////

	public PictureViewer(JFrame myMainWindow) {
			
			this.myMainWindow = myMainWindow;
			
			// Create a JPanel containing the picture
		    JPanel myPanel = new JPanel();
		    myPanel.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
		    myPanel.setLocation(0, 0);
		    myPanel.setLayout(null);
		    myPanel.setVisible(true);
		    
		    // add the picture to the JPanel

		    // add the panel containing the picture to the JFrame
		    getContentPane().add(myPanel);

		    // Turn off the layout manager, widgets will be sized
		    // and positioned explicitly.
		    setLayout(null);
		    setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
		    setTitle("Picture viewer");

		    // fix the window size for now...
			setResizable(false);
			
			this.setLocationRelativeTo(myMainWindow); // will center the window according to the position of the MainWindow

			// move the window a little
			this.setLocation(this.getX() - 150, this.getY() - 100);
			
		    setVisible(true);
		  }  

}
