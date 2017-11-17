package ocp;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.UserPrincipal;
import java.util.stream.Stream;

public class CH9_Attributes {

	public static void main(String[] args) {
		
		Path path = Paths.get("/zoo/animals/bear/koala/food.txt");
		System.out.println(path.subpath(1,3).getName(1));
		System.out.println(path.subpath(1,3).getName(1).toAbsolutePath());
		
		System.out.println(Paths.get(".").normalize().getName(0));

		try {
			UserPrincipal userPrincipal = FileSystems.getDefault().getUserPrincipalLookupService()
					.lookupPrincipalByName("feng");
			System.out.println(userPrincipal.getName());
			path = Paths.get("c:/users/feng/desktop");
			System.out.println(Files.getOwner(path).getName());
			Files.setOwner(path, userPrincipal);
		} catch (IOException e) {
			e.printStackTrace();
		}

		path = Paths.get("se8/ocp/CH9_Attributes.java");
		System.out.println(Files.isRegularFile(path));
		BasicFileAttributes data;
		try {
			data = Files.readAttributes(path, BasicFileAttributes.class);
			System.out.println("Is path a directory? " + data.isDirectory());
			System.out.println("Is path a regular file? " + data.isRegularFile());
			System.out.println("Is path a symbolic link? " + data.isSymbolicLink());
			System.out.println("Path not a file, directory, nor symbolic link? " + data.isOther());
			System.out.println("Size (in bytes): " + data.size());
			System.out.println("Creation date/time: " + data.creationTime());
			System.out.println("Last modified date/time: " + data.lastModifiedTime());
			System.out.println("Last accessed date/time: " + data.lastAccessTime());
			System.out.println("Unique file identifier (if available): " + data.fileKey());
		} catch (IOException e) {
			e.printStackTrace();
		}

		BasicFileAttributeView view = Files.getFileAttributeView(path, BasicFileAttributeView.class);
		try {
			BasicFileAttributes attributes = view.readAttributes();
			FileTime fileTime = attributes.lastModifiedTime();
			System.out.println("FileTime before change: " + fileTime);
			fileTime = FileTime.fromMillis(fileTime.toMillis() + 100_100);
			view.setTimes(fileTime, null, null);
			System.out.println("FileTime after change: " + fileTime);

		} catch (IOException e) {
		}

		// traverse directories. Files.walk(path) returns Stream<Path>
		try {
			Files.walk(Paths.get("."))
			.filter(p -> p.toString().endsWith("Attributes.java"))
			.forEach(System.out::println);
		} catch (IOException e) {
			// Handle file I/O exception...
		}

		//Files.find(path, depth, BiPredicate), another way to find a files
		try {
			Files.find(Paths.get("."), 10, (p, b)-> p.toString().endsWith("Attributes.java")
					&& b.creationTime().toMillis() < System.currentTimeMillis())
			.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Files.list() searches one level deep, and return Stream
		try {
			Files.list(Paths.get("se8"))
			.filter(p -> !Files.isDirectory(p))
			.map(p -> p.toAbsolutePath())
			.forEach(System.out::println);
		} catch (IOException e) {
			// Handle file I/O exception...
		}

		//Files.readAllLines() read a very large file could result in an OutOfMemoryError problem. 
		//Files.lines(Path) method that returns a Stream<String> object and does not suffer from this same issue.
		//read and processed lazily, which means that only a small portion of the file is stored in memory at any given time.
		try {
			Stream<String> stream = Files.lines(Paths.get("se8/ocp/Zoo_en.properties"));
			stream.forEach(System.out::println);
			stream.close();
		} catch (IOException e) {
		}

	}

}
