//The Floor is Lava! - A Mine Escape solver Using Stacks, an Interface, and Exceptions
//Code made by Noah Kostesku

public class ArrayStack<T> implements StackADT<T> {

    private T[] array;
    private int top;

    @SuppressWarnings("unchecked")
    public ArrayStack() {
        array = (T[])(new Object[10]);
        top = -1;
    }

    @Override
    public void push(T element) {      
        top++;
        array[top] = element;
        
        //if 75%+ of the array's capacity is being used, increase the array's capacity
        if (top >= (array.length * 0.75)) {
            expandCapacity();
        }
    }

    @Override
    public T pop() throws StackException {
    	
        if (isEmpty()) {
            throw new StackException("Stack is empty");
        }
        T temp = array[top];
        array[top] = null;
        
        
        //if the array's capacity is greater or equal to 20 and the amount of the array that is used is less than or equal to 3/4 of the array's capacity...
        if (array.length >= 20 && (double) size() <= (double) array.length * 0.25) {
            shrinkCapacity();
        }
        top--;
        return temp;
    }

    @Override
    public T peek() throws StackException {
        if (isEmpty()) {
            throw new StackException("Stack is empty");
        }
        return array[top];
    }

    @Override
    public boolean isEmpty() {
        return top == -1;
    }

    @Override
    public int size() {
        return top + 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() { 	
    	//create a new array and reinitialize the top pointer of that of an empty array
        array = (T[])(new Object[10]);
        top = -1;
    }

    public int getCapacity() {
        return array.length;
    }

    public int getTop() {
        return top;
    }

    public String toString() {
        if (isEmpty()) {
            return "Empty stack.";
        }
        String result = "Stack: ";
        boolean comma = false;

        for (int i = top; i >= 0; i--) {
            if (array[i] != null) {
                if (comma) {
                    result += ", ";
                }
                result += array[i];
                comma = true;
            }
        }
        result += ".";
        return result.toString();
    }

    @SuppressWarnings("unchecked")
    private void expandCapacity() {
    	int newArrayCapacity = array.length + 10;
    	T[] newArray = (T[])new Object[newArrayCapacity];
        for (int i = 0; i <= top; i++) {
            newArray[i] = array[i];
        }
        //for garbage collection
        array = newArray;
    }
    @SuppressWarnings("unchecked")
    private void shrinkCapacity() {
    	
    	//if the amount used in the array divided by the array's capacity is less than 0.25 and the array's capacity is greater than or equal to 20...
    	if (((double) size() / (double) array.length) < 0.25 && array.length >= 20) {
    		int newArrayCapacity = array.length - 10;
            T[] newArray = (T[])(new Object[newArrayCapacity]);
            for (int i = 0; i <= top; i++) {
                newArray[i] = array[i];
            }
            array = newArray;
        }
    }
}
