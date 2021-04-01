package battleship;

import java.util.Arrays;

public class Board {

    final private RowLetter[] letters;
    final private String[][] field;
    private int o;
    private int damaged;

    public Board() {
        this.letters = RowLetter.values();
        this.field = new String[10][10];
        this.o = 0;
        this.damaged = 0;

        for (String[] strings : field) {
            Arrays.fill(strings, "~ ");
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
            System.out.print(letters[row] + " ");
            for (int col = 0; col < field[row].length; col++) {
                System.out.print(field[row][col]);
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
                field[type.getFirstRow()][col] = "O ";
            }
        } else {
            for (int row = smallestRow; row <= largestRow; row++) {
                field[row][type.getFirstCol()] = "O ";
            }
        }
    }

    public boolean isFree(int row, int col) {
        try {
            if (field[row - 1][col - 1].contains("O") || field[row - 1][col].contains("O") ||
                    field[row - 1][col + 1].contains("O")) {
                return true;
            }
            if (field[row][col + 1].contains("O") || field[row][col - 1].contains("O")) {
                return true;
            }
            if (field[row + 1][col - 1].contains("O") || field[row + 1][col].contains("O") ||
                    field[row + 1][col + 1].contains("O")) {
                return true;
            }
        } catch (IndexOutOfBoundsException ignored) {
        }
        return false;
    }

    public void totalShips() {
        int count = 0;
        for (String[] strings : field) {
            for (String string : strings) {
                this.o = string.contains("O") ? count++ : count;
            }
        }
        this.o = count;
    }

    public void isSunken(Ship type, Board board) {
        if (type.isHorizontal()) {
            for (int col = type.getFirstCol(); col <= type.getSecondCol(); col++) {
                if (board.getIndex(type.getFirstRow(), col).contains("X")) {
                    damaged++;
                }
            }
        } else {
            for (int row = type.getFirstRow(); row <= type.getSecondRow(); row++) {
                if (board.getIndex(row, type.getFirstCol()).contains("X")) {
                    damaged++;
                }
            }
        }
        type.setStatus(damaged == type.getLength());
    }

    public void setIndex(int row, int col, String status) {
        field[row][col] = status;
    }

    public String getIndex(int row, int col) {
        return field[row][col];
    }

    public int getO() {
        return o;
    }
}
