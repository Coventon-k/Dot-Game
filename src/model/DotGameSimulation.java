package model;

import java.util.*;

public class DotGameSimulation extends  Grille<Dot>{

    private final Player player1;
    private final Player player2;

    private int coup = 1;
    //Ensemble de directions que peut prendre un point pour la recherche d'arcs
    private HashSet<Coordonnes> arcDirections;

    //Dans un cycle il y a les points morts (0) et les points jouer (1 || 2)
    private ArrayList<Dot> cycleTemporaire;
    private ArrayList<Dot> pointsCapture;

    //Detecteur de cycle
    private boolean cycle;
    private boolean endGame = false;


    public DotGameSimulation(int longeur, int largeur) {
        super(longeur, largeur);
        initializeDots();
        initializeArcDirections();

        player1 = new Player(1);
        player2 = new Player(2);
    }

    private void initializeDots() {
        for(int ligne = 1; ligne <= super.getLongeur(); ligne++) {
            for(int colonne = 1; colonne <= super.getLargeur(); colonne++) {
                setCellule(ligne,colonne, new Dot(ligne, colonne));
            }
        }
    }

    private void initializeArcDirections(){
        arcDirections = new HashSet<>();

        for(int i = -1; i <= 1; i++ ){
            for (int ii = -1; ii<= 1 ; ii++){
                if(!(i == 0 && ii == 0)){
                    arcDirections.add(new Coordonnes(i,ii));
                }
            }
        }
    }

    public boolean canPlay(int ligne, int colonne){
        return !getCellule(ligne, colonne).isActive() && !endGame;
    }

    public void setDotNumber(int ligne, int colonne, int numeroJoueur){

        Dot dot = getCellule(ligne, colonne);
        dot.setDotNumber(numeroJoueur);
        dot.setActive(true);
        updateParams();
        insertDot(dot);
        trouverCycle(dot);
    }

    private void updateParams(){
        coup++;
        pointsCapture = new ArrayList<>();
        cycleTemporaire = new ArrayList<>();
        cycle = false;
    }

    private void insertDot(Dot dot){
        Player player = getPlayer(dot.getDotNumber());
        ArrayList<Dot> initialList = new ArrayList<>();
        player.getMap().put(dot,initialList);
        createArc(dot);
    }

    private void createArc(Dot dot){
        Iterator<Coordonnes> iterator = arcDirections.iterator();

        int ligne   = dot.getCoordonnes().getX();
        int colonne = dot.getCoordonnes().getY();

        while (iterator.hasNext()){
            Coordonnes coordonnes = iterator.next();
            int x = coordonnes.getX();
            int y = coordonnes.getY();

            if(coordCorrectes(ligne + x , colonne + y)){
                Dot dot1 = getCellule(ligne + x, colonne + y);

                if(dot.getDotNumber() == dot1.getDotNumber() && !dot1.isCaptured()){
                    //ARC BIDIRECTIONEL
                    insertSetDots(dot,dot1);  //(X,Y)
                    insertSetDots(dot1,dot); //(Y,X)
                }
            }
        }
    }

    private void insertSetDots(Dot dot, Dot dot1){

        Player player = getPlayer(dot.getDotNumber());
        HashMap<Dot, ArrayList<Dot>> playerGraph = player.getMap();
        ArrayList<Dot> arrayList = playerGraph.get(dot);
        arrayList.add(dot1);
    }

    public Player getPlayer(int playerNumber){
        if(playerNumber == 1 ){
            return player1;
        }else {
            return player2;
        }
    }

    /*
          CYCLE
     */
    public void trouverCycle(Dot dot){
        Player player = getPlayer(dot.getDotNumber());
        HashMap<Dot, ArrayList<Dot>> playerGraph = player.getMap();
        ArrayList<Dot> dotList = playerGraph.get(dot);
        cycleTemporaire.add(dot);
        //s'il n'a pas d'arc on fait rien
        if(!dotList.isEmpty()){
            Iterator<Dot> iterator = dotList.iterator();
            //Tant qu'il y a des arc && que le cycle n'est pas valide
            while (iterator.hasNext()){
                Dot dotHashSet = iterator.next();
                //Si un point se retrouve dans la liste alors c'est un cycle
                if (cycleTemporaire.contains(dotHashSet)) {
                    //List Valide
                    if (cycleValide(cycleTemporaire, dotHashSet)) {
                        //capture
                        //Pour la capture chaque point essie de rencontrer le point qui est sur sa ligne et il capture ce qu'il trouve
                        if (cycleCapture(cycleTemporaire)) {
                            cycle = true;
                            deadDots(pointsCapture);
                        }
                    }
                } else {
                    trouverCycle(dotHashSet);
                    //On enleve le dot si on trouve pas de cycle
                    if(!cycle){
                        removeDot(cycleTemporaire, dotHashSet);
                    }
                }
            }
        }
    }

    //on evite Les formmes indesirables
    private boolean pointsCoteAcote(ArrayList<Dot> arrayList){
        //un triangle n'est pas un cycle valide
        //cette fontion est a ameliorer
        if(arrayList.size() < 4){
           return true;
        }
        //Point coordonne closeTo 3 de la
        for (Dot dot: arrayList){
            int carre = 0;
            for (Dot dot1 : arrayList) {
                if (!dot.equals(dot1)) {
                    Coordonnes coordonnesDot = dot.getCoordonnes();
                    Coordonnes coordonnesDot1 = dot1.getCoordonnes();

                    if (coordonnesDot.closeTo(coordonnesDot1)) {
                        carre++;
                        if(carre == 3){
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    //enviter des cycles indesirables
    private void removeDot(ArrayList<Dot> arrayList, Dot dot){

        if(arrayList.size() > 0 && !dot.equals(arrayList.get(0))){
            arrayList.remove(arrayList.size() - 1);
        }
    }

    private boolean cycleValide(ArrayList<Dot> arrayList, Dot dot){
        //  Pour eviter un cycle de la forme A-B-C-B
        Dot dot1 = arrayList.get(arrayList.size() - 2);
        //le point de depart doit etre le point d'arrive
        boolean boucle = arrayList.get(0).equals(dot);
        boolean formeIndesirable = pointsCoteAcote(arrayList);

        //il suffit qu'un des ces conditions snitialListoit vrai pour qu'elle soit invalide
        return  !(dot.getCoordonnes().equals(dot1.getCoordonnes())  || !boucle || formeIndesirable);
    }

    /*
          CAPTURE
     */
    private boolean cycleCapture(ArrayList<Dot> arrayList){
        boolean capture = false;

        for(int i = 0; i < arrayList.size(); i++){
            Dot dotDepart = arrayList.get(i);
            for(int ii = i+1; ii < arrayList.size(); ii++){
                Dot dotArrive = arrayList.get(ii);
                //Si les points sont sur la meme ligne
                int ligne1 = dotDepart.getCoordonnes().getX();
                int ligne2 = dotArrive.getCoordonnes().getX();

                if(ligne1 == ligne2){
                    Dot debut = (dotDepart.getCoordonnes().getY() < dotArrive.getCoordonnes().getY()) ?
                            dotDepart : dotArrive;
                    Dot arrive = (dotDepart.getCoordonnes().getY() > dotArrive.getCoordonnes().getY()) ?
                            dotDepart : dotArrive;

                    if(capture(debut, arrive)){
                        capture = true;
                    }
                }
            }
        }
        return capture;
    }

    private boolean capture(Dot dotDepart, Dot dotArrive){
        boolean capture = false;

        int numeroCycle = dotDepart.getDotNumber();
        Player player = getPlayer(numeroCycle);

        int ligne = dotDepart.getCoordonnes().getX();
        int colonneDepart = dotDepart.getCoordonnes().getY() + 1;
        int colonneArrive = dotArrive.getCoordonnes().getY() ;

        while (colonneDepart  < colonneArrive){

            Dot dotCapturer = getCellule(ligne,colonneDepart);
            int numeroDotCapturer = dotCapturer.getDotNumber();

            if(!cycleTemporaire.contains(dotCapturer) && dotIncludeInCycle(dotCapturer)) {

                if (numeroDotCapturer == 0) {
                    //on les evite
                } else if (numeroDotCapturer != numeroCycle && !dotCapturer.isCaptured()) {

                    dotCapturer.setCaptured(true);
                    player.getPointsCapture().add(dotCapturer);

                    //Enlevons le point capturer dans l'ensemble
                    Player playerCapturer = getPlayer(numeroDotCapturer);
                    HashMap<Dot, ArrayList<Dot>> map = playerCapturer.getMap();
                    removeDotCaptured(map, dotCapturer);

                    capture = true;
                } else if (numeroDotCapturer != numeroCycle && dotCapturer.isCaptured()) {
                    player.getPointsCapture().add(dotCapturer);
                } else if (numeroCycle == numeroDotCapturer) {
                    //on dois enlever les points retrouve
                    int numroAdverse = (numeroCycle != 1) ? 1 : 2;
                    Player playerAdverse = getPlayer(numroAdverse);
                    playerAdverse.getPointsCapture().remove(dotCapturer);
                    dotCapturer.setCaptured(true);
                }
                pointsCapture.add(dotCapturer);
            }
            colonneDepart++;
        }
        return capture;
    }

    private void deadDots(ArrayList<Dot> pointsCapture){
        for (Dot dot1: pointsCapture){
            if(dot1.getDotNumber() == 0){
                dot1.setDead(true);
                dot1.setCaptured(true);
                dot1.setActive(true);
            }
        }
    }
    //Pour enlever les points capturer su tableau
    private void removeDotCaptured(HashMap<Dot, ArrayList<Dot>> map, Dot dot){
        ArrayList<Dot> hashSet = map.get(dot);

        for (Dot dot1: hashSet){
            ArrayList<Dot> hashSet1 = map.get(dot1);
            hashSet1.remove(dot);
        }

        map.remove(dot);
    }
    //un Dot est inclus dans un cycle s'il est limite par un element du cycle pour les 4 direction
    private boolean dotIncludeInCycle(Dot dot){
        boolean left = lefttDirection(dot);
        boolean right = rightDirection(dot);
        boolean up = upDirection(dot);
        boolean down = downDirection(dot);

        return (left && right && up && down);
    }

    private boolean rightDirection(Dot dot){
        int ligne = dot.getCoordonnes().getX();
        int colonne = dot.getCoordonnes().getY();

        int limite = largeur;
        boolean include = false;

        while (colonne < limite && !include){

            colonne++;
            Dot dot1 = getCellule(ligne, colonne);

            if(cycleTemporaire.contains(dot1)){
                include = true;
            }
        }
        return include;
    }

    private boolean lefttDirection(Dot dot){
        int ligne = dot.getCoordonnes().getX();
        int colonne = dot.getCoordonnes().getY();

        int limite = 1;
        boolean include = false;

        while (colonne > limite && !include){

            colonne--;
            Dot dot1 = getCellule(ligne, colonne);

            if(cycleTemporaire.contains(dot1)){
                include = true;
            }
        }
        return include;
    }

    private boolean downDirection(Dot dot){
        int ligne = dot.getCoordonnes().getX();
        int colonne = dot.getCoordonnes().getY();

        int limite = longeur;
        boolean include = false;

       while (ligne < limite && !include){

            ligne++;
            Dot dot1 = getCellule(ligne, colonne);

            if(cycleTemporaire.contains(dot1)){
                include = true;
            }
        }
        return include;
    }

    private boolean upDirection(Dot dot){
        int ligne = dot.getCoordonnes().getX();
        int colonne = dot.getCoordonnes().getY();

        int limite = 1;
        boolean include = false;

        while (ligne > limite && !include){

            ligne--;
            Dot dot1 = getCellule(ligne, colonne);

            if(cycleTemporaire.contains(dot1)){
                include = true;
            }
        }
        return include;
    }

    public boolean isCycle() {
        return cycle;
    }

    public ArrayList<Dot> getPointsCapture() {
        return pointsCapture;
    }

    public ArrayList<Dot> getCycleTemporaire() {
        return cycleTemporaire;
    }

    public boolean isEndGame() {
        double taille = longeur * largeur * 0.85;
        endGame =  taille < (double) coup;
        return endGame;
    }

    public int getCoup() {
        return coup;
    }
}
