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

    //Constants globals
    public static final Scanner scn = new Scanner(System.in);
    public static final Random rnd = new Random();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Declaració de variables
        final int TIPUS_JOC = 75;
        final String BOMBO = "@";
        final int MARCAT = -1;
        final int MAX_BOMBO = 4;
        boolean fiJoc, bingo, linia, seguent;
        fiJoc = bingo = linia = false;
        seguent = true;
        int contadorTirades, opcioJugador;
        contadorTirades = 0;
        opcioJugador = 1;

        int []extraccions = new int[TIPUS_JOC];
        
        do {
            System.out.print("Benvingut a la tarda de Bingo!\n"
                    + "Quants cartrons vols jugar? ");
            opcioJugador = Tools.validIntPositiu();

            Cartro[] cartrons = new Cartro[opcioJugador];
            crearCartrons(cartrons, TIPUS_JOC);
            mostrarCartro(cartrons, BOMBO);

            while (!bingo && contadorTirades < TIPUS_JOC /*&& seguent*/) {

                //extraccio > mostra extracció vàlida
                //marcar cartroMarcat si escau
                //seguent = continuarTirant("Següent número (s/n)?: ");
                //mostrar post extracció \\
                //mostrarCartroMarcat(cartrons, BOMBO, MARCAT);
            }

            fiJoc = repetirJoc();
            //seguent = true;
        } while (!fiJoc);
    }

    // ******METODES****** //
    public static void crearCartrons(Cartro[] cartrons, int tipusJoc) {
        for (int i = 0; i < cartrons.length; i++) {
            cartrons[i] = new Cartro();
            cartrons[i].cartro = new int[3][9];
            cartrons[i].cartroMarcat = new int[3][9];
            omplirCartrons(cartrons[i], tipusJoc);
        }
    }

    public static void omplirCartrons(Cartro cartro, int tipusJoc) {
        int numero = 0;
        int[] numeros = new int[tipusJoc];
        boolean colocat = false;
        for (int i = 0; i < cartro.cartro.length; i++) {
            for (int j = 0; j < cartro.cartro[i].length; j++) {
                do {
                    numero = rnd.nextInt(tipusJoc) + 1;
                    if (numeros[numero - 1] == 0) {
                        cartro.cartro[i][j] = numero;
                        cartro.cartroMarcat[i][j] = numero;
                        numeros[numero - 1] = 1;
                        colocat = true;
                    }
                } while (!colocat);
                colocat = false;
            }
        }
        posarSimbol(cartro.cartro, cartro.cartroMarcat);

    }

    public static void posarSimbol(int[][] cartro, int[][] cartroCopia) {
        int random, contador;
        random = contador = 0;

        for (int i = 0; i < cartro.length; i++) {
            int[] indexZeros = new int[cartro[i].length];
            do {
                random = rnd.nextInt(cartro[i].length); //+ 1;
                if (indexZeros[random] == 0) {
                    indexZeros[random] = 1;
                    cartro[i][random] = 0;
                    cartroCopia[i][random] = 0;
                    contador++;
                }
            } while (contador < 4);
            contador = 0;
        }
    }

    public static void mostrarCartro(Cartro[] cartrons, String simbol) {
        for (int i = 0; i < cartrons.length; i++) {
            System.out.println("Cartro " + (i + 1));
            for (int j = 0; j < cartrons[i].cartro.length; j++) {
                System.out.print("|");
                for (int k = 0; k < cartrons[i].cartro[j].length; k++) {
                    if (cartrons[i].cartro[j][k] == 0) {
                        System.out.printf("%2s|", simbol);
                    } else {
                        System.out.printf("%2d|", cartrons[i].cartro[j][k]);
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static void mostrarCartroMarcat(Cartro[] cartrons, String simbol, int marcat) {
        for (int i = 0; i < cartrons.length; i++) {
            System.out.println("Cartro " + (i + 1));
            for (int j = 0; j < cartrons[i].cartroMarcat.length; j++) {
                System.out.print("|");
                for (int k = 0; k < cartrons[i].cartroMarcat[j].length; k++) {
                    switch (cartrons[i].cartroMarcat[j][k]) {
                        case 0:
                            System.out.printf("%2s|", simbol);
                            break;
                        case -1:
                            System.out.printf("%2s", "X");
                            break;
                        default:
                            System.out.printf("%2d|", cartrons[i].cartro[j][k]);
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static void novaExtracció() {
        
    }

    public static boolean continuarTirant(String missatge) {
        String continuar;
        System.out.println(missatge);
        continuar = scn.next();
        if (continuar.equalsIgnoreCase("S")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean repetirJoc() {
        String novaPartida;

        System.out.print("Vols tornar a jugar?: ");
        novaPartida = scn.next();
        if (!novaPartida.equalsIgnoreCase("S")) {
            return true;
        } else {
            System.out.println("Seguim provant sort!");
            return false;
        }
    }
}
