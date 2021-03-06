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
package com.syncleus.core.dann.examples.hyperassociativemap.visualization;

import com.syncleus.dann.ComponentUnavailableException;
import com.syncleus.dann.graph.drawing.hyperassociativemap.visualization.*;
import java.awt.event.*;
import java.util.concurrent.*;
import javax.swing.*;

public class ViewMap extends JFrame implements ActionListener, WindowListener, KeyListener {

    private HyperassociativeMapCanvas mapVisual;
    private LayeredHyperassociativeMap associativeMap;
    private final ExecutorService executor;
    private FutureTask<Void> lastRun;

    public ViewMap() {
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.associativeMap = new LayeredHyperassociativeMap(8, executor);


        try {
            this.mapVisual = new HyperassociativeMapCanvas(this.associativeMap, 0.07F);
            initComponents();

            this.lastRun = new FutureTask<Void>(new UpdateViewRun(this.mapVisual, associativeMap), null);
            this.executor.execute(this.lastRun);

            this.mapVisual.setFocusTraversalKeysEnabled(false);
            this.mapVisual.addKeyListener(this);
            this.addKeyListener(this);

            new Timer(100, this).start();
            
        this.mapVisual.setLocation(0, 0);
        this.mapVisual.setSize(800, 600);
        this.mapVisual.setVisible(true);
        this.mapVisual.refresh();

        } catch (ComponentUnavailableException e) {
            this.add(e.newPanel());
        }

        this.addWindowListener(this);
        this.setFocusTraversalKeysEnabled(false);

        this.setSize(800, 600);

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_R) {
            this.associativeMap.reset();
        }
        if (e.getKeyCode() == KeyEvent.VK_L) {
            this.associativeMap.resetLearning();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (this.associativeMap.getEquilibriumDistance() < 1.0) {
                this.associativeMap.setEquilibriumDistance(this.associativeMap.getEquilibriumDistance() * 1.1);
            } else {
                this.associativeMap.setEquilibriumDistance(this.associativeMap.getEquilibriumDistance() + 1.0);
            }
            this.associativeMap.resetLearning();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (this.associativeMap.getEquilibriumDistance() < 2.0) {
                this.associativeMap.setEquilibriumDistance(this.associativeMap.getEquilibriumDistance() * 0.9);
            } else {
                this.associativeMap.setEquilibriumDistance(this.associativeMap.getEquilibriumDistance() - 1.0);
            }
            this.associativeMap.resetLearning();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        this.executor.shutdown();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void actionPerformed(ActionEvent evt) {
        if ((this.lastRun != null) && (this.lastRun.isDone() == false)) {
            return;
        }

        if (this.isVisible() == false) {
            return;
        }

        this.lastRun = new FutureTask<Void>(new UpdateViewRun(this.mapVisual, this.associativeMap), null);
        this.executor.execute(this.lastRun);
    }

    private static boolean checkClasses() {
        try {
            Class.forName("javax.media.j3d.NativePipeline");
        } catch (ClassNotFoundException caughtException) {
            System.out.println("java3D library isnt in classpath!");
            return false;
        }

        return true;
    }

    public static void main(String args[]) throws Exception {
        //check that the java3D drivers are present
        if (!checkClasses()) {
            return;
        }

        System.out.println("controls:");
        System.out.println("R: reset");
        System.out.println("L: reset learing curve");
        System.out.println("up arrow: increase Equilibrium");
        System.out.println("down arrow: decrease Equilibrium");

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ViewMap().setVisible(true);
            }
        });
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
}
