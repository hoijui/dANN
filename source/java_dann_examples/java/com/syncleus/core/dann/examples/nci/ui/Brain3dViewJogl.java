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

/**
 * 3d component of the graphical user interface based on java swing
 * for the nci example application of dANN.
 * -> "nci" stands for "Neural Compressed Image".
 * 
 * This class implements the jogl Window of the gui, to
 * be compared with the java3d version implemented in
 * parallel.
 * The nciBrain is visualized in 3d in a JFrame started
 * from the MainWindow class (main window of the gui)
 * 
 * <!-- Author: chickenf -->
 * @author chickenf
 */


import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Brain3dViewJogl extends JFrame implements GLEventListener {

	////////////////
	// CONFIG START
	
	// Size of the Window containing the 3d visualization
	private final int JFRAME_WIDTH = 500;
	private final int JFRAME_HEIGHT = 400;
	private JFrame myMainWindow;

	// CONFIG END
	////////////////

	
	private int gear1, gear2, gear3;
	
	public Brain3dViewJogl(JFrame myMainWindow) {
		
		this.myMainWindow = myMainWindow;

		// initialization: creation of a GLCanvas with JOGL
//		JOGLRenderer renderer = new JOGLRenderer();
//		GLCapabilities capabilities = new GLCapabilities();
//		GLCanvas myGLCanvas = GLDrawableFactory.getFactory().createGLCanvas(capabilities);
//		GLCanvas myGLCanvas = GLDrawableFactory.getFactory().
//		myGLCanvas.addGLEventListener(this);
		GLCanvas myGLCanvas = new GLCanvas();
		
		myGLCanvas.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
		myGLCanvas.setLocation(0, 0);

		
	    myGLCanvas.addGLEventListener(this);
//	    myGLCanvas.addKeyListener(this);
//	    myGLCanvas.addMouseListener(this);
//	    myGLCanvas.addMouseMotionListener(this);
	    
//	    GL gl = GLAutoDrawable.getGL();
	    
	    // Create the GLcanvas container.
	    JPanel myPanel = new JPanel();
	    myPanel.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
	    myPanel.setLocation(0, 0);
	    myPanel.setVisible(true);
	    myPanel.setLayout(null);

	    // Add the 3D canvas to the container.
	    myPanel.add(myGLCanvas);

	    // a the panel containing the 3D canvas to the JFrame
	    getContentPane().add(myPanel);

	    // Turn off the layout manager, widgets will be sized
	    // and positioned explicitly.
	    setLayout(null);
	    setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
	    setTitle("dANN Brain visulization (JOGL)");

	    // fix the window size for now...
		setResizable(false);
		
		this.setLocationRelativeTo(myMainWindow); // will center the window according to the position of the MainWindow

		// move the window a little
		this.setLocation(this.getX() - 190, this.getY() - 100);
		
	    setVisible(true);
	}

	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		
		gl.setSwapInterval(1);
		
	
	}

	public void display(GLAutoDrawable drawable) {
//		GL gl = drawable.getGL();
		
	}

	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
	}
}
