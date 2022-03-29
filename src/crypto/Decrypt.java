package crypto;

import static crypto.Helper.bytesToString;
import static crypto.Helper.stringToBytes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Decrypt {
	
	
	public static final int ALPHABETSIZE = Byte.MAX_VALUE - Byte.MIN_VALUE + 1 ; //256
	public static final int APOSITION = 97 + ALPHABETSIZE/2; 
	
	//source : https://en.wikipedia.org/wiki/Letter_frequency
	public static final double[] ENGLISHFREQUENCIES = {0.08497,0.01492,0.02202,0.04253,0.11162,0.02228,0.02015,0.06094,0.07546,0.00153,0.01292,0.04025,0.02406,0.06749,0.07507,0.01929,0.00095,0.07587,0.06327,0.09356,0.02758,0.00978,0.0256,0.0015,0.01994,0.00077};
	
	/**
	 * Method to break a string encoded with different types of cryptosystems
	 * @param type the integer representing the method to break : 0 = Caesar, 1 = Vigenere, 2 = XOR
	 * @return the decoded string or the original encoded message if type is not in the list above.
	 */
	public static String breakCipher(String cipher, int type) {
		String breakCipher = null;
		byte[] plainCipher = stringToBytes(cipher);
		byte[][] bruteForceResult = null;
		byte key = 0;
		switch(type) {
		case 0 :
			System.out.println("------Caesar decryption with frequencies------");
			key = caesarWithFrequencies(plainCipher);
			breakCipher = bytesToString(Encrypt.caesar(plainCipher, (byte) -key)); 
			//on chiffre le message avec l'opposé de la clé, qui équivaut à le déchiffrer avec la clé
			break;
		case 1:
			System.out.println("------Vigenere decryption with frequencies------");
			byte[] plainText = vigenereWithFrequencies(plainCipher); 
			breakCipher = bytesToString(plainText);
			break;
		case 2 :
			System.out.println("------XOR decryption with brute force------");
			bruteForceResult = xorBruteForce(plainCipher);
			breakCipher = arrayToString(bruteForceResult);
			break;
		default :
			breakCipher = cipher; //on retourne le message original
			break;
		}
		
		return breakCipher;
	}
	
	
	/**
	 * Converts a 2D byte array to a String
	 * @param bruteForceResult a 2D byte array containing the result of a brute force method
	 */
	public static String arrayToString(byte[][] bruteForceResult) {
		String message = null;
		for (int key=0; key<256; ++key) {
			message = bytesToString(bruteForceResult[key]) + System.lineSeparator();
		}
		return message; 
	}
	
	
	//-----------------------Caesar-------------------------
	
	/**
	 *  Method to decode a byte array  encoded using the Caesar scheme
	 * This is done by the brute force generation of all the possible options
	 * @param cipher the byte array representing the encoded text
	 * @return a 2D byte array containing all the possibilities
	 */
	public static byte[][] caesarBruteForce(byte[] cipher) {
		byte[][] bruteForce = new byte[256][cipher.length];
		for (int key=0; key<256; ++key) { //pour tester des clés de 0 à 255
			for (int j=0; j<cipher.length; ++j) { //parcourt le message
				if (cipher[j]==32) { //les espaces ne sont pas chiffrés, on les laisse tels quels
					bruteForce[key][j] = cipher[j];
				}else {
				bruteForce[key][j] = (byte) (cipher[j]-key); //on soustrait la clé à chaque caractère du message
				}
			}
		}
		return bruteForce;
	}	
	
	
	/**
	 * Method that finds the key to decode a Caesar encoding by comparing frequencies
	 * @param cipherText the byte array representing the encoded text
	 * @return the encoding key
	 */
	public static byte caesarWithFrequencies(byte[] cipherText) {
		float[] charFrequencies = computeFrequencies(cipherText);
		byte key = caesarFindKey(charFrequencies);

		return key;
	}
	
	/**
	 * Method that computes the frequencies of letters inside a byte array corresponding to a String
	 * @param cipherText the byte array 
	 * @return the character frequencies as an array of float
	 */
	public static float[] computeFrequencies(byte[] cipherText) {
		float [] charFrequencies = new float [256];
		//pour compter la fréquence d'apparition
		for (int i = 0; i<256; ++i) { //parcourt le tableau cipher de 0 à 256
			for (int j = 0; j<charFrequencies.length; ++j) { //parcourt le message
				if (i==cipherText[j]) { 
					//compare l'index du tableau cipher, soit la valeur du caractère, avec le caractère i de cipherText
					charFrequencies[i] = (float) (charFrequencies[i] + 1); //si les valeurs sont égales, on ajoute 1
				}
			}
		}
		for (int i = 0; i<256; ++i) {
			charFrequencies[i] = (float) (charFrequencies[i]/(cipherText.length-charFrequencies[32]));
			//on divise le nombre d'apparitions d'un caractère par le nombre total de caractères (longueur du texte moins les espaces)
		}
		charFrequencies[32]=0; //on ne compte pas les espaces
		return charFrequencies;
	}
	
	
	/**
	 * Method that finds the key used by a  Caesar encoding from an array of character frequencies
	 * @param charFrequencies the array of character frequencies
	 * @return the key
	 */
	public static byte caesarFindKey(float[] charFrequencies) {
		float [] cipher = new float [256];
		//pour compter la fréquence d'apparition
		for (int i = 0; i<256; ++i) { //parcourt le tableau cipher de 0 à 256
			for (int j = 0; j<charFrequencies.length; ++j) { //parcourt le message
				if (i==charFrequencies[j]) {
					//compare l'index du tableau cipher, soit la valeur du caractère, avec le caractère i de cipherText
					cipher[i] = (float) (cipher[i] + 1); //si les valeurs sont égales, on ajoute 1
				}
			}
		}
		for (int i = 0; i<256; ++i) {
			cipher[i] = (float) (cipher[i]/(charFrequencies.length-cipher[32]));
			//on divise le nombre d'apparitions d'un caractère par le nombre total de caractères (longueur du texte moins les espaces)
		}
		cipher[32]=0; //on ne compte pas les espaces
		System.out.println(Arrays.toString(cipher));

		
		//----------------------------Calcul de la clé----------------------------------
		double[] ENGLISHFREQUENCIES = {0.08497,0.01492,0.02202,0.04253,0.11162,0.02228,0.02015,0.06094,0.07546,0.00153,0.01292,0.04025,0.02406,0.06749,0.07507,0.01929,0.00095,0.07587,0.06327,0.09356,0.02758,0.00978,0.0256,0.0015,0.01994,0.00077};
		double produit[] = new double[256];
		for (int i = 0; i<256; ++i) { //itération
			for (int j = 0; j<26; ++j){
					produit[i] = produit[i] + (cipher[(i+j)%256]*ENGLISHFREQUENCIES[j]); //%256 permet de ne pas dépasser les indices de cipher en les gérant de façon cyclique
			}
			System.out.println("Produit scalaire pour itération "+ i +" = " + produit[i]);
		}
		System.out.println(Arrays.toString(produit));
		//on cherche la clé i telle que la valeur de cipher[i] est maximale
		double ciphermax = 0.0; //on initialise le maximum à zéro
		byte key = 0;
		for (int i = 0; i<256; ++i) {
			if(ciphermax<cipher[i]) {
				ciphermax = cipher[i];
				key = (byte) i;
			}
		}
	    key -= 97;
		return key; 
	}
	
	
	
	//-----------------------XOR-------------------------
	
	/**
	 * Method to decode a byte array encoded using a XOR 
	 * This is done by the brute force generation of all the possible options
	 * @param cipher the byte array representing the encoded text
	 * @return the array of possibilities for the clear text
	 */
	public static byte[][] xorBruteForce(byte[] cipher) {
		//fonctionne de la même manière que caesarBruteForce
		byte[][] bruteForce = new byte[256][cipher.length];
		for (int key=0; key<256; ++key) {
			for (int j=0; j<cipher.length; ++j) {
				if (cipher[j]==32) { //si c'est un espace
					bruteForce[key][j] = cipher[j]; //rien à déchiffrer
				}else {
				bruteForce[key][j] = (byte) (cipher[j]^key); //opération XOR entre chaque caractère du message et la clé
				}
			}
		}
		return bruteForce;
	}
	
	
	
	//-----------------------Vigenere-------------------------
	// Algorithm : see  https://www.youtube.com/watch?v=LaWp_Kq0cKs	
	/**
	 * Method to decode a byte array encoded following the Vigenere pattern, but in a clever way, 
	 * saving up on large amounts of computations
	 * @param cipher the byte array representing the encoded text
	 * @return the byte encoding of the clear text
	 */
	public static byte[] vigenereWithFrequencies(byte[] cipher) {
		List<Byte> list = removeSpaces(cipher);
		int keyLength = vigenereFindKeyLength(list);
		byte[] keyword = vigenereFindKey(list, keyLength);
		byte[] plainText = Encrypt.vigenere(cipher, keyword); //on déchiffre le message codé en appliquant la clé inverse
		return plainText; 
	}
	
	
	
	/**
	 * Helper Method used to remove the space character in a byte array for the clever Vigenere decoding
	 * @param array the array to clean
	 * @return a List of bytes without spaces
	 */
	public static List<Byte> removeSpaces(byte[] array){
		List<Byte> list = new ArrayList <Byte >();
		for (int i = 0; i<array.length; ++i) {
			if (array[i]!=32) { //on ajoute les éléments de cipher qui ne sont pas des espaces
				list.add(array[i]);
			}
		}
		return list;
	}
	
	
	/**
	 * Method that computes the key length for a Vigenere cipher text.
	 * @param cipher the byte array representing the encoded text without space
	 * @return the length of the key
	 */
	public static int vigenereFindKeyLength(List<Byte> cipher) {
		//on met le array list dans un array
		int listSize = cipher.size();
		byte[] array = new byte [listSize];
		for (int i = 0; i<listSize; ++i) {
			array[i] = cipher.get(i);
		}
		
		//on note les coincidences dans un tableau
		byte[] coincidence = new byte[listSize];
		for (int i = 1; i<listSize; ++i) { //boucle pour itérer
			for (int j= 0; j<(listSize-i); ++j) { //boucle pour parcourir le message
				if (array[j]==array[i+j]) { //si il y a coincidence pour une itération de i
					coincidence[i+j]+=1;
				}
			}
		}
		System.out.println(Arrays.toString(coincidence));
		
		//on repère les maximums locaux sur la première moitié soit i tel que coincidence[i]>coincidence[i+-1]
		List<Integer> max = new ArrayList <Integer>();
		int moitié =(int) Math.ceil(listSize/2.0);
		for (int i = 0; i<moitié; ++i) { //seulement sur la première moitié du tableau			
			switch(i) {
			case 0:
				if((coincidence[i]>coincidence[i+1]) && (coincidence[i]>coincidence[i+2])) { 
					//on compare la valeur avec les deux suivantes
					max.add(i);
				}
				break;
			case 1:
				if((coincidence[i]>coincidence[i-1]) && (coincidence[i]>coincidence[i+1]) && (coincidence[i]>coincidence[i+2])) { 
					//on compare la valeur avec la précédente et les deux suivantes
					max.add(i);
				}
				break;
			default:
				if((coincidence[i]>coincidence[i-1]) && (coincidence[i]>coincidence[i-2]) && (coincidence[i]>coincidence[i+1]) && (coincidence[i]>coincidence[i+2])) { 
					//on compare la valeur avec les deux précédentes et les deux suivantes
					max.add(i);
				}
				break;
			}
		}
		System.out.println(max);
		
		//---------------------------------------Récupérer la taille de la clé-------------------------------------
		//on met la liste max dans un tableau
		int maxSize = max.size(); 
		int[] maxArray = new int [maxSize];
		for (int i = 0; i<maxSize; ++i) {
			maxArray[i] = max.get(i);
		}
		int[] distance = new int [maxSize-1]; //on crée un tableau pour y mettre les distances entre chaque indice contenus dans maxArray
		for (int i = 0; i<(maxSize-1); ++i) {
			distance[i]=maxArray[i+1]-maxArray[i];
		}
		
		int[] recurrence = new int [maxSize-1]; //tableau pour le nombre de fois qu'on trouve la même distance entre deux indices
		for(int i = 0; i<(maxSize-1); ++i) {
			for(int j = 0; j<(maxSize-1); ++j) {
				if(distance[i]==distance[j]) {
					recurrence[i]+=1;
				}
			}
		}
		//on cherche la valeur maximale de récurrence
		int recurrenceMax = 0;
		int keyLength = 0;
		for(int i = 0; i<(maxSize-1); ++i) {
			if(recurrenceMax<recurrence[i]) {
				recurrenceMax = recurrence[i];
				keyLength = i;
			}
		}
		return keyLength;
	}

	
	
	
	
	//-----------------------Basic CBC-------------------------
	
	/**
	 * Method used to decode a String encoded following the CBC pattern
	 * @param cipher the byte array representing the encoded text
	 * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
	 * @return the clear text
	 */
	public static byte[] decryptCBC(byte[] cipher, byte[] iv) {
		int size = iv.length;
		byte[] plainText = new byte[cipher.length]; //boucle pour la partie du message divisible par la taille du pad
		int modulo = cipher.length%size;
		for (int i = 0; i<cipher.length-modulo; i+=size)  {
				for (int j = 0; j<size; ++j) {
					plainText[i+j]=(byte) (cipher[i+j]^iv[j]);
					iv[j]=cipher[i+j]; //on remplace la clé par la partie cryptée que l'on vient de décrypter
				}
		}
		if (modulo!=0) { //si la taille du message n'est pas un multiple de la taille de clé
			byte[] ivmodulo = new byte[modulo]; //on crée un tableau de byte de la taille du reste non codé du message, auquel on va affecter le débute de la clé cipher
			for (int k = 0; k<modulo; ++k) {
				ivmodulo[k]= cipher[k+(plainText.length-modulo-size)]; //on affecte a ivmodulo une partie de taille modulo du dernier plainText obtenu
			}
			for (int i = cipher.length-modulo; i<cipher.length; ++i){ //boucle pour la partie du message inférieure à la taille du pad
					plainText[i]=(byte) (cipher[i]^ivmodulo[i-(cipher.length-modulo)]);
			}
		}
		return plainText; 
	}
	
	
	

		
		
		
		
		
}
