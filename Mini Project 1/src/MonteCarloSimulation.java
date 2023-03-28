import java.util.Arrays;

public class MonteCarloSimulation {
	
	public static void main(String[] arg)
	{
		//System.out.println("this works"); good thing i still know how to code
		
		// we are keeping this array so we can perform statistical analysis on the numbers while also maintain some semblance of format
		double[] data = new double[1000];
		
		for(int i = 0; i < 1000; i++)
		{
			int trial = i + 1;	// trial number is increased by 1 because the simulation requires a nonzero integer as a parameter
			
			// trial number multiplied by 4 to get seed number because each trial will need to regenerate four random numbers max
			int seed = 4 * trial;

			double out = simulation(seed);
			
			System.out.println("Trial " + trial + ": " + out + " seconds");
			
			
			// System.out.println(out + " seconds \n");	commented out to preserve space in output terminal
			
			data[i] = out;
		}
		
		System.out.println("\n");	// give me some space, richard!
		
		
		// the statistical part
		
		Arrays.sort(data);
		
		
		double mean = mean(data);
		System.out.println("Mean: " + mean + " seconds");
		
		double q1 = q1(data);
		System.out.println("First Quartile: " + q1 + " seconds");
		
		double med = median(data);
		System.out.println("Median: " + med + " seconds");
		
		double q3 = q3(data);
		System.out.println("Third Quartile: " + q3 + " seconds");
		
		System.out.println();
		
		int[] leftEvents = {15, 20, 30};
		for(int i = 0; i < leftEvents.length; i++)
		{
			System.out.println("P[W <= " + leftEvents[i] + "] = " + probNonexceed(data, leftEvents[i]));
			// System.out.println("P[W > " + leftEvents[i] + "] = " + probExceed(data, leftEvents[i]));
		}
		
		System.out.println();
		
		int[] rightEvents = {40, 70, 100, 120};
		for(int i = 0; i < rightEvents.length; i++)
		{
			System.out.println("P[W > " + rightEvents[i] + "] = " + probExceed(data, rightEvents[i]));
			// System.out.println("P[W <= " + rightEvents[i] + "] = " + probNonexceed(data, rightEvents[i]));
		}
		
		System.out.println("\nCDF Values: ");
		int[] totalEvents = {6, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 120, 128};
		for(int i = 0; i < totalEvents.length; i++)
		{
			System.out.println("P[W <= " + totalEvents[i] + "] = " + probNonexceed(data, totalEvents[i]));
		}
		
		
		/*
		 * Testing log:
		 * test 1: loop works and prints out each trial and number correctly
		 */
		
		// inverse function test
		/*
		System.out.println(inverse(0.80));
		System.out.println(inverse(0.20));
		
		System.out.println(inverse(0.90));
		System.out.println(inverse(0.10));
		
		System.out.println(inverse(0.99));
		System.out.println(inverse(0.01));
		*/
		// looks like it works??
	}
	
	/*
	 * @brief main simulation process
	 * @param integer n, used to generate a random number with "random" method above which decides the course of the simulation
	 */
	public static double simulation(int seed)
	{
		// instantiate an empty string for output
		// System.out.println("Trial " + n + ": \n");	removed because we want to use output to calculate other statistics
		
		// instantiate an integer to keep track of how many times the customer has been called
		int calls = 0;
		
		// instantiate parameters for number of seconds before either customer picks up or representative gives up calling
		double w = 0;
		
		/* modeled after flowchart, on page 6 of report
		 * 
		 * if() statements are comparing random value against benchmark values in seemingly reverse order because
		 * inverse() function returns higher values as doubles closer to 1 return higher and higher values, thus it's better
		 * to call inverse() when rand is determined to be less than 0.5 because we will then get a value that's reasonable for our model
		 */
		while(calls < 4)	// repeat process either until customer answers or four unsuccessful calls have been made
		{
			// System.out.println("w: " + w);
			// System.out.println("seed: " + seed);
			w += 6;	// it takes 6 seconds to pick up the phone and dial customer's number
			
			// generate random value for trial n by plugging n into linear congruential random number generator 0
			double rand = random(seed);
			// System.out.println(rand);
			
			if(rand >= 0.8)	// probability that line is busy
			{
				w += 3;	// time to detect busy signal
				w++;	// time to end call
			}
			else if(rand >=  0.5)	// probability that customer is unavailable to answer call
			{
				w += 25;	// time to wait for 5 rings
				w++;	// time to end call
			}
			else	// if customer's line isn't busy and isn't unavailable, then he/she MUST be available
			{
				// System.out.println(w);
				double x = inverse(rand);
				// System.out.println("x: " + x);
				w += x;
				break;
			}
			
			calls++;	// no pickup, increase seed by one and number of calls by one, repeat
			seed++;
		}
		
		return w;
	}
	
	/*
	 * @brief recursive method implementation of RNG algo
	 * @param integer val, plugged into formula to output a "random" value, represents a certain iteration
	 */
	public static double random(int val)
	{
		// Parameter definitions
		double ans = 0.0;
		int i = val - 1;
		double x = 0;
		double a = 24693;
		double c = 3517;
		double k = Math.pow(2, 17);
		
		// Recursive base case
		if(i == 0)
		{
			x = ((a * 1000) + c) % k;
			ans = x / k;
			return ans;
		}
		
		x = ((a * (random(val - 1) * k)) + c) % k;
		ans = x / k;
		return ans;
	}

	/*
	 * @brief method to act as inverse function for CDF function for inverse function of exponential distribution
	 * for continuous random variable X, described on page 5 of report
	 */
	public static double inverse(double u)
	{
		double x = -12 * Math.log(1 - u);
		return x;
	}
	
	// method to determine average time value
	public static double mean(double[] data)
	{
		double mean = 0;
		double sum = 0;
		
		for(int i = 0; i < data.length; i++)
		{
			sum += data[i];
		}
		
		mean = sum / data.length;
		return mean;
	}
	
	// methods to find quartiles and median value
	public static double q1(double[] data)
	{
		double q1 = 0;
		int q1Index = data.length / 4;
		q1 = data[q1Index];
		return q1;
	}
	public static double median(double[] data)
	{
		double median = 0;
		int medIndex = data.length / 2;
		median = data[medIndex];
		return median;
	}
	public static double q3(double[] data)
	{
		double q3 = 0;
		int q3Index = data.length / 4 * 3;
		q3 = data[q3Index];
		return q3;
	}
	
	// methods to find probabilities of nonexceedence/exceedence
	public static double probNonexceed(double[] data, int bound)
	{
		double p = 0;
		double count = 0;
		
		for(int i = 0; i < data.length; i++)
		{
			if(data[i] <= bound)
				count++;
		}
		
		p = count / data.length;
		
		return p;
	}
	public static double probExceed(double[] data, int bound)
	{
		double p = 0;
		double count = 0;
		
		for(int i = 0; i < data.length; i++)
		{
			if(data[i] > bound)
				count++;
		}
		
		p = count / data.length;
		
		return p;
	}
}