import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import szte.mi.Move;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;


public class GUI_Main extends Application {

    User player1 = Field.player;
    User currentPlayer = player1;
    User player2;
    User oponentPlayer = player2;

    Label whosTurnLabel = new Label();
    Label whiteCounterLabel = new Label();
    Label blackCounterLabel = new Label();

    static Field field = new Field();
    Bot bot = new Bot();
    Move userMove;

    boolean singlePlayerMode = true;
    boolean passing = false;

    long initTime;
    long afterMoveTime;
    long beforeMoveTime;
    long beforeMoveTimeBot;
    int buttonClickCounter;
    ArrayList<Long>timeSaver = new ArrayList<>(); // will always save max 2 values {time prev click on button, time now clicked}

    Button timeButtonBlack;
    Button timeButtonWhite;
    Button passButton;
    Button botMoveButton;
    Button userMoveButton;
    ArrayList<Button> buttons = new ArrayList<>();

    Image blackDisc = new Image("Images/black-disc.png");
    Image whiteDisc = new Image("Images/white-disc.png");
    Image suggestionDisc = new Image("Images/suggestion-disc.png");


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        createFirstWindow(stage);
    }

    /** first window to choose single or multiplayer mode of game**/
    public void createFirstWindow(Stage primaryStage) {
        Stage stage = new Stage();
        VBox layout = new VBox(); // first window layout

        Label welcomeText = new Label("Welcome to Othello");
        welcomeText.setStyle("-fx-font: 30 arial;");

        // set of radiobuttons to chose single game ar multi
        HBox gameOptionBox = new HBox();
        gameOptionBox.setSpacing(20);
        gameOptionBox.setAlignment(Pos.CENTER);

        // choose player mode
        ToggleGroup multiplayerGroup = new ToggleGroup();
        RadioButton singlePl = new RadioButton("Play vs. Bot");
        singlePl.setSelected(true);
        singlePl.setToggleGroup(multiplayerGroup);
        RadioButton multiPl = new RadioButton("Play vs. Friend");
        multiPl.setToggleGroup(multiplayerGroup);
        gameOptionBox.getChildren().addAll(singlePl, multiPl);

        // confirmation button
        Button submitButton = new Button("Submit");
        submitButton.setDefaultButton(true);
        submitButton.setPrefSize(200, 100);
        submitButton.setOnAction(e -> {
            if (multiPl.isSelected())
                singlePlayerMode = false;
            player2 = new User();
            player2.init(1, (int) Math.pow(10,5), new Random());
            try {
                createTableInterface(primaryStage);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
            stage.hide();
        });

        VBox.setMargin(welcomeText, new Insets(10, 10, 10, 10));
        VBox.setMargin(submitButton, new Insets(10, 10, 10, 10));

        layout.setSpacing(10);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getChildren().addAll(welcomeText, gameOptionBox, submitButton);

        Scene scene = new Scene(layout, 400, 200);
        stage.setTitle("Othello");
        stage.setScene(scene);
        stage.show();
    }

    public void createTableInterface(Stage primaryStage) throws FileNotFoundException {
        // general layout
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: darkgray");

        // upper welcome labels
        VBox welcomeBox = new VBox();
        welcomeBox.setSpacing(10);
        welcomeBox.setAlignment(Pos.TOP_CENTER);
        Label welcomeText = new Label("Welcome to Othello");
        welcomeText.setStyle("-fx-font: 24 Copperplate;-fx-underline: true;");
        welcomeBox.getChildren().addAll(welcomeText);

        // black and white discs counter
        HBox counterBox = new HBox();
        counterBox.setAlignment(Pos.BOTTOM_CENTER);
        counterBox.setSpacing(50);
        counterBox.setPadding(new Insets(30, 10, 10, 10));
        blackCounterLabel.setText(Integer.toString(Field.counterBLACK));
        blackCounterLabel.setStyle("-fx-font: 24 arial;");
        whiteCounterLabel.setText(Integer.toString(Field.counterWHITE));
        whiteCounterLabel.setStyle("-fx-font: 24 arial;");
        counterBox.getChildren().addAll(blackCounterLabel, whiteCounterLabel);

        VBox turnBox = new VBox();
        turnBox.setSpacing(10);
        turnBox.setAlignment(Pos.CENTER);
        whosTurnLabel.setText(" ==> turn");
        whosTurnLabel.setStyle("-fx-font: 30 Copperplate;-fx-border-color: black; -fx-border-width: 2 2 2 2;");
        turnBox.getChildren().addAll(whosTurnLabel, counterBox);

        // remaining time buttons and labels
        VBox timeBox = new VBox();
        Label timeLabelBlack = new Label();
        timeLabelBlack.setText("Remaining time Black");
        timeBox.setAlignment(Pos.TOP_CENTER);
        timeButtonBlack = new Button();
        timeButtonBlack.setPrefSize(150,75);
        timeButtonBlack.setText("100 sec.");

        Label timeLabelWhite = new Label();
        timeLabelWhite.setText("Remaining time White");
        timeButtonWhite = new Button();
        timeButtonWhite.setPrefSize(150,75);
        timeButtonWhite.setText("100 sec.");

        timeBox.getChildren().addAll(timeLabelBlack, timeButtonBlack, timeLabelWhite, timeButtonWhite);

        // exit and pass buttons
        VBox exitBox = new VBox();
        exitBox.setAlignment(Pos.TOP_CENTER);
        exitBox.setSpacing(10);
        userMoveButton = new Button();
        if(singlePlayerMode){
            userMoveButton.setText("User last move: none");}
        else {
            userMoveButton.setText("User 0 last move: none");;
        }
        botMoveButton = new Button();
        if(singlePlayerMode){
        botMoveButton.setText("Bot last move: none");}
        else {
            botMoveButton.setText("User 1 last move: none");
        }



        Button exitButton = new Button();
        exitButton.setText("Exit");
        exitButton.setPrefSize(100, 50);
        exitButton.setOnAction(e -> {
            System.exit(0);
        });
        passButton = new Button();
        passButton.setText("Pass");
        passButton.setPrefSize(100, 50);
        passButton.setOnAction(e -> {
            passing = true;
        });
        exitBox.getChildren().addAll(exitButton,passButton, userMoveButton, botMoveButton);
        // init game and players
        field.initStartField();
        player1.init(0, (int) Math.pow(10,5), new Random());
        Bot.isGuiGame = true;
        bot.init(1, (int) Math.pow(10,5), new Random());
        updateTimeButton();
        // prevMove init -1, -1 as Move before the first move
        player1.setPrevMove(new Move(-1,-1));
        player2.setPrevMove(new Move(-1,-1));
        bot.setPrevMove(new Move(-1,-1));

        // grid layout fot buttons
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(2);
        grid.setHgap(2);
        // init grid with buttons
        for (int i = 0; i < field.field.length; i++) {
            for (int j = 0; j < field.field.length; j++) {
                Button button = new Button();
                button.setPrefSize(50, 50);
                button.setStyle("-fx-background-color: seagreen; -fx-border-color: black; -fx-border-width: 1 1 1 1;");
                String id = Integer.toString(j) + Integer.toString(i);  // id to identify buttons
                button.setId(id);
                updateTurnLabel();

                // actions on buttons
                button.setOnAction(e -> {
                    // no continue after win
                    if(Field.won || Field.draw){
                        System.out.println("End of Game");
                    }
                    else {
                        // extract coodinates form id
                        int x = Character.getNumericValue(button.getId().charAt(0));
                        int y = Character.getNumericValue(button.getId().charAt(1));
                        field.printSuggestions(); // init currentsuggestions

                        // if passing not allowed
                        if (passing && Field.currentSuggestions.size() != 0) {
                            passing = false;
                            System.out.println("Passing not allowed");
                        }

                        if (!passing && Field.currentSuggestions.contains(button.getId())) {
                            beforeMoveTime = System.currentTimeMillis();
                            timeSaver.add(beforeMoveTime);
                            long diff = timeSaver.get(1) - timeSaver.get(0);
                            currentPlayer.subtractTime(diff);
                            timeSaver.remove(0);
                            // if allowed to pass
                            if (passing || Field.currentSuggestions.size() == 0) {
                                System.out.println("In passing");
                                userMove = new Move(-1, -1);
                                passing = false;
                            } else {
                                userMove = new Move(x, y);
                                String moveOfUser = "User " + currentPlayer.getDisc() + " last move: " + userMove.x + "," + userMove.y;
                                userMoveButton.setText(moveOfUser);
                            }
                            //field.updateSuggestion(currentPlayer.getDisc());
                            field.makeMove(userMove, currentPlayer.getDisc());
                            currentPlayer.setPrevMove(userMove);
                            field.updateAfterMove(userMove);
                            syncWithData();
                            if (!singlePlayerMode) {
                                playerSwitch();
                                updateTurnLabel();
                            }

                            // bot moves
                            if (singlePlayerMode && Field.currentSuggestions.size() != 0) {
                                field.updateSuggestion(bot.getOrderBot());
                                userMove = bot.nextMove(new Move(0, 0), currentPlayer.getRemainingTime(), bot.getRemainingTime());
                                bot.setPrevMove(userMove);
                                String moveOfBot = "Bot last move: " + userMove.x + "," + userMove.y;
                                botMoveButton.setText(moveOfBot);
                                field.makeMove(userMove, bot.getOrderBot());
                                field.updateAfterMove(userMove);
                                syncWithData();
                                checkWinner();
                            }
                            field.counting();
                            updateCounterLabel();
                            // have to switch to previous user to track his time
                            playerSwitch();
                            updateTimeButton();
                            playerSwitch();
                            field.updateSuggestion(currentPlayer.getDisc());
                            syncWithData();
                            checkWinner();
                        }
                    }
                });
                grid.add(button, j, i);
                buttons.add(button);
                field.counting();
                updateCounterLabel();
                field.updateSuggestion(currentPlayer.getDisc());
                syncWithData();

            }
        }
        // init time after game starts (for first calculation)
        initTime = System.currentTimeMillis();
        timeSaver.add(initTime);

        // add all layout to main Borderpane
        layout.setBottom(grid);
        layout.setTop(welcomeBox);
        layout.setCenter(turnBox);
        layout.setLeft(exitBox);
        layout.setRight(timeBox);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setTitle("Othello");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /** check for end of game**/
    public void checkWinner() {
        field.counting();
        field.checkWinOrDraw(field.field);
        printDraw();
        printWin();
    }

    // visualize draw on gui
    public void printDraw() {
        if (Field.draw && !Field.won) {
            whosTurnLabel.setGraphic(null);
            whosTurnLabel.setText("DRAW");
            whosTurnLabel.setStyle("-fx-font: 30 arial; -fx-font-weight: bold;");
        }
    }
    // visualize win on gui
    public void printWin() {
        if (Field.won && !Field.draw) {
            whosTurnLabel.setText(" ==> WINNER");
            if (Field.counterBLACK > Field.counterWHITE) {
                whosTurnLabel.setGraphic(new ImageView(blackDisc));
            }
            if (Field.counterBLACK < Field.counterWHITE) {
                whosTurnLabel.setGraphic(new ImageView(whiteDisc));
            }
            whosTurnLabel.setStyle(
                    "-fx-font: 30 arial; -fx-font-weight: bold; -fx-text-fill: red ;-fx-border-width: 4; -fx-border-color: yellow;");
        }
    }

    // sync GUI grid with field of class Field
    public void syncWithData() {
        for (int i = 0; i < field.field.length; i++) {
            for (int j = 0; j < field.field.length; j++) {
                if (field.field[i][j].getValue() == Discs.WHITE) {
                    for (Button button : buttons) {
                        if (button.getId().equals(field.field[i][j].getCoordinates())) {
                            button.setGraphic(new ImageView(whiteDisc));
                        }
                    }
                }
                if (field.field[i][j].getValue() == Discs.BLACK) {
                    for (Button button : buttons) {
                        if (button.getId().equals(field.field[i][j].getCoordinates())) {
                            button.setGraphic(new ImageView(blackDisc));
                        }
                    }
                }
                if (field.field[i][j].getValue() == Discs.SUGGESTION) {
                    for (Button button : buttons) {
                        if (button.getId().equals(field.field[i][j].getCoordinates())) {
                            button.setGraphic(new ImageView(suggestionDisc));
                        }
                    }
                }
                if (field.field[i][j].getValue() == Discs.EMPTY) {
                    for (Button button : buttons) {
                        if (button.getId().equals(field.field[i][j].getCoordinates())) {
                            button.setGraphic(null);
                        }
                    }
                }
            }
        }
    }

    // for mulitplayer needed only
    public void playerSwitch() {
        if (currentPlayer.getDisc() == 0){
            currentPlayer = player2;
            oponentPlayer = player1;
        }
        else{
            currentPlayer = player1;
            oponentPlayer = player2;
        }
    }

    // update of values on buttons and labels
    public void updateTurnLabel() {
        if (currentPlayer.getDisc() == Discs.BLACK) {
            whosTurnLabel.setGraphic(new ImageView(blackDisc));
        }
        if (currentPlayer.getDisc() == Discs.WHITE) {
            whosTurnLabel.setGraphic(new ImageView(whiteDisc));
        }
    }
    // discs counter label updater
    public void updateCounterLabel() {
        blackCounterLabel.setGraphic(new ImageView(blackDisc));
        blackCounterLabel.setText(Integer.toString(Field.counterBLACK));

        whiteCounterLabel.setGraphic(new ImageView(whiteDisc));
        whiteCounterLabel.setText(Integer.toString(Field.counterWHITE));
    }
    // time counter label updater
    public void updateTimeButton(){
        // convert milis to sec with floating point
        double sec = 0;
        double sec2 = 0;

        sec = currentPlayer.getRemainingTime() / 1000.0;
        sec = Math.floor(sec * 1000) / 1000.0;
        if(currentPlayer.getDisc() == 0){
            timeButtonBlack.setText(String.valueOf(sec) + " sec.");
        }
        else{
            timeButtonWhite.setText(String.valueOf(sec) + " sec.");
        }
    }
}