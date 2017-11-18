package ocp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

/*
 * NIO.2 also includes helper classes such as java.nio.file.Files, 
 * whose primary purpose is to operate on instances of Path objects.
 */
public class CH9_Paths_Files {

	public static void main(String[] args) {

		/* absolute apth */
		String s = "c:\\users\\" + System.getProperty("user.name") + "\\desktop";
		Path path = Paths.get(s);
		System.out.println("Path is  " + path);
		/*
		 * same as the one above. Paths.get internally use
		 * FileSystems.getDefault().getPath()
		 */
		path = FileSystems.getDefault().getPath(s);
		Path absolutePath = Paths.get(s);
		Path relativePath = Paths.get("relative/parent/child");
		/*
		 * retrieve the number of elements in the path Notice that the root
		 * element / is not included in the list of names. If the Path object
		 * represents the root element itself, then the number of names in the
		 * Path object returned by getNameCount() will be 0.
		 */
		for (int i = 0; i < path.getNameCount(); i++) {
			Path tempPath = path.getName(i);
			System.out.println("getName()  " + tempPath);
		}
		/* subpath() doesn't include root */
		System.out.println("subpath()  " + path.subpath(0, path.getNameCount()));

		/* getFileName() returns a new Path instance rather than a String. */
		Path tempPath = path.getFileName();
		System.out.println("getFileName()  " + tempPath);

		/* getParent() only work on the path provided, not traverse outside */
		tempPath = path;
		while ((tempPath = tempPath.getParent()) != null) {
			System.out.println("getParent()  " + tempPath);
		}
		tempPath = Paths.get("relative/parent/child");
		while ((tempPath = tempPath.getParent()) != null) {
			System.out.println("getParent()  " + tempPath);
		}

		/*
		 * getRoot(), returns the root element for the Path object or null if
		 * the Path object is relative.
		 */
		System.out.println(path.getRoot());
		tempPath = Paths.get("relative/parent/child");
		System.out.println("getRoot()  " + (tempPath.getRoot() == null));// relative
																			// path
																			// return
																			// null

		/* relative path */
		String relative = System.getProperty("user.name") + "desktop";
		path = Paths.get(relative);
		path = FileSystems.getDefault().getPath(relative);

		/*
		 * operating system-dependent path.separator is automatically inserted
		 * between elements
		 */
		path = Paths.get("c:", "users", System.getProperty("user.name"), "desktop");

		/* Working with Legacy File Instances */
		File file = new File(s);
		path = file.toPath();
		file = path.toFile();

		/* isAbsolute() */
		System.out.println("isAbsolute()  " + absolutePath.isAbsolute());
		System.out.println("isAbsolute()  " + relativePath.isAbsolute());

		/*
		 * toAbsolutePath() converts a relative Path object to an absolute Path
		 * object by joining it to the current working directory. If the Path
		 * object is already absolute, then the method just returns an
		 * equivalent copy of it.
		 */
		System.out.println("toAbsolutePath()  " + absolutePath.toAbsolutePath());
		System.out.println("toAbsolutePath()  " + relativePath.toAbsolutePath());

		/* Path Symbols .. parent, . current */
		path = Paths.get("aninal/dog/../cat");
		System.out.println("Path Symbols  " + path);

		/* normalize() clean up ../, ./ */
		System.out.println("after nomarlize()  " + path.normalize());

		/*
		 * relativize(Path) for constructing the relative path from one Path
		 * object to another. relativize() method requires that both paths be
		 * absolute or both relative, it will throw an IllegalArgumentException
		 * if a relative path value is mixed with an absolute path value.
		 */
		Path path2 = Paths.get("bird.txt");
		Path path3 = Paths.get("Dog.txt");
		tempPath = path2.relativize(path3);
		System.out.println("relativize(Path) relative  " + tempPath);
		path2 = Paths.get("../bird.txt");
		path3 = Paths.get("./Dog.txt");
		tempPath = path2.relativize(path3);
		System.out.println("relativize(Path) relative " + tempPath);
		path2 = Paths.get("E:\\habitat");
		path3 = Paths.get("E:\\sanctuary\\raven");
		System.out.println("relativize(Path) absolute " + path2.relativize(path3));
		System.out.println("relativize(Path) absolute " + path3.relativize(path2));

		/*
		 * The Path interface includes a resolve(Path) method for creating a new
		 * Path by joining an existing path to the current path.
		 */
		path = Paths.get("c:/user/feng/desktop/test.txt");
		path2 = Paths.get("java.txt");
		path3 = Paths.get("test.txt");
		System.out.println("resolve()  absolute.resolve(relative)  " + path.resolve(path2));// absolute
																							// append
																							// relative
		System.out.println("resolve()  relative.resolve(absolute)  " + path2.resolve(path));// ignore
																							// 1st,
																							// copy
																							// 2nd
																							// absolute
		System.out.println("resolve()  relative.resolve(relative)  " + path2.resolve(path3));// 1st
																								// append
																								// 2nd
		System.out.println("resolve()  absolute.resolve(absolute)  " + path.resolve(path));// ingore
																							// 1st,
																							// copy
																							// 2nd
																							// absolute

		/*
		 * toRealPath() check if file exists, throw IOException. Also perform
		 * normalize() implicitly
		 */
		try {
			path = Paths.get("c:/users/feng/desktop/").toRealPath(LinkOption.NOFOLLOW_LINKS);
			System.out.println("toRealPath()  " + path);
			System.out.println("toRealPath() current path   " + Paths.get(".").toRealPath());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		System.out.println("Files.exists()  " + Files.exists(path));
		try {
			System.out.println("Files.isSameFile()  " + Files.isSameFile(path, path));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			Files.createDirectory(Paths.get("/bison/field"));
			// creates the target directory along with any nonexistent parent
			// directories leading up to the target directory in the path.
			Files.createDirectories(Paths.get("/bison/field/pasture/green"));
		} catch (IOException e) {
		}

		try {
			Files.delete(Paths.get("/vulture/feathers.txt"));
			Files.deleteIfExists(Paths.get("/pigeon"));
		} catch (IOException e) {
		}

		try {
			Files.move(Paths.get("c:\\zoo"), Paths.get("c:\\zoo-new"));
			Files.move(Paths.get("c:\\user\\addresses.txt"), Paths.get("c:\\zoo-new\\addresses.txt"));
		} catch (IOException e) {
			// Handle file I/O exception...
		}

		try (InputStream is = new FileInputStream("source-data.txt");
				OutputStream out = new FileOutputStream("output-data.txt")) {
			// Copy stream data to file
			Files.copy(is, Paths.get("c:\\mammals\\wolf.txt"));
			// Copy file data to stream
			Files.copy(Paths.get("c:\\fish\\clown.xsl"), out);
		} catch (IOException e) {
			// Handle file I/O exception...
		}

		path = Paths.get("/animals/gopher.txt");
		try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("US-ASCII"))) {

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		path = Paths.get("/animals/gorilla.txt");
		List<String> data = new ArrayList();
		try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-16"))) {
			writer.write("Hello World");
		} catch (IOException e) {
			// Handle file I/O exception...
		}

		path = Paths.get("/fish/sharks.log");
		try {
			final List<String> lines = Files.readAllLines(path);
			for (String line : lines) {
				System.out.println(line);
			}
		} catch (IOException e) {
			// Handle file I/O exception...
		}

		try {
			path = Paths.get("/rabbit/food.jpg");
			System.out.println(Files.getLastModifiedTime(path).toMillis());

			Files.setLastModifiedTime(path, FileTime.fromMillis(System.currentTimeMillis()));

			System.out.println(Files.getLastModifiedTime(path).toMillis());
		} catch (IOException e) {
			// Handle file I/O exception...
		}

		/*
		 * uniform resource identifier (URI) is a string of characters that
		 * identify a resource. It begins with a schema that indicates the
		 * resource type, followed by a path value. Examples of schema values
		 * include file://, http://, https://, and ftp://. The java.net.URI
		 * class is used to create and manage URI values. constructor new
		 * URI(String) does throw a checked URISyntaxException Note : URIs must
		 * reference absolute paths at runtime
		 */
		try {
			URI uri = new URI("files//c:/users");// absolute path
			uri = new URI("file:///users");// relative path, throw exception at
											// runtime
			path = Paths.get(uri);
			path = Paths.get(new URI("http://www.wiley.com"));
			path = Paths.get(new URI("ftp://username:password@ftp.the-ftp-server.com"));
			uri = path.toUri();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		/*
		 * FileSystems.getDefault().getPath() is internally used by Paths.get()
		 * FileSystems factory class does give us the ability to connect to a
		 * remote file system. It is useful when we need to construct Path
		 * objects frequently for a remote file system
		 */
		try {
			FileSystem fileSystem = FileSystems.getFileSystem(new URI("http://www.selikoff.net"));
			path = fileSystem.getPath("duck.txt");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

}
