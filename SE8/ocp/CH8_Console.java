package ocp;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class CH8_Console {

	public static void main(String[] args) throws IOException {
		/*
		 * old way
		 * System.in returns an InputStream
		 * wrap the System.in object using the InputStreamReader
		 */
		try(InputStream inputStream = System.in;
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader)){
			
			String s = bufferedReader.readLine();
			System.out.println("You entered : "+s);
		}
		
		Console console = System.console();
		if (console == null){
			System.out.println("Console is null");
		}else{
			String s = console.readLine();
			console.writer().println("Console read : " + s);
			/*
			 * read password return char array because As soon as the data is read and used, 
			 * the sensitive password data in the array can be “erased” by writing garbage data 
			 * to the elements of the array. If stored as string, immutable object stays forever
			 */
			char[] password = console.readPassword("Enter your password: ");
			console.format("Enter your password again:  ");
			console.flush();
			char[] verify = console.readPassword();
			boolean match = Arrays.equals(password,verify);
			// Immediately clear passwords from memory
			Arrays.fill(password,'x');
			console.format("Your password was "+(match ? "correct": "incorrect"));
		}
		
	}

}
