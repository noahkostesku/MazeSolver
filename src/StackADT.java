
public interface StackADT<T> {

  public void push (T dataItem);

  public T pop() throws StackException;

  public T peek() throws StackException;

  public boolean isEmpty();

  public int size();

  public void clear();
  
  public String toString();
}
