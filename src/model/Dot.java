package model;

import java.util.Objects;

public class Dot {
    private boolean isActive;     // Il faut joue pour activer un point
    private boolean captured;    // Si un point est capturer il peut etre utilise
    private boolean isDead;     // Pour designer un point mort (Cature - Inactive)
    private int dotNumber;     // Pour determiner le joueur  (Valide)
    private Coordonnes coordonnes;


    public Dot(int x, int y) {
        this.isActive = false;
        this.captured = false;
        this.isDead = false;
        this.dotNumber = 0;
        this.coordonnes = new Coordonnes(x,y);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public int getDotNumber() {
        return dotNumber;
    }

    public void setDotNumber(int dotNumber) {
        this.dotNumber = dotNumber;
    }

    public Coordonnes getCoordonnes() {
        return coordonnes;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dot dot = (Dot) o;
        return Objects.equals(coordonnes, dot.coordonnes);
    }


}
