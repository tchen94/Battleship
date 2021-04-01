package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

final public class Battleship {

    final private Scanner scanner;
    private int o;

    public Battleship() {

        this.scanner = new Scanner(System.in);
        this.o = 0;
    }

    public void setShipType(Ship type, Board board) {
        System.out.printf("Enter the coordinates of the %s (%d cells):%n", type.getName(), type.getLength());

        while (true) {
            // Separating the letters and number
            String[] ship = scanner.nextLine().split(" ");

            if (rowNum(ship, 0) == rowNum(ship, 1) &&
                    Math.abs(colNum(ship, 0) - colNum(ship, 1)) != type.getLength() - 1 ||
                    colNum(ship, 0) == colNum(ship, 1) &&
                            Math.abs(rowNum(ship, 0) - rowNum(ship, 1)) != type.getLength() - 1) {
                System.out.printf("Error! Wrong length of the %s! Try again:%n", type.getName());
            } else if (rowNum(ship, 0) != rowNum(ship, 1) &&
                    colNum(ship, 0) != colNum(ship, 1)) {
                System.out.println("Error! Wrong ship location! Try again:");
            } else if (board.isFree(rowNum(ship, 0), colNum(ship, 0)) ||
                    board.isFree(rowNum(ship, 1), colNum(ship, 1))) {
                System.out.println("Error! You placed it too close to another one. Try again:");
            } else {
                type.setRowMin(rowNum(ship, 0));
                type.setColMin(colNum(ship, 0));
                type.setRowMax(rowNum(ship, 1));
                type.setColMax(colNum(ship, 1));
                type.setHorizontal();
                board.placeShip(type);
                System.out.println();
                break;
            }
        }
    }

    public int rowNum(String[] letters, int index) {
        final String letter = letters[index].replaceAll("[0-9]", "");

        return Board.letterToRow(letter.charAt(0));
    }

    public int colNum(String[] num, int index) {

        return Integer.parseInt(num[index].replaceAll("[A-Z]", "")) - 1;
    }

    public void gameplay() {
        Board board = new Board();
        Board hiddenBoard = new Board();
        List<Ship> ships = new ArrayList<>();
        Ship aircraft = new Ship(5, "Aircraft Carrier");
        Ship battleship = new Ship(4, "Battleship");
        Ship submarine = new Ship(3, "Submarine");
        Ship cruiser = new Ship(3, "Cruiser");
        Ship destroyer = new Ship(2, "Destroyer");

        ships.add(aircraft);
        ships.add(battleship);
        ships.add(submarine);
        ships.add(cruiser);
        ships.add(destroyer);

        board.printField();
        setShipType(aircraft, board);
        board.printField();
//        setShipType(battleship, board);
//        board.printField();
//        setShipType(submarine, board);
//        board.printField();
//        setShipType(cruiser, board);
//        board.printField();
        setShipType(destroyer, board);
        board.printField();
        board.totalShips();

        System.out.println("The game starts!");
        hiddenBoard.printField();
        System.out.println("Take a shot!");
        int oCounter = board.getO();

        while (oCounter != 0) {
            String[] attackPos = scanner.nextLine().split(" ");
            int row = rowNum(attackPos, 0);
            int col = colNum(attackPos, 0);

            try {
                if (checkHit(board, row, col)) {
                    hiddenBoard.setIndex(row, col, "X ");
                    board.setIndex(row, col, "X ");
                    hiddenBoard.printField();
                    for (Ship ship : ships) {
                        board.isSunken(ship, board);
                    }
                    for (Ship type : ships) {
                        if (type.isSunken()) {
                            System.out.println("You sank a ship! Specify a new target:");
                            ships.remove(type);
                        } else {
                            System.out.println("You hit a ship! Try again:");
                        }
                    }
                    oCounter--;
                } else if (!checkHit(board, row, col)) {
                    hiddenBoard.setIndex(row, col, "M ");
                    board.setIndex(row, col, "M ");
                    hiddenBoard.printField();
                    System.out.println("You missed. Try again:");
                }
            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
            }
        }
        System.out.println("You sank the last ship. You won. Congratulations!");
    }

    public boolean checkHit(Board board, int row, int col) {
        return board.getIndex(row, col).contains("O");
    }

    public void start() {
        gameplay();
    }

    public static void main(String[] args) {
        Battleship game = new Battleship();
        game.start();
    }
}
