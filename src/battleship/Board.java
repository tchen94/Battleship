package battleship;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Board {
    public static final int MAX_ROWS = 10;
    public static final int MAX_COLS = 10;

    public static final Pattern COORDINATE_PATTERN = Pattern.compile(" *([A-Za-z][1-9][0-9]*) *");
    public static final Pattern COORDINATE_PAIR_PATTERN = Pattern.compile(" *([A-Za-z][1-9][0-9]*) *([A-Za-z][1-9][0-9]*) *");

    final private char[][] field;
    private int o;

    public static Optional<String> parseCoordinate(String input) {
        Matcher m = COORDINATE_PATTERN.matcher(input);

        if (m.matches()) {
            return Optional.of(m.group(1));
        }

        return Optional.empty();
    }

    public static Optional<String[]> parseCoordinatePair(String input) {
        Matcher m = COORDINATE_PAIR_PATTERN.matcher(input);

        if (m.matches()) {
            return Optional.of(new String[] { m.group(1), m.group(2) });
        }

        return Optional.empty();
    }

    public static int charToBoardRow(final char letter) {
        final int row = letter - (letter >= 'a' ? 'a' : 'A');
        return row < 0 || row >= MAX_ROWS ? -1 : row;
    }

    public static char boardRowToLetter(final int row) {
        return row < 0 || row >= MAX_ROWS ? 0 : (char) ('A' + row);
    }

    public static int intToBoardCol(int value) {
        return value <= 0 || value > MAX_COLS ? -1 : (value - 1);
    }

    public Board() {
        this.field = new char[MAX_ROWS][MAX_COLS];

        for (char[] row : field) {
            Arrays.fill(row, '~');
        }
    }

    public void printField() {
        for (int colNumber = 1; colNumber <= MAX_COLS; colNumber++) {
            System.out.print(colNumber == 1 ? "  " : " ");
            System.out.print(colNumber);
        }
        System.out.println();

        // Generating playing field here with row labeling
        for (int row = 0; row < MAX_ROWS; row++) {
            System.out.print(boardRowToLetter(row));
            System.out.print(' ');

            for (int col = 0; col < MAX_COLS; col++) {
                System.out.print(field[row][col]);
                System.out.print(" ");
            }
            System.out.println();
        }
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

    public boolean isMiss(int row, int col) {
        return field[row][col] == 'M';
    }

    public boolean isEmpty(int row, int col) {
        return field[row][col] == '~';
    }

    public boolean isFree(int row, int col) {
        try {
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
        } catch (ArrayIndexOutOfBoundsException ignored) {}

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
        final int rMin = Math.min(type.getFirstRow(), type.getSecondRow());
        final int rMax = Math.max(type.getFirstRow(), type.getSecondRow());

        final int cMin = Math.min(type.getFirstCol(), type.getSecondCol());
        final int cMax = Math.min(type.getFirstCol(), type.getSecondCol());

        boolean sunk = true;

        for (int r = rMin; r <= rMax; r++) {
            for (int c = cMin; c <= cMax; c++) {
                if (!isHit(r, c)) {
                    sunk = false;
                }
            }
        }

        type.setStatus(sunk);
    }

    public void setIndex(int row, int col, char status) {
        field[row][col] = status;
    }

    public int getO() {
        return o;
    }
}
