package Clustering;

import java.util.ArrayList;

public class DataPoint
{
	private ArrayList<Double> data;
	private String lable;
	private int clusterID;
	private boolean inCluster;
	public DataPoint(double [] in, String l)
	{
		data = new ArrayList <Double> ();
		for (int i = 0; i < in.length; i++)
		{
			data.add(in[i]);
		}
		inCluster = false;
		this.lable = l;
	}
	public double euclideanDistance (DataPoint p2)
	{
		double distance = 0;
		if (this.size()!=p2.size())
		{
			printDataPoint();
			p2.printDataPoint();
			System.out.println ("Incompatible Data Sets: p1: " + this.size() + " p2: " + p2.size());
			return -1;
		}
		for (int i = 0; i < data.size(); i++)
		{
			double temp = Math.abs(this.get(i) - p2.get(i));
			Math.pow(temp, 2);
			distance += temp;
		}
		distance = Math.sqrt(Math.abs(distance));
		//System.out.println("DISTANCE: " + distance);
		return distance;
	}
	
	public int getClusterID ()
	{
		return clusterID;
	}
	
	public void setClusterID(int id)
	{
		this.clusterID=id;
	}
	
	public void setFlag(boolean t)
	{
		inCluster = t;
	}
	public boolean checkFlag ()
	{
		return inCluster;
	}
	public int size ()
	{
		return data.size();
	}
	public double get (int i)
	{
		return data.get(i);
	}
	public boolean samePoint (DataPoint p)
	{
		for (int i = 0; i < this.size(); i++)
			{
				if (this.get(i) != p.get(i)) return false;
			}
		return true;
	}
	public String getLable ()
	{
		return this.lable;
	}
	public void printDataPoint ()
	{
		System.out.print(this.lable + " ");
		for (int i = 0; i < data.size(); i++)
		{
			System.out.print(data.get(i) + " ");
		}
		System.out.println("");
	}
}