package BattleShip;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

/**
 * High score graphical user interface
 *
 * @author Weinan Cai, Ziliang Wang
 */
public class HighScoreGUI extends javax.swing.JDialog {

    private JTextArea highScoreTextArea;
    private JScrollPane jScrollPane1;
    private JButton okButton;
    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;

    /**
     * Creates new form Board
     */
    public HighScoreGUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        fileReader(highScoreTextArea);

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().
                getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * This method is called from within the constructor to initialize 
     * the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        okButton = new JButton();
        jScrollPane1 = new JScrollPane();
        highScoreTextArea = new JTextArea();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        highScoreTextArea.setEditable(false);
        highScoreTextArea.setColumns(20);
        highScoreTextArea.setRows(5);
        jScrollPane1.setViewportView(highScoreTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addGap(175, 175, 175)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(okButton)
                .addGap(25, 25, 25)));

        getRootPane().setDefaultButton(okButton);

        pack();
    }// </editor-fold>                        

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        doClose(RET_OK);
    }

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        doClose(RET_CANCEL);
    }

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    private int returnStatus = RET_CANCEL;

    /**
     * Read high scores from a text file
     *
     * @param text the text will be read
     */
    private void fileReader(JTextArea text) {
        ArrayList<String> highscore = new ArrayList<String>();
        File file = new File("highscore.txt");
        try {
            if (file.exists()) {
                Scanner in = new Scanner(file);
                while (in.hasNextLine()) {
                    highscore.add(in.nextLine());
                }
            } else {
                highscore.add("No high score at this time");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        if (highscore.get(0).equals("No high score at this time")) {
            text.append(highscore.get(0));
        } else {
            for (int i = 0; i < highscore.size(); i++) {
                text.append((i + 1) + ". " + highscore.get(i));
                text.append("\r\n");
            }
        }
    }
}
