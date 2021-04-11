package battleship;

import java.io.IOException;
import java.util.*;

final public class Battleship {

    final private Scanner scanner;
    private List<Ship> playerOneShips = new ArrayList<>();
    private List<Ship> playerTwoShips = new ArrayList<>();
    private Board playerOneField = new Board();
    private Board playerTwoField = new Board();
    private Board playerOneHidden = new Board();
    private Board playerTwoHidden = new Board();
    private int playerOneShipsOnField;
    private int playerTwoShipsOnField;
    private int player = 1;

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

    public void playerMove(int player, List<Ship> playerShips, Board playerField, Board hiddenField) {
        System.out.printf("Player %s, it's your turn:%n", player);

        while (true) {
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
                if (playerField.isShip(row, col)) {
                    hiddenField.setIndex(row, col, 'X');
                    playerField.setIndex(row, col, 'X');
                    if (player == 1) {
                        playerTwoShipsOnField--;
                    } else if (player == 2) {
                        playerOneShipsOnField--;
                    }
                    for (Ship ship : playerShips) {
                        playerField.isSunken(ship);
                    }
                    boolean sank = false;
                    for (Ship type : playerShips) {
                        if (type.isSunken()) {
                            sank = true;
                            playerShips.remove(type);
                            break;
                        }
                    }
                    if (sank) {
                        System.out.println("You sank a ship!");
                    } else {
                        System.out.println("You hit a ship!");
                    }
                    break;
                } else if (playerField.isEmpty(row, col)) {
                    hiddenField.setIndex(row, col, 'M');
                    playerField.setIndex(row, col, 'M');
                    System.out.println("You missed!");
                    break;
                } else if (playerField.isHit(row, col)) {
                    System.out.println("You already hit this one.");
                    break;
                } else if (playerField.isMiss(row, col)) {
                    System.out.println("You already missed this one.");
                    break;
                }
            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
            }
        }
    }

    public void playerTurn() {
        switch (player) {
            case 1:
                playerOne();
                playerMove(1, playerTwoShips, playerTwoField, playerTwoHidden);
                player = 2;
                enterKeyPrompt();

            case 2:
                playerTwo();
                playerMove(2, playerOneShips, playerOneField, playerOneHidden);
                player = 1;
                enterKeyPrompt();
        }
    }

    public void enterKeyPrompt() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (IOException e) {
            System.out.println("Please press Enter to continue");
        }
    }

    public void gameplay() {

        while (true) {
            if (playerOneShipsOnField == 0) {
                break;
            } else if (playerTwoShipsOnField == 0) {
                break;
            }
            playerTurn();
        }
        System.out.println("You sank the last ship. You won. Congratulations!");
    }

    public void setPlayerBoard(final List<Ship> playerShips, final Board board) {
        final List<Ship> ships = new ArrayList<>(Arrays.asList(
                new Ship(5, "Aircraft Carrier"),
                new Ship(4, "Battleship"),
                new Ship(3, "Submarine"),
                new Ship(3, "Cruiser"),
                new Ship(2, "Destroyer")
        ));

        playerShips.addAll(ships);

        for (final Ship ship : ships) {
            board.printField();
            setShipType(ship, board);
        }
    }

    public void playerOne() {
        playerTwoHidden.printField();
        System.out.println("---------------------");
        playerOneField.printField();
    }

    public void playerTwo() {
        playerOneHidden.printField();
        System.out.println("---------------------");
        playerTwoField.printField();
    }

    public void start() {

        setPlayerBoard(playerOneShips, playerOneField);
        System.out.println("Press Enter and pass the move to another player");
        String input = scanner.nextLine();

        if (input.isEmpty()) {
            System.out.println("Player 2, place your ships to the game field");
            setPlayerBoard(playerTwoShips, playerTwoField);
        }
        System.out.println("Press Enter and pass the move to another player");
        String secondInput = scanner.nextLine();

        if (secondInput.isEmpty()) {
            playerOneField.totalShips();
            playerTwoField.totalShips();
            playerOneShipsOnField = playerOneField.getO();
            playerTwoShipsOnField = playerTwoField.getO();
            gameplay();
        }
    }

    public static void main(String[] args) {
        Battleship game = new Battleship();
        game.start();
    }
}
