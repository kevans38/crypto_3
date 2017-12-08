//validates a tag for a given file and tag
import java.util.Arrays;


public class cbcmac_validate {
	static int BIT_BLOCK_SIZE = 128;
	//need to divide by 8 because it will be in BYTES
	// so 128 bits / 8 = 16 bytes
	static int BLOCK_SIZE = BIT_BLOCK_SIZE/8;
	boolean validate;
	
	public cbcmac_validate (byte[] key_data, byte[] input_data, byte[] intag){

		byte[] iv_data;
		byte[] tag = null;
		byte[] message_length = new byte[BLOCK_SIZE];
		byte[][] input_blocks;
		byte[] cipher_text;
		byte[] temp_data = null;
		
		int input_size = input_data.length;

				
		Arrays.fill(message_length, (byte)0);
		message_length[0] = (byte)input_data.length;
		iv_data = message_length;// first iv is message length
		
		try{		
		//create blocks and pad - using 128 bit blocks
		input_blocks = ctfuncs.make_blocks(input_data, input_size);
		
		input_blocks = ctfuncs.padding(input_blocks, input_size);
		
		//+1 for the IV at the front
		cipher_text = new byte[(input_blocks.length+1)*BLOCK_SIZE];
		
		cipher_text = ctfuncs.append_bytes(cipher_text, iv_data, 0);
		
		//XOR Message: 1st with IV, then remaining with ciphertext
		for ( int i = 0; i < input_blocks.length; i++ )
		{
			if ( i == 0 )
			{
				temp_data = ctfuncs.xor_bytes(input_blocks[i], iv_data);
			}
			else
			{
				temp_data = ctfuncs.xor_bytes(input_blocks[i], temp_data);
			}
			
			temp_data = ctfuncs.encrypt_data(temp_data, key_data);
			

		}
		tag = temp_data;
		if(Arrays.equals(tag, intag)){
			
			validate = true;
		}else validate = false;
		
		}catch (Exception e){

			System.out.println("There was an error in cbcmac_validate");
			System.exit(1);

		}
	}

	public static void main(String[] args) throws Exception
	{
		boolean validate = false;
		//use test_printing(byte[]) to print out a byte array
		byte[] key_data;
		byte[] input_data;
		byte[] intag;
			
		//open files from cmd line args
		key_data = ctfuncs.key_file(args);
		input_data = ctfuncs.input_file_msg(args);
		
		//get tag from file to compare later
		intag = ctfuncs.input_file_intag(args);
		
		//iv_data = ctfuncs.iv_file(args);

				
		//ctfuncs.test_printing(tag);
		//ctfuncs.test_printing(intag);
		
		cbcmac_validate C = new cbcmac_validate(key_data, input_data, intag);	
		
		//Prints True or False for validation
		System.out.println(C.validate);
	}
}
