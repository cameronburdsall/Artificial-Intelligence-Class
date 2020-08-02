package Clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
public class LloydMethod {
	ArrayList <Cluster> clusters;
	ArrayList <DataPoint> dataPoints;
	boolean convergence;
	public LloydMethod (String file, int k)
	{
		convergence = false;
		clusters = new ArrayList <Cluster> ();
		dataPoints = new ArrayList <DataPoint> ();
		this.populateDataPoints(file);
		this.selectInitialClusters(k);
		this.setClusterCenters();
		System.out.println("INITIAL CLUSTERS");
		System.out.println ("---------------------------");
		this.printClusters();
		while (true)
		{
			for (int i = 0; i < dataPoints.size(); i++)
			{
				DataPoint temp = dataPoints.get(i);
				if (temp.checkFlag() == false)
				{
					int index = findClosestCluster (temp);
					Cluster c = clusters.get(index);
					if (index == temp.getClusterID()) //if it was here before, do not set changed flag
					{
						temp.setFlag(true);
						temp.setClusterID(index);
						c.add(temp);
						clusters.set(index, c);
						continue;
					}
					//sets changed flag if data changed cluster
					temp.setFlag(true);
					temp.setClusterID(index);
					c.add(temp);
					c.setChanged(true);
					clusters.set(index, c);
				}
			}
			this.setClusterCenters();
			this.setFlagsFalse();
			this.convergenceCheck(); //checks if any of the clusters are changed, sets flag if not
			if (convergence) break;
			this.resetChanged();
			this.clearClusters();
		}
		System.out.println("FINAL CLUSTERS");
		System.out.println ("---------------------------");
		this.printClusters();
	}
	public void convergenceCheck()
	{
		for (int i = 0; i < clusters.size(); i++)
		{
			if (clusters.get(i).getChanged()) return;
		}
		this.convergence = true;
	}
	public void resetChanged ()
	{
		for (int i = 0; i < clusters.size(); i++)
		{
			Cluster temp = clusters.get(i);
			temp.setChanged(false);
			clusters.set(i, temp);
		}
	}
	public int findClosestCluster (DataPoint p)
	{
		//returns the index of the closest cluster based on the clusters center
		int index = -1;
		double distance = Double.MAX_VALUE;
		for (int i = 0; i < clusters.size(); i++)
		{
			double d = p.euclideanDistance(clusters.get(i).getCenter());
			if (d < distance)
			{
				distance = d;
				index = i;
			}
		}
		
		return index;
	}
	
	public void setClusterCenters ()
	{
		for (int i = 0; i < clusters.size(); i++)
		{
			Cluster temp = clusters.get(i);
			temp.setCenter();
			clusters.set(i, temp);
		}
	}
	public void clearClusters ()
	{
		for (int i = 0; i < clusters.size(); i++)
		{
			Cluster temp = clusters.get(i);
			temp.clearDataPoints();
			clusters.set(i, temp);
		}
	}
	
	public void setFlagsFalse ()
	{
		for (int i = 0; i < dataPoints.size(); i++)
		{
			DataPoint temp = dataPoints.get(i);
			temp.setFlag(false);
			dataPoints.set(i, temp);
		}
	}
	
	public void selectInitialClusters (int k)
	{
		Random rand = new Random();
		for (int i = 0; i < k; i++)
		{
			int r = rand.nextInt(dataPoints.size());
			if (dataPoints.get(r).checkFlag() == false) //makes sure data point has not been selected already
			{
				DataPoint temp = dataPoints.get(r);
				temp.setFlag(true);
				temp.setClusterID(r);
				dataPoints.set(r, temp);
				clusters.add(new Cluster(temp));
			}
		}
	}
	
	public void populateDataPoints (String file)
	{
		//this method will read in any comma separated file and extract the values as well as labels from each data point
		//then saves every data point in an array list
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
		        	catch (NumberFormatException e) //captures the label when it is detected
		        	{
		        		label = tokens[i];
		        	}
		        }
		        if (values.length == 0) return;
		        dataPoints.add(new DataPoint (values, label)); //creates array list of every data point
		    }
		}
		catch (IOException e) {
		    System.out.println("File Read Error");
		}
	}
	public void printClusters ()
	{
		for (int i = 0; i < clusters.size(); i++)
		{
			System.out.println("CLUSTER " + (i+1));
			clusters.get(i).printCluster();
		}
	}
}
