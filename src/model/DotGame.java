package model;

public class DotGame {
    private final DotGameSimulation dotGameSimulation;
    private int playerNumber;

    public DotGame(int longeur, int largeur) {
        dotGameSimulation = new DotGameSimulation(longeur, largeur);
        playerNumber = 1;
    }

    public void play(int ligne, int colonne){
        //Determiner si le Joueur peut Jouer dans cette case
        if(dotGameSimulation.canPlay(ligne, colonne)) {
            int numeroJoueur = (playerNumber % 2 != 0) ? 1 : 2;
            dotGameSimulation.setDotNumber(ligne, colonne, numeroJoueur);
            playerNumber++;
        }
    }

    public DotGameSimulation getDotGameSimulation() {
        return dotGameSimulation;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
