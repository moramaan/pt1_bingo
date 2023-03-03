/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pt1_bingo;

import java.util.Scanner;

/**
 * 
 * @author moraman
 */
public class Tools {


    /**
     * Aquest mètode demana un nombre enter fins que l'usuari introdueix un de
     * més gran que zero.
     * @return El nombre introduït per l'usuari, quan és vàlid.
     */
    public static int validIntPositiu() {
        Scanner scn = new Scanner(System.in);
        boolean llegit = false;
        int val = 0;
        do {
            if (scn.hasNextInt()) {
                val = scn.nextInt();
                if (val > 0) {
                    llegit = true;
                } else {
                    System.out.println("Error! Introdueix un nombre sencer positiu!");
                }
            } else {
                System.out.println("Error! Introdueix un nombre sencer positiu!");
                scn.nextLine();
            }
        } while (!llegit);

        return val;
    }

    /**
     * Aquest mètode cerca un nombre enter en un vector de dues dimensions i
     * emmagatzema en un array de sencers la posició en cas de trobar-lo.
     *
     * @param array vector de dues dimensions on s'ha de cercar.
     * @param num nombre enter a cercar.
     * @param posicioTrobat Array que emmagatzema la posició en cas de trobar 
     * el nombre o [-1, -1] en cas contrari.
     *
     */
    public static void cercaNumBi(int array[][], int num, int[] posicioTrobat) {
        boolean trobat = false;
        int i, j;
        i = j = 0;

        while (!trobat && i < array.length) {
            while (!trobat && j < array[i].length) {
                if (num == array[i][j]) {
                    posicioTrobat[0] = i;
                    posicioTrobat[1] = j;
                    trobat = true;
                } else {
                    j++;
                }
            }
            j = 0;
            i++;
        }
        if (!trobat) {
            posicioTrobat[0] = -1;
            posicioTrobat[1] = -1;
        }
    }
}
