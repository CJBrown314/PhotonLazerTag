import DAO.PlayerDAO;
import Entities.EventList;
import Entities.PlayerColumn;
import Entities.PlayerTaggedEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javafx.geometry.*;
import javafx.scene.paint.*;
import javafx.scene.text.Text;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.control.Label;


public class Run extends Application{

    private TableView table = new TableView();
    private final ObservableList<PlayerColumn> data = FXCollections.observableArrayList();
    private final int TABLE_SIZE = 16; // total number of table fields
    private static final EventList events = new EventList(30);
    private int greenCount = 0;
    private int playerCount = 0;
    private VBox greenBox = new VBox();
    private VBox blueBox = new VBox();

    @Override
    public void start(Stage stage) {
        try {
            /*SPLASH SCENE*/
            //load in splash image
            BorderPane spRoot = new BorderPane();
            InputStream spInput = getClass().getClassLoader().getResourceAsStream("splash_screen.png");
            //FileInputStream spInput = new FileInputStream("splash_screen.png");
            if (spInput == null) {
                System.out.println("ERROR ERROR ERROR");
                System.exit(0);
            }
            Image spImage = new Image(spInput);
            //Image spImage = new Image((String.valueOf(new File("splash_screen.png"))));
            // set title for the stage
            Scene spScene = new Scene(spRoot, 1366, 768);
            // Creates the background size in such a way that the image fits the screen
            BackgroundSize spSize = new BackgroundSize(spImage.getWidth(), spImage.getHeight(), false, false, false, true);
            // Creates a background image from the fetched image
            BackgroundImage spBackgroundimage = new BackgroundImage(spImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, spSize);
            // create Background
            Background spBackground = new Background(spBackgroundimage);
            // set the background of the borderpane
            spRoot.setBackground(spBackground);

            /*PLAYER ENTRY SCENE */
            // set title for the stage
            stage.setTitle("Photon LazerTag");
            //Create root borderpane layout
            BorderPane root = new BorderPane();
            // create a vertical box layout to put in the center pane of the borderpane layout
            VBox centerPane = new VBox();
            // set the alignment of the center pane vbox layout to center all items
            centerPane.setAlignment(Pos.CENTER);
            // Add space between items in the vbox
            centerPane.setSpacing(10);


            // create a scene with the borderpane as the parent
            Scene scene = new Scene(root, 1366, 768);

            // create a label and add it to the center pane vbox
            Label label = new Label("Add Players to the Table: ");
            label.setTextFill(Color.BLACK);
            centerPane.getChildren().add(label);

            //create input stream for image
            InputStream input = getClass().getClassLoader().getResourceAsStream("header.png");
            if (input == null) {
                System.out.println("ERROR ERROR");
                System.exit(0);
            }

            // create the image
            Image image = new Image(input);

            // Creates the background size in such a way that the image fits the screen
            BackgroundSize size = new BackgroundSize(image.getWidth(), image.getHeight(), false, false, false, true);
            // Creates a background image from the fetched image
            BackgroundImage backgroundimage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size);

            // create Background
            Background background = new Background(backgroundimage);

            // set the background of the borderpane
            root.setBackground(background);

            TableColumn<PlayerColumn, String> greenteamplayers = new TableColumn("Green Team Players");
            greenteamplayers.setMinWidth(100);
            greenteamplayers.setCellValueFactory(
                    new PropertyValueFactory<PlayerColumn, String>("leftPlayerID"));
            greenteamplayers.setCellFactory(TextFieldTableCell.forTableColumn());
            greenteamplayers.setOnEditCommit(
                    new EventHandler<CellEditEvent<PlayerColumn, String>>() {
                        @Override
                        public void handle(CellEditEvent<PlayerColumn, String> t) {
                            ((PlayerColumn) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setLeftPlayerID(t.getNewValue());
                        }
                    }
            );

            TableColumn<PlayerColumn, String> blueteamplayers = new TableColumn("Blue Team Players");
            blueteamplayers.setMinWidth(100);
            blueteamplayers.setCellValueFactory(
                    new PropertyValueFactory<PlayerColumn, String>("rightPlayerID"));
            blueteamplayers.setCellFactory(TextFieldTableCell.forTableColumn());
            blueteamplayers.setOnEditCommit(
                    new EventHandler<CellEditEvent<PlayerColumn, String>>() {
                        @Override
                        public void handle(CellEditEvent<PlayerColumn, String> t) {
                            ((PlayerColumn) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setRightPlayerID(t.getNewValue());
                        }
                    }
            );

            scene.setOnKeyPressed(e -> {
                if(e.getCode() == KeyCode.ESCAPE)
                {
                    System.exit(0);
                }
            });

            spScene.setOnKeyPressed(e -> {
                if(e.getCode() == KeyCode.ESCAPE)
                {
                    System.exit(0);
                }
            });

            
            
            //adds button and makes button functional
            Button button = new Button("Start Game");
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    submitForm(stage, greenteamplayers, blueteamplayers);
                    //button dissappears
                    button.setVisible(false);
                    //when start game is pushed timer starts
                     /*Test countdown label*/
                    // Create Timer with a countdown starting at 100
                    Countdown counter = new Countdown(0, 10);
                    // Create a label and bind its text to the counters count property
                    Label count = new Label("");
                    count.setStyle("-fx-font-size: 70; -fx-text-fill: maroon; -fx-font-family: 'Comic Sans MS'");
                    count.textProperty().bind(counter.getCountProperty());
                    // Add the label to the center pane
                    centerPane.getChildren().add(count);
                    // Create counter thread and start the counter
                    Thread countThread = new Thread(counter);
                    countThread.start();

                    SplitPane actionPane = new SplitPane();

                    //Creating ArrayLists
                    ArrayList<Integer> playerScores = new ArrayList<Integer>();
                    ArrayList<String> playerNames = new ArrayList<String>();
                    ArrayList<Label> playerLines = new ArrayList<Label>();

                    //Int counters
                    playerCount = 0;
                    greenCount = 0;

                    //Load Array Lists
                    int n = 0;
                    while(greenteamplayers.getCellObservableValue(n).getValue() != "")
                    {
                        playerScores.add(0);
                        playerNames.add(PlayerDAO.getDAO().retrievePlayerName(Integer.parseInt((greenteamplayers.getCellObservableValue   (n).getValue()))));
                        playerLines.add(new Label(playerNames.get(n) + ".............................." + playerScores.get(n)));
                        n++;
                        playerCount++;
                        greenCount++;
                    }
                    System.out.println("PlayerNames ArrayList After green: " + playerNames.toString());
                    n = 0;
                    while(blueteamplayers.getCellObservableValue(n).getValue() != "")
                    {
                        playerScores.add(0);
                        playerNames.add(PlayerDAO.getDAO().retrievePlayerName(Integer.parseInt((blueteamplayers.getCellObservableValue   (n).getValue()))));
                        playerLines.add(new Label(playerNames.get(n+greenCount) + ".............................." + playerScores.get(n+greenCount-1)));
                        n++;
                        playerCount++;
                    }
                    System.out.println("PlayerNames ArrayList After blue: " + playerNames.toString());
            
                    greenBox = new VBox();
                    //greenBox.setPrefWidth(200);
                    greenBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#B2E6C3"), CornerRadii.EMPTY, Insets.EMPTY)));
                    Label GreenTeam = new Label("Green team score - 0");
                    GreenTeam.setStyle("-fx-font-size: 35;");
                    greenBox.getChildren().add(GreenTeam);
                    for(int i = 0; i < greenCount; i++)
                    {
                        greenBox.getChildren().addAll(playerLines.get(i));
                    }
                    blueBox = new VBox();
                    //blueBox.setPrefWidth(200);
                    blueBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#C0E0FF"), CornerRadii.EMPTY, Insets.EMPTY)));
                    Label BlueTeam = new Label("Blue team score - 0");
                    BlueTeam.setStyle("-fx-font-size: 35;");
                    blueBox.getChildren().add(BlueTeam);
                    for(int i = greenCount; i< playerCount ; i++)
                    {
                        blueBox.getChildren().addAll(playerLines.get(i));
                    }

                    VBox actionBox = new VBox();

                    /*Game Timer*/
                    // Create Timer with a countdown starting at 100
                    Countdown gameClock = new Countdown(2,00 );
                    // Create a label and bind its text to the counters count property
                    Label timer = new Label("");
                    timer.setStyle("-fx-font-size: 60; -fx-text-fill: black; -fx-font-family: 'Comic Sans MS'");
                    timer.textProperty().bind(gameClock.getCountProperty());
                    // Add the label to the center pane
                    actionBox.getChildren().add(timer);
                    // Create counter thread and start the counter
                    Thread timerThread = new Thread(gameClock);
                    timerThread.start();

                    Label action = new Label("Current Actions");
                    actionBox.getChildren().addAll(action);
                    actionBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#FED5D5"), CornerRadii.EMPTY, Insets.EMPTY)));
                    actionPane.getItems().addAll(greenBox,actionBox,blueBox);
                    actionPane.setDividerPositions(.3,.7,1);
                    Scene actionScene = new Scene(actionPane, 1366, 768);
                    actionScene.setOnKeyPressed(e -> {
                        if(e.getCode() == KeyCode.ESCAPE)
                        {
                            System.exit(0);
                        }
                    });
                    PauseTransition newPause = new PauseTransition(Duration.seconds(10));
                    AtomicReference<Thread> serverThread = new AtomicReference<>();
                    newPause.setOnFinished(e -> {
                        Server server = new Server(7501);
                        serverThread.set(new Thread(server));
                        serverThread.get().start();

                        events.addListener(new ListChangeListener<PlayerTaggedEvent>() {
                            @Override
                            public void onChanged(Change<? extends PlayerTaggedEvent> change) {
                                // This code runs everytime a new event is added to the events list.
                                ObservableList<PlayerTaggedEvent> list = events.getList();
                                actionBox.getChildren().remove(1, actionBox.getChildren().size());
                                for (PlayerTaggedEvent item : events.getList()) {
                                    actionBox.getChildren().add(1, new Label(item.toString()));
                                }
                                for(int i = 0; i< playerNames.size(); i++)
                                {
                                    if(playerNames.get(i).equals((list.get(list.size()-1)).getTransmitPlayer()))
                                    {
                                        playerScores.set(i, playerScores.get(i)+50);
                                        playerLines.set(i, new Label(playerNames.get(i) + "........................." + playerScores.get(i)));
                                        
                                        greenBox.getChildren().clear();
                                        int greenScore = 0;
                                        for(int j = 0; j < greenCount; j++)
                                        {
                                            greenScore += playerScores.get(j);
                                            greenBox.getChildren().addAll(playerLines.get(j));
                                        }
                                        Label greenLabel = new Label("Green team score - " + greenScore);
                                        greenLabel.setStyle("-fx-font-size: 35;");
                                        greenBox.getChildren().add(0, greenLabel);
                                        
                                        blueBox.getChildren().clear();
                                        int blueScore = 0;
                                        for(int j = greenCount; j< playerCount ; j++)
                                        {
                                            blueScore += playerScores.get(j);
                                            blueBox.getChildren().addAll(playerLines.get(j));
                                        }
                                        Label blueLabel = new Label("Blue team score - " + blueScore);
                                        blueLabel.setStyle("-fx-font-size: 35;");
                                        blueBox.getChildren().add(0, blueLabel);

                                        break;
                                    }
                                }
                            }
                        });

                        stage.setScene(actionScene);
                    });
                    newPause.play();
                    stage.show();
                    PauseTransition playTime = new PauseTransition(Duration.seconds(130));
                    playTime.play();
                    serverThread.get().interrupt();
                }
            });

            centerPane.getChildren().add(button);

            // create columns
            greenteamplayers.setPrefWidth(root.getWidth() / 4);

            blueteamplayers.setPrefWidth(root.getWidth() / 4);

            for (int i = 0; i < TABLE_SIZE; i++) {
                data.add(new PlayerColumn());
            }

            table.setItems(data);

            // set table properties
            table.getColumns().addAll(greenteamplayers, blueteamplayers);
            //table.getItems().add(new TextField());
            table.setEditable(true);
            table.setMaxHeight(root.getHeight() / 2);
            table.setMaxWidth(root.getWidth() / 2);
            centerPane.getChildren().add(table);


            root.setCenter(centerPane);

            //create Action Player Scene

            // set the scene 
            //splash scene first
            stage.setScene(spScene);
            stage.show();
            //take splash screen away
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(e -> stage.setScene(scene));
            pause.play();
            stage.show();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        
    }

    public void submitForm(Stage stage, TableColumn greenteamplayers, TableColumn blueteamplayers)
    {

        int i = 0;
        while(greenteamplayers.getCellObservableValue(i).getValue() != "")
        {
            boolean check = true;
            int pass = Integer.parseInt((String)greenteamplayers.getCellObservableValue(i).getValue());
            try{
                check = PlayerDAO.getDAO().playerExists(pass);
            } catch (Exception e) {
                e.printStackTrace();
            }

            i++;
            if(!check)
            {
                String newUser = popupBox(stage, pass);
                PlayerDAO.getDAO().savePlayer(pass, newUser);
            }
        }

        int k = 0;
        while(blueteamplayers.getCellObservableValue(k).getValue() != "")
        {
            boolean check = true;
            int pass = Integer.parseInt((String)blueteamplayers.getCellObservableValue(k).getValue());
            System.out.println("blue pass: " + pass);
            try{
                check = PlayerDAO.getDAO().playerExists(pass);
            } catch (Exception e) {
                e.printStackTrace();
            }
            k++;
            if(!check)
            {
                String newUser = popupBox(stage, pass);
                PlayerDAO.getDAO().savePlayer(pass, newUser);
            }
        }

    }

    public String popupBox(Stage stage, int id)
    {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        VBox dialogVbox = new VBox(20);
        Text user = new Text(id + " Enter Your User Name Below");
        TextField un = new TextField();
        Button userButton = new Button("Enter");
        userButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                System.out.println(un.getText());
                //sendBack = un.getText();
                dialog.close();
            }
        });
        dialogVbox.getChildren().addAll(user,un,userButton);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
        return un.getText();
    }

    public static void addEvent(PlayerTaggedEvent event) {
        //adds the event to the eventlist
        Platform.runLater(() -> events.add(event));
    }

    public static void main(String[] args) throws Exception {
        //Launch the Java application
        launch();
    }
}
