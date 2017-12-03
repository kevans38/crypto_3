import java.security.*;
import java.math.BigInteger;

public class rsa_sign{

	BigInteger encrypted_data;

	
	public rsa_sign (BigInteger[] key_data, String message){

		
		BigInteger message_hash = hash(message);
		encrypted_data = rsa_funcs.encrypt_data(key_data, message_hash);		

	}

	private BigInteger hash (String message){
		
		BigInteger B = BigInteger.ZERO;
	
		try{
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte [] hashed_val = md.digest(message.getBytes("UTF-8"));
			
		B = new BigInteger(hashed_val);
		
		}catch(Exception e){
			
			System.out.println("Error hashing");
			System.exit(1);

		}
		
		return B;
	}

	public static void main (String[] args) throws Exception{


		BigInteger[] key_data = null;
		String message;
		
		key_data = rsa_funcs.key_file(args);
		
		/*Not sure if we're hashing the content of the message or the file itself
		 for now, I'm just using the input_file function from the last lab and hashing
		 the resulting value of the contents*/
		message = rsa_funcs.input_file(args);
		rsa_sign R = new rsa_sign(key_data, message);
		String output_data = new String(R.encrypted_data.toString());
		rsa_funcs.output_file(args, output_data);



	}



}
