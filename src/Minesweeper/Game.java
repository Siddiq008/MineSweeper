package Minesweeper;
public class Game {
    public static void main(String[] args) {
        MineSweeper mineSweeper = MineSweeper.getInstance();
        mineSweeper.initiateBoard();
        mineSweeper.setMinesWithFirstClick();
        mineSweeper.playGame();
    }
}

//I deleted these lines for good visibility


//        while (true){
//            System.out.println("Enter a choice : \n" +
//                    "1. Easy\n" +
//                    "2. Medium\n" +
//                    "3. Hard");
//            int difficultyLevel = scan.nextInt();
//            switch (difficultyLevel){
//                case 1:
//                    BoardRowSize = 10;
//                    BoardColSize = 7;
//                    TotalMines = 10;
//                    break;
//
//                case 2:
//                    BoardRowSize = 15;
//                    BoardColSize = 9;
//                    TotalMines = 12;
//                    break;
//
//                case 3:
//                    BoardRowSize = 20;
//                    BoardColSize = 13;
//                    TotalMines = 18;
//                    break;
//
//                default:
//                    System.out.println("Enter a Valid one");
//                    break;
//
//            }
//
//        }