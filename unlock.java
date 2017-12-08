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
			key_data = rsa_funcs.read_integer_file(privkey);
			/*aes_key*/ String test = unlock_lock_funcs.read_string_file(manifestpath);
			//System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(aes_key));
			unlock_lock_funcs.manifest_aes_key(key_data, test);
	
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
					
					File this_tag = new File(tagpath);
					this_tag.delete();
					C = null;
				}


			}

			System.out.println("All encrypted file tags verified. Decrypting Now!!!");					
			dir = new File(path);
			files = dir.listFiles();

			for (int i = 0; i < files.length; i++){

				if (!files[i].toString().equals(manifestpath) && !files[i].toString().equals(mpsig)){

					input_file = ctfuncs.read_file(files[i].toString());
					cbcdec D = new cbcdec(aes_key, input_file);
					ctfuncs.write_file(files[i].toString(), D.message);

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
