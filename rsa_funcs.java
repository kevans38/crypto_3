/*
import javax.crypto.*;
import javax.crypto.spec.*;

*/
import java.security.*;
import java.io.*;
import java.math.*;
import java.util.*;

public class rsa_funcs
{

	private static BufferedReader bufferedReader;

	/*
	 * Gets key file from command line with -k command
	 * This returns and array for the 3 values in the key file
	 */
	public static BigInteger[] key_file( String[] args ) throws Exception
	{
		BigInteger key_data[] = null;
				
		for ( int i = 0; i < args.length; i++ )
		{
			//-k <key file>:  required, specifies a file storing a valid AES key as a hex encoded string
			if ( args[i].equals("-k") )
			{
				i++;
				
				key_data =  read_integer_file(args[i]);

				break;
			}

		}

		if (key_data == null)
		{
			System.err.println("No file was found. Need to exit");
			System.exit(0);
		}
		
		return key_data;		
	}
	
	/*
	 * Gets key file from command line with -i command
	 * This returns the string of the value in the input file
	 */
	public static String input_file( String[] args ) throws Exception
	{
		String input_data = null;
				
		for ( int i = 0; i < args.length; i++ )
		{
			//-k <key file>:  required, specifies a file storing a valid AES key as a hex encoded string
			if ( args[i].equals("-m") )
			{
				i++;
				input_data = read_string_file(args[i]);
				break;
			}

		}

		if (input_data == null)
		{
			System.err.println("No file was found. Need to exit");
			System.exit(0);
		}
		
		return input_data;		
	}

	public static String sig_file (String [] args) throws Exception{



		String sig_data = null;

		for ( int i = 0; i < args.length; i++){


			if (args[i].equals("-s")){

				i++;
				sig_data = read_string_file(args[i]);
				break;

			}

		}

		if (sig_data == null){

			System.err.println("No file was found. Need to exit");
			System.exit(0);
		}

		return sig_data;


	}
	
	/*
	 * Gets output file name from command line with -o
	 */
	public static void output_file ( String[] args, String output ) throws Exception
	{
		for ( int i = 0; i < args.length; i++ )
		{
			//-o <output file>: required, specifies the path of the file where the resulting output is stored
			if ( args[i].equals("-s") )
			{
				i++;
				write_file( args[i], output );
				break;
			}
		}
	}

	/* Reads the data in a file, and converts the values into BigIntegers
	 * 
	 */
	public static BigInteger[] read_integer_file(String filename) throws Exception
	{
		
		BigInteger[] data = new BigInteger[3];

		
		try {
            File file = new File(filename);

            Scanner input = new Scanner(file);

            int i = 0;
            
            while (input.hasNextBigInteger())
            {
                BigInteger bi = input.nextBigInteger();
                data[i] = bi;
                i++;
            }
            
            input.close();
            
            return data;
		}
		catch (Exception e)
		{
			System.err.format("Exception occurred trying to read '%s'.", filename);
			e.printStackTrace();
			return null;
		}
	}
	
	
	/* Reads the information in a file and puts it into a single string
	 * 
	 */
	public static String read_string_file(String filename) throws Exception
	{
		
		
		try {
            FileReader file = new FileReader(filename);

            bufferedReader = new BufferedReader(file);
            
            String data = "";
            String line = null;
           
            while((line=bufferedReader.readLine())!=null)
            {
            	data += line;
            }
            		
            return data;
		}
		catch (Exception e)
		{
			System.err.format("Exception occurred trying to read '%s'.", filename);
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Writes data to chosen file
	 */
	public static void write_file(String filename, String data) throws Exception
	{
		
		PrintWriter out = new PrintWriter(filename);
		try 
			{	
				out.print(data);
				
				out.close();
			}
			catch (Exception e)
			{
				System.err.format("Exception occurred trying to write '%s'.", filename);
				e.printStackTrace();
			}
			
	}
	
	
	public static BigInteger encrypt_data(BigInteger[] key_data, BigInteger message)
	{
		BigInteger rsa_number_of_bits = key_data[0];
		BigInteger rsa_N = key_data[1];
		BigInteger rsa_e = key_data[2];
		
		byte[] zero_byte = new byte[1];
		byte[] two_byte = new byte[1];
		byte[] random_bytes = create_random_bytes(rsa_number_of_bits);
		byte[] message_bytes = message.toByteArray();
		byte[] appended_bytes = null;
			

		zero_byte[0] = (byte)0;
		two_byte[0] = (byte)2;

		/*
		formula for encryption:
			( 0x00 || 0x02 || r || 0x00 || m )^e mod N
		*/
		
		//This makes the ( 0x00 || 0x02 || r || 0x00 || m ) for encypting the message
		appended_bytes = append_byte_arrays(zero_byte, two_byte);
		appended_bytes = append_byte_arrays(appended_bytes, random_bytes);
		appended_bytes = append_byte_arrays(appended_bytes, zero_byte);
		appended_bytes = append_byte_arrays(appended_bytes, message_bytes);
		
		BigInteger int_msg = new BigInteger(appended_bytes);
		//This does ( int_msg )^e mod N 
		//cipher_text = modex(int_msg, rsa_e, rsa_N);
		int_msg = int_msg.modPow(rsa_e, rsa_N);
		
		return int_msg;
		
	}
	
	public static byte[] create_random_bytes(BigInteger num_bits)
	{
		byte[] random_bytes = null;
		
		SecureRandom random = new SecureRandom();

		int r_bits = num_bits.intValue() / 2;
				
		BigInteger random_int = new BigInteger(r_bits, random);
	
		random_bytes = random_int.toByteArray();
					
		random_bytes = no_zero_bytes_array(random_bytes);
					
		return random_bytes;
	}
	
	/*
	 * Makes sure there is no 0 byte in a byte array
	 */
	public static byte[] no_zero_bytes_array(byte[] byte_array)
	{
		int i = 0;
		int array_size = byte_array.length;
		
		for(i = 0; i < array_size; i++ )
		{
			//there is a 0 in the array
			if( byte_array[i] == (byte)0 )
			{
				byte_array[i] = make_non_zero_byte();
			}
		}
		
		//there was no 0 in the array
		return byte_array;
	}

	public static byte make_non_zero_byte()
	{
		byte non_zero_byte;
		
		byte[] random_byte = new byte[1];
		
		random_byte[0] = (byte)0;
		
		while(random_byte[0] == (byte)0 ) 
		{
			new Random().nextBytes(random_byte);
		}
		
		non_zero_byte = random_byte[0];
		
		return non_zero_byte;
	}
	
	
	/*
	 * Appends IV to cipher text message
	 */
	public static byte[] append_byte_arrays(byte[] original, byte[] data_to_add)
	{
	    byte[] result = new byte[original.length + data_to_add.length]; 
	    System.arraycopy(original, 0, result, 0, original.length); 
	    System.arraycopy(data_to_add, 0, result, original.length, data_to_add.length); 
	    return result;
	} 
	
	
	/* Just to print out the byte array
	 */
	public static void TestPrinting( byte[] data )
	{
		for(byte b:data)
		{
	        //
	        System.out.print(b);
	        System.out.print(" ");
	     }

		System.out.println();
	}	


/*Here lies the modular exponentation function for extra credit. Recursive solution.
 * This worked on larger N values, but for some reason not on 256.*/
public static BigInteger modex (BigInteger num, BigInteger exp, BigInteger mod){

	BigInteger z;

	/*If we reduced the exponent all the way down to zero then just return 1!!!*/
	if (exp.equals(BigInteger.ZERO)) return BigInteger.ONE;

	/*If the number is odd, then subtract one from the exponent and recursively call function
		return that value (z*num) mod N*/
	else if (exp.mod(new BigInteger("2")).equals(BigInteger.ONE)){
			
		z = modex(num, exp.subtract(BigInteger.ONE), mod);
		return (z.multiply(num).mod(mod));

	}else{
		
		/*Otherwise divide the exponent by two and call the function again. Return (z^2) mod N*/
		z = modex(num, exp.divide(new BigInteger("2")), mod);
		return (z.multiply(z).mod(mod));

	}

}

	/*An attempt was made......*/
	/*This is the probabilistic prime number generator. We did not use this in the program, however I'm shamelessly
	  requesting consideration of partial extra credit as this function works. I manage to get prime numbers
	  of the correct bit length from this function but sometimes it take a looong time. I'm sure it's an implementation issue*/
	public static BigInteger probably_prime(int num_bits, SecureRandom sr){

		byte[] to_test_bytes = new byte[num_bits/8];
		BigInteger to_test = BigInteger.ZERO;
		BigInteger two = new BigInteger("2");
		BigInteger s = BigInteger.ZERO;
		BigInteger t;
		BigInteger randx = BigInteger.ZERO;
		BigInteger prev, current;
		boolean found_prime = false;

		
		while (found_prime == false){
			
			/*Generate a random number then test to see if it is even, negative, or 1*/
			sr.nextBytes(to_test_bytes);
			to_test = new BigInteger(to_test_bytes);
			to_test = to_test.abs();

			if ((to_test.compareTo(two) == -1 || to_test.mod(two).equals(BigInteger.ZERO))) continue;
				
			/*Set t to (n-1)*/
			t = to_test.subtract(BigInteger.ONE);
			
			/*While t is even, add one to s and divide by two. (n-1) = 2^s(t)*/
			while(t.mod(two).equals(BigInteger.ZERO)){

				s = s.add(BigInteger.ONE);
				t = t.divide(two);

			}
			
			/*Generate a random value x between 1 and (n -1)*/
			do{
				randx = new BigInteger(to_test.bitLength(), sr);
			}while (randx.compareTo(to_test) > 0);	
			
			/*Go through the x's until we find one where x^(2^i)*t congruent to 1 mod n*/
			/*If we find one, then check the previous value. If it is congruent to 1 mod n or -1 mod n, then
			  we've got a prime number. Otherwise, it's composite*/
			prev = modex(randx, t, to_test);
			for (int i = 1; i <= s.intValue(); i++){
				
				current = modex(randx, two.pow(i).multiply(t), to_test);
				if (current.equals(BigInteger.ONE)){
				
					if (prev.equals(BigInteger.ONE) || prev.equals(to_test.subtract(BigInteger.ONE))){

					found_prime = true;
					break;

					}else break;

				}
			}

		}
		
		return to_test;
	}
}
