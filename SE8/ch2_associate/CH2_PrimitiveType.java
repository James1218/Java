//package ch2_associate;
//
//public class CH2_PrimitiveType {
//	
//	static boolean b;
//
//	public static void main(String[] args) {
//		
//		
//		int i = 11;
//		float f = 11.1f;
//		short s = 11;
//		byte b = 11;
//		long l = 11;
//		char c = 11;
//		double d = 11.1;
//		f = i; f = 10; d = i;//auto cast int to float/double
//		
//		/* wrong cast example */
//		c = -11; //can't auto cast negative value to char
//		s = i; b = i; b = s; //can't auto cast large range runtime variable to short range runtime variable
//		i = f; i = 11.1f;//can't auto cast float to int
//		f = 11.1; //can't auto cast double to float
//		
//		this.b = true; //can't use this in static method
//		
//
//		
//	}
//	
//	public void method(){
//		this.b = true;
//	}
//
//}
