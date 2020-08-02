package Clustering;

import java.util.Scanner;

public class Control {
	public static void main (String args[])
	{
		/*if (args.length != 1)
		{
			System.out.println("Usage: java Control <name of dataset>");
			return;
		}*/
		//else
		//{
			System.out.println ("1. for Single-Linkage");
			System.out.println ("2. for Average-Linkage");
			System.out.println ("3. for Lloyd Method");
			Scanner scan = new Scanner (System.in);
			int num = scan.nextInt();
			switch (num)
			{
				case 1:
					System.out.println("Enter a value for k: the number of clusters you would like to end with");
					num = scan.nextInt();
					new SingleLinkage ("src/Clustering/iris.data", num);
					System.out.println("Continue to Average-Linkage? (1 = Y, 2 = N)");
					num = scan.nextInt();
					if (num == 2) break;
				case 2:
					System.out.println("Enter a value for k: the number of clusters you would like to end with");
					num = scan.nextInt();
					new AverageLinkage ("src/Clustering/iris.data", num);
					System.out.println("Continue to Lloyd's Method? (1 = Y, 2 = N)");
					num = scan.nextInt();
					if (num == 2) break;
				case 3:
					System.out.println("Enter a value for k: the number of clusters you would like to end with");
					num = scan.nextInt();
					new LloydMethod ("src/Clustering/iris.data", num);
					System.out.println("Continue to Next Method? (1 = Y, 2 = N)");
					num = scan.nextInt();
					if (num == 2) break;
			}
			scan.close();
		//}
	}
}
