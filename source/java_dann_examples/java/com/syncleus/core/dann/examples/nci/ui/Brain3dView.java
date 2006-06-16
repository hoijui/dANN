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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingLeaf;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PickRay;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * 3d component of the graphical user interface based on java swing
 * for the nci example application of dANN.
 * -> "nci" stands for "Neural Compressed Image".
 * 
 * This class implements the java3d Window of the gui.
 * The nciBrain is visualized in 3d in a JFrame started
 * from the MainWindow class (main window of the gui)
 * 
 * <!-- Author: chickenf -->
 * @author chickenf
 */

public class Brain3dView extends JFrame {
	
	
	////////////////
	// CONFIG START

	// Size of the Window containing the 3d visualization
	private final int JFRAME_WIDTH = 500;
	private final int JFRAME_HEIGHT = 400;
	private MainWindow myMainWindow;

	// CONFIG END
	////////////////

	public Brain3dView(MainWindow myMainWindow) {
		
		this.myMainWindow = myMainWindow;
		
		// Create a 3D graphics canvas.
	    Canvas3D myCanvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
	    myCanvas3D.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
	    myCanvas3D.setLocation(0, 0);

	    // Create the scene branchgroup.
	    BranchGroup scene3D = createScene3D();
	    
	   // Create a universe with the Java3D universe utility.
	    SimpleUniverse universe = new SimpleUniverse(myCanvas3D);
	    universe.addBranchGraph(scene3D);
	    
	    // Add a pick behavior
//	    addPick(scene3D);
	 
	    // Use parallel projection.
	    View view = universe.getViewer().getView();
//	    view.setProjectionPolicy(View.PARALLEL_PROJECTION);

	    // Set the view Transform3D object.
	    TransformGroup tgView = universe.getViewingPlatform().getViewPlatformTransform();
	    Transform3D transformView = new Transform3D();
	    transformView.set(1f, new Vector3f(0f, 0f, 0f));
	    tgView.setTransform(transformView);
	     
	    // add an orbital mouse control to the scene
	    OrbitBehavior myOrbital = new OrbitBehavior(myCanvas3D);
	    myOrbital.setRotationCenter(new Point3d(0f, 0f, -10f));
	    myOrbital.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.POSITIVE_INFINITY));
	    universe.getViewingPlatform().setViewPlatformBehavior(myOrbital);

	    // a zoom control with the mouse wheel
	    MouseWheelZoom myMouseWheelZ = new MouseWheelZoom(tgView);
	    
	    // Create the canvas container.
	    JPanel myPanel = new JPanel();
	    myPanel.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
	    myPanel.setLocation(0, 0);
	    myPanel.setVisible(true);
	    myPanel.setLayout(null);

	    // Add the 3D canvas to the container.
	    myPanel.add(myCanvas3D);

	    // add the panel containing the 3D canvas to the JFrame
	    getContentPane().add(myPanel);

	    // Turn off the layout manager, widgets will be sized
	    // and positioned explicitly.
	    setLayout(null);
	    setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
	    setTitle("dANN Brain visulisation (Java3D)");

	    // fix the window size for now...
		setResizable(false);
		
		this.setLocationRelativeTo(myMainWindow); // will center the window according to the position of the MainWindow

		// move the window a little
		this.setLocation(this.getX() - 220, this.getY() - 150);
		
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
	    transform1.set(1f, new Vector3f(-2f, 0.0f, -10.0f));
	    transformGroup1.setTransform(transform1);

	    TransformGroup transformGroup2 = new TransformGroup();
	    transformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    transformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    Transform3D transform2 = new Transform3D();
	    transform2.set(1f, new Vector3f(2f, 0.0f, -10.0f));
	    transformGroup2.setTransform(transform2);
	    
	    //ColorCube myCube1 = new ColorCube(0.4);
	    //ColorCube myCube2 = new ColorCube(0.4);
	    Sphere myNeuron1 = new Sphere(1.0f);
	    
//	    myNeuron1.setAppearance(this.makeMappingFromImage(this.createNeuronTextureImage("Neuron 1", Color.RED)));
	    
	    Sphere myNeuron2 = new Sphere(1.0f);
	    
//	    Box myBox = new Box(10f, 10f, 10f, poleMaterial);
//	    transformGroup.addChild(myBox);
	    
	    transformGroup1.addChild(myNeuron1);
	    transformGroup2.addChild(myNeuron2);
	    
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
	  
//	  public BufferedImage createNeuronTextureImage(String
//			  myName, String myDescription1, String myDescription2, String m
//			 yDescription3, String myDescription4, Color myColor) {

	  public BufferedImage createNeuronTextureImage(String
				  myNeuronName, Color myColor) {

		  int imSizeX = 512; // high quality for now
		  int imSizeY = 512;
		  
		  BufferedImage myNeuronTextureImage = new BufferedImage(imSizeX, imSizeY, BufferedImage.TYPE_INT_RGB);

		  Graphics2D g2d = myNeuronTextureImage.createGraphics(); // creates a Graphics2D to draw in the BufferedImage
		  g2d.setBackground(myColor);
		  g2d.clearRect(0, 0, myNeuronTextureImage.getWidth(), myNeuronTextureImage.getHeight());
		  
		  int tempFontSize = 128; // high quality for now
		  
		  g2d.setFont(new Font("Courrier",Font.BOLD,tempFontSize));
		  g2d.setColor(Color.BLACK);
		  FontMetrics fm = g2d.getFontMetrics();
		  g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		  	
		  int tempStringWidth = fm.stringWidth(myNeuronName);
		  int tempTextPosX = Math.round(imSizeX/2 - tempStringWidth/2);
		  g2d.drawString(myNeuronName, tempTextPosX, 200);
		
		  g2d.drawImage(myNeuronTextureImage, null, 0, 0);
		  return(myNeuronTextureImage);

	  }
	  
      public Appearance makeMappingFromImage(BufferedImage myImage) {
    	  Appearance mapping = new Appearance();

    	  mapping.setCapability(Appearance.ALLOW_MATERIAL_READ);
    	  mapping.setCapability(Appearance.ALLOW_MATERIAL_WRITE);

    	  TextureLoader loader = new TextureLoader(myImage, TextureLoader.GENERATE_MIPMAP);
    	  ImageComponent2D image = loader.getImage();

    	  int imageWidth = image.getWidth();
    	  int imageHeight = image.getHeight();

    	  Texture2D texture = new Texture2D(Texture.MULTI_LEVEL_MIPMAP,Texture.RGB, imageWidth, imageHeight);

    	  // Mipmapping of the texture -- WARNING: original picture sizes have to be ^2 (e.g. 1024x512)
    	  int imageLevel = 0;
    	  texture.setImage(imageLevel, image);

    	  while (imageWidth > 1 || imageHeight > 1) {
    		  imageLevel++;
    		  if (imageWidth > 1) imageWidth /= 2;
    		  if (imageHeight > 1) imageHeight /= 2;
    		  image = loader.getScaledImage(imageWidth, imageHeight);
    		  texture.setImage(imageLevel, image);
    		  System.out.println("From mipmapping in Container: image: Auto-generated image - width:"+imageWidth);
    	  }

    	  // Texture quality
    	  texture.setMagFilter(Texture.BASE_LEVEL_LINEAR); //nice!
    	  texture.setMinFilter(Texture.MULTI_LEVEL_LINEAR); //nice!
    	  
    	  mapping.setTexture(texture);

    	  ////////
    	  // Nicer appearance:

//    		                // Coloring Attributes
//    		                // Intrinsic color, Gouraud shading
//    		                ColoringAttributes ca = new ColoringAttributes();
//    		                ca.setColor(1.0f, 1.0f, 0.0f);
//    		                ca.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
//    		                mapping.setColoringAttributes(ca);
    		  //
    		  //
//    		                // Material Attributes
//    		                // Ambient, emissive, diffuse, and specular colors
//    		                Material mat = new Material();
//    		                mat.setAmbientColor(0.3f, 0.3f, 0.3f);
//    		                mat.setDiffuseColor(1.0f, 0.0f, 0.0f);
//    		                mat.setEmissiveColor(0.0f, 0.0f, 0.0f);
//    		                mat.setSpecularColor(1.0f, 1.0f, 1.0f);
//    		                mat.setShininess(80.0f);
//    		                mat.setCapability(Material.ALLOW_COMPONENT_READ);
//    		                mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
//    		                mat.setLightingEnable(true);
//    		                mapping.setMaterial(mat);
    		  //
//    		                // Transparency Attributes
//    		                // Semi-transparent, alpha-blended
//    		                TransparencyAttributes ta = new TransparencyAttributes();
//    		                ta.setTransparency(0.01f);
//    		                ta.setTransparencyMode(TransparencyAttributes.BLENDED);
//    		                mapping.setTransparencyAttributes(ta);
    		  //
    		  //
//    		                // Point Attributes
//    		                // 10 pixel points, not anti-aliased
//    		                PointAttributes pta = new PointAttributes();
//    		                pta.setPointSize(10.0f);
//    		                pta.setPointAntialiasingEnable(false);
//    		                mapping.setPointAttributes(pta);
    		  //
//    		                // Line Attributes
//    		                // 10 pixel lines, solid, not anti-aliased
//    		                LineAttributes lta = new LineAttributes();
//    		                lta.setLineWidth(10.0f);
//    		                lta.setLineAntialiasingEnable(true);
////    		              lta.setLineAntialiasingEnable(false);
//    		                lta.setLinePattern(LineAttributes.PATTERN_SOLID);
//    		                mapping.setLineAttributes(lta);
    		  //
//    		                // Polygon Attributes
//    		                // Filled polygons, front and back faces
//    		                PolygonAttributes pa = new PolygonAttributes();
////    		              pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);
////    		              pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
////    		              pa.setCullFace(PolygonAttributes.CULL_NONE);
//    		                mapping.setPolygonAttributes(pa);

    		                  //
    		                  ////////

    	  return mapping;

      }



}
