package Minesweeper;

import java.util.Random;
import java.util.Scanner;

class MineSweeper{
    private final int ROWSIZE;
    private final int COLSIZE;
    private final int TOTALMINES;
    private int [][] board;
    private int [][] mines;
    private final int TOTALSFLAGS;
    private final int TOTALSAFEPOSITIONS;
    private static boolean[][] revealedCells;
    private static int flagCount;
    private static int safePositionsCount;
    private static final int[][] DIRECTIONS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},          {0, 1},
            {1, -1}, {1, 0}, {1, 1}
    };
    public static Scanner scanner = new Scanner(System.in);

    private MineSweeper() {
        this.ROWSIZE = GameConstants.BOARDROWSIZE;
        this.COLSIZE = GameConstants.BOARDCOLSIZE;
        this.TOTALMINES = GameConstants.MINES;
        this.board = new int[ROWSIZE][COLSIZE];
        this.mines = new int[TOTALMINES][2];
        this.TOTALSFLAGS = TOTALMINES;
        this.TOTALSAFEPOSITIONS = ROWSIZE * COLSIZE - TOTALMINES;
        revealedCells = new boolean[ROWSIZE][COLSIZE];
    }
    public static MineSweeper getInstance(){
        return new MineSweeper();
    }

    public void initiateBoard(){
        for (int i = 0; i < ROWSIZE; i++) {
            for (int j = 0; j < COLSIZE; j++) {
                System.out.print("# ");
            }
            System.out.println();
        }
    }
    public void setMinesWithFirstClick(){
        int firstClickRow = getRow();
        int firstClickCol = getCol();
        setMines(firstClickRow, firstClickCol);

        //If you want to know where the mines were actually placed
        //you can uncomment this line
        //showGrid(true);


        setMinesCount();
        digHere(firstClickRow, firstClickCol);
    }
    private void setMines(int firstClickRow, int firstClickCol){
        int minesCount = 0;
        Random random = new Random();
        int row;
        int col;
        while (minesCount < TOTALMINES){
            row = random.nextInt(ROWSIZE);
            col = random.nextInt(COLSIZE);
            if(!isMine(row, col) &&
                    !relatedToFirstClick(firstClickRow, firstClickCol, row, col)){
                mines[minesCount][0] = row;
                mines[minesCount][1] = col;
                board[row][col] = -1;
                minesCount++;
            }
        }
    }
    private boolean isMine(int row, int col){
        return board[row][col] == -1 ;
    }
    private boolean relatedToFirstClick(int firstClickRow, int firstClickCol, int row, int col){
        for (int[] dir : DIRECTIONS) {
            int newRow = firstClickRow + dir[0];
            int newCol = firstClickCol + dir[1];
            if (newRow == row && newCol == col) {
                return true;
            }
        }
        return false;
    }
    private void setMinesCount(){
        for (int i = 0; i < TOTALMINES; i++) {
            int row = mines[i][0];
            int col = mines[i][1];
            for (int[] dir : DIRECTIONS) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                increaseCount(newRow, newCol);
            }
        }

    }

    private void increaseCount(int row, int col){
        if(isInBoundary(row, col) && !isMine(row, col)){
            board[row][col]++;
        }
    }

    public void playGame(){
        while (true){
            int row = getRow();
            int col = getCol();
            selectAction(row, col);
        }
    }

    private void selectAction(int row, int col){
        int choice;
        System.out.println(" Enter a valid option :\n 1. Dig \n 2. Flag \n 3. Un flag");
        while (true){
            choice = scanner.nextInt();
            switch (choice){
                case 1:
                    digHere(row, col);
                    return;
                case 2:
                    setFlag(row, col);
                    return;
                case 3:
                    removeFlag(row, col);
                    return;
                default:
                    System.out.println(" Enter a valid option :\n 1. Dig \n 2. Flag \n 3. Un flag");
            }
        }
    }
    private void setFlag(int row, int col){
        if(revealedCells[row][col]) {
            System.out.println("The cell is already revealed! ");
            return;
        }
        if(isFlag(row, col)){
            System.out.println("There is a flag placed already");
            return;
        }
        if(!isFlagAvailable()){
            System.out.println("Flags un available Dig");
            return;
        }
        flagCount++;
        board[row][col] = -2;
        printGrid();
    }

    private void removeFlag(int row, int col){
        if(revealedCells[row][col]) {
            System.out.println("The cell is already revealed! ");
            return;
        }
        if(board[row][col] != -2){
            System.out.println("Row " +row + " Col " +col + " Is not a flag");
            return;
        }
        board[row][col] = setMinesForCell(row, col);
        flagCount--;
        System.out.println("The flag is removed");
    }
    private int setMinesForCell(int row, int col) {
        int mineCount = 0;

        for (int[] dir : DIRECTIONS) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (isInBoundary(newRow, newCol) && isMine(newRow, newCol)) {
                mineCount++;
            }
        }

        return mineCount; // Return the total count of adjacent mines
    }

    private boolean isFlagAvailable(){
        return flagCount < TOTALSFLAGS;
    }


    private void digHere(int row, int col){
        if(revealedCells[row][col]){
            System.out.println("The cell is already revealed! ");
        } else if (isMine(row, col)) {
            showGrid(false);
        } else if (isFlag(row, col)) {
            System.out.println("There is a flag already! you can't reverse it");
        } else {

            if(board[row][col] != 0){
                revealedCells[row][col] = true;
                safePositionsCount++;
                isAllSafePositionsExposed();
            }else {
                digNear(row, col, new boolean[ROWSIZE][COLSIZE]);
            }
        }

        printGrid();
    }

    private boolean isFlag(int row, int col){
        return board[row][col] == -2;
    }

    private void isAllSafePositionsExposed(){
        if(safePositionsCount >= TOTALSAFEPOSITIONS){
            showGrid(true);
        }
    }

    private void showGrid(boolean win){
        for (int i = 0; i < 5; i++) {
            System.out.println();
        }

        if(win){
            System.out.println("Congrats you won the game ");
        }else {
            System.out.println("You exposed the mine! \n Try again!");
        }

        for (int i = 0; i < ROWSIZE; i++) {
            for (int j = 0; j < COLSIZE; j++) {

                if(board[i][j] == -1){
                    System.out.print("X ");
                } else if (win) {
                    System.out.println(board[i][j] + " ");
                } else if (isFlag(i, j)) {
                    System.out.print(setMinesForCell(i, j) + " ");
                }else if(revealedCells[i][j]){
                    System.out.print(board[i][j] + " ");
                }else {
                    System.out.print("# ");
                }
            }
            System.out.println();
        }

        System.out.println("Whereas X is Mines ");
        if(!win) System.out.println("\nAnd, # is not revealed");
        System.exit(0);
    }

    private void printGrid(){
        for (int i = 0; i < 5; i++) {
            System.out.println();
        }
        for (int i = 0; i < ROWSIZE; i++) {
            for (int j = 0; j < COLSIZE; j++) {
                if(isFlag(i, j)){
                    System.out.print("F ");
                }else {
                    if(revealedCells[i][j]){
                        System.out.print(board[i][j] + " ");
                    }else {
                        System.out.print("# ");
                    }
                }
            }
            System.out.println();
        }
        System.out.println("Flags used : " + flagCount);
        System.out.println("Flags remaining : " + (TOTALSFLAGS - flagCount));
        System.out.println("Safe Positions exposed : " + safePositionsCount);
        System.out.println("Safe positions remaining : " + (TOTALSAFEPOSITIONS - safePositionsCount));
    }

    private void digNear(int row, int col, boolean[][] visited) {
        if (isInBoundary(row, col) && !revealedCells[row][col] && !visited[row][col]) {
            if (board[row][col] == -1) {
                return;
            }

            revealedCells[row][col] = true;
            safePositionsCount++;

            if (board[row][col] == 0) {
                visited[row][col] = true;
                for (int[] dir : DIRECTIONS) {
                    int newRow = row + dir[0];
                    int newCol = col + dir[1];
                    digNear(newRow, newCol, visited);
                }
                visited[row][col] = false;
            }

            isAllSafePositionsExposed();
        }
    }



    private int getRow(){
        int row;
        while (true){
            System.out.println("Enter a row : ");
            row = scanner.nextInt();
            if(row >= 0 && row < ROWSIZE){
                break;
            }
            System.out.println("Enter a valid column between 0 and " + (COLSIZE - 1));
        }
        return row;
    }
    private int getCol(){
        int col;
        while (true){
            System.out.println("Enter a column : ");
            col = scanner.nextInt();
            if(col >= 0 && col < COLSIZE){
                break;
            }
            System.out.println("Enter a valid row between 0 and " + (ROWSIZE - 1));
        }
        return col;
    }

    private boolean isInBoundary(int row, int col){
        return row >= 0 && row < ROWSIZE && col >= 0 && col < COLSIZE;
    }
}