## Problem Set 5

### Due Thursday, October 18 @ 11:59pm

[Click here for an up-to-date version of this README](https://github.com/BC-CSCI-1102-F18-MW/ps5/README.md) 

---
## SW 2.4.24 Priority queue with explicit links

As we have seen, *complete binary trees* can be efficiently represented in a sequential array by allocating the elements of the tree in level-order in the array. Binary trees can also be represented with a linked data structures, as shown below, in which each node contains some value and has a pointer to the left child and a pointer to the right child.


```java
class Node {
                                           +-----------------+
  private T info;                          |       info      |
  private Node lchild;                     +--------+--------+
  private Node richild;                    | lchild | rchild |
  ...                                      +--------+--------+
}
```

In this problem set, you will implement a priority queue using a **heap-ordered binary tree**, which you will implement with a **triply linked Node structure** instead of an array. You will need **three** links per node: two to traverse down the tree (one for the left child and one for the right child) and one to traverse up the tree to the node's parent. 

### Interface
Please use the following interface for your ADT (included in the `src` directory):

```java
public interface MaxPQ<T extends Comparable<T>> {
  T delMax();
  void insert(T key);
  boolean isEmpty();
  int size();
  String toString();
}
```

Note that the specification of the generic type variable `T` in:

```java
T extends Comparable<T>
```

means that the type variable `T` can be replaced by any type that includes *at least* an `int compareTo(T other)` function. Recall that compareTo returns a positive number when the object calling it is larger than the argument, 0 if they're equal, and a negative number if the object calling is smaller than the argument.

### Getting started
I have included some skeleton code to get you started in the `LinkedMaxPQ.java` file in the `src` directory. *You do not have to use this code*, but you should name your implementation `LinkedMaxPQ.java`.

As with the sequential implementation, the `insert` operation must find the tree node to which a new entry is to be attached, which should be the next available node at the bottom of the tree. It should then "swim" that new entry up to a node where it is bigger than its children but smaller than its parent.

Similarly, the `delMax` operation, after it removes the top (max) node, must replace the top node with the last entry in the complete binary tree, and then "sink" it down to a location where it is bigger than its children but smaller than its parent. 

In the sequential implementation these tree locations were easy to find using the size of the tree to compute the appropriate array index. With this linked implementation, a little more work is required. You can still use the size of the tree, but you will use it to compute the path from the root to the desired node (i.e., whether to go right or left at each node, as you proceed down from the root). Integer division by 2 will come in handy, as will calculating the remainder when divided by 2 by using the modulus operator %.

### Testing your code
As usual, write a few unit tests in order to increase confidence that the code is working correctly.

### Extra credit opportunity
If you implement your method for finding the path from the root to a desired node **recursively** you will get 1 bonus point that you can add to any other problem set score, or 2 bonus points that you can add to Hour Exam 1.
