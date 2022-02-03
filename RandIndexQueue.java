import java.util.Random;

import java.util.Random;

public class RandIndexQueue<T> implements MyQ<T>, Indexable<T> ,Shufflable {

    private int moves;
    private T[] queue;
    private int size;
    private int front;
    private int rear;

    public RandIndexQueue(int sz){
        @SuppressWarnings("unchecked")
        T[] temp= (T[]) new Object[sz];
        queue = temp;
        front = 0;
        rear = 0;
        size = 0;
    }

    /////////////////////////////////////////////////////////
    ///QueueInterface Methods
    
    /** Adds a new entry to the back of this queue.
    @param newEntry  An object to be added. */
    public void enqueue(T newEntry) {
        if(size == 0) { // Sp. case of empty queue
            front = 0;
            rear = 0;
            queue[0] = newEntry;
        }
        else if (size == queue.length) { //Sp. case - resize the array 
            T[] oldQ = queue;
            int oldSz = oldQ.length;
            int newSz = oldSz * 2;
            @SuppressWarnings("unchecked")
            T[] tempQueue = (T[]) new Object[newSz];
            queue = tempQueue;  
            for (int i = 0 ;i < oldSz;i++) {
                queue[i] = oldQ[front];
                front = (front + 1) % oldSz; //The front of the doubled queue is 0 and all objects come after it 
            }
            front = 0;
            rear = oldSz;
            queue[rear] = newEntry;
        }
        else {
            rear = (rear + 1) % queue.length; //In all cases, enqueue at the end of the array. 
            queue[rear] = newEntry;
        }
            moves++;
            size++;
    }
    
    /** Removes and returns the entry at the front of this queue.
    @return  The object at the front of the queue. 
    @throws  EmptyQueueException if the queue is empty before the operation. */
    public T dequeue() {
        if(isEmpty()) { // empty case
            throw new EmptyQueueException();
        }
        else { //normal case
            T frontItem = queue[front];
            queue[front] = null;
            front = (front + 1) % queue.length; 
            size--;
            moves++;
            
            return frontItem;
        }
    }
    
    /**  Retrieves the entry at the front of this queue.
    @return  The object at the front of the queue.
    @throws  EmptyQueueException if the queue is empty. */
    public T getFront() {
        if(isEmpty())
            throw new EmptyQueueException();
        else
            return queue[front];
    }

    /** Detects whether this queue is empty.
    @return  True if the queue is empty, or false otherwise. */
    public boolean isEmpty() {
        if(size == 0) {
        	return true;
        }
        else return false;
    }

    /** Removes all entries from this queue. */
    public void clear() {
        for(int i = 0; i<queue.length; i++) {
        	queue[i] = null;
        }
        size = 0;
    }
    
    //////////////////////////////////////////////////////////////////
    //// MyQ Methods
    
    // Return the number of items currently in the MyQ.  Determining the
 	// length of a queue can sometimes very useful.
    public int size() {
        return size;
    }

    // Return the length of the underlying data structure which is storing the
 	// data.  In an array implementation, this will be the length of the array.
 	// This method would not normally be part of a queue but is included here to
 	// enable testing of your resizing operation.
    public int capacity() {
        return queue.length;
    }

    // Methods to get and set the value for the moves variable.  The idea for
 	// this is the same as shown in Recitation Exercise 2 -- but now instead
 	// of a separate interface these are incorporated into the MyQ<T>
 	// interface.  The value of your moves variable should be updated during
 	// an enqueue() or dequeue() method call.  However, any data movement required
 	// to resize the underlying array should not be counted in the moves.
    public int getMoves() {
        return moves;
    }

    
    public void setMoves(int moves) {
        this.moves=moves;
    }
    
    //////////////////////////////////////////////////////////////////
    /////Indexable Methods
    
    public T get(int i) {
        if(size < i + 1) {
            throw new IndexOutOfBoundsException();
        }
        else {
            return queue[(front + i)% queue.length]; // Logical position = front + i % length
        }
    }

    
    public void set(int i, T item) {
        if(size<i+1) {
            throw new IndexOutOfBoundsException("Index out of bounds"); 
        }
        else{
            queue[(front + i)% queue.length] = item;
        }
    }

    //////////////////////////////////////////////////////////////////
    /////Shufflable Methods - NOT DONE
    
    //TODO: FINISH

    public void shuffle() {
    	Random rand = new Random();
    	Random rand2 = new Random();
    	for(int i=0;i<size()*2;i++) {
    		int firstSwap = (rand.nextInt(size) + front) % queue.length; // Choose a random position within the bounds of the array
    		int secondSwap = (rand2.nextInt(size) + front) % queue.length; // Choose a random position within the bounds of the array
    		
    		T temp = queue[firstSwap];
    		queue[firstSwap] = queue[secondSwap];
    		queue[secondSwap] = temp;
    	}
    }
    
    //////////////////////////////////////////////////////////////////
    /////Functionality Methods

    @SuppressWarnings("unchecked") //safe cast
	public RandIndexQueue(RandIndexQueue<T> old){
        int fr = old.front;
        queue = (T[]) new Object[old.queue.length];
        size = old.size;
        for(int i=0;i<size;i++){
            queue[i] = old.queue[fr];
            fr = (fr + 1)% old.queue.length; //Get every object from old queue and put it into position i of the new queue
        }
        front = 0; //Queue is now beginning from 0 and everything is after that 
        rear = size - 1;
    }


    public boolean equals(RandIndexQueue<T> rhs) {
        int a = front;
        int b = rhs.front;
        if (size != rhs.size) {
            return false;
        }
        else {
            for (int i=0;i<size; i++) {
                if (queue[a].equals(rhs.queue[b])) {
                    a = (a + 1) % queue.length;
                    b = (b + 1) % rhs.queue.length; // Start from the logical front of each queue and check every object till end
                }
                else {
                    return false;
            }
            }
            return true;
        }
    }


    public String toString () {
    	int fr = front;
    	StringBuilder str = new StringBuilder();
    	str.append("Contents: ");
		for(int i = 0; i < size ; i++) {
			str.append(queue[fr].toString());
			str.append(" ");
			fr = (fr + 1) % queue.length;
			}
		return str.toString();
    }
}