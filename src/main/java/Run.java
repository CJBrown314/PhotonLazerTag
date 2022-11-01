import Entities.PlayerColumn;
import javafx.application.Application;
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
import javafx.geometry.*;
import javafx.scene.paint.*;
import javafx.scene.text.Text;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;


public class Run extends Application{

    private TableView table = new TableView();
    private final ObservableList<PlayerColumn> data = FXCollections.observableArrayList();
    private final int TABLE_SIZE = 16; // total number of table fields

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
            
            
                    VBox greenBox = new VBox(new Label("left box"));
                    //greenBox.setPrefWidth(200);
                    int i = 0;
                    greenBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#B2E6C3"), CornerRadii.EMPTY, Insets.EMPTY)));
                    while(greenteamplayers.getCellObservableValue(i).getValue() != "")
                    {
                        Label player = new Label((greenteamplayers.getCellObservableValue(i).getValue()) + "............. 0");
                        greenBox.getChildren().addAll(player);
                        i++;
                    }
                    VBox blueBox = new VBox(new Label("right box"));
                    //blueBox.setPrefWidth(200);
                    blueBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#C0E0FF"), CornerRadii.EMPTY, Insets.EMPTY)));
                    int j = 0;
                    while(blueteamplayers.getCellObservableValue(j).getValue() != "")
                    {
                        Label blueplayer = new Label((blueteamplayers.getCellObservableValue(j).getValue()) + "............. 0");
                        blueBox.getChildren().addAll(blueplayer);
                        j++;
                    }
                    VBox actionBox = new VBox(new Label("bottom Box"));
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
                    newPause.setOnFinished(e -> stage.setScene(actionScene));
                    newPause.play();
                    stage.show();
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


    public static void main(String[] args) throws Exception {
        //Launch the Java application
        launch();
    }
}
