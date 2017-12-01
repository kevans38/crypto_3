import java.math.BigInteger;
import java.security.*;
public class rsa_validate{



	public static void main (String [] args) throws Exception{

		BigInteger[] key_data = null;
		BigInteger B = BigInteger.ZERO;
		BigInteger S = BigInteger.ZERO;
		String message;
		String signature;

		key_data = rsa_funcs.key_file(args);
		message = rsa_funcs.input_file(args);
		signature = rsa_funcs.sig_file(args);
		
		S = new BigInteger(signature);
		
		try{


			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte [] hashed_val = md.digest(message.getBytes("UTF-8"));
			
			B = new BigInteger(hashed_val);



		} catch (Exception e){


			System.out.println("Error hashing");
			System.exit(1);

		}
		

		
		rsa_dec R = new rsa_dec(key_data, S);
		
		System.out.println("B: " + B/*.mod(key_data[1])*/);
		System.out.println("R: " + R.plaintext);	
		if (R.plaintext.equals(B.mod(key_data[1]))){

			System.out.println("True");
		}else{

			System.out.println("False");

		}
	}


}
