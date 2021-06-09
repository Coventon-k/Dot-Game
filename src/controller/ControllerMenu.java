package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerMenu {

    private static int ligne, colonne;
    private static String player1Name, player2Name;

    @FXML
    TextField field_PlayerOne, field_PlayerTwo, field_Row, field_column;

    @FXML
    private void start(ActionEvent e){

        if (textFieldEmpty()) {
            alertMessage("Champ(s) Vide", "Il faut remplir tout les champs");
        }else if(sameName()) {
            alertMessage("Champ(s) Eguales", "2 joueur ne peuvent pas avoir le meme nom");
            clearField(field_PlayerOne);
            clearField(field_PlayerTwo);
        }
        else if(!sizeMinimum()){
            alertMessage("Taille Mininum", "taille Minimum (5x5) maximum (32x32)");
            clearField(field_Row);
            clearField(field_column);
        }else{
            vueDots(e);
        }
    }

    public void vueDots(ActionEvent e){
        try {
            Parent vueDots   = FXMLLoader.load(getClass().getResource("/vue/ViewDots.fxml"));
            Scene  sceneMorpion = new Scene(vueDots);
            Stage  dotStage = (Stage) ((Node)e.getSource()).getScene().getWindow();

            dotStage.setScene(sceneMorpion);
            dotStage.show();
        }catch (Exception exception){

        }

    }

    private boolean sizeMinimum(){
        try {
            player1Name = field_PlayerOne.getText();
            player2Name = field_PlayerTwo.getText();
            ligne = Integer.parseInt(field_Row.getText());
            colonne = Integer.parseInt(field_column.getText());
        } catch (NumberFormatException nfe) {
            alertMessage("Erreur Typage", "Entrez un nombre pour les lignes et les colonnes");
        }
        return (ligne >= 5 && ligne <= 32) && (colonne >= 5 && colonne <= 32);
    }

    private void clearField(TextField textField){
        textField.clear();
    }

    private boolean textFieldEmpty(){
         return field_PlayerOne.getText().isEmpty() || field_PlayerTwo.getText().isEmpty() ||
                field_Row.getText().isEmpty() || field_column.getText().isEmpty();
    }

    private boolean sameName(){
        return (field_PlayerOne.getText().toLowerCase()).equals(field_PlayerTwo.getText().toLowerCase());
    }

    private void alertMessage(String errorTitle, String erroMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(errorTitle);
        alert.setHeaderText(erroMessage);
        alert.showAndWait();
    }


    public static int getLigne() {
        return ligne;
    }
    public static int getColonne() {
        return colonne;
    }

    public static String getPlayer1Name() {
        return player1Name;
    }
    public static String getPlayer2Name() {
        return player2Name;
    }
}
