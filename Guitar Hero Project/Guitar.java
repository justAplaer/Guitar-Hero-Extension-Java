// The Guitar interface specifies a set of methods that can be used to play a
// musical instrument that can play various notes.  You can specify exactly
// which note to play (playNote) or you can specify a character that indicates
// which note to play (pluck).  Different guitar objects will have different
// mappings from characters to notes.  There is a hasString method that lets
// you verify that a particular string exists.  It also has methods for getting
// the current sound sample (the sum of all samples from the strings of the
// guitar), to advance the time forward one "tic," and an optional method for
// determining the current time (the number of times tic has been called).  If
// the time method is not implemented, it returns -1.

import java.util.*;
public interface Guitar 
{
    //public void playNote(int pitch);
    public boolean hasString(char key);
    public void pluck(char key);
    public double sample();
    public void tic();
    public int time();
    public void toStringColumns();
    public Map<Character, Integer> printHistogram();
    
    // public void greetings(); - we make greetings a private method in our program
    // because you cannot add private method to interface, we simply remove it
     
    /*
    private String interfaceRemoval(); // compile error
    Why it fails
    
      An abstract method (one with no body, like all the others in your interface) exists so that implementing 
      classes must provide the code. For a class to implement it, it has to be visible to that class.
      A private member is visible only inside the type that declares it. An implementing class is a different type,
      so it could never see or override a private method.
      So "you must implement this" and "nobody else can see this" can't both be true. 
      Java resolves this by saying: abstract methods in an interface cannot be private.
    */
    
    // Keyboard adjustment logic
    
    //public String addKey(Scanner console);
    //public String adjustValue(Scanner console, char letter);
    public String removeKey(Scanner console);
    public String adjustKey(Scanner console);
    public void reset();
    public void deffaultReset();
    
    // Keyboard layouts logic
    public String createNewInterface(Scanner console);
    public String removeInterface(Scanner console);
    public String renameInterface(Scanner console);
    public String switchInterface(Scanner console);
    public void interfaceOverview();
    public String currentInterface();
    
    /*
     ~~~Note on the logic of interfaces~~~
     As can be observed, crearing private methods in interfaces is impossible. Private methods
     are not that easily accessed, so Java cannot really check its existence properly. It can be
     there, but exactly because it is private it won't be seen
     Notably, it is possible to create public and private methods in a class that implements 
     corresponding Interface. The main condition is to expliclty create method in a class
     that are described in an interface, not fully copy it logic
     However, if you create such "extra" method, declare your class using the interface,
     you would not be able to access that method. In your interface, it does not exist, and 
     becayse interface is your box, you cannot pull out something that is not formally there
     
     public interface Fruit
     {
        public void juice();
     }
     
     public class Apple implements Fruit
     {
      public Apple()
      {
         
      }
      public void juice()
      {
         System.out.println("Apple Juice");
      }
      
      public void crucnch()
      {
         System.out.println("CRUNCH!");
      }
     }
    */
    
}
