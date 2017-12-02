import java.math.BigInteger;
import java.util.Arrays;


public class cbcenc
{
	static int BIT_BLOCK_SIZE = 128;
	//need to divide by 8 because it will be in BYTES
	// so 128 bits / 8 = 16 bytes
	static int BLOCK_SIZE = BIT_BLOCK_SIZE/8;
	
	byte[] cipher_text;
	byte[] key_data;
	byte[] input_data;
	byte[] iv_data;
	
	public cbcenc(byte[] key_data, byte[] input_data, byte[] iv_data){
		this.key_data = key_data;
		this.input_data = input_data;
		this.iv_data = iv_data;
		encrypt();
	}
	
	public void encrypt(){
		byte[][] input_blocks;
		int input_size = input_data.length;
		byte[] temp_data = null;
		
		//create blocks and pad - using 128 bit blocks
		input_blocks = ctfuncs.make_blocks(input_data, input_size);
		
		input_blocks = ctfuncs.padding(input_blocks, input_size);
		
		//+1 for the IV at the front
		cipher_text = new byte[(input_blocks.length+1)*BLOCK_SIZE];
		
		//---------------must have iv_data set, 1st is 0's , then is output of each 
		cipher_text = ctfuncs.append_bytes(cipher_text, this.iv_data, 0);
		
		//XOR Message: 1st with IV, then remaining with ciphertext
		for ( int i = 0; i < input_blocks.length; i++ )
		{
			if ( i == 0 )
			{
				temp_data = ctfuncs.xor_bytes(input_blocks[i], this.iv_data);
			}
			else
			{
				temp_data = ctfuncs.xor_bytes(input_blocks[i], temp_data);
			}
			
			//YELLING AT THIS STATEMENT----------------------------
			temp_data = ctfuncs.encrypt_data(temp_data, this.key_data);
			
			cipher_text = ctfuncs.append_bytes(cipher_text, temp_data, i+1);

		}

	}
	
	
	public static void main(String[] args) throws Exception
	{
		//use test_printing(byte[]) to print out a byte array

		byte[] key;
		byte[] input;
		byte[] iv = new byte[100];	//should be n bytes long 0n, length of message
		cbcenc C;
		
		//open files from cmd line args
		key = ctfuncs.key_file(args);
		input = ctfuncs.input_file(args);
			
		//fill array to 0n to start
		Arrays.fill(iv, (byte)0);
		
		//call the cbc constructor with key, input, and starting iv value
		C = new cbcenc(key, input, iv);
		
		//create encrypted file
		//ctfuncs.output_file(args, cipher_text );
	}
}
