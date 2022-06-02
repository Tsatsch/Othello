import szte.mi.Move;

import java.util.Scanner;

public class Runner {
    public Field field = new Field();
    public Bot bot;

    public static void main(String[] args) {
        Runner game = new Runner();
        System.out.println("Hello and Welcome! \n");
        System.out.println("Notation for the Command Line version: ");
        System.out.println("1 --> BLACK DISC \n2 --> WHITE DISC \n0 --> Empty field \n4 --> VALID MOVES/SUGGESTION for current player");
        System.out.println("You can pass with \"pass\" and exit with \"exit\"");
        game.gameFLow();
    }


    public void gameFLow() {
        field.initStartField();

        while (!Field.won && !Field.draw) {
            Move userMove = new Move(-1,-1); // has to be initialized
            field.playerSwitchOne();
            // Black disk is player, white is bot
            if(Field.player.getDisc() == Discs.BLACK) {
                System.err.println("Player with " + Field.player.getDisc() + ", its your turn");
            }
            field.updateSuggestion(Field.player.getDisc());
            field.printField();
            field.printSuggestions();
            if(Field.player.getDisc() == Discs.BLACK){
             userMove = getUserInput();
            }
            if(Field.player.getDisc() == Discs.WHITE){
                userMove = bot.nextMove(new Move(-1,-1) ,10,10);    // invalid data!!!
                System.err.println("Bot makes move to: " + userMove.x + "" + userMove.y);
            }

            if (userMove.x == -1 && userMove.y == -1) {
                System.out.println("Player with " + Field.player.getDisc() + ", passing...");
            } else {
                field.makeMove(userMove, Field.player.getDisc());
                field.updateAfterMove(userMove);
                field.counting();
                field.checkWinOrDraw(field.field);
               ;

            }
        }

        if(Field.won){ System.out.println("Winner is: " + Field.winner);}
        else if(Field.draw){ System.out.println("Draw");
        }

    }

    /** analyse user input*/
    public Move getUserInput() {
        System.out.println("Enter coordinates in that form <x><>y> (like in suggestion above): ");
        Move move;
        int x = 0;
        int y = 0;
        boolean flag = true;

        do {
            try {
                Scanner scan = new Scanner(System.in);
                String input = scan.nextLine();

                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("QUITTING...");
                    System.exit(0);
                }

                if(input.equalsIgnoreCase("pass") ){
                    return new Move(-1,-1);
                }

                x = Character.getNumericValue(input.charAt(0));
                y = Character.getNumericValue(input.charAt(1));

                if (x < 0 || x > 7 || y < 0 || y > 7) {
                    System.err.println("Coordinates should be between 0 and 7!");
                    throw new NumberFormatException();
                }
                flag = false;

            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                flag = true;
                System.out.println("Please enter a coordinates in form <x><y> (x and y between 0 and 7) ");
            }
        } while (flag) ;

            return new Move(x, y);
    }
}
