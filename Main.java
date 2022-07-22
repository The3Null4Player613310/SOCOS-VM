//################################################################
//#SOCOS: Virtual Machine
//#Copyright © 2021  Nathan Munn
//#FULL COPYRIGHT NOTICE IS IN README
//################################################################
import java.util.Scanner;
import java.io.File;

class Main
{
	static int tapeSize = 8192;//2048;
	
	static char[] tapeA = new char[tapeSize];
	static char[] tapeB = new char[tapeSize];
	
	static int ptrA = 0, ptrB = 0, level = 0;
	
	static String input = "boot";
	static String path = (new File(".")).getAbsolutePath() + File.separator + "root";
	
	static Scanner inputScanner = new Scanner(System.in);
	
	public static void main(String[] args)
	{
		
		inputScanner.useDelimiter("");
		
		do
		{
			try
			{
				exec(input);
				//for(int i=0; i<tapeSize; i++)
				//	System.out.print(tapeB[i]);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			System.out.print(": ");
			input = inputScanner.nextLine();
		}while(!input.equalsIgnoreCase("exit"));
	}
	
	private static void exec(String fileName) throws Exception
	{
		File file = new File(path + File.separator + fileName + ".b");
		if(!file.exists())
			System.out.println("'" + fileName + "' not found");
		else
		{
			
			//load file to tapeA
			Scanner fileScanner = new Scanner(file);
			fileScanner.useDelimiter("");
			char curChar;
			ptrA=0;
			do
			{
				curChar = fileScanner.next().charAt(0);
				switch(curChar)
				{
					case ',':
					case '.':
					case '[':
					case ']':
					case '<':
					case '>':
					case '+':
					case '-':
						tapeA[ptrA] = curChar;
						ptrA++;
					break;
					default:
					break;
				}
				
			}while(fileScanner.hasNext());
			fileScanner.close();
			
			//wipe remaining space
			for(int i=ptrA; i<tapeSize; i++)
				tapeA[ptrA] = 0;
			
			//reset head
			ptrA=0;
			
			do 
			{
				switch(tapeA[ptrA])
				{
					case ',':
						tapeB[ptrB] = (char) inputScanner.next().charAt(0);
					break;
					case '.':
						System.out.print(tapeB[ptrB]);
					break;
					case '[':
						if(tapeB[ptrB] == 0)
							while((tapeA[ptrA] != ']') || (level > 0))
							{
								if(tapeA[ptrA]=='[') level++;
								ptrA++;
								if(tapeA[ptrA]==']') level--;
								if(ptrA == tapeSize) ptrA=0;
							}
					break;
					case ']':
						if(tapeB[ptrB] != 0)
							while((tapeA[ptrA] != '[') || (level > 0))
							{
								if(tapeA[ptrA]==']') level++;
								ptrA--;
								if(tapeA[ptrA]=='[') level--;
								if(ptrA == -1) ptrA=(tapeSize-1);
							}
					break;
					case '<':
						ptrB--;
						if(ptrB == -1)
							ptrB=(tapeSize-1);
					break;
					case '>':
						ptrB++;
						if(ptrB == tapeSize)
							ptrB=0;
					break;
					case '+':
						tapeB[ptrB]++;
						if(tapeB[ptrB] == 256)
							tapeB[ptrB] = 0;
					break;
					case '-':
						tapeB[ptrB]--;
						if(tapeB[ptrB] == -1)
							tapeB[ptrB] = 255;
					break;
					default:
					break;
				}
				ptrA++;
			}while(tapeA[ptrA] != 0);
		}	
	}
	
}