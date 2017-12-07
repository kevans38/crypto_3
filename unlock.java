import java.io.File;
import java.security.*;
import java.math.BigInteger;

public class unlock{

	
	public unlock (String path, String pubkey, String privkey){
	
		/*Create the AES key that will be used for the symmetric key manifest*/	
		BigInteger[] key_data = null;
		byte[] aes_key;
		String manifestpath = path +"/manifest";

	
		try{
			
			/*Encrypt the AES key with action public key*/
			key_data = rsa_funcs.read_integer_file(pubkey);
			aes_key = ctfuncs.read_hex_file(manifestpath);
			System.out.print(javax.xml.bind.DatatypeConverter.printHexBinary(aes_key));
		
	
		}catch (Exception e){

			System.out.println("Error Generating and Encrypting AES key");
			System.exit(1);

		}

		
/*
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
*/
	}

	public static void main (String[] args) {

		/*Verify Integrity of Ununlocking party's pub. key info*/

		unlock L = new unlock (args[0], args[1], args[2]);
			
			
		return;
	}



}
