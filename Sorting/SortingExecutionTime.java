/*
 * Class: CMSC214 
 * Instructor: Mr.Estep
 * Description: The program displays the runtimes in milliseconds for certain sorts of a 
   random array of a given size.
 * Due: 7/12/2020
 * I pledge that I have completed the programming assignment independently.
   I have not copied the code from a student or any source.
   I have not given my code to any student.
   Print your Name here: Dale Ren
*/
package renD_Project05;

import java.util.ArrayList;
import java.util.Arrays;

public class SortingExecutionTime {
	public static int[] randomArray(int length) {
		int[] output = new int[length];
		for(int i = 0; i < length; i++) {
			output[i] = (int)(Math.random() * length+1);
		}
		return output;
	}
	
	public static int selection(int[] array) {
		int temp = 0;
		int minimumIndex = 0;
		long startTime = System.currentTimeMillis();
		
		int length = array.length;
		for(int j = 0; j < length-1; j++) {
			minimumIndex = j;
			for(int i = j+1; i < length; i++) {
				if(array[i] < array[minimumIndex]) {
					minimumIndex = i;
				}
			}
			temp = array[minimumIndex];
			array[minimumIndex] = array[j];
			array[j] = temp;
		}
		long endTime = System.currentTimeMillis();
		return (int)(endTime - startTime);
	}

	public static int bubble(int[] array) {
		int temp;
		boolean sorted = false;
		long startTime = System.currentTimeMillis();
		
		while(sorted == false) {
			sorted = true;
			for(int i = 1; i < array.length; i++) {
				if(array[i] < array[i-1]) {
					temp = array[i];
					array[i] = array[i-1];
					array[i-1] = temp;
					sorted = false;
				}
			}
		}
		long endTime = System.currentTimeMillis();
		return (int)(endTime - startTime);
	}
	
	public static int quick(int[] list) { // Code from Textbook
		long startTime = System.currentTimeMillis();
		QuickSort ob = new QuickSort(); 
		ob.sort(list, 0, list.length - 1);
		long endTime = System.currentTimeMillis();
		return (int) (endTime - startTime);
	}
	
	public static void main(String[] args) {
//		Integer[] what = Arrays.stream(test).boxed().toArray(Integer[]::new);
//		System.out.println(HeapSort.heap(what));
		int[] array1 = randomArray(50000);
		int[] array2 = randomArray(100000);
		int[] array3 = randomArray(150000);
		int[] array4 = randomArray(200000);
		int[] array5 = randomArray(250000);
		int[] array6 = randomArray(300000);
		int[] array7 = randomArray(10000);
		int[] array8 = randomArray(20000);
		int[] array9 = randomArray(30000);
		int[] array10 = randomArray(40000);
		
		//QuickSort had issues when it was directly called into Strint.valueOf() in the table
		int quick1 = quick(array1);
		int quick2 = quick(array2);
		int quick3 = quick(array3);
		int quick4 = quick(array4);
		int quick5 = quick(array5);
		int quick6 = quick(array6);
		int quick7 = quick(array7);
		int quick8 = quick(array8);
		int quick9 = quick(array9);
		int quick10 = quick(array10);
		//Conversion of random int arrays into Integer arrays for heap sort
		Integer[] heap1 = Arrays.stream(array1).boxed().toArray(Integer[]::new);
		Integer[] heap2 = Arrays.stream(array2).boxed().toArray(Integer[]::new);
		Integer[] heap3 = Arrays.stream(array3).boxed().toArray(Integer[]::new);
		Integer[] heap4 = Arrays.stream(array4).boxed().toArray(Integer[]::new);
		Integer[] heap5 = Arrays.stream(array5).boxed().toArray(Integer[]::new);
		Integer[] heap6 = Arrays.stream(array6).boxed().toArray(Integer[]::new);
		Integer[] heap7 = Arrays.stream(array7).boxed().toArray(Integer[]::new);
		Integer[] heap8 = Arrays.stream(array8).boxed().toArray(Integer[]::new);
		Integer[] heap9 = Arrays.stream(array9).boxed().toArray(Integer[]::new);
		Integer[] heap10 = Arrays.stream(array10).boxed().toArray(Integer[]::new);
		
		//Each Row in the table measures the runtimes in milliseconds for certain sorts of a random array of a given size
		final Object[][] table = new String[11][];
		table[0] = new String[] { "Array Size", "Selection Sort", "Radix Sort", "Bubble Sort", "Merge Sort", "Quick Sort", "Heap Sort" };
		table[1] = new String[] { "10,000", String.valueOf(selection(randomArray(10000))), String.valueOf(RadixSort.radix(randomArray(10000))), String.valueOf(bubble(randomArray(10000))), String.valueOf(MergeSort.merge(randomArray(10000))), String.valueOf(quick7), String.valueOf(HeapSort.heap(heap7))};
		table[2] = new String[] { "20,000", String.valueOf(selection(randomArray(20000))), String.valueOf(RadixSort.radix(randomArray(20000))), String.valueOf(bubble(randomArray(20000))), String.valueOf(MergeSort.merge(randomArray(20000))), String.valueOf(quick8), String.valueOf(HeapSort.heap(heap8))};
		table[3] = new String[] { "30,000", String.valueOf(selection(randomArray(30000))), String.valueOf(RadixSort.radix(randomArray(30000))), String.valueOf(bubble(randomArray(30000))), String.valueOf(MergeSort.merge(randomArray(30000))), String.valueOf(quick9), String.valueOf(HeapSort.heap(heap9))};
		table[4] = new String[] { "40,000", String.valueOf(selection(randomArray(40000))), String.valueOf(RadixSort.radix(randomArray(40000))), String.valueOf(bubble(randomArray(40000))), String.valueOf(MergeSort.merge(randomArray(40000))), String.valueOf(quick10), String.valueOf(HeapSort.heap(heap10))};
		table[5] = new String[] { "50,000", String.valueOf(selection(randomArray(50000))), String.valueOf(RadixSort.radix(randomArray(50000))), String.valueOf(bubble(randomArray(50000))), String.valueOf(MergeSort.merge(randomArray(50000))), String.valueOf(quick1), String.valueOf(HeapSort.heap(heap1))};
		table[6] = new String[] { "100,000", String.valueOf(selection(randomArray(100000))), String.valueOf(RadixSort.radix(randomArray(100000))), String.valueOf(bubble(randomArray(100000))), String.valueOf(MergeSort.merge(randomArray(100000))), String.valueOf(quick2), String.valueOf(HeapSort.heap(heap2))};		
		table[7] = new String[] { "150,000", String.valueOf(selection(randomArray(150000))), String.valueOf(RadixSort.radix(randomArray(150000))), String.valueOf(bubble(randomArray(150000))), String.valueOf(MergeSort.merge(randomArray(150000))), String.valueOf(quick3), String.valueOf(HeapSort.heap(heap3))};
		table[8] = new String[] { "200,000", String.valueOf(selection(randomArray(200000))), String.valueOf(RadixSort.radix(randomArray(200000))), String.valueOf(bubble(randomArray(200000))), String.valueOf(MergeSort.merge(randomArray(200000))), String.valueOf(quick4), String.valueOf(HeapSort.heap(heap4))};
		table[9] = new String[] { "250,000", String.valueOf(selection(randomArray(250000))), String.valueOf(RadixSort.radix(randomArray(250000))), String.valueOf(bubble(randomArray(250000))), String.valueOf(MergeSort.merge(randomArray(250000))), String.valueOf(quick5), String.valueOf(HeapSort.heap(heap5))};
		table[10] = new String[] { "300,000", String.valueOf(selection(randomArray(300000))), String.valueOf(RadixSort.radix(randomArray(300000))), String.valueOf(bubble(randomArray(300000))), String.valueOf(MergeSort.merge(randomArray(300000))), String.valueOf(quick6), String.valueOf(HeapSort.heap(heap6))};
		for (final Object[] row : table) {
		    System.out.format("%10s%15s%15s%15s%15s%15s%15s\n", row);
		}
	}
}


