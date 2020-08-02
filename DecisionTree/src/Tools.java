import java.util.ArrayList;

public abstract class Tools {
	public static boolean checkIfSame (AttributeList AL, int attribute)
	{
		//checks if all values in a given attribute column are the same
		for (int i = 1; i < AL.size(); i++)
		{
			if (!AL.getData(i, attribute).equals(AL.getData(0,  attribute)))
			{
				return false;
			}
		}
		return true;
	}
	public static String mostCommonAttributeValue (AttributeList set, int attribute)
	{
		//returns the most common value in a given attribute column
		String result;
		ArrayList<String> values = set.getPossibleAttributes(attribute);
		int [] counts = new int [values.size()];
		
		for (int i = 0; i < set.size(); i++)
		{
			for (int j = 0; j < values.size(); j++)
			{
				if (set.getData(i,  attribute).equals(values.get(j)))
				{
					counts[j]++;
				}
					
			}
		}
		
		int max = 0;
		int index = 0;
		set.printAL();
		System.out.println (attribute);
		for (int i = 0; i < counts.length; i++)
		{
			if (counts[i] > max)
			{
				max = counts[i];
				index = i;
			}
		}
		
		result = values.get(index);
		
		return result;
	}
	public static int findBestAttribute (AttributeList set, ArrayList<Integer> used)
	{
		//returns the index of the best candidate attribute based on the highest calculated information gain
		int index = 0;
		double max = -1;
		
		for (int i = 0; i < set.getNumAttributes(); i++)
		{
			if (used.contains(i)) continue; //dont try if we already used this attribute on the tree branch
			if (set.getTargetIndex() == i) continue; //dont evaluate the target attribute...
			double temp;
			if (set.getCA() == i)
			{
				set.findBestThresh();
				temp = Tools.informationGainContinuous(set, i, set.getThresh());
			}
			else
			{
				temp = Tools.informationGain(set, i);
			}
			if (max < temp)
			{
				max = temp;
				index = i;
			}
		}
		
		return index;
	}
	public static double informationGain (AttributeList AL, int attribute)
	{
		//calculates information gain for discrete attributes
		double ans = 0;
		ArrayList<String> values = AL.getPossibleAttributes(AL.getTargetIndex());
		double counts [] = new double [values.size()];
		for (int i = 0; i < values.size(); i++)
		{
			counts[i] = 0;
			for (int j = 0; j < AL.size(); j++)
			{
				if (values.get(i).equals(AL.getData(j, AL.getTargetIndex())))
				{
					counts[i]++;
				}
			}
			
			counts[i] = counts[i] / new Double(AL.size());
		}
		ans = Tools.entropy (counts);
		double  partitions = 0;
		values = AL.getPossibleAttributes(attribute);
		for (int i = 0; i < values.size(); i++)
		{
			ArrayList <DataEntry> subsetV = new ArrayList <DataEntry>();
			for (int j = 0; j < AL.size(); j++) //find and fill subset
			{
				String temp = AL.getData(j, attribute);
				if (values.get(i).equals(temp))
					{
						subsetV.add(AL.get(j));
					}
			}
			double ratio = new Double (subsetV.size()) / new Double (AL.size());
			System.out.println (ratio);
			AttributeList sub = new AttributeList (subsetV, AL.getTargetIndex());
			partitions += ratio * Tools.entropy(AL, sub, attribute);
			
		}
		ans = (ans - partitions);
		
		System.out.println ("INFO GAIN : " + ans);
		
		return ans;
	}
	
	public static int splitOverThresh (AttributeList AL, double thresh)
	{
		int count = 0;
		for (int i = 0; i < AL.size(); i++)
		{
			double temp = Double.parseDouble(AL.getData(i, AL.getCA()));
			if (temp > thresh) count ++;
		}
		return count;
	}
	
	public static double informationGainContinuous (AttributeList AL, int attribute, double thresh)
	{
		//calculates information gain for continuous attributes
		double ans = 0;
		ArrayList<String> values = AL.getPossibleAttributes(AL.getTargetIndex());
		double  partitions = 0;
			ArrayList <DataEntry> subset1 = new ArrayList <DataEntry>();
			ArrayList <DataEntry> subset2 = new ArrayList <DataEntry>();

			double counts [] = new double [values.size()];
			for (int i = 0; i < values.size(); i++)
			{
				counts[i] = 0;
				for (int j = 0; j < AL.size(); j++)
				{
					if (values.get(i).equals(AL.getData(j, AL.getTargetIndex())))
					{
						counts[i]++;
					}
				}
				System.out.println(counts[i]);
				counts[i] = counts[i] / new Double(AL.size());
			}
			ans = entropy (counts);
			for (int j = 0; j < AL.size(); j++) //find and fill subset
			{
				double temp = Double.parseDouble(AL.getData(j, attribute));
				if (temp > thresh)
					{
						subset1.add(AL.get(j));
					}
				else {
					subset2.add(AL.get(j)); }
			}
			double ratio = new Double (subset1.size()) / new Double (AL.size());

			AttributeList sub = new AttributeList (subset1, AL.getTargetIndex());
			partitions += ratio * Tools.entropyCont(AL, sub, attribute);
			ratio = new Double (subset2.size()) / new Double (AL.size());
			System.out.println(ratio);
			sub = new AttributeList (subset2, AL.getTargetIndex());
			partitions += ratio * Tools.entropyCont(AL, sub, attribute);
			System.out.println ("PART GAIN : " + partitions);
			
		
		ans = (ans - partitions);
		System.out.println ("INFO GAIN : " + ans);
		return ans;
	}
	public static double entropyCont (AttributeList AL, AttributeList subset, int at)
	{
		//finds entropy for continuous attributes
		double result = 0;
		ArrayList <String> values = subset.getPossibleAttributes(AL.getTargetIndex());
		double counts [] = new double [values.size()];
		for (int i = 0; i < values.size(); i++)
		{
			counts[i] = 0;
			for (int j = 0; j < subset.size(); j++)
			{
				if (values.get(i).equals(subset.getData(j, AL.getTargetIndex())))
				{
					counts[i]++;
				}
			}
			counts[i] = counts[i] / new Double(subset.size());
		}
		subset.continuousAttribute = AL.getCA();

		for (int i = 0; i < counts.length; i++)
		{
			double temp = -counts[i] * (Math.log(counts[i]) / Math.log(2)); //convert log to base 2
			result += temp; //sum into result
		}
		System.out.println ("ENTROPY: " + result);
		return result;
	}
	public static double entropy (AttributeList AL, AttributeList subset, int at)
	{
		//finds entropy for discrete attributes
		double result = 0;
		ArrayList <String> values = subset.getPossibleAttributes(AL.getTargetIndex());
		double counts [] = new double [values.size()];
		for (int i = 0; i < values.size(); i++)
		{
			counts[i] = 0;
			for (int j = 0; j < subset.size(); j++)
			{
				if (values.get(i).equals(subset.getData(j, AL.getTargetIndex())))
				{
					counts[i]++;
				}
			}
			counts[i] = counts[i] / new Double(subset.size());
		}
		subset.continuousAttribute = AL.getCA();

		for (int i = 0; i < counts.length; i++)
		{
			double temp = -counts[i] * (Math.log(counts[i]) / Math.log(2)); //convert log to base 2
			result += temp; //sum into result
		}
		System.out.println ("ENTROPY: " + result);
		return result;
	}
	public static double entropy (double [] p)
	{
		//used for first part of Information Gain calculation (first entropy calc)
		double result = 0;
	
		//calculate entropy for arbitrary number of possible values c
		for (int i = 0; i < p.length; i++)
		{
			double temp = -p[i] * (Math.log(p[i]) / Math.log(2)); //convert log to base 2
			result += temp; //sum into result
		}
		//System.out.println ("FIN " + result);
		return result;
	}
}

