package crypto;

import static crypto.Helper.stringToBytes;

import java.util.Scanner;
import java.util.Arrays;

public class caesarFrequencies {
	public static void main(String[] args) {
		//----------------------computeFrequencies----------------------------------------
		Scanner scanner = new Scanner (System.in);
		System.out.print("Entrer le message � d�crypter : ");
		String message = scanner.nextLine();
		scanner.close();
		byte[] cipherText = {-85, -103, -90, -84, -103, 32, -84, -99, -92, -92, 32, -91, -99, 32, -95, -98, 32, -79, -89, -83, 32, -86, -99, -103, -92, -92, -79, 32, -101, -103, -86, -99, 32, -94, -83, -85, -84, 32, -95, -98, 32, -96, -99, 32, -81, -89, -90, 95, -84, 32, -102, -99, 32, -90, -99, -80, -84, 32, -79, -99, -103, -86, 32, -86, -99, -103, -92, -92, -79, 32, -86, -99, -103, -92, -92, -79, 32, -85, -89, -91, -99, -102, -89, -100, -79, 32, -96, -99, -92, -88};
		float [] cipher = new float [256];
		//pour compter la fr�quence d'apparition
		for (int i = 0; i<256; ++i) { //parcourt le tableau cipher de 0 � 256
			for (int j = 0; j<cipherText.length; ++j) { //parcourt le message
				if (i==cipherText[j]) {
					//compare l'index du tableau cipher, soit la valeur du caract�re, avec le caract�re i de cipherText
					cipher[i] = (float) (cipher[i] + 1); //si les valeurs sont �gales, on ajoute 1
				}
			}
		}
		for (int i = 0; i<256; ++i) {
			cipher[i] = (float) (cipher[i]/(cipherText.length-cipher[32]));
			//on divise le nombre d'apparitions d'un caract�re par le nombre total de caract�res (longueur du texte moins les espaces)
		}
		cipher[32]=0; //on ne compte pas les espaces
		System.out.println(Arrays.toString(cipher));

		
		//----------------------------Calcul de la cl�----------------------------------
		double[] ENGLISHFREQUENCIES = {0.08497,0.01492,0.02202,0.04253,0.11162,0.02228,0.02015,0.06094,0.07546,0.00153,0.01292,0.04025,0.02406,0.06749,0.07507,0.01929,0.00095,0.07587,0.06327,0.09356,0.02758,0.00978,0.0256,0.0015,0.01994,0.00077};
		double produit[] = new double[256];
		for (int i = 0; i<256; ++i) { //it�ration
			for (int j = 0; j<26; ++j){
					produit[i] = produit[i] + (cipher[(i+j)%256]*ENGLISHFREQUENCIES[j]); //%256 permet de ne pas d�passer les indices de cipher en les g�rant de fa�on cyclique
			}
			System.out.println("Produit scalaire pour it�ration "+ i +" = " + produit[i]);
		}
		System.out.println(Arrays.toString(produit));
		//on cherche la cl� i telle que la valeur de cipher[i] est maximale
		double ciphermax = 0.0; //on initialise le maximum � z�ro
		byte key = 0;
		for (int i = 0; i<256; ++i) {
			if(ciphermax<cipher[i]) {
				ciphermax = cipher[i];
				key = (byte) i;
			}
		}
	    key = (byte) (key);
		System.out.print(key);
		//-------------------------------------Combiner--------------------------------
		
		
		
	}
	
	
	
			
}