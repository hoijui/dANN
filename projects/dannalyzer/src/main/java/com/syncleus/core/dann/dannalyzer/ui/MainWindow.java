package com.syncleus.core.dann.dannalyzer.ui;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


public class MainWindow extends JFrame
{
    private Designer designer = new Designer();
    private static final String COMPONENTS_NODE_TEXT = "Components";
    private static final String NEURONS_NODE_TEXT = "Neurons";
    private static final String NEURON_NODE_TEXT = "Neuron";
    private static final String NEURON_GROUPS_NODE_TEXT = "Neuron Groups";
    private static final String NEURON_GROUP_NODE_TEXT = "Neuron Group";



    public MainWindow()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
            System.out.println("Danger Will Robinson, Danger! Can not set native look and feel! " + e);
            e.printStackTrace();
        }

        initComponents();

        this.paletteTree.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(COMPONENTS_NODE_TEXT);

        DefaultMutableTreeNode neuronsNode = new DefaultMutableTreeNode(NEURONS_NODE_TEXT);
        rootNode.add(neuronsNode);

        DefaultMutableTreeNode neuronNode = new DefaultMutableTreeNode(NEURON_NODE_TEXT);
        neuronsNode.add(neuronNode);

        DefaultMutableTreeNode neuronGroupsNode = new DefaultMutableTreeNode(NEURON_GROUPS_NODE_TEXT);
        rootNode.add(neuronGroupsNode);

        DefaultMutableTreeNode neuronGroupNode = new DefaultMutableTreeNode(NEURON_GROUP_NODE_TEXT);
        neuronGroupsNode.add(neuronGroupNode);

        this.paletteTree.setModel(new DefaultTreeModel(rootNode));
          
//       this.add(this.designer);
//        this.editSplitter.setRightComponent(this.designer);
        this.designer.setLocation(0,0);
        this.designer.setSize(1,1);
        this.designer.setPreferredSize(new Dimension(1,1));
        this.designer.setVisible(true);
        this.designerScrollPane.setViewportView(this.designer);
//        this.designerScrollPane.getViewport().setPreferredSize(new Dimension(2000,2000));
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        editSplitter = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        paletteTree = new javax.swing.JTree();
        designerScrollPane = new javax.swing.JScrollPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        paletteTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                paletteTreeMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(paletteTree);

        editSplitter.setLeftComponent(jScrollPane1);

        designerScrollPane.setAutoscrolls(true);
        editSplitter.setRightComponent(designerScrollPane);

        jMenu1.setText("File");

        jMenuItem1.setText("Quit");
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Help");

        jMenuItem2.setText("About");
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(editSplitter, javax.swing.GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(editSplitter, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void paletteTreeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paletteTreeMousePressed
    int row = this.paletteTree.getRowForLocation(evt.getX(), evt.getY());

    TreePath path = this.paletteTree.getPathForLocation(evt.getX(), evt.getY());

    if (row != -1)
        if (evt.getClickCount() == 2)
        {
            String selectedText = ((DefaultMutableTreeNode)(path.getLastPathComponent())).getUserObject().toString();

            if (selectedText.compareTo(COMPONENTS_NODE_TEXT) == 0)
            {
                //do nothing
            }
            else if (selectedText.compareTo(NEURONS_NODE_TEXT) == 0)
            {
                //do nothing
            }
            else if (selectedText.compareTo(NEURON_NODE_TEXT) == 0)
                this.designer.addDesignerElement(new NeuronDesignerElement());
            else if (selectedText.compareTo(NEURON_GROUPS_NODE_TEXT) == 0)
            {
                //do nothing
            }
            else if (selectedText.compareTo(NEURON_GROUP_NODE_TEXT) == 0)
                this.designer.addDesignerElement(new NeuronGroupDesignerElement());
        }
}//GEN-LAST:event_paletteTreeMousePressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane designerScrollPane;
    private javax.swing.JSplitPane editSplitter;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree paletteTree;
    // End of variables declaration//GEN-END:variables

}
