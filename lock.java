import java.io.File;
import java.security.*;
import java.math.BigInteger;

public class lock{

	
	public lock (String path, String pubkey){
	
		/*Create the AES key that will be used for the symmetric key manifest*/	
		BigInteger[] key_data = null;
		byte aes_encryption[] = aes_keygen(256);
		BigInteger ctext = BigInteger.ZERO;
				
		try{
			
			/*Encrypt the AES key with action public key*/
			key_data = rsa_funcs.read_integer_file(pubkey);
			System.out.print(javax.xml.bind.DatatypeConverter.printHexBinary(aes_encryption));
			BigInteger i = new BigInteger(aes_encryption);

			/*This currently does not encrypt. It just puts hex AES key in manifest file plainly!!!!*/
			
			//ctext = rsa_funcs.encrypt_data(key_data, new BigInteger(aes_encryption));
			rsa_funcs.write_file("manifest", javax.xml.bind.DatatypeConverter.printHexBinary(aes_encryption));

	
		}catch (Exception e){

			System.out.println("Error Generating and Encrypting AES key");
			System.exit(1);

		}

		

		try{
			File dir = new File (path);
			File [] files = dir.listFiles();

			for (int i = 0; i < files.length; i++){

				if (files[i].isFile()){
				
					cbcenc C = new cbcenc(aes_encryption, files[i].toString());
					ctfuncs.write_file(files[i].toString(), C.cipher_text);
					cbcmac_tag T = new cbcmac_tag(aes_encryption, C.cipher_text);
					
					String tagname = files[i].toString() + ".tag";
					ctfuncs.write_file(tagname, T.tag);
					C = null;
					T = null;
				}

			}

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
