// Tair Moldakhmetov 09/27/2026 Fall 2026
// This is the main program that calls the adequate method based on user's input
// Program mimics the behavior of a real world guitar and treats corresponding key
// on the keyboard as a guitarString, mimicing its sound
import java.util.*;
public class GuitarHeroPlus 
{  
   // Just testing the posibilities of Java - it appear that if you just do static, java will execute anything inside it even before the main!
   static
   {
      System.out.println("Testing...");
   }
   
   // The following code essentially builds the main menu and processes the interactions with user
   // Since we use switch logic in the main method, we create correspodning windows via other methods
   // in this program mainMenu() is just one of them. Other two are the actual game and the Settings menu
   public static String mainMenu(Guitar g, Scanner console, String input)
   {
      System.out.println("\n~~~Welcome to the main menu of Guitar Hero!~~~");
      System.out.println("This program let's you play a guitar using the keyboard");
      
      while (true)
      {
         System.out.println("Enter 'Settings' to access the settings, adjust the guitar, and interact with guitar interfaces");
         System.out.println("Enter 'QUIT' to stop the program");
         
         if (g.currentInterface().equals("null"))
         {
            System.out.println("Your current interface is null, you would not be able to access some features of guitarHero program");
            System.out.println("To restore full access to the game, please access settings and create at least one guitar layout");
         }
         
         else
         {
            System.out.println("Enter 'Play' to play the guitar");
            System.out.println("Enter 'Layout' to see available keyboard layouts"); // Should have Guitar37, Guitar+ with full keyboard or something like that and Custom
            System.out.println("Enter 'History' to see the amount of times each key was pressed in the current interface");
            System.out.println("Enter 'Overview' to see the information regarding current guitar interface layout with Hz values for each key");
         }
         
         input = console.next();
         
         // if inputs listed in this if statement are received, then we return input so that the 
         if (input.equalsIgnoreCase("QUIT") || input.equalsIgnoreCase("Settings") || (input.equalsIgnoreCase("Play") && !g.currentInterface().equals("null")))
         {
            return input;
         }
         
         else if (input.equalsIgnoreCase("History") && !g.currentInterface().equals("null"))
         {
            System.out.println(g.printHistogram());
         }
         
         else if (input.equalsIgnoreCase("Overview") && !g.currentInterface().equals("null"))
         {
            g.toStringColumns();
         }
         
         else if (input.equalsIgnoreCase("Layout"))
         {
            g.interfaceOverview();
         }
         
         else
         {
            System.out.println("\nERROR. No such command. Please try again\n");
         }       
      }
   }
   
   // Guitar g and Scanner concole are required as inputs to actually alter the guitar gimmicks and properties
   public static String settings(Guitar g, Scanner console)   
   {
      String input;
      System.out.println("/n~~~Welcome to the Settings of Guitar Hero!~~~");
      while (true)
      {
         // Do switch method(switching interfaces of a guitar) as well
         System.out.println("Enter 'QUIT' to stop the program");
         System.out.println("Enter 'Menu' to get back to the main menu");
         System.out.println("Enter 'Overview' to see current guitar interface name and key layout with corresponding Hz values \n");
         System.out.println("Enter 'Adjust' to add or change guitar's key settings");
         System.out.println("Enter 'Delete' to delete a correposding key logic");
         System.out.println("Enter 'Deffault' to reset the current guitarInterface to deffault settings\n");
         
         System.out.println("Enter 'Reset' to clear the current interface");
         System.out.println("Enter 'Create' to create a new interface");
         System.out.println("Enter 'remove' to delete an interfafe");
         System.out.println("Enter 'Rename' to rename the interface");
         System.out.println("Enter 'Switch' to switch between available interfaces");
         System.out.println("Enter 'Interfaces' to see the information regarding interfaces of the guitar");
         
         //Write a method that would allow to go back in changing the key to switch the key you are working with
         input = console.next();
         if (input.equalsIgnoreCase("QUIT"))
         {
            return input;
         }
         
         else if (input.equalsIgnoreCase("Menu"))
         {
            return "`";
         }
         
         else if (input.equalsIgnoreCase("Create"))
         {
            input = g.createNewInterface(console);
         }
         
         else if (input.equalsIgnoreCase("Adjust"))
         {
            input = g.adjustKey(console);
         }
         
         else if (input.equalsIgnoreCase("Delete"))
         {
            input = g.removeKey(console);
         }
         
         else if (input.equalsIgnoreCase("Remove"))
         {
            input = g.removeInterface(console);
         }
         
         else if (input.equalsIgnoreCase("Reset"))
         {
            g.reset();
         }
         
         else if (input.equalsIgnoreCase("Deffault"))
         {
            g.deffaultReset();
         }
         
         else if (input.equalsIgnoreCase("Overview"))
         {
            System.out.println("\nYour current interface is: " + g.currentInterface());
            System.out.println("Bellow is the key layout of the current interface");
            g.toStringColumns();
         }
         
         else if (input.equals("Interfaces"))
         {
            g.interfaceOverview();
         }
         
         else if (input.equalsIgnoreCase("Switch"))
         {
            input = g.switchInterface(console);
         }
         
         else if (input.equalsIgnoreCase("Rename"))
         {
            input = g.renameInterface(console);
         }
        
         else
         {
            System.out.println("\nERROR. Invalid input statement or current interface is null. Please Try again or create at least one guitar layout\n");
         }

         
         //It is important to start if section from begining there and also leave else statements here
         //When a method from GuitarPlus class is used to adjust, remove, or add a key and it return a String
         // THis string is once again assigned to variable "input" - see code higher and GuitarPlus Interface
         // Putting quit, menu, and other key phrases at the beggining cuts the ablity of a program to then
         // Process the returned string from GuitarPlus class method properly, which result in user getting thrown back to Settings
         // to Settings Interface without taking in account what he actually submitted while he was adjusting the keys
         
         
         if (input.equalsIgnoreCase("QUIT"))
         {
            return input;
         }
         
         else if (input.equalsIgnoreCase("Menu") || input.equals("`"))
         {
            return "`";
         }
         
         // if statement below cathes returned input = rename value in case user decided to go back in the rename method,
         // sending him back to choosing which interface to rename
         else if (input.equalsIgnoreCase("Rename"))
         {
            input = g.renameInterface(console);
         }
      }
   }
   
    public static String guitarHero(Guitar g)
    {
        System.out.println("\n~~~Welcome to Guitar Hero Game! Press the 'esc' key on the keyboard to quit the program~~~");
        System.out.println("Press '~' to access the Menu");
        System.out.println("Press 'esc' to end the program");
        System.out.println("Here is the current keyboard Layout");
        g.toStringColumns(); 
        // this is an infinite loop--user must quit the application
        StdDraw.init();
        for (;;) 
        {
            // check if the user has typed a key; if so, process it   P
            // Notice that our if checks if StdDraw had key typed - this means it will only detected keys actually 
            // pressed in that small window that pops up when you run the program, not just any key pressed anywhere
            if (StdDraw.hasNextKeyTyped())   
            {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                
                if (key == '`') 
                {
                     System.out.println("\nBelow is the statistics showing how many times you played each key:");
                     System.out.println(g.printHistogram()); // Printing the history of pressed keys
                     System.out.print("\nThe amount of times method 'tic()' was called is: ");
                     System.out.print(g.time() + "\n"); // Printing the amount of times tic was called
                     StdDraw.windowDispose();
                     return "`";             
                }
                
                else if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_ESCAPE))
                {
                  System.out.println("\nBelow is the statistics showing how many times you played each key:");
                  System.out.println(g.printHistogram());
                  System.out.print("\nThe amount of times method 'tic()' was called is: ");
                  System.out.println(g.time());
                  System.exit(0);
                }
                
                else if (g.hasString(key)) 
                {
                
                    g.pluck(key);
                } 
                
                else 
                {
                    System.out.println("bad key: " + key);
                }
            }
            
            // send the result to the sound card
            StdAudio.play(g.sample());
            g.tic(); 
        }
    }
    
    public static void main(String[] args) 
    {
      // We use screen state as a storage folder. All desribed method on top have return type of String
      // Casting screenState = guitarHero(guitar), for example, let's the logic work and a program actually runs a gamemode
      // At the meantime, this allows us to use screenState as mode switch if we set it equal to a method with retrun type String
       String screenState = "`"; 
       Guitar guitar = new GuitarPlus();
       String userInput = "";
       Scanner console = new Scanner(System.in);
       
       while (!screenState.equalsIgnoreCase("QUIT"))
       {
           switch (screenState.toLowerCase())
           {
              case "play"->{ //Play method
                 screenState = guitarHero(guitar);
                 }
              
              case "`"->{ // Main Menu
                 screenState = mainMenu(guitar, console, userInput);  
                 }
                 
              case "settings"->{ // Settings 
                  screenState = settings(guitar, console);
              }                          
           }
                      
           //guitarHero(guitar)
           //state = mainMenu(guitar);
       }
       
       for (java.awt.Window window : java.awt.Window.getWindows())
       {
          window.dispose();
       }
    }
}
 