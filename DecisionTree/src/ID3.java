import java.util.ArrayList;

public class ID3 {
	AttributeList AL;
	public ID3 ()
	{
			String file1 = "src/data.txt";
			String file1t = "src/data_test.txt";
			String file2 = "src/data2.txt";
			String file2t = "src/data2_test.txt";
			String file3 = "src/data1.txt";
			String file3t = "src/data1_test.txt";
			//AL = new AttributeList (file2, 0, 7);
			//AL = new AttributeList (file1, 0, 1 );
			AL = new AttributeList (file3, 1, 5, 2);
			ArrayList <Integer> used = new ArrayList<Integer>();
			Node root = runID3(AL, used, new Node());
			System.out.println("Tree Created");

			//test(file2t, root, 0, 7, -1);
			//test(file1t, root, 0, 1, -1);
			test(file3t, root, 1, 5, 2);
		System.out.println("Done");
	}
	public static void main (String args[])
	{
		new ID3();
	}
	
	AttributeList getAL ()
	{
		return AL;
	}
	
	public Node runID3 (AttributeList examples, ArrayList<Integer> used, Node n)
	{
		//examples - subset of data being evaluated
		//used - ArrayList that hold attributes that have already been tested on current traversal
		//n - Node currently being evaluated
		//Node root = new Node (AL, Tools.findBestAttribute(examples,  used));
		Node root = n;
		examples.setCA(AL.getCA());
		
		//if all target attributes have the same value, set that as root label and return
		if (Tools.checkIfSame(examples, examples.getTargetIndex()))
		{
			if (examples.getCA() != -1) //when there is a continuous value
			{
				examples.findBestThresh();
				root.setThresh(examples.getThresh());
			}
			System.out.println ("SAME");
			root.setLabel(examples.getData(0, examples.getTargetIndex()));
			System.out.println ("LABEL " + root.label);
			root.setFlag();
			return root;
		}
		
		if (examples.getNumAttributes() == used.size())
		{
			//if all possible attributes have been tested, set the most common target value as node label
			if (examples.getCA() != -1)
			{
				examples.findBestThresh();
				root.setThresh(examples.getThresh());
			}
			System.out.println ("FULL " + examples.getNumAttributes());
		
			examples.printAL();
			root.setLabel(Tools.mostCommonAttributeValue(examples, AL.getTargetIndex()));
			System.out.println ("LABEL " + root.label);
			root.setFlag();
			return root;
		}
		
		//BEGIN
		
		//find best attribute through calculating information gain
		if (examples.getCA()!= -1) examples.findBestThresh();
		int attribute = Tools.findBestAttribute(examples,  used);
		System.out.println ("BEST ATT GAIN: " + attribute);
		used.add(attribute);
		
		//data structure to hold possible attribute values
		ArrayList<String>  valueI = examples.getPossibleAttributes(attribute);
		//set best attribute to current node
		root.setAttributeIndex(attribute);
		if (AL.getCA() == attribute) {
			//for continous attributes, splits data set over the threshold
			ArrayList<DataEntry> sub1 = new ArrayList<DataEntry>();
			ArrayList<DataEntry> sub2 = new ArrayList<DataEntry>();
			for (int j = 0; j < examples.size(); j++){
				Double temp = Double.parseDouble(examples.getData(j,  attribute));
				if (temp > AL.getThresh())
				{
					sub1.add(examples.get(j));
				}
				else
				{
					sub2.add(examples.get(j));
				}
			}
			AttributeList examplesV1 = new AttributeList(sub1, examples.getTargetIndex(), AL.getCA());
			AttributeList examplesV2 = new AttributeList(sub2, examples.getTargetIndex(), AL.getCA());
			examplesV1.setCA(AL.getCA());
			examplesV2.setCA(AL.getCA());
			examples.findBestThresh();
			System.out.println ("Cont Sub set 1 CA: " + examples.getCA() + " " + examples.getThresh());
			examplesV1.printAL();
			System.out.println ("Cont Sub set 2 CA: " + examples.getCA() + " " + examples.getThresh());
			examplesV2.printAL();
			Node node1 = new Node ();
			Node node2 = new Node();
			root.setCont();
			root.setThresh(examples.getThresh());
			root.setChild(runID3(examplesV1, used, node1), Double.toString(examplesV1.getThresh()));
			root.setChild(runID3(examplesV2, used, node2), Double.toString(examplesV2.getThresh()));
		}
		else {
			//for discrete attributes, divides data set into subsets based on the possible values for said attribute
			for (int i = 0; i < valueI.size(); i++)
			{
				//create a subset AttributeList with only attribute with valueI[i]
				ArrayList<DataEntry> sub = new ArrayList<DataEntry>();
				for (int j = 0; j < examples.size(); j++){
					String temp = examples.getData(j,  attribute);
					if (valueI.get(i).equals(temp))
					{
						//examples.get(j).printEntries();
						sub.add(examples.get(j));
					}
				}

				AttributeList examplesVI = new AttributeList(sub, examples.getTargetIndex());
				System.out.println ("Sub set" + i);
				examplesVI.printAL();
				Node node = new Node ();
				root.setChild(runID3(examplesVI, used, node), valueI.get(i));
			}
		}
		used.remove(used.indexOf(attribute));
		return root;
	}
	
	public void test (String file, Node root, int offset, int target, int ca)
	{
		//tests the decision tree by traversing its nodes and coming up with a best guess for the target attribute
		AttributeList AL;
		if (ca != -1)  AL = new AttributeList(file, offset, target, ca);
		else AL = new AttributeList (file, offset, target);
		Node p = root;
		int count = 0;
		for (int i = 0; i < AL.size(); i++)
		{
			System.out.print("TEST #" + i + ": ");
			p = root;
			while (!p.isLeaf)
			{
				if (p.isCont()) //if the node is testing the continuous attribute
				{
					double val = Double.parseDouble(AL.getData(i, p.getAttributeIndex()));
					if (val > p.getThresh())
					{
						p = p.children.get(0);
					}
					else {
						p = p.children.get(1);
					}
				}
				else //if a discrete attribute
				{
					boolean flag = false;
					String val = AL.getData(i, p.getAttributeIndex());
					for (int j = 0; j < p.children.size(); j++)
					{
						if (val.equals(p.childrenValues.get(j)))
						{
							p = p.children.get(j);
							flag = true;
							break;
						}
					}
					if (flag != true) {
						p=p.children.get(0);
					}
				}
			}
			
			AL.get(i).printEntries();
			System.out.println (" BEST GUESS: " + p.label + " CORRECT ANSWER: " + AL.getData(i, AL.getTargetIndex()));
			if (p.label.equals(AL.getData(i, AL.getTargetIndex())))
			{
				count++;
			}
		}
		System.out.println("SUCCESS RATE: " + count + " / " + AL.size() + " = " + (new Double (count)) / new Double (AL.size()) * 100 + "%");
	}
}
