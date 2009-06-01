/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.core.dann.examples.test;


import hep.aida.*;

public class Test
{
    public static void main(String[] args)
    {
        IAnalysisFactory af = IAnalysisFactory.create();
        ITree tree = af.createTreeFactory().create();
        IDataPointSetFactory dpsf = af.createDataPointSetFactory(tree);
        IPlotterFactory plotterFactory = af.createPlotterFactory();
        //IFunctionFactory     funcF  = af.createFunctionFactory(tree);
        //IFitFactory          fitF   = af.createFitFactory();
        //IFitter              fitter = fitF.createFitter("Chi2","jminuit");

        // Create a two dimensional IDataPointSet.
        IDataPointSet dataPointSet = dpsf.create("dataPointSet", "two dimensional IDataPointSet", 2);

        DistributedFormedWaveMathFunction waveA = new DistributedFormedWaveMathFunction();
        waveA.setX(0.0);
        waveA.setFrequency(1.0);
        waveA.setPhase(0.0);
        waveA.setAmplitude(1.0);
        waveA.setForm(10);
        waveA.setCenter(0.5f);
        waveA.setDistribution(1.0f);
        DistributedFormedWaveMathFunction waveB = new DistributedFormedWaveMathFunction();
        waveB.setX(0.0);
        waveB.setFrequency(1.2);
        waveB.setPhase(87.0);
        waveB.setAmplitude(5.0);
        waveB.setForm(13);
        waveB.setCenter(0.5f);
        waveB.setDistribution(1.0f);
        for(int i = 0;i < 10000;i++)
        {
            dataPointSet.addPoint();
            IDataPoint dp = dataPointSet.point(i);
            dp.coordinate(0).setValue(i / 1000.0);
            waveA.setX(i / 1000.0);
            waveB.setX(i / 1000.0);
            dp.coordinate(1).setValue(waveA.calculate() + waveB.calculate());
        }


        IPlotter plotter = plotterFactory.create("CreateAndFitDataPointSet.java plot");
        IPlotterStyle plotterStyle = plotterFactory.createPlotterStyle();
        plotterStyle.dataStyle().markerStyle().setSize(0);
        plotter.region(0).plot(dataPointSet, plotterStyle);
        plotter.show();

    //IFunction line = funcF.createFunctionByName("line","p1");

    //IFitResult result = fitter.fit(dataPointSet,line);
    //plotter.region(0).plot(result.fittedFunction());

    //System.out.println("Chi2="+result.quality());   

    }
}
