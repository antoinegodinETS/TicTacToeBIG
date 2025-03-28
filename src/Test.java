// Penser à placer les méthodes de BigBoard que vous voulez tester en public

class Test{
    public static void main(String[] args){
        
        int[] resultat = new int[2];
        int[] testResultat = new int[2];

        try {
            System.out.println("\nTest 1: isCenterControlled");
            testResultat = testIsCenterControlled();
            resultat[0] += testResultat[0];
            resultat[1] += testResultat[1];
        } catch (Exception e) {}

        try {
            System.out.println("\nTest 2: hasCornerAdvantage");
        testResultat = testHasCornerAdvantage();
        resultat[0] += testResultat[0];
        resultat[1] += testResultat[1];
        } catch (Exception e) {}


        System.out.println("\nRésultat des tests: " + resultat[1] + " succès sur " + resultat[0]);
        if (resultat[0] == resultat[1]){
            System.out.println(ANSI_GREEN + "Tous les tests ont passé" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Tous les tests n'ont pas passé" + ANSI_RESET);
        }
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static int[] testIsCenterControlled(){
        
        BigBoard bigBoard = new BigBoard();
        int[] resultat = new int[2];

        System.out.println("Si le centre ne contient qu'une marque");
        bigBoard = new BigBoard();
        bigBoard.play("E5", Mark.X);
        if (bigBoard.isCenterControlled(Mark.X) == 15){
            System.out.println(ANSI_GREEN + "L'évaluation de la marque vaut 15" + ANSI_RESET);
            resultat[1] += 1;
        } else {
            System.out.println(ANSI_RED + "L'évaluation de la marque ne vaut pas 15" + ANSI_RESET);
        }
        resultat[0] += 1;
        if (bigBoard.isCenterControlled(Mark.O) == -15){
            System.out.println(ANSI_GREEN + "L'évaluation de la marque adverse vaut -15" + ANSI_RESET);
            resultat[1] += 1;
        } else {
            System.out.println(ANSI_RED + "L'évaluation de la marque adverse ne vaut pas -15" + ANSI_RESET);
        }
        resultat[0] += 1;

        System.out.println("Si le centre contient 2 marques");
        bigBoard = new BigBoard();
        bigBoard.play("E5", Mark.X);
        bigBoard.play("D5", Mark.O);
        if (bigBoard.isCenterControlled(Mark.X) == 0){
            System.out.println(ANSI_GREEN + "L'évaluation de la marque vaut 0" + ANSI_RESET);
            resultat[1] += 1;
        } else {
            System.out.println(ANSI_RED + "L'évaluation de la marque ne vaut pas 0" + ANSI_RESET);
        }
        resultat[0] += 1;

        System.out.println("Si le centre est gagné par une marque");
        bigBoard = new BigBoard();
        bigBoard.play("E5", Mark.X);
        bigBoard.play("D5", Mark.X);
        bigBoard.play("F5", Mark.X);
        if (bigBoard.isCenterControlled(Mark.X) == 30){
            System.out.println(ANSI_GREEN + "L'évaluation de la marque vaut 100" + ANSI_RESET);
            resultat[1] += 1;
        } else {
            System.out.println(ANSI_RED + "L'évaluation de la marque ne vaut pas 100" + ANSI_RESET);
        }
        resultat[0] += 1;
        if (bigBoard.isCenterControlled(Mark.O) == -30){
            System.out.println(ANSI_GREEN + "L'évaluation de la marque adverse vaut -100" + ANSI_RESET);
            resultat[1] += 1;
        } else {
            System.out.println(ANSI_RED + "L'évaluation de la marque adverse ne vaut pas -100" + ANSI_RESET);
        }
        resultat[0] += 1;

        System.out.println("Si le centre est vide");
        bigBoard = new BigBoard();
        if (bigBoard.isCenterControlled(Mark.X) == 0){
            System.out.println(ANSI_GREEN + "L'évaluation d'une' marque vaut 0" + ANSI_RESET);
            resultat[1] += 1;
        } else {
            System.out.println(ANSI_RED + "L'évaluation d'une' marque ne vaut pas 0" + ANSI_RESET);
        }
        resultat[0] += 1;

        return resultat;
    }

    public static int[] testHasCornerAdvantage(){
        BigBoard bigBoard = new BigBoard();
        int[] resultat = new int[2];
        
        System.out.println("Si les coins sont contrôlés par une marque");
        bigBoard = new BigBoard();
        bigBoard.play("A1", Mark.X);
        if (bigBoard.hasCornerAdvantage(Mark.X) == 8){
            System.out.println(ANSI_GREEN + "L'évaluation de la marque vaut 8" + ANSI_RESET);
            resultat[1] += 1;
        } else {
            System.out.println(ANSI_RED + "L'évaluation de la marque ne vaut pas 8" + ANSI_RESET);
        }
        resultat[0] += 1;
        if (bigBoard.hasCornerAdvantage(Mark.O) == -8){
            System.out.println(ANSI_GREEN + "L'évaluation de la marque adverse vaut -8" + ANSI_RESET);
            resultat[1] += 1;
        } else {
            System.out.println(ANSI_RED + "L'évaluation de la marque adverse ne vaut pas -8" + ANSI_RESET);
        }
        resultat[0] += 1;

        System.out.println("Si les coins sont contrôlés par les deux marques");
        bigBoard = new BigBoard();
        bigBoard.play("A1", Mark.X);
        bigBoard.play("A9", Mark.O);
        if (bigBoard.hasCornerAdvantage(Mark.X) == 0){
            System.out.println(ANSI_GREEN + "L'évaluation de la marque vaut 0" + ANSI_RESET);
            resultat[1] += 1;
        } else {
            System.out.println(ANSI_RED + "L'évaluation de la marque ne vaut pas 0" + ANSI_RESET);
        }
        resultat[0] += 1;

        System.out.println("Si un coin est gagné par une marque");
        bigBoard = new BigBoard();
        bigBoard.play("A1", Mark.X);
        bigBoard.play("A2", Mark.X);
        bigBoard.play("A3", Mark.X);
        if (bigBoard.hasCornerAdvantage(Mark.X) == 16){
            System.out.println(ANSI_GREEN + "L'évaluation de la marque vaut 16" + ANSI_RESET);
            resultat[1] += 1;
        } else {
            System.out.println(ANSI_RED + "L'évaluation de la marque ne vaut pas 16" + ANSI_RESET);
        }
        resultat[0] += 1;
        if (bigBoard.hasCornerAdvantage(Mark.O) == -16){
            System.out.println(ANSI_GREEN + "L'évaluation de la marque adverse vaut -16" + ANSI_RESET);
            resultat[1] += 1;
        } else {
            System.out.println(ANSI_RED + "L'évaluation de la marque adverse ne vaut pas -16" + ANSI_RESET);
        }
        resultat[0] += 1;

        System.out.println("Si les coins sont vides");
        bigBoard = new BigBoard();
        if (bigBoard.hasCornerAdvantage(Mark.X) == 0){
            System.out.println(ANSI_GREEN + "L'évaluation d'une' marque vaut 0" + ANSI_RESET);
            resultat[1] += 1;
        } else {
            System.out.println(ANSI_RED + "L'évaluation d'une' marque ne vaut pas 0" + ANSI_RESET);
        }
        resultat[0] += 1;

        return resultat;
    }

}