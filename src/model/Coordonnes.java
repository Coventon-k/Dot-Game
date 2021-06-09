package model;

import java.util.Objects;

public class Coordonnes {
    private int x;
    private int y;

    public Coordonnes(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean closeTo(Coordonnes coordonnes){
        int x1 = coordonnes.getX();
        int y1 = coordonnes.getY();

        if(x1 == x && Math.abs(y1 - y) == 1){
            //si c'est sur la meme ligne (GAUCHE + DROITE)
            return true;

        }else if (y1 == y && Math.abs(x1 - x) == 1){
            //si c'est sur la meme colonne (EN HAUT EN BAS )
            return true;

        }else if(Math.abs(x1 - x) == 1 && Math.abs(y1 - y ) == 1){
            //si c'est sur la meme Diagonales
            return true;

        }else{
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordonnes that = (Coordonnes) o;
        return x == that.x && y == that.y;
    }
}
