/*
import javax.crypto.*;
import javax.crypto.spec.*;
*/
import java.security.*;
import java.io.*;
import java.math.*;
import java.util.*;

public class unlock_lock_funcs
{

	private static BufferedReader bufferedReader;
	
	/*
	 * Gets the directory name from command line with -d command
	 */
	public static String directory_name( String[] args ) throws Exception
	{
		String input_data = null;
				
		for ( int i = 0; i < args.length; i++ )
		{
			//-d <directtory name>:  required, specifies a directory name
			if ( args[i].equals("-d") )
			{
				i++;
				input_data = args[i+1];
				break;
			}

		}

		if (input_data == null)
		{
			System.err.println("No directory was found. Need to exit");
			System.exit(0);
		}
		
		return input_data;		
	}

	/*
	 * Gets key file from command line with -p command
	 * This returns and array for the 3 values in the key file
	 */
	public static BigInteger[] pub_key_file( String[] args ) throws Exception
	{
		BigInteger key_data[] = null;
				
		for ( int i = 0; i < args.length; i++ )
		{
			//-p <key file>:  required, specifies a file storing a valid AES key as a hex encoded string
			if ( args[i].equals("-p") )
			{
				i++;
				
				key_data = read_integer_file(args[i]);

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
	 * Gets key file from command line with -r command
	 * This returns and array for the 3 values in the key file
	 */
	public static BigInteger[] priv_key_file( String[] args ) throws Exception
	{
		BigInteger key_data[] = null;
				
		for ( int i = 0; i < args.length; i++ )
		{
			//-r <key file>:  required, specifies a file storing a valid AES key as a hex encoded string
			if ( args[i].equals("-r") )
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
	 * Gets key file from command line with -vk command
	 * This returns and array for the 3 values in the key file
	 */
	public static BigInteger[] val_pub_key_file( String[] args ) throws Exception
	{
		BigInteger key_data[] = null;
				
		for ( int i = 0; i < args.length; i++ )
		{
			//-vk <key file>:  required, specifies a file storing a valid AES key as a hex encoded string
			if ( args[i].equals("-vk") )
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
	
	/*Generate random AES key for encryption and tagging*/
        public static byte [] aes_keygen (int size){

                SecureRandom random  = new SecureRandom();
                byte bytes[] = new byte[size/8];
                random.nextBytes(bytes);

                return bytes;
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

}
