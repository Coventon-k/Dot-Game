package controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.Dot;
import model.DotGame;
import model.DotGameSimulation;
import model.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private int ligne = ControllerMenu.getLigne();
    private int colonne = ControllerMenu.getColonne();
    private DotGame dotGame = new DotGame(ligne,colonne);
    private DotGameSimulation dotGameSimulation = dotGame.getDotGameSimulation();



    @FXML AnchorPane Grille;
    @FXML Label label_capturePlayerOne, label_capturePlayerTwo, player_Id, playerOneName,playerTwoName;
    @FXML ProgressBar progressbar;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialization();
    }

    private void initialization(){
        playerOneName.setText(ControllerMenu.getPlayer1Name());
        playerTwoName.setText(ControllerMenu.getPlayer2Name());

        double valueX = 20.0, valueY = 15.0,radius = 10.0;
        int largeur = dotGameSimulation.getLargeur() + 1;
        int longeur = dotGameSimulation.getLongeur() + 1 ;

        for (int ligne = 1; ligne <  longeur; ligne++){
            for (int colonne = 1; colonne < largeur; colonne++){

                //OPTIMISATION
                Circle circle = new Circle(valueX, valueY, radius, Color.WHITE);
                circle.setStroke(Color.BLACK);
                circle.setId(ligne + "-" + colonne);

                Grille.getChildren().add(circle);

                if ((colonne + 1) % largeur == 0) {
                    valueX  = 20;
                    valueY += 40;
                } else {
                    valueX += 40;
                }
            }
        }
    }

    @FXML private void onMouseClicked(MouseEvent mouseEvent) {
        boolean endGame = dotGameSimulation.isEndGame();
        if(!endGame){
            play(mouseEvent);
        }
    }

    @FXML private void nouvelle_partie(ActionEvent e){
        try {
            Parent vueDots   = FXMLLoader.load(getClass().getResource("/vue/MenuDot.fxml"));
            Scene  sceneMorpion = new Scene(vueDots);
            Stage  dotStage = new Stage();

            dotStage.setScene(sceneMorpion);
            dotStage.show();
        }catch (Exception exception){

        }

    }

    @FXML private void reset_partie(){
        System.out.println("Reset");
    }

    @FXML private void quitter(){
        System.exit(-1);
    }

    private void play(MouseEvent mouseEvent) {
        try {
            Circle circle = (Circle) mouseEvent.getTarget();

            String str = circle.getId();
            String[] parts = str.split("-");

            int ligne   = Integer.parseInt(parts[0]);
            int colonne = Integer.parseInt(parts[1]);

            dotGame.play(ligne,colonne);
            colorTheCircle(circle);
            capture();
            updateComponents();
        } catch (Exception ignored) {

        }
    }

    private void colorTheCircle(Circle circle){
        if(circle.getFill() == Color.WHITE){
            int playerNumber = dotGame.getPlayerNumber();
            if (playerNumber % 2 == 0) {
                circle.setFill(Color.BLUE);
            } else {
                circle.setFill(Color.RED);
            }
        }
    }

    private void capture(){
        //s'il y a un cycle
        boolean cycle = dotGameSimulation.isCycle();

        if (cycle){
            colorTheCapture();
            circuit();


        }

        if(dotGameSimulation.isEndGame()){
            showWinner();
        }
    }

    private void colorTheCapture(){
        for (Dot dot: dotGameSimulation.getPointsCapture()){
            int ligne = dot.getCoordonnes().getX();
            int colonne = dot.getCoordonnes().getY();

            String strId = "#" + ligne + "-" + colonne;

            Parent parent = Grille.getParent();
            Circle circle = (Circle) parent.lookup(strId);

            Paint paint;

            if(dot.isDead()){
                paint = Color.GRAY;
            }else if (dotGame.getPlayerNumber() % 2 == 0){
                paint = Color.rgb(130,80,215);
            }else {
                paint = Color.rgb(245,154,154);
            }
            circle.setFill(paint);
        }
    }

    private void circuit(){

        ArrayList<Dot> cycle = dotGameSimulation.getCycleTemporaire();

        for(int i = 0; i < cycle.size() - 1; i++) {
            Dot dot1 = cycle.get(i);
            for (int ii = 1; ii < cycle.size(); ii++) {
                Dot dot2 = cycle.get(ii);
                if (dot1.getCoordonnes().closeTo(dot2.getCoordonnes())) {

                    double y1 = (dot1.getCoordonnes().getX() - 1) * 40 + 15;
                    double x1 = (dot1.getCoordonnes().getY() - 1) * 40 + 20;

                    double y2 = (dot2.getCoordonnes().getX() - 1) * 40 + 15;
                    double x2 = (dot2.getCoordonnes().getY() - 1) * 40 + 20;


                    Circle circleStart = new Circle(x1 , y1, 10, Color.WHITE);
                    Circle circleEnd   = new Circle(x2 , y2, 10, Color.WHITE);

                    addline(circleStart,circleEnd);
                }
            }
        }
    }

    private void addline(Circle circleStart, Circle circleEnd){
        Line line = new Line();
        line.setStrokeWidth(2);

      if(dotGame.getPlayerNumber() % 2 != 1){
            line.setStroke(Color.BLUE);
      }else {
           line.setStroke(Color.RED);
      }

        line.setStartX(circleStart.getCenterX());
        line.setStartY(circleStart.getCenterY());

        line.setEndX(circleEnd.getCenterX());
        line.setEndY(circleEnd.getCenterY());

        Grille.getChildren().add(line);
    }

    private void updateComponents(){
        int capture1 = dotGameSimulation.getPlayer(1).getNombreDotCapture();
        int capture2 = dotGameSimulation.getPlayer(2).getNombreDotCapture();

        setDotProgressBarValue(capture1, capture2);
        updateLabels(capture1, capture2);
    }

    private void setDotProgressBarValue(int capture1, int capture2){

        double value = 0;
        double total = ligne * colonne;
        double notEmpty = total - dotGameSimulation.getCoup();

        value = 1.0 - (notEmpty / total);

        if(dotGameSimulation.isEndGame()){
            value = 1.0;
        }

        String style = "Gray";

        if(capture1 != capture2){
            style = (capture1 > capture2) ? "Blue" : "Red";
        }

        progressbar.setProgress(value);
        progressbar.setStyle("-fx-accent:" + style);
    }

    private void updateLabels(int capture1, int capture2){
        String turn = (dotGame.getPlayerNumber() % 2 != 0) ?  playerOneName.getText() : playerTwoName.getText();
        player_Id.setText(turn);
        label_capturePlayerOne.setText("" + capture1);
        label_capturePlayerTwo.setText("" + capture2);
    }


    private void showWinner(){
        try {

        }catch (Exception exception){

        }
    }
}