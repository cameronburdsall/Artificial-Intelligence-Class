package Clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AverageLinkage {
	ArrayList <Cluster> clusters;
	public AverageLinkage (String file, int k)
	{
		clusters = new ArrayList <Cluster> ();
		this.populateClusters(file);
		System.out.println("INITIAL CLUSTERS");
		System.out.println ("---------------------------");
		this.printClusters();
		if (k > clusters.size()) {
			System.out.println("Invalid Parameters");
			return;
		}
		while (clusters.size() > k)
		{
			findClosestClusters();
		}
		System.out.println("FINAL CLUSTERS");
		System.out.println ("---------------------------");
		this.printClusters();
	}
	public void findClosestClusters ()
	{
		//needs to iterate through all clusters and find the two with the shortest distance to each other
		//will then merge the two together
		int k = clusters.size(), p = clusters.size();
		double distance = Double.MAX_VALUE;
		for (int i = 0; i < clusters.size(); i++)
		{
			Cluster temp = clusters.get(i);
			for (int j = i + 1; j < clusters.size(); j++)
			{
				Cluster temp2 = clusters.get(j);
				temp.setCenter();
				temp2.setCenter();
				DataPoint p1 = temp.getCenter();
				DataPoint p2 = temp2.getCenter();
				double d = p1.euclideanDistance(p2);
				if (distance > d && d > 0)
				{
					distance = d;
					k = i;
					p = j;
				}
			}
		}
		//these lines of code merge clusters k and p into k cluster and then removes p from the ArrayList
		Cluster temp = clusters.get(k);
		temp.mergeCluster(clusters.get(p));
		clusters.set(k, temp);
		clusters.remove(p);
	}
	
	public void printClusters ()
	{
		for (int i = 0; i < clusters.size(); i++)
		{
			System.out.println("CLUSTER " + (i+1));
			clusters.get(i).printCluster();
		}
	}
	
	public void populateClusters (String file)
	{
		//this method will read in any comma separated file and extract the values as well as labels from each data point
		//then assigns each datapoint to its own cluster to prepare for average linkage, this is the same as single linkage in terms of setup
		try(BufferedReader in = new BufferedReader(new FileReader(file))) {
		    String str;
		    double [] values;
		    String label = "";
		    while ((str = in.readLine()) != null) {
		        String [] tokens = str.split(",");
		        values = new double [tokens.length - 1];
		        int ii = 0;
		        for (int i = 0; i < tokens.length; i++)
		        {
		        	try { //grabs the double values from the tokens
		        		values [ii] = Double.parseDouble(tokens[i]);
		        		ii++;
		        	}
		        	catch (NumberFormatException e) //captures the lable when it detects
		        	{
		        		label = tokens[i];
		        	}
		        }
		        if (values.length == 0) return;
		        clusters.add(new Cluster(new DataPoint (values, label))); //creates a cluster for each data point to start off
		    }
		}
		catch (IOException e) {
		    System.out.println("File Read Error");
		}
	}
}
