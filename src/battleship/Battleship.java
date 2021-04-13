package battleship;

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
            int[] rows = {rowNum(coordinates[0]), rowNum(coordinates[1])};
            int[] cols = {colNum(coordinates[0]), colNum(coordinates[1])};

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

                    boolean sank = playerShips.removeIf(Ship::isSunken);

                    if (sank) {

                        if (player == 1) {
                            playerTwoShipsOnField--;
                        } else if (player == 2) {
                            playerOneShipsOnField--;
                        }
                        for (Ship ship : playerShips) {
                            playerField.isSunken(ship);
                        }

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
                if (playerTwoShipsOnField != 0) {
                    enterKeyPrompt();
                }
                break;
            case 2:
                playerTwo();
                playerMove(2, playerOneShips, playerOneField, playerOneHidden);
                player = 1;
                if (playerOneShipsOnField != 0) {
                    enterKeyPrompt();
                }
                break;
        }
    }

    public void enterKeyPrompt() {
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
    }

    public void gameplay() {
        boolean gameOver = false;
        while (!gameOver) {
            playerTurn();
            gameOver = playerOneShipsOnField == 0 || playerTwoShipsOnField == 0;
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
        System.out.println("Player 1, place your ships to the game field");
        System.out.println();
        setPlayerBoard(playerOneShips, playerOneField);
        playerOneField.printField();
        playerOneShipsOnField = playerOneField.totalShips();
        enterKeyPrompt();

        System.out.println("Player 2, place your ships to the game field");
        System.out.println();
        setPlayerBoard(playerTwoShips, playerTwoField);
        playerTwoField.printField();
        playerTwoShipsOnField = playerTwoField.totalShips();
        enterKeyPrompt();

        gameplay();
    }

    public void hijackStart() {
        String[] board = {
                "OOOOO~~~~~",
                "~~~~~~~~~~",
                "OOOO~~~~~~",
                "~~~~~~~~~~",
                "OOO~~~~~~~",
                "~~~~~~~~~~",
                "OOO~~~~~~~",
                "~~~~~~~~~~",
                "OO~~~~~~~~",
                "~~~~~~~~~~"
        };
        Board p1 = new Board();
        p1.hijackField(board);

        Board p2 = new Board();
        p2.hijackField(board);

        playerOneField = p1;
        playerTwoField = p2;

        List<Ship> ships = new ArrayList<>(Arrays.asList(
                new Ship(5, "Aircraft Carrier"),
                new Ship(4, "Battleship"),
                new Ship(3, "Submarine"),
                new Ship(3, "Cruiser"),
                new Ship(2, "Destroyer")
        ));
        playerOneShips.addAll(ships);

        ships = new ArrayList<>(Arrays.asList(
                new Ship(5, "Aircraft Carrier"),
                new Ship(4, "Battleship"),
                new Ship(3, "Submarine"),
                new Ship(3, "Cruiser"),
                new Ship(2, "Destroyer")
        ));
        playerTwoShips.addAll(ships);

        int[][] nums = {
                {0, 0, 4, 0},
                {0, 2, 3, 2},
                {0, 4, 2, 4},
                {0, 6, 2, 6},
                {0, 8, 1, 8}
        };

        for (int i = 0; i < 5; ++i) {
            playerOneShips.get(i).setRowMin(nums[i][0]);
            playerOneShips.get(i).setColMin(nums[i][1]);
            playerOneShips.get(i).setRowMax(nums[i][2]);
            playerOneShips.get(i).setColMax(nums[i][3]);

            playerTwoShips.get(i).setRowMin(nums[i][0]);
            playerTwoShips.get(i).setColMin(nums[i][1]);
            playerTwoShips.get(i).setRowMax(nums[i][2]);
            playerTwoShips.get(i).setColMax(nums[i][3]);
        }

        playerOneShipsOnField = playerOneField.totalShips();
        playerTwoShipsOnField = playerTwoField.totalShips();

        gameplay();
    }

    public static void main(String[] args) {
        Battleship game = new Battleship();
        game.hijackStart();
    }
}
