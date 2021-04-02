package battleship;

import java.util.*;

final public class Battleship {

    final private Scanner scanner;

    public Battleship() {

        this.scanner = new Scanner(System.in);
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
        Board board = new Board();
        Board hiddenBoard = new Board();
        List<Ship> ships = new ArrayList<>(Arrays.asList(
            new Ship(5, "Aircraft Carrier"),
             new Ship(4, "Battleship"),
             new Ship(3, "Submarine"),
             new Ship(3, "Cruiser"),
            new Ship(2, "Destroyer")
        ));

        for (final Ship ship : ships) {
            board.printField();
            setShipType(ship, board);
        }

        board.printField();
        board.totalShips();

        System.out.println("The game starts!");
        hiddenBoard.printField();
        System.out.println("Take a shot!");
        int oCounter = board.getO();

        while (oCounter != 0) {
            Optional<String> coordinate = Board.parseCoordinate(scanner.nextLine());

            if (coordinate.isEmpty()) {
                System.out.println("Invalid input. Try again.");
                continue;
            }

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
