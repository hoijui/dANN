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

            String[] newArgs = null;
            if (args.length > 1)
            {
                newArgs = new String[args.length - 1];
                for (int index = 1; index < args.length; index++)
                    newArgs[index - 1] = args[index];
            }
            else
                newArgs = new String[0];

            String selectorArg = null;
            if (args.length > 0)
                selectorArg = args[0];

            if (selectorArg != null)
            {
                if (selectorArg.compareTo("--xor") == 0)
                    com.syncleus.core.dann.examples.xor.Main.main(newArgs);
                else if (selectorArg.compareTo("--nci") == 0)
                    com.syncleus.core.dann.examples.nci.ui.NciDemo.main(newArgs);

                return;
            }

            System.out.println("dANN Example Sets");

            int currentCommand = 'q';
            do
            {
                boolean received = false;
                while (received == false)
                {
                    System.out.println();
                    System.out.println("X) XOR Example");
                    System.out.println("I) Image Compression Example w/GUI");
                    System.out.println("V) Associative Map Visualizations");
                    System.out.println("H) Command Line Help");
                    System.out.println("Q) quit");
                    System.out.println("\tEnter command: ");

                    received = true;
                    try
                    {
                        while( inReader.ready() != true )
                            Thread.sleep(100);
                        currentCommand = inReader.readLine().toLowerCase().toCharArray()[0];
                    }
                    catch (ArrayIndexOutOfBoundsException caughtException)
                    {
                        received = false;
                    }
                }

                System.out.println();

                switch (currentCommand)
                {
                case 'x':
                    com.syncleus.core.dann.examples.xor.Main.main(newArgs);
                    break;
                case 'i':
                    com.syncleus.core.dann.examples.nci.ui.NciDemo.main(newArgs);
                    break;
                case 'h':
                    System.out.println("The command line differs for each of the example files.");
                    System.out.println();
                    System.out.println("XOR Exmaple:");
                    System.out.println("java -jar bin dANN-examples.jar --xor [save-location]");
                    break;
                case 'v':
                    com.syncleus.core.dann.examples.associativemap.visualization.ViewMap.main(newArgs);
                    break;
                case 'q':
                    break;
                default:
                    System.out.println("Invalid command");
                }
            }
            while ((currentCommand != 'q') && (currentCommand >= 0));
        }
        catch (Exception caughtException)
        {
            caughtException.printStackTrace();
            System.out.println();
            throw new InternalError("CaughtException: " + caughtException);
        }
    }
}
