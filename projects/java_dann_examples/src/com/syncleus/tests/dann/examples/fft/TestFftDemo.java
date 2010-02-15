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
package com.syncleus.tests.dann.examples.fft;

import com.syncleus.core.dann.examples.colormap.ColorMapDemo;
import com.syncleus.core.dann.examples.fft.FftDemo;
import org.fest.swing.exception.UnexpectedException;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.timing.Timeout;
import org.junit.*;

public class TestFftDemo
{
	private FrameFixture fftDemoFixture;

	@BeforeClass
	public static void setUpOnce()
	{
		FailOnThreadViolationRepaintManager.install();
	}


	@Before
	public void onSetUp()
	{
		FftDemo fftDemo = GuiActionRunner.execute(new GuiQuery<FftDemo>()
		{
			protected FftDemo executeInEDT()
			{
				return new FftDemo();
			}
		});

		fftDemoFixture = new FrameFixture(fftDemo);
		fftDemoFixture.show();
	}

	@After
	public void tearDown()
	{
		fftDemoFixture.cleanUp();
	}

	@Test
	public void testComponents()
	{
		fftDemoFixture.requireVisible();

		//start listening
		fftDemoFixture.button("listenButton").click();

		//check that its listening
		fftDemoFixture.button("listenButton").requireText("Stop");

		//stop listening
		fftDemoFixture.button("listenButton").click();

		//check if stopped
		fftDemoFixture.button("listenButton").requireText("Listen");
	}
}
