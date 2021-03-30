package battleship;

import java.util.Arrays;
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

    public int rowNum(String[] letter, int index) {
        return RowLetter.valueOf(letter[index].replaceAll("[0-9]", "")).ordinal();
    }

    public int colNum(String[] num, int index) {

        return Integer.parseInt(num[index].replaceAll("[A-Z]", "")) - 1;
    }

    public void startBoard() {
        Board board = new Board();
        Board hidden = new Board();
        Ship aircraft = new Ship(5, "Aircraft Carrier");
        Ship battleship = new Ship(4, "Battleship");
        Ship submarine = new Ship(3, "Submarine");
        Ship cruiser = new Ship(3, "Cruiser");
        Ship destroyer = new Ship(2, "Destroyer");

        board.printField();
        setShipType(aircraft, board);
        board.printField();
        setShipType(battleship, board);
        board.printField();
        setShipType(submarine, board);
        board.printField();
        setShipType(cruiser, board);
        board.printField();
        setShipType(destroyer, board);
        board.printField();
    }

//    public void gameplay() {
//        System.out.println("The game starts!");
//        printField(hiddenField);
//        System.out.println("Take a shot!");
//        while (true) {
//            String[] attackPos = scanner.nextLine().split(" ");
//            try {
//                if (field[rowLetter(attackPos, 0)][colNum(attackPos, 0)].contains("O")) {
//                    hiddenField[rowLetter(attackPos, 0)][colNum(attackPos, 0)] = "X ";
//                    field[rowLetter(attackPos, 0)][colNum(attackPos, 0)] = "X ";
//                    printField(hiddenField);
//                    System.out.println("You hit a ship!");
//                    printField(field);
//                    break;
//                } else if (field[rowLetter(attackPos, 0)][colNum(attackPos, 0)].contains("~")) {
//                    hiddenField[rowLetter(attackPos, 0)][colNum(attackPos, 0)] = "M ";
//                    field[rowLetter(attackPos, 0)][colNum(attackPos, 0)] = "M ";
//                    printField(hiddenField);
//                    System.out.println("You missed!");
//                    printField(field);
//                    break;
//                }
//            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
//                System.out.println("Error! You entered the wrong coordinates! Try again:");
//            }
//        }
//    }

    public void start() {
        startBoard();
//        gameplay();
    }
}
