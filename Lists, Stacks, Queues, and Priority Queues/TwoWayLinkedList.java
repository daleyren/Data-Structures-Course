/*
 * Class: CMSC214 
 * Instructor: Mr.Estep
 * Description: The program implements MyList and overrides the needed methods to work for a Doubly 
 * Linked List; the program also overrides the iterator methods from the iterator interface.
 * Due: 7/12/2020
 * I pledge that I have completed the programming assignment independently.
   I have not copied the code from a student or any source.
   I have not given my code to any student.
   Print your Name here: Dale Ren
*/

package renD_Project05;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import renD_Project05.TwoWayLinkedList.Node;

public class TwoWayLinkedList<E> implements MyList<E> {
	Node<E> tail;
	Node<E> head;
	int size = 0;

	public static void main(String[] args) {
		TwoWayLinkedList<Integer> testList = new TwoWayLinkedList<Integer>();
		testList.add(100); //Check adding to empty list
		testList.add(97); //Check adding to end
		testList.add(96);
		testList.add(95);
		testList.add(1,99); //Check middle insertion
		testList.add(2,98);

		testList.printList();
		System.out.println("");
		
		testList.remove(0); //Check remove index at beginning
		testList.remove(4);	//Check remove index at end
		testList.remove(2); //Check remove index at middle
		
		testList.set(0, 100); //Check set index at beginning
		testList.set(1, 99); //Check set index at middle
		testList.set(2, 100); //Check set index at end
		
		testList.printList();
		System.out.println("");
		
		System.out.println("Check lastIndexOf Method:");
		System.out.println("Last index of 99: " + testList.lastIndexOf(99)); //Checks last index of 99
		System.out.println("Last index of 100: " + testList.lastIndexOf(100)); //Checks last index of 99

		System.out.println("");
		System.out.println("Check (first)indexOf Method:");
		System.out.println("First index of 99: " + testList.indexOf(99)); //Checks first index of 99
		System.out.println("First index of 100: " + testList.indexOf(100)); //Checks last index of 99

		System.out.println("");
		System.out.println("Check Get Method:");
		System.out.println("Get value at index 0: " + testList.get(0)); //Check get index at beginning
		System.out.println("Get value at index 1: " + testList.get(1)); //Check get index at middle
		System.out.println("Get value at index 2: " + testList.get(2)); //Check get index at end
		
		System.out.println("");
		System.out.println("Check Contains:");
		System.out.println("Does the list contain 97? " + testList.contains(97)); //Check contains object
		System.out.println("Does the list contain 98? " + testList.contains(98)); //Check contains object
		System.out.println("Does the list contain 99? " + testList.contains(99)); //Check contains object
		
		System.out.println("");
		testList.printList();
		System.out.println("");
		testList.clear(); //Clears the DoublyLinkedList 
	
		testList.add(1);
		testList.add(2);
		testList.add(3);
		testList.add(4);
		testList.add(5);
		testList.add(6);
		
		testList.printList(); //Prints out updated List
		System.out.println("");
		
		System.out.println("Test of iterator method: ");
		for(Integer e: testList) { //Test the iterator() method
			System.out.print(e + " ");
		}
		System.out.println("");
		System.out.println("");
		System.out.println("Test of iterator method with index: "); //Test the iterator(int index) method
		Iterator<Integer> iterate = testList.iterator(1); 
		while(iterate.hasNext()) {
			Integer cure = iterate.next();
			System.out.print(cure + " ");
		}
		
	}

	public static class Node<E> {
		E element;
		Node<E> next;
		Node<E> previous;

		public Node(E element) {
			this.element = element;
		}
	}

	@Override
	public void clear() {
		int ogSize = this.size();
		for(int i = 0; i < ogSize; i++) {
			this.remove(0);
		}

	}

	@Override
	public boolean contains(Object o) {
		Node<E> currNode = head;
		for(int i = 0; i < this.size(); i++) {
			if(currNode.element.equals(o)) {
				return true;
			}
			currNode = currNode.next;
		}
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		return new DLL_Iterator<E>(this);
	}
	
	public Iterator<E> iterator(int index) {
		return new DLL_Iterator<E>(this, index);
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public void add(int index, E e) {
		if(size == 0) {
			Node<E> newNode = new Node<E>(e);
			head = newNode;
			tail = newNode;
			size += 1;
			return;
		}
		
		if(index == 0) {
			Node<E> newNode = new Node<E>(e);
			newNode.next = head;
			head.previous = newNode;
			head = newNode;
		}
		else if(index == size) {
			Node<E> newNode = new Node<E>(e);
			newNode.previous = tail;
			tail.next = newNode;
			tail = newNode;
		}
		else { //Middle Cases
			Node<E> newNode = new Node<E>(e);
			Node<E> beforeNode = head; //Node at index-1
			Node<E> afterNode; //Node at index
			for(int i = 0; i < index-1; i++) {
				beforeNode = beforeNode.next;
			}
			afterNode = beforeNode.next;
			//Swapping pointers for beforeNode
			beforeNode.next = newNode;
			//Swapping pointers for afterNode
			afterNode.previous = newNode;
			//Setting pointers for newNode
			newNode.next = afterNode;
			newNode.previous = beforeNode;
		}
		size += 1;
	}

	@Override
	public E get(int index) {
		Node<E> currNode = head;
		for(int i = 0; i < index; i++) {
			currNode = currNode.next;
		}
		return currNode.element;
	}

	@Override
	public int indexOf(Object e) {
		if(this.size() == 0) return -1;
		Node<E> currNode = head;
		for(int i = 0; i < this.size(); i++) {
			if(currNode.element == e) {
				return i;
			}
			currNode = currNode.next;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(E e) {
		if(this.size() == 0) return -1;
		Node<E> currNode = tail;
		for(int i = 0; i < this.size(); i++) {
			if(currNode.element == e) {
				return this.size()-i-1;
			}
			currNode = currNode.previous;
		}
		return -1;
	}

	@Override
	public E remove(int index) {
		//Edge Cases
		if(size == 0) {
			return null;
		}
		else if(size == 1) {
			Node<E> onlyNode = head;
			head = null;
			tail = null;
			size -= 1;
			return onlyNode.element;
		}
		
		//Removing Head and Tail
		E oldElement;
		if(index == 0) {
			Node<E> newHead = head.next;
			oldElement = head.element;
			newHead.previous = null;
			head = newHead;
		}
		else if(index == size-1) {
			Node<E> newTail = tail.previous;
			oldElement = tail.element;
			newTail.next = null;
			tail = newTail;
		}
		else { //Middle Cases
			Node<E> removeNode;
			Node<E> beforeNode = head; //Node at index-1
			Node<E> afterNode; //Node at index+1
			for(int i = 0; i < index-1; i++) {
				beforeNode = beforeNode.next;
			}
			removeNode = beforeNode.next;
			afterNode = beforeNode.next.next;
			oldElement = removeNode.element;
			//Swapping pointers for beforeNode
			beforeNode.next = afterNode;
			//Swapping pointers for afterNode
			afterNode.previous = beforeNode;
		}
		size -= 1;
		return oldElement;
	}

	@Override
	public E set(int index, E e) {
		Node<E> currNode = head;
		for(int i = 0; i < index; i++) {
			currNode = currNode.next;
		}
		currNode.element = e;
		return e;
	}
	
	public void printList() {
		Node<E> currNode = head;
		System.out.print("Current List: ");
		for(int i = 0; i < this.size(); i++) {
			System.out.print(currNode.element + "  ");
			currNode = currNode.next;
		}
		System.out.println("");
	}
}
class DLL_Iterator<E> implements Iterator<E> { 
    Node<E> currNode;
    // constructor 
    DLL_Iterator(TwoWayLinkedList list) { 
        currNode = list.head;
    } 
    
    DLL_Iterator(TwoWayLinkedList list, int index) { 
    	currNode = list.head;
    	for(int i = 0; i < index; i++) {
    		currNode = currNode.next;
    	}
    } 
    
    // Checks if the next element exists 
    public boolean hasNext() { 
    	if(currNode == null) {
    		return false;
    	}
    	return true;
    } 
      
    // moves the cursor/iterator to next element 
    public E next() {
    	E oldElement = currNode.element;
    	currNode = currNode.next;
		return oldElement; 
    } 
} 


