package BattleShip;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

/**
 * This class will draw grids on the panel
 *
 * @author Weinan Cai, Ziliang Wang
 */
public class Grid {

    int gridSize;
    Pane user;
    Pane pc;
    JScrollPane statusScroll;
    JScrollBar sBar;
    JTextArea gameStatus;
    JFrame boardFrame;
    Cell[][] userCell;
    Cell[][] pcCell;

    public Grid(int size) {
        Container a, b;
        gridSize = size;
        statusScroll = new JScrollPane();
        gameStatus = new JTextArea();
        boardFrame = new JFrame();
        boardFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameStatus.setEditable(false);
        gameStatus.setColumns(20);
        gameStatus.setRows(5);
        statusScroll.setViewportView(gameStatus);
        statusScroll.setAutoscrolls(true);
        user = new Pane(gridSize, false);
        pc = new Pane(gridSize, true);
        userCell = new Cell[gridSize][gridSize];
        userCell = user.getCell();
        pcCell = new Cell[gridSize][gridSize];
        pcCell = pc.getCell();
        a = boardFrame.getContentPane();
        b = boardFrame.getContentPane();
        a.add(user, BorderLayout.WEST);
        b.add(pc, BorderLayout.EAST);
        a.add(statusScroll, BorderLayout.CENTER);
        sBar= statusScroll.getVerticalScrollBar();
        sBar.setValue(sBar.getMaximum());
        boardFrame.setSize(800, 800);
        boardFrame.setResizable(false);
        boardFrame.pack();
        boardFrame.setVisible(true);
    }

    /**
     * Get user's cell
     *
     * @return user's cell
     */
    public Cell[][] getUserCell() {
        return userCell;
    }

    /**
     * Move the scroll to bottom
     */
    public void toBottom(){
        sBar.setValue(sBar.getMaximum());
    }
    /**
     * Change the text content in JTextArea
     *
     * @param s the content you want to show
     */
    public void updateStatus(String s) {
        gameStatus.setText(s);
    }

    /**
     * Add text content in JTextArea
     *
     * @param s the content you want to add
     */
    public void addStatus(String s) {
        gameStatus.append(s + "\n");
    }

    /**
     * Get pc's cell
     *
     * @return pc's cell
     */
    public Cell[][] getPcCell() {
        return pcCell;
    }
}
