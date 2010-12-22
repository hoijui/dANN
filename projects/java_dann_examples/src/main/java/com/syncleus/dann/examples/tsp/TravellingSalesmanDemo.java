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
package com.syncleus.dann.examples.tsp;

import java.awt.Graphics;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;
import org.apache.log4j.Logger;
import com.syncleus.dann.math.Vector;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.Future;

public class TravellingSalesmanDemo extends JFrame implements ActionListener
{
	private final static Logger LOGGER = Logger.getLogger(TravellingSalesmanDemo.class);
	private final static Random RANDOM = new Random();

	private final SpinnerNumberModel citiesModel = new SpinnerNumberModel(10, 1, 100,1);
	private final SpinnerNumberModel mutabilityModel = new SpinnerNumberModel(1.0, Double.MIN_VALUE, 10000, 0.1);
	private final SpinnerNumberModel populationModel = new SpinnerNumberModel(100, 4, 1000, 10);
	private final SpinnerNumberModel crossoverModel = new SpinnerNumberModel(0.1, Double.MIN_VALUE, 1.0, 0.01);
	private final SpinnerNumberModel dieOffModel = new SpinnerNumberModel(0.4, Double.MIN_VALUE, 1.0, 0.01);
	private final SpinnerNumberModel generationsModel = new SpinnerNumberModel(100, 1, 100000, 100);

	private static final int MAP_X = 12;
	private static final int MAP_Y = 130;
	private static final int MAP_WIDTH = 635;
	private static final int MAP_HEIGHT = 500;

	private final ExecutorService executor = Executors.newFixedThreadPool(1);

	private final Timer progressTimer = new Timer(100, this);

	private PopulationEvolveCallable populationCallable = null;
	private Future<TravellingSalesmanChromosome> futureWinner = null;
	private TravellingSalesmanChromosome currentWinner = null;
	private Vector cities[] = null;

	private static class PopulationEvolveCallable implements Callable<TravellingSalesmanChromosome>
	{
		private final TravellingSalesmanPopulation population;
		private volatile int iterations = 0;
		private final int iterationsMax;
		private final static Logger LOGGER = Logger.getLogger(PopulationEvolveCallable.class);

		public PopulationEvolveCallable(final TravellingSalesmanPopulation population, final int iterationsMax)
		{
			if(iterationsMax <= 0)
				throw new IllegalArgumentException("iterationsMax must be greater than 0");
			
			this.population = population;
			this.iterationsMax = iterationsMax;
		}

		public TravellingSalesmanChromosome call()
		{
			try
			{
				for(; this.iterations < this.iterationsMax; this.iterations++)
					this.population.nextGeneration();

				return this.population.getWinner();
			}
			catch(Exception caught)
			{
				LOGGER.error("Exception was caught", caught);
				throw new RuntimeException("Throwable was caught", caught);
			}
			catch(Error caught)
			{
				LOGGER.error("Error was caught", caught);
				throw new Error("Throwable was caught");
			}
		}

		public int getIterations()
		{
			return this.iterations;
		}

		public int getIterationsMax()
		{
			return this.iterationsMax;
		}
	}

    public TravellingSalesmanDemo()
	{
		LOGGER.info("Instantiating Travelling Salesman Demo Frame");

        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception caught)
        {
            LOGGER.warn("Could not set the UI to native look and feel", caught);
        }

        initComponents();

		this.citiesSpinner.setModel(this.citiesModel);
		this.mutabilitySpinner.setModel(this.mutabilityModel);
		this.populationSpinner.setModel(this.populationModel);
		this.crossoverSpinner.setModel(this.crossoverModel);
		this.dieOffSpinner.setModel(this.dieOffModel);
		this.generationsSpinner.setModel(this.generationsModel);

		this.setResizable(false);
		this.repaint();
    }

	@Override
	public void paint(Graphics graphics)
	{
		super.paint(graphics);
		graphics.drawRect(MAP_X, MAP_Y, MAP_WIDTH, MAP_HEIGHT);

		if(this.cities != null)
		{
			for(Vector city : this.cities)
			{
				int currentX = (int) city.getCoordinate(1);
				int currentY = (int) city.getCoordinate(2);

				graphics.fillArc((currentX + MAP_X) - 5, (currentY + MAP_Y) - 5, 10, 10, 0, 360);
			}
		}

		if((this.cities != null)&&(this.currentWinner != null))
		{
			int ordering[] = this.currentWinner.getCitiesOrder();
			Vector[] orderedPoints = new Vector[ordering.length];
			for(int cityIndex = 0; cityIndex < this.cities.length; cityIndex++)
			{
				orderedPoints[ordering[cityIndex]] = this.cities[cityIndex];
			}

			//draw the points
			Vector firstPoint = null;
			Vector lastPoint = null;
			for(Vector point : orderedPoints)
			{
				if(lastPoint == null)
				{
					lastPoint = point;
					firstPoint = point;
				}
				else
				{
					final int lastX = (int) lastPoint.getCoordinate(1);
					final int lastY = (int) lastPoint.getCoordinate(2);

					final int currentX = (int) point.getCoordinate(1);
					final int currentY = (int) point.getCoordinate(2);

					graphics.drawLine(lastX + MAP_X, lastY + MAP_Y, currentX + MAP_X, currentY + MAP_Y);

					lastPoint = point;
				}
			}
			if((lastPoint != null)&&(firstPoint != null))
			{
				final int lastX = (int) lastPoint.getCoordinate(1);
				final int lastY = (int) lastPoint.getCoordinate(2);

				final int firstX = (int) firstPoint.getCoordinate(1);
				final int firstY = (int) firstPoint.getCoordinate(2);

				graphics.drawLine(lastX + MAP_X, lastY + MAP_Y, firstX + MAP_X, firstY + MAP_Y);
			}
		}

	}

	private static Vector[] getRandomPoints(int cityCount)
	{
		if(cityCount < 4)
			throw new IllegalArgumentException("cityCount must have atleast 4 elements");

		HashSet<Vector> pointsSet = new HashSet<Vector>();
		while(pointsSet.size() < cityCount)
			pointsSet.add(new Vector(new double[]{RANDOM.nextDouble() * MAP_WIDTH, RANDOM.nextDouble() * MAP_HEIGHT}));

		Vector[] points = new Vector[cityCount];
		pointsSet.toArray(points);

		return points;
	}

	public void actionPerformed(ActionEvent evt)
	{
		if((this.futureWinner != null)&&(this.populationCallable != null))
		{
			this.progressBar.setValue(this.populationCallable.getIterations());

			if( this.futureWinner.isDone() )
			{
				LOGGER.debug("this.futureWinner.isDone() == true");
				
				try
				{
					this.currentWinner = this.futureWinner.get();
				}
				catch(Exception caught)
				{
					LOGGER.error("futureWinner threw an unexpected exception", caught);
					throw new AssertionError("futureWinner threw an unexpected exception");
				}
				this.populationCallable = null;
				this.futureWinner = null;

				this.progressTimer.stop();
				this.evolveDisplayButton.setEnabled(true);

				this.repaint();
			}
		}
	}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        citiesLabel = new javax.swing.JLabel();
        citiesSpinner = new javax.swing.JSpinner();
        populationLabel = new javax.swing.JLabel();
        populationSpinner = new javax.swing.JSpinner();
        mutabilityLabel = new javax.swing.JLabel();
        mutabilitySpinner = new javax.swing.JSpinner();
        crossoverLabel = new javax.swing.JLabel();
        crossoverSpinner = new javax.swing.JSpinner();
        dieOffLabel = new javax.swing.JLabel();
        dieOffSpinner = new javax.swing.JSpinner();
        evolveDisplayButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        generationsLabel = new javax.swing.JLabel();
        generationsSpinner = new javax.swing.JSpinner();
        menuBar = new javax.swing.JMenuBar();
        fileMenuItem = new javax.swing.JMenu();
        quitMenuItem = new javax.swing.JMenuItem();
        helpMenuItem = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Travelling Salesman Demo");

        citiesLabel.setText("Cities:");

        populationLabel.setText("Population:");

        mutabilityLabel.setText("Mutability:");

        crossoverLabel.setText("Crossover:");

        dieOffLabel.setText("Die Off:");

        evolveDisplayButton.setText("Evolve & Display");
        evolveDisplayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                evolveDisplayButtonActionPerformed(evt);
            }
        });

        progressBar.setStringPainted(true);

        generationsLabel.setText("Generations:");

        fileMenuItem.setText("File");

        quitMenuItem.setText("Quit");
        quitMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                quitMenuItemMouseReleased(evt);
            }
        });
        fileMenuItem.add(quitMenuItem);

        menuBar.add(fileMenuItem);

        helpMenuItem.setText("Help");

        aboutMenuItem.setText("About");
        helpMenuItem.add(aboutMenuItem);

        menuBar.add(helpMenuItem);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(citiesLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(citiesSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(populationLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(populationSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mutabilityLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mutabilitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(crossoverLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(crossoverSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dieOffLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dieOffSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(generationsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(generationsSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(evolveDisplayButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(citiesLabel)
                    .addComponent(citiesSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(populationLabel)
                    .addComponent(populationSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mutabilityLabel)
                    .addComponent(mutabilitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(crossoverLabel)
                    .addComponent(crossoverSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dieOffLabel)
                    .addComponent(dieOffSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generationsLabel)
                    .addComponent(generationsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(evolveDisplayButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(520, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void quitMenuItemMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_quitMenuItemMouseReleased
	{//GEN-HEADEREND:event_quitMenuItemMouseReleased
		System.exit(0);
	}//GEN-LAST:event_quitMenuItemMouseReleased

	private void evolveDisplayButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_evolveDisplayButtonActionPerformed
	{//GEN-HEADEREND:event_evolveDisplayButtonActionPerformed
		this.currentWinner = null;
		this.cities = getRandomPoints(this.citiesModel.getNumber().intValue());

		final int populationSize = this.populationModel.getNumber().intValue();
		final double mutability = this.mutabilityModel.getNumber().doubleValue();
		final double crossover = this.crossoverModel.getNumber().doubleValue();
		final double dieOff = this.dieOffModel.getNumber().doubleValue();
		final int generations = this.generationsModel.getNumber().intValue();

		final TravellingSalesmanPopulation population = new TravellingSalesmanPopulation(this.cities, mutability, crossover, dieOff);
		population.initializePopulation(populationSize);

		this.populationCallable = new PopulationEvolveCallable(population, generations);
		this.futureWinner = this.executor.submit(this.populationCallable);

		this.progressBar.setMaximum(generations);

		this.evolveDisplayButton.setEnabled(false);
		this.progressTimer.start();

		this.repaint();
	}//GEN-LAST:event_evolveDisplayButtonActionPerformed

    public static void main(String args[])
	{
		LOGGER.info("Starting Travelling Salesman Demo from main()");
        java.awt.EventQueue.invokeLater(new Runnable()
		{
            public void run()
			{
                new TravellingSalesmanDemo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JLabel citiesLabel;
    private javax.swing.JSpinner citiesSpinner;
    private javax.swing.JLabel crossoverLabel;
    private javax.swing.JSpinner crossoverSpinner;
    private javax.swing.JLabel dieOffLabel;
    private javax.swing.JSpinner dieOffSpinner;
    private javax.swing.JButton evolveDisplayButton;
    private javax.swing.JMenu fileMenuItem;
    private javax.swing.JLabel generationsLabel;
    private javax.swing.JSpinner generationsSpinner;
    private javax.swing.JMenu helpMenuItem;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel mutabilityLabel;
    private javax.swing.JSpinner mutabilitySpinner;
    private javax.swing.JLabel populationLabel;
    private javax.swing.JSpinner populationSpinner;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem quitMenuItem;
    // End of variables declaration//GEN-END:variables

}
