import szte.mi.Move;
import szte.mi.Player;

import java.util.*;

public class Bot implements Player {

    private int orderBot;
    private Random random;
    private long t;
    private Move prevMove;
    Move userMove;
    Move botMove;
    public static int orderUser = 0;
    public Field field;
    public Field temporaryField;

    public static boolean isGuiGame = false;
    public ArrayList<String> validMoves;


    public int getOrderBot() {
        return orderBot;
    }

    public long getRemainingTime() {
        return t;
    }

    public Move getPrevMove() {
        return prevMove;
    }

    public void setPrevMove(Move move) {
        prevMove = move;
    }

    public void subtractTime(long time) {
        t -= time;
    }


    @Override
    public void init(int order, long t, Random rnd) {
        this.orderBot = order;
        this.t = t;
        this.random = rnd;

        if (order == 0) {
            orderUser = 1;
        } else {
            orderUser = 0;
        }

        if (!isGuiGame) {
            field = new Field();
            field.initStartField();
            field.updateSuggestion(orderUser);
            field.printField();
            field.printSuggestions();
        } else {
            field = GUI_Main.field;
        }
    }

    @Override
    public Move nextMove(Move prevMove, long tOpponent, long t) {
        // gui
        if (isGuiGame) {
            getValidMoves(field);
            if (validMoves.isEmpty()) {
                return new Move(-1,-1);
            }
            if (validMoves.size() > 1) {
                if (validMoves.contains("00") || validMoves.contains("77") || validMoves.contains("70") || validMoves.contains("07")) {
                    Move botMove = new Move(-1, -1);
                    for (int i = 0; i < validMoves.size(); i++) {
                        if (validMoves.get(i).equals("00")) {
                            botMove = new Move(0, 0);
                        }
                        if (validMoves.get(i).equals("07")) {
                            botMove = new Move(0, 7);
                        }
                        if (validMoves.get(i).equals("70")) {
                            botMove = new Move(7, 0);
                        }
                        if (validMoves.get(i).equals("77")) {
                            botMove = new Move(7, 7);
                        }
                    }
                    return botMove;

                } else {
                    // make copy of field
                    ArrayList<Integer> turnedDiscs = new ArrayList<>();
                    ArrayList<Integer> opponentValidMoves = new ArrayList<>();

                    for (int j = 0; j < validMoves.size(); j++) {
                        String koordinates = validMoves.get(j);
                        int x = Character.getNumericValue(koordinates.charAt(0));
                        int y = Character.getNumericValue(koordinates.charAt(1));
                        temporaryField = new Field();
                        temporaryField.field = new Square[field.field.length][];
                        for (int i = 0; i < field.field.length; i++) {
                            temporaryField.field[i] = Arrays.copyOf(field.field[i].clone(), field.field[i].length);
                        }
                        for (int i = 0; i < 8; i++) {
                            for (int k = 0; k < 8; k++) {
                                String coordinates = Integer.toString(i) + Integer.toString(j);
                                Square square = new Square(coordinates, field.field[i][k].getValue());
                                temporaryField.field[i][k] = square;
                            }
                        }
                        int before = countDiscs(temporaryField, orderBot);
                        botMove = new Move(x, y);
                        temporaryField.makeMove(botMove, orderBot);
                        temporaryField.updateAfterMove(botMove);
                        int after = countDiscs(temporaryField, orderBot);
                        int diff = after - before;
                        turnedDiscs.add(diff);

                        temporaryField.updateSuggestion(orderUser); // get opponents suggestions
                        opponentValidMoves.add(countDiscs(temporaryField, 4));
                    }

                    // if mulitple max elements, chose the one with less possible moves for oponent
                    int max = Collections.max(turnedDiscs);
                    String bestCoordinates = "";
                    int min;
                    boolean searching = true;
                    while (searching) {
                        min = Collections.min(opponentValidMoves);
                        int index = opponentValidMoves.indexOf(min);
                        if (turnedDiscs.get(index) == max) {
                            searching = false;
                            bestCoordinates = validMoves.get(index);
                        } else {
                            opponentValidMoves.set(index, 64); // surely not min anymore and we still have all the indexes
                        }
                    }

                    int x = Character.getNumericValue(bestCoordinates.charAt(0));
                    int y = Character.getNumericValue(bestCoordinates.charAt(1));
                    return new Move(x, y);
                }
            }
                if (validMoves.size() == 1) {
                    String guessedCoordinates = validMoves.get(0);
                    int x = Character.getNumericValue(guessedCoordinates.charAt(0));
                    int y = Character.getNumericValue(guessedCoordinates.charAt(1));
                    return new Move(x, y);
                }

            // server
        } else {
            field.updateSuggestion(orderUser);
            field.printField();
            field.printSuggestions();
            getValidMoves(field);
            if (validMoves.size() == 0) {
                field.checkWinOrDraw(field.field);
            }


            if (prevMove != null) {
                userMove = new Move(prevMove.y, prevMove.x);
                field.makeMove(userMove, orderUser);
                field.printField();
                field.updateAfterMove(userMove);
                field.counting();
                field.checkWinOrDraw(field.field);
            }


            field.updateSuggestion(orderBot);
            field.printField();
            field.printSuggestions();
            getValidMoves(field);

            // pass
            if (validMoves.isEmpty()) {
                return null;
            }
            // if only one suggestion possible
            if (validMoves.size() == 1) {
                String guessedCoordinates = validMoves.get(0);
                int x = Character.getNumericValue(guessedCoordinates.charAt(0));
                int y = Character.getNumericValue(guessedCoordinates.charAt(1));
                botMove = new Move(x, y);

                field.makeMove(botMove, orderBot);
                field.updateAfterMove(botMove);
                field.counting();
                field.checkWinOrDraw(field.field);
                field.printField();
                botMove = new Move(y, x);
                return botMove;
            }

            if (validMoves.size() > 1) {
                // take corners if possible
                if (validMoves.contains("00") || validMoves.contains("77") || validMoves.contains("70") || validMoves.contains("07")) {
                    Move botMove = new Move(-1,-1);
                    for (int i = 0; i < validMoves.size() ; i++) {
                        if(validMoves.get(i).equals("00")){
                            botMove = new Move(0,0); }
                        if(validMoves.get(i).equals("07")) {
                            botMove =  new Move(0, 7); }
                        if(validMoves.get(i).equals("70")) {
                            botMove =  new Move(7, 0); }
                        if(validMoves.get(i).equals("77")) {
                            botMove =  new Move(7, 7); }
                    }
                    field.makeMove(botMove, orderBot);
                    field.updateAfterMove(botMove);
                    botMove = new Move(botMove.y, botMove.x);
                    return botMove;

                } else {
                    // make copy of field to simulate moves in future
                    ArrayList<Integer> turnedDiscs = new ArrayList<>();
                    ArrayList<Integer> opponentValidMoves = new ArrayList<>();

                    for (int j = 0; j < validMoves.size(); j++) {
                        String koordinates = validMoves.get(j);
                        int x = Character.getNumericValue(koordinates.charAt(0));
                        int y = Character.getNumericValue(koordinates.charAt(1));
                        temporaryField = new Field();
                        temporaryField.field = new Square[field.field.length][];
                        for (int i = 0; i < field.field.length; i++) {
                            temporaryField.field[i] = Arrays.copyOf(field.field[i].clone(), field.field[i].length);
                        }
                        for (int i = 0; i < 8; i++) {
                            for (int k = 0; k < 8; k++) {
                                String coordinates = Integer.toString(i) + Integer.toString(j);
                                Square square = new Square(coordinates, field.field[i][k].getValue());
                                temporaryField.field[i][k] = square;
                            }
                        }
                        int before = countDiscs(temporaryField, orderBot);
                        botMove = new Move(x, y);
                        temporaryField.makeMove(botMove, orderBot);
                        temporaryField.updateAfterMove(botMove);
                        int after = countDiscs(temporaryField, orderBot);
                        int diff = after - before;
                        turnedDiscs.add(diff);

                        temporaryField.updateSuggestion(orderUser); // get opponents suggestions
                        opponentValidMoves.add(countDiscs(temporaryField, 4));
                    }

                    // if mulitple max elements, chose the one with less possible moves for oponent
                    int max = Collections.max(turnedDiscs);
                    String bestCoordinates = "";
                    int min;
                    boolean searching = true;
                    while (searching) {
                        min = Collections.min(opponentValidMoves);
                        int index = opponentValidMoves.indexOf(min);
                        if (turnedDiscs.get(index) == max) {
                            searching = false;
                            bestCoordinates = validMoves.get(index);
                        } else {
                            opponentValidMoves.set(index, 64); // surely not min anymore and we still have all the indexes
                        }
                    }

                    int x = Character.getNumericValue(bestCoordinates.charAt(0));
                    int y = Character.getNumericValue(bestCoordinates.charAt(1));

                    Move bestMove = new Move(x, y);
                    field.makeMove(bestMove, orderBot);
                    field.updateAfterMove(bestMove);
                    bestMove = new Move(y, x);

                    return bestMove;
                }
            }
        }
        return null;
    }

    // get all suggestions
    public void getValidMoves(Field field) {
        validMoves = new ArrayList<>();
        for (int i = 0; i < field.field.length; i++) {
            for (int j = 0; j < field.field.length; j++) {
                if (field.field[i][j].getValue() == Discs.SUGGESTION) {
                    validMoves.add(field.field[i][j].getCoordinates());
                }
            }
        }
    }

    public int countDiscs(Field field, int disc) {
        int sum = 0;
        for (int i = 0; i < field.field.length; i++) {
            for (int j = 0; j < field.field.length; j++) {
                if (field.field[i][j].getValue() == disc) {
                    sum++;
                }
            }
        }
        return sum;
    }
}

