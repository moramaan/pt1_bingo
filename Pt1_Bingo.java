/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pt1_bingo;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author moraman
 */
public class Pt1_Bingo {

    //Declaració i inicialització de constants globals\\
    public static final Scanner SCN = new Scanner(System.in);
    public static final Random RND = new Random();
    public static final int MAX_TIPUS_JOC = 90;//75 o 90; //Minim pot ser 27, que son els espais disponibles sense repetició d'un cartro.
    public static final int MIN_TIPUS_JOC = 1;
    public static final String BOMBO = "\u262B";
    public static final int MARCAT = -1;
    public static final String MARCAT_STRING = "\u2718";
    public static final int MAX_BOMBO = 4;
    public static final int RESET_ARRAY = 0;
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String GREEN = "\u001B[32m";
    public static final String CYAN = "\u001B[36m";
    public static final String NO_EXTRET = CYAN + "--" + RESET;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Declaració i inicialització de variables\\
        boolean jugar, bingo, linia, seguent;
        bingo = linia = false;
        jugar = seguent = true;
        //seguent = false;
        int contadorTirades, opcioJugador, ultimaExtraccio;
        contadorTirades = 0;
        int[] registreExtraccions = new int[MAX_TIPUS_JOC];

        System.out.println("Benvingut a la tarda de Bingo!");
        //Bucle principal del joc\\
        //***********************\\
        do {
            //Primera part de preparació dels elements necessaris per jugar\\
            System.out.print("Quants cartrons vols jugar? ");
            opcioJugador = Tools.validIntPositiu();
            System.out.println();

            Cartro[] cartrons = new Cartro[opcioJugador];
            crearCartrons(cartrons);
            for (int i = 0; i < cartrons.length; i++) {
                System.out.println(BLUE + "\nCartro " + (i + 1) + RESET);
                mostrarCartro(cartrons[i].cartro);
            }
            //Dinàmica d'extraccions\\
            //**********************\\
            while (!bingo && contadorTirades < MAX_TIPUS_JOC && seguent) {
                ultimaExtraccio = novaExtracció(registreExtraccions);
                contadorTirades++;
                //System.out.println("contadorTirades = " + contadorTirades);

                comprovarCartronsMarcats(cartrons, ultimaExtraccio);
                //seguent = continuar("Següent número (s/n)?: ");

                for (int i = 0; i < cartrons.length; i++) {
                    System.out.println(BLUE + "\nCartro " + (i + 1) + RESET);
                    mostrarCartro(cartrons[i].cartroMarcat);
                }
                if (!linia) {
                    linia = validarLinia(cartrons);
                    if (linia) {
                        seguent = continuar("Següent número (s/n)?: ");
                    }
                }
                if (!bingo) { //contadorTirades > 14 && 
                    bingo = validarBingo(cartrons);
                }
            }
            //Impressions finals\\
            mostrarExtraccions(registreExtraccions, cartrons);

            //***Fi de joc o no i resets necessaris***\\
            jugar = continuar("Vols tornar a jugar?: ");
            seguent = true;
            linia = false;
            bingo = false;
            Arrays.fill(registreExtraccions, 0, registreExtraccions.length, RESET_ARRAY);;
            contadorTirades = 0;
        } while (jugar);
    }

    // ******METODES****** \\
    //*********************\\
    /**
     * Mètode que genera un nombre aleatòri.
     *
     * @param registre array on s'emmagatzemen amb 0(no) i 1(si) l'aparició d'un
     * nombre.
     * @param upperBound Nombre superior del rang de nombres a generar.
     * @param lowerBound Nombre inferior del rang de nombres a generar.
     * @return nombreRandom-1.
     */
    public static int nombreNoRepetit(int[] registre, int upperBound, int lowerBound) {
        int nombreRandom;
        boolean validNum = false;

        do {
            nombreRandom = RND.nextInt((upperBound - lowerBound + 1)) + lowerBound;
            if (registre[nombreRandom - 1] == 0) {
                validNum = true;
                registre[nombreRandom - 1] = 1;
            }
        } while (!validNum);

        return nombreRandom - 1;
    }

    /**
     * Mètode per a crear tants objectes de tipus Cartro com index tingui
     * l'array de tipus Cartro que li passem.
     *
     * @param cartrons Array d'objectes de tipus Cartro.
     */
    public static void crearCartrons(Cartro[] cartrons) {
        for (int i = 0; i < cartrons.length; i++) {
            cartrons[i] = new Cartro();
            cartrons[i].cartro = new int[3][9];
            cartrons[i].cartroMarcat = new int[3][9];
            omplirCartrons(cartrons[i]);
        }
    }

    /**
     * Mètode que omple de de nombres sencers aleatoris, dintre del rang
     * [1-MAX_TIPUS_JOC], els dos arrays bidimensionals que conté com atributs
     * un objecte de tipus Cartro.
     *
     * @param cartro Objecte de tipus Cartro.
     */
    public static void omplirCartrons(Cartro cartro) {
        int nouNumero = 0;
        int[] numeros = new int[MAX_TIPUS_JOC];

        for (int i = 0; i < cartro.cartro.length; i++) {
            for (int j = 0; j < cartro.cartro[i].length; j++) {
                nouNumero = nombreNoRepetit(numeros, MAX_TIPUS_JOC, MIN_TIPUS_JOC);
                cartro.cartro[i][j] = nouNumero + 1; //Així mai pot ser 0.
                cartro.cartroMarcat[i][j] = nouNumero + 1;
            }
        }
        posarSimbol(cartro.cartro, cartro.cartroMarcat);
    }

    /**
     * Mètode que col·loca aleatòriament 4 elements bombo(0) als arrays
     * bidimensionals cartro i cartroCopia.
     *
     * @param cartro Array bidimensional de nombres sencers.
     * @param cartroCopia Una còpia igual a cartro.
     */
    public static void posarSimbol(int[][] cartro, int[][] cartroCopia) {
        int nouIndex, contador;
        nouIndex = contador = 0;

        for (int i = 0; i < cartro.length; i++) {
            int[] indexZeros = new int[cartro[i].length];
            do {
                nouIndex = nombreNoRepetit(indexZeros, cartro[i].length, 1);
                cartro[i][nouIndex] = 0;
                cartroCopia[i][nouIndex] = 0;
                contador++;
            } while (contador < 4);
            contador = 0;
        }
    }

    /**
     * Mètode que s'encarrega de mostrar per pantalla els nombres d'un array
     * cartro, ben formatat.
     *
     * @param cartro Array bidimensional que conté nombres sencers.
     */
    public static void mostrarCartro(int[][] cartro) {
        for (int i = 0; i < cartro.length; i++) {
            System.out.print("|");
            for (int j = 0; j < cartro[i].length; j++) {
                switch (cartro[i][j]) {
                    case 0:
                        System.out.printf(CYAN + "%2s" + RESET + "|", BOMBO);
                        break;
                    case -1:
                        System.out.printf(GREEN + "%2s" + RESET + "|", MARCAT_STRING);
                        break;
                    default:
                        System.out.printf("%2d|", cartro[i][j]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Mètode que genera un nombre aleatòri, l'emmagatzema en un registre i el
     * mostra per pantalla.
     *
     * @param registreExtraccions Array que emmagatzema les extraccions.
     * @return nombreExtret+1, atès que sense sumar res fa referència a l'index
     * que ocupa al registre.
     */
    public static int novaExtracció(int[] registreExtraccions) {
        int nouNumero;
        nouNumero = nombreNoRepetit(registreExtraccions, MAX_TIPUS_JOC, MIN_TIPUS_JOC);

        System.out.println("Nou número: " + RED + (nouNumero + 1) + RESET);

        return nouNumero + 1;
    }

    /**
     * Mètode que mostra per pantalla les extraccions realitzades durant la
     * partida. Mostra en color XXX les coincidencies amb el/s teu/s cartro/ns.
     *
     * @param registreExtraccions Array on s'emmagatzemen les extraccions.
     */
    public static void mostrarExtraccions(int[] registreExtraccions, Cartro[] cartrons) {
        int contadorPrintSalt, contadorPrintGeneral;
        contadorPrintGeneral = contadorPrintSalt = 0;
        boolean trobat;
        System.out.println(BLUE + "=========="
                + "Extraccions"
                + "==========" + RESET);
        System.out.print(BLUE + "|" + RESET);
        for (int i = 0; i < registreExtraccions.length; i++) {

            if (registreExtraccions[i] == 0) {
                System.out.printf("%2s" + BLUE + "|" + RESET, NO_EXTRET); //Numero no extret
            } else {
                trobat = cercarExtraccio(i + 1, cartrons); //falta hacer algo con esto
                if (trobat) {
                    System.out.printf(RED + "%2d" + BLUE + "|" + RESET, i + 1); //Numero extret
                } else {
                    System.out.printf("%2d" + BLUE + "|" + RESET, i + 1); //Numero extret
                }

            }
            contadorPrintSalt++;
            contadorPrintGeneral++;
            if (contadorPrintSalt == 10 && contadorPrintGeneral < registreExtraccions.length) {
                System.out.println();
                System.out.print(BLUE + "|" + RESET);
                contadorPrintSalt = 0;
            }
        }
        //System.out.println();
        System.out.println("\n" + BLUE + "===============================" + RESET + "\n");
        //System.out.println();
    }

    public static boolean cercarExtraccio(int nombre, Cartro[] cartrons) {
        boolean trobat = false;
        int i = 0;
        int[] resultatCerca = new int[2];
        while (!trobat && i < cartrons.length) {
            Tools.cercaNumBi(cartrons[i].cartro, nombre, resultatCerca);
            trobat = (resultatCerca[0] != -1 && resultatCerca[1] != -1);
            i++;
        }

        return trobat;
    }

    /**
     * Mètode que comprova si l'últim nombre extret es troba en algún
     * cartroMarcat, dels existents com a atribut dels objectes de tipus Cartro.
     * Si el troba, marca la posició que ocupa amb un '-1' i mostra un missatge
     * per pantalla indicant on.
     *
     * @param cartrons
     * @param ultimaExtraccio
     */
    public static void comprovarCartronsMarcats(Cartro[] cartrons, int ultimaExtraccio) {
        int[] resultatCerca = new int[2];
        for (int i = 0; i < cartrons.length; i++) {
            boolean trobat = false;
            Tools.cercaNumBi(cartrons[i].cartroMarcat, ultimaExtraccio, resultatCerca);
            trobat = (resultatCerca[0] != -1 && resultatCerca[1] != -1);

            if (trobat) {
                System.out.printf("Aquest número el tenies al Cartro-%d, el marco com -1\n", i + 1);
                cartrons[i].cartroMarcat[resultatCerca[0]][resultatCerca[1]] = -1;
            }
        }
    }

    /**
     * Mètode que revisa cartroMarcat de cada objecte existent a []cartrons i
     * comprova si pot cantar linia, en cas afirmatiu no revisa la resta
     * d'objectes i mostra un missatge cantant-la.
     *
     * @param cartrons Array que conté objectes de tipus Cartro.
     * @return linia (true o false) depenent si l'ha pogut cantar o no.
     */
    public static boolean validarLinia(Cartro[] cartrons) {
        boolean linia = false;
        int j, k, contador;
        int i = 0;
        while (!linia && i < cartrons.length) {
            j = k = contador = 0;
            while (!linia && j < cartrons[i].cartroMarcat.length) {
                while (!linia && k < cartrons[i].cartroMarcat[j].length) {
                    if (cartrons[i].cartroMarcat[j][k] == MARCAT) {
                        contador++;
                    }
                    k++;
                }
                if (contador == 5) {
                    linia = true;
                    System.out.printf(GREEN + "Linia!!" + RESET + " Cartro:%d row:%d\n", i + 1, j + 1);
                } else {
                    contador = 0;
                    k = 0;
                }
                j++;
            }
            if (!linia) { //creo que no cal el if
                i++;
            }
        }

        return linia;
    }

    /**
     * Mètode que revisa cartroMarcat de cada objecte existent a []cartrons i
     * comprova si pot cantar Bingo, en cas afirmatiu no revisa la resta
     * d'objectes i mostra un missatge cantant-lo.
     *
     * @param cartrons Array que conté objectes de tipus Cartro.
     * @return bingo (true o false) depenent si l'ha pogut cantar o no.
     */
    public static boolean validarBingo(Cartro[] cartrons) {
        boolean bingo;
        bingo = false;
        int i, j, contador, contadorLinies;
        i = contadorLinies = 0;
        while (contadorLinies < 3 && i < cartrons.length) {
            j = contador = 0;
            while (contadorLinies < 3 && j < cartrons[i].cartroMarcat.length) {
                for (int k = 0; k < cartrons[i].cartroMarcat[j].length; k++) {
                    if (cartrons[i].cartroMarcat[j][k] == MARCAT) {
                        contador++;
                    }
                }
                if (contador == 5) {
                    contadorLinies++;
                }
                contador = 0;
                j++;
            }
            if (contadorLinies == 3) {
                bingo = true;
                //contadorLinies = 0;
                System.out.printf(GREEN + "Bingo!" + RESET + "\n"
                        + "Has guanyat amb el Cartró: %d\n\n", i + 1);
            } else {
                contadorLinies = 0;
                i++;
            }
        }
        return bingo;
    }

    /**
     * Mètode per controlar si continuem fent una acció o no.
     *
     * @param missatge rep el missatge de pregunta adient.
     * @return true o false segons la resposta de l'usuari.
     */
    public static boolean continuar(String missatge) {
        String continuar;
        System.out.print(missatge);
        continuar = SCN.next();
        if (continuar.equalsIgnoreCase("S")) {
            return true;
        } else {
            return false;
        }
    }
}
