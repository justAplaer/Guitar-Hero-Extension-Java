// The following code is the alternative to the Guitar37 file that provides the necesary flexibility to code a logic
// The GuitarPlus class allows to assign different Hz values to each key, create and delte whole layouts of guitar keyboards
// It uses Map as its main logic to process pressed keys and also store corresponding interfaces in memory

// Add a method that would allow to clear the current interface
import java.util.*;

/**
* @author Tair Moldakhmetov
* @version 1.0
* The following class can be used to mimic real world Guitar behavior
* which includes producing a sound, creating guitar layouts with corresponding frequency in Hz
* assigned to each specific key on the keyboard. Every engaged keys represents a "String" of a 
* guitar. In addition, it provides a logic that can adjust or delete keys of a corresponding
* keyboard layout using Maps and more
*/

public class GuitarPlus implements Guitar
{
   public String keyboard =  "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";  
   private GuitarString[] strings; 
   private int ticAmount = 0; 
   private String currentInterface;
   
   // The following two maps are used to create new maps interfaces
   //private LinkedHashMap<Character, StringPlus> guitarMapHolder = new LinkedHashMap<>();
   //private LinkedHashMap<Character, Integer> countMapHolder = new LinkedHashMap<>();
   
   private Map<String, Map<Character, StringPlus>> interfaceMap = new LinkedHashMap<>(); //This map contains the guitar layouts
   private Map<String, Map<Character, Integer>> interfaceCountMap = new LinkedHashMap<>(); // This map contains the corresponding history to each guitar layout
   
   private Map<Character, StringPlus> guitarMap = new LinkedHashMap<>(); // THis is the map that tracks added key and their Hz value
   private Map<Character, Integer> countMap = new LinkedHashMap<>();
   // Put the layout in the big mao, then use guitarMap as a switch to control everything. Supposedely, when change are applied to
   // GuitarMap, it will be reflected in corresponding layout
   
   // Contructor
        
   // Notice that here we use countMap and guitarMap together with interfaceMap and
   // interfaceCountMap. guitarMap and countMap is what actually is used in all the methods
   // We use currentInterface to track the current interface a user is currently using
   // The reason we want to explicitly use such variables is because in case user deletes all
   // the interfaces, we need to block him of from some methods for logical reason, because 
   // otherwise he would create keys in ghost guitarMap and program would somewhat "run"
   // even though all keyboards layouts are deleted from the guitarInterfaces holder
   // Logically, this should not happen and that is why we engage guitarInterface
   // variable to track the current interface and make it "null" to prevent illogical behavior
   
   
   /**
   * The following method constructs GuitarPlus class with all deffault values in the class
   */
   public GuitarPlus()
   {
      for (int i = 0; i < keyboard.length(); i++)
      {
         countMap.put(keyboard.charAt(i), 0);
         guitarMap.put(keyboard.charAt(i), new StringPlus(440*Math.pow(2, (i-24)/12.0) ));
      }
      
      currentInterface = "Deffault";
      interfaceMap.put("Deffault", guitarMap);
      interfaceCountMap.put("Deffault", countMap);      
   }
   
   /**
   * @param char key - accepts a key and then checks whether this key is inside the current guitar layout
   * @return guitarMap.containsKey(key); - return a true or false based on whether the key is inside the current map or not
   */
   public boolean hasString(char key)
   {
      return guitarMap.containsKey(key);  
   }
   
   /*
    Recall the physical meaning behind the method tic
    We go through each and every guitarString inside the guitarMap and "decay"
    each sound of the String in the guitarMap. Eventually, the ringbuffer of
    corresponding String becomes filled with zeroes and even though we do 
    StdAudio.play(g.sample()); it literally produces no sound - even though technically
    The program tries to do so. That is how tic(here and in StringPlus), pluck, and
    StdAudio.play(g.sample()); work together to mimic sound of a guitarString
   */
   
   /**
   * Method tic mimics the "decay" in the sound of each guitarString in the guitarMap
   */
   public void tic() 
   {
        for (char letter : guitarMap.keySet())
        {
            guitarMap.get(letter).tic();
        }
        
        ticAmount++; // Adding 1 to the variable tracking the amount of times tic() was called
   }
   
   /**
   * @return countMap - returns countMap, which is then printed in main to show
   * how many times each key was pressed - smal summary of the activity
   */
   public Map<Character, Integer> printHistogram()
   {
       return countMap;
   }
    
   /**
   * @return double returnValue - returns a sum of all samples of each key in map ("Guitar String")
   */
   public double sample()
   {
      double returnValue = 0.0;
      
      for (char letter : guitarMap.keySet())
      {
         returnValue += guitarMap.get(letter).sample();
      }
      
      return returnValue;
   }
   
   /**
   * @param char key - the passed key, which represent the guitar String of a real guitar
   * The following method uses "pluck" described in StringPlus to mimic the behavior of a real-world guitar
   * 
   */
   public void pluck(char key)
   { 
      if (guitarMap.containsKey(key)) 
      {
         countMap.put(key, countMap.get(key) + 1); // Adding 1 to the value of key associated with the one just pressed
         guitarMap.get(key).pluck();
      }
   }
   
   /**
   * @return ticAmount - return the amount of times the method tic was called
   */
   public int time() 
   {
       return ticAmount; // Returning the ticAmount value
   }
   
   /**
   * @return guitarMap.toString() - returns a String representation of current guitarMap
   */
   public String toString()
   {
      return guitarMap.toString();
   }
   
   /**
   * The following methods prints out the key with its corresponding frequency in Hz
   */
   public void toStringColumns()
   {
      for (char letter : guitarMap.keySet())
      {
         System.out.println(letter + " = " + guitarMap.get(letter));
      }
   }
   
   // Methods
   
   /**
   * @param Scanner console - accepts a scanner that serves as a communication tool between user and the program
   * @return return String - returns a String that is then processed in the main to properly output the adequate menu
   * The following method provides the necessary logic to add new keys or adjust the existing keys in the 
   * corresponding keyboard layout
   */
   public String adjustKey(Scanner console)
   {
      if (currentInterface.equals("null"))
      {
         System.out.println("ERROR. Your current interface is null. Please create at least one interface layour to access this feature");
         return "Null";
      }
      
      this.greetings();
      
      System.out.println("Here is the current layout of the guitar interface");
      this.toStringColumns();
      String input = "";
      while (true)
      {
         System.out.println("Enter the key you want to add or adjust?");
         input = console.next();
         
         if (input.equalsIgnoreCase("QUIT"))
         {
            return input;
         }
         
         else if (input.equalsIgnoreCase("Return"))
         {
            return "settings";
         }
         
         else if (input.equalsIgnoreCase("Menu"))
         {
            return "`";
         }
         
         char letterInput = input.charAt(0);
         if (input.length() > 1 || letterInput == '`')
         {
            System.out.println("\nERROR. Must enter a valid single character key not equal to '~'. Please try again\n");
         }
         
         else
         {
            input = this.adjustValue(console, letterInput);
         }
         
         if (input.equalsIgnoreCase("QUIT") || input.equalsIgnoreCase("`") || input.equalsIgnoreCase("Settings"))
         {
            return input;
         }
      }
   }
   
   /*
    The following method is a helper to the adjust key method on top
    it is required so that in case user wants to return back to choosing
    the key he wants to modify, the whole method is not stopped after return
    and only the helper method stops
   */
   private String adjustValue(Scanner console, char letter)
   {
      double value = 0;
      while (true)
      {
         System.out.println("\nIn addition to the commands above:");
         System.out.println("Enter 'Return' to go back to the last stage (choosing the key)");
         System.out.println("Enter 'Settings' to go back to the Settings Menu");
         System.out.println("Enter 'Menu' to go back to the Main Menu");
         System.out.println("Which frequency in Hz would you like to assign to '" + letter + "' key?");
         String input = console.next();
         
         /*
            Double.parseDouble(String s) Converts text into a double
            
            double a = Double.parseDouble("66.9");    // a = 66.9
            double b = Double.parseDouble("  440 ");  // b = 440.0 (surrounding spaces are OK)
            double c = Double.parseDouble("hello");   // throws NumberFormatException 
         */
         
         /*
            Double.isFinite(double d)
   
            Returns true if the number is a normal, real value, and false if it is Infinity, -Infinity, or NaN ("not a number").
            
            java
            Double.isFinite(440.0);                       // true
            Double.isFinite(Double.POSITIVE_INFINITY);    // false
            Double.isFinite(0.0 / 0.0);                   // false (that's NaN)
            
            Double.isFinite("66.9");   // won't compile: String can't be converted to double
         */
         
         try
         {
            value = Double.parseDouble(input);
         }
         
         // For the settings in particular does not reallt matter what is returned - we just want a value 
         // that would be caught by the if statement in our "Mother" method AFTER we execute the helper method. 
         // Then we return a value in our MOTHER method since it is caught there (in this specific case, we choose
         // "Settings" as our key word). After the return happens in MOTHER method, it sends us back to the 
         // settings in the main method and because "settings" are not the key word in the main method
         // if statements, it will actually return a user to the main method properly
         
         catch (NumberFormatException e)
         {
            if (input.equalsIgnoreCase("QUIT") || input.equalsIgnoreCase("Return") || input.equalsIgnoreCase("Settings"))
            {
               return input;
            }
            
            else if (input.equalsIgnoreCase("Menu"))
            {
               return "`";
            }
         }
         
         // the 20000 limit is needed so that length of ringbuffer for corresponding guitarString is at least 2
         // Math.round(41000/20000) = 2. 41000 is our sample Hz
         
         if (value > 0 && value <= 20000)
         {
            guitarMap.put(letter, new StringPlus(value));
            countMap.put(letter, 0);
            return "Return";
         }
         
         else
         {
            System.out.println("\nERROR. Ivalid Input. Input must be an existing command or value greater than 0 and less than 20000. Please try again\n");
         }
      }    
   }
   
   /**
   * @param Scanner console - accepts a scanner that serves as a communication tool between user and the program
   * @return return String - returns a String that is then processed in the main to properly output the adequate menu
   * The following method uses Scanner to let user remove existing key from a current guitar interface
   */
   public String removeKey(Scanner console)
   {
      if (currentInterface.equals("null"))
      {
         System.out.println("\nERROR. Your current interface is null. Please create at least one interface layour to access this feature\n");
         return "Null";
      }
      
      String input = "";
      while (!input.equalsIgnoreCase("QUIT"))
      {
         this.greetings();
         
         System.out.println("Here is the current layout of the guitar interface");
         this.toStringColumns();
         System.out.println("Which key would you like to remove?");
         input = console.next();
         
         if (input.equalsIgnoreCase("QUIT"))
         {
            return input;
         }
         
         else if (input.equalsIgnoreCase("Return"))
         {
            return "settings";
         }
         
         else if (input.equalsIgnoreCase("Menu"))
         {
            return "`";
         }
         
         char letterInput = input.charAt(0);
         
         if (!guitarMap.containsKey(letterInput) || input.length() > 1 || letterInput == '`')
         {
            System.out.println("ERROR. Must enter a valid single character key that is in the current layout and not equal to '~'. Please try again");
         }
         
         else
         {
            guitarMap.remove(letterInput);
            System.out.println("The key was succesfully removed!\n");
         }
      }    
      return "Quit";
   }
   
   
   public void interfaceOverview()
   {
      for (String name : interfaceMap.keySet())
      {
         System.out.println(name + " Guitar Interface");
         System.out.println("Key layout: " + interfaceMap.get(name));
         System.out.println("History: " + interfaceCountMap.get(name) + "\n");
      }
      System.out.println("Your current interface is: " + currentInterface);
   }
   
   /**
   * @param Scanner console - accepts a scanner that serves as a communication tool between user and the program
   * @return return String - returns a String that is then processed in the main to properly output the adequate menu
   * The following method allows user to create a whole new keyboard in the settings. This means that a completely
   * new interface with separate keys (empty upon creation) will be available for user to choose after full
   * and proper execution of this method
   */
   public String createNewInterface(Scanner console)
   {
      while (true)
      {
         this.greetings();
        
         System.out.println("Which name would you like to give to the new interface?");
         String input = console.next();
         
         if (input.equalsIgnoreCase("QUIT"))
         {
            return input;
         }
         
         else if (input.equalsIgnoreCase("Return"))
         {
            return "settings";
         }
         
         else if (input.equalsIgnoreCase("Menu"))
         {
            return "`";
         }
         
         if (interfaceMap.containsKey(input))
         {
            System.out.println("ERROR. The following name is already taken. Please try again.");
         }
         
         else
         {
            System.out.println("Note: the new layout will be empty upon creation. You may adjust it throught the settings menu");
            Map<Character, StringPlus> guitarMapHolder = new LinkedHashMap<>();
            Map<Character, Integer> countMapHolder = new LinkedHashMap<>();
            
            interfaceMap.put(input, guitarMapHolder);
            interfaceCountMap.put(input, countMapHolder);
            System.out.println("New interface successfully added!");
            if (currentInterface.equals("null"))
            {
               currentInterface = input;
            }
         }
      }
   } 
   
   /**
   * @param Scanner console - accepts a scanner that serves as a communication tool between user and the program
   * @return return String - returns a String that is then processed in the main to properly output the adequate menu
   * The following method allows user to switch current keyboard layout to another one from the available list
   */
   public String switchInterface(Scanner console)
   {
      if (currentInterface.equals("null"))
      {
         System.out.println("ERROR. Your current interface is null. Please create at least one interface layour to access this feature");
         return "Null";
      }
      
      while (true)
      {
         
         System.out.println("Here is the list of available interfaces:");
         this.interfaceOverview();
         
         this.greetings();
         
         System.out.println("To which interface would you like to switch to?");
         String input = console.next();
         
         if (input.equalsIgnoreCase("QUIT"))
         {
            return input;
         }
         
         else if (input.equalsIgnoreCase("Return"))
         {
            return "settings";
         }
         
         else if (input.equalsIgnoreCase("Menu"))
         {
            return "`";
         }

         else if (interfaceMap.containsKey(input))
         {
            guitarMap = interfaceMap.get(input);
            countMap = interfaceCountMap.get(input);
            currentInterface = input;
         }
         
         else
         {
            System.out.println("ERROR. Entered name does not refer to any existing interfaces. Please Try again");
         }
      }   
   }
   
   /**
   * @return return String - returns String that represents the currentInterface
   */
   public String currentInterface()
   {
      return currentInterface;
   }
   
   /**
   * @param Scanner console - accepts a scanner that serves as a communication tool between user and the program
   * @return return String - returns a String that is then processed in the main to properly output the adequate menu
   * The following method allows user to rename currently existing interface
   */
   public String renameInterface(Scanner console)
   {  
      if (currentInterface.equals("null"))
      {
         System.out.println("ERROR. Your current interface is null. Please create at least one interface layour to access this feature");
         return "Null";
      }
      
      Queue<String> guitarKeys = new LinkedList<String>(interfaceMap.keySet());
      Queue<Map<Character, StringPlus>> guitarValues = new LinkedList<Map<Character, StringPlus>>(interfaceMap.values());
      Queue<Map<Character, Integer>> countValues = new LinkedList<Map<Character, Integer>>(interfaceCountMap.values());
      
      while (true)
      {  
         System.out.println("Here is the list of available interfaces:");
         this.interfaceOverview();
         
         this.greetings();

         System.out.println("Which interface would you like to rename?");
         String input = console.next();
         
         if (input.equalsIgnoreCase("QUIT"))
         {
            return input;
         }
         
         else if (input.equalsIgnoreCase("Return"))
         {
            return "settings";
         }
         
         else if (input.equalsIgnoreCase("Menu"))
         {
            return "`";
         }
            
         else if (interfaceMap.containsKey(input))
         {  
            int size = guitarKeys.size();
            String oldName = input;
            
            // Once map does not contain input, it indicates user chose a new name that does not match any of the existing
            
            while (interfaceMap.containsKey(input))
            {
               
               System.out.println("Enter 'Back' to return to the last stage\n");
               System.out.println("Which new name would you like to give to this interface?");
               input = console.next();
                
               if (input.equalsIgnoreCase("QUIT"))
               {
                   return input;
               }
                  
               else if (input.equalsIgnoreCase("Return"))
               {
                  return "settings";
               }
                
               else if (input.equalsIgnoreCase("Menu"))
               {
                  return "`";
               }
                  
               // The following return will be caught in settings mode in GuitarHero java, sending user back to the last stage in this method
               
               else if (input.equalsIgnoreCase("Back"))
               {
                  return "Rename";
               }
               
               else if (interfaceMap.containsKey(input))
               {
                  System.out.println("ERROR. New name cannot match any of the existing interface names");
               }
               
               else
               {
                  System.out.println("New name has been accepted");
               }
            }
               
            interfaceMap.clear();
            interfaceCountMap.clear();
                  
            for (int i=0; i < size; size--)
            {
               String name = guitarKeys.remove();
               if (name.equals(oldName))
               {
                  interfaceMap.put(input, guitarValues.remove());
                  interfaceCountMap.put(input, countValues.remove());
               }
                     
               else
               {
                  interfaceMap.put(name, guitarValues.remove());
                  interfaceCountMap.put(name, countValues.remove());
               }
             }    
             
             // if statement below is important so tha in case user renames the interface they are currently using
             // variable currentInterface will not contain ghost of old interface's name
             
             if (oldName.equals(currentInterface))
             {
               currentInterface = input;
             }
             
             // No need to do any manipulations on guitarMap - since interface is just getting renamed and nothing else
             // GuitarMap and countMap can both stay same whether user changes interface he is currently using or not
             
             // It is very important to "reset" the following structures here so that user stays inside this method in case they want
             // to rename multiple interfaces and the program works properly    
                    
             guitarKeys = new LinkedList<String>(interfaceMap.keySet());
             guitarValues = new LinkedList<Map<Character, StringPlus>>(interfaceMap.values());
             countValues = new LinkedList<Map<Character, Integer>>(interfaceCountMap.values());      
         }
         
         else
         {
            System.out.println("ERROR. No interface with such name. Please try again");
         }
      } 
   }
   
   /**
   * @param Scanner console - accepts a scanner that serves as a communication tool between user and the program
   * @return return String - returns a String that is then processed in the main to properly output the adequate menu
   * The following method allows user to delete an existing guitar keyboard layout. In case user deletes all existing
   * guitar interfaces, currentInterface is switched to null and some features are blocked of from user end
   */
   public String removeInterface(Scanner console)
   {
      
      while (true)
      {
         // It is important to put if statement below inside the while loop, so that in case user deletes last interface
         // they do not get to stay inside the loop and no potential issues arise
         
         if (currentInterface.equals("null"))
         {
            System.out.println("ERROR. Your current interface is null. Please create at least one interface layour to access this feature");
            return "Null";
         }
         
         System.out.println("Here is the list of available interfaces:");
         this.interfaceOverview();
            
         this.greetings();
            
         System.out.println("Your current interface is: " + currentInterface);
         
         System.out.println("Which interface would you like to remove?");
         
         String input = console.next();
         
         if (input.equalsIgnoreCase("QUIT"))
         {
            return input;
         }
            
         else if (input.equalsIgnoreCase("Return"))
         {
            return "settings";
         }
            
         else if (input.equalsIgnoreCase("Menu"))
         {
            return "`";
         }
         
         if (interfaceMap.containsKey(input))
         {
            // If statement above is crucial so that deletion of the interface user is currently using will be more logical
            // By assigning null to current interface, we then assign the first available interface in interfaceMap to current guitarMap
            // If interfaceMap is completely empty, then other commands such as reset, switch, etc, will not work
            // That way, user will need at least one interface inside interfaceMap to actually play guitar hero
            // This does not have a programming reason, but rather a user and logical one: if the null logic is cut, then after deletion of last
            // Interface in interfaceMap, guitarMap would still contain the ghost of the last guitar layout, which is unwanted because it breaks
            // The logical aspect of storage gimmick. In addition, this potentially can cause extra trouble if more
            // complex method engagin with guitar interface are to be added
            
            if (input.equalsIgnoreCase(currentInterface))
            {
               currentInterface = "null"; 
               System.out.println("Your current interface will be switch to the first guitar layout in the list"); 
            }
            
            interfaceMap.remove(input);
            interfaceCountMap.remove(input);
            System.out.println("Interface '" + input + "' was successfully deleted");
            
            if (currentInterface.equals("null"))
            {
                input = interfaceRemoval();
            }
            
            // The reason it is important to double check whethet current interface is null is simple: interfaceMap can potentially be empty
            // Hence, when for loop start, it will not produce anything valuable and currentInterface will remain null - while guitarMap and
            // countMap will contain the ghosts of just deleted, last guitar layouts. That is why it is required to double chekc through essentially
            // Two same if statements and in case currentInterface is null after the for loop, set guitarMap and countMap to be essentially empty maps
            
            if (currentInterface.equals("null"))
            {
               System.out.println("There are no avaliable interfaces left \nYou would not be able to access some feautures of the program, including play mode");
               System.out.println("To return the full fucntionality of the program, please access the settings menu and create at least one guitar interface");
               countMap = new LinkedHashMap<Character, Integer>();
            }
         }
         
         else 
         {
            System.out.println("ERROR. There is no interface with such name. Please Try again\n");
         }
      }
   }
   
   // This small method bellow is required so that when it is called in the method above, after the first interface from interfaceMap is found
   // and assigned as a new guitarMap, return statement will end inner method, not the whole method "removeInterface"
   // In case interfaceMap is empty, there is a return statement outside the for loop
   // Method 
   
   // The following method is a helper to the remove interface. It is required so that
   // in case user deletes the interface he is currently, he will get reasigned to the first 
   // keyboard layout in interfaceMap. So will the current interface be reset to the corresponding
   // layout. In case there are no more interfaces left, loop will not be executed since
   // there are no values in interfaceMap.keySet(), so user's currentInterface variable
   // will be set to equal "null" and program will handle this case properly
   
   private String interfaceRemoval()
   {
      for (String name : interfaceMap.keySet())
      {
          guitarMap = interfaceMap.get(name);
          countMap = interfaceCountMap.get(name);
          currentInterface = name;
          return name;     
      }
      
      return "null";
   }
   
   /**
   * The following method reset the current guitar interface to the deffault settings
   * using the String Keyboard variable in this class
   */
   public void deffaultReset()
   {
      guitarMap.clear();
      for (int i = 0; i < keyboard.length(); i++)
      {
         countMap.put(keyboard.charAt(i), 0);
         guitarMap.put(keyboard.charAt(i), new StringPlus(440*Math.pow(2, (i-24)/12.0) ));
      }
   }
   
   /**
   * The following method simply clearls the current guitatInterface
   */
   public void reset()
   {
      guitarMap.clear();
   }
   
   // The following method is just a helper that is used to provide basic
   // instructions in each methods
   private void greetings()
   {
      System.out.println("Enter 'QUIT' to quit the program");
      System.out.println("Enter 'Return' to go back to settings menu");
      System.out.println("Enter 'Menu' to go back to main page");
   }
   
   // We need a ToString method that would print out each key and its frequence in Hz and pitches
   // When the user intend to change the String, whatvere way he chooses(pitch or HZ), method should modify both values
   // Sample method and all methods overall should strictly work in HZ 
}