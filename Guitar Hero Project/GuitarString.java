import java.util.*;
public class GuitarString
{
   // Data fields
   private Queue <Double> ringbuffer;
   private int size;
   // Decay factor is a pretty simple term. It defined how long will it take for a sound to completely vanish and stop making any noice
   // The smaller the decay factor, the faster the sound vanishes
   double decayFactor = 0.996;
   
   // Constructors
   public GuitarString(double frequency)
   {
      if (frequency <= 0)
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
   
   public GuitarString(double[] init)
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
   I was first highly confused by the logic of this program so I decided to understand the "physics" of the process throuhg real world analogy
   I didn't understand why the outputted sound of keys A and C remains the same for each of them. Shouldn't random values of ringbuffer make it
   random on each type? But the physics process is very logicall. Frequence is more of a "fixed" property for each object. The force acting upon a
   String with frequency 440 Hz would never change that value since more energy means more distance travelled, which evens out that impact difference
   That is exactly why random numbers in ringbuffer, especially after being "averged" don't change the outputted frequence or time it takes for the
   sound to vanish. I am looking forward to start working on the second part. Maybe I could make it possible to arrange frequence for each key for
   a user. I will think about about posibilities. There are a lot of them in such big project.
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
   
   // Tic is one of the most crucial parts of this project. It is the logic that actually "decays" the sound overtime, making it dimmer and dimmer
   public void tic()
   {
      double remove = ringbuffer.remove();
      
      double peek = ringbuffer.peek();
      
      double average = (remove+peek)/2.0;
      
      double finalValue = average*decayFactor;
      
      ringbuffer.add(finalValue);
   }
   
   public double sample()
   {
      return ringbuffer.peek(); // Returning the peeked value
   }
}