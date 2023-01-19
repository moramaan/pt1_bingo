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
    public static final String BOMBO = "@";
    public static final int MARCAT = -1;
    public static final String MARCAT_STRING = "X";
    public static final int MAX_BOMBO = 4;
    public static final int RESET_ARRAY = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Declaració i inicialització de variables\\
        boolean fiJoc, bingo, linia, seguent;
        fiJoc = bingo = linia = false;
        seguent = true;
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
                System.out.println("Cartro " + (i + 1));
                mostrarCartro(cartrons[i].cartro);
            }

            //Dinàmica d'extraccions\\
            while (!bingo && contadorTirades < MAX_TIPUS_JOC && seguent) {
                ultimaExtraccio = novaExtracció(registreExtraccions);
                contadorTirades++;
                System.out.println("contadorTirades = " + contadorTirades);
                //Comprovar i marcar cartroMarcat si escau
                comprovarCartrons(cartrons, ultimaExtraccio);
                //seguent = continuar("Següent número (s/n)?: ");
                //mostrar cartroMarcat post extracció \\
                for (int i = 0; i < cartrons.length; i++) {
                    System.out.println("\nCartro " + (i + 1));
                    mostrarCartro(cartrons[i].cartroMarcat);
                }

                //Validar linia i bingo
                if (!linia) {
                    //metode
                    linia = validarLinia(cartrons);
                    if (linia) {
                        System.out.println("Linia!!!");
                        seguent = continuar("Següent número (s/n)?: ");
                    }
                }
            }
            //mostrar extraccions
            mostrarExtraccions(registreExtraccions);
            
            //Fi de joc o no i resets necessaris\\
            fiJoc = repetirJoc("Vols tornar a jugar?: ");
            seguent = true;
            linia = false;
            Arrays.fill(registreExtraccions, 0, registreExtraccions.length - 1, RESET_ARRAY);;
            contadorTirades = 0;
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
                        System.out.printf("%2s|", MARCAT_STRING);
                        break;
                    default:
                        System.out.printf("%2d|", cartro[i][j]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static int novaExtracció(int[] registreExtraccions) {
        int nouNumero;
        nouNumero = nombreNoRepetit(registreExtraccions, MAX_TIPUS_JOC, MIN_TIPUS_JOC);

        System.out.println("Nou número: " + nouNumero);

        return nouNumero;
    }

    public static void mostrarExtraccions(int[] registreExtraccions) {
        int contadorPrintSalt, contadorPrintGeneral;
        contadorPrintGeneral = contadorPrintSalt = 0;
        System.out.print("|");
        for (int i = 0; i < registreExtraccions.length; i++) {
            if (registreExtraccions[i] == 0) {
                System.out.printf("%2d|", 0); //Numero no extret
            } else {
                System.out.printf("%2d|", i + 1); //Numero extret
            }
            contadorPrintSalt++;
            contadorPrintGeneral++;
            if (contadorPrintSalt == 10 && contadorPrintGeneral < registreExtraccions.length) {
                System.out.println();
                System.out.print("|");
                contadorPrintSalt = 0;
            }
        }
        System.out.println();
    }

    public static void comprovarCartrons(Cartro[] cartrons, int ultimaExtraccio) {
        int[] resultatCerca = new int[2];
        for (int i = 0; i < cartrons.length; i++) {
            boolean trobat = false;
            Tools.cercaNumBi(cartrons[i].cartroMarcat, ultimaExtraccio, resultatCerca);
            trobat = (resultatCerca[0] != -1 && resultatCerca[1] != -1);

            if (trobat) {
                System.out.printf("Aquest número el tenies al Cartro-%d, el marco com -1\n", i + 1);
                cartrons[i].cartroMarcat[resultatCerca[0]][resultatCerca[1]] = -1;
            }
            //trobat = false;
        }
    }

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
                } else {
                    contador = 0;
                    k = 0;
                }
                j++;
            }
            i++;
        }

        return linia;
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
