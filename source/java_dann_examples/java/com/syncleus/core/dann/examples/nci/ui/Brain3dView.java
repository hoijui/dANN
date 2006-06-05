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

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingLeaf;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PickRay;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;


/**
 * 3d component of the graphical user interface based on java swing
 * for the nci example application of dANN.
 * -> "nci" stands for "Neural Compressed Image".
 * 
 * This class implements the java3d Window of the gui.
 * The nciBrain is visualized in 3d in a JFram started
 * from the MainWindow class (main window of the gui)
 * 
 * <!-- Author: chickenf -->
 * @author chickenf
 */

public class Brain3dView extends JFrame {
	
	
	////////////////
	// CONFIG START

	// Size of the Window containing the 3d visualization
	private final int JFRAME_WIDTH = 800;
	private final int JFRAME_HEIGHT = 600;

	// CONFIG END
	////////////////

	public Brain3dView() {
		// Create a 3D graphics canvas.
	    Canvas3D canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
	    canvas3D.setSize(900, 700);
	    canvas3D.setLocation(0, 0);

	    // Create the scene branchgroup.
	    BranchGroup scene3D = createScene3D();
	    
	   // Create a universe with the Java3D universe utility.
	    SimpleUniverse universe = new SimpleUniverse(canvas3D);
	    universe.addBranchGraph(scene3D);
	    
	    // Add a pick behavior
//	    addPick(scene3D);
	 
	    // Use parallel projection.
	    View view = universe.getViewer().getView();
//	    view.setProjectionPolicy(View.PARALLEL_PROJECTION);

	    // Set the universe Transform3D object.
	    TransformGroup tg = universe.getViewingPlatform().getViewPlatformTransform();
	    Transform3D transform = new Transform3D();
	    transform.set(1f, new Vector3f(2.0f, 2.0f, 10.0f));
	    tg.setTransform(transform);

	    // Create the canvas container.
	    JPanel myPanel = new JPanel();
	    myPanel.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
	    myPanel.setLocation(0, 0);
	    myPanel.setVisible(true);
	    myPanel.setLayout(null);

	    // Add the 3D canvas to the container.
	    myPanel.add(canvas3D);

	    // a the panel containing the 3D canvas to the JFrame
	    getContentPane().add(myPanel);

	    // Turn off the layout manager, widgets will be sized
	    // and positioned explicitly.
	    setLayout(null);
	    setSize(900, 700);
	    setTitle("dANN Brain visulisation");
	    setVisible(true);
	  }  
	    
	  public BranchGroup createScene3D() {

	    // Define colors
	    Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
	    Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
	    Color3f red = new Color3f(0.80f, 0.20f, 0.2f);
	    Color3f ambient = new Color3f(0.25f, 0.25f, 0.25f);
	    Color3f diffuse = new Color3f(0.7f, 0.7f, 0.7f);
	    Color3f specular = new Color3f(0.9f, 0.9f, 0.9f);
	    Color3f ambientRed = new Color3f(0.2f, 0.05f, 0.0f);
	    Color3f bgColor = new Color3f(0.05f, 0.05f, 0.2f);

	    // Create the branch group
	    BranchGroup branchGroup = new BranchGroup();

	    // Create the bounding leaf node
	    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
	        1000.0);
	    BoundingLeaf boundingLeaf = new BoundingLeaf(bounds);
	    branchGroup.addChild(boundingLeaf);

	    // Create the background
	    Background bg = new Background(bgColor);
	    bg.setApplicationBounds(bounds);
	    branchGroup.addChild(bg);

	    // Create the ambient light
	    AmbientLight ambLight = new AmbientLight(white);
	    ambLight.setInfluencingBounds(bounds);
	    branchGroup.addChild(ambLight);

	    // Create the directional light
	    Vector3f dir = new Vector3f(-1.0f, -1.0f, -1.0f);
	    DirectionalLight dirLight = new DirectionalLight(white, dir);
	    dirLight.setInfluencingBounds(bounds);
	    branchGroup.addChild(dirLight);

	    // Create the pole appearance
	    Material poleMaterial = new Material(ambient, black, diffuse, specular,
	        110.f);
	    poleMaterial.setLightingEnable(true);
	    Appearance poleAppearance = new Appearance();
	    poleAppearance.setMaterial(poleMaterial);

	    // Create the transform group node
	    TransformGroup transformGroup1 = new TransformGroup();
	    transformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    transformGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    Transform3D transform1 = new Transform3D();
	    transform1.set(1f, new Vector3f(-2.0f, 0.0f, -10.0f));
	    transformGroup1.setTransform(transform1);

	    TransformGroup transformGroup2 = new TransformGroup();
	    transformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    transformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    Transform3D transform2 = new Transform3D();
	    transform2.set(1f, new Vector3f(2.0f, 0.0f, -10.0f));
	    transformGroup2.setTransform(transform2);
	    
	    ColorCube myCube1 = new ColorCube(0.4);
	    ColorCube myCube2 = new ColorCube(0.4);

//	    Box myBox = new Box(10f, 10f, 10f, poleMaterial);
//	    transformGroup.addChild(myBox);
	    
	    transformGroup1.addChild(myCube1);
	    transformGroup2.addChild(myCube2);
	    
	    branchGroup.addChild(transformGroup1);
	    branchGroup.addChild(transformGroup2);

	    // Create the poles
//	    Poles poles = new Poles(poleAppearance);
//	    transformGroup.addChild(poles.getChild());

	    // Add the position markers to the transform group
//	    transformGroup.addChild(positions.getChild());

	    // Let the positions object know about the transform group
//	    positions.setTransformGroup(transformGroup);

	    return branchGroup;
	    
	  }
	  
	  // nothing can be picked on the Brain3dView for now
//	  public void addPick(BranchGroup myBranchGroup) {
////	    PickRay myRay = new PickRay( origin, direction );
//	    PickRay myRay = new PickRay();
//	    SceneGraphPath[] pickResults = myBranchGroup.pickAllSorted(myRay);
//	    Node pickedObject = pickResults[0].getObject();
//	  }
	  

}
