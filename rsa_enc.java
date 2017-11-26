//import java.io.*;
import java.math.*;

public class rsa_enc {
	
	public static void main(String[] args) throws Exception{
		
		BigInteger[] key_data = null;
		String input_data;
		BigInteger encrypted_rsa;
		
		/* key_file function returns an array that contains the 3 values we need in the key file */
		key_data = rsa_funcs.key_file(args);
		input_data = rsa_funcs.input_file(args);
		
		/* This converts a String to a Big Integer*/
		BigInteger rsa_public_key = new BigInteger(input_data);
		
			
		encrypted_rsa = rsa_funcs.encrypt_data(key_data, rsa_public_key);
				
		/* This converts a Big Integer to a string */
		String output_data = new String(encrypted_rsa.toString());
		rsa_funcs.output_file(args, output_data);
		
	}
	
	
}
