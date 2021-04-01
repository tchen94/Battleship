package battleship;

import java.util.Arrays;

public class Board {
    public static final int MAX_ROWS = 10;
    public static final int MAX_COLS = 10;

    final private char[][] field;
    private int o;
    private int damaged;

    public static int letterToRow(final char letter) {
        final int row = letter - 'A';
        return row < 0 || row >= MAX_ROWS ? -1 : row;
    }

    public static char rowToLetter(final int row) {
        return row < 0 || row >= MAX_ROWS ? 0 : (char) ('A' + row);
    }

    public Board() {
        this.field = new char[MAX_ROWS][MAX_COLS];
        this.o = 0;
        this.damaged = 0;

        for (char[] row : field) {
            Arrays.fill(row, '~');
        }
    }


    public void printField() {
        for (int y = 0; y <= field[0].length; y++) {
            if (y == 0) {
                System.out.print("  ");
            } else {
                System.out.print(y + " ");
            }
        }
        // Generating playing field here with row labeling
        for (int row = 0; row < field.length; row++) {
            if (row == 0) {
                System.out.println("  ");
            }

            System.out.print(rowToLetter(row));
            System.out.print(' ');

            for (int col = 0; col < field[row].length; col++) {
                System.out.print(field[row][col]);
                System.out.print(' ');
            }
            System.out.println();
        }
        System.out.println();
    }

    public void placeShip(Ship type) {
        int smallestRow = Math.min(type.getFirstRow(), type.getSecondRow());
        int largestRow = Math.max(type.getFirstRow(), type.getSecondRow());
        int smallestCol = Math.min(type.getFirstCol(), type.getSecondCol());
        int largestCol = Math.max(type.getFirstCol(), type.getSecondCol());
        boolean isHorizontal = type.isHorizontal();

        if (isHorizontal) {
            for (int col = smallestCol; col <= largestCol; col++) {
                field[type.getFirstRow()][col] = 'O';
            }
        } else {
            for (int row = smallestRow; row <= largestRow; row++) {
                field[row][type.getFirstCol()] = 'O';
            }
        }
    }

    public boolean isShip(int row, int col) {
        return field[row][col] == 'O';
    }

    public boolean isHit(int row, int col) {
        return field[row][col] == 'X';
    }

    public boolean isFree(int row, int col) {
        final int rMin = Math.max(row - 1, 0);
        final int rMax = Math.min(row + 1, MAX_ROWS);

        final int cMin = Math.max(col - 1, 0);
        final int cMax = Math.min(col + 1, MAX_COLS);

        for (int r = rMin; r <= rMax; r++) {
            for (int c = cMin; c <= cMax; c++) {
                if (isShip(r, c)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void totalShips() {
        int count = 0;
        for (char[] row : field) {
            for (char c : row) {
                if (c == 'O') {
                    count++;
                }
            }
        }
        this.o = count;
    }

    public void isSunken(Ship type) {
        if (type.isHorizontal()) {
            for (int col = type.getFirstCol(); col <= type.getSecondCol(); col++) {
                if (isHit(type.getFirstRow(), col)) {
                    damaged++;
                }
            }
        } else {
            for (int row = type.getFirstRow(); row <= type.getSecondRow(); row++) {
                if (isHit(row, type.getFirstCol())) {
                    damaged++;
                }
            }
        }
        type.setStatus(damaged == type.getLength());
    }

    public void setIndex(int row, int col, char status) {
        field[row][col] = status;
    }

    public int getO() {
        return o;
    }
}
