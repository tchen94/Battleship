package battleship;

 final public class Ship {

    final private String name;
    final private int length;
    private boolean horizontal;
    private int rowMin;
    private int colMin;
    private int rowMax;
    private int colMax;

    public Ship(int length, String name) {
        this.name = name;
        this.length = length;
        this.horizontal = false;
        this.rowMin = 0;
        this.colMin = 0;
        this.rowMax = 0;
        this.colMax = 0;
    }

     public String getName() {
         return name;
     }

     public int getLength() {
         return length;
     }

     public void setRowMin(int rowMin) {
         this.rowMin = rowMin;
     }

     public void setRowMax(int rowMax) {
         this.rowMax = rowMax;
     }

     public void setColMin(int colMin) {
         this.colMin = colMin;
     }

     public void setColMax(int colMax) {
         this.colMax = colMax;
     }

     public int getFirstRow() {
         return rowMin;
     }

     public int getSecondRow() {
         return rowMax;
     }

     public int getFirstCol() {
         return colMin;
     }

     public int getSecondCol() {
         return colMax;
     }

     public void setHorizontal() {
        this.horizontal = rowMin == rowMax;
     }

     public boolean isHorizontal() {
         return this.horizontal;
     }
 }
