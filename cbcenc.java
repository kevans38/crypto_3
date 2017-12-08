public class cbcenc
{
	static int BIT_BLOCK_SIZE = 128;
	//need to divide by 8 because it will be in BYTES
	// so 128 bits / 8 = 16 bytes
	static int BLOCK_SIZE = BIT_BLOCK_SIZE/8;
	
	byte [] cipher_text;
	byte [] input_data;

	public cbcenc (byte[] key_data, String filename){

		byte[] iv_data;
		byte[][] input_blocks;
		byte[] temp_data = null;

		int input_size;
		try{
			input_data = ctfuncs.read_file(filename);
			input_size = input_data.length;

			iv_data = ctfuncs.IVgen();

			input_blocks = ctfuncs.make_blocks(input_data, input_size);
			input_blocks = ctfuncs.padding(input_blocks, input_size);

			cipher_text = new byte [(input_blocks.length+1)*BLOCK_SIZE];

			cipher_text = ctfuncs.append_bytes(cipher_text, iv_data, 0);
		
			for ( int i = 0; i < input_blocks.length; i++){

				if (i == 0){

					temp_data = ctfuncs.xor_bytes(input_blocks[i], iv_data);
				}else{
				
					temp_data = ctfuncs.xor_bytes(input_blocks[i], temp_data);

				}
			
				temp_data = ctfuncs.encrypt_data(temp_data, key_data);
				cipher_text = ctfuncs.append_bytes(cipher_text, temp_data, i+1);

			}

		} catch (Exception e){

			System.out.println("Error");
			System.exit(1);
		

		}

	}

	public static void main(String[] args) throws Exception
	{
		//use test_printing(byte[]) to print out a byte array
		byte[] key_data;
		byte[] input_data;
		byte[] iv_data;
		
		//open files from cmd line args
		key_data = ctfuncs.key_file(args);
		input_data = ctfuncs.input_file(args);
		
		//create encrypted file
		//ctfuncs.output_file(args, cipher_text);
	}
}
