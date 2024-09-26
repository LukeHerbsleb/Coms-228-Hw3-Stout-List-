package edu.iastate.cs228.hw3;

import java.lang.reflect.Array;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import com.sun.tools.sjavac.Util;

/**
 * Implementation of the list interface based on linked nodes that store
 * multiple items per node. Rules for adding and removing elements ensure that
 * each node (except possibly the last one) is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E> {
	/**
	 * Default number of elements that may be stored in each node.
	 */
	private static final int DEFAULT_NODESIZE = 4;

	/**
	 * Number of elements that can be stored in each node.
	 */
	private final int nodeSize;

	/**
	 * Dummy node for head. It should be private but set to public here only for
	 * grading purpose. In practice, you should always make the head of a linked
	 * list a private instance variable.
	 */
	public Node head;

	/**
	 * Dummy node for tail.
	 */
	private Node tail;

	/**
	 * Number of elements in the list.
	 */
	private int size;

	/**
	 * Constructs an empty list with the default node size.
	 */
	public StoutList() {
		this(DEFAULT_NODESIZE);
	}

	/**
	 * Constructs an empty list with the given node size.
	 * 
	 * @param nodeSize number of elements that may be stored in each node, must be
	 *                 an even number
	 */
	public StoutList(int nodeSize) {
		if (nodeSize <= 0 || nodeSize % 2 != 0)
			throw new IllegalArgumentException();

		// dummy nodes
		this.head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		this.nodeSize = nodeSize;// defaults to 0
	}

	/**
	 * Constructor for grading only. Fully implemented.
	 * 
	 * @param head
	 * @param tail
	 * @param nodeSize
	 * @param size
	 */
	public StoutList(Node head, Node tail, int nodeSize, int size) {
		this.head = head;
		this.tail = tail;
		this.nodeSize = nodeSize;
		this.size = size;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean add(E item) {
		// get to the end of the the array and add an item
		Node temp = tail;
		if (temp.previous == head) { // then the list is empty and add the first set of nodes
			Node n = new Node();
			n.previous = head;
			n.next = tail;
			head.next = n;
			tail.previous = n;
		} else if (temp.previous.count == nodeSize) { // if not the first node but the last node is full
			Node n = new Node();
			n.previous = temp.previous;
			n.next = tail;
			temp.previous.next = n;
			tail.previous = n;
		}
		temp.previous.addItem(item);

		size++;
		return true;
	}

	@Override
	public void add(int pos, E item) {
		// getting rules from 5.1
		Node temp = head;
		// check if the list is empty
		if (temp.next == tail) {
			// then create a new node and put the item at offset zero (5.1)
			Node n = new Node();
			n.previous = head;
			n.next = tail;
			head.next = n;
			tail.previous = n;
			n.addItem(0, item);
			size++;
			return;
		}

		// find the correct node (include null elements)
		temp = temp.next;
		while (pos >= nodeSize) {
			temp = temp.next;
			pos -= nodeSize;
		}
		// pos is now the datapoint in the node that needs to be changed(or "offset")

		// if the offset is zero two things can happen
		if (pos % nodeSize == 0) { // checking for an offset of zero(dont need to check if its the first element
									// because of above)
			// if temps previous node is not full then put it in there
			if (temp.previous.data[nodeSize - 1] == null) {
				// move back a node and add the item to that
				temp = temp.previous;
				temp.addItem(item);
				size++;
				return;
			} else if (temp == tail) { // create a new node and put the item at offset 0
				Node n = new Node();
				n.previous = tail.previous;
				tail.previous = n;
				n.previous.next = n;
				n.next = tail;
				n.addItem(0, item);
				size++;
				return;
			}
		}
		// otherwise if there is space in node n put the item at offset and move over
		// other items
		if (temp.data[nodeSize - 1] == null) {
			temp.addItem(pos, item);
			size++;
			return;
		}
		// otherwise preform a split operation

		// first create a new node
		Node n = new Node();
		
		
		n.previous = temp;
		n.next = temp.next;
		temp.next = n;
		n.next.previous = n;

		// then copy the second half of temp
		ArrayList<E> arr = new ArrayList<E>();
		for (int i = nodeSize / 2; i < nodeSize; i++) {
			arr.add(temp.data[i]);
			temp.data[i] = null;
			temp.count--;
		}

		// now move that data to the new array
		for (int i = 0; i < arr.size(); i++) {
			n.data[i] = arr.get(i);
			n.count++;
		}

		// finally add item to the correct node
		if (pos <= (nodeSize / 2)) {
			temp.addItem(pos, item);
			size++;
			return;
		} else {
			n.addItem(pos - (nodeSize / 2), item);
			size++;
			return;
		}

	}

	@Override
	public E remove(int pos) {
		// following ruels according to 5.2

		// find what node the element is in then use removeItem(offset) to do the rest
		// node go through each element
		Node temp = head;
		temp = temp.next;
		while (pos >= temp.count) {
			pos -= temp.count;
			temp = temp.next;
		}
		// now temp is in the correct node
		// and pos = offset
		E returnData = temp.data[pos];

		// if the node only has one item, delete it
		if (temp.data[1] == null) {
			tail.previous = temp.previous;
			temp.previous.next = tail;
			size--;
			return returnData;
		}
		// otherwise if n is in the last node or if it has more than M/2 elements remove
		if (tail.previous == temp || temp.count > nodeSize / 2) {
			temp.removeItem(pos);
			size--;
			return returnData;
		}
		// otherwise either mini or full merge
		if (temp.next.count > nodeSize / 2) {
			E firstElementofn1 = temp.next.data[0];
			temp.next.removeItem(0);
			temp.removeItem(pos); // this might be done incorrectly directions are unclear what to remove
			temp.addItem(firstElementofn1);
			size--;
			return returnData;
		} else {
			// full merge
			// first copy from n'
			ArrayList<E> arr = new ArrayList<E>();
			for (int i = 0; i < temp.next.count; i++) {
				arr.add(temp.next.data[i]);
				temp.next.data[i] = null;
			}
			// delete n'
			Node n1 = temp.next;
			temp.next = n1.next;
			n1.next.previous = temp;
			// now can remove what needs to be removed
			temp.removeItem(pos);

			// now add those to temp
			for (int i = 0; i < arr.size(); i++) {
				temp.data[i + temp.count] = arr.get(i);
				temp.count++;
			}

			size--;
			return returnData;
		}
	}

	/**
	 * Sort all elements in the stout list in the NON-DECREASING order. You may do
	 * the following. Traverse the list and copy its elements into an array,
	 * deleting every visited node along the way. Then, sort the array by calling
	 * the insertionSort() method. (Note that sorting efficiency is not a concern
	 * for this project.) Finally, copy all elements from the array back to the
	 * stout list, creating new nodes for storage. After sorting, all nodes but
	 * (possibly) the last one must be full of elements.
	 * 
	 * Comparator<E> must have been implemented for calling insertionSort().
	 */
	public void sort() {
		// first transfer all elements into an array ignoring null elemnents
		@SuppressWarnings("unchecked")
		E[] arr = (E[]) new Comparable[this.size];
		Node temp = head.next;

		int j = 0;
		while (temp != tail) {
			for (int i = 0; i < nodeSize; i++) {
				if (temp.data[i] != null) {
					arr[j] = temp.data[i];
					j++;
				}
			}
			temp = temp.next;
		}
		// now can delete all nodes to save sapce
		head.next = tail;
		tail.previous = head;

		// then sort array accoring to my inerstion sort method

		// dont know what to do with this warning
		StoutList.Comparator<? super E> comp = new StoutList.Comparator();
		insertionSort(arr, comp);

		// then transfer the sorted data into a stout list

		int i = 0;
		Node prevNode = head;
		while (arr[arr.length - 1] != null) {
			Node n = new Node();
			n.previous = prevNode;
			prevNode.next = n;
			tail.previous = n;
			n.next = tail;

			prevNode = prevNode.next;
			// now has a new empty node
			for (int dataPoint = 0; (dataPoint < nodeSize); dataPoint++) {
				prevNode.data[dataPoint] = arr[i];
				arr[i] = null;
				i++;
				if (i == arr.length) {
					break;
				}
			}
		}
	}

	/**
	 * Sort all elements in the stout list in the NON-INCREASING order. Call the
	 * bubbleSort() method. After sorting, all but (possibly) the last nodes must be
	 * filled with elements.
	 * 
	 * Comparable<? super E> must be implemented for calling bubbleSort().
	 */
	public void sortReverse() {
		// first transfer all elements into an array ignoring null elemnents
		// ArrayList<E> arr = new ArrayList<E>(size);
		@SuppressWarnings("unchecked")
		E[] arr = (E[]) new Comparable[this.size];
		Node temp = head.next;

		int j = 0;
		while (temp != tail) {
			for (int i = 0; i < nodeSize; i++) {
				if (temp.data[i] != null) {
					arr[j] = temp.data[i];
					j++;
				}
			}
			temp = temp.next;
		}
		// now can delete all nodes to save sapce
		head.next = tail;
		tail.previous = head;

		// then sort array accoring to my bubble sort method since it sorts in
		// non-increasing order

		bubbleSort(arr);

		// uncomment this section if instead insertion sort is used in order to swap the
		// final result
//		// the twist, swap array from first to last
//		for (int i = 0; i < arr.length / 2; i++) {
//			E tempNum = arr[i];
//			arr[i] = arr[arr.length - 1 - i];
//			arr[arr.length - 1 - i] = tempNum;
//
//		}

		// then transfer the sorted data into a stout list

		int i = 0;
		Node prevNode = head;
		while (arr[arr.length - 1] != null) {
			Node n = new Node();
			n.previous = prevNode;
			prevNode.next = n;
			tail.previous = n;
			n.next = tail;

			prevNode = prevNode.next;
			// now has a new empty node
			for (int dataPoint = 0; (dataPoint < nodeSize); dataPoint++) {
				prevNode.data[dataPoint] = arr[i];
				arr[i] = null;
				i++;
				if (i == arr.length) {
					break;
				}
			}
		}
	}

	@Override
	public Iterator<E> iterator() {
		StoutList<E>.StoutListIterator iter = new StoutListIterator();
		return iter;
	}

	@Override
	public ListIterator<E> listIterator() {
		StoutList<E>.StoutListIterator iter = new StoutListIterator();
		return iter;
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		StoutList<E>.StoutListIterator iter = new StoutListIterator(index);
		return iter;
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes.
	 */
	public String toStringInternal() {
		return toStringInternal(null);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes and the position of the iterator.
	 *
	 * @param iter an iterator for this list
	 */
	public String toStringInternal(ListIterator<E> iter) {
		int count = 0;
		int position = -1;
		if (iter != null) {
			position = iter.nextIndex();
		}

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Node current = head.next;
		while (current != tail) {
			sb.append('(');
			E data = current.data[0];
			if (data == null) {
				sb.append("-");
			} else {
				if (position == count) {
					sb.append("| ");
					position = -1;
				}
				sb.append(data.toString());
				++count;
			}

			for (int i = 1; i < nodeSize; ++i) {
				sb.append(", ");
				data = current.data[i];
				if (data == null) {
					sb.append("-");
				} else {
					if (position == count) {
						sb.append("| ");
						position = -1;
					}
					sb.append(data.toString());
					++count;

					// iterator at end
					if (position == size && count == size) {
						sb.append(" |");
						position = -1;
					}
				}
			}
			sb.append(')');
			current = current.next;
			if (current != tail)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Node type for this list. Each node holds a maximum of nodeSize elements in an
	 * array. Empty slots are null.
	 */
	private class Node {
		/**
		 * Array of actual data elements.
		 */
		// Unchecked warning unavoidable.
		@SuppressWarnings("unchecked") // dont know if im supposed to do this, but it makes me happy :D
		public E[] data = (E[]) new Comparable[nodeSize];

		/**
		 * Link to next node.
		 */
		public Node next;

		/**
		 * Link to previous node;
		 */
		public Node previous;

		/**
		 * Index of the next available offset in this node, also equal to the number of
		 * elements in this node.
		 */
		public int count;

		/**
		 * Adds an item to this node at the first available offset. Precondition: count
		 * < nodeSize
		 * 
		 * @param item element to be added
		 */
		void addItem(E item) {
			if (count >= nodeSize) {
				return;
			}
			data[count++] = item;
			// useful for debugging
			// System.out.println("Added " + item.toString() + " at index " + count + " to
			// node " + Arrays.toString(data));
		}

		/**
		 * Adds an item to this node at the indicated offset, shifting elements to the
		 * right as necessary.
		 * 
		 * Precondition: count < nodeSize
		 * 
		 * @param offset array index at which to put the new element
		 * @param item   element to be added
		 */
		void addItem(int offset, E item) {
			if (count >= nodeSize) {
				return;
			}
			for (int i = count - 1; i >= offset; --i) {
				data[i + 1] = data[i];
			}
			++count;
			data[offset] = item;
			// useful for debugging
//      System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
		}

		/**
		 * Deletes an element from this node at the indicated offset, shifting elements
		 * left as necessary. Precondition: 0 <= offset < count
		 * 
		 * @param offset
		 */
		void removeItem(int offset) {
			// E item = data[offset];
			for (int i = offset + 1; i < nodeSize; ++i) {
				data[i - 1] = data[i];
			}
			data[count - 1] = null;
			--count;
		}
	}

	private class StoutListIterator implements ListIterator<E> {

		/*
		 * the current node that the iterator is at
		 */
		private Node currNode;

		/*
		 * same as "offset"
		 */
		private int currDataInNode;

		/*
		 * the current index the iterator is at(ignores null elements)
		 */
		private int index;

		/*
		 * direction given by next or previous
		 * 
		 * -1 means went backwards last move, 0 means did not move(aslo can't delete), 1
		 * means moved forward
		 */
		private int direction;

		/**
		 * Default constructor
		 */
		public StoutListIterator() {
			index = 0;
			direction = 0;
			currNode = head.next;
			currDataInNode = 0;
		}

		/**
		 * Constructor finds node at a given position.
		 * 
		 * @param pos
		 */
		public StoutListIterator(int pos) {
			index = pos;
			direction = 0;
			// need to find the current node according to the index
			// now fining what node the data is in
			Node temp = head.next;
			while (pos > 0) {
				temp = temp.next;
				pos -= temp.previous.count;
			}
			// likely went past the intended node backtrack according to pos
			if (pos < 0) {
				temp = temp.previous;
				// needs to bring back the iterator to curect number of times
				currDataInNode = pos + temp.count;
			} else {
				currDataInNode = 0;
			}
			// now has the correct node
			currNode = temp;
		}

		@Override
		public boolean hasNext() {
			if (index + 1 < size) {
				return true;
			}
			// else
			return false;
		}

		@Override
		public E next() { // will skip null elements, staight to next element
			boolean foundNext = false;

			if (!hasNext()) {
				throw new NoSuchElementException();
			} else {
				currDataInNode++;
				while (!foundNext) {
					// check to see if needs to move to next node;
					if (currDataInNode >= nodeSize) {
						// then go to then next node + update variables
						currNode = currNode.next;
						currDataInNode = 1;// now at the first element in the next node(adds one later(superscuffed))

					} else if (currNode.data[currDataInNode - 1] != null) {
						foundNext = true;

					}

				}
			}

			direction = 1;
			index++;
			return currNode.data[currDataInNode - 1];

		}

		@Override
		public void remove() {
			// if direction = 0 then dont remove anything
			if (direction == 0) {
				throw new IllegalStateException();
			} else if (direction == 1) { // moving forward as normal
				// so remove whatever is behind
				if (currDataInNode == nodeSize) { // if needs to traverse a node
					currNode.removeItem(currDataInNode - 1);
					direction = 0;
					index--;
					currNode = currNode.previous;
					size--;
					return;
				}
				
				if (currDataInNode == 1) {
					currNode = currNode.previous;
					currNode.removeItem(nodeSize);
					direction = 0;
					index--;
					size--;
					return;
				}

				currNode.removeItem(currDataInNode - 1);
				direction = 0;
				index--;
				size--;
				

			} else { // direction == -1
				currNode.removeItem(currDataInNode);
				direction = 0;
			}
		}

		// Other methods you may want to add or override that could possibly facilitate
		// other operations, for instance, addition, access to the previous element,
		// etc.
		//
		// ...
		//

		// my new methods

		@Override
		public void add(E item) {
			//since null elemets are not counted count the null elements before this and add it to index
			//null elements will not occour in the fist node before an element because everyhting is shifted left
			//so count the number of nulls in previous nodes
			
			
			Node temp = currNode;
			temp = temp.previous;
			int numNulls = 0;
			while (temp != head) {
				numNulls += nodeSize - temp.count;
				temp = temp.previous;
			}
			int pos = index + numNulls;
			
			
			
			// then use stoutlist.add() to take care of everythig  else
			currNode.addItem(pos, item);
			
			//cleaning up
			size++;
			index++;
		}

		@Override
		public boolean hasPrevious() {
			if (index >= 1) {
				return true;
			}
			// else
			return false;
		}

		@Override
		public E previous() { // none of this has been tested + i stopped halfway through
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}
			E returnElement = currNode.data[currDataInNode - 1]; // still actually needs to do stuff
			if (currDataInNode == 0) {
				// backtrack nodes and find the next element thats not null
				currNode = currNode.previous;
				E nextElement = null;
				int j = nodeSize - 1; // since counting starts at zero
				while (nextElement == null) {
					nextElement = currNode.data[j];
					j--;
				}
				j++;
				currDataInNode = j;
			} else {
				int j = currDataInNode;
				while (j != -1) {
					if (currNode.data[j] == null) {
						j--;
						currDataInNode--;

					} else {
						index--;
						break;
					}
				}
			}

			direction = -1;
			return returnElement;
		}

		@Override
		public int nextIndex() { // needs to be implemented for toString with an iterator
			return index; // since counting starts at zero this is correct
		}

		@Override
		public int previousIndex() {
			return index - 1;
		}

		@Override
		public void set(E e) {
			// not implemented

		}
	}

	/**
	 * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING
	 * order.
	 * 
	 * @param arr  array storing elements from the list
	 * @param comp comparator used in sorting
	 */
	private void insertionSort(E[] arr, Comparator<? super E> comp) {
		int n = arr.length;
		for (int i = 1; i < n; ++i) {
			E biggestNum = arr[i];
			int j = i - 1;

			/*
			 * Move elements of arr[0..i-1], that are greater than key, to one position
			 * ahead of their current position
			 */
			while (j >= 0 && (comp.compare(arr[j], biggestNum) > 0)) {
				arr[j + 1] = arr[j];
				j = j - 1;
			}
			arr[j + 1] = biggestNum;
		}
	}

	/**
	 * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a
	 * description of bubble sort please refer to Section 6.1 in the project
	 * description. You must use the compareTo() method from an implementation of
	 * the Comparable interface by the class E or ? super E.
	 * 
	 * @param arr array holding elements from the list
	 */
	private void bubbleSort(E[] arr) {
		Comparator<E> comp = new Comparator<E>();
		for (int i = 0; i < arr.length - 1; i++) {
			for (int j = 0; j < arr.length - 1 - i; j++) {
				if (comp.compare(arr[j], arr[j + 1]) < 0) {
					// swap
					E tempNum = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = tempNum;
				}
			}
		}
	}

	// my private class compare to that is used for sort/ the imput for insertion
	// sort

	// private static class Comparator<T extends Comparable<T>> implements
	// Comparable<Comparator<T>>{
	private static class Comparator<E> implements Comparable<E> {

		public int compare(E x, E y) {
			return ((Comparable) x).compareTo(y);
		}

		@Override
		public int compareTo(E o) {
			return 0; // have to implement compareto but currently does noting
		}
	}

}