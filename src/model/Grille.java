package model;

public class Grille<T>{
	
	protected int longeur;
	protected int largeur;
	protected T [][] tab;
	
	public Grille(int longeur, int largeur) {
		assert longeur >= 0 : "la longeur doit etre positif";
		assert largeur >= 0 : "La Largeur doit etre positif";

		this.longeur = longeur;
		this.largeur = largeur;
		tab = (T[][]) new Object[longeur][largeur];
	}
	
	public int getLongeur() {
		return longeur;
	}

	public int getLargeur() {
		return largeur;
	}
	
	public boolean coordCorrectes(int ligne, int colonne) {
		return ((ligne >= 1 && ligne <= longeur) && (colonne >= 1 && colonne <= largeur));
	}
	
	public T getCellule(int ligne, int colonne) {
		assert coordCorrectes(ligne,colonne) : "coordonnes incorrecte(s)";
		return tab[ligne - 1][colonne - 1];
	}

	public void setCellule(int ligne, int colonne,T t) {
		assert coordCorrectes(ligne,colonne) : "coordonnes incorrecte(s)";
		tab[ligne-1][colonne -1] = t;

	}
	
	public String toString() {
		String grille = "";
		for(T tab2[] : tab){
			for(T str : tab2) {
				grille += str + "|";
			}
			grille += "\n";
		}
		return grille;
	}
}