package ocp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;

/*
 * FileReader/Writer,FileInput/OutStream throws FileNotFoundException(subclass), IO Exception(superclass)
 * ObjectInputStream.readObject() throws ClassNotFoundException
 * 
 */
public class CH8_IO {

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		
		//file system default separator, windows \, linux /
		System.out.println(System.getProperty("file.separator"));
		System.out.println(File.separator);
		
		
		//check file, create a test.txt on the desk top
		String user = System.getProperty("user.name");
		System.out.println(user);
		File file = new File("C:\\Users\\"+user+"\\Desktop\\test.txt"); //without leading \
		//file = new File("C:\\Users\\feng\\Desktop\\test.txt");
		System.out.println(file.exists());
		System.out.println(file.isFile());
		System.out.println(file.isDirectory());
		file = new File("\\C:\\Users\\feng\\Desktop\\test.txt");//with leading \ which is root
		System.out.println(file.exists());
		File parent = new File("C:\\Users\\"+user+"\\Desktop");
		File child = new File(parent, "test.txt");
		System.out.println(file.exists());
		Long lastModified = file.lastModified();
		Date date = new Date(lastModified);
		System.out.println(date);
		
		
		//read file by using reader stream
		//Stream read and write from disk one byte a time. Using buffer would improve performance by reading a chuck bytes a time
		try(FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				FileWriter fileWriter = new FileWriter(file);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){
			
			//BufferReader read each line, return null if reach the end
			String line;
			while ( (line=bufferedReader.readLine()) != null){
				bufferedWriter.write(line);
				bufferedWriter.newLine();//reader ignore new line, so writer should add new line in the end of each line
			}
		}
		
		
		//read file using stream
		//InputStreamReader	Reads character data from an existing InputStream (convert stream to reader)
		try(FileInputStream fileInputStream = new FileInputStream(file);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
				ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
				InputStreamReader inputStreamReader = new InputStreamReader(bufferedInputStream);
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream)){
			
			/*mark method*/
			if (bufferedInputStream.markSupported()){
				bufferedInputStream.mark(100);
				//do something
				bufferedInputStream.reset();
			}
			/*skip*/
			bufferedInputStream.skip(100);
			/*FileInputStream read, return -1 if reach the end*/
			int b;
			while ( (b = fileInputStream.read()) != -1){
				fileOutputStream.write(b);
			}
			/*BufferedInputStream read, return length of bytes; if reach end, return length 0*/
			byte [] buffer = new byte[1024];
			int length;
			while ( (length=bufferedInputStream.read(buffer)) > 0){
				bufferedOutputStream.write(buffer, 0, length);
				bufferedOutputStream.flush();
			}
			
			
			/*
			 * Read and Write objects
			 * To find the end of the file, the proper technique is to catch an EOFException,
			 * which marks the program encountering the end of the file. 
			 * Notice that we don’t do anything with the exception other than finish the method. 
			 * This is one of the few times when it is perfectly acceptable to swallow an exception.*/
			class Employer{};
			while (true){
				Object object = objectInputStream.readObject();
				//read and write may have null, which is allowed. check instance of can avoid null object
				if (object instanceof Employer){
					objectOutputStream.writeObject(object);
				}
			}
		} catch(EOFException e){
			/*
			 * To find the end of the file, the proper technique is to catch an EOFException,
			 * which marks the program encountering the end of the file. 
			 * */
		}
		
		/*
		 * write files using writer
		 * Writes formatted representations of Java objects to a text-based output stream
		 * */
		try(FileWriter fileWriter = new FileWriter(file);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				PrintWriter printWriter = new PrintWriter(fileWriter)){
			
			bufferedWriter.flush();
			
		}
		
		/*
		 * write file using stream
		 * PrintStream Writes formatted representations of Java objects to a binary stream
		 * */
		try(FileOutputStream fileOutputStream = new FileOutputStream(file);
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
				PrintStream printStream = new PrintStream(bufferedOutputStream);){
			
			bufferedOutputStream.flush();
		}
		
		/*
		 * PrintWriter and PrintStream
		 * Write formatted representation of Java objects to a text-based output stream.
		 * For convenience, both of these classes include constructors that can open and write to files directly. 
		 * System.out and System.err are actually PrintStream objects.
		 * Unlike the underlying write() method, which throws a checked IOException that must be caught in your application, 
		 * these print-based methods do not throw any checked exceptions.
		 * Both classes provide a method, checkError(), that can be used to detect the presence of a problem after attempting to write data to the stream.
		 */
		
		try (PrintWriter printWriter = new PrintWriter(file);){
			/*
			 * print method calls Strnig.valueOf internally.
			 * valueOf() applied to an object calls the object’s toString() method 
			 * or returns null if the object is not set.
			 */
			printWriter.print(5);
			printWriter.write(String.valueOf(5));//same as print();
			printWriter.append(String.valueOf(5));//same as write();
			Animal animal = new Animal();
			printWriter.print(animal); // PrintWriter method
			printWriter.write(animal==null ? "null": animal.toString()); // Writer method
		}
		

	}

}
