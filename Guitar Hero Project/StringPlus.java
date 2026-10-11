import java.util.*;

/**
* @author Tair Moldakhmetov
* @version 1.0
* The following class mimicks the behaviour of a real world guitar
* It provides the core processing logic, which makes the program
* run properly and correclty output corresponding sound upon
* the press of adequate key on user's keyboard
*/

public class StringPlus
{
   // Data fields
   
   private Queue <Double> ringbuffer;
   private int size;
   private double frequency;
   
   // Decay factor is a pretty simple term. It defined how long will it take 
   // for a sound to completely vanish and stop making any noice
   // The smaller the decay factor, the faster the sound vanishes
   
   double decayFactor = 0.996;
   
   // Constructors
   
   /**
   * @param double frequency - frequency associated with corresponding key-string in the program
   * @throws IllegalArgumentException() if frequency is less or equal to zero, or if variable size is
   * less than 2. Last requirement is connected to the program logic in mimicing the guitar
   */
   public StringPlus(double frequency)
   {
      if (frequency <= 0)
      {
         throw new IllegalArgumentException();
      }
      
      size = Math.round ((float)(StdAudio.SAMPLE_RATE/frequency));
      ringbuffer = new LinkedList<>();
      
      // The reason we want size to be more than 2 is becuase of the way method tic processes the sound
      // Tic needs at least 2 elements in the ringbuffer. the tic method comment.
      // Notive that as frequency gets larger, size shrinks: our sample rate is 41000 - a constant
      // 41000/1000=410=size, but 41000/30000 will be rounded to just 1.
      // The reason logic is written like that is because we aim to mimic real world behavior of a guitae
      // And in reality, the higher the frequenct of a guitar string, the less "time" it actually
      // makes any sound. We work with frequncies bigger than 0 and less that 22050 - or otherwise
      // Tic will remove the first value, peel will yield null - and you cannot convert null to double

      if (size < 2)
      {
         throw new IllegalArgumentException();
      }
      
      for (int i = 1; i <= size; i++)
      {
         ringbuffer.add(0.0);
      }
      
      this.frequency = frequency;
   }
   
   /**
   * @param double[] init - array that contains a random swing data of a String, that helps mimic 
   * the behaviour of a real guitar and its String
   * @throws IllegalArgumentException() if passed array is null or has less than 2 elements
   * IllegalArgumentException is required to prevent issues with tic() method
   */
   public StringPlus(double[] init) //Might wanna delete
   {
      if (init == null || init.length < 2) // I have added another null check in case passed array is completely empty so that the programm won't crash
      {
         throw new IllegalArgumentException();
      }
       
      size = init.length;             // it is important to pass a value to size in here so that future methods such as pluck work properly
      ringbuffer = new LinkedList<>();
      
      for (int i = 0; i < size; i++)
      {
         ringbuffer.add(init[i]);
      }
   }
       
   /*
      This one of the most fundamental methods in the whole program. It is the method that fills the ringbuffer
      with random noise that is then used to mimic the sound of a real guitar String
      Notably, upon pressing the key first few second of sound will be "different" each time
      This is the real world behaviour of an actual guitarString since upon pressing indeed the "swinging"
      in the first few moments produces a slightly different sound that it should. For example, a 440 Hz string
      would produce a 441 or 439 Hz sound in first few moments, "averaging" to 440 thereafter
      That is exactly the way method tic is designed to work
   */
   
   /**
   * The following method fills ringbuffer LinkedList with random values ranging from -0.5 to 0.5
   */
   public void pluck()
   {
      Random random = new Random();
      ringbuffer = new LinkedList<>(); // Cleaning the queue
      
      // I had noticed that boundries can be set by inputing values inside random methods, so I have decided to include that solution as well
      /*
      for (int i = 0; i < size; i++)
      {
         ringbuffer.add(random.nextDouble(-0.5, 0.5));
      }   
      */
      
      // To ensure the proper compilation, the actuall solution is more "general"
      
      for (int i = 0; i < size; i++)
      {
         ringbuffer.add(random.nextDouble() - 0.5);
      }
   }
   
   // Tic is one of the most crucial parts of this project. It is the logic that actually "decays" 
   // the sound overtime, making it dimmer and dimmer. In addition, because we filled ringbuffer with random
   // swings, we perfectly mimic the behavior of a real world guitar string. The lower the frequency of a guitat string,
   // the longer it stays "alive" and produces a sound. That is perfectly reflected by our size line of code in the
   // contructor: size = Math.round ((float)(StdAudio.SAMPLE_RATE/frequency)); As we have pointed out, the higher the
   // frequency, the smaller the ringbuffer, hence, decay factor of 0.996 will make ringbuffer an array with all 0 values
   
   // Notive how tic actually works: we never "shrink" our ringbuffer in a common way. Yes, we do remove a value, but then
   // we use peek and create an average value doble average, which we put back in the array. But because we multiplied
   // the average by decay value, the value we put back in the ringbuffer will get smaller and smaller every time we call tic
   // Eventually, the corresponding ringbuffer of a specific key representin some Hz value for a "real world"
   // guitar string has all 0.0 in it. You can try to do StdAudio.play(g.sample()); in the main of GuitarHero
   // But it simply will simply produce no sound - because value you pass through sample is 0.0, it does not produce any sound
   
   /**
   * Method tic is the method that contributes to sound decaying over time
   */
   public void tic()
   {
      double remove = ringbuffer.remove();
      
      double peek = ringbuffer.peek();
      
      double average = (remove+peek)/2.0;
      
      double finalValue = average*decayFactor;
      
      ringbuffer.add(finalValue);
   }
   
   /**
   * @return ringbuffer.peek() - returns the most resent "String swing" from ringbuffer array
   * The following method helps StdAudio to achive a value used in playing the sound of correposponding key
   */
   public double sample()
   {
      return ringbuffer.peek(); // Returning the peeked value
   }
   
   /**
   * @return String.format("%.2f Hz", frequency) - returns the frequency of corresponding GuitarString with "Hz" label
   */
   public String toString()
   {
      return String.format("%.2f Hz", frequency);
   }
   
   /*
   public void changeFrequency()
   {
      Scanner console = new Scanner(System.in);
      System.out.println("Please enter the HZ value you want to assign to the key");
      while (!console.hasNextInt() && this.frequency <= 0)
      {
         double frequency = console.nextInt();
         if (console.hasNextInt() && frequency <= 0)
         {
            this.frequency = frequency;
         }
         
         else
         {
            System.out.println("ERROR. Frequency value must be equal to or greater than 0");
            System.out.println("Please try again: ");
         }
      }
      
      if (frequency <= 0) // Wanna remove
      {
         throw new IllegalArgumentException();
      }
      
      size = Math.round ((float)(StdAudio.SAMPLE_RATE/frequency));
      ringbuffer = new LinkedList<>();
      
      if (size < 2)
      {
         throw new IllegalArgumentException();
      }
      
      for (int i = 1; i <= size; i++)
      {
         ringbuffer.add(0.0);
      }
   }
   */
}