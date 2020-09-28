import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;


/**
 * Definition for a binary tree node.
 */
class TreeNode {
   
    // **** class members ****
    int         val;
    TreeNode    left;
    TreeNode    right;
   
    // **** constructors ****
    TreeNode() {}
   
    TreeNode(int val) { this.val = val; }
   
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
 
    // **** ****
    @Override
    public String toString() {
        return "" + this.val;
    }
}


/**
 * 617. Merge Two Binary Trees
 * https://leetcode.com/problems/merge-two-binary-trees/
 */
public class Solution {


    /**
     * Enumerate which child in the node at the head of the queue 
     * (see populateTree function) should be updated.
     */
    enum Child {
        LEFT,
        RIGHT
    }
    
    
    // **** child turn to insert on node at head of queue ****
    static Child  insertChild = null;           // Child.LEFT;


    /**
     * Generate the number of nodes in th eBT at the specified level.
     */
    static int nodesInLevel(int level) {
        if (level < 1)
            return 0;
        else
            return (int)Math.pow(2.0, level - 1);
    }
    

    /**
     * This function inserts the next value into the specified BT.
     * This function is called repeatedly from the populateTree method.
     * This function supports 'null' values.
     */
    static TreeNode insertValue(TreeNode root, String strVal, Queue<TreeNode> q) {
    
        // **** node to add to the BST in this pass ****
        TreeNode node = null;
    
        // **** create a node (if needed) ****
        if (!strVal.equals("null"))
            node = new TreeNode(Integer.parseInt(strVal));
    
        // **** check is the BST is empty (this becomes the root node) ****
        if (root == null)
            root = node;
    
        // **** add node to left child (if possible) ****
        else if (insertChild == Child.LEFT) {
        
            // **** add this node as the left child ****
            if (node != null)
                q.peek().left = node; 
            
            // **** for next pass ****
            insertChild = Child.RIGHT;
        }
    
        // **** add node to right child (if possible) ****
        else if (insertChild == Child.RIGHT) {
        
            // **** add this node as a right child ****
            if (node != null)
                q.peek().right = node;
    
            // **** remove node from queue ****
            q.remove();
    
            // **** for next pass ****
            insertChild = Child.LEFT;
        }
    
        // **** add this node to the queue (if NOT null) ****
        if (node != null)
            q.add(node);
        
        // **** return the root of the binary tree ****
        return root;
    }


    /**
     * Populate the specified level in the specified binary tree.
     */
    static TreeNode populateBTLevel(TreeNode root, int level, String[] subArr, Queue<TreeNode> q) {
    
        // **** populate binary tree root (if needed) ****
        if (root == null) {
            root = new TreeNode(Integer.parseInt(subArr[0]));
            q.add(root);
            return root;
        }
    
        // **** ****
        TreeNode child = null;
    
        // **** traverse the sub array of node values ****
        for (int i = 0; (i < subArr.length) && (subArr[i] != null); i++) {
    
            // **** child node ****
            child = null;
    
            // **** create and attach child node (if needed) ****
            if (!subArr[i].equals("null"))
                child = new TreeNode(Integer.parseInt(subArr[i]));
    
            // **** add child to the queue ****
            q.add(child);
    
            // **** attach child node (if NOT null) ****
            if (child != null) {
                if (insertChild == Child.LEFT)
                    q.peek().left = child;
                else
                    q.peek().right = child;
            }
    
            // **** remove node from the queue (if needed) ****
            if (insertChild == Child.RIGHT) {    
                q.remove();
            }
    
            // **** toggle insert for next child ****
            insertChild = (insertChild == Child.LEFT) ? Child.RIGHT : Child.LEFT;
        }
    
        // **** return root of binary tree ****
        return root;
    }

    /**
     * Populate a binary tree using the specified array of integer and null values.
     */
    static TreeNode populateBT(String[] arr) {
    
        // **** auxiliary queue ****
        Queue<TreeNode> q = new LinkedList<TreeNode>();
    
        // **** ****
        TreeNode root = null;

        // **** start with the left child ****
        insertChild = Child.LEFT;
    
        // **** begin and end of substring to process ****
        int b   = 0;
        int e   = 0;
    
        // **** loop once per binary tree level ****
        for (int level = 1; b < arr.length; level++) {
    
            // **** count of nodes at this level ****
            int count = nodesInLevel(level);
    
            // **** compute e ****
            e = b + count;
    
            // **** generate sub array of strings ****
            String[] subArr = Arrays.copyOfRange(arr, b, e);
    
            // **** populate the specified level in the binary tree ****
            root = populateBTLevel(root, level, subArr, q);
    
            // **** update b ****
            b = e;
        }
    
        // **** return populated binary tree ****
        return root;
    }


    /**
    * This method implements a breadth-first search traversal of a binary tree.
    * This method is iterative.
    * It displays all nodes at each level on a separate line.
    */
    static String bfsTraversal(TreeNode root) {
    
        // **** to generate string ****
        StringBuilder sb = new StringBuilder();
    
        // **** define the current and next queues ****
        List<TreeNode> currentQ = new LinkedList<TreeNode>();
        List<TreeNode> nextQ    = new LinkedList<TreeNode>();
    
        // **** add the root node to the current queue ****
        currentQ.add(root);
    
        // **** loop while the current queue has entries ****
        while (!currentQ.isEmpty()) {
    
            // **** remove the next node from the current queue ****
            TreeNode n = currentQ.remove(0);
    
            // **** display the node value ****
            if (n != null)
                sb.append(n.toString() + " ");
            else
                sb.append("null ");
    
            // **** add left and right children to the next queue ****
            if (n != null) {
                if (n.left != null)
                    nextQ.add(n.left);
                else
                    nextQ.add(null);
    
                if (n.right != null)
                    nextQ.add(n.right);
                else
                    nextQ.add(null);
            }
    
            // **** check if the current queue is empty (reached end of level) ****
            if (currentQ.isEmpty()) {
                
                // **** end of current level ****
                sb.append("\n");
    
                // **** check if we have all null nodes in the next queue ****
                boolean allNulls = true;
                for (TreeNode t : nextQ) {
                    if (t != null) {
                        allNulls = false;
                        break;
                    }
                }
    
                // **** point the current to the next queue ****
                currentQ = nextQ;
    
                // **** clear the next queue ****
                nextQ = new LinkedList<TreeNode>();
    
                // **** clear the current queue (all null nodes) ****
                if (allNulls)
                    currentQ = new LinkedList<TreeNode>();
            }
        }
    
        // **** return a string representation of the BT ****
        return sb.toString();
    }


    /**
     * Merge two binary trees.
     * Use an in-order traversal model.
     * Recursive method.
     * Returns a new merged binaty tree.
     * Runtime: 0 ms, faster than 100.00% of Java online submissions.
     * Memory Usage: 39 MB, less than 99.82% of Java online submissions.
     * Runtime O(n)
     */
    static TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
        
        // **** base case(s) ****
        if ((t1 == null) && (t2 == null)) {

            // ???? ????
            System.out.println("mergeTrees <<< m.val: null");

            // **** both nodes are null ****
            return null;
        }

        if (t1 == null) {

            // ???? ????
            System.out.println("mergeTrees <<< m.val: " + t2.val);

            // **** use node from t2 ****
            return t2;
        }

        if (t2 == null) {

            // ???? ????
            System.out.println("mergeTrees <<< m.val: " + t1.val);

            // **** use node from t1 ****
            return t1;
        }

        // *** both nodes are available (sum their values) ****
        TreeNode m = new TreeNode(t1.val + t2.val);

        // ???? ????
        System.out.println("mergeTrees <<< m.val: " + m.val);

        // **** traverse left children ****
        m.left = mergeTrees(t1.left, t2.left);

        // **** traverse right children ****
        m.right = mergeTrees(t1.right, t2.right);

        // **** returned merged tree ****
        return m;
    }


    /**
     * Test scaffolding
     */
    public static void main(String[] args) {
        
        // **** open scanner ****
        Scanner sc = new Scanner(System.in);

        // **** read data for tree 1 ****
        String[] arr1 = sc.nextLine().split(",");

        // **** populate tree 1 ****
        TreeNode t1 = populateBT(arr1);

        // **** done with this array ****
        arr1 = null;

        // ???? BFS traversal of t1 to check if all is well so far ????
        System.out.println("main <<< t1: ");
        System.out.print(bfsTraversal(t1) + "\n");

        // **** read data for tree 2 ****
        String[] arr2 = sc.nextLine().split(",");

        // **** close scanner ****
        sc.close();

        // **** populate tree 2 ****
        TreeNode t2 = populateBT(arr2);

        // **** done with this array ****
        arr2 = null;

        // ???? BFS traversal of t1 to check if all is well so far ????
        System.out.println("main <<< t2: ");
        System.out.print(bfsTraversal(t2) + "\n");

        // **** merge treee****
        TreeNode m = mergeTrees(t1, t2);

        // ???? BFS traversal of merged tree to check if all is well ????
        System.out.println("main <<< m: ");
        System.out.print(bfsTraversal(m) + "\n");

       // ???? ????
       System.out.println("main <<< t1: ");
       System.out.print(bfsTraversal(t1) + "\n");
    }

}