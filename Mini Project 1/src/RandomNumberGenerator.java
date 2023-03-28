/*
 * @brief Random Number Generator Algorithm for Section 3 of Project
 * @author Winston Zhang, wyz5rge
 */
public class RandomNumberGenerator {
	public static void main(String[] arg)
	{
		System.out.println(random(1));
		System.out.println(random(2));
		System.out.println(random(3));
		
		
		System.out.println(random(51));
		System.out.println(random(52));
		System.out.println(random(53));
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
}
