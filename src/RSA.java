/*
Name: Hung Siew Kee
Student ID: 5986606

parallels@parallels-Parallels-Virtual-Platform:~$ java -versionopenjdk version "11.0.3" 2019-04-16OpenJDK Runtime Environment (build 11.0.3+7-Ubuntu-1ubuntu218.04.1)OpenJDK 64-Bit Server VM (build 11.0.3+7-Ubuntu-1ubuntu218.04.1, mixed mode, sharing)
*/

import java.math.BigInteger;
import java.security.SecureRandom;
import java.io.*;
import java.util.*;


public class RSA
{
	static BigInteger one = new BigInteger("1");
	
	public static void main (String args[]) throws Exception
	{
		int option = 0;
		do
		{
				Scanner console = new Scanner(System.in);
				System.out.println("\nPlease select the following menu options: " );
				System.out.print("\n1) Key generation \n2) Sign message \n3) Verify singature \n4) Encryption \n5) Decryption \n6) Exit\n\nChoice: ");
				option = console.nextInt();
				
				if (option == 1)
				{
					System.out.print("\nPlease enter bit length for key: ");
					int bit_len = console.nextInt();
					keyGeneration(bit_len);
					System.out.println("Keys have been generated!");
				}
				else if(option == 2)
				{
					BigInteger signature = digitalSign(false);
					System.out.println("Message has been signed and saved to text file!");
				}
				else if(option == 3)
				{
					BigInteger signed_msg = digitalSign_verify(false);
					System.out.println("Signed message: " + signed_msg.toString());
					
					//read msg.txt
					BigInteger msg = new BigInteger("0");
					File file = new File("msg.txt");
					try(BufferedReader br = new BufferedReader (new FileReader(file));)	
					{
						msg = new BigInteger(br.readLine());
					}
					catch (IOException a) 
					{
						System.err.format("IOException: %s%n", a);
						System.exit(-1);
					}
					
					if (signed_msg.equals(msg))
						System.out.println("Verification: True");
					else
						System.out.println("Verification: False");
				}	
				else if (option == 4)
				{
					//encryption with PK = verify signature
					BigInteger encrypted_msg = digitalSign_verify(true);
					System.out.println("Encrypted message: " + encrypted_msg.toString());
					
				}
				else if (option == 5)
				{
					//decryption with SK = digital signing
					BigInteger decrypted_msg = digitalSign(true);
					System.out.println("Decrypted message: " + decrypted_msg.toString());
				}
				
		}while(option != 6);	
	}	
	
	public static void keyGeneration(int bit_len)
	{
		
		//random p with requried bit length
		//random q with requried bit length
		
		//initialize variables
		BigInteger p = one;
		BigInteger q = one;
		
		while (p.equals(q))
		{
			SecureRandom secure_rand = new SecureRandom(); 
			p = BigInteger.probablePrime(bit_len, secure_rand);
			q = BigInteger.probablePrime(bit_len, secure_rand);
		}
		
		//n = p * q
		BigInteger n = p.multiply(q);
		
		//m = phi n = (p-1)(q-1)
		BigInteger m = (p.subtract(one)).multiply(q.subtract(one));
		
		//select e where gcd(e, m) = 1
		boolean gcd_one = false;
		int e_max = (int)(Math.pow(2, bit_len) / 2);
		int e = 0;
		BigInteger e_bigint = new BigInteger("0");
		
		do
		{
			do
			{
				SecureRandom secure_rand = new SecureRandom();
				e = secure_rand.nextInt(e_max) + 2;
				e_bigint = BigInteger.valueOf(e);
			}while(e_bigint.compareTo(m) == 1);

			BigInteger gcd = m.gcd(e_bigint);
			
			if (gcd.equals(one))
				gcd_one = true;	
		}while(gcd_one == false);
		
		
		//write file: pk = (n, e)
		String n_str = n.toString();
		String e_str = Long.toString(e);
		
		File file = new File("pk.txt");
		try(FileWriter write = new FileWriter(file, false);
		BufferedWriter bw = new BufferedWriter(write))
		{
			bw.write(n_str + "\n");
			bw.write(e_str);
		}
		catch (IOException a) 
		{
			System.err.format("IOException: %s%n", a);
			System.exit(-1);
		}
		
		//d = e-1 mod m	
		BigInteger d = e_bigint.modInverse(m);
		
		//write file: sk = (n, p, q, d)
		String p_str = p.toString();
		String q_str = q.toString();
		String d_str = d.toString();
		
		File file_2 = new File("sk.txt");
		try(FileWriter write = new FileWriter(file_2, false);
		BufferedWriter bw_2 = new BufferedWriter(write))
		{
			bw_2.write(n_str + "\n");
			bw_2.write(p_str + "\n");
			bw_2.write(q_str + "\n");
			bw_2.write(d_str);
		}
		catch (IOException b) 
		{
			System.err.format("IOException: %s%n", b);
			System.exit(-1);
		}
				
	}
		
	public static BigInteger digitalSign(boolean decrypt)
	{
		String fileName;
		if (!decrypt)
			fileName = "msg.txt";
		else
			fileName = "cipher.txt";
			
		//intialize
		BigInteger msg = one;
			
		//read msg.txt or cipher.txt
		File file = new File(fileName);
		try(BufferedReader br = new BufferedReader (new FileReader(file));)	
		{
			msg = new BigInteger(br.readLine());
		}
		catch (IOException b) 
		{
			System.err.format("IOException: %s%n", b);
		}
		
		//read sk.txt
		BigInteger d = new BigInteger("0");
		BigInteger n = new BigInteger("0");
		file = new File("sk.txt");
		try(BufferedReader br = new BufferedReader (new FileReader(file));)	
		{
			n = new BigInteger(br.readLine());
			br.readLine();
			br.readLine();
			d = new BigInteger(br.readLine());
		}
		catch (IOException b) 
		{
			System.err.format("IOException: %s%n", b);
		}
		
		BigInteger signed_message = msg.modPow(d, n);
		
		//write sig.txt
		File file_write = new File("sig.txt");
		try(FileWriter write = new FileWriter(file_write, false);
		BufferedWriter bw_2 = new BufferedWriter(write))
		{
			bw_2.write(signed_message.toString());
		}
		catch (IOException b) 
		{
			System.err.format("IOException: %s%n", b);
			System.exit(-1);
		}
		
		return signed_message;
		
	}
	
	
	public static BigInteger digitalSign_verify(boolean encrypt)
	{
		//intialize
		BigInteger msg = one;
		String fileName;
		
		if (!encrypt)
			fileName = "sig.txt";
		else
			fileName = "msg.txt";
			
		//read sig.txt or msg.txt
		File file = new File(fileName);
		try(BufferedReader br = new BufferedReader (new FileReader(file));)	
		{
			msg = new BigInteger(br.readLine());
		}
		catch (IOException b) 
		{
			System.err.format("IOException: %s%n", b);
		}
		
		//read pk.txt
		BigInteger n = new BigInteger("0");
		BigInteger e = new BigInteger("0");
		file = new File("pk.txt");
		try(BufferedReader br = new BufferedReader (new FileReader(file));)	
		{
			n = new BigInteger(br.readLine());
			e = new BigInteger(br.readLine());
		}
		catch (IOException b) 
		{
			System.err.format("IOException: %s%n", b);
			System.exit(-1);
		}

		BigInteger signed_msg = msg.modPow(e, n);
		
		//write cipher on cipher.txt
		if (encrypt)
		{
			File file_write = new File("cipher.txt");
			try(FileWriter write = new FileWriter(file_write, false);
			BufferedWriter bw_2 = new BufferedWriter(write))
			{
				bw_2.write(signed_msg.toString());
			}
			catch (IOException b) 
			{
				System.err.format("IOException: %s%n", b);
				System.exit(-1);
			}
		}
		
		return signed_msg;
	}
		
		

}




