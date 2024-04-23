package sorting;

import bridges.base.LineChart;
import bridges.benchmark.SortingBenchmark;
import bridges.connect.Bridges;
import bridges.validation.RateLimitException;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import credentials.*;

public class sortingbenchmark {

  // NOTE: The following sorting methods use the Java "Consumer"
  // model. For our purposes, it's sufficient to know that "arr"
  // is an input array of ints to be sorted, and the method should
  // ensure that when it finishes execution, "arr" is sorted in 
  // increasing order, with its minimum element at index 0.

  static Consumer <int[]> selectionSort = arr -> {
    //TODO: Delete the following line and implement 
    // selection sort on the input array instead.
	  int i=0;
	  int j=0;
	  int indexSmall=0;
	  int temp=0; // temporary variable for swap
	  
	  for (i=0; i<arr.length; i++) {
		  // find index of smallest remaining element
		  indexSmall=i;
		  for (j=i+1; j<arr.length; j++) {
			if (arr[j] < arr[indexSmall]) {
				indexSmall = j;
			}
		  }
		  // swap numbers[i] & numbers[smallest index]
		  temp = arr[i];
		  arr[i] = arr[indexSmall];
		  arr[indexSmall] = temp;
	  }
  };

  private static void merge(int[] arr, int i, int j, int k) {
	  int mergedSize = k - i + 1; 	// size of merged partition
	  int mergePos = 0;				// position to insert merged number
	  int leftPos = i;				// position of elements in left partition
	  int rightPos = j+1;			// position of elements in right partition
	  int [] mergedNumbers = new int[mergedSize];	// dynamically allocates temp array for merged numbers
	  
	  // add smallest element from left or right partition to merged numbers
	  while (leftPos <= j && rightPos <= k) {
		  if (arr[leftPos] <= arr[rightPos]) {
			  mergedNumbers[mergePos] = arr[leftPos];
			  leftPos++;
		  }
		  else {
			  mergedNumbers[mergePos] = arr[rightPos];
			  rightPos++;
		  }
		  mergePos++;
	  }
	  
	  // if left partition is not empty, add remaining elements to merged numbers
	  while (leftPos <= j) {
		  mergedNumbers[mergePos] = arr[leftPos];
		  leftPos++;
		  mergePos++;
	  }
	// if right partition is not empty, add remaining elements to merged numbers
	  while (rightPos <= k) {
		  mergedNumbers[mergePos] = arr[rightPos];
		  rightPos++;
		  mergePos++;
	  }
	  
	  // copy merge number back to arr
	  for (mergePos = 0; mergePos < mergedSize; mergePos++) {
		  arr[i + mergePos] = mergedNumbers[mergePos];
	  }
		 
  }  
  
  private static void mergeSortAlg(int[] arr, int i, int k) {
	  if (i<k) {
	    int j = (i+k) / 2; //find the midpoint of in the partition
	    	// recursively sort left & right partitions
	    mergeSortAlg(arr, i, j);
	    mergeSortAlg(arr, j+1, k);
	    // merge left & right partition in sorted order
	    merge(arr, i, j, k);
	  }
  }  
  
  static Consumer <int[]> mergeSort = arr -> {
    //TODO: Delete the following line and implement 
    // merge sort on the input array instead. 
	  if (arr.length > 1) {
		  mergeSortAlg(arr,0,arr.length - 1);
	  }
  };
  
  private static void maxHeapPercolateDown(int nodeIndex, int[] heapArr, int arraySize) {
	  int childIndex = (2 * nodeIndex) + 1;
	  int value = heapArr[nodeIndex];
	  
	  while (childIndex < arraySize) {
		  // find the max among the node and it's children
		  int maxValue = value;
		  int maxIndex = -1;
		  for (int i=0; i<2 && (i+childIndex) < arraySize; i++) {
			  if (heapArr[i+childIndex] > maxValue) {
				  maxValue = heapArr[i+childIndex];
				  maxIndex = i + childIndex;
			  }
		  }
		  if (maxValue == value) {return;}
		  else {
			  int temp = heapArr[nodeIndex];
			  heapArr[nodeIndex] = heapArr[maxIndex];
			  heapArr[maxIndex] = temp;
			  nodeIndex = maxIndex;
			  childIndex = (2 * nodeIndex) +1;
		  }
	  }
  }
  
  private static void heapSortAlg(int [] arr, int arrSize) {
	  //heapify numbers array
	  for (int i = (arr.length/2)-1; i>=0; i--) {
		  maxHeapPercolateDown(i, arr, arr.length);
	  }
	  for (int j = arr.length-1; j>0; j--) {
		  int temp = arr[0];
		  arr[0] = arr[j];
		  arr[j] = temp;
		  maxHeapPercolateDown(0,arr,j);
	  }
  }

  static Consumer <int[]> heapSort = arr -> {
    //TODO: Delete the following line and implement 
    // heap sort on the input array instead.
    heapSortAlg(arr, arr.length);

  };
  
  private static int radixGetLength(int value) {
	  if (value == 0) {return 1;}
	  int digits = 0;
	  while (value != 0) {
		  digits++;
		  value = value / 10;
	  }
	  return digits;
  }
  private static int radixGetMaxLength(int[] arr, int arrSize) {
	  // returns max length, in # of digits, out of all elements in array
	  int maxDig = 0;
	  for (int i=0; i<arrSize; i++) {
		  int digCount = radixGetLength(arr[i]);
		  if (digCount > maxDig) {
			  maxDig = digCount;
		  }
	  }
	  return maxDig;
  }
  
  private static void arrIndexSwap(int[] arr, int indexOne, int indexTwo) {
	  int temp = arr[indexOne];
	  arr[indexOne] = arr[indexTwo];
	  arr[indexTwo] = temp;
  }
  
  private static void radixSortAlg(int[] arr, int arrSize) {
	  int[][] buckets = new int[10][arrSize];
	  int[] colSize = new int[10];
	  
	  // find max length, in # of digits
	  int maxDig = radixGetMaxLength(arr,arr.length);
	  
	  int powTen = 1;
	  for (int digIndex=0; digIndex < maxDig; digIndex++) {
		  int[] colIndex = new int [10];
		  colSize = new int[10];
		  for (int i=0; i<arr.length; i++) {
			  int bucketIndex = Math.abs(arr[i]/powTen) % 10;
			  int bucketsColIndexVal = colIndex[bucketIndex];
			  buckets[bucketIndex][bucketsColIndexVal] = arr[i];
			  colIndex[bucketIndex] = bucketsColIndexVal + 1;
			  colSize[bucketIndex] = colSize[bucketIndex] + 1;
		  }
		  int arrIndex = 0;
		  for (int j=0; j<10; j++) {
			  for (int k=0; k<colSize[j]; k++) {
				  arr[arrIndex] = buckets[j][k];
				  arrIndex++;
			  }
		  }
		  powTen = powTen * 10;
		  buckets = new int[10][arrSize];
	  }
	  int[] negs = new int[arrSize];
	  int negSize = 0;
	  int[] positives = new int[arrSize];
	  int posSize = 0;
	  
	  // isolate positives and negatives
	  for (int i=0; i<10; i++) {
		  for (int j=0; j<colSize[i]; j++) {
			  if (buckets[i][j] < 0) {
				  negs[negSize] = buckets[i][j];
				  negSize++;
			  }
			  else {
				  positives[posSize] = buckets[i][j];
				  posSize++;
			  }
		  }
	  }
	  // reverse negative array
	  int frontPos = 0;
	  int backPos = negSize - 1;
	  while (frontPos < backPos) {
		  arrIndexSwap(negs,frontPos,backPos);
		  frontPos++;
		  backPos--;
	  }
	  for (int i=0; i<negSize; i++) {
		  arr[i] = negs[i];
	  }
	  for (int i=0; i<posSize; i++) {
		  int pos = negSize + i;
		  arr[pos] = positives[i];
	  }
  }
  
  static Consumer <int[]> radixSort = arr -> {
    //TODO: Delete the following line and implement 
    // radix sort on the input array instead.
    radixSortAlg(arr,arr.length);

  };

  /** Runs a built-in Java sort.
    * @param arr An array of ints to be sorted.
    * @post arr is sorted.
  */
  static Consumer <int[]> javaSort = arr -> {
    // DO NOT MODIFY this code.  It is included for benchmarking.
    Arrays.sort(arr);
  };


  public static void main(String[] args) throws IOException, RateLimitException, InterruptedException {

    Bridges bridges = new Bridges(Assignment.ASSIGNMENT_ID, User.USERNAME, User.APIKEY);

    bridges.setTitle("M6 Lab: Sorting Benchmark");
    bridges.setDescription("Sorting Benchmark Test");

    LineChart plot = new LineChart();
    plot.setTitle("Sort Runtime");
    SortingBenchmark bench = new SortingBenchmark(plot);
    bench.linearRange(100, 20000, 50);
    bench.setTimeCap(1000 * 1); // 1 seconds
    bench.run("Built-in Java sort", javaSort);

    // TODO: uncomment the lines below once sorts are implemented to 
    // generate their running time graphs
     bench.run("Selection sort", selectionSort);
     bench.run("Merge sort", mergeSort);
     bench.run("Heap sort", heapSort);
     bench.run("Radix sort", radixSort);

    bridges.setDataStructure(plot);
    bridges.visualize();

    LineChart plot2 = new LineChart();
    plot2.setTitle("Sort Runtime");
    SortingBenchmark bench2 = new SortingBenchmark(plot2);
    bench2.geometricRange(100, 10000000, 1.5);
    bench2.setTimeCap(1000 * 1); // 1 second
    bench2.run("Built-in Java sort", javaSort);

    // TODO: uncomment the lines below once sorts are implemented
    // generate their running time graphs
     bench2.run("Selection sort", selectionSort);
     bench2.run("Merge sort", mergeSort);
     bench2.run("Heap sort", heapSort);
     bench2.run("Radix sort", radixSort);

    bridges.setDataStructure(plot2);
    bridges.visualize();
  }

}
