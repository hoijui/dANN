package com.syncleus.core.dann.examples.nci.ui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.util.Random;
import javax.swing.JFileChooser;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;


public class NciDemo extends javax.swing.JFrame implements ActionListener
{
    private final static int BLOCK_WIDTH = 10;
    private final static int BLOCK_HEIGHT = 10;
    private BrainThread brainThread = new BrainThread(0.1, BLOCK_WIDTH, BLOCK_HEIGHT, false);
    private static Random random = new Random();
    private File trainingDirectory = null;
    private File originalImageLocation = null;
    private Image originalImage = null;
    private ImagePanel originalImagePanel = new ImagePanel();
    private BufferedImage finalImage = null;
    private ImagePanel finalImagePanel = new ImagePanel();
    int trainingRemaining = 0;
    int currentTrainingCycles = 1000;



    public NciDemo()
    {
        initComponents();

        this.add(this.originalImagePanel);
        int currentX = this.separator.getX() + 5;
        int currentY = 0;
        this.originalImagePanel.setLocation(currentX, currentY);
        currentY = this.getHeight() / 2;
        this.originalImagePanel.setSize(this.getWidth() - currentX, currentY);
        this.originalImagePanel.setVisible(true);

        this.add(this.finalImagePanel);
        this.finalImagePanel.setLocation(currentX, currentY);
        this.finalImagePanel.setSize(this.getWidth() - currentX, this.getY() - currentY);
        this.finalImagePanel.setVisible(true);

        this.brainThread.start();
        new Timer(100, this).start();
    }



    public void actionPerformed(ActionEvent evt)
    {
        if (this.brainThread.isBusy())
        {
            this.statusLabel.setText("Busy...");
            this.trainButton.setEnabled(false);
            this.processButton.setEnabled(false);
        }
        else
        {
            this.statusLabel.setText("Ready!");
            
            if(this.trainingRemaining <= 0)
            {
                this.trainButton.setEnabled(true);
                this.processButton.setEnabled(true);
            }
            else
            {
                this.trainingRemaining--;
                BufferedImage trainImage = null;
                try
                {
                    trainImage = this.getRandomTrainingBlock(BLOCK_WIDTH, BLOCK_HEIGHT);
                    this.brainThread.setLearning(true);
                    this.brainThread.processImage(trainImage);
                }
                catch(Exception e)
                {
                    System.out.println("Danger will robinson, Danger: " + e);
                    e.printStackTrace();
                    this.trainingRemaining = 0;
                    return;
                }
                
                int progressPercent = ((this.currentTrainingCycles-this.trainingRemaining) * 100)/(this.currentTrainingCycles);
                this.progress.setValue(progressPercent);
            }
        }
    }



    private BufferedImage getRandomTrainingBlock(int width, int height) throws Exception
    {
        Image randomImage = this.getRandomTrainingImage();

        BufferedImage imageBlock = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        while((randomImage.getWidth(null) < 0)||(randomImage.getHeight(null) < 0))
            try{ Thread.sleep(10); }catch(Exception e){}

        int randomX = this.random.nextInt(randomImage.getWidth(null) - width);
        int randomY = this.random.nextInt(randomImage.getHeight(null) - height);

        PixelGrabber grabber = new PixelGrabber(randomImage, randomX, randomY, width, height, true);
        grabber.grabPixels();
        int[] pixels = (int[]) grabber.getPixels();

        for (int currentX = 0; currentX < width; currentX++)
            for (int currentY = 0; currentY < height; currentY++)
                imageBlock.setRGB(currentX, currentY, pixels[currentX + (currentY * width)]);

        return imageBlock;

    }



    private Image getRandomTrainingImage() throws Exception
    {
        File[] files = this.getTrainingImages();
        File randomFile = files[this.random.nextInt(files.length)];
        return Toolkit.getDefaultToolkit().getImage(randomFile.getAbsolutePath());
    }



    private File[] getTrainingImages() throws Exception
    {
        if (this.trainingDirectory == null)
            throw new Exception("Training Directory Not Set");

        return trainingDirectory.listFiles(new PngFileFilter());
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
        processButton = new javax.swing.JButton();
        trainingCylcesInput = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        learningRateInput = new javax.swing.JSpinner();
        separator = new javax.swing.JSeparator();
        statusLabel = new javax.swing.JLabel();
        progress = new javax.swing.JProgressBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        quitMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("NCI Demo");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        trainingDirectoryText.setEditable(false);
        trainingDirectoryText.setText("C:\\nci\\training\\");

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
            originalImageText.setText("C:\\nci\\sample\\original.bmp");

            trainButton.setText("Train");
            trainButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    trainButtonActionPerformed(evt);
                }
            });

            processButton.setText("Process");

            trainingCylcesInput.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1000), Integer.valueOf(1), null, Integer.valueOf(1000)));

            jLabel5.setText("Training Cycles");

            jLabel6.setText("Learning Rate");

            learningRateInput.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.01d), Double.valueOf(0.0010d), null, Double.valueOf(0.01d)));

            separator.setOrientation(javax.swing.SwingConstants.VERTICAL);

            statusLabel.setText("Ready!");

            progress.setStringPainted(true);

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
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(originalImageText, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(originalImageSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(trainingDirectoryText, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(trainingDirectorySelect, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel1)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(76, 76, 76)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(trainButton, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(trainingCylcesInput)
                                        .addComponent(learningRateInput, javax.swing.GroupLayout.PREFERRED_SIZE, 57, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addGap(10, 10, 10)
                                            .addComponent(processButton)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(49, 49, 49)
                                    .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(145, 145, 145)
                            .addComponent(statusLabel)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(separator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(577, 577, 577))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(separator, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(trainingDirectorySelect)
                                .addComponent(trainingDirectoryText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(originalImageSelect)
                                .addComponent(originalImageText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 448, Short.MAX_VALUE)
                            .addComponent(statusLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
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
                                .addComponent(trainButton))
                            .addGap(15, 15, 15))))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

private void quitMenuItemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quitMenuItemMouseReleased
    System.exit(0);
}//GEN-LAST:event_quitMenuItemMouseReleased

private void quitMenuItemMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_quitMenuItemMenuKeyPressed
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
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
    {
        this.originalImageText.setText(chooser.getSelectedFile().getAbsolutePath());
        this.originalImageLocation = chooser.getSelectedFile();

        this.refreshOriginalImage();
    }
}//GEN-LAST:event_originalImageSelectActionPerformed

private void trainingDirectorySelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trainingDirectorySelectActionPerformed
    JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode(chooser.DIRECTORIES_ONLY);
    chooser.setMultiSelectionEnabled(false);
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
    {
        this.trainingDirectoryText.setText(chooser.getSelectedFile().getAbsolutePath());
        this.trainingDirectory = chooser.getSelectedFile();
    }
}//GEN-LAST:event_trainingDirectorySelectActionPerformed

private void trainButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trainButtonActionPerformed
    this.trainButton.setEnabled(false);
    this.processButton.setEnabled(false);

    this.currentTrainingCycles = ((Integer) this.trainingCylcesInput.getValue()).intValue();
    this.trainingRemaining = this.currentTrainingCycles;
}//GEN-LAST:event_trainButtonActionPerformed

private void refreshOriginalImage()
{
    if( this.originalImageLocation == null)
        return;
    
    originalImage = Toolkit.getDefaultToolkit().getImage(this.originalImageLocation.getAbsolutePath());
    this.originalImagePanel.setImage(this.originalImage);
    while((originalImage.getWidth(null) < 0)||(originalImage.getHeight(null)<0))
        try{Thread.sleep(10);}catch(Exception e){}
    this.finalImage = new BufferedImage(originalImage.getWidth(null), originalImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
}

    private void displayAbout()
    {
        AboutDialog about = new AboutDialog(this, true);
        about.setVisible(true);
    }



    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception
    {
        java.awt.EventQueue.invokeLater(new Runnable()
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
    private javax.swing.JButton trainButton;
    private javax.swing.JSpinner trainingCylcesInput;
    private javax.swing.JButton trainingDirectorySelect;
    private javax.swing.JTextField trainingDirectoryText;
    // End of variables declaration//GEN-END:variables
}
