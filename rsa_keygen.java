import java.math.*;
import java.io.PrintWriter;
import java.lang.Integer;
import java.lang.reflect.Array;
import java.security.*; // for SecureRandom

public class rsa_keygen {

	static int[] low_primes= {3,7,9,11,13,17,19,23,29,31,37,41,43,47,53};//to find small coprime
	static BigInteger one = new BigInteger("1");

	public static void main(String[] args) throws Exception{

		BigInteger p, q, N, e, d,  pSub, qSub, order;
		String public_keyfile;
		String private_keyfile;
		int num_bits = 32;

		//check cmd line args
		if(args.length == 0 || args.length < 6) {
			System.out.println("Usage: -p <public key file> -s <secret key file> -n <number of bits>");
			System.exit(0);	
		}

		//set cmd line args 
		num_bits = Integer.parseInt(args[5]); //convert string to int
		public_keyfile = args[1];
		private_keyfile = args[3];

		//A. 2 large prime numbers, use a different seed for each
		p = BigInteger.probablePrime(num_bits/2, new SecureRandom());
		q = BigInteger.probablePrime(num_bits/2, new SecureRandom());


		//B. Compute N , is private: p*q
		N = p.multiply(q);

		//C. Compute order of the group Zn* = (p-1)(q-1)
		pSub = p.subtract(one);
		qSub = q.subtract(one);
		order = pSub.multiply(qSub); 

		//D. loop through smallest possible primes
		e = rsa_keygen.coprime_val(order);

		//E. compute multiplicative inverse e mod the order of the group
		d = e.modInverse(order);

		//write public key: (N,e). private key: (N,d) to files
		rsa_keygen.write_keys_file(public_keyfile, args[5], N, e);
		rsa_keygen.write_keys_file(private_keyfile, args[5], N, d);

		/*So, read in the key data from the -c flag*/
		BigInteger key_data[] = null;
		for (int i = 0; i < args.length; i++){

			if (args[i].equals("-c")){

				i++;
				key_data = rsa_funcs.read_integer_file(args[i]);
				break;

			}

		}
		/*If there was no -c flag, then use the private key that was just created*/
		if (key_data == null){

			key_data = rsa_funcs.read_integer_file(private_keyfile);

		}

		String filename = public_keyfile + "-casig";
		/*Create and rsa signature on the public key*/
		rsa_sign R = new rsa_sign (key_data, rsa_funcs.read_string_file(public_keyfile));
		/*Output it to same directory as public key + -casig*/
		rsa_funcs.write_file(filename, R.encrypted_data.toString());

		/* coprime_val:
		 * 	returns a possible value for e, preferably a smaller value
		 * 	that is coprime to the order of the group	
		 */

	}
	private static BigInteger coprime_val(BigInteger order) {

		BigInteger e = new BigInteger("-1");
		BigInteger temp = new BigInteger("0");
		BigInteger param = new BigInteger("1");

		int i;

		//loop through possible lower primes to find one that is coprime
		for(i = Array.getLength(low_primes)-1; i >= 0 ; i--){

			param = BigInteger.valueOf(low_primes[i]); 
			temp = order.gcd(param);//returns gcd b/n the 2 numbers
			if(temp.equals(one)) {
				e = BigInteger.valueOf(low_primes[i]);
			}			
		}

		return e;
	}

	/* write_keys_file: 
	 * 	Writes keys to chosen file
	 */
	public static void write_keys_file(String filename, String num_bits, BigInteger N, BigInteger temp) throws Exception
	{
		PrintWriter writer = new PrintWriter(filename, "UTF-8");

		try 
		{	
			writer.println(num_bits);
			writer.println(N.toString());
			writer.println(temp.toString());
		}
		catch (Exception e)
		{
			System.err.format("Exception occurred trying to write '%s'.", filename);
			e.printStackTrace();
		}
		finally
		{
			writer.close();
		}

	}
}
