import java.util.Arrays;

public class MonteCarloSimulation {
	
	public static void main(String[] arg)
	{
		// basically, i am a dumbass and my code from last time was dogshit so i'm starting over
		
		/*
		 * Simulation process	Done!
		 * 		1. Generate a realization of X		Done!
		 * 			a. Generate a pseudo-random number u, 0 < u < 1		Done!
		 * 			b. Plug pseudo-random number u into inverseCDF function to obtain realization x		Done!
		 * 		2. Repeat for sample size n		Done!
		 * 		3. Calculate estimations m_n of sample mean M_n		Done!
		 * 
		 * 		Sample sizes to be used: n = 10, 30, 50, 100, 250, 500, 1000	Done!
		 */
		
		// random number generator test
		// System.out.println(psuedoRandom(51));
		// it works???
		
		// simulation test
		/*
		for(int i = 51; i < 151; i++)
		{
			System.out.println(simulation(i));
		}
		*/
		// looks like this also works??

		// finding the x generated by u51, u2, u3 for the report
		// System.out.println(simulation(51) + " " + simulation(52) + " " + simulation(53));
		
		
		int[] sampleSizes = {10, 30, 50, 100, 250, 500, 1000};	// pretty much useless
		int seed = 51;
		
		// System.out.println(seed);
		/*
		for(int i = 0; i < sampleSizes.length; i++)
		{
			System.out.println("Sample Size: " + sampleSizes[i]);
			System.out.println("Before Seed: " +  seed);
			findEstimations(sampleSizes[i], seed);
			seed += sampleSizes[i] * 110;	// please fucking work
			System.out.println("After Seed: " +  seed + "\n");
		}
		*/
		
		
		System.out.println("Section 4 estimations: ");

		for(int i = 0; i < 3; i++)
		{
			System.out.println("Sample Size: " + sampleSizes[i]);
			// System.out.println("Before Seed: " +  seed);
			printArray(findEstimations(sampleSizes[i], seed, 110));
			seed += sampleSizes[i] * 110;	// please fucking work
			// System.out.println("After Seed: " +  seed + "\n");
			System.out.println();
		}

		// simulation algorithm starts to crash at sample sizes larger than 100
		for(int i = 3; i < 7; i++)
		{
			System.out.println("Sample Size: " + sampleSizes[i]);
			printArray(findEstimationsLargeInput(sampleSizes[i], 110));
			System.out.println();
		}
		
		for(int i = 0; i < 10; i++)
		{
			System.out.println();
		}
		
		
		System.out.println("Section 5 estimations: ");
		
		int[] newSampleSizes = {3, 9, 27, 81};
		seed = 1;
		
		for(int i = 0; i < 2; i++)
		{
			double[] mn = new double[newSampleSizes[i]];
			System.out.println("Sample Size: " + newSampleSizes[i]);
			mn = findEstimations(newSampleSizes[i], seed, 550);
			seed += sampleSizes[i] * 550;
			System.out.println("Sample mean: " + findSampleMean(mn));
			System.out.println("Sample variance: " + findSampleVAR(mn));
			// printArray(mn);
			/*
			System.out.println("Z-scores:");
			printArray(standardize(mn));
			*/
			
			calculateProbabilities(standardize(mn));
			
			System.out.println();
		}
		
		for(int i = 2; i < 4; i++)
		{
			double[] mn = new double[newSampleSizes[i]];
			System.out.println("Sample Size: " + newSampleSizes[i]);
			mn = findEstimationsLargeInput(newSampleSizes[i],  550);
			System.out.println("Sample mean: " + findSampleMean(mn));
			System.out.println("Sample variance: " + findSampleVAR(mn));
			// printArray(mn);
			/*
			System.out.println("Z-scores:");
			printArray(standardize(mn));
			*/
			
			calculateProbabilities(standardize(mn));
			
			System.out.println();
		}
		
	}
	
	/**
	 * method we will call to run a single simulation - this will return a single realization of X
	 * @param seed - input value
	 * @return x - a realization of distance X from the dropoff point
	 */
	public static double simulation(int seed)
	{
		double x = 0;
		
		double u = pseudoRandom(seed);	// given an integer input value, plug it into random number generator
		x = inverseCDF(u);	// plug random number into inverseCDF to find distance, simple enough
		
		return x;
	}
	
	/**
	 * recursive implementation of linear congruential random number generator
	 * @param val - subscript of u, number of iterations through recursive loop
	 * @return - u, a pseudo-random number generated by the linear congruential random number generator
	 */
	public static double pseudoRandom(int val)
	{
		// Parameter definitions
		double ans = 0.0;
		int i = val - 1;
		double x = 1000;	// seed
		double a = 24693;	// multiplier
		double c = 3967;	// increment
		double k = Math.pow(2, 18);	// modulus
		
		// recursion base case
		if(i == 0)
		{
			x = ((a * 1000) + c) % k;
			ans = x / k;
			return ans;
		}
		
		x = ((a * (pseudoRandom(val - 1) * k)) + c) % k;	// recursive call
		ans = x / k;
		return ans;
	}

	/**
	 * inverse cumulative distribution function, derived from CDF given in assignment description
	 * @param u - pseudo random number that ranges from 0 to 1
	 * @return x - a radius of a circle which the drone has a u probability of landing the newspaper in
	 */
	public static double inverseCDF(double u)
	{
		double x = 0;
	
		// mathematical expression: sqrt(-6498ln(1-u))
		double ln = Math.log(1 - u);
		double radicand = -6498 * ln;
		x = Math.sqrt(radicand);
		
		return x;
	}
	
	/**
	 * method will take in an array of doubles and return the mean values
	 * @param x - array of doubles, sample size n, each element a realization of Rayleigh random variable X
	 * @return m_n - an estimation of the sample mean for that sample
	 */
	public static double findSampleMean(double[] x)
	{
		double sum = 0.0;
		double m_n = 0.0;
		
		for(int i = 0; i < x.length; i++)
		{
			sum += x[i];
		}
		
		m_n = sum / x.length;
		
		return m_n;
	}

	/**
	 * method will take in array of doubles and return sample variance
	 * @param x - array of doubles, all of which are realizations of random variable X
	 * @param m_n - array of sample means
	 * @return var - an estimation of the variance
	 */
	public static double findSampleVAR(double[] m_n)
	{
		double var = 0.0;
		
		double sum = 0.0;
		
		for(int i = 0; i < m_n.length; i++)
		{
			sum += Math.pow(m_n[i], 2) - Math.pow(findSampleMean(m_n), 2);
		}
		var = sum / m_n.length;
		
		return var;
	}
	
	/**
	 * returns an array of standardized values, calculated from an array of sample means
	 * @param m_n - array of sample means
	 * @return z - standardized sample means
	 */
	public static double[] standardize(double[] m_n)
	{
		double mean = findSampleMean(m_n);
		double var = findSampleVAR(m_n);
		
		double[] z = new double[m_n.length];
		
		for(int i = 0; i < m_n.length; i++)
		{
			z[i] = (m_n[i] - mean)/Math.sqrt(var);
		}
		
		return z;
	}
	
	/**
	 * prints an array of doubles to terminal, duh
	 * @param x - array of doubles
	 */
	public static void printArray(double[] x)
	{
		for(int i = 0; i < x.length; i++)
		{
			System.out.println(x[i]);
		}
	}

	/**
	 * for whatever reason trying to do a full loop in the main method kept giving me stack overflows so i made the two inner loops their own method
	 * @param n - sample size n
	 * @param seed - seed value, fuck you
	 */
	public static double[] findEstimations(int n, int seed, int reps)
	{
		double[] m = new double[reps];		// loop for storing "reps" estimates of sample mean of size n
		for(int i = 0; i < m.length; i++)	// loop for generating "reps" estimates of the sample mean
		{
			double[] r = new double[n];	// array for storing sample data
			for(int j = 0; j < r.length; j++)	// loop for generating sample data
			{
				r[j] = simulation(j + seed);
			}
			seed += r.length;	// increment seed so we aren't getting the same values over and over again
			m[i] = findSampleMean(r);	// find sample mean, store in sample mean array
			// System.out.println(i + ": " + m[i]);
		}
		// System.out.println();
		// printArray(m);
		
		return m;
	}

	/**
	 * large sample sizes result in stack overflow due to large recursion, so this method finds sample, but with some reusing of values
	 * @param n - sample size
	 * @param reps - number of repeated trials
	 */
	public static double[] findEstimationsLargeInput(int n, int reps)
	{
		double[] m = new double[reps];		// loop for storing "reps" estimates of sample mean of size n
		for(int i = 0; i < m.length; i++)	// loop for generating "reps" estimates of the sample mean
		{
			double[] r = new double[n];	// array for storing sample data
			for(int j = 0; j < r.length; j++)	// loop for generating sample data
			{
				int seed = (int)(Math.random() * 10000 + 1);	// generate a random number for the seed so that we still have a somewhat random generation of numbers while avoiding stack overflow
				r[j] = simulation(seed);
			}
			m[i] = findSampleMean(r);	// find sample mean, store in sample mean array
			// System.out.println(i + ": " + m[i]);
		}
		// System.out.println();
		// printArray(m);
		
		return m;
	}

	/**
	 * find the probabilities that a realization of a z-score lands to the left of certain threshold values
	 * @param mn - array of doubles used to calculate probabilities
	 */
	public static void calculateProbabilities(double[] mn)
	{
		double p = 0.0;
		
		double[] z = {-1.4, -1.0, -0.5, 0.0, 0.5, 1.0, 1.4};
		for(int i = 0; i < z.length; i++)
		{
			double count = 0.0;
			for(int j = 0; j < mn.length; j++)
			{
				if(mn[j] < z[i])
					count++;
			}
			p = count / mn.length;
			System.out.println("P[z <= " + z[i] + "] = " + p);
			// System.out.println(p);
		}
		
	}
}