package ocp;

public class CH6_Exception {

	public static void main(String[] args) {

		try (JammedTurkeyCage t = new JammedTurkeyCage()) {
			System.out.println("put turkeys in");
			throw new IllegalStateException("turkeys ran off");
		} catch (IllegalStateException e) {
			e.printStackTrace();
			System.out.println("caught: " + e.getMessage());
			for (Throwable t: e.getSuppressed())
				System.out.println(t.getMessage());
		}
	}

}


class JammedTurkeyCage implements AutoCloseable {
	public void close() throws IllegalStateException {
		throw new IllegalStateException("Cage door does not close");
	}
}
