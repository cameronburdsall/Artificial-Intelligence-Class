import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AttributeList {
	
	/*
	 * Data Set is structured as follows:
	 * 
	 * 					Attribute 1		Attribute 2		Attribute 3		...
	 *
	 * Data Entry #1		val				val				val
	 * 
	 * Data Entry #2		...				...				...
	 * 
	 * Data Entry #3		...				...				...
	 * 
	 * 		...
	 */
	
	ArrayList<DataEntry> attributes;
	private int targetIndex;
	int continuousAttribute; //denotes which attribute is continuous
	double thresh; //holds the calculated threshold for a continuous dataset
	
	/*		CONSTRUCTORS	*/
	public AttributeList (String file, int si, int target)
	{
		//read dataset from file, non continuous
		attributes = new ArrayList <DataEntry> ();
		this.targetIndex = target - si - 1;
		readInFile (file, si);
		this.continuousAttribute = -1;
	}
	public AttributeList (String file, int si, int target, int ca)
	{
		//read dataset from file, continous
		attributes = new ArrayList <DataEntry> ();
		this.targetIndex = target - si - 1;
		readInFile (file, si);
		this.continuousAttribute = ca - si - 1;
		this.sortByContinuous();
		this.printAL();
	}
	public AttributeList (ArrayList <DataEntry> subset, int target)
	{
		//creates a subset, non continuous
		this.attributes = subset;
		this.targetIndex = target;
		this.continuousAttribute = -1;
	}
	public AttributeList (ArrayList <DataEntry> subset, int target, int ca)
	{
		//creates subset, continous
		this.attributes = subset;
		this.targetIndex = target;
		this.continuousAttribute = ca;
		this.sortByContinuous();
		this.printAL();
	}
	
	/*	UTILITIES	*/
	public void setTresh(double t)
	{
		this.thresh = t;
	}
	public void setCA(int val)
	{
		this.continuousAttribute = val;
	}
	public double getThresh()
	{
		return thresh;
	}
	public void findBestThresh()
	{
		//implements the strategy to find the best possible threshold for a continuous attribute
		//sets threshold to the value with greatest information gain
		//if (this.getCA() == -1) return; //if accidentally called when not continuous
		double max = 0;
		double val = -1;
		for (int j = 0; j < this.getNumAttributes(); j++)
		{
			if (this.getCA()==j) continue; //only want to test discrete attributes
		
			for (int i = 0; i < this.size() - 1; i++)
			{
				String val1 = this.getData(i, j);
				String val2 = this.getData(i+1, j);
				if (!val1.equals(val2))
				{
					double d1 = Double.parseDouble(this.getData(i,  this.getCA()));
					double d2 = Double.parseDouble(this.getData(i+1,  this.getCA()));
					double tresh = (d1 + d2) / 2;
					double result = Tools.informationGainContinuous(this, this.getCA(), tresh);
					if (result > max)
					{
						max = result;
						val = tresh;
					}
				}
			}
		}
		if (val == -1)
		{
			this.findThresh();
			return;
		}
		System.out.println("Best Threshold Value: " + val);
		this.setTresh(val);
	}
	public void findThresh()
	{
		if (this.continuousAttribute == -1) return;
		double sum = 0;
		for (int j = 0; j < this.size(); j++) {
			sum += Double.parseDouble(this.getData(j, this.continuousAttribute));
		}
		sum = sum / this.size();
		this.thresh = sum;
	}
	public int getCA()
	{
		return this.continuousAttribute;
	}

	public void sortByContinuous ()
	{
		//sorts the data set by its continuous attribute's value from least to greatest
		if (this.continuousAttribute == -1) return;
		ArrayList<DataEntry> newAttributes = new ArrayList<DataEntry>();
		int i;
		int j;
		int mini;
		for (i = 0; i < attributes.size(); i++)
		{
			mini = i;
			for (j = i; j < attributes.size();j++)
			{
				double val1 = Double.parseDouble(attributes.get(j).get(continuousAttribute));
				double val2 = Double.parseDouble(attributes.get(mini).get(continuousAttribute));
				if (val2 > val1)
					{
						mini = j;
					}
			}
			this.swapEntries(mini,  i);
		}
	}
	public void swapEntries (int i, int j)
	{
		//performs a simple swap of two elements, used for sorting
		DataEntry temp = new DataEntry();
		temp = attributes.get(i);
		attributes.set(i, attributes.get(j));
		attributes.set(j,  temp);
	}
	public int getNumAttributes ()
	{
		return attributes.get(0).size() - 1; //minus 1 to account for target index
	}
	public int numOfOccurrances (String value, int attribute)
	{
		//returns the number of time a value appears in a given attribute column
		int ans = 0;
		for (int i = 0; i < this.size(); i++)
		{
			if (this.getData(i,  attribute).equals(value)) 
				{
					ans ++;
					//System.out.println("BOO " + ans + value + this.getData(i,  attribute));
				}
		}
		return ans;
	}
	public void printAL ()
	{
		//prints the data entries in the current set
		for (int i = 0; i < attributes.size(); i++)
		{
			for (int j = 0; j < attributes.get(i).size(); j++)
			{
				System.out.print(attributes.get(i).get(j) + " ");
			}
			System.out.println();
		}
	}
	
	public ArrayList<String> getPossibleAttributes (int attribute)
	{
		//returns an array list with all possible attribute values in a given attribute column
		ArrayList<String> results = new ArrayList<String>();
		
		for (int i = 0; i < this.size(); i++)
		{
			
			String value = this.getData(i,  attribute);
			if (!results.contains(value))
			{
				results.add(value);
			}
		}
		
		return results;
	}
	
	public int countPossibleAttributes (int attribute)
	{
		// returns number of possible attributes
		ArrayList<String> stack = new ArrayList<String>();
		for (int i = 0; i < attributes.size(); i++)
		{
			String value = this.getData(i,  attribute);
			if (!stack.contains(value))
			{
				stack.add(value);
			}
		}
		return stack.size();
	}
	
	public String getData (int i, int j)
	{
		// i is the index into the DataEntry (Rows)
		// j is the index in the selection of the attribute (Columns)
		return attributes.get(i).get(j);
	}
	
	public int size()
	{
		return attributes.size();
	}
	
	public DataEntry get (int index)
	{
		return attributes.get(index);
	}
	
	public int getTargetIndex()
	{
		return this.targetIndex;
	}
	
	public void readInFile (String file, int si)
	{
		//imports data set from a file stored in csv format, can be modified for other delimiters
		try(BufferedReader in = new BufferedReader(new FileReader(file))) {
		    String str;
		    String label = "";
		    while ((str = in.readLine()) != null) {
		        String [] tokens = str.split(",");
		        DataEntry d = new DataEntry();
		        for (int i = si; i < tokens.length; i++)
		        {
		        	d.add(tokens[i]);
		        }
		        attributes.add(d);
		    }
		}
		catch (IOException e) {
		    System.out.println("File Read Error");
		}
	}

}

class DataEntry
{
	//class that stores each individual data entry
	ArrayList<String> data;
	public void printEntries()
	{
		for (int i = 0; i < data.size(); i++)
		{
			System.out.print(data.get(i) + "  ");
		}
	}
	public DataEntry ()
	{
		data = new ArrayList <String> ();
	}

	public void add (String in)
	{
		data.add(in);
	}
	
	public String get (int i)
	{
			return data.get(i); 
	}
	public int size()
	{
		return data.size();
	}
}
