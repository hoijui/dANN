package com.syncleus.core.dann.examples.nci.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;


public class NciDemo extends javax.swing.JFrame implements ActionListener, BrainListener
{
    private final static int BLOCK_WIDTH = 16;
    private final static int BLOCK_HEIGHT = 16;
    private BrainRunner brainRunner;
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



    public NciDemo()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e)
        {
            System.out.println("Danger Will Robinson, Danger! Can not set native look and feel! " + e);
            e.printStackTrace();
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
        
        this.setSize(600, this.getHeight());

        new Timer(250, this).start();
    }



    public void actionPerformed(ActionEvent evt)
    {
        if (this.trainingRemaining > 0)
        {
            this.trainingRemaining = this.brainRunner.getTrainingCycles();
            int progressPercent = ((this.currentTrainingCycles - this.trainingRemaining) * 100) / (this.currentTrainingCycles);
            this.progress.setValue(progressPercent);
        }
        else if (this.processing == true)
            this.progress.setValue(this.brainRunner.getSampleProgress());
    }



    @SuppressWarnings("unchecked")
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
        jLabel6 = new javax.swing.JLabel();
        learningRateInput = new javax.swing.JSpinner();
        separator = new javax.swing.JSeparator();
        statusLabel = new javax.swing.JLabel();
        progress = new javax.swing.JProgressBar();
        stopButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        quitMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        quitMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

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

        trainingCylcesInput.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(100000), Integer.valueOf(1), null, Integer.valueOf(10000)));

        processButton.setText("Process");
        processButton.setEnabled(false);
        processButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                processButtonActionPerformed(evt);
            }
        });

        jLabel5.setText("Training Cycles");

        jLabel6.setText("Learning Rate");

        learningRateInput.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0010d), Double.valueOf(0.0010d), null, Double.valueOf(0.0010d)));

        separator.setOrientation(javax.swing.SwingConstants.VERTICAL);

        statusLabel.setText("Ready!");

        progress.setStringPainted(true);

        stopButton.setText("Stop");
        stopButton.setEnabled(false);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        fileMenu.setText("File");

        quitMenuItem.setText("Quit");
        quitMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                quitMenuItemMouseReleased(evt);
            }
        });
        quitMenuItem.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                quitMenuItemMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        fileMenu.add(quitMenuItem);

        jMenuBar1.add(fileMenu);

        helpMenu.setText("Help");

        aboutMenuItem.setText("About");
        aboutMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                aboutMenuItemMouseReleased(evt);
            }
        });
        aboutMenuItem.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                aboutMenuItemMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        helpMenu.add(aboutMenuItem);

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        fileMenu.setText("File");

        quitMenuItem.setText("Quit");
        quitMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                quitMenuItemMouseReleased(evt);
            }
        });
        quitMenuItem.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                quitMenuItemMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        fileMenu.add(quitMenuItem);

        jMenuBar1.add(fileMenu);

        helpMenu.setText("Help");

        aboutMenuItem.setText("About");
        aboutMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                aboutMenuItemMouseReleased(evt);
            }
        });
        aboutMenuItem.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                aboutMenuItemMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });
        helpMenu.add(aboutMenuItem);

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(trainingCylcesInput)
                            .addComponent(learningRateInput, javax.swing.GroupLayout.PREFERRED_SIZE, 57, Short.MAX_VALUE)))
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
                    .addComponent(separator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 464, Short.MAX_VALUE)
                        .addComponent(statusLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(trainingCylcesInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(learningRateInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(processButton)
                            .addComponent(stopButton)
                            .addComponent(trainButton))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void quitMenuItemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quitMenuItemMouseReleased
    if (this.brainRunner != null)
        this.brainRunner.shutdown();
    System.exit(0);
}//GEN-LAST:event_quitMenuItemMouseReleased

private void quitMenuItemMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_quitMenuItemMenuKeyPressed
    if (this.brainRunner != null)
        this.brainRunner.shutdown();
    System.exit(0);
}//GEN-LAST:event_quitMenuItemMenuKeyPressed

private void aboutMenuItemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aboutMenuItemMouseReleased
    displayAbout();
}//GEN-LAST:event_aboutMenuItemMouseReleased

private void aboutMenuItemMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_aboutMenuItemMenuKeyPressed
    displayAbout();
}//GEN-LAST:event_aboutMenuItemMenuKeyPressed

private void originalImageSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_originalImageSelectActionPerformed
//    this.originalImageText.setText("C:\\Documents and Settings\\All Users\\Documents\\My Pictures\\Sample Pictures\\In3.PNG");
//    this.originalImageLocation = new File("C:\\Documents and Settings\\All Users\\Documents\\My Pictures\\Sample Pictures\\In3.PNG");

//    this.refreshOriginalImage();

//    if (true)
//        return;

    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
    chooser.setFileFilter(filter);
    chooser.setFileSelectionMode(chooser.FILES_ONLY);
    chooser.setMultiSelectionEnabled(false);
    chooser.setVisible(true);
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
    {
        this.originalImageText.setText(chooser.getSelectedFile().getAbsolutePath());
        this.originalImageLocation = chooser.getSelectedFile();

        this.refreshOriginalImage();

        if (this.brainRunnerThread != null)
        {
            this.processButton.setEnabled(true);
            this.trainButton.setEnabled(true);
        }
    }
}//GEN-LAST:event_originalImageSelectActionPerformed

private void trainingDirectorySelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trainingDirectorySelectActionPerformed
//    this.trainingDirectoryText.setText("C:\\Documents and Settings\\All Users\\Documents\\My Pictures\\Sample Pictures");
//    this.trainingDirectory = new File("C:\\Documents and Settings\\All Users\\Documents\\My Pictures\\Sample Pictures");


    JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode(chooser.DIRECTORIES_ONLY);
    chooser.setMultiSelectionEnabled(false);
    chooser.setVisible(true);
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
    {
        this.trainingDirectoryText.setText(chooser.getSelectedFile().getAbsolutePath());
        this.trainingDirectory = chooser.getSelectedFile();


        try
        {

            File[] trainingFiles = trainingDirectory.listFiles(new PngFileFilter());
            this.brainRunner = new BrainRunner(this, trainingFiles, 0.8, BLOCK_WIDTH, BLOCK_HEIGHT, true);
            this.brainRunnerThread = new Thread(this.brainRunner);
            this.brainRunnerThread.start();

            if (this.originalImageLocation != null)
            {
                this.processButton.setEnabled(true);
                this.trainButton.setEnabled(true);
            }
        }
        catch (Exception e)
        {
            System.out.println("Danger will robinson, Danger: " + e);
            e.printStackTrace();
            return;
        }
    }
}//GEN-LAST:event_trainingDirectorySelectActionPerformed

private void trainButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trainButtonActionPerformed
    this.trainButton.setEnabled(false);
    this.processButton.setEnabled(false);
    this.stopButton.setEnabled(true);

    this.currentTrainingCycles = ((Integer) this.trainingCylcesInput.getValue()).intValue();
    this.trainingRemaining = this.currentTrainingCycles;

    this.brainRunner.setTrainingCycles(this.currentTrainingCycles);
}//GEN-LAST:event_trainButtonActionPerformed

private void processButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_processButtonActionPerformed
    if ((this.processing == true) || (finalImage == null) || (originalImage == null))
        return;
    
    this.processButton.setEnabled(false);
    this.trainButton.setEnabled(false);
    this.stopButton.setEnabled(false);

    this.processing = true;

    this.brainRunner.setSampleImage(this.originalImageLocation);
}//GEN-LAST:event_processButtonActionPerformed

private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed

//    this.processing = false;
//    this.finalWriteX = 0;
//    this.finalWriteY = 0;

    this.brainRunner.stop();
//    this.trainingRemaining = 0;
}//GEN-LAST:event_stopButtonActionPerformed

private void refreshOriginalImage()
{
    if( this.originalImageLocation == null)
        return;
    
    //originalImage = Toolkit.getDefaultToolkit().getImage(this.originalImageLocation.getAbsolutePath());
    try
    {
        originalImage = ImageIO.read(this.originalImageLocation);
    }
    catch(Exception e)
    {
        System.out.println("Danger will robinson, Danger: " + e);
        e.printStackTrace();
        return;
    }
    this.originalImagePanel.setImage(this.originalImage);
    this.finalImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
}

    private void displayAbout()
    {
        AboutDialog about = new AboutDialog(this, true);
        about.setVisible(true);
    }
    
    public void brainFinishedBuffering()
    {
    }
    
    public void brainSampleProcessed(BufferedImage finalImage)
    {
        this.processing = false;
        this.progress.setValue(100);
        this.finalImage = finalImage;
        this.finalImagePanel.setImage(this.finalImage);
        this.finalImagePanel.repaint();
        
        this.processButton.setEnabled(true);
        this.trainButton.setEnabled(true);
        this.stopButton.setEnabled(false);
    }
    
    public void brainTrainingComplete()
    {
        this.trainingRemaining = 0;
        this.progress.setValue(100);
        
        this.processButton.setEnabled(true);
        this.trainButton.setEnabled(true);
        this.stopButton.setEnabled(false);
    }



    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception
    {
        java.awt.EventQueue.invokeAndWait(new Runnable()
                                        {
                                            public void run()
                                            {
                                                new NciDemo().setVisible(true);
                                            }
                                        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JSpinner learningRateInput;
    private javax.swing.JButton originalImageSelect;
    private javax.swing.JTextField originalImageText;
    private javax.swing.JButton processButton;
    private javax.swing.JProgressBar progress;
    private javax.swing.JMenuItem quitMenuItem;
    private javax.swing.JSeparator separator;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JButton stopButton;
    private javax.swing.JButton trainButton;
    private javax.swing.JSpinner trainingCylcesInput;
    private javax.swing.JButton trainingDirectorySelect;
    private javax.swing.JTextField trainingDirectoryText;
    // End of variables declaration//GEN-END:variables
}
