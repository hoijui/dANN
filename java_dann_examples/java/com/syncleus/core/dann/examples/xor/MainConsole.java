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
package com.syncleus.core.dann.examples.xor;

import java.io.*;
import com.syncleus.dann.Brain;
import com.syncleus.dann.DNA;


/**
 * @author Jeffrey Phillips Freeman
 */
public class MainConsole
{
	private static DNA myDNA = new DNA();
	private static Brain myBrain = null;
	private static BufferedReader inReader = null;
			
	public static void main(String args[])
	{
		try
		{
			inReader = new BufferedReader(new InputStreamReader(System.in));
			myBrain = new Brain(myDNA, 3, 1);
        
			//creates the neurons in the brain.
			myBrain.AddLayerAfterInput(10);
			myBrain.AddLayerAfterInput(10);
			
			//connects all the neurons in the brain in a feedforward style.
			myBrain.ConnectAllFeedForward();

			System.out.println("dANN nXOR Example");
			System.out.println();

			int currentCommand = 'q';
			do
			{
				System.out.println("d) display current circuit pin-out");
				System.out.println("t) train the current circuit");
				System.out.println("q) quit");
				System.out.print("\tEnter command: ");
				currentCommand = inReader.readLine().toLowerCase().toCharArray()[0];
				System.out.println();
				
				switch( currentCommand )
				{
					case 'd':
						testOutput();
						break;
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
						System.out.println("Quiting...");
						break;
					default:
						System.out.println("Invalid command");
				}
			} while( (currentCommand != 'q')&&(currentCommand >= 0) );
		}
		catch(Exception caughtException)
		{
			throw new InternalError("Unhandled Exception: " + caughtException);
		}
	}
	
	private static void testOutput()
	{
        double[] curInput = {0, 0, 0};
        myBrain.SetCurrentInput(curInput);
        myBrain.PropogateOutput();
        double[] curOutput;
        curOutput = myBrain.GetCurrentOutput();
        System.out.println(curInput[0] + ", " + curInput[1] + ", " + curInput[2] + ":\t" + curOutput[0]);
		  
        curInput[0] = 1;
		  curInput[1] = 0;
		  curInput[2] = 0;
        myBrain.SetCurrentInput(curInput);
        myBrain.PropogateOutput();
        curOutput = myBrain.GetCurrentOutput();
        System.out.println(curInput[0] + ", " + curInput[1] + ", " + curInput[2] + ":\t" + curOutput[0]);
		  
        curInput[0] = 0;
		  curInput[1] = 1;
		  curInput[2] = 0;
        myBrain.SetCurrentInput(curInput);
        myBrain.PropogateOutput();
        curOutput = myBrain.GetCurrentOutput();
        System.out.println(curInput[0] + ", " + curInput[1] + ", " + curInput[2] + ":\t" + curOutput[0]);
		  
        curInput[0] = 0;
		  curInput[1] = 0;
		  curInput[2] = 1;
        myBrain.SetCurrentInput(curInput);
        myBrain.PropogateOutput();
        curOutput = myBrain.GetCurrentOutput();
        System.out.println(curInput[0] + ", " + curInput[1] + ", " + curInput[2] + ":\t" + curOutput[0]);
		  
        curInput[0] = 1;
		  curInput[1] = 1;
		  curInput[2] = 0;
        myBrain.SetCurrentInput(curInput);
        myBrain.PropogateOutput();
        curOutput = myBrain.GetCurrentOutput();
        System.out.println(curInput[0] + ", " + curInput[1] + ", " + curInput[2] + ":\t" + curOutput[0]);
		  
        curInput[0] = 0;
		  curInput[1] = 1;
		  curInput[2] = 1;
        myBrain.SetCurrentInput(curInput);
        myBrain.PropogateOutput();
        curOutput = myBrain.GetCurrentOutput();
        System.out.println(curInput[0] + ", " + curInput[1] + ", " + curInput[2] + ":\t" + curOutput[0]);
		  
        curInput[0] = 1;
		  curInput[1] = 0;
		  curInput[2] = 1;
        myBrain.SetCurrentInput(curInput);
        myBrain.PropogateOutput();
        curOutput = myBrain.GetCurrentOutput();
        System.out.println(curInput[0] + ", " + curInput[1] + ", " + curInput[2] + ":\t" + curOutput[0]);
		  
        curInput[0] = 1;
		  curInput[1] = 1;
		  curInput[2] = 1;
        myBrain.SetCurrentInput(curInput);
        myBrain.PropogateOutput();
        curOutput = myBrain.GetCurrentOutput();
        System.out.println(curInput[0] + ", " + curInput[1] + ", " + curInput[2] + ":\t" + curOutput[0]);
	}
	
	private static void train(int count)
	{
        for(int lcv = 0; lcv < count; lcv++)
        {
            double[] curInput = {0, 0, 0};
            double[] curTrain = {-1};
            myBrain.SetCurrentInput(curInput);
            myBrain.PropogateOutput();
            myBrain.SetCurrentTraining(curTrain);
            myBrain.BackPropogateTraining();
				
            curInput[0] = 1;
				curInput[1] = 0;
				curInput[2] = 0;
            curTrain[0] = 1;
            myBrain.SetCurrentInput(curInput);
            myBrain.PropogateOutput();
            myBrain.SetCurrentTraining(curTrain);
            myBrain.BackPropogateTraining();
				
            curInput[0] = 0;
				curInput[1] = 1;
				curInput[2] = 0;
            curTrain[0] = 1;
            myBrain.SetCurrentInput(curInput);
            myBrain.PropogateOutput();
            myBrain.SetCurrentTraining(curTrain);
            myBrain.BackPropogateTraining();
				
            curInput[0] = 0;
				curInput[1] = 0;
				curInput[2] = 1;
            curTrain[0] = 1;
            myBrain.SetCurrentInput(curInput);
            myBrain.PropogateOutput();
            myBrain.SetCurrentTraining(curTrain);
            myBrain.BackPropogateTraining();
				
            curInput[0] = 1;
				curInput[1] = 1;
				curInput[2] = 0;
            curTrain[0] = -1;
            myBrain.SetCurrentInput(curInput);
            myBrain.PropogateOutput();
            myBrain.SetCurrentTraining(curTrain);
            myBrain.BackPropogateTraining();
				
            curInput[0] = 0;
				curInput[1] = 1;
				curInput[2] = 1;
            curTrain[0] = -1;
            myBrain.SetCurrentInput(curInput);
            myBrain.PropogateOutput();
            myBrain.SetCurrentTraining(curTrain);
            myBrain.BackPropogateTraining();
				
            curInput[0] = 1;
				curInput[1] = 0;
				curInput[2] = 1;
            curTrain[0] = -1;
            myBrain.SetCurrentInput(curInput);
            myBrain.PropogateOutput();
            myBrain.SetCurrentTraining(curTrain);
            myBrain.BackPropogateTraining();
				
            curInput[0] = 1;
				curInput[1] = 1;
				curInput[2] = 1;
            curTrain[0] = -1;
            myBrain.SetCurrentInput(curInput);
            myBrain.PropogateOutput();
            myBrain.SetCurrentTraining(curTrain);
            myBrain.BackPropogateTraining();
        }
	}
}
