//------------------------------------------------------------------
// University of Central Florida
// CIS3360 - Fall 2017
// Program Author: Hylton Williamson
//------------------------------------------------------------------

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.io.File;
import java.io.FileNotFoundException;

public class CbcVigenere {
	public static void main(String[] args)
	{
		
		File file  = new File(args[0]);
		//Will hold the entire message into a string for manipulation.
		String message = "";
		Scanner s;
		String key = args[1];
		String iVector = args[2];
		int padding = 0, count = 0;
		
		//Tries to read in file and returns and exception if the file fails to open.
		try 
		{
			//Read in the file into scanner and store the first line into message.
			s = new Scanner(file);
			message = s.nextLine();
			
			//Continues to add lines into message until there is nothing left.
			while(s.hasNextLine())
			{
				message = message + "\n" + s.nextLine();
			}
			System.out.println("Clean Plaintext: \n");
			
			//Converts all characters of the string into lower case.
			message = message.toLowerCase();
			
			//Removes everything that isn't a letter between a to z inclusively.
			message = message.replaceAll("[^a-z]", "");
			count = message.length();
			
			//Replaces the first 80 characters with themselves and
			//puts a newline at the end for the output's readability.
			message = message.replaceAll("(.{80})", "$1\n");
			System.out.println(message);
			
			//Removes the newlines just put in so the plain text can pad correctly.
			message = message.replaceAll("[^a-z]", "");
			
			//Call to the replaceDuplicates method in case consecutive letters aren't allow. 
			//message = replaceDuplicates(message);
			
			//Adds x's to the end of message until message.length() is a multiple of key.lenght().
			while(message.length() % key.length() != 0){
            	message += "x";
            	padding++;
            }
			
			//Returns the newly encrypted message back into message.
			message = encrypt(key, iVector, message);
			
			//Adds newline every 80 characters for readability.
			message = message.replaceAll("(.{80})", "$1\n");
			
			System.out.println("Ciphertext: \n");
			System.out.println(message);
			System.out.printf("\nNumber of characters in clean plain file: %d\n", count);
			System.out.printf("Block size = %d\n", key.length());
			System.out.printf("Number of pad characters added: %d", padding);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Changes the string into a char array and compares the current and next char
	//to see if they are the same. If they are the next char gets 'x' else
	//the next char gets itself. Converts the char array back into a string
	//and returns it.
	public static String replaceDuplicates(String str)
	{
		char[] ch = str.toCharArray();
		
		for(int i = 0; i < str.length() - 1; i++)
		{
			ch[i+1] = (ch[i] == ch[i+1]) ? 'x' : ch[i+1];
		}
		String done = new String(ch);
		return done;
	}
	
	//For every block of key.length() chars they are encrypted
	//with the key initialization vector. After encryption of those 
	//key.length() chars, they become the new IV and the process repeats
	//until the end of the input. Returns newly encrypted input.
	public static String encrypt(String key, String vector, String str)
	{
		int maxIndx = 0, lowerIndx = 0, section = 0, k , j , i;
		char[] ch = str.toCharArray();
		char[] ke = key.toCharArray();
		char[] ve = vector.toCharArray();
		String result;
		
		for(i = 0; i < str.length(); i++)
		{
			
			if((i+1) % key.length() == 0)
			{
				section = (i+1)/key.length();
				maxIndx = (key.length() * section) - 1;
				lowerIndx = (key.length() * (section - 1)) - 1;
				
				for(j = lowerIndx + 1, k = 0; j <= maxIndx; j++, k++ )
				{
					ch[j] += ve[k] + ke[k];
					ch[j] = convertToCorrectChar(ch[j]);
					ve[k] = ch[j];
				}
			}
		}
		System.out.println();
		result = new String(ch);
		return result;
	}
	
	//Takes in a char containing the sum of three chars from encrypt and
	//converts them into the ASCII values following the a=0 z=25 loop.
	public static char convertToCorrectChar(char letter)
	{
		int result = (letter - (97*3)) % 26 + 97;
		return (char) result;
	}
	
	//Randomly generates an initialization vector with the same length as the
	//key.
	public static String createIVector(String key)
	{
		int n = key.length(), i;
		char[] holder = new char[n];
		String vec;
		
		for(i = 0; i < n; i++)
		{
			holder[i] = (char) (ThreadLocalRandom.current().nextInt(97, 122 + 1));
		}
		vec = new String(holder);
		return vec;
	}
}
