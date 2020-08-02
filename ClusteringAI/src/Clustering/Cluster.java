package Clustering;

import java.util.ArrayList;

public class Cluster {
	private ArrayList <DataPoint> dataPoints;
	private DataPoint center;
	private boolean changed;
	//	CONSTRUCTORS
	/*public Cluster ()
	{
		dataPoints = new ArrayList <DataPoint> ();
	}*/
	
	public Cluster (DataPoint dp)
	{
		dataPoints = new ArrayList <DataPoint> ();
		//dp.setFlag();
		dataPoints.add(dp);
		center = dp;
		changed = false;
	}
	
	public void setChanged (boolean t)
	{
		this.changed = t;
	}
	public boolean getChanged ()
	{
		return this.changed;
	}
	
	public void clearDataPoints ()
	{
		//clears data points, keeps center
		dataPoints.clear();
	}
	
	public void mergeCluster (Cluster c)
	{
		for (int i = 0; i < c.size(); i++)
		{
			this.dataPoints.add(c.getDataPoint(i));
		}
	}
	DataPoint findClosest (DataPoint p)
	{
		//returns the DataPoint in this cluster that has the closest coordinates to the argument
		DataPoint closest;
		
		double distance = Double.MAX_VALUE;
		int index = this.size();
		
		
		for (int i = 0; i < this.size(); i++)
		{
			double temp = p.euclideanDistance(this.dataPoints.get(i));
			if (temp < distance) //may need to change to account for duplicate values, as of now, assume we add those too 
			{
					distance = temp; 
					index = i;
			}
		}
		
		closest = dataPoints.get(index);
		
		return closest;
	}
	
	DataPoint getDataPoint(int index)
	{
		return dataPoints.get(index);
	}
	
	DataPoint findClosest (Cluster c)
	{
		//returns the DataPoint in this cluster that has the closest coordinates to the argument
		DataPoint closest;
		double distance = Double.MAX_VALUE;
		int index = this.size();
		for (int i = 0; i < c.size(); i++)
		{
			DataPoint temp = c.getDataPoint(i);
			for (int j = 0; j < this.size(); j++)
			{
				double d2 = temp.euclideanDistance(this.getDataPoint(j));
				if ( d2 < distance)
				{
					distance = d2;
					index = i; //finds closest datapoint from argument to this cluster
				}
			}
		}
		closest = c.getDataPoint(index);
		return closest;
	}
	DataPoint findFromClosest (Cluster c)
	{
		//if (this.size() == 1) return this.getDataPoint(0);
		//returns the DataPoint in this cluster that has the closest coordinates to the argument
		DataPoint closest;
		
		double distance = Double.MAX_VALUE;
		int index = this.size();
		for (int i = 0; i < c.size(); i++)
		{
			DataPoint temp = c.getDataPoint(i);
			for (int j = 0; j < this.size(); j++)
			{
				double d2 = temp.euclideanDistance(this.getDataPoint(j));
				if (d2 < distance)
				{
					distance = d2;
					index = j; //finds closest datapoint from this cluster to the argument cluster
				}
			}
		}
		closest = this.getDataPoint(index);
		return closest;
	}
	
	void setCenter ()
	{
		if (this.size() == 0) return;
		
		double temp [] = new double [this.dataPoints.get(0).size()];
		
		//initialize to 0
		for (int i = 0; i < temp.length; i++)
		{
			temp[i] = 0;
		}
		
		//sum of all data fields from each data point
		for (int i = 0; i < this.size(); i++)
		{
			DataPoint dp = dataPoints.get(i);
			
			for (int j = 0; j < temp.length; j++)
			{
				temp[j] += dp.get(j); 
			}
		}
		
		//find average of each data field
		
		for (int i = 0; i < temp.length; i++)
		{
			temp[i] = temp[i] / this.size();
		}
		
		center = new DataPoint (temp, "CENTER");
	}
	
	
	//	UTILITIES
	DataPoint getCenter ()
	{
		return center;
	}
	
	
	int size ()
	{
		return dataPoints.size();
	}
	void add(DataPoint dp)
	{
		//dp.setFlag(true);
		dataPoints.add(dp);
	}
	
	int findIndex (DataPoint dp)
	{
		for (int i = 0; i < dataPoints.size(); i++)
		{
			if (dp.samePoint(dataPoints.get(i))) return i;
		}
		return -1;
	}
	public void printCluster ()
	{
		for (int i = 0; i < dataPoints.size(); i++)
		{
			dataPoints.get(i).printDataPoint();
		}
	}
}