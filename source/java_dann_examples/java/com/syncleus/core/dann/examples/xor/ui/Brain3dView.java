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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.media.j3d.Alpha;
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
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Primitive;
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
	private final int JFRAME_WIDTH = 800;
	private final int JFRAME_HEIGHT = 600;
	private MainWindow myMainWindow;

    // Define colors
	private Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
	private Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
	private Color3f red = new Color3f(0.80f, 0.20f, 0.2f);
	private Color3f ambient = new Color3f(0.25f, 0.25f, 0.25f);
	private Color3f diffuse = new Color3f(0.7f, 0.7f, 0.7f);
	private Color3f specular = new Color3f(0.9f, 0.9f, 0.9f);
	private Color3f ambientRed = new Color3f(0.2f, 0.05f, 0.0f);
	private Color3f bgColor = new Color3f(0.05f, 0.05f, 0.2f);

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

	    // Set the initial view position
	    TransformGroup tgView = universe.getViewingPlatform().getViewPlatformTransform();
	    Transform3D transformView = new Transform3D();
	    transformView.set(1f, new Vector3f(0f, 0f, 10f));
	    tgView.setTransform(transformView);
	     
	    // add an orbital mouse control to the scene
	    OrbitBehavior myOrbital = new OrbitBehavior(myCanvas3D);
	    myOrbital.setRotationCenter(new Point3d(0f, 0f, -2f));
	    myOrbital.setReverseRotate(true);
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
	    Vector3f dir = new Vector3f(-1f, -1f, -1f);
	    DirectionalLight dirLight = new DirectionalLight(white, dir);
	    dirLight.setInfluencingBounds(bounds);
	    branchGroup.addChild(dirLight);

//	    Sphere myNeuron1 = new Sphere(1.0f);
	    float positionsX[] = {
	    		-2f,-1f,
	    		0f,1f,2f
	    };

	    float positionsY[] = {
	    		-2f,0f,2f
	    };
	    float positionsZ[] = {
	    		-1f, 0f,1f
	    };

//	    branchGroup.addChild(this.createSphere(-10f, 0.5f, 0.0f, 1f));
	    int k = 1;
	    int l = 1;
	    for (int i = 1; i < 4; i++) {
	    	// add one sphere (a neuron) to the 3d scene
	    	branchGroup.addChild(this.createNeuronSphere("Input", "neuron "+k, Color.BLUE, positionsX[i],positionsY[0],positionsZ[1], 0.4f));
	    	k++;
	    }

	    for (int i = 0; i < 5; i++) {
	    	for (int j = 0; j < 4; j+=2) {
	    		// add one sphere (a neuron) to the 3d scene
	    		branchGroup.addChild(this.createNeuronSphere("Processing", "neuron "+l, Color.RED, positionsX[i],positionsY[1],positionsZ[j], 0.4f));
	    		l++;
	    	}
	    }

	    for (int i = 2; i < 3; i++) {
	    	// 	add one sphere (a neuron) to the 3d scene
	    	branchGroup.addChild(this.createNeuronSphere("Output", "neuron", Color.GREEN, positionsX[i],positionsY[2],positionsZ[1], 0.4f));
	    }
	    
	    return branchGroup;
	    
	  }
	  
	  public TransformGroup createNeuronSphere(String textLine1, String textLine2, Color myColor, float posX, float posY, float posZ, float radius) {
		  
	  	// Create the transform group node holding the sphere
	    TransformGroup myTransformGroup = new TransformGroup();
	    myTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    myTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    Transform3D myTransform = new Transform3D();
	    myTransform.set(1f, new Vector3f(posX, posY, posZ));
	    myTransformGroup.setTransform(myTransform);
	    
	    // Create a "metal" appearance
//	    Material metal = new Material(ambient, black, diffuse, specular, 110.f);
//	    metal.setLightingEnable(true);
//	    Appearance metalAppearance = new Appearance();
//	    metalAppearance.setMaterial(metal);
//	    myNeuron.setAppearance(metalAppearance);
	    
	    // check the texture generation
//	    JFrame myCheckFrame = new JFrame();
//	    myCheckFrame.setSize(new Dimension(600,600));
//	    BufferedImage myCheckImage = createNeuronTextureImage("test", Color.RED);
//	    JLabel myCheckImLabel = new JLabel();
//	    ImageIcon myCheckImIcon = new ImageIcon(myCheckImage);
//	    myCheckImLabel.setIcon(myCheckImIcon);
//	    myCheckFrame.add(myCheckImLabel);
//	    myCheckFrame.setVisible(true);
	    
	    // create a nice texture image for the 3d sphere
	    BufferedImage myTextureImage = createNeuronTextureImage(textLine1, textLine2, myColor);
	    Appearance myAppearance = makeMappingFromImage(myTextureImage);
//	    myNeuron.setAppearance(myAppearance);

//	    Sphere myNeuron = new Sphere(radius);
//	    Sphere myNeuron = new Sphere(radius, Primitive.GENERATE_TEXTURE_COORDS, 500, myAppearance); // very nice sphere with 500 shapes, but slow animation (CPU overload)
	    Sphere myNeuron = new Sphere(radius, Primitive.GENERATE_TEXTURE_COORDS, 100, myAppearance); // animation ok on p4 2GHz and radeon X1600 GPU
//	    Sphere myNeuron = new Sphere(radius, Primitive.GENERATE_TEXTURE_COORDS, 10, myAppearance); // ugly spheres! 

	    Alpha myAlpha = new Alpha(-1, 5000);
	    Transform3D yAxis = new Transform3D();
	    TransformGroup myTgRot = new TransformGroup();
	    myTgRot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    myTgRot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    
//	    RotationInterpolator myRot = new RotationInterpolator(myAlpha, myTgRot, yAxis, 0.0f, (float)Math.PI * 2.0f);
	    RotationInterpolator myRot = new RotationInterpolator(myAlpha, myTgRot, yAxis, (float)Math.PI * 2.0f, 0.0f);

//	    RotationInterpolator myRot = new RotationInterpolator(myAlpha, myTgRot);

	    BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0), 10000f);
	    myRot.setSchedulingBounds(bounds);
	    
	    myTgRot.addChild(myRot);
	    myTgRot.addChild(myNeuron);
	    myTransformGroup.addChild(myTgRot);
	  
	    return myTransformGroup;
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
				  textLine1, String textLine2, Color myColor) {

		  int imSizeX = 256; // high quality for now - we will optimize later
		  int imSizeY = 128;
		  
		  BufferedImage myNeuronTextureImage = new BufferedImage(imSizeX, imSizeY, BufferedImage.TYPE_INT_RGB);

		  Graphics2D g2d = myNeuronTextureImage.createGraphics(); // creates a Graphics2D to draw in the BufferedImage
		  g2d.setBackground(myColor);
		  g2d.clearRect(0, 0, myNeuronTextureImage.getWidth(), myNeuronTextureImage.getHeight());
		  
		  int tempFontSize = 32; // high quality for now
		  
		  g2d.setFont(new Font("Arial",Font.BOLD,tempFontSize));
		  g2d.setColor(Color.BLACK);
		  FontMetrics fm = g2d.getFontMetrics();
		  g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		  	
		  int tempStringWidth1 = fm.stringWidth(textLine1);
		  int tempTextPosX1 = Math.round(imSizeX/2 - tempStringWidth1/2);
//		  int tempTextPosX = 100;
		  g2d.drawString(textLine1, tempTextPosX1, 60);

		  int tempStringWidth2 = fm.stringWidth(textLine1);
		  int tempTextPosX2 = Math.round(imSizeX/2 - tempStringWidth2/2);

		  g2d.drawString(textLine2, tempTextPosX2, 90);
		  
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
    		  System.out.println("From mipmapping in Brain3dView: image: Auto-generated image - width:"+imageWidth);
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
