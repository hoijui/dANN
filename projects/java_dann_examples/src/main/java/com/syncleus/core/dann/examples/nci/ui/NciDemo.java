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
package com.syncleus.core.dann.examples.nci.ui;

import com.syncleus.core.dann.examples.nci.*;
import com.syncleus.dann.ComponentUnavailableException;
import java.awt.event.*;
import javax.swing.*;
import com.syncleus.dann.graph.drawing.hyperassociativemap.visualization.*;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import org.apache.log4j.Logger;

public class NciDemo extends JFrame implements ActionListener, BrainListener {

    private final static int BLOCK_WIDTH = 7;
    private final static int BLOCK_HEIGHT = 7;
    private BrainRunner brainRunner;
    private HyperassociativeMapCanvas brainVisual;
    private Component errorPanel;
    private Thread brainRunnerThread;
    private File trainingDirectory;
    private File originalImageLocation;
    private BufferedImage originalImage;
    private ImagePanel originalImagePanel = new ImagePanel();
    private BufferedImage finalImage;
    private ImagePanel finalImagePanel = new ImagePanel();
    private boolean processing = false;
    int trainingRemaining;
    int currentTrainingCycles = 100000;
    private ViewBrain viewBrain;
    private final static Logger LOGGER = Logger.getLogger(NciDemo.class);

    public NciDemo() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception caught) {
            LOGGER.warn("Could not set the UI to native look and feel", caught);
        }

        initComponents();

        this.add(this.originalImagePanel);
        int currentX = this.separator.getX() + 5;
        int currentY = 0;
        this.originalImagePanel.setLocation(currentX, currentY);
        this.originalImagePanel.setSize(800, 400);
        currentY = 400;
        this.originalImagePanel.setVisible(true);

        this.add(this.finalImagePanel);
        this.finalImagePanel.setLocation(currentX, currentY);
        this.finalImagePanel.setSize(800, 600);
        this.finalImagePanel.setVisible(true);


        this.setSize(600, 350);
        this.setExtendedState(MAXIMIZED_BOTH);


        new Timer(250, this).start();
    }

    public void actionPerformed(ActionEvent evt) {

        if (this.trainingRemaining > 0) {
            this.trainingRemaining = this.brainRunner.getTrainingCycles();
            int progressPercent = ((this.currentTrainingCycles - this.trainingRemaining) * 100) / (this.currentTrainingCycles);
            this.progress.setValue(progressPercent);
        } else if (this.processing == true) {
            this.progress.setValue(this.brainRunner.getSampleProgress());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        trainingDirectoryText = new javax.swing.JTextField();
        trainingDirectorySelect = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        originalImageSelect = new javax.swing.JButton();
        originalImageText = new javax.swing.JTextField();
        trainButton = new javax.swing.JButton();
        trainingCylcesInput = new javax.swing.JSpinner();
        processButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        separator = new javax.swing.JSeparator();
        statusLabel = new javax.swing.JLabel();
        progress = new javax.swing.JProgressBar();
        stopButton = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        fileMenu1 = new javax.swing.JMenu();
        quitMenuItem1 = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        brainViewMenu = new javax.swing.JMenuItem();
        helpMenu1 = new javax.swing.JMenu();
        aboutMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("NCI Demo");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        trainingDirectoryText.setEditable(false);

        trainingDirectorySelect.setText("...");
        trainingDirectorySelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trainingDirectorySelectActionPerformed(evt);
            }
        });

        jLabel1.setText("Training Images");

        jLabel2.setText("Original Image(s)");

        originalImageSelect.setText("...");
        originalImageSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                originalImageSelectActionPerformed(evt);
            }
        });

        originalImageText.setEditable(false);

        trainButton.setText("Train");
        trainButton.setEnabled(false);
        trainButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trainButtonActionPerformed(evt);
            }
        });

        trainingCylcesInput.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(10000), Integer.valueOf(1), null, Integer.valueOf(1000)));

        processButton.setText("Process");
        processButton.setEnabled(false);
        processButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                processButtonActionPerformed(evt);
            }
        });

        jLabel5.setText("Training Cycles");

        separator.setOrientation(javax.swing.SwingConstants.VERTICAL);

        statusLabel.setText("Please make selections!");

        progress.setStringPainted(true);

        stopButton.setText("Stop");
        stopButton.setEnabled(false);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        fileMenu1.setText("File");

        quitMenuItem1.setText("Quit");
        quitMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                quitMenuItemMouseReleased(evt);
            }
        });
        quitMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitMenuItem1ActionPerformed(evt);
            }
        });
        quitMenuItem1.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                quitMenuItemMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        fileMenu1.add(quitMenuItem1);

        jMenuBar2.add(fileMenu1);

        toolsMenu.setText("Tools");

        brainViewMenu.setText("3D Brain View");
        brainViewMenu.setEnabled(false);
        brainViewMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                brainViewMenuMouseReleased(evt);
            }
        });
        brainViewMenu.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                brainViewMenuMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        toolsMenu.add(brainViewMenu);

        jMenuBar2.add(toolsMenu);

        helpMenu1.setText("Help");

        aboutMenuItem1.setText("About");
        aboutMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                aboutMenuItemMouseReleased(evt);
            }
        });
        aboutMenuItem1.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                aboutMenuItemMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        helpMenu1.add(aboutMenuItem1);

        jMenuBar2.add(helpMenu1);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(statusLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(trainingCylcesInput, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(trainingDirectoryText, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(originalImageText, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(originalImageSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(trainingDirectorySelect, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(progress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(trainButton)
                                .addGap(18, 18, 18)
                                .addComponent(processButton)
                                .addGap(18, 18, 18)
                                .addComponent(stopButton)))))
                .addGap(18, 18, 18)
                .addComponent(separator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10000, 10000, 10000))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(separator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 715, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(trainingDirectoryText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(trainingDirectorySelect))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(originalImageText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(originalImageSelect))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 470, Short.MAX_VALUE)
                        .addComponent(statusLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(trainingCylcesInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(processButton)
                            .addComponent(stopButton)
                            .addComponent(trainButton))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // </editor-fold>

private void quitMenuItemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quitMenuItemMouseReleased
    if (this.brainRunner != null) {
        this.brainRunner.shutdown();
    }
    System.exit(0);
}//GEN-LAST:event_quitMenuItemMouseReleased

private void quitMenuItemMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_quitMenuItemMenuKeyPressed
    if (this.brainRunner != null) {
        this.brainRunner.shutdown();
    }
    System.exit(0);
}//GEN-LAST:event_quitMenuItemMenuKeyPressed

private void aboutMenuItemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aboutMenuItemMouseReleased
    displayAbout();
}//GEN-LAST:event_aboutMenuItemMouseReleased

private void aboutMenuItemMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_aboutMenuItemMenuKeyPressed
    displayAbout();
}//GEN-LAST:event_aboutMenuItemMenuKeyPressed

private void originalImageSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_originalImageSelectActionPerformed
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
    chooser.setFileFilter(filter);
    chooser.setFileSelectionMode(chooser.FILES_ONLY);
    chooser.setMultiSelectionEnabled(false);
    chooser.setVisible(true);
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        this.originalImageText.setText(chooser.getSelectedFile().getAbsolutePath());
        this.originalImageLocation = chooser.getSelectedFile();

        this.refreshOriginalImage();

        if (this.brainRunnerThread != null) {
            this.processButton.setEnabled(true);
            this.trainButton.setEnabled(true);

            this.statusLabel.setText("Ready!");
        }
    }
}//GEN-LAST:event_originalImageSelectActionPerformed

private void trainingDirectorySelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trainingDirectorySelectActionPerformed
    JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode(chooser.DIRECTORIES_ONLY);
    chooser.setMultiSelectionEnabled(false);
    chooser.setVisible(true);
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        this.trainingDirectoryText.setText(chooser.getSelectedFile().getAbsolutePath());
        this.trainingDirectory = chooser.getSelectedFile();

        File[] trainingFiles = trainingDirectory.listFiles(new PngFileFilter());
        if (trainingFiles.length <= 0) {
            this.trainingDirectory = null;
            this.trainingDirectoryText.setText("");

            JOptionPane.showMessageDialog(this, "The selected training directory does not contain *.png files! Select a new directory", "Invalid Training Directory", JOptionPane.ERROR_MESSAGE);

            return;
        }

        this.brainRunner = new BrainRunner(this, trainingFiles, 0.875, BLOCK_WIDTH, BLOCK_HEIGHT, true);
        this.brainRunnerThread = new Thread(this.brainRunner);
        this.brainRunnerThread.start();

        if (this.originalImageLocation != null) {
            this.processButton.setEnabled(true);
            this.trainButton.setEnabled(true);

            this.statusLabel.setText("Ready!");
        }
    }
}//GEN-LAST:event_trainingDirectorySelectActionPerformed

private void trainButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trainButtonActionPerformed
    this.trainButton.setEnabled(false);
    this.processButton.setEnabled(false);
    this.stopButton.setEnabled(true);

    this.statusLabel.setText("Please wait, training...");

    this.currentTrainingCycles = ((Integer) this.trainingCylcesInput.getValue()).intValue();
    this.trainingRemaining = this.currentTrainingCycles;

    this.brainRunner.setTrainingCycles(this.currentTrainingCycles);
}//GEN-LAST:event_trainButtonActionPerformed

private void processButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_processButtonActionPerformed
    if ((this.processing == true) || (finalImage == null) || (originalImage == null)) {
        return;
    }

    this.processButton.setEnabled(false);
    this.trainButton.setEnabled(false);
    this.stopButton.setEnabled(false);

    this.processing = true;

    this.statusLabel.setText("Please wait, processing...");

    this.brainRunner.setSampleImage(this.originalImageLocation);
}//GEN-LAST:event_processButtonActionPerformed

private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
    this.brainRunner.stop();
}//GEN-LAST:event_stopButtonActionPerformed

    public void viewBrain() {
        if (brainVisual != null) {
            this.brainVisual.refresh();

            if (this.viewBrain == null) {
                this.viewBrain = new ViewBrain(this, brainVisual);
            }

            this.viewBrain.setVisible(true);
        } else {
            JDialog errorDialog = new JDialog();
            errorDialog.add(errorPanel);
            errorDialog.setVisible(true);
        }
    }
    
private void brainViewMenuMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_brainViewMenuMenuKeyPressed

    viewBrain();
}//GEN-LAST:event_brainViewMenuMenuKeyPressed

private void brainViewMenuMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_brainViewMenuMouseReleased
    viewBrain();
}//GEN-LAST:event_brainViewMenuMouseReleased

private void quitMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitMenuItem1ActionPerformed
    if (this.brainRunner != null) {
        this.brainRunner.shutdown();
    }
    System.exit(0);
}//GEN-LAST:event_quitMenuItem1ActionPerformed

    private void refreshOriginalImage() {
        if (this.originalImageLocation == null) {
            return;
        }

        try {
            originalImage = ImageIO.read(this.originalImageLocation);
        } catch (IOException caught) {
            LOGGER.warn("IO Exception when reading image", caught);
            return;
        }
        this.originalImagePanel.setImage(this.originalImage);
        this.finalImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
    }

    private void displayAbout() {
        AboutDialog about = new AboutDialog(this, true);
        about.setVisible(true);
    }

    public void brainFinishedBuffering() {
        try {
            this.brainVisual = new HyperassociativeMapCanvas(this.brainRunner.getBrainMap());
            this.brainViewMenu.setEnabled(true);
        } catch (ComponentUnavailableException e) {
            this.brainVisual = null;
            this.errorPanel = e.newPanel();
            this.brainViewMenu.setEnabled(false);

        }
    }

    public void brainSampleProcessed(BufferedImage finalImage) {
        this.processing = false;
        this.progress.setValue(100);
        this.finalImage = finalImage;
        this.finalImagePanel.setImage(this.finalImage);
        this.finalImagePanel.repaint();

        this.processButton.setEnabled(true);
        this.trainButton.setEnabled(true);
        this.stopButton.setEnabled(false);

        this.statusLabel.setText("Ready!");
    }

    public void brainTrainingComplete() {
        this.trainingRemaining = 0;
        this.progress.setValue(100);

        this.processButton.setEnabled(true);
        this.trainButton.setEnabled(true);
        this.stopButton.setEnabled(false);

        this.statusLabel.setText("Ready!");
    }

    public static void main(String args[]) throws Exception {
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {

                public void run() {
                    try {
                        new NciDemo().setVisible(true);
                    } catch (Exception caught) {
                        LOGGER.error("Exception was caught", caught);
                        throw new RuntimeException("Throwable was caught", caught);
                    } catch (Error caught) {
                        LOGGER.error("Error was caught", caught);
                        throw new Error("Throwable was caught");
                    }
                }
            });
        } catch (Exception caught) {
            LOGGER.error("Exception was caught", caught);
            throw new RuntimeException("Throwable was caught", caught);
        } catch (Error caught) {
            LOGGER.error("Error was caught", caught);
            throw new Error("Throwable was caught");
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem1;
    private javax.swing.JMenuItem brainViewMenu;
    private javax.swing.JMenu fileMenu1;
    private javax.swing.JMenu helpMenu1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JButton originalImageSelect;
    private javax.swing.JTextField originalImageText;
    private javax.swing.JButton processButton;
    private javax.swing.JProgressBar progress;
    private javax.swing.JMenuItem quitMenuItem1;
    private javax.swing.JSeparator separator;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JButton stopButton;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JButton trainButton;
    private javax.swing.JSpinner trainingCylcesInput;
    private javax.swing.JButton trainingDirectorySelect;
    private javax.swing.JTextField trainingDirectoryText;
    // End of variables declaration//GEN-END:variables
}
