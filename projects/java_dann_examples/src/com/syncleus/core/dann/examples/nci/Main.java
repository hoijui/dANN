/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                    *
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

package com.syncleus.core.dann.examples.nci;

import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Console entry point.<BR>
 * random number generator.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 */
public class Main
{
	/**
	 * random number generator.<BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
	private static final Random random = new Random();
	
	/**
	 * this is the neural component. It processes an image in small chunks.<BR>
	 * random number generator.<BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
	private static NciBrain brain = null;

	/**
	 * A list of *.bmp files to be used to train the brain.<BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
	private static File[] trainingImages = null;
	
	/**
	 * Not used.<BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
        
	private static File compressIn = null;
	private static File compressOut = null;
	private static File uncompressIn = null;
	private static File uncompressOut = null;
	private static File save = null;
	private static File load = null;
	
	/**
	 * The x component (width) of the brain's input and output image in pixles.
	 * <BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
	private static final int x = 10;
	
	/**
	 * The y component (height) of the brain's input and output image in pixles.
	 * <BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
	private static final int y = 10;
	
	/**
	 * The entry Point for the NCI console.<BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 0.1
	 */
	public static void main(String args[])
	{
		try
		{
			//a nice buffered text reader to use for the input stream. Don't close it!
			BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
			
			
			
			//obtains the command line arguments
			String trainDirArgument = "/img/";
			String saveFileArgument = "default.dann";
			String loadFileArgument = "default.dann";
			String compressInArgument = "in.bmp";
			String compressOutArgument = "out.nci";
			String uncompressInArgument = "out.nci";
			String uncompressOutArgument = "out.bmp";

			for( int lcv = 0; lcv < args.length; lcv++ )
			{
				String trainDirIdentifier = "--train-dir=";
				String saveFileIdentifier = "--save=";
				String loadFileIdentifier = "--load=";
				String compressInIdentifier = "--compress-in=";
				String compressOutIdentifier = "--compress-out=";
				String uncompressInIdentifier = "--uncompress-in=";
				String uncompressOutIdentifier = "--uncompress-out=";
				String currentArg = args[lcv];
				if( currentArg.startsWith(trainDirIdentifier) == true )
				{
					trainDirArgument = currentArg.substring(trainDirIdentifier.length());
				}
				else if( currentArg.startsWith(saveFileIdentifier) == true )
				{
					saveFileArgument = currentArg.substring(saveFileIdentifier.length());
				}
				else if( currentArg.startsWith(loadFileIdentifier) == true )
				{
					loadFileArgument = currentArg.substring(loadFileIdentifier.length());
				}
				else if( currentArg.startsWith(compressInIdentifier) == true )
				{
					compressInArgument = currentArg.substring(compressInIdentifier.length());
				}
				else if( currentArg.startsWith(compressOutIdentifier) == true )
				{
					compressOutArgument = currentArg.substring(compressOutIdentifier.length());
				}
				else if( currentArg.startsWith(uncompressInIdentifier) == true )
				{
					uncompressInArgument = currentArg.substring(uncompressInIdentifier.length());
				}
				else if( currentArg.startsWith(uncompressOutIdentifier) == true )
				{
					uncompressOutArgument = currentArg.substring(uncompressOutIdentifier.length());
				}
			}
			
			
			
			//set the various files we will be using based on the command line
			//arguments
			compressIn = new File(compressInArgument);
			compressOut = new File(compressOutArgument);
			uncompressIn = new File(uncompressInArgument);
			uncompressOut = new File(uncompressOutArgument);
			load = new File(loadFileArgument);
			save = new File(saveFileArgument);
			
			//obtain all the images to be used as training data using the directory
			//specified ont he command line.
			File trainDirectory = new File(trainDirArgument);
			File[] subTrainDirectory = trainDirectory.listFiles();
			ArrayList<File> trainImagesList = new ArrayList<File>();
			//use only files with a .bmp extention
			for( File currentFile : subTrainDirectory )
			{
				if( currentFile.isFile() )
				{
					String currentExtention = currentFile.getCanonicalPath().substring(currentFile.getCanonicalPath().lastIndexOf(".") + 1).toLowerCase();
					if( currentExtention.compareTo("bmp") == 0 )
					{
						trainImagesList.add(currentFile);
					}
				}
			}
			trainingImages = new File[trainImagesList.size()];
			trainImagesList.toArray(trainingImages);
			
			
			//ok now that were all configured lets get down to buisness			
			System.out.println("dANN Image Compression Example");

			
			
			//create the brain, this takes care of creating all the neurons and
			//everything.
			System.out.println();
			System.out.println("creating brain...");
			brain = new NciBrain(0.5, x, y, true);
			System.out.println("brain created!");

			
			
			//now lets just keep getting user input and take the apropriate action
			int currentCommand = 'q';
			do
			{
				boolean received = false;
				while( received == false )
				{
					System.out.println();
					System.out.println("C) Compress");
					System.out.println("U) Uncompress");
					System.out.println("T) Train");
					System.out.println("Q) Quit");
					System.out.print("\tEnter command: ");
				
					received = true;
					try
					{
						currentCommand = inReader.readLine().toLowerCase().toCharArray()[0];
					}
					catch(ArrayIndexOutOfBoundsException caughtException)
					{
						received = false;
					}
				}
				
				System.out.println();

				switch( currentCommand )
				{
					case 't':
						System.out.print("How many training cycles [Default: 1000]: ");
						int cycles = 1000;
						try
						{
							cycles = Integer.parseInt(inReader.readLine());
						}
						catch(NumberFormatException caughtException)
						{
						}
						System.out.println();
						train(cycles);
						System.out.println("Training Complete!");
						break;
					case 'q':
						break;
					default:
						System.out.println("Invalid command");
				}
			} while( (currentCommand != 'q')&&(currentCommand >= 0) );
		}
		catch(Exception caughtException)
		{
			caughtException.printStackTrace();
			System.out.println();
			throw new InternalError("CaughtException: " + caughtException);
		}
	}
	
	/**
	 * Trains the brain off of the trainingImages.<BR>
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @param cycles number of times to train.
	 * @since 0.1
	 * @see com.syncleus.core.dann.examples.nci.Main#trainingImages
	 */
	private static void train(int cycles) throws IOException
	{
		//this is a buffer that holds the current image we will use for training.
		BufferedImage currentTrainImage = null;
		
		//tell the brain to learn when we compress. this should only be set to
		//true during training.
		brain.setLearning(true);
		for( int lcv = 0; lcv < cycles; lcv++)
		{
			//obtain a random buffered image from the choice of images int he training directory
			currentTrainImage = ImageIO.read(trainingImages[random.nextInt(trainingImages.length)]);

			//select a random subsection of the image of the proper size for our
			//compression. We want an image no larger then x wide and y high.
			currentTrainImage = currentTrainImage.getSubimage(random.nextInt(currentTrainImage.getWidth() - x), random.nextInt(currentTrainImage.getHeight() - y), x, y);

			//now train the image
			brain.compress(currentTrainImage);
			
			//tell the user how many training cycles have completed.
			System.out.print("\rcycle: " + lcv + " complete: " + ((int) (((double)lcv)/((double)cycles) * 100.0)) + "%");
		}
		System.out.println();
		
		//we are done learning.
		brain.setLearning(false);
	}
}
