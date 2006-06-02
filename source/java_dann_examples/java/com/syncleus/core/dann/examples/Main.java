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

package com.syncleus.core.dann.examples;

import java.io.*;

public class Main
{	
	public static void main(String args[])
	{
		try
		{
			BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("dANN Example Sets");

			int currentCommand = 'q';
			do
			{
				boolean received = false;
				while( received == false )
				{
					System.out.println();
					System.out.println("X) XOR Example");
					System.out.println("Q) quit");
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

				switch( currentCommand )
				{
					case 'x':
						com.syncleus.core.dann.examples.xor.Main.main(args);
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
}
