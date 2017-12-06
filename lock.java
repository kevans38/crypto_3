import java.security.*;
import java.math.BigInteger;

public class lock{

	
	public lock (String path){
		
		BigInteger[] key_data = null;
		byte bytes[] = keygen(256);
		BigInteger ctext = BigInteger.ZERO;
				
		try{
		

			key_data = rsa_funcs.read_integer_file(path);
			System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(bytes));
			//ctext = rsa_funcs.encrypt_data(key_data, new BigInteger(bytes));
//			ctfuncs.write_file("manifest", javax.xml.bind.DatatypeConverter.printHexBinary(bytes));
	
		}catch (Exception e){

			System.out.println("Error");
			System.exit(1);

		}
	}

	/*Generate random AES key for encryption and tagging*/
	private byte [] keygen (int size){

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
