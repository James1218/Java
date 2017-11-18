package ocp;

public class CH1_Instanceof {

	public static void main(String[] args) {
		HeavyAnimal hippo = new Hippo();
		boolean b6 = hippo instanceof Mother;
		System.out.println(b6);
	}

}

interface Mother {}
class Hippo extends HeavyAnimal { }
class HeavyAnimal { }
class Elephant extends HeavyAnimal { }
class MotherHippo extends Hippo implements Mother { }
