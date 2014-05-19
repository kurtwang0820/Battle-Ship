package BattleShip;

/**
 *
 * @author Ziliang Wang,Weinan Cai
 */
public class Ship {

    private int length;
    private String shipName;
    public int hitsLeft;
    public int direction;//0 horizontal 1 vertical
    public int sRow, sCol, eRow, eCol;

    /**
     * Creat a ship
     *
     * @param name ship name
     * @param len length of the ship
     * @param startRow start row of the ship
     * @param startCol start column of the ship
     * @param endRow end row of the ship
     * @param endCol end column of the ship
     */
    public Ship(String name, int len, int startRow, int startCol, int endRow, int endCol) {
        length = len;
        hitsLeft = len;
        shipName = name;
        sRow = startRow;
        sCol = startCol;
        eRow = endRow;
        eCol = endCol;
        if (startRow == endRow) {
            direction = 0;
        } else {
            direction = 1;
        }
    }

    /**
     * Gets the length of the ship
     *
     * @return the length of the ship
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets ship name
     *
     * @return the ship name
     */
    public String getShipName() {
        return shipName;
    }

    /**
     * If this ship is sunk
     *
     * @return if this ship is sunk
     */
    public boolean isSunk() {
        return hitsLeft == 0;
    }
}
