import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Test {

	public static void main(String[] args) {
//		Pencil pencil = new ColorPencil("blue");
//		//System.out.println(pencil.color);
//		List<? super Pencil> list = new ArrayList<>();
//		list.add(new ColorPencil("blue"));
//		list.add(new Pencil(1));
		
		
		Queue<String> queue = new LinkedList<>();
		queue.offer("a");
		queue.offer("b");
		queue.remove();
		
		System.out.println(lengthOfLongestSubstring("abcabcbb"));
	}
	
	public static int lengthOfLongestSubstring(String s) {
	        
	        if (s == null) return 0;
	        if (s.length() <= 1) return s.length();
	        int [] map = new int [256];
	        int start = 0, max = 0;
	        for (int i = 0; i < s.length(); i++){
	            char c = s.charAt(i);
	            if (map[c] == 0){
	                map[c]++;
	                max = max > i - start + 1 ? max : i - start + 1;
	            }
	            else{
	                while (start < i){
	                    if (c != s.charAt(start)){
	                        map[s.charAt(start)]--;
	                        start++;
	                    }
	                }
	            }
	        }
	        return max;
	    }
	

}

class Pencil {
	String test;
	public Pencil(int x) {
		System.out.println("pencil");
	}
}
class ColorPencil extends Pencil {
    String color;
    ColorPencil(String color) {
	    super(1);
	    System.out.println("colorpencil");
	    this.color = color;}
}

class ListNode {
	      int val;
	     ListNode next;
	      ListNode(int x) { val = x; }
	 }

