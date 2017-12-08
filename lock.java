import java.io.File;
import java.security.*;
import java.math.BigInteger;

public class lock{

	
	public lock (String path, String pubkey){
	
		/*Create the AES key that will be used for the symmetric key manifest*/	
		BigInteger[] key_data = null;
		byte aes_encryption[] = aes_keygen(256);
		BigInteger ctext = BigInteger.ZERO;
		BigInteger idiot = BigInteger.ZERO;				
		try{
			
			/*Encrypt the AES key with action public key*/
			key_data = rsa_funcs.read_integer_file(pubkey);
			System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(aes_encryption));
			idiot = new BigInteger(1, aes_encryption);
			/*This currently does not encrypt. It just puts hex AES key in manifest file plainly!!!!*/
		
				
		}catch (Exception e){

			System.out.println("Error Generating and Encrypting AES key");
			System.exit(1);

		}

		

		try{
			File dir = new File (path);
			File [] files = dir.listFiles();

			for (int i = 0; i < files.length; i++){

				if (!files[i].toString().endsWith(".tag")){
				
					cbcenc C = new cbcenc(aes_encryption, files[i].toString());
					ctfuncs.write_file(files[i].toString(), C.cipher_text);
					cbcmac_tag T = new cbcmac_tag(aes_encryption, C.cipher_text);
					
					String tagname = files[i].toString() + ".tag";
					ctfuncs.write_file(tagname, T.tag);
					C = null;
					T = null;
				}

			}
			String manifestpath = path + "/manifest";	
			
			ctext = rsa_funcs.encrypt_data(key_data, idiot);
			//rsa_funcs.write_file(manifestpath, javax.xml.bind.DatatypeConverter.printHexBinary(aes_encryption));
			unlock_lock_funcs.write_string_to_file(manifestpath, ctext.toString());

		}catch (Exception e){ 

			System.out.println("Error encrypting files");
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

		lock L = new lock (args[0], args[1]);
			
			
		return;
	}



}
