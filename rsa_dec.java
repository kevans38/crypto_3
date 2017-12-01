import java.math.*;
import java.util.*;

public class rsa_dec{

	BigInteger ciphertext;
	byte[] cipherbytes;
	BigInteger plaintext;
	BigInteger[] key;				/*0 = #bits; 1 = N; 2 = d*/
	

	/*Constructor just assigns the cipher text and key data to their proper class variables then calls decrypt*/
	public rsa_dec (BigInteger[] key, BigInteger ciphertext){
		this.key = key;
		this.ciphertext = ciphertext;
		decrypt();
	}
	
	
	/*Class function that will decrypt a message encrypted with padded RSA*/
	private void decrypt(){

		BigInteger original_msg;
		byte[] original_bytes;
		byte[] plain_bytes;	
		int x;


		/*Take the ciphertext and raise it by (c^d) mod N or put another way: ((m^e)^d) mod N*/
		//original_msg = rsa_funcs.modex(this.ciphertext,key[2],key[1]);
		original_msg = this.ciphertext;
		System.out.println("OG: " + original_msg);
		original_msg = original_msg.modPow(key[2], key[1]);
		
		System.out.println("OG: " + original_msg);
		original_bytes = original_msg.toByteArray();
		
		/*Go through the cipher text until we find the 0x00 byte*/	
		x = 0;
		while(true){
				if (original_bytes[x] == 0x00) break;
				x += 1;
		}
		
		/*The remaining bytes are the plaintext!!*/
		plain_bytes = Arrays.copyOfRange(original_bytes, (x + 1), original_bytes.length);
		this.plaintext = new BigInteger(plain_bytes);
		
		System.out.println("Ptxt: " + this.plaintext);		
		return;
		
	}

	public static void main(String[] args) throws Exception{

		BigInteger[] key_data = null;
		String input;
		BigInteger ciphertext;
		rsa_dec R;
		
		/*Read in the key data and the input file*/
		key_data =  rsa_funcs.key_file(args);
		input = rsa_funcs.input_file(args);
		ciphertext = new BigInteger(input);
		
		/*Call the RSA Decrypt constructor with key information and the ciphertext*/
		R = new rsa_dec(key_data, ciphertext);

		/*Convert the plaintext to a string before passing it to the output file function*/
		rsa_funcs.output_file(args, R.plaintext.toString());
		
	}


}
