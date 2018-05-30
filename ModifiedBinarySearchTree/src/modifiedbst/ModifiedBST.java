package modifiedbst;

import java.util.ArrayList;
import java.util.Scanner;

public class ModifiedBST {
    private ArrayList<Integer> list;

    ModifiedBST() {
        list = new ArrayList<>();
    }

    void add(int item) {
        list.add(item);
    }

    int search(int item) {
        int low = 0, high = list.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;

            // return if item is in the middle
            if (item == list.get(mid)) {
                return mid;
            }

            if((item <= list.get(mid) && list.get(mid) <= list.get(high) && list.get(high) <= list.get(low)) ||
                (list.get(mid) <= list.get(high) && list.get(high) <= list.get(low) && list.get(low) <= item) ||
                (list.get(high) <= list.get(low) && list.get(low) <= item && item <= list.get(mid))) {
                high = mid - 1; // element is found at the left
            } else if ((list.get(high) <= list.get(low) && list.get(low) <= list.get(mid)&& list.get(mid) <= list.get(item)) ||
                (item <= list.get(high) && list.get(high) <= list.get(low) && list.get(low) <= list.get(mid)) ||
                (list.get(mid) <= item && item <= list.get(high) && list.get(high) <= list.get(low))) {
                low = mid + 1; // element is found at the right
            } else if(item < list.get(mid)) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ModifiedBST tree = new ModifiedBST();

        System.out.print("Enter number of elements: ");
        int size = scan.nextInt();

        // ask for input
        for(int i = 0; i < size; i++) {
            System.out.print("Input item #" + (i + 1) + ": ");
            tree.add(scan.nextInt());
        }

        //search for element
        System.out.print("What item would you like to search? ");

        int index = tree.search(scan.nextInt());
        if(index == -1) {
            System.out.println("Item not found.");
        } else {
            System.out.println("Item is found @ index [" + index + "]");
        }
    }
}