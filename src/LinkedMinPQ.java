import java.util.*;
public class LinkedMinPQ<T extends Comparable<T>> implements MinPQ<T> {

  ////////////////////////
  // Instance variables //
  ////////////////////////
  Node top;
  int n;


  /////////////////////////////////
  // Node inner class definition //
  /////////////////////////////////

  private class Node {
    T info;
    Node rchild;
    Node lchild;
    Node parent;
  }

  //////////////////////////////////////////////////////
  //     Methods you *might* want to implement.       //
  // There could be others, but these are some ideas. //
  //////////////////////////////////////////////////////

  // Recursively swap the node's info with the smaller of its children until the both children are larger.
  public void sink(Node d) {
    // Finish if there is no left child
    if (d.lchild == null) return;

    Node child;

    // Pick the smaller of the two children. If there is no right child, pick left by default
    if (d.rchild == null) {
      child = d.lchild;
    } else if (d.lchild.info.compareTo(d.rchild.info) > 0) {
      child = d.rchild;
    } else {
      child = d.lchild;
    }

    // Compare the chilren with d, swap infos if needed.
    if (d.info.compareTo(child.info) > 0) {
      T temp = d.info;
      d.info = child.info;
      child.info = temp;
      
      // Recursively sink down if swapped
      sink(child);
    }
  }

  // Recursively swap the node's info with its parent until the parent is smaller.
  public void swim(Node d) {
    Node parent = d.parent;

    // Finish if there is no left child
    if (parent == null) return;

    // Compare the parent with d, swap infos if needed.
    if (d.info.compareTo(parent.info) < 0) {
      T temp = d.info;
      d.info = parent.info;
      parent.info = temp;

      // Recursively swim up if swapped
      swim(parent);
    }
  }
  
  // Retrieve the node at the provided position. Recursively travels
  // up the tree to the top and then back down the correct sides.
  private Node retrieve(int pos) {
    if (pos == 1) return top;
    if (pos % 2 == 0) return retrieve(pos / 2).lchild;
    return retrieve(pos / 2).rchild;
  }


  // Freebie helper method you can call in toString().
  // You do not need to use this method, but you can if you like.
  // This method will return a String listing the info in all the nodes
  // in a level in a binary tree, from left to right.
  // It is recursive: it calls itself in order to get the next level.
  String printThisLevel (Node root ,int level) {
    StringBuilder s = new StringBuilder();
    if (root == null) {
      return s.toString();
    }
    if (level == 1) {
      s.append( root.info.toString());
    } else if (level > 1) {
      s.append( printThisLevel(root.lchild, level-1));
      s.append( printThisLevel(root.rchild, level-1));
    }
    return s.toString();
  }


  /////////////////////////////////////////////////////////
  // Methods you must implement from the PQ interface //
  /////////////////////////////////////////////////////////

  // Remove and return the min (top) element
  public T delMin() {
    // Error if empty
    if (isEmpty()) {
      throw new NoSuchElementException("Stack underflow!");
    }

    T min = top.info;

    // If there's only node left, remove it and set top to null.
    if (n == 1) {
      top = null;
      n--;
    } else {
      Node last = retrieve(n);

      // Set the pointer to the last node from its parent to null (now nothing can access last)
      if (n % 2 == 0) {
        last.parent.lchild = null;
      } else {
        last.parent.rchild = null;
      }

      // Set the top's info to last's
      top.info = last.info;

      n--;
      sink(top);
    }

    return min;
  }

  // Insert a new element.
  public void insert(T key) {
    // Create the new node
    Node node = new Node();
    node.info = key;

    // If the priority queue is empty, set the top to the new node
    if (n == 0) {
      top = node;
      n++;
    } else {
      // Find the parent of what would be this new node's position
      Node parentOfLast = retrieve((n + 1) / 2);

      // Determine which side to go from parentOfLast, and set the correct child
      if ((n + 1) % 2 == 0) {
        parentOfLast.lchild = node;
      } else {
        parentOfLast.rchild = node;
      }
      node.parent = parentOfLast;

      n++;
      swim(node);
    }
  }

  // Return true if the PQ is empty
  public boolean isEmpty() {
    return size() == 0;
  }

  // Return the size of the PQ.
  public int size() {
    return n;
  }

  // Return a string showing the PQ in level order, i.e.,
  // containing the info at each node, L to R, from top level to bottom.
  
  @SuppressWarnings({"unchecked", "rawtypes"})
  public String toString() {
    // Convert the entire priority queue into an array (because that's easier to iterate through)
    if (n == 0) return "[]";
    T[] array = (T[]) new Comparable[n + 1];
    addToArray(array, top, 1);

    // Find the length of the longest item in the array
    int maxLength = 0;
    for (int i = 1; i < n + 1; i++) {
      int length = array[i].toString().length();
      if (length > maxLength) maxLength = length;
    }
    // Make maxLength odd (so each item has a definite center)
    if (maxLength % 2 == 0) maxLength++;

    // Calculate the height of the tree with a log base 2 of n+1
    int height = (int) Math.ceil(Math.log((double) n + 1) / Math.log(2.0));
    // Calculate the number of items in the lowest level (if complete)
    int maxItems = (int) Math.pow(2, height - 1);
    // Calculate the length that completed lowest level would be
    int lineLength = (int) (Math.pow(2, height - 1) * (maxLength + 1)) - 1;
    
    StringBuilder lines = new StringBuilder();

    // Populate the lines
    for (int level = 0; level < height; level++) {
      StringBuilder line = new StringBuilder();
      StringBuilder barLine = new StringBuilder();

      // Calculate the max number of items based on the level
      int numItems = (int) Math.pow(2, level);
      
      // Calculate the width of the margins and create the string
      int marginWidth = ((maxLength + 1) / 2) * (int) (Math.pow(2, height - level - 2));
      StringBuilder margin = new StringBuilder();
      for (int i = 0; i < marginWidth; i++) {
        margin.append(" ");
      }

      // Calculate how long each item should be
      int itemLength = (lineLength - numItems + 1) / numItems - (marginWidth * 2);

      // If we're on the last level, update numItems to the actual number of items.
      // We're doing it down here because the above calculation requires the theoretical
      // number of items in a completed level
      if (level == height - 1) {
        numItems = n - (int) Math.pow(2, level) + 1;
      }

      // Create the left and right slashes
      StringBuilder leftBarBuilder = new StringBuilder();
      StringBuilder rightBarBuilder = new StringBuilder();
      for (int i = 0; i < itemLength; i++) {
        if (i == itemLength / 2) {
          leftBarBuilder.append("/");
          rightBarBuilder.append("\\");
        } else {
          leftBarBuilder.append(" ");
          rightBarBuilder.append(" ");
        }
      }
      String leftBar = leftBarBuilder.toString();
      String rightBar = rightBarBuilder.toString();

      // Populate the items
      for (int i = 0; i < numItems; i++) {
        // Use an underscore to pad up to itemLength (to connect the diagonal bars)
        String pad = "_";
        // If we're on the last level, change pad to a space because there are no diagonal bars
        if (level == height - 1) pad = " ";

        StringBuilder item = new StringBuilder();
        // Set start to the first item on the level
        int start = (int) Math.pow(2, level);
        String value = array[start + i].toString();

        // Create the left and right paddings depending on the length of value and itemLength
        StringBuilder leftBuilder = new StringBuilder();
        for (int j = 0; j < (itemLength - value.length() - 1); j += 2) {
          leftBuilder.append(pad);
        }
        String right = leftBuilder.toString();

        // If we didn't pad all the way to itemLength (because we need an asymmetrical pad),
        // add a pad to the left side
        if (itemLength > leftBuilder.length() * 2 + value.length()) {
          leftBuilder.append(pad);
        }
        String left = leftBuilder.toString();

        // Create the item with its pads
        item.append(left);
        item.append(array[start + i].toString());
        item.append(right);
        
        // Add the item, and its margins, to line (with a space in between each item)
        line.append(margin);
        line.append(item);
        line.append(margin);
        line.append(" ");

        // Add the correct diagonal bar, with its margins and a space in between
        barLine.append(margin);
        if (i % 2 == 0) barLine.append(leftBar);
        else barLine.append(rightBar);
        barLine.append(margin);
        barLine.append(" ");
      }
      
      // Add the bar line before every line except the first
      if (level != 0) lines.append(barLine).append("\n");
      // Add the line to lines
      lines.append(line).append("\n");
    }

    return lines.toString();
  }

  // This method will add the provided node to the array in the defined
  // position, and recursively add its children.
  private void addToArray(T[] array, Node root, int pos) {
    if (root == null) return;

    // Put the current node into the array
    array[pos] = root.info;

    // Recursively put the child nodes into the array
    addToArray(array, root.lchild, pos * 2);
    addToArray(array, root.rchild, pos * 2 + 1);
  }


  ////////////////////////////////////////////////////////////
  // Main method you must write to test out your code above //
  ////////////////////////////////////////////////////////////

  public static void main (String[] args) {
    LinkedMinPQ<Integer> mpd = new LinkedMinPQ<>();
    System.out.println(mpd.isEmpty()); // true
    System.out.println(mpd);
    // []

    mpd.insert(3);
    mpd.insert(5);
    mpd.insert(2);
    mpd.insert(4);
    mpd.insert(6);
    mpd.insert(7);
    mpd.insert(6);
    System.out.println(mpd);
    //    2 
    //  4   3
    // 5 6 7 6

    System.out.println(mpd.delMin()); // 2
    System.out.println(mpd.delMin()); // 3
    System.out.println(mpd);
    //    4
    //  5   6
    // 7 6

    System.out.println(mpd.size()); // 5
    System.out.println(mpd.isEmpty()); // false

    System.out.println(mpd.delMin()); // 4
    System.out.println(mpd.delMin()); // 5
    System.out.println(mpd);
    //  6
    // 7 6

    System.out.println(mpd.delMin()); // 6
    System.out.println(mpd.delMin()); // 6
    System.out.println(mpd.delMin()); // 7

    System.out.println(mpd.isEmpty()); // true
    System.out.println(mpd);
    // []

    for (int i = 0; i < 63; i++) {
      mpd.insert((int) Math.pow(i, 2));
    }
    System.out.println(mpd); // Just showing off my toString function that I spent way too long on

    while (!mpd.isEmpty()) {
      mpd.delMin();
    }

    System.out.println(mpd.isEmpty()); // true
    System.out.println(mpd.delMin()); // Stack underflow!
  }

}
