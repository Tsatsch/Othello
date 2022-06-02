import szte.mi.Move;

import java.util.ArrayList;

public class Field {

    public Square[][] field;
    public static int counterBLACK = 0;
    public static int counterWHITE = 0;
    public static User player = new User();
    public static ArrayList<String> currentSuggestions;
    public static boolean won = false;
    public static boolean draw = false;
    public static int winner  = Discs.EMPTY;

    // init of start field
    public void initStartField(){
       field = new Square[8][8];
        for (int i = 0; i <field.length ; i++) {
            for (int j = 0; j <field.length; j++) {
                String coordinates = Integer.toString(i) + Integer.toString(j);
                field[i][j] = new Square(coordinates,Discs.EMPTY);
            }
        }
        field[3][3].setValue(Discs.WHITE); field[4][4].setValue(Discs.WHITE);
        field[3][4].setValue(Discs.BLACK); field[4][3].setValue(Discs.BLACK);
    }

    // get suggestions for current player
    public void printSuggestions(){
        currentSuggestions = new ArrayList<>();
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if(field[i][j].getValue() == 4){
                    currentSuggestions.add(field[i][j].getCoordinates());
                }
            }
        }
        //System.err.println("Valid moves coordinates for player with disc "+ player.getDisc() + ": " + currentSuggestions);
    }

    /**
     * suggestions generator for player with disc <userDisc>
     */
    public void updateSuggestion(int userDisc){
        resetPrevSuggestions();
        chekLeftRight(userDisc);
        checkTopDown(userDisc);
        checkDiagRightLeft(userDisc);
        checkDiagLeftRight(userDisc);
    }

    // before updating suggestion delete all previous
    public void resetPrevSuggestions(){
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j <field.length; j++) {
                if (field[i][j].getValue() == Discs.SUGGESTION){
                    field[i][j].setValue(Discs.EMPTY);
                }
            }
        }
    }

    /**
     * suggestions generators according to discs of user on field
     * - left right line
     * - top down line
     * - right left diagonal
     * - left right diagonal
     */
    public void chekLeftRight(int userDisc){
        int complementaryDisk;
        if (userDisc == Discs.BLACK) {
            complementaryDisk = Discs.WHITE;
        } else complementaryDisk = Discs.BLACK;

        // left-line
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (field[i][j].getValue() == userDisc) {
                    int col = j;
                    int counter = 0;
                    while (col > 0){
                        if (field[i][col-1].getValue() == Discs.EMPTY && counter > 0){
                            field[i][col-1].setValue(Discs.SUGGESTION);
                            break;
                        }
                        if (field[i][col-1].getValue() == complementaryDisk){
                            counter++;
                            col--;
                        }
                        else break;
                    }
                }
            }
        }

        // right-line
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (field[i][j].getValue() == userDisc) {
                    int col = j;
                    int counter = 0;
                    while (col < field.length-1){
                        if (field[i][col+1].getValue() == Discs.EMPTY && counter > 0){
                            field[i][col+1].setValue(Discs.SUGGESTION);
                            break;
                        }
                        if (field[i][col+1].getValue() == complementaryDisk){
                            counter++;
                            col++;
                        }
                        else break;
                    }
                }
            }
        }
    }

    public void checkTopDown(int userDisc) {
        int complementaryDisk;
        if (userDisc == Discs.BLACK) {
            complementaryDisk = Discs.WHITE;
        } else complementaryDisk = Discs.BLACK;

        // upline
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (field[i][j].getValue() == userDisc) {
                    int row = i;
                    int counter = 0;
                    while (row > 0){
                        if (field[row-1][j].getValue() == Discs.EMPTY && counter > 0){
                            field[row-1][j].setValue(Discs.SUGGESTION);
                            break;
                        }
                        if (field[row-1][j].getValue() == complementaryDisk){
                            counter++;
                            row--;
                        }
                        else break;
                    }
                }
            }
        }
        // downline
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (field[i][j].getValue() == userDisc) {
                    int row = i;
                    int counter = 0;
                    while (row < field.length-1){
                        if (field[row+1][j].getValue() == Discs.EMPTY && counter > 0){
                            field[row+1][j].setValue(Discs.SUGGESTION);
                            break;
                        }
                        if (field[row+1][j].getValue() == complementaryDisk){
                            counter++;
                            row++;
                        }
                        else break;
                    }
                }
            }
        }
    }

    public void checkDiagRightLeft(int userDisc) {
        int complementaryDisk;

        if (userDisc == Discs.BLACK) {
            complementaryDisk = Discs.WHITE;
        } else complementaryDisk = Discs.BLACK;

        // left down
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (field[i][j].getValue() == userDisc) {
                    int row = i;
                    int col = j;
                    int counter = 0;
                    while (row < field.length-1 && col > 0) {
                        if (field[row + 1][col - 1].getValue() == Discs.EMPTY && counter > 0) {
                            field[row + 1][col - 1].setValue(Discs.SUGGESTION);
                            break;
                        }
                        if (field[row + 1][col - 1].getValue() == complementaryDisk) {
                            counter++;
                            row++;
                            col--;
                        } else break;
                    }
                }
            }
        }

        // rigth up down
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (field[i][j].getValue() == userDisc) {
                    int row = i;
                    int col = j;
                    int counter = 0;
                    while (row > 0 && col < field.length-1) {
                        if (field[row - 1][col + 1].getValue() == Discs.EMPTY  && counter > 0) {
                            field[row - 1][col + 1].setValue(Discs.SUGGESTION);
                            break;
                        }
                        if (field[row - 1][col + 1].getValue() == complementaryDisk) {
                            counter++;
                            row--;
                            col++;
                        } else break;
                    }
                }
            }
        }
    }

    public void checkDiagLeftRight(int userDisc){
        int complementaryDisk;

        if (userDisc == Discs.BLACK) {
            complementaryDisk = Discs.WHITE;
        } else complementaryDisk = Discs.BLACK;

        // left up
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (field[i][j].getValue() == userDisc) {
                    int row = i;
                    int col = j;
                    int counter = 0;
                    while (row > 0 && col > 0){
                        if (field[row-1][col-1].getValue() == Discs.EMPTY && counter > 0){
                            field[row-1][col-1].setValue(Discs.SUGGESTION);
                            break;
                        }
                        if (field[row-1][col-1].getValue() == complementaryDisk){
                            counter++;
                            row--;
                            col--;
                        }
                        else break;
                    }
                }
            }
        }
        // right down
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (field[i][j].getValue() == userDisc) {
                    int row = i;
                    int col = j;
                    int counter = 0;
                    while (row < field.length-1 && col < field.length-1){
                        if (field[row+1][col+1].getValue() == Discs.EMPTY && counter > 0){
                            field[row+1][col+1].setValue(Discs.SUGGESTION);
                            break;
                        }
                        if (field[row+1][col+1].getValue() == complementaryDisk){
                            counter++;
                            row++;
                            col++;
                        }
                        else break;
                    }
                }
            }
        }

    }

    /**
     * make valid user move
     */
    public void makeMove(Move move, int player){

        if (move == null || (move.x == -1 && move.y == -1) ){
            System.out.println("passing");
        }
        else if(field[move.x][move.y].getValue() == Discs.SUGGESTION){
            field[move.x][move.y].setValue(player);
        }
        else {
            System.err.println("Not a valid move, you can go only on suggestions (4)!");
        }
    }

    /** if some discs are outflanked, flip them over*/
    public void updateAfterMove(Move move) {
        if (move == null || move.x == -1 && move.y == -1) {
            System.out.println("passing");
        } else {
            int currentdisk = field[move.x][move.y].getValue();
            int complementaryDisk;

            if (currentdisk == 0) {
                complementaryDisk = 1;
            } else complementaryDisk = 0;

            for (int i = 0; i < field.length; i++) {
                for (int j = 0; j < field.length; j++) {
                    if (field[i][j] == field[move.x][move.y]) {
                        int row = i;
                        // check down
                        ArrayList<Square> saved7 = new ArrayList<>();
                        while (row < field.length - 1) {
                            if (field[row + 1][j].getValue() > 1) {
                                break;
                            }
                            if (field[row + 1][j].getValue() == complementaryDisk) {
                                saved7.add(field[row + 1][j]);
                            }
                            if (field[row + 1][j].getValue() == currentdisk) {
                                for (Square square : saved7) {
                                    square.setValue(currentdisk);
                                }
                                break;
                            }
                            row++;
                        }

                        row = i;
                        // check up
                        ArrayList<Square> saved6 = new ArrayList<>();
                        while (row > 0) {
                            if (field[row - 1][j].getValue() == complementaryDisk) {
                                saved6.add(field[row - 1][j]);
                            }
                            if (field[row - 1][j].getValue() > 1) {
                                break;
                            }
                            if (field[row - 1][j].getValue() == currentdisk) {
                                for (Square square : saved6) {
                                    square.setValue(currentdisk);
                                }
                                break;
                            }
                            row--;
                        }

                        int col = j;
                        // check right
                        ArrayList<Square> saved5 = new ArrayList<>();
                        while (col < field.length - 1) {
                            if (field[i][col + 1].getValue() == complementaryDisk) {
                                saved5.add(field[i][col + 1]);
                            }
                            if (field[i][col + 1].getValue() > 1) {
                                break;
                            }
                            if (field[i][col + 1].getValue() == currentdisk) {
                                for (Square square : saved5) {
                                    square.setValue(currentdisk);
                                }
                                break;
                            }
                            col++;
                        }

                        col = j;
                        // check left
                        ArrayList<Square> saved4 = new ArrayList<>();
                        while (col > 0) {
                            if (field[i][col - 1].getValue() == complementaryDisk) {
                                saved4.add(field[i][col - 1]);
                            }
                            if (field[i][col - 1].getValue() > 1) {
                                break;
                            }
                            if (field[i][col - 1].getValue() == currentdisk) {
                                for (Square square : saved4) {
                                    square.setValue(currentdisk);
                                }
                                break;
                            }
                            col--;
                        }

                        row = i;
                        col = j;
                        // check left-up
                        ArrayList<Square> saved3 = new ArrayList<>();
                        while (row > 0 && col > 0) {
                            if (field[row - 1][col - 1].getValue() == complementaryDisk) {
                                saved3.add(field[row - 1][col - 1]);
                            }
                            if (field[row - 1][col - 1].getValue() > 1) {
                                break;
                            }
                            if (field[row - 1][col - 1].getValue() == currentdisk) {
                                for (Square square : saved3) {
                                    square.setValue(currentdisk);
                                }
                                break;
                            }
                            row--;
                            col--;
                        }

                        row = i;
                        col = j;
                        // check right down
                        ArrayList<Square> saved2 = new ArrayList<>();
                        while (row < field.length - 1 && col < field.length - 1) {
                            if (field[row + 1][col + 1].getValue() == complementaryDisk) {
                                saved2.add(field[row + 1][col + 1]);
                            }
                            if (field[row + 1][col + 1].getValue() > 1) {
                                break;
                            }
                            if (field[row + 1][col + 1].getValue() == currentdisk) {
                                for (Square square : saved2) {
                                    square.setValue(currentdisk);
                                }
                                break;
                            }
                            row++;
                            col++;
                        }

                        row = i;
                        col = j;
                        // check left down
                        ArrayList<Square> saved1 = new ArrayList<>();
                        while (row < field.length - 1 && col > 0) {
                            if (field[row + 1][col - 1].getValue() == complementaryDisk) {
                                saved1.add(field[row + 1][col - 1]);
                            }
                            if (field[row + 1][col - 1].getValue() > 1) {
                                break;
                            }
                            if (field[row + 1][col - 1].getValue() == currentdisk) {
                                for (Square square : saved1) {
                                    square.setValue(currentdisk);
                                }
                                break;
                            }
                            row++;
                            col--;
                        }
                        row = i;
                        col = j;
                        // check right up -> this one surely works
                        ArrayList<Square> saved0 = new ArrayList<>();
                        while (row > 0 && col < field.length - 1) {
                            if (field[row - 1][col + 1].getValue() == complementaryDisk) {
                                saved0.add(field[row - 1][col + 1]);
                            }
                            if (field[row - 1][col + 1].getValue() > 1) {
                                break;
                            }
                            if (field[row - 1][col + 1].getValue() == currentdisk) {
                                for (Square square : saved0) {
                                    square.setValue(currentdisk);
                                }
                                break;
                            }
                            row--;
                            col++;
                        }
                    }
                }
            }
        }
    }

    /**
     * counter of current discs on field
     */
    public void counting(){
        counterWHITE = 0;
        counterBLACK = 0;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if(field[i][j].getValue() == Discs.WHITE){
                    counterWHITE++;
                }
                else if (field[i][j].getValue() == Discs.BLACK){
                    counterBLACK++;
                }
            }
        }
    }
    // player switcher for cmdLine version
    public void playerSwitchOne() {
        if (player.getDisc() == Discs.BLACK)
            player.setDisc(Discs.WHITE);
        else
            player.setDisc(Discs.BLACK);
    }

    /**
     check for end of game
     */
    public void checkWinOrDraw(Square[][] field) {
        counting();
        // if field is full
        if(counterBLACK == 0 || counterWHITE == 0){
            won = true;
        }
        if (counterWHITE + counterBLACK == 64) {
            if (counterBLACK == counterWHITE) {
                draw = true;
            } else {
                won = true;
            }
//        } else {
//            // no suggestion possible
//            int suggestionCounter = 0;
//            for (int i = 0; i < field.length; i++) {
//                for (int j = 0; j < field.length; j++) {
//                    if (field[i][j].getValue() == Discs.SUGGESTION) {
//                        suggestionCounter++;
//                    }
//                }
//            }
//            if (suggestionCounter == 0 && (counterBLACK != counterWHITE)) {
//                System.out.println("wtfwtf");
//                won = true;
//                if (counterBLACK > counterWHITE) {
//                    winner = Discs.BLACK;
//                }
//                if (counterBLACK < counterWHITE) {
//                    winner = Discs.WHITE;
//                }
//            } else if (suggestionCounter == 0) {
//                draw = true;
//            }
        }
    }

    /**cmdline matrix table view printer*/
    public void printField(){
        System.out.println("-----------------------");
        for (int r = 0; r < field.length; r++) {
            for (int c = 0; c < field[r].length; c++)
                System.out.print(field[r][c].getValue()+ " ");
            System.out.println();
        }
        System.out.println("-----------------------");
    }
}
