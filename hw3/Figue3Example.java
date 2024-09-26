package edu.iastate.cs228.hw3;

public class Figue3Example {

	public static void main(String[] args) {
		StoutList<Character> List = new StoutList<Character>();
		List.add(0, 'A');
		List.add(1, 'B');
		List.add(2, 'C');	//empty
		List.add(4, 'E');
		List.add(3, 'D');
		System.out.println(List.toStringInternal());
		
		//all set up now
		List.add('V');
		System.out.println(List.toStringInternal());
		
		List.add('W');
		System.out.println(List.toStringInternal());
		
		List.add(2, 'X');
		System.out.println(List.toStringInternal());
		
		List.add(2, 'Y');
		System.out.println(List.toStringInternal());
		
		List.add(2, 'Z');
		System.out.println(List.toStringInternal());
		
		//Figure 9
		
		List.remove(9);//remove w
		System.out.println(List.toStringInternal());
		
		List.remove(3);//remove Y
		System.out.println(List.toStringInternal());
		
		List.remove(3);	//remove X
		System.out.println(List.toStringInternal());
		
		List.remove(5); //remove E
		System.out.println(List.toStringInternal());
		
		List.remove(3);	//remove C
		System.out.println(List.toStringInternal());
	}

}
