package ocp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Ch8_Deserialization {

	public static void main(String[] args) {
		
/*
 * When you deserialize an object, the constructor of the serialized class is not called. 
 * In fact, Java calls the first no-arg constructor for the first nonserializable parent class, 
 * skipping the constructors of any serialized class in between. 
 * Furthermore, any static variables or default initializations are ignored.
 */
		Animal animal = new Animal("name",20,'C');
		System.out.println(animal);
		String uri = "c:\\users\\"+System.getProperty("user.name")+"\\desktop\\animal.text";
		File file = new File(uri);
		try (FileOutputStream fileOutputStream = new FileOutputStream(file);
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);){
			
			objectOutputStream.writeObject(animal);
			objectOutputStream.flush();
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		} 
		
		try (FileInputStream fileInputStream = new FileInputStream(file);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
				ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream)){
			
			Object object = objectInputStream.readObject();
			if (object instanceof Animal){
				animal = (Animal) object;
				System.out.println(animal);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}

class Animal implements Serializable {
		private static final long serialVersionUID = 2L;
		private transient String name;
		private int age = 10;
		private static char type = 'C';
		{this.age = 14;}

		public Animal() {
			this.name = "Unknown";
			this.age = 12;
			this.type = 'Q';
		}

		public Animal(String name, int age, char type) {
			this.name = name;
			this.age = age;
			this.type = type;
		}
		public String getName() { return name; }
		public int getAge() { return age; }
		public char getType() { return type; }

		public String toString() {
			return "Animal [name=" + name + ", age=" + age + ", type=" + type + "]";
		}
	}
