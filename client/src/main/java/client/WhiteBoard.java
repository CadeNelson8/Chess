package client;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class WhiteBoard {

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        whiteBoard();

    }

    public static void blackBoard(){
        header(" hgfedcba ");
        edge(1);
        printWhiteSquare("r");
        printBlackSquare("n");
        printWhiteSquare("b");
        printBlackSquare("q");
        printWhiteSquare("k");
        printBlackSquare("b");
        printWhiteSquare("n");
        printBlackSquare("r");
        edge(1);
        System.out.println("\u001b[0m");
        edge(2);
        for(int i = 0; i < 4; i++){
            printBlackSquare("p");
            printWhiteSquare("p");
        }
        edge(2);
        System.out.println("\u001b[0m");
        printEmptyRowW(3);
        printEmptyRowB(4);
        printEmptyRowW(5);
        printEmptyRowB(6);
        edge(7);
        for(int i = 0; i < 4; i++){
            printWhiteSquare("P");
            printBlackSquare("P");
        }
        edge(7);
        System.out.println("\u001b[0m");
        edge(8);
        printBlackSquare("R");
        printWhiteSquare("N");
        printBlackSquare("K");
        printWhiteSquare("Q");
        printBlackSquare("K");
        printWhiteSquare("B");
        printBlackSquare("N");
        printWhiteSquare("R");
        edge(8);
        System.out.println("\u001b[0m");
        header(" hgfedcba ");
    }

    public static void whiteBoard(){
        header(" abcdefgh ");
        edge(8);
        printWhiteSquare("R");
        printBlackSquare("N");
        printWhiteSquare("B");
        printBlackSquare("Q");
        printWhiteSquare("K");
        printBlackSquare("B");
        printWhiteSquare("N");
        printBlackSquare("R");
        edge(8);
        System.out.println("\u001b[0m");
        edge(7);
        for(int i = 0; i < 4; i++){
            printBlackSquare("P");
            printWhiteSquare("P");
        }
        edge(7);
        System.out.println("\u001b[0m");
        printEmptyRowW(6);
        printEmptyRowB(5);
        printEmptyRowW(4);
        printEmptyRowB(3);
        edge(2);
        for(int i = 0; i < 4; i++){
            printWhiteSquare("p");
            printBlackSquare("p");
        }
        edge(2);
        System.out.println("\u001b[0m");
        edge(1);
        printBlackSquare("r");
        printWhiteSquare("n");
        printBlackSquare("b");
        printWhiteSquare("q");
        printBlackSquare("k");
        printWhiteSquare("b");
        printBlackSquare("n");
        printWhiteSquare("r");
        edge(1);
        System.out.println("\u001b[0m");
        header(" abcdefgh ");
    }

    private static void edge(int e){
        System.out.print("\u001b[30;107m");
        System.out.printf(" %s ", e);
    }

    private static void header(String s){
        System.out.print("\u001b[30;107m");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            System.out.printf(" %s ", c);
        }
        System.out.println("\u001b[0m");
    }

    private static void printEmptyRowB(int row){
        edge(row);
        for(int i = 0; i < 4; i++){
            printBlackSquare(" ");
            printWhiteSquare(" ");
        }
        edge(row);
        System.out.println("\u001b[0m");
    }
    private static void printEmptyRowW(int row){
        edge(row);
        for(int i = 0; i < 4; i++){
            printWhiteSquare(" ");
            printBlackSquare(" ");
        }
        edge(row);
        System.out.println("\u001b[0m");
    }

    private static void printBlackSquare(String c){
        System.out.print("\u001b[37;40m");
        System.out.printf(" %s ", c);
    }

    private static void printWhiteSquare(String c){
        System.out.print("\u001b[30;47m");
        System.out.printf(" %s ", c);
    }

}
