package model;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage DotsGame;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/vue/MenuDot.fxml"));
        primaryStage.setTitle("Russian Dot Game");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void show(Parent parent,ActionEvent actionEvent){

        try {
            DotsGame = new Stage();
            DotsGame.setScene(new Scene(parent));
            DotsGame.show();
            System.out.println("Yooo");
        }catch (Exception ex){

            System.out.println(ex);
        }


    }

    public static void main(String[] args) {
        launch(args);
    }
}
