package crypto;

import java.util.Scanner;

import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;
import static crypto.Helper.stringToBytes;

public class VigenereFrequencies {
	public static void main(String[] args) {
		Scanner scanner = new Scanner (System.in);
		System.out.print("Entrer le message � d�crypter : ");
		String message = scanner.nextLine();
		scanner.close();
		byte[] cipher = stringToBytes(message);
		
		//------------------------removeSpace-----------------------
		//on met les �l�ments de cipher dans une array list
		List<Byte> list = new ArrayList <Byte>();
		for (byte b : cipher) {
			if (b != 32) { //on ajoute les �l�ments de cipher qui ne sont pas des espaces
				list.add(b);
			}
		}
		System.out.println(list);
		
		//------------------------find key length-------------------
		//on met le array list dans un array
		int listSize = list.size();
		byte[] array = new byte [listSize];
		for (int i = 0; i<listSize; ++i) {
			array[i] = list.get(i);
		}
		System.out.println(Arrays.toString(array));
		
		//on note les coincidences dans un tableau
		byte[] coincidence = new byte[listSize];
		for (int i = 1; i<listSize; ++i) { //boucle pour it�rer
			for (int j= 0; j<(listSize-i); ++j) { //boucle pour parcourir le message
				if (array[j]==array[i+j]) { //si il y a coincidence pour une it�ration de i
					coincidence[i+j]+=1;
				}
			}
		}
		System.out.println(Arrays.toString(coincidence));
		
		//on rep�re les maximums locaux sur la premi�re moiti� soit i tel que coincidence[i]>coincidence[i+-1]
		List<Integer> max = new ArrayList <Integer>();
		int half =(int) Math.ceil(listSize/2.0);
		for (int i = 0; i<half; ++i) { //seulement sur la premi�re moiti� du tableau
			switch(i) {
			case 0:
				if((coincidence[i]>coincidence[i+1]) && (coincidence[i]>coincidence[i+2])) { 
					//on compare la valeur avec les deux suivantes
					max.add(i);
				}
				break;
			case 1:
				if((coincidence[i]>coincidence[i-1]) && (coincidence[i]>coincidence[i+1]) && (coincidence[i]>coincidence[i+2])) { 
					//on compare la valeur avec la pr�c�dente et les deux suivantes
					max.add(i);
				}
				break;
			default:
				if((coincidence[i]>coincidence[i-1]) && (coincidence[i]>coincidence[i-2]) && (coincidence[i]>coincidence[i+1]) && (coincidence[i]>coincidence[i+2])) { 
					//on compare la valeur avec les deux pr�c�dentes et les deux suivantes
					max.add(i);
				}
				break;
			}
		}
		System.out.println(max);
		
		//---------------------------------------R�cup�rer la taille de la cl�-------------------------------------
		//on met la liste max dans un tableau
		int maxSize = max.size(); 
		int[] maxArray = new int [maxSize];
		for (int i = 0; i<maxSize; ++i) {
			maxArray[i] = max.get(i);
		}
		int[] distance = new int [maxSize-1]; //on cr�e un tableau pour y mettre les distances entre chaque indice contenus dans maxArray
		for (int i = 0; i<(maxSize-1); ++i) {
			distance[i]=maxArray[i+1]-maxArray[i];
		}
		
		int[] recurrence = new int [maxSize-1]; //tableau pour le nombre de fois qu'on trouve la m�me distance entre deux indices
		for(int i = 0; i<(maxSize-1); ++i) {
			for(int j = 0; j<(maxSize-1); ++j) {
				if(distance[i]==distance[j]) {
					recurrence[i]+=1;
				}
			}
		}
		//on cherche la valeur maximale de r�currence
		int recurrenceMax = 0;
		int keyLength = 0;
		for(int i = 0; i<(maxSize-1); ++i) {
			if(recurrenceMax<recurrence[i]) {
				recurrenceMax = recurrence[i];
				keyLength = i;
			}
		}
		System.out.print("Key length = " + keyLength);
	}
}
