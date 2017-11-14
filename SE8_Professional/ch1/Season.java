package ch1;

import javax.net.ssl.SSLEngineResult.Status;

public enum Season {
	
	WINTER("low"), SUMMER;
	
	private String value;

	private Season(String s){
		this.value = s;
	}
	
	private Season(String s, int i, int j){
		
	}
	
	private Season(){
		
	}
	public void print(){
		System.out.println(this.value);
	}
}
