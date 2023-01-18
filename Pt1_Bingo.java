/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pt1_bingo;

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
    public static final int MAX_TIPUS_JOC = 75;
    public static final int MIN_TIPUS_JOC = 1;
    public static final String BOMBO = "@";
    public static final int MARCAT = -1;
    public static final String MARCAT_STRING = "X";
    public static final int MAX_BOMBO = 4;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Declaració i inicialització de variables\\
        boolean fiJoc, bingo, linia, seguent;
        fiJoc = bingo = linia = false;
        //seguent = true;
        seguent = false;
        int contadorTirades, opcioJugador;
        contadorTirades = 0;
        //opcioJugador = 1;

        int[] extraccions = new int[MAX_TIPUS_JOC];

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
                System.out.println("Cartro " + (i + 1));
                mostrarCartro(cartrons[i].cartro);
            }

            //Dinàmica d'extraccions\\
            while (!bingo && contadorTirades < MAX_TIPUS_JOC && seguent) {

                //extraccio > mostra extracció vàlida
                //marcar cartroMarcat si escau
                //seguent = continuarTirant("Següent número (s/n)?: ");
                //mostrar post extracció \\
                //mostrarCartroMarcat(cartrons, BOMBO, MARCAT);
            }

            fiJoc = repetirJoc("Vols tornar a jugar?: ");
            //seguent = true;
        } while (!fiJoc);
    }

    // ******METODES****** \\
    //*********************\\
    public static int nombreNoRepetit(int[] registre, int upperBound, int lowerBound) {
        int nombreRandom;
        boolean validNum = false;

        do {
            nombreRandom = RND.nextInt((upperBound - lowerBound + 1)) + lowerBound;
            if (nombreRandom != 0) {
                if (registre[nombreRandom - 1] == 0) {
                    validNum = true;
                    registre[nombreRandom - 1] = 1;
                }
            } else {
                if (registre[nombreRandom] == 0) {
                    validNum = true;
                    registre[nombreRandom] = 1;
                }
            }
        } while (!validNum);
        
        //Format de la sortida per evitar out of bounds als mètodes on s'utilitza aquesta dada\\
        if (nombreRandom == registre.length) {
            return nombreRandom - 1;
        } else {
            return nombreRandom;
        }

    }

    public static void crearCartrons(Cartro[] cartrons) {
        for (int i = 0; i < cartrons.length; i++) {
            cartrons[i] = new Cartro();
            cartrons[i].cartro = new int[3][9];
            cartrons[i].cartroMarcat = new int[3][9];
            omplirCartrons(cartrons[i]);
        }
    }

    public static void omplirCartrons(Cartro cartro) {
        int nouNumero = 0;
        int[] numeros = new int[MAX_TIPUS_JOC];

        for (int i = 0; i < cartro.cartro.length; i++) {
            for (int j = 0; j < cartro.cartro[i].length; j++) {
                nouNumero = nombreNoRepetit(numeros, MAX_TIPUS_JOC, MIN_TIPUS_JOC);
                cartro.cartro[i][j] = nouNumero;
                cartro.cartroMarcat[i][j] = nouNumero;
            }
        }
        posarSimbol(cartro.cartro, cartro.cartroMarcat);
    }

    public static void posarSimbol(int[][] cartro, int[][] cartroCopia) {
        int nouIndex, contador;
        nouIndex = contador = 0;

        for (int i = 0; i < cartro.length; i++) {
            int[] indexZeros = new int[cartro[i].length];
            do {
                nouIndex = nombreNoRepetit(indexZeros, cartro[i].length, 0);
                cartro[i][nouIndex] = 0;
                cartroCopia[i][nouIndex] = 0;
                contador++;
            } while (contador < 4);
            contador = 0;
        }
    }

    public static void mostrarCartro(int[][] cartro) {
        for (int i = 0; i < cartro.length; i++) {
            System.out.print("|");
            for (int j = 0; j < cartro[i].length; j++) {
                switch (cartro[i][j]) {
                    case 0:
                        System.out.printf("%2s|", BOMBO);
                        break;
                    case -1:
                        System.out.printf("%2s", MARCAT_STRING);
                        break;
                    default:
                        System.out.printf("%2d|", cartro[i][j]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    //OMPLIR AQUEST METODE I LA VALIDACIO BINGO I LINIA
    public static void novaExtracció() {
        
    }

    /**
     * Mètode per controlar si continuem fent una acció o no.
     * @param missatge rep el missatge de pregunta adient.
     * @return true o false segons la resposta de l'usuari.
     */
    public static boolean continuar(String missatge) {
        String continuar;
        System.out.println(missatge);
        continuar = SCN.next();
        if (continuar.equalsIgnoreCase("S")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean repetirJoc(String missatge) {
        String novaPartida;
        System.out.print(missatge);
        novaPartida = SCN.next();
        if (!novaPartida.equalsIgnoreCase("S")) {
            return true;
        } else {
            System.out.println("\nSeguim provant sort!");
            return false;
        }
    }
}
