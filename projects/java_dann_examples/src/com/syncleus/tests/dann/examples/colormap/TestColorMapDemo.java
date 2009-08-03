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
package com.syncleus.tests.dann.examples.colormap;

import com.syncleus.core.dann.examples.colormap.ColorMapDemo;
import org.fest.swing.exception.UnexpectedException;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.timing.Timeout;
import org.junit.*;

public class TestColorMapDemo
{
	private FrameFixture colorMapDemoFixture;

	@BeforeClass
	public static void setUpOnce()
	{
		FailOnThreadViolationRepaintManager.install();
	}


	@Before
	public void onSetUp()
	{
		ColorMapDemo colorMapDemo = GuiActionRunner.execute(new GuiQuery<ColorMapDemo>()
		{
			protected ColorMapDemo executeInEDT()
			{
				return new ColorMapDemo();
			}
		});

		colorMapDemoFixture = new FrameFixture(colorMapDemo);
		colorMapDemoFixture.show();
	}

	@After
	public void tearDown()
	{
		colorMapDemoFixture.cleanUp();
	}

	@Test
	public void testComponents()
	{
		colorMapDemoFixture.requireVisible();

		//test the spinner
		//spinners should take values of arbitrary granularity
		colorMapDemoFixture.spinner("iterationsSpinner").enterTextAndCommit("257");
		colorMapDemoFixture.spinner("iterationsSpinner").requireValue(257);
		colorMapDemoFixture.spinner("learningRateSpinner").enterTextAndCommit("0.16492");
		colorMapDemoFixture.spinner("learningRateSpinner").requireValue(0.16492);
		//lets try incrementing
		colorMapDemoFixture.spinner("iterationsSpinner").enterTextAndCommit("100");
		colorMapDemoFixture.spinner("iterationsSpinner").requireValue(100);
		colorMapDemoFixture.spinner("iterationsSpinner").increment(9);
		colorMapDemoFixture.spinner("iterationsSpinner").requireValue(1000);
		colorMapDemoFixture.spinner("iterationsSpinner").increment(100);
		colorMapDemoFixture.spinner("iterationsSpinner").requireValue(10000);
		colorMapDemoFixture.spinner("learningRateSpinner").enterTextAndCommit("0.01");
		colorMapDemoFixture.spinner("learningRateSpinner").increment(9);
		double currentValue = Double.valueOf(colorMapDemoFixture.spinner("learningRateSpinner").text());
		Assert.assertTrue("learning rate spinner did notincrement properly", (currentValue - 0.1) < 0.00001);
		colorMapDemoFixture.spinner("learningRateSpinner").increment(100);
		currentValue = Double.valueOf(colorMapDemoFixture.spinner("learningRateSpinner").text());
		Assert.assertTrue("learning rate spinner did notincrement properly", (currentValue - 1.0) < 0.001);
		//lets try decrementing
		colorMapDemoFixture.spinner("iterationsSpinner").enterTextAndCommit("10000");
		colorMapDemoFixture.spinner("iterationsSpinner").requireValue(10000);
		colorMapDemoFixture.spinner("iterationsSpinner").decrement(10);
		colorMapDemoFixture.spinner("iterationsSpinner").requireValue(9000);
		colorMapDemoFixture.spinner("iterationsSpinner").decrement(100);
		colorMapDemoFixture.spinner("iterationsSpinner").requireValue(100);
		colorMapDemoFixture.spinner("learningRateSpinner").enterTextAndCommit("1.0");
		colorMapDemoFixture.spinner("learningRateSpinner").decrement(10);
		currentValue = Double.valueOf(colorMapDemoFixture.spinner("learningRateSpinner").text());
		Assert.assertTrue("learning rate spinner did notincrement properly", (currentValue - 0.9) < 0.00001);
		colorMapDemoFixture.spinner("learningRateSpinner").decrement(100);
		currentValue = Double.valueOf(colorMapDemoFixture.spinner("learningRateSpinner").text());
		Assert.assertTrue("learning rate spinner did notincrement properly", (currentValue - 0.01) < 0.00001);
	}

	@Test(expected=UnexpectedException.class)
	public void testIterationsMinimum()
	{
		colorMapDemoFixture.spinner("iterationsSpinner").enterTextAndCommit("1000");
		colorMapDemoFixture.spinner("iterationsSpinner").requireValue(1000);
		colorMapDemoFixture.spinner("iterationsSpinner").enterTextAndCommit("0");
	}

	@Test(expected=UnexpectedException.class)
	public void testIterationsMaximum()
	{
		colorMapDemoFixture.spinner("iterationsSpinner").enterTextAndCommit("1000");
		colorMapDemoFixture.spinner("iterationsSpinner").requireValue(1000);
		colorMapDemoFixture.spinner("iterationsSpinner").enterTextAndCommit("100000");
	}

	@Test(expected=UnexpectedException.class)
	public void testLearningRateMinimum()
	{
		colorMapDemoFixture.spinner("learningRateSpinner").enterTextAndCommit("0.5");
		colorMapDemoFixture.spinner("learningRateSpinner").requireValue(0.5);
		colorMapDemoFixture.spinner("learningRateSpinner").enterTextAndCommit("0");
	}

	@Test(expected=UnexpectedException.class)
	public void testLearningRateMaximum()
	{
		colorMapDemoFixture.spinner("learningRateSpinner").enterTextAndCommit("0.5");
		colorMapDemoFixture.spinner("learningRateSpinner").requireValue(0.5);
		colorMapDemoFixture.spinner("learningRateSpinner").enterTextAndCommit("1.001");
	}

	@Test
	public void testTrainingDisplay()
	{
		colorMapDemoFixture.requireVisible();

		//train and display for various parameters
		colorMapDemoFixture.button("trainDisplayButton").requireEnabled();

		colorMapDemoFixture.button("trainDisplayButton").click();
		colorMapDemoFixture.button("trainDisplayButton").requireDisabled();
		colorMapDemoFixture.button("trainDisplayButton").requireEnabled(Timeout.timeout(30000));

		colorMapDemoFixture.spinner("iterationsSpinner").enterTextAndCommit("1000");
		colorMapDemoFixture.spinner("iterationsSpinner").requireValue(1000);
		colorMapDemoFixture.button("trainDisplayButton").click();
		colorMapDemoFixture.button("trainDisplayButton").requireDisabled();
		colorMapDemoFixture.button("trainDisplayButton").requireEnabled(Timeout.timeout(30000));

		colorMapDemoFixture.spinner("iterationsSpinner").enterTextAndCommit("100");
		colorMapDemoFixture.spinner("iterationsSpinner").requireValue(100);
		colorMapDemoFixture.spinner("learningRateSpinner").enterTextAndCommit("0.1");
		colorMapDemoFixture.spinner("learningRateSpinner").requireValue(0.1);
		colorMapDemoFixture.comboBox("dimentionalityComboBox").selectItem("2D");
		colorMapDemoFixture.comboBox("dimentionalityComboBox").requireSelection(1);
		colorMapDemoFixture.button("trainDisplayButton").click();
		colorMapDemoFixture.button("trainDisplayButton").requireDisabled();
		colorMapDemoFixture.button("trainDisplayButton").requireEnabled(Timeout.timeout(30000));

		colorMapDemoFixture.spinner("iterationsSpinner").enterTextAndCommit("250");
		colorMapDemoFixture.spinner("iterationsSpinner").requireValue(250);
		colorMapDemoFixture.spinner("learningRateSpinner").enterTextAndCommit("1.0");
		colorMapDemoFixture.spinner("learningRateSpinner").requireValue(1.0);
		colorMapDemoFixture.comboBox("dimentionalityComboBox").selectItem("1D");
		colorMapDemoFixture.comboBox("dimentionalityComboBox").requireSelection(0);
		colorMapDemoFixture.button("trainDisplayButton").click();
		colorMapDemoFixture.button("trainDisplayButton").requireDisabled();
		colorMapDemoFixture.button("trainDisplayButton").requireEnabled(Timeout.timeout(30000));
	}
}
