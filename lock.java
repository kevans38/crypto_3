import java.security.*;
import java.math.BigInteger;

public class lock{

	
	public lock (String path){
		
		BigInteger[] key_data = null;
		byte aes_encryption[] = aes_keygen(256);
		BigInteger ctext = BigInteger.ZERO;
				
		try{

			key_data = rsa_funcs.read_integer_file(path);
			System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(aes_encryption));
			ctext = rsa_funcs.encrypt_data(key_data, new BigInteger(aes_encryption));
			ctfuncs.write_file("manifest", ctext.toByteArray());
	
		}catch (Exception e){

			System.out.println("Error");
			System.exit(1);

		}
	}

	/*Generate random AES key for encryption and tagging*/
	private byte [] aes_keygen (int size){

		SecureRandom random  = new SecureRandom();
		byte bytes[] = new byte[size/8];
		random.nextBytes(bytes);

		return bytes;
	}

	public static void main (String[] args) {

		/*Verify Integrity of Unlocking party's pub. key info*/

		lock L = new lock (args[0]);
			
			
		return;
	}



}
