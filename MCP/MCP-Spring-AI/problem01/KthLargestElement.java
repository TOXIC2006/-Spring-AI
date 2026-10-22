import java.util.PriorityQueue;

/**
 * Problem 01: Find the Kth Largest Element in an Array using a Min-Heap.
 *
 * Approach (Min-Heap):
 *   - Read the first k elements into a min-heap.
 *   - For every remaining element:
 *       If the element is greater than the heap's top (current kth largest),
 *       remove the top and add the new element.
 *   - At the end, the top of the heap is the kth largest element.
 *
 * Time Complexity:  O(n log k)
 * Space Complexity: O(k)
 */
public class KthLargestElement {

    /**
     * Finds the kth largest element in the array using a min-heap.
     *
     * @param nums the input array
     * @param k    the rank of the element to find (1-based)
     * @return     the kth largest element
     */
    public static int findKthLargest(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty.");
        }
        if (k <= 0 || k > nums.length) {
            throw