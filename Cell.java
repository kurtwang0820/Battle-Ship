package BattleShip;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 * This class will generate a cell in a JPanel
 *
 * @author Weinan Cai, Ziliang Wang
 */
public class Cell extends JPanel {

    public Color defaultBackground;
    public boolean getSelected = false;
    public boolean isPcBoard = false;
    public boolean putShip = false;
    public boolean gameStart = false;

    public Cell() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                defaultBackground = getBackground();
                if (!isPcBoard && !putShip && !gameStart) {
                    getSelected = true;
                    setBackground(Color.BLUE);
                }
                if (isPcBoard && gameStart) {
                    getSelected = true;
                }
            }
        });
    }

    /**
     * Change the color of the cell
     *
     * @param whatColor the color will be changed to
     */
    public void setColor(Color whatColor) {
        setBackground(whatColor);
    }

    @Override
    /**
     * Set the size of the cell
     */
    public Dimension getPreferredSize() {
        return new Dimension(30, 30);
    }
}
