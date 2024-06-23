package ch.albertmiel.rhlauncher.ui;

import ch.albertmiel.rhlauncher.Logger;
import ch.albertmiel.rhlauncher.Login;
import ch.albertmiel.rhlauncher.MSLogin;
import ch.albertmiel.rhlauncher.MinecraftLauncher;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainScreen extends Application{

    StackPane root = new StackPane();
    HBox hbox = new HBox();
    
    public Button createButtonForPrimaryStage(String value, String className) {
        Button btn = new Button();
        btn.setText(value);
        btn.getStyleClass().add(className);
        return btn;

    }
    
    public Button createButtonForLoginStage(String value, int columnIndex, int rowIndex, GridPane grid, String className) {
        Button btn = new Button();
        btn.setText(value);
        btn.getStyleClass().add(className);
        grid.add(btn, columnIndex, rowIndex);
        return btn;
    }
    
    public Label createLabelForLoginStage(String value, int columnIndex, int rowIndex, GridPane grid, String className) {
        Label label = new Label();
        label.setText(value);
        label.getStyleClass().add(className);
        grid.add(label, columnIndex, rowIndex);
        return label;
    }

    public TextField createTextfieldForLoginStage(String value, int columnIndex, int rowIndex, GridPane grid, String className) {
        TextField txtField = new TextField();
        txtField.getStyleClass().add(className);
        grid.add(txtField, columnIndex, rowIndex);
        return txtField;
    }


    @Override
    public void start(Stage primaryStage) {

        //ÉLÉMENTS GRAPHIQUES
        /*//Ancienne vidéo
        MediaPlayer backgroundVideoMediaPlayer = new MediaPlayer(new Media(getClass().getResource("/images/BG.mp4").toExternalForm()));
        MediaView backgroundVideo = new MediaView(backgroundVideoMediaPlayer);
        backgroundVideo.getStyleClass().add("backgroundVideo");
        //backgroundVideo.fitWidthProperty().bind(root.widthProperty());
        //backgroundVideo.fitHeightProperty().bind(root.heightProperty());
        backgroundVideo.setPreserveRatio(true);
        backgroundVideo.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            double scaleFactor = Math.max(
                    newBounds.getWidth() / backgroundVideoMediaPlayer.getMedia().getWidth(),
                    newBounds.getHeight() / backgroundVideoMediaPlayer.getMedia().getHeight());
                    backgroundVideo.setFitWidth(scaleFactor * backgroundVideoMediaPlayer.getMedia().getWidth());
                    backgroundVideo.setFitHeight(scaleFactor * backgroundVideoMediaPlayer.getMedia().getHeight());
        });
        root.getChildren().add(backgroundVideo);
        StackPane.setAlignment(backgroundVideo, javafx.geometry.Pos.CENTER);
        
        backgroundVideoMediaPlayer.play();

        /*primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            double height =  primaryStage.getHeight();
            double width = newVal.doubleValue();
            if(height < width) {
                backgroundVideo.setFitWidth(width);
                System.out.println("WIDTH - WIDTH");
            }
            else {
                backgroundVideo.setFitHeight(height);
                System.out.println("WIDTH - HEIGHT");
            }
        });

        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            double height =  newVal.doubleValue();
            double width = primaryStage.getWidth();
            if(height < width) {
                backgroundVideo.setFitWidth(width);
                System.out.println("HEIGHT - WIDTH");
            }
            else {
                backgroundVideo.setFitHeight(height);
                System.out.println("HEIGHT - HEIGHT");
            }
        });*/


        //BOUTONS
        hbox.getStyleClass().add("menubar");
        hbox.prefWidthProperty().bind(primaryStage.widthProperty());
        hbox.prefHeightProperty().bind(primaryStage.heightProperty().divide(12));
        root.getChildren().add(hbox);
        root.getStyleClass().add("mainRoot");
        

        Button msLoginButton = createButtonForPrimaryStage("MS Login", "msLoginButton");
        msLoginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MSLogin msLogin = new MSLogin();
                msLogin.start();
            }
        });
        this.hbox.getChildren().add(msLoginButton);

        Button crackedLoginButton = createButtonForPrimaryStage("Login", "loginButton");
        crackedLoginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loginScreen();
            }
        });
        this.hbox.getChildren().add(crackedLoginButton);

        Button LogoutButton = createButtonForPrimaryStage("Logout", "logoutButton");
        LogoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Login.logout();
            }
        });
        this.hbox.getChildren().add(LogoutButton);


        Button LaunchRhodiumButton = createButtonForPrimaryStage("Launch Rhodium!", "launchRhButton");
        LaunchRhodiumButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.close();
                MinecraftLauncher.launch("1.19.2", "forge", "43.3.0");
            }
        });
        this.root.getChildren().add(LaunchRhodiumButton);


        //SCENE & STAGE
        Scene scene = new Scene(this.root, 1000, 600);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(600);
        primaryStage.setTitle("Rh Launcher!");
        primaryStage.setScene(scene);
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/css/MainScreen.css").toExternalForm());
        primaryStage.getIcons().add(new Image("images/logo2-v1.png"));
        primaryStage.show();
        
        

        
    }

    public void loginScreen() {
        Stage loginStage = new Stage();
        GridPane grid = new GridPane();
        Scene scene = new Scene(grid, 300, 275);

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label titleLabel = new Label("Login");
        titleLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(titleLabel, 0, 0, 2, 1);
        /*Label pseudoLabel =*/ createLabelForLoginStage("Your pseudo", 0, 1, grid, "pseudoLabel");
        /*Label uuidLabel =*/ createLabelForLoginStage("Your UUID (optionnal)", 0, 2, grid, "uuidLabel");
        TextField pseudoTxt = createTextfieldForLoginStage("Your pseudo", 1, 1, grid, "pseudoTxt");
        TextField uuidTxt = createTextfieldForLoginStage("Your UUID", 1, 2, grid, "uuidTxt");

        pseudoTxt.getText();

        Button loginButton = createButtonForLoginStage("Login", 1, 3, grid, "loginButton");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!pseudoTxt.getText().equals("")) {
                    if(uuidTxt.getText().equals("")) {
                        Login.CrackLogin(pseudoTxt.getText());
                    }
                    else {Login.CrackLogin(pseudoTxt.getText(), uuidTxt.getText());}
                    loginStage.close();
                }
            }
        });

        loginStage.setWidth(300);
        loginStage.setHeight(300);
        loginStage.setTitle("Login");
        loginStage.setScene(scene);
        loginStage.getIcons().add(new Image("images/logo2-v1.png"));
        loginStage.show();

    }

}
