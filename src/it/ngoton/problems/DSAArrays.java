package it.ngoton.problems;

import java.util.Arrays;
import java.util.HashMap;

public class DSAArrays {
    /**
     * Tìm số bị thiếu trong dãy số từ 1 - N
     * Input: list[] = {1, 2, 4, 6, 3, 7, 8, 10, 5}
     * Output: 9
     * <u>
     *     <li>Tính tổng của N số tự nhiên đầu tiên là Tổng = (1 + N) * N / 2 : (số hạng đầu + số hạng cuối) * số số hạng / 2
     *     <li>Tạo biến sum để lưu trữ tổng các phần tử của mảng.
     *     <li>Lặp lại mảng từ đầu đến cuối.
     *     <li>Cập nhật giá trị của tổng thành tổng = tổng + mảng [i]
     *     <li>In số còn thiếu dưới dạng Tổng – tổng
     * </u>
     */
    static int findMissingNumber(int[] arr, int n) {
        int sum = (1+n)*n/2;
        for (int i = 0; i < n-1; i++) {
            sum -= arr[i];
        }
        return sum;
    }

    /**
     * Tìm số trùng lặp trong mảng
     * Input: list[] = {1, 2, 4, 1}
     * Output: 1
     * <u>
     *     <li>Tạo một mảng tần số có kích thước N và khởi tạo nó bằng 0. (Có thể thay bằng Set)
     *     <li>Duyệt qua mảng.
     *     <li>Nếu tần số của phần tử là 0 thì tăng nó lên 1, nếu khác 0 thì phần tử đó là câu trả lời cần tìm.
     * </u>
     */
    static int findDuplicateNumber(int[] arr) {
        int n = arr.length;
        int[] freq = new int[n];
        for (int i = 0; i < n; i++) {
            if (freq[arr[i]-1] == 1) {
                return arr[i];
            }
            freq[arr[i]-1] = 1;
        }
        return -1;
    }

    /**
     * Tìm số trùng lặp trong dãy số từ 1 - N, gồm n + 1 số. Vì có nhiều hơn n phần tử nên ít nhất 1 số bị lặp lại
     * Input: list[] = {3,1,3,4,2}
     * Output: 3
     * <u>
     *     <li>Xem mảng như một danh sách liên kết ảo: Mỗi phần tử nums[i] là một "con trỏ" trỏ đến nums[nums[i]]
     *     <li>Do có số lặp lại ⇒ sẽ tạo thành một chu trình (cycle).
     *     <li>Floyd’s Tortoise and Hare (thuật toán phát hiện chu trình) sẽ giúp tìm điểm lặp.
     * </u>
     */
    static int findDuplicateFloyd(int[] nums) {
        int tortoise = nums[0];
        int hare = nums[0];

        // Phase 1: Find the intersection point (điểm giao nhau)
        do {
            tortoise = nums[tortoise]; // di chuyển 1 bước mỗi lần
            hare = nums[nums[hare]]; // di chuyển 2 bước mỗi lần
        } while (tortoise != hare);

        // Phase 2: Find the entry point of the cycle (the duplicate) (điểm bắt đầu chu trình)
        tortoise = nums[0]; // đưa 1 con trỏ về đầu mảng
        while (tortoise != hare) {
            // di chuyển từng bước 1 tới điểm bắt đầu chu trình
            tortoise = nums[tortoise];
            hare = nums[hare];
        }
        // Khoảng cách từ head đến entry point = khoảng cách từ intersection đến entry point
        return hare; // The duplicate number
    }

    /**
     * Tìm các cặp số có tổng = k.
     * Input: list[] = {3,1,3,4,2,5,1}
     * Output: 3 3, 1 5, 4 2, 2 4, 5 1
     * <u>
     *     <li>Tạo 1 Map chứa vị trí của các số. (Nếu dùng Arrays.sort sẽ làm thay đổi vị trí.)
     *     <li>Duyệt qua mảng
     *     <li>Kiểm tra tồn tại trong trong Map giá trị k - arr[i] (tức tồn tại số hạng ở vị trí khác)
     * </u>
     */
    static void findPairsEqualsToK(int[] arr, int k) {
        HashMap<Integer, Integer> elementIndexMap = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            elementIndexMap.put(arr[i], i);
        }
        for (int i = 0; i < arr.length; i++) {
            // we have used elementIndexMap.get(X-arr[i])!=i to avoid using same element twice
            if (elementIndexMap.get(k - arr[i]) != null && elementIndexMap.get(k - arr[i]) != i)
            {
                System.out.println(arr[i] + " " + (k - arr[i]));
            }
        }
    }

    /**
     * Xóa các số trùng lặp, thay = 0 vào cuối mảng. Không extra space. (Array được phép sort)
     * Input: list[] = {3,1,3,4,2,5,1}
     * Output: [1,2,3,4,5,0,0]
     * <u>
     *     <li>Sort mảng ban đầu. (Nếu phải giữ nguyên thứ tự thì dùng Set)
     *     <li>Áp dụng two pointers, con trỏ j lưu các giá trị không trùng
     *     <li>Duyệt mảng
     *     <li>Cập nhật lại giá trị của mảng,
     *     nếu giá trị là duy nhất thì thêm tiếp vào vị trí nums[j+1] (j bắt đầu từ 0), thay giá trị nums[i] hiện tại = 0
     * </u>
     * Không extra space, giữ order -> O(n2) : 2 loop
     * Không extra space, k giữ order -> O(nlogn) : Sort array
     * Extra space, giữ order -> O(n) : Set
     */
    static int[] removeDuplicates(int[] nums) {
        Arrays.sort(nums);
        int j = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != nums[j]) {
                j++;
                nums[j] = nums[i]; // Move the unique element to the next position
                nums[i] = 0; // Update the current element to 0
            }
        }
        return nums;
    }

    /**
     * Di chuyển 0 về cuối mảng không làm thay đổi thứ tự. Không extra space.
     * Input: list[] = {3,0,4,2,0,1}
     * Output: [3,4,2,1,0,0]
     * <u>
     *     <li>Dùng 2 con trỏ đọc, ghi
     *     <li>Duyệt mảng với con trỏ đọc
     *     <li>Nếu giá trị khác 0 thì hoán đổi giá trị 2 con trỏ, tăng con trỏ ghi (vì con trỏ ghi đang bị dừng lại tại giá trị 0)
     * </u>
     */
    static int[] moveZeroes(int[] arr) {
        int read = 0;
        int write = 0;
        while (read < arr.length) {
            if (arr[read] != 0) {
                int temp = arr[read];
                arr[read] = arr[write];
                arr[write] = temp;
                write++;
            }
            read++;
        }
        return arr;
    }

    /**
     * Sắp xếp 3 giá trị 0,1,2. Không extra space.
     * Input: list[] = {1,0,1,2,2,1,0}
     * Output: [0,0,1,1,1,2,2]
     * <u>
     *     <li>Dùng 3 con trỏ left, right, i
     *     <li>Duyệt mảng với i <= right
     *     <li>Bên trái left là 0
     *     <li>Bên phải left tới i là 1
     *     <li>Bên phải right là 2
     *     <li>Dùng con trỏ i để duyệt:
     *     <li>Khi arr[i]=0, thì hoán đổi với arr[left], i++, left++ (left dừng lại giữa 0 & 1)
     *     <li>Khi arr[i]=1, thì đi tiếp, i++
     *     <li>Khi arr[i]=2, thì hoán đổi với arr[right], right-- (right dừng giữa 2 & 1)
     * </u>
     */
    static int[] sortThree(int[] arr) {
        int i =0, left =0, right = arr.length-1;
        while (i <= right) {
            if (arr[i] == 0) {
                int temp = arr[left];
                arr[left] = arr[i];
                arr[i] = temp;
                left++;
                i++;
            } else if (arr[i] == 2) {
                int temp = arr[right];
                arr[right] = arr[i];
                arr[i] = temp;
                right--;
            }
            else {
                i++;
            }
        }
        return arr;
    }

    public static void main(String[] args) {
        int[] list = {1, 2, 4, 6, 3, 7, 8, 10, 5};
        System.out.println("Missing: " + findMissingNumber(list, list.length+1));
        int[] list2 = {1,2,4,1};
        System.out.println("Duplicate: " + findDuplicateNumber(list2));
        int[] list3 = {3,1,3,4,2};
        System.out.println("Duplicate Floyd: " + findDuplicateFloyd(list3));
        int[] list4 = {3,1,3,4,2,5,1};
        System.out.println("Find pairs:");
        findPairsEqualsToK(list4, 6);
        int[] list5 = {3,1,3,4,2,5,1};
        System.out.println("Remove duplicates:");
        int[] arr = removeDuplicates(list5);
        for (int i : arr) {
            System.out.print(i);
        }
        System.out.println("\nMove zeroes:");
        int[] list6 = {3,0,4,2,0,1};
        int[] arr2 = moveZeroes(list6);
        for (int i : arr2) {
            System.out.print(i);
        }
        System.out.println("\nSort three:");
        int[] list7 = {1,0,1,2,2,1,0};
        int[] arr3 = sortThree(list7);
        for (int i : arr3) {
            System.out.print(i);
        }
    }
}
