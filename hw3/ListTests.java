package edu.iastate.cs228.hw3;

import java.util.ListIterator;

public class ListTests {

	public static void main(String[] args) {
		//Node sendNodes = new Node();
		
		
		
//		//doesnt currently work testing nodes
//		StoutList<Integer> List = new StoutList<Integer>();
//		List.add(3);
//		//should hav size of 4 by default
//		System.out.println("");
//		List.add(4);
//		List.add(5);
//		List.add(6);
//		List.add(7);
//		
//		//works for default size testing for wierd sizes
//		StoutList<Integer> List2 = new StoutList<Integer>(2);
//		List2.add(3);
//		//should hav size of 4 by default
//		System.out.println("");
//		List2.add(4);
//		List2.add(5);
//		List2.add(6);
//		List2.add(7);
//		
//		ListIterator<Integer> iter = List.listIterator();      //should i have to say the type of element int he list?
//		System.out.println(iter.hasNext());	//expected true
//		System.out.println(iter.next());	//3
//		System.out.println(iter.next());	//4
//		System.out.println(iter.next());	//5				//look up what should be happening here
//		System.out.println(iter.next());	//6		//6 is missing	
//		System.out.println(iter.hasNext());		//expected true
//		System.out.println(iter.next());	//7
//		System.out.println(iter.hasNext());		//expected false
//		//System.out.println(iter.next());	//expected exception	//working
//		
		
		//------------------------------------
		
//		StoutList<Integer> List = new StoutList<Integer>();
//		List.add(1);
//		List.add(2);
//		List.add(3);
//		List.add(4);
//		List.add(5);
//		List.add(6);
//		List.add(7);
//		List.add(8);
//		System.out.println(List.toStringInternal());
//		ListIterator<Integer> iter = List.listIterator();
//		iter.next();
//		iter.remove();
//		//iter.remove(); should throw an exception
//		System.out.println(List.toStringInternal());
//		
		//works for the first element
		
		//-----------------------------------
		
		//testing the second element
		
//		StoutList<Integer> List = new StoutList<Integer>();
//		List.add(1);
//		List.add(2);
//		List.add(3);
//		List.add(4);
//		List.add(5);
//		List.add(6);
//		List.add(7);
//		List.add(8);
//		System.out.println(List.toStringInternal());
//		ListIterator<Integer> iter = List.listIterator();
//		iter.next();
//		iter.next();
//		iter.remove();
//		//iter.remove(); should throw an exception
//		System.out.println(List.toStringInternal());
		
		//works for the second element
		
		
		//------------------------------------
		
		//testing the first element in the second node
		
//		StoutList<Integer> List = new StoutList<Integer>();
//		List.add(1);
//		List.add(2);
//		List.add(3);
//		List.add(4);
//		List.add(5);
//		List.add(6);
//		List.add(7);
//		List.add(8);
//		System.out.println(List.toStringInternal());
//		ListIterator<Integer> iter = List.listIterator(0);	//need to test with null elements but works
//		iter.next();
//		iter.next();
//		iter.next();
//		System.out.println(List.toStringInternal(iter));
//		iter.next();
//		System.out.println(List.toStringInternal(iter));
//		iter.remove();		//when removing this the iterator moves wierd
//		System.out.println(List.toStringInternal(iter));
		
		
		
		
		
		
		
//		StoutList<Integer> List = new StoutList<Integer>();
//		List.add(1);
//		List.add(2);
//		List.add(3);
//		List.add(4);
//		List.add(5);
//		System.out.println(List.toStringInternal());
//		List.add(8, 0);
//		System.out.println(List.toStringInternal());
//		List.sortReverse();
//		System.out.println(List.toStringInternal());
		
		
//		
//		StoutList<Character> List = new StoutList<Character>();
//		List.add('Z');
//		List.add('Y');
//		List.add('X');
//		List.add('W');
//		List.add('C');
//		System.out.println(List.toStringInternal());
//		List.sort();
//		System.out.println(List.toStringInternal());
		
		//-------------------------------------
		//previous() tests
		
//		StoutList<Integer> List = new StoutList<Integer>();
//		List.add(1);
//		List.add(2);
//		List.add(3);
//		List.add(4);
//		List.add(5);
//		List.add(6);
//		List.add(7);
//		List.add(8);
//		System.out.println(List.toStringInternal());
//		ListIterator<Integer> iter = List.listIterator(0);	//need to test with null elements but works
//		iter.next();
//		System.out.println(List.toStringInternal(iter));
//		iter.previous();
//		System.out.println(List.toStringInternal(iter));
//		iter.next();
//		System.out.println(List.toStringInternal(iter));
//		iter.next();
//		iter.next();
//		iter.next();
//		System.out.println(List.toStringInternal(iter));
//		iter.previous();
//		System.out.println(List.toStringInternal(iter));
		
		//testing iter.remove
		StoutList<Character> List = new StoutList<Character>();
		List.add(0, 'A');
		List.add(1, 'B');
		List.add(2, 'C');	//empty
		List.add(4, 'E');
		List.add(3, 'D');
		
		//all set up now
		List.add('V');
		List.add('W');
		List.add(2, 'X');
		List.add(2, 'Y');
		List.add(2, 'Z');
		List.remove(9);//remove w
		List.remove(3);//remove Y
		List.remove(3);	//remove X
		List.remove(5); //remove E
		List.remove(3);	//remove C			//count gets messed up at this remove
		System.out.println(List.toStringInternal());
		ListIterator<Character> iter = List.listIterator();
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		iter.remove();
		System.out.println(List.toStringInternal());
		iter.previous();
		iter.add('E');
		System.out.println(List.toStringInternal());
	}

}
