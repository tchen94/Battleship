package battleship;

import java.util.*;

final public class Battleship {

    final private Scanner scanner;
    private List<Ship> playerOneShips;
    private List<Ship> playerTwoShips;
    private Board playerOneField;
    private Board playerTwoField;
    private Board playerOneHidden;
    private Board playerTwoHidden;
    private int playerOneShipsOnField;
    private int playerTwoShipsOnField;

    public Battleship() {

        this.scanner = new Scanner(System.in);
        this.playerOneShips = new ArrayList<>();
        this.playerTwoShips = new ArrayList<>();
        this.playerOneField = new Board();
        this.playerTwoField = new Board();
        this.playerOneHidden = new Board();
        this.playerTwoHidden = new Board();
        this.playerOneShipsOnField = 0;
        this.playerTwoShipsOnField = 0;
    }

    public void setShipType(Ship type, Board board) {
        System.out.printf("Enter the coordinates of the %s (%d cells):%n", type.getName(), type.getLength());

        while (true) {
            // Separating the letters and number
            Optional<String[]> coordinatePair = Board.parseCoordinatePair(scanner.nextLine());

            if (coordinatePair.isEmpty()) {
                System.out.println("Invalid input. Try again.");
                continue;
            }

            final String[] coordinates = coordinatePair.get();
            int[] rows = { rowNum(coordinates[0]), rowNum(coordinates[1]) };
            int[] cols = { colNum(coordinates[0]), colNum(coordinates[1]) };

            if (rows[0] < 0 || rows[1] < 0 || cols[0] < 0 || cols[1] < 0) {
                System.out.println("Invalid location. Try again.");
                continue;
            }

            int rowDiff = Math.abs(rows[1] - rows[0]);
            int colDiff = Math.abs(cols[1] - cols[0]);

            if (rowDiff == 0 && colDiff != type.getLength() - 1
                || colDiff == 0 && rowDiff != type.getLength() - 1) {
                System.out.printf("Error! Wrong length of the %s! Try again:%n", type.getName());
            } else if (rowDiff != 0 && colDiff != 0) {
                System.out.println("Error! Wrong ship location! Try again:");
            } else if (board.isFree(rows[0], cols[0]) || board.isFree(rows[1], cols[1])) {
                System.out.println("Error! You placed it too close to another one. Try again:");
            } else {
                type.setRowMin(rows[0]);
                type.setColMin(cols[0]);
                type.setRowMax(rows[1]);
                type.setColMax(cols[1]);
                board.placeShip(type);
                System.out.println();
                break;
            }
        }
    }

    public int rowNum(String coordinate) {
        return Board.charToBoardRow(coordinate.charAt(0));
    }

    public int colNum(String coordinate) {
        return Board.intToBoardCol(Integer.parseInt(coordinate, 1, coordinate.length(), 10));
    }

    public void gameplay() {

        boolean playerOneWin = false;
        boolean playerTwoWin = false;

            int row = rowNum(coordinate.get());
            int col = colNum(coordinate.get());

            if (row < 0 || col < 0) {
                System.out.println("Invalid location. Try again.");
                continue;
            }

            try {
                if (board.isShip(row, col)) {
                    hiddenBoard.setIndex(row, col, 'X');
                    board.setIndex(row, col, 'X');
                    hiddenBoard.printField();
                    for (Ship ship : ships) {
                        board.isSunken(ship);
                    }
                    boolean sank = false;
                    for (Ship type : ships) {
                        if (type.isSunken()) {
                            sank = true;
                            ships.remove(type);
                            break;
                        }
                    }
                    if (sank) {
                        System.out.println("You sank a ship! Specify a new target:");
                    } else {
                        System.out.println("You hit a ship! Try again:");
                    }
                    oCounter--;
                } else if (board.isEmpty(row, col)) {
                    hiddenBoard.setIndex(row, col, 'M');
                    board.setIndex(row, col, 'M');
                    hiddenBoard.printField();
                    System.out.println("You missed. Try again:");
                } else if (board.isHit(row, col)) {
                    hiddenBoard.printField();
                    System.out.println("You already hit this one. Try again:");
                } else if (board.isMiss(row, col)) {
                    hiddenBoard.printField();
                    System.out.println("You already missed this one. Try again:");
                }
            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
            }
        }
        System.out.println("You sank the last ship. You won. Congratulations!");
    }

    public void start() {
        gameplay();
    }

    public static void main(String[] args) {
        Battleship game = new Battleship();
        game.start();
    }
}
