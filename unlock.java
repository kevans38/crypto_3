import java.io.File;
import java.security.*;
import java.math.BigInteger;

public class unlock{

	
	public unlock (String path, String pubkey, String privkey){
	
		/*Create the AES key that will be used for the symmetric key manifest*/	
		BigInteger[] key_data = null;
		byte[] aes_key = null;
		byte[] input_file;
		byte[] intag;
		String manifestpath = path +"/manifest";
		String mpsig = path +"/manifest.sig";
	
		try{
			
			/*Encrypt the AES key with action public key*/
			key_data = rsa_funcs.read_integer_file(pubkey);
			aes_key = ctfuncs.read_hex_file(manifestpath);
			System.out.print(javax.xml.bind.DatatypeConverter.printHexBinary(aes_key));
		
	
		}catch (Exception e){

			System.out.println("Error Generating and Encrypting AES key");
			System.exit(1);

		}

		

		try{
			File dir = new File (path);
			File [] files = dir.listFiles();

			for (int i = 0; i < files.length; i++){
				
				/*This awful if statement ensures that we're not checking the cbcmac-tag of the manifest, the manifest signatures,
				  or any of the tags themselves. When we've got a file, check it's tag.*/
				if (!files[i].toString().equals(manifestpath) && !files[i].toString().equals(mpsig) && !files[i].toString().endsWith(".tag")){
					input_file = ctfuncs.read_file(files[i].toString());
					String tagpath = files[i].toString() + ".tag";
					intag = ctfuncs.read_file(tagpath);
					cbcmac_validate C = new cbcmac_validate(aes_key, input_file, intag);
					
					if (C.validate) System.out.println("Verified " + files[i].toString());
					else { System.out.println("Tag error on file: " + files[i].toString()); System.exit(1); }

				}					

			}

		}catch (Exception e){ 

			System.out.println("Error encrypting files");
			System.exit(1);

		}

	}

	public static void main (String[] args) {

		/*Verify Integrity of Ununlocking party's pub. key info*/

		unlock L = new unlock (args[0], args[1], args[2]);
			
			
		return;
	}



}
