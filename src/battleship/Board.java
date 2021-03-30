package battleship;

import java.util.Arrays;

public class Board {

    final private RowLetter[] letters;
    final private String[][] field;

    public Board() {
        this.letters = RowLetter.values();
        this.field = new String[10][10];

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
}
