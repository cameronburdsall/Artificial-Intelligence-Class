import java.util.ArrayList;


public class Node {
		int attributeIndex;
		ArrayList<Node> children;
		ArrayList<String> childrenValues;
		//ArrayList <Integer> usedAttributes;
		//AttributeList AL;
		String label;
		boolean isLeaf;
		boolean isContinuous;
		double thresh;
		public Node ()
		{ 	
			//this.AL=in;
			//this.attributeIndex = attribute;
			//children = new Node [AL.countPossibleAttributes(attribute)];
			children = new ArrayList<Node>();
			childrenValues = new ArrayList<String>();
			//this.usedAttributes = new ArrayList <Integer> ();
			//this.usedAttributes.add(attribute);
			isLeaf = false;
			thresh = -1;
			this.isContinuous=false;
		}
		public ArrayList<String> getChildValues()
		{
			return this.childrenValues;
		}
		public void setThresh(double t)
		{
			this.thresh = t;
		}
		public double getThresh()
		{
			return this.thresh;
		}
		public void setFlag ()
		{
			isLeaf = true;
		}
		public void setCont ()
		{
			this.isContinuous = true;
		}
		public boolean isCont()
		{
			return this.isContinuous;
		}
		/*public Node (AttributeList in, int attribute, ArrayList<Integer> used)
		{
			this.AL = in;
			this.attributeIndex = attribute;
			children = new Node [AL.countPossibleAttributes(attribute)];
			//this.usedAttributes = used;
			//this.usedAttributes.add(attribute);
			isLeaf = false;
		}*/
		
		public void setLabel(String l)
		{
			this.label = new String (l);
		}
		
		public int getAttributeIndex() {
			return attributeIndex;
		}
		public void setAttributeIndex(int attributeIndex) {
			this.attributeIndex = attributeIndex;
		}
		public ArrayList<Node> getChildren() {
			return children;
		}
		public void setChildren(ArrayList<Node> children) {
			this.children = children;
		}
		public void setChild (Node n, String value)
		{
			children.add(n);
			childrenValues.add(value);
		}
		
	}