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

  // 1. A method that figures out where to insert a new node, i.e.,
  //    a method that tells you which rights and lefts to follow in
  //    in order to find the next available place for a node.

  // 2. public void sink (Node d)
  //    a method that will sink new info down to a node where it
  //    is bigger than its children but smaller than its parent

  // 3. public void swim (Node d)
  //    a method that will swim info up from down to a node where it
  //    smaller than its parent and bigger than its children


  // Freebie helper method you can call in toString().
  // You do not *need* to use this method, but you can if you like.
  // This method will return a String listing the info in all the nodes
  // in a level in a binary tree, from right to left.
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
  // public T delMin ();

  // Insert a new element.
  // public void insert(T key);

  // Return true if the PQ is empty
  // public boolean isEmpty();

  // Return the size of the PQ.
  // public int size();

  // Return a string showing the PQ in level order, i.e.,
  // containing the info at each node, L to R, from top level to bottom.
  // public String toString();


  ////////////////////////////////////////////////////////////
  // Main method you must write to test out your code above //
  ////////////////////////////////////////////////////////////

  // public static void main (String[] args) {}

}
