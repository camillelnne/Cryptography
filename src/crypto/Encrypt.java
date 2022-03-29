package crypto;

import java.util.Random;
import static crypto.Helper.*;

public class Encrypt {
	
	public static final int CAESAR = 0;
	public static final int VIGENERE = 1;
	public static final int XOR = 2;
	public static final int ONETIME = 3;
	public static final int CBC = 4; 
	
	public static final byte SPACE = 32;
	
	final static Random rand = new Random();
	
	//-----------------------General-------------------------
	
	/**
	 * General method to encode a message using a key, you can choose the method you want to use to encode.
	 * @param message the message to encode already cleaned
	 * @param key the key used to encode
	 * @param type the method used to encode : 0 = Caesar, 1 = Vigenere, 2 = XOR, 3 = One time pad, 4 = CBC
	 * 
	 * @return an encoded String
	 * if the method is called with an unknown type of algorithm, it returns the original message
	 */
	public static String encrypt(String message, String key, int type) {
		byte[] plainText = stringToBytes(message);
		byte[] pad = stringToBytes(key);
		byte[] cipher = new byte [plainText.length];
		switch (type) {
		case CAESAR :
			System.out.println("------Caesar------");
			cipher = caesar(plainText, pad[0]); //remarque : pour un chiffre donn� en string la conversion en byte n'est pas �quivalente; ex : "5" �quivaut a [53]
			break;
		case VIGENERE :
			System.out.println("------Vigenere------");
			cipher = vigenere(plainText, pad);
			break;
		case XOR :
			System.out.println("-------XOR-------");
			cipher = xor(plainText, pad[0]);
			break;
		case ONETIME :
			System.out.println("----One-Time Pad----");
			cipher = oneTimePad(plainText, pad);
			break;
		case CBC :
			System.out.println("-------CBC-------");
			cipher = cbc(plainText, pad);
			break;
		default :
			cipher = plainText; //on va renvoyer le message original
			break;
		}
		String cipherText = bytesToString(cipher);
		return cipherText; 
	}
	
	
	//-----------------------Caesar-------------------------
	
	/**
	 * Method to encode a byte array message using a single character key
	 * the key is simply added to each byte of the original message
	 * @param plainText The byte array representing the string to encode
	 * @param key the byte corresponding to the char we use to shift
	 * @param spaceEncoding if false, then spaces are not encoded
	 * @return an encoded byte array
	 */
	public static byte[] caesar(byte[] plainText, byte key, boolean spaceEncoding) {
		assert(plainText != null);
		byte[] cipher = new byte[plainText.length];
		for (int i = 0; i<plainText.length; ++i) {
			if (spaceEncoding) { 
				cipher[i]= (byte) (plainText[i]+key); 
			}else { 
				if (plainText[i]!=32) { 
					cipher[i]= (byte) (plainText[i]+key);
		        }
				else {
					cipher[i] = plainText[i];
				}	
			}
		}
		return cipher;
	}
	
	/**
	 * Method to encode a byte array message  using a single character key
	 * the key is simply added  to each byte of the original message
	 * spaces are not encoded
	 * @param plainText The byte array representing the string to encode
	 * @param key the byte corresponding to the char we use to shift
	 * @return an encoded byte array
	 */
	public static byte[] caesar(byte[] plainText, byte key) {
		byte[] cipher = new byte[plainText.length]; 
		for (int i = 0; i<plainText.length; ++i) {
			if (plainText[i]!=32) { 
				cipher[i]= (byte) (plainText[i]+key);
	        }else {
				cipher[i] = plainText[i];
			}
		}
		return cipher;
	}
	
	//-----------------------XOR-------------------------
	
	/**
	 * Method to encode a byte array using a XOR with a single byte long key
	 * @param plaintext the byte array representing the string to encode
	 * @param key the byte we will use to XOR
	 * @param spaceEncoding if false, then spaces are not encoded
	 * @return an encoded byte array
	 */
	public static byte[] xor(byte[] plainText, byte key, boolean spaceEncoding) {
		byte[] cipher = new byte[plainText.length];
		for (int i = 0; i<plainText.length; ++i) {
			if (spaceEncoding) {
					cipher[i] = (byte) (plainText[i]^key);
			}else { 
				if (plainText[i]!=32) {
					cipher[i] = (byte) (plainText[i]^key);
				}else {
					cipher[i] = plainText[i];
				}
			}
		}
		return cipher;
	}
	/**
	 * Method to encode a byte array using a XOR with a single byte long key
	 * spaces are not encoded
	 * @param key the byte we will use to XOR
	 * @return an encoded byte array
	 */
	public static byte[] xor(byte[] plainText, byte key) {
		byte[] cipher = new byte[plainText.length];
		for (int i = 0; i<plainText.length; ++i) {
			if (plainText[i]!=32) { 
				cipher[i] = (byte) (plainText[i]^key); 
			}else {
				cipher[i] = plainText[i];
			}
		}
		return cipher;
	}
	//-----------------------Vigenere-------------------------
	
	/**
	 * Method to encode a byte array using a byte array keyword
	 * The keyword is repeated along the message to encode
	 * The bytes of the keyword are added to those of the message to encode
	 * @param plainText the byte array representing the message to encode
	 * @param keyword the byte array representing the key used to perform the shift
	 * @param spaceEncoding if false, then spaces are not encoded
	 * @return an encoded byte array 
	 */
	public static byte[] vigenere(byte[] plainText, byte[] keyword, boolean spaceEncoding) {
		byte[] cipher = new byte[plainText.length];
		if (plainText.length<=keyword.length) { //si le mot cl� est plus long que le message, on va le tronquer, si les tailles sont �gales la cl� tronqu�e sera �gale � la cl�
			byte[]keyshort = new byte [plainText.length]; //on cr�e un nouveau tableau de taille du message
			for (int i=0; i<plainText.length; ++i) {
				keyshort[i]=keyword[i];
				if (spaceEncoding) {
					cipher[i]=(byte) (plainText[i]+keyshort[i]); //on applique le chiffrement pour toute valeur de plainText[i]
				}else {
					if (plainText[i]==32) { //si plainText[i] correspond � un espace
						cipher[i]=plainText[i]; //on affecte plainText[i] directement � cipher[i] : �a reste un espace
					}else {
						cipher[i]=(byte) (plainText[i]+keyshort[i]); //on crypte plainText[i] en additionnant la valeur correspondante de la cl�
					}
				}
			} //remarque : on peut aussi regrouper dans cette boucle conditionnelle les deux cas "taille de la cl� inf�rieure ou �gale au message"
		}else if (plainText.length>keyword.length) { //si la taille du message est sup�rieure � celle de la cl�
			//on va chiffrer le message par parties de la m�me taille que la cl� (soit la partie multiple de la cl�)  puis le reste
			int modulo = plainText.length%keyword.length; //le reste
			for (int i = 0; i<plainText.length-modulo; i+=keyword.length) { //pour la partie de taille multiple � celle de la cl�
				for (int j = 0; j<keyword.length; ++j) {
					if (spaceEncoding){
						cipher[i+j]=(byte) (plainText[i+j]+keyword[j]);
					}else {
						if (plainText[i+j]==32) { //on ne chiffre pas les espaces
							cipher[i+j]=plainText[i+j];
						}else {
							cipher[i+j]=(byte) (plainText[i+j]+keyword[j]);
						}
					}
				}
			}//pour le reste :
			byte[] keymodulo = new byte[modulo]; //on tronque la cl� � la taille du reste du message
			for (int k = 0; k<modulo; ++k) {
				keymodulo[k]= keyword[k];
			}
			for (int i = plainText.length-modulo; i<plainText.length; ++i){ //on chiffre le reste de la m�me mani�re
				for (int j = 0; j<modulo; ++j) {
					if (spaceEncoding) {
						cipher[i+j]=(byte) (plainText[i+j]+keyword[j]);
					}else {
						if (plainText[i+j]==32) {
							cipher[i+j]=plainText[i+j];
						}else {
							cipher[i+j]=(byte) (plainText[i+j]+keyword[j]);
						}
					}
				}
			}
		}
		return cipher;
	}
	
	/**
	 * Method to encode a byte array using a byte array keyword
	 * The keyword is repeated along the message to encode
	 * spaces are not encoded
	 * The bytes of the keyword are added to those of the message to encode
	 * @param plainText the byte array representing the message to encode
	 * @param keyword the byte array representing the key used to perform the shift
	 * @return an encoded byte array 
	 */
	public static byte[] vigenere(byte[] plainText, byte[] keyword) {
		byte[] cipher = new byte[plainText.length];
		if (plainText.length<=keyword.length) { //si le mot cl� est plus long que le message, on va le tronquer, si les tailles sont �gales la cl� tronqu�e sera �gale � la cl�
			byte[]keyshort = new byte [plainText.length]; //on cr�e un nouveau tableau de taille du message
			for (int i=0; i<plainText.length; ++i) {
				keyshort[i]=keyword[i];
				if (plainText[i]==32) { //si plainText[i] correspond � un espace
					cipher[i]=plainText[i]; //on affecte plainText[i] directement � cipher[i] : �a reste un espace
				}else {
					cipher[i]=(byte) (plainText[i]+keyshort[i]); //on crypte plainText[i] en additionnant la valeur correspondante de la cl�
				}
			} //remarque : on peut aussi regrouper dans cette boucle conditionnelle les deux cas "taille de la cl� inf�rieure ou �gale au message"
		}else if (plainText.length>keyword.length) { //si la taille du message est sup�rieure � celle de la cl�
			//on va chiffrer le message par parties de la m�me taille que la cl� (soit la partie multiple de la cl�)  puis le reste
			int modulo = plainText.length%keyword.length; //le reste
			for (int i = 0; i<plainText.length-modulo; i+=keyword.length) { //pour la partie de taille multiple � celle de la cl�
				for (int j = 0; j<keyword.length; ++j) {
					if (plainText[i+j]==32) { //on ne chiffre pas les espaces
						cipher[i+j]=plainText[i+j];
					}else {
						cipher[i+j]=(byte) (plainText[i+j]+keyword[j]);
					}
				}
			}//pour le reste :
			byte[] keymodulo = new byte[modulo]; //on tronque la cl� � la taille du reste du message
			for (int k = 0; k<modulo; ++k) {
				keymodulo[k]= keyword[k];
			}
			for (int i = plainText.length-modulo; i<plainText.length; ++i){ //on chiffre le reste de la m�me mani�re
				for (int j = 0; j<modulo; ++j) {
					if (plainText[i+j]==32) {
						cipher[i+j]=plainText[i+j];
					}else {
						cipher[i+j]=(byte) (plainText[i+j]+keyword[j]);
					}
				}
			}
		}
		return cipher; 
	}
	
	
	
	//-----------------------One Time Pad-------------------------
	
	/**
	 * Method to encode a byte array using a one time pad of the same length.
	 *  The method  XOR them together.
	 * @param plainText the byte array representing the string to encode
	 * @param pad the one time pad
	 * @return an encoded byte array
	 */
	public static byte[] oneTimePad(byte[] plainText, byte[] pad) {
		assert (pad.length>=plainText.length); //v�rifie que la taille de la cl� et du message sont la m�me
		if (pad.length>plainText.length) {
				pad = new byte[plainText.length];
			for (int i=0; i<plainText.length; ++i) {
				pad[i]=pad[i];
			}	
		}
		byte[] cipher = new byte[plainText.length]; //on cr�e un tableau pour le message crypt� apr�s op�ration XOR
		for (int i = 0; i<plainText.length; ++i) { //boucle pour effectuer xor sur le message
			cipher[i] = (byte) (plainText[i]^pad[i]); //l'op�ration XOR donne des entiers qu'on convertit en bits pour les affecter au tableau
		}
		return cipher;
	}
	
	
	
	
	//-----------------------Basic CBC-------------------------
	
	/**
	 * Method applying a basic chain block counter of XOR without encryption method. Encodes spaces.
	 * @param plainText the byte array representing the string to encode
	 * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
	 * @return an encoded byte array
	 */
	public static byte[] cbc(byte[] plainText, byte[] iv) {
		byte[] cipher = new byte[plainText.length]; //boucle pour la partie du message divisible par la taille du pad
		int size = iv.length;
		int modulo = plainText.length%size;
			for (int i = 0; i<plainText.length-modulo; i+=size)  { 
					for (int j = 0; j<size; ++j) {
							cipher[i+j]=(byte) (plainText[i+j]^iv[j]);
							iv[j]=cipher[i+j]; //on remplace la cl� par la partie qui vient d'�tre crypt�e
					}
			}
			if (modulo!=0) { //si la taille du message n'est pas un multiple de la taille de cl�
				byte[] ivmodulo = new byte[modulo]; //on cr�e un tableau de byte de la taille du reste non cod� du message, auquel on va affecter le d�bute de la cl� cipher
				for (int k = 0; k<modulo; ++k) {
					ivmodulo[k]= cipher[k+(plainText.length-modulo-size)]; //on affecte a ivmodulo les valeurs de la derni�re partie crypt�e
				}
				for (int i = plainText.length-modulo; i<plainText.length; ++i){ //boucle pour la partie du message inf�rieure � la taille du pad//on parcourt la cl� de taille = modulo
						cipher[i]=(byte) (plainText[i]^ivmodulo[i-(plainText.length-modulo)]);
				}
			}
		return cipher;
	}
	
	
	/**
	 * Generate a random pad/IV of bytes to be used for encoding
	 * @param size the size of the pad
	 * @return random bytes in an array
	 */
	public static byte[] generatePad(int size) {
		byte [] pad = new byte [size]; //on cr�e un tableau de la taille voulue (d�pend si la m�thode est OTP ou CBC)
		for (int i = 0; i<size; ++i) { //on le remplit avec des entiers al�atoires convertis en bits
		    Random r = new Random();
			pad[i]= (byte) r.nextInt(256);
		}	
		return pad;
	}

	
	//-------------------Méthodes supplémentaires------------------------
	
	
//alternative a la methode caesar ou le cipher est composé uniquement de lettres de l'alphabet
//sachant que les valeurs correspondant aux lettres de l'alphabet sont comprises entre 97 et 122
	public static byte[] caesarAlphabet(byte[] plainText, byte key) {
		byte[] cipher = new byte[plainText.length]; 
		for (int i = 0; i<plainText.length; ++i) {
			if (plainText[i]!=32) { 
				cipher[i]= (byte) (plainText[i]+key);
				if (cipher[i]>122) { //dépassement de l'alphabet
					while (cipher[i]>122) { 
						cipher[i]= (byte) (cipher[i]-26); 
						//on soustrait 26 (taille de l'alphabet) jusqu'� ce que la valeur soit comprise entre 97 et 122
					}
				}else if (cipher[i]<97) { //dépassement de l'alphabet
					while (cipher[i]<97) {
						cipher[i]= (byte) (cipher[i]+26); 
						//on ajoute 26 (taille de l'alphabet) jusqu'� ce que la valeur soit comprise entre 97 et 122
					}
				}
	        }else {
				cipher[i] = plainText[i];
			}	
		}
		return cipher;
	}

	
	
	
}
