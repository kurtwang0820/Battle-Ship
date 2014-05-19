package BattleShip;

import java.awt.Color;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author Ziliang Wang, Weinan Cai
 */
public class BattleShipPlay {

    private int userHit;
    private int userMiss;
    private int pcHit;
    private int pcMiss;
    private String userName;
    private int[][] userArray;
    private int[][] userArrayBackup;
    private int[][] pcArray;
    private int[][] pcArrayBackup;
    private int[][] pcArrayCheck;
    private int level;
    private static final int EASY = 5;
    private static final int MED = 10;
    private static final int HARD = 15;
    private Grid gameBoard;
    boolean clicked;
    private Ship[] pcShip, userShip;
    private int[] shipPosition = new int[4];
    private boolean isPcHit = false;
    private Ship currentPcHitShip, currentPlayerHitShip;

    public BattleShipPlay() throws InterruptedException {
        userName = JOptionPane.
                showInputDialog("Please enter the player name", userName);
        int option = JOptionPane.showConfirmDialog(null, "Board size: 5x5?", null, JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            level = 1;
        } else {
            option = JOptionPane.showConfirmDialog(null, "Board size: 10x10?", null, JOptionPane.YES_NO_OPTION);
            if (option == 0) {
                level = 2;
            } else {
                option = JOptionPane.showConfirmDialog(null, "Board size will be set up as 15x15",
                        null, JOptionPane.DEFAULT_OPTION);
                level = 3;
            }
        }
        boardIni();
        option = -1;
        option = JOptionPane.showConfirmDialog(null, "Would you like to see high score first?",
                null, JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            final HighScoreGUI dialog =
                    new HighScoreGUI(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    dialog.setVisible(false);
                }
            });
            dialog.setVisible(true);
        }
        userHit = 0;
        userMiss = 0;
        pcHit = 0;
        pcMiss = 0;
        option = -1;
        option = JOptionPane.showConfirmDialog(null, "Do you want to let the computer puts the ships for you",
                null, JOptionPane.YES_NO_OPTION);
        gameBoard.gameStatus.setEditable(false);
        if (option == 1) {
            gameBoard.addStatus("This is " + userName + "'s game");
            gameBoard.addStatus("Difficulity level: " + level);
            showNumOfShip();
            autoShipSet(gameBoard.getPcCell(), pcArray, false, pcShip);
            gameBoard.
                    addStatus("Put the ship by clicking the start cell "
                    + "and then clicking the end cell");
            userShipSet();
            gameBoard.addStatus("All done!");
        } else {
            gameBoard.addStatus("This is " + userName + "'s game");
            gameBoard.addStatus("Difficulity level: " + level);
            showNumOfShip();
            gameBoard.
                    addStatus("The ships will be set up automatically for you");
            autoShipSet(gameBoard.getUserCell(), userArray, true, userShip);
            autoShipSet(gameBoard.getPcCell(), pcArray, false, pcShip);
        }
        arrayCopy();
        String winner = "";
        setLockUserBoard();
        option = -1;
        option = JOptionPane.showConfirmDialog(null, "Do you want to play the game by yourself?",
                null, JOptionPane.YES_NO_OPTION);
        gameBoard.updateStatus("");
        if (option == 0) {
            gameBoard.updateStatus("");
            gameBoard.addStatus("Game start!");
            gameBoard.addStatus(userName + " first");
            winner = startPlaying();
        } else {
            gameBoard.updateStatus("");
            gameBoard.addStatus("The computer will play the game for you");
            winner = autoPlay();
        }
        gameBoard.updateStatus("");
        showResult(winner);

        Thread.sleep(2500);
        option = -1;
        option = JOptionPane.showConfirmDialog(null, "Would you like to see high score at this time?",
                null, JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            final HighScoreGUI dialog = new HighScoreGUI(new JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    dialog.setVisible(false);
                }
            });
            dialog.setVisible(true);
        }
    }

    /**
     * Show number of ships in JTextArea
     */
    private void showNumOfShip() {
        switch (level) {
            case 1:
                gameBoard.addStatus("There are 3 ships (size:5,4,3).");
                break;
            case 2:
                gameBoard.addStatus("There are 5 ships (size:8 5 4 3 2).");
                break;
            case 3:
                gameBoard.addStatus("There are 7 ships (size:12 8 5 4 3 2 2).");
                break;
        }
    }

    /**
     * Copy the boards set by user and computer to a new array.
     */
    private void arrayCopy() {
        userArrayBackup = new int[userArray.length][userArray[0].length];
        pcArrayBackup = new int[userArray.length][userArray[0].length];
        pcArrayCheck = new int[userArray.length][userArray[0].length];
        for (int i = 0; i < userArray.length; i++) {
            for (int j = 0; j < userArray[0].length; j++) {
                userArrayBackup[i][j] = userArray[i][j];
                pcArrayBackup[i][j] = pcArray[i][j];
            }
        }
    }

    /**
     * Initialize the empty board for user and computer
     */
    private void boardIni() {
        if (level == 1) {
            userArray = new int[EASY][EASY];
            pcArray = new int[EASY][EASY];
            gameBoard = new Grid(EASY);
            pcShip = new Ship[3];
            userShip = new Ship[3];
        } else if (level == 2) {
            userArray = new int[MED][MED];
            pcArray = new int[MED][MED];
            gameBoard = new Grid(MED);
            pcShip = new Ship[5];
            userShip = new Ship[5];
        } else {
            userArray = new int[HARD][HARD];
            pcArray = new int[HARD][HARD];
            gameBoard = new Grid(HARD);
            pcShip = new Ship[7];
            userShip = new Ship[7];
        }
    }

    /**
     * User set up its ships
     */
    private void userShipSet() {
        switch (level) {
            case 1:
                gameBoard.addStatus("Please put the large ship (size:5)");
                userPutShip(1, "large");
                userShip[0] = new Ship("large", 5, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                gameBoard.addStatus("Please put the medium ship (size:4)");
                userPutShip(1, "medium");
                userShip[1] = new Ship("medium", 4, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                gameBoard.addStatus("Please put the small ship (size:3)");
                userPutShip(1, "small");
                userShip[2] = new Ship("small", 3, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                break;
            case 2:
                gameBoard.addStatus("Please put the very large ship (size:8)");
                userPutShip(2, "verylarge");
                userShip[0] = new Ship("verylarge", 8, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                gameBoard.addStatus("Please put the large ship (size:5)");
                userPutShip(2, "large");
                userShip[1] = new Ship("large", 5, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                gameBoard.addStatus("Please put the medium ship (size:4)");
                userPutShip(2, "medium");
                userShip[2] = new Ship("medium", 4, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                gameBoard.addStatus("Please put the small ship (size:3)");
                userPutShip(2, "small");
                userShip[3] = new Ship("small", 3, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                gameBoard.addStatus("Please put the very small ship (size:2)");
                userPutShip(2, "verysmall");
                userShip[4] = new Ship("verysmall", 2, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                break;
            case 3:
                gameBoard.addStatus("Please put the super large ship (size:12)");
                userPutShip(3, "superlarge");
                userShip[0] = new Ship("superlarge", 12, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                gameBoard.addStatus("Please put the very large ship (size:8)");
                userPutShip(3, "verylarge");
                userShip[1] = new Ship("verylarge", 8, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                gameBoard.addStatus("Please put the large ship (size:5)");
                userPutShip(3, "large");
                userShip[2] = new Ship("large", 5, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                gameBoard.addStatus("Please put the medium ship (size:4)");
                userPutShip(3, "medium");
                userShip[3] = new Ship("medium", 4, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                gameBoard.addStatus("Please put the small ship (size:3)");
                userPutShip(3, "small");
                userShip[4] = new Ship("small", 3, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                gameBoard.addStatus("Please put the very small ship (size:2)");
                userPutShip(3, "verysmall");
                userShip[5] = new Ship("verysmall", 2, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                gameBoard.addStatus("Please put the very small ship (size:2)");
                userPutShip(3, "verysmall");
                userShip[6] = new Ship("verysmall", 2, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                break;
        }
    }

    /**
     * User put the ship on the board by themselves
     *
     * @param temp the two diminitional array will hold the data for the ships
     * @param level the difficulty level
     * @param shipSize the ship you want to set up
     */
    private void userPutShip(int level, String shipSize) {
        int col0 = -1;
        int row0 = -1;
        int col1 = -1;
        int row1 = -1;
        do {
            int[] rowCol = new int[2];
            while (!clicked) {
                rowCol = getSelectedCell(gameBoard.getUserCell());
                if (row0 == -1) {
                    row0 = rowCol[0];
                }
                if (col0 == -1) {
                    col0 = rowCol[1];
                }
                if (row0 != -1 && col0 != -1) {
                    gameBoard.getUserCell()[row0][col0].getSelected = false;
                    clicked = true;
                }
            }
            clicked = false;
            while (!clicked) {
                rowCol = getSelectedCell(gameBoard.getUserCell());
                if (row1 == -1) {
                    row1 = rowCol[0];
                }
                if (col1 == -1) {
                    col1 = rowCol[1];
                }
                if (row1 != -1 && col1 != -1) {
                    gameBoard.getUserCell()[row1][col1].getSelected = false;
                    clicked = true;
                }
            }
            clicked = false;
            if (!shipCheck(userArray, level, shipSize, row0, row1, col0, col1)) {
                gameBoard.
                        addStatus("Invalid position,please put the ship again");
                gameBoard.getUserCell()[row0][col0].setColor(null);
                gameBoard.getUserCell()[row1][col1].setColor(null);
                gameBoard.getUserCell()[row0][col0].putShip = false;
                gameBoard.getUserCell()[row1][col1].putShip = false;
                col0 = -1;
                row0 = -1;
                col1 = -1;
                row1 = -1;
            }
        } while (!shipCheck(userArray, level, shipSize, row0, row1, col0, col1));

        if (row0 == row1) {
            for (int i = Math.min(col0, col1); i <= Math.max(col0, col1); i++) {
                userArray[row0][i] = 1;
                gameBoard.getUserCell()[row0][i].setColor(Color.BLUE);
            }
        } else if (col0 == col1) {
            for (int i = Math.min(row0, row1); i <= Math.max(row0, row1); i++) {
                userArray[i][col0] = 1;
                gameBoard.getUserCell()[i][col0].setColor(Color.BLUE);
            }
        }
        shipPosition[0] = Math.min(row0, row1);
        shipPosition[1] = Math.min(col0, col1);
        shipPosition[2] = Math.max(row0, row1);
        shipPosition[3] = Math.max(col0, col1);
    }

    /**
     * Check if user has selected a cell on the panel
     *
     * @param theCell user's cell array
     * @return the row, column of the cell the user selected
     */
    public int[] getSelectedCell(Cell[][] theCell) {
        int[] rowCol = {-1, -1};
        for (int i = 0; i < theCell.length; i++) {
            for (int j = 0; j < theCell[0].length; j++) {
                if (theCell[i][j].getSelected && !theCell[i][j].putShip) {
                    rowCol[0] = i;
                    rowCol[1] = j;
                    theCell[i][j].putShip = true;
                    return rowCol;
                }
            }
        }
        return rowCol;
    }

    /**
     * Automatically set up the ships
     *
     * @param theCell the cell array that stores the information
     * @param temp the cell array that stores the information
     * @param isPlayer if it is a human player
     */
    private void autoShipSet(Cell[][] theCell, int[][] temp, boolean isPlayer, Ship[] whosShip) {
        switch (level) {
            case 1:
                autoPutShip(theCell, temp, 1, "large", isPlayer);
                whosShip[0] = new Ship("large", 5, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                autoPutShip(theCell, temp, 1, "medium", isPlayer);
                whosShip[1] = new Ship("medium", 4, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                autoPutShip(theCell, temp, 1, "small", isPlayer);
                whosShip[2] = new Ship("small", 3, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                break;
            case 2:
                autoPutShip(theCell, temp, 2, "verylarge", isPlayer);
                whosShip[0] = new Ship("verylarge", 8, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                autoPutShip(theCell, temp, 2, "large", isPlayer);
                whosShip[1] = new Ship("large", 5, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                autoPutShip(theCell, temp, 2, "medium", isPlayer);
                whosShip[2] = new Ship("medium", 4, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                autoPutShip(theCell, temp, 2, "small", isPlayer);
                whosShip[3] = new Ship("small", 3, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                autoPutShip(theCell, temp, 2, "verysmall", isPlayer);
                whosShip[4] = new Ship("verysmall", 2, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                break;
            case 3:
                autoPutShip(theCell, temp, 3, "superlarge", isPlayer);
                whosShip[0] = new Ship("superlarge", 12, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                autoPutShip(theCell, temp, 3, "verylarge", isPlayer);
                whosShip[1] = new Ship("verylarge", 8, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                autoPutShip(theCell, temp, 3, "large", isPlayer);
                whosShip[2] = new Ship("large", 5, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                autoPutShip(theCell, temp, 3, "medium", isPlayer);
                whosShip[3] = new Ship("medium", 4, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                autoPutShip(theCell, temp, 3, "small", isPlayer);
                whosShip[4] = new Ship("small", 3, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                autoPutShip(theCell, temp, 3, "verysmall", isPlayer);
                whosShip[5] = new Ship("verysmall", 2, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                autoPutShip(theCell, temp, 3, "verysmall", isPlayer);
                whosShip[6] = new Ship("verysmall", 2, shipPosition[0], shipPosition[1], shipPosition[2], shipPosition[3]);
                break;
        }
    }

    /**
     * Automatically put the ship on the board.
     *
     * @param temp the two diminitional array will hold the data for the ships
     * @param level the difficulty level
     * @param shipSize the ship you want to set up
     * @param isPlayer if it is a human player
     */
    private void autoPutShip(Cell[][] theCell, int[][] temp, int level,
            String shipSize, boolean isPlayer) {
        Random generator = new Random();
        int col0 = -1;
        int row0 = -1;
        int col1 = -1;
        int row1 = -1;
        switch (level) {
            case 1:
                do {
                    switch (generator.nextInt(2)) {
                        case 0:
                            row0 = generator.nextInt(5);
                            row1 = row0;
                            col0 = generator.nextInt(5);
                            col1 = generator.nextInt(5);
                            break;
                        case 1:
                            col0 = generator.nextInt(5);
                            col1 = col0;
                            row0 = generator.nextInt(5);
                            row1 = generator.nextInt(5);
                            break;
                    }

                } while (!shipCheck(temp, level, shipSize, row0, row1, col0, col1));
                break;
            case 2:
                do {
                    switch (generator.nextInt(2)) {
                        case 0:
                            row0 = generator.nextInt(10);
                            row1 = row0;
                            col0 = generator.nextInt(10);
                            col1 = generator.nextInt(10);
                            break;
                        case 1:
                            col0 = generator.nextInt(10);
                            col1 = col0;
                            row0 = generator.nextInt(10);
                            row1 = generator.nextInt(10);
                            break;
                    }

                } while (!shipCheck(temp, level, shipSize, row0, row1, col0, col1));
                break;
            case 3:
                do {
                    switch (generator.nextInt(2)) {
                        case 0:
                            row0 = generator.nextInt(15);
                            row1 = row0;
                            col0 = generator.nextInt(15);
                            col1 = generator.nextInt(15);
                            break;
                        case 1:
                            col0 = generator.nextInt(15);
                            col1 = col0;
                            row0 = generator.nextInt(15);
                            row1 = generator.nextInt(15);
                            break;
                    }

                } while (!shipCheck(temp, level, shipSize, row0, row1, col0, col1));
                break;
        }
        if (row0 == row1) {
            for (int i = Math.min(col0, col1); i <= Math.max(col0, col1); i++) {
                temp[row0][i] = 1;
                if (isPlayer) {
                    theCell[row0][i].setColor(Color.BLUE);
                }
            }
        } else if (col0 == col1) {
            for (int i = Math.min(row0, row1); i <= Math.max(row0, row1); i++) {
                temp[i][col0] = 1;
                if (isPlayer) {
                    theCell[i][col0].setColor(Color.BLUE);
                }
            }
        }
        shipPosition[0] = Math.min(row0, row1);
        shipPosition[1] = Math.min(col0, col1);
        shipPosition[2] = Math.max(row0, row1);
        shipPosition[3] = Math.max(col0, col1);
    }

    /**
     * Check if the ship is put in the right place
     *
     * @param temp the two diminitional array will hold the data for the ships
     * @param level the difficulty level
     * @param shipSize the ship will be set up
     * @param row0 row number of the start position
     * @param row1 row number of the end position
     * @param col0 col number of the start position
     * @param col1 col number of the end position
     * @return
     */
    private boolean shipCheck(int[][] temp, int level, String shipSize, int row0, int row1, int col0, int col1) {
        boolean shipOk = true;
        switch (level) {
            case 1:
                switch (shipSize) {
                    case "large":
                        if (row0 > EASY || row1 > EASY || col0 > EASY || col1 > EASY || row0 < 0 || row1 < 0 || col0 < 0 || col1 < 0) {
                            shipOk = false;
                            break;
                        }
                        if (((row0 == row1) && (Math.abs(col1 - col0) + 1) != 5) || ((col0 == col1) && (Math.abs(row1 - row0) + 1) != 5) || ((row0 != row1) && (col0 != col1))) {
                            shipOk = false;
                            break;
                        }
                        if (collisionCheck(temp, row0, row1, col0, col1)) {
                            shipOk = false;
                            break;
                        }
                        break;
                    case "medium":
                        if (row0 > EASY || row1 > EASY || col0 > EASY || col1 > EASY || row0 < 0 || row1 < 0 || col0 < 0 || col1 < 0) {
                            shipOk = false;
                            break;
                        }
                        if (((row0 == row1) && (Math.abs(col1 - col0) + 1) != 4) || ((col0 == col1) && (Math.abs(row1 - row0) + 1) != 4) || ((row0 != row1) && (col0 != col1))) {
                            shipOk = false;
                            break;
                        }
                        if (collisionCheck(temp, row0, row1, col0, col1)) {
                            shipOk = false;
                            break;
                        }
                        break;
                    case "small":
                        if (row0 > EASY || row1 > EASY || col0 > EASY || col1 > EASY || row0 < 0 || row1 < 0 || col0 < 0 || col1 < 0) {
                            shipOk = false;
                            break;
                        }
                        if (((row0 == row1) && (Math.abs(col1 - col0) + 1) != 3) || ((col0 == col1) && (Math.abs(row1 - row0) + 1) != 3) || ((row0 != row1) && (col0 != col1))) {
                            shipOk = false;
                            break;
                        }
                        if (collisionCheck(temp, row0, row1, col0, col1)) {
                            shipOk = false;
                            break;
                        }
                        break;
                }
                break;
            case 2:
                switch (shipSize) {
                    case "verylarge":
                        if (row0 > MED || row1 > MED || col0 > MED || col1 > MED || row0 < 0 || row1 < 0 || col0 < 0 || col1 < 0) {
                            shipOk = false;
                            break;
                        }
                        if (((row0 == row1) && (Math.abs(col1 - col0) + 1) != 8) || ((col0 == col1) && (Math.abs(row1 - row0) + 1) != 8) || ((row0 != row1) && (col0 != col1))) {
                            shipOk = false;
                            break;
                        }
                        if (collisionCheck(temp, row0, row1, col0, col1)) {
                            shipOk = false;
                            break;
                        }
                        break;
                    case "large":
                        if (row0 > MED || row1 > MED || col0 > MED || col1 > MED || row0 < 0 || row1 < 0 || col0 < 0 || col1 < 0) {
                            shipOk = false;
                            break;
                        }
                        if (((row0 == row1) && (Math.abs(col1 - col0) + 1) != 5) || ((col0 == col1) && (Math.abs(row1 - row0) + 1) != 5) || ((row0 != row1) && (col0 != col1))) {
                            shipOk = false;
                            break;
                        }
                        if (collisionCheck(temp, row0, row1, col0, col1)) {
                            shipOk = false;
                            break;
                        }
                        break;
                    case "medium":
                        if (row0 > MED || row1 > MED || col0 > MED || col1 > MED || row0 < 0 || row1 < 0 || col0 < 0 || col1 < 0) {
                            shipOk = false;
                            break;
                        }
                        if (((row0 == row1) && (Math.abs(col1 - col0) + 1) != 4) || ((col0 == col1) && (Math.abs(row1 - row0) + 1) != 4) || ((row0 != row1) && (col0 != col1))) {
                            shipOk = false;
                            break;
                        }
                        if (collisionCheck(temp, row0, row1, col0, col1)) {
                            shipOk = false;
                            break;
                        }
                        break;
                    case "small":
                        if (row0 > MED || row1 > MED || col0 > MED || col1 > MED || row0 < 0 || row1 < 0 || col0 < 0 || col1 < 0) {
                            shipOk = false;
                            break;
                        }
                        if (((row0 == row1) && (Math.abs(col1 - col0) + 1) != 3) || ((col0 == col1) && (Math.abs(row1 - row0) + 1) != 3) || ((row0 != row1) && (col0 != col1))) {
                            shipOk = false;
                            break;
                        }
                        if (collisionCheck(temp, row0, row1, col0, col1)) {
                            shipOk = false;
                            break;
                        }
                        break;
                    case "verysmall":
                        if (row0 > MED || row1 > MED || col0 > MED || col1 > MED || row0 < 0 || row1 < 0 || col0 < 0 || col1 < 0) {
                            shipOk = false;
                            break;
                        }
                        if (((row0 == row1) && (Math.abs(col1 - col0) + 1) != 2) || ((col0 == col1) && (Math.abs(row1 - row0) + 1) != 2) || ((row0 != row1) && (col0 != col1))) {
                            shipOk = false;
                            break;
                        }
                        if (collisionCheck(temp, row0, row1, col0, col1)) {
                            shipOk = false;
                            break;
                        }
                        break;
                }
                break;
            case 3:
                switch (shipSize) {
                    case "superlarge":
                        if (row0 > HARD || row1 > HARD || col0 > HARD || col1 > HARD || row0 < 0 || row1 < 0 || col0 < 0 || col1 < 0) {
                            shipOk = false;
                            break;
                        }
                        if (((row0 == row1) && (Math.abs(col1 - col0) + 1) != 12) || ((col0 == col1) && (Math.abs(row1 - row0) + 1) != 12) || ((row0 != row1) && (col0 != col1))) {
                            shipOk = false;
                            break;
                        }
                        if (collisionCheck(temp, row0, row1, col0, col1)) {
                            shipOk = false;
                            break;
                        }
                        break;
                    case "verylarge":
                        if (row0 > HARD || row1 > HARD || col0 > HARD || col1 > HARD || row0 < 0 || row1 < 0 || col0 < 0 || col1 < 0) {
                            shipOk = false;
                            break;
                        }
                        if (((row0 == row1) && (Math.abs(col1 - col0) + 1) != 8) || ((col0 == col1) && (Math.abs(row1 - row0) + 1) != 8) || ((row0 != row1) && (col0 != col1))) {
                            shipOk = false;
                            break;
                        }
                        if (collisionCheck(temp, row0, row1, col0, col1)) {
                            shipOk = false;
                            break;
                        }
                        break;
                    case "large":
                        if (row0 > HARD || row1 > HARD || col0 > HARD || col1 > HARD || row0 < 0 || row1 < 0 || col0 < 0 || col1 < 0) {
                            shipOk = false;
                            break;
                        }
                        if (((row0 == row1) && (Math.abs(col1 - col0) + 1) != 5) || ((col0 == col1) && (Math.abs(row1 - row0) + 1) != 5) || ((row0 != row1) && (col0 != col1))) {
                            shipOk = false;
                            break;
                        }
                        if (collisionCheck(temp, row0, row1, col0, col1)) {
                            shipOk = false;
                            break;
                        }
                        break;
                    case "medium":
                        if (row0 > HARD || row1 > HARD || col0 > HARD || col1 > HARD || row0 < 0 || row1 < 0 || col0 < 0 || col1 < 0) {
                            shipOk = false;
                            break;
                        }
                        if (((row0 == row1) && (Math.abs(col1 - col0) + 1) != 4) || ((col0 == col1) && (Math.abs(row1 - row0) + 1) != 4) || ((row0 != row1) && (col0 != col1))) {
                            shipOk = false;
                            break;
                        }
                        if (collisionCheck(temp, row0, row1, col0, col1)) {
                            shipOk = false;
                            break;
                        }
                        break;
                    case "small":
                        if (row0 > HARD || row1 > HARD || col0 > HARD || col1 > HARD || row0 < 0 || row1 < 0 || col0 < 0 || col1 < 0) {
                            shipOk = false;
                            break;
                        }
                        if (((row0 == row1) && (Math.abs(col1 - col0) + 1) != 3) || ((col0 == col1) && (Math.abs(row1 - row0) + 1) != 3) || ((row0 != row1) && (col0 != col1))) {
                            shipOk = false;
                            break;
                        }
                        if (collisionCheck(temp, row0, row1, col0, col1)) {
                            shipOk = false;
                            break;
                        }
                        break;
                    case "verysmall":
                        if (row0 > HARD || row1 > HARD || col0 > HARD || col1 > HARD || row0 < 0 || row1 < 0 || col0 < 0 || col1 < 0) {
                            shipOk = false;
                            break;
                        }
                        if (((row0 == row1) && (Math.abs(col1 - col0) + 1) != 2) || ((col0 == col1) && (Math.abs(row1 - row0) + 1) != 2) || ((row0 != row1) && (col0 != col1))) {
                            shipOk = false;
                            break;
                        }
                        if (collisionCheck(temp, row0, row1, col0, col1)) {
                            shipOk = false;
                            break;
                        }
                        break;
                }
                break;
        }
        return shipOk;
    }

    /**
     * Check if there is already a ship
     *
     * @param temp the two diminitional array will hold the data for the ships
     * @param row0 row number of the start position
     * @param row1 row number of the end position
     * @param col0 col number of the start position
     * @param col1 col number of the end position
     * @return
     */
    private boolean collisionCheck(int[][] temp, int row0, int row1, int col0, int col1) {
        boolean find = false;
        if (row0 == row1) {
            for (int i = Math.min(col0, col1); i <= Math.max(col0, col1); i++) {
                if (temp[row0][i] == 1) {
                    find = true;
                    break;
                }
            }
        } else if (col0 == col1) {
            for (int i = Math.min(row0, row1); i <= Math.max(row0, row1); i++) {
                if (temp[i][col0] == 1) {
                    find = true;
                    break;
                }
            }
        }
        return find;
    }

    /**
     * Automatically play the game
     *
     * @return the winner
     */
    private String autoPlay() throws InterruptedException {
        String winner = "";
        while (true) {
            Thread.sleep(500);//if you want to test the program,just delete this line to save time
            userAutoPlay();
            gameBoard.toBottom();
            if (winCheck(pcArrayBackup)) {
                winner = userName;
                break;
            }
            Thread.sleep(500);//if you want to test the program,just delete this line to save time
            pcPlay();
            gameBoard.toBottom();
            if (winCheck(userArrayBackup)) {
                winner = "PC";
                break;
            }
        }
        return winner;
    }

    /**
     * Starting playing
     *
     * @return the winner
     */
    private String startPlaying() throws InterruptedException {
        String winner = "";
        while (true) {
            userPlay();
            gameBoard.toBottom();
            if (winCheck(pcArrayBackup)) {
                winner = userName;
                break;
            }
            Thread.sleep(1000);
            pcPlay();
            gameBoard.toBottom();
            if (winCheck(userArrayBackup)) {
                winner = "PC";
                break;
            }
        }
        return winner;
    }

    /**
     * Lock user's board then human cannot select its cell
     */
    private void setLockUserBoard() {
        for (int i = 0; i < gameBoard.getUserCell().length; i++) {
            for (int j = 0; j < gameBoard.getUserCell().length; j++) {
                gameBoard.getUserCell()[i][j].gameStart = true;
            }
        }
        for (int i = 0; i < gameBoard.getPcCell().length; i++) {
            for (int j = 0; j < gameBoard.getPcCell().length; j++) {
                gameBoard.getPcCell()[i][j].gameStart = true;
            }
        }
    }

    /**
     * User play by itself
     */
    private void userPlay() {
        int row = -1;
        int col = -1;
        boolean redun = false;
        do {
            int[] rowCol = new int[2];
            while (!clicked) {
                rowCol = getSelectedCell(gameBoard.getPcCell());
                if (row == -1) {
                    row = rowCol[0];
                }
                if (col == -1) {
                    col = rowCol[1];
                }
                if (row != -1 && col != -1) {
                    gameBoard.getPcCell()[row][col].getSelected = false;
                    clicked = true;
                }
            }
            clicked = false;
            if (pcArrayBackup[row][col] == 1) {
                pcArrayCheck[row][col] = -1;
                pcArrayBackup[row][col] = -1;
                gameBoard.getPcCell()[row][col].setColor(Color.ORANGE);
                userHit++;
                getUserHitShip(row, col);
                currentPlayerHitShip.hitsLeft--;
                gameBoard.addStatus(userName + " Hit!!!");
                if (currentPlayerHitShip.isSunk()) {
                    gameBoard.addStatus("PC just lost a ship(size:" + currentPlayerHitShip.getLength() + ")");
                }
                redun = false;
            } else if (pcArrayBackup[row][col] == 0) {
                pcArrayCheck[row][col] = -2;
                pcArrayBackup[row][col] = -2;
                gameBoard.getPcCell()[row][col].setColor(Color.BLACK);
                userMiss++;
                gameBoard.addStatus(userName + " Miss...");
                redun = false;
            } else {
                redun = true;
                gameBoard.addStatus("You have already hit this cell.");
            }
        } while (redun);
    }

    /**
     * Computer plays for the user
     */
    private void userAutoPlay() {
        int row = -1;
        int col = -1;
        boolean redun = false;
        Random generator = new Random();
        do {
            switch (level) {
                case 1:
                    row = generator.nextInt(5);
                    col = generator.nextInt(5);
                    break;
                case 2:
                    row = generator.nextInt(10);
                    col = generator.nextInt(10);
                    break;
                case 3:
                    row = generator.nextInt(15);
                    col = generator.nextInt(15);
                    break;
            }
            if (pcArrayBackup[row][col] == 1) {
                pcArrayCheck[row][col] = -1;
                pcArrayBackup[row][col] = -1;
                gameBoard.getPcCell()[row][col].setColor(Color.ORANGE);
                userHit++;
                getUserHitShip(row, col);
                currentPlayerHitShip.hitsLeft--;
                gameBoard.addStatus(userName + " Hit!!!");
                if (currentPlayerHitShip.isSunk()) {
                    gameBoard.addStatus("PC just lost a ship(size:" + currentPlayerHitShip.getLength() + ")");
                }
                redun = false;
            } else if (pcArrayBackup[row][col] == 0) {
                pcArrayCheck[row][col] = -2;
                pcArrayBackup[row][col] = -2;
                gameBoard.getPcCell()[row][col].setColor(Color.BLACK);
                userMiss++;
                gameBoard.addStatus(userName + " Miss...");
                redun = false;
            } else {
                redun = true;
            }
        } while (redun);
    }

    /**
     * Get the ship the user hits in this turn
     *
     * @param row hitted row
     * @param col hitted column
     * @param whosShip whose ship array
     */
    private void getUserHitShip(int row, int col) {
        for (int i = 0; i < pcShip.length; i++) {
            Ship tmp = pcShip[i];
            if (tmp.direction == 0) {
                if (tmp.sRow == row) {
                    for (int j = tmp.sCol; j <= tmp.eCol; j++) {
                        if (col == j) {
                            currentPlayerHitShip = pcShip[i];
                            break;
                        }
                    }
                }
            } else {
                if (tmp.sCol == col) {
                    for (int j = tmp.sRow; j <= tmp.eRow; j++) {
                        if (row == j) {
                            currentPlayerHitShip = pcShip[i];
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Computer plays
     */
    private void pcPlay() {
        int row = -1;
        int col = -1;
        boolean redun = false;
        Random generator = new Random();
        do {
            switch (level) {
                case 1:
                    row = generator.nextInt(5);
                    col = generator.nextInt(5);
                    break;
                case 2:
                    row = generator.nextInt(10);
                    col = generator.nextInt(10);
                    break;
                case 3:
                    row = generator.nextInt(15);
                    col = generator.nextInt(15);
                    break;
            }
            if (isPcHit) {
                row = getNextHitPosition(currentPcHitShip)[0];
                col = getNextHitPosition(currentPcHitShip)[1];
            }
            if (userArrayBackup[row][col] == 1) {
                userArrayBackup[row][col] = -1;
                gameBoard.getUserCell()[row][col].setColor(Color.ORANGE);
                pcHit++;
                //if (!thereIsPlayerShip) {
                getHitShip(row, col);
                //    thereIsPlayerShip = true;
                //}
                currentPcHitShip.hitsLeft--;
                isPcHit = true;
                gameBoard.addStatus("PC Hit!!!");
                if (currentPcHitShip.isSunk()) {
                    isPcHit = false;
                    //  thereIsPlayerShip = false;
                    gameBoard.addStatus("You just lost a ship(size:" + currentPcHitShip.getLength() + ")");
                }
                redun = false;
            } else if (userArrayBackup[row][col] == 0) {
                userArrayBackup[row][col] = -2;
                gameBoard.getUserCell()[row][col].setColor(Color.BLACK);
                pcMiss++;
                gameBoard.addStatus("PC Miss...");
                isPcHit = false;
                redun = false;
            } else {
                redun = true;
            }
        } while (redun);
    }

    /**
     * Get the ship the pc hits in this turn
     *
     * @param row hitted row
     * @param col hitted column
     * @param whosShip whose ship array
     */
    private void getHitShip(int row, int col) {
        for (int i = 0; i < userShip.length; i++) {
            Ship tmp = userShip[i];
            if (tmp.direction == 0) {
                if (tmp.sRow == row) {
                    for (int j = tmp.sCol; j <= tmp.eCol; j++) {
                        if (col == j) {
                            currentPcHitShip = userShip[i];
                            break;
                        }
                    }
                }
            } else {
                if (tmp.sCol == col) {
                    for (int j = tmp.sRow; j <= tmp.eRow; j++) {
                        if (row == j) {
                            currentPcHitShip = userShip[i];
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * To choose which cell to hit next
     *
     * @param currentShip which ship the pc hits in this turn
     * @return row and cloumn number
     */
    private int[] getNextHitPosition(Ship currentShip) {
        int[] hitPosition = new int[2];
        if (currentShip.direction == 0) {
            for (int i = currentShip.sCol; i <= currentShip.eCol; i++) {
                if (userArrayBackup[currentShip.sRow][i] != -1 && userArrayBackup[currentShip.sRow][i] != -2) {
                    hitPosition[0] = currentShip.sRow;
                    hitPosition[1] = i;
                    break;
                }
            }
        } else {
            for (int i = currentShip.sRow; i <= currentShip.eRow; i++) {
                if (userArrayBackup[i][currentShip.sCol] != -1 && userArrayBackup[i][currentShip.sCol] != -2) {
                    hitPosition[0] = i;
                    hitPosition[1] = currentShip.sCol;
                    break;
                }
            }
        }
        return hitPosition;
    }

    /**
     * Check if there is a winner
     *
     * @param temp the board you want to check
     * @return if there is a winner
     */
    private boolean winCheck(int[][] temp) {
        boolean win = true;
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length; j++) {
                if (temp[i][j] == 1) {
                    win = false;
                    break;
                }
            }
        }
        return win;
    }

    /**
     * Show the game result in JTextArea
     *
     * @param winner the winner
     */
    private void showResult(String winner) {
        if (winner.equals(userName)) {
            highScoreWriter();
            gameBoard.addStatus("Congratulations!!! You win!!!!");
            gameBoard.addStatus("User Data:");
            gameBoard.addStatus("Hit: " + userHit);
            gameBoard.addStatus("Miss: " + userMiss);
            gameBoard.addStatus("Accuracy rate: " + Math.round(100 * (float) userHit / (float) (userHit + userMiss)) + "%");
            gameBoard.addStatus("PC Data:");
            gameBoard.addStatus("Hit: " + pcHit);
            gameBoard.addStatus("Miss: " + pcMiss);
            gameBoard.addStatus("Accuracy rate: " + Math.round(100 * (float) pcHit / (float) (pcHit + pcMiss)) + "%");
        } else {
            gameBoard.addStatus("Game Over");
            gameBoard.addStatus("PC Data:");
            gameBoard.addStatus("Hit: " + pcHit);
            gameBoard.addStatus("Miss: " + pcMiss);
            gameBoard.addStatus("Accuracy rate: " + Math.round(100 * (float) pcHit / (float) (pcHit + pcMiss)) + "%");
            gameBoard.addStatus("User Data:");
            gameBoard.addStatus("Hit: " + userHit);
            gameBoard.addStatus("Miss: " + userMiss);
            gameBoard.addStatus("Accuracy rate: " + Math.round(100 * (float) userHit / (float) (userHit + userMiss)) + "%");
        }
    }

    /**
     * Write the high score to the txt file
     */
    private void highScoreWriter() {
        ArrayList<String> highscore = new ArrayList<String>();
        try {
            File file = new File("highscore.txt");
            if (file.exists()) {
                Scanner in = new Scanner(file);
                while (in.hasNextLine()) {
                    highscore.add(in.nextLine());
                }
                file.delete();
            }
            file.createNewFile();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String thisScore = userName + "'s miss rate: \t" + Math.round(100 * (float) userHit / (float) (userHit + userMiss)) + "% \t" + df.format(new Date());
            highscore.add(thisScore);
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            String[] highScoreSorted = new String[highscore.size()];
            for (int i = 0; i < highscore.size(); i++) {
                highScoreSorted[i] = highscore.get(i);
            }
            for (int i = 0; i < highScoreSorted.length; i++) {
                String min = highScoreSorted[i];
                int index = i;
                for (int j = i; j < highScoreSorted.length; j++) {
                    if (min.substring(highScoreSorted[i].indexOf("%") - 2,
                            highScoreSorted[i].indexOf("%")).compareTo(highScoreSorted[j].substring(highScoreSorted[j].indexOf("%") - 2,
                            highScoreSorted[j].indexOf("%"))) > 0) {
                        index = j;
                    }
                }
                highScoreSorted[i] = highScoreSorted[index];
                highScoreSorted[index] = min;
            }
            for (int i = 0; i < Math.min(highScoreSorted.length, 100); i++) {
                output.write(highScoreSorted[i]);
                output.write("\r\n");
            }
            output.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
