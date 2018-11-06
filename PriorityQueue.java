
/*
 *      Author: Ramzi Doughan
 *      Date:   10/23/2018
 *   
 *      This is a PriorityQueue that is FIFO 
 *      except when items that are enqueued are of a lower priority,
 *      then those items are put behind items of higher priority
 */

public class PriorityQueue<T extends Comparable<T>> implements PriorityQueueInterface<T>
{
    
    private Node front;
    private Node rear;
    
    // Default Constructor
    public PriorityQueue() {
    
        // Initialize the front and rear of the Queue
        front = new Node();
        rear = new Node();

        front.next = null;
        front.prev = rear;
        rear.next = front;
        rear.prev = null;
    }

    /*
    *   enqueue(item) adds an item to the queue based on its priority
    */

    public void enqueue(T item) {

        // start looking from the rear
        Node current = rear.next;

        // while not the current node is higher priority
        while (current.next != null && item.compareTo(current.item) > 0) {
            // keep advancing toward the front of the list
            current = current.next;
        }
        // initialize the new node
        Node nNode = new Node();
        nNode.item = item;

        // add the new node between the current and previous nodes
        nNode.next = current;
        nNode.prev = current.prev;
        current.prev.next = nNode;
        current.prev = nNode;
    }

    /*
    *   dequeue() removes the front node and returns its item
    */

    public T dequeue() {
        if (isEmpty())
            return null;
        Node temp = front.prev;
        temp.prev.next = front;
        front.prev = temp.prev;
        return temp.item;
    }

    /*
    *   front() returns the item at the front of the queue
    */

    public T front() {
        return front.prev.item;
    }
    
    /*
    *   isEmpty() returns true if the PriorityQueue is empty
    */

    public boolean isEmpty() {
        // return true if the rear is next to the front
        return (rear.next == front) ? true : false;  
    }

    /*
    *   isFull() returns true if the Priority Queue is full
    */

    public boolean isFull(){
        // linked list is not full (until we run out of address space)
        return false;
    }

    /*
    *   toString() returns a string representing the Priority Queue
    *   Rear is printed at the top, feeds to front
    */

    public String toString() {
        String str = "The Queue \n------rear------\n";
        Node ptr = rear.next;

        while (ptr.next != null) {
            str = str + ptr.item.toString();
            str = str + "\n";
            ptr = ptr.next;
        }
        str = str + "------front------";
        return str;
    }

    /*
    *   Node : A simple Node class with generic item T 
    *   with pointers to the next and previous nodes in the list
    */
    
    private class Node {
        public T item;
        public Node prev;
        public Node next;
    }
}