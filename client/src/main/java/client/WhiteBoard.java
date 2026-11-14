package client;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;

public class WhiteBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 3;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN_BG = "\u001B[42m";
    public static final String ANSI_BLACK_TEXT = "\u001B[30m"; // Optional: improves contrast

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        // Set foreground magenta (35) and background dark gray (100)

        printWhiteSquare("R");
        printBlackSquare("N");
        printWhiteSquare("B");
        printBlackSquare("Q");
        printWhiteSquare("K");
        printBlackSquare("B");
        printWhiteSquare("N");
        printBlackSquare("R");
        System.out.println("\u001b[0m");

        for(int i = 0; i < 4; i++){
            printBlackSquare("P");
            printWhiteSquare("P");
        }
        System.out.println("\u001b[0m");
        printEmptyRowW();
        printEmptyRowB();
        printEmptyRowW();
        printEmptyRowB();
        for(int i = 0; i < 4; i++){
            printWhiteSquare("p");
            printBlackSquare("p");
        }
        System.out.println("\u001b[0m");
        printBlackSquare("r");
        printWhiteSquare("n");
        printBlackSquare("b");
        printWhiteSquare("q");
        printBlackSquare("k");
        printWhiteSquare("b");
        printBlackSquare("n");
        printWhiteSquare("r");

    }
    private static void printEmptyRowB(){
        for(int i = 0; i < 4; i++){
            printBlackSquare(" ");
            printWhiteSquare(" ");
        }
        System.out.println("\u001b[0m");
    }
    private static void printEmptyRowW(){
        for(int i = 0; i < 4; i++){
            printWhiteSquare(" ");
            printBlackSquare(" ");
        }
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
