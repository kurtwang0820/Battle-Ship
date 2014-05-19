package BattleShip;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 * This class will draw a panel for the user/pc
 *
 * @author Weinan Cai,Ziliang Wang
 */
public class Pane extends JPanel {

    private int size;
    private Cell[][] whosCell;

    public Pane(int theSize, boolean isPcBoard) {
        size = theSize;
        setLayout(new GridBagLayout());
        whosCell = new Cell[size][size];
        GridBagConstraints gbc = new GridBagConstraints();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                gbc.gridx = col;
                gbc.gridy = row;
                Cell cellPane = new Cell();
                if (isPcBoard) {
                    cellPane.isPcBoard = true;
                }
                whosCell[row][col] = cellPane;
                Border border = null;
                if (row < size - 1) {
                    if (col < size - 1) {
                        border = new MatteBorder(1, 1, 0, 0, Color.GRAY);
                    } else {
                        border = new MatteBorder(1, 1, 0, 1, Color.GRAY);
                    }
                } else {
                    if (col < size - 1) {
                        border = new MatteBorder(1, 1, 1, 0, Color.GRAY);
                    } else {
                        border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                    }
                }
                cellPane.setBorder(border);
                add(cellPane, gbc);
            }
        }
    }

    /**
     * Gets player/pc's cell
     *
     * @return player/pc's cell
     */
    public Cell[][] getCell() {
        return whosCell;
    }
}
