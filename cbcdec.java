public class cbcdec{

	static int BIT_BLOCK_SIZE = 128;
	//need to divide by 8 because it will be in BYTES
	// so 128 bits / 8 = 16 bytes
	static int BLOCK_SIZE = BIT_BLOCK_SIZE/8;//16

	byte[] message;

	public cbcdec (byte[] key_data, byte[] cipher_text){


		byte[] msg;
		byte[] after_xor;
		byte[] prev_block = null;
		byte[][] cipher_blocks;
		byte[] temp_data = null;

		int cipher_size = cipher_text.length;
		int padding_length = 0;
		int msg_size;

		/*Break message apart into blocks of 128 bits*/
		cipher_blocks = ctfuncs.make_b(cipher_text, cipher_size);
		msg = new byte[cipher_size];

		try{

		//Send IV and cipherblocks through AES, XOR with prev block
		for ( int i = 1; i < (cipher_size/BLOCK_SIZE); i++ ){
			after_xor = null;

			//if last block, take off padding
			if(i == (cipher_size/BLOCK_SIZE)-1) { 
				temp_data = ctfuncs.decrypt_data(cipher_blocks[i], key_data);
				prev_block = cipher_blocks[i-1];

				after_xor = ctfuncs.xor_bytes(prev_block, temp_data);


				padding_length = ctfuncs.padding_length(after_xor);

				byte [] unpadded = ctfuncs.unpadding(after_xor);

				ctfuncs.append_bytes(msg, unpadded, i-1);



			}
			else {
				temp_data = ctfuncs.decrypt_data(cipher_blocks[i], key_data);
				prev_block = cipher_blocks[i-1];//first one is IV

				after_xor = ctfuncs.xor_bytes(prev_block, temp_data);

				ctfuncs.append_bytes(msg, after_xor, i-1);
			}
		}	

		} catch (Exception e){


			System.out.println("Error decrypting the directory. I'm freaking out here");
			System.exit(1);

		}
		//have to remove BLOCK_SIZE since that is the IV
		msg_size = cipher_size-padding_length-BLOCK_SIZE;

		message = new byte[msg_size];

		System.arraycopy( msg, 0, message, 0, msg_size );


	}

	public static void main(String[] args) throws Exception{

		byte[] key_data;
		byte[] cipher_text;

		//open files from cmd line args
		key_data = ctfuncs.key_file(args);
		cipher_text = ctfuncs.input_file(args);

		cbcdec C = new cbcdec (key_data, cipher_text);

		//create decrypted file
		ctfuncs.output_file(args, C.message);

	}
}
