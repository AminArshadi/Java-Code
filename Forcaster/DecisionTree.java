
public class DecisionTree {

	private static class Node<E> {
		E data;
		Node<E>[] children;

		Node(E data) {
			this.data = data;
		}
	}

	Node<VirtualDataSet> root;

	public DecisionTree(ActualDataSet data) {
		//Edge cases:
		if ((data == null) || (! (data instanceof ActualDataSet))){
			throw new IllegalArgumentException("Argument --data-- in method --DecisionTree-- cannot be null nor have any other type except ActualDataSet.");
		}
		//code:
		root = new Node<VirtualDataSet>(data.toVirtual());
		build(root);
	}

	@SuppressWarnings("unchecked")
	private void build(Node<VirtualDataSet> node) {
		//1. Edge cases:
		if ((node == null) || (node.data == null)){
			throw new NullPointerException("Argument --node-- in method --build-- cannot be null.");
		}

		if (node.data.numAttributes < 1){
			throw new IllegalStateException("There should be at least one attribute.");
		}

		if (node.data.numRows == 0){
			throw new IllegalStateException("There should be at least one datapoint.");
		}


		//2. Base cases:
		if (node.data.numAttributes == 1){
			toString();
		}

		else if (node.data.attributes[node.data.numAttributes - 1].getValues().length == 1){
			toString();
		}

		else if (node.data.numRows < 1){
			toString();
		}
			

		//3. Recursive case:
		else{
			GainInfoItem[] sortedGainList = InformationGainCalculator.calculateAndSortInformationGains(node.data);
			GainInfoItem a_max = sortedGainList[0];

			int i = 0;
			int j = 0;
			
			//for nominal attributes
			if (a_max.getAttributeType() == AttributeType.NOMINAL){

				for (int n=0; n < node.data.numAttributes; n++){

					if (node.data.attributes[n].getName().equals( a_max.getAttributeName() )){
						i = n;
						break;
					}
				}

				VirtualDataSet[] tmp = node.data.partitionByNominallAttribute(i);
				node.children = new Node[tmp.length];

				for (int t=0; t < tmp.length; t ++){
					node.children[t] = new Node<VirtualDataSet>(tmp[t]);
				}

				for (int n=0; n < node.children.length; n++) {
					build(node.children[n]);
				}
			}

			//for numeric attributes
			else{
				for (int n=0; n < node.data.numAttributes; n++){

					if (node.data.attributes[n].getName().equals( a_max.getAttributeName() )){
						i = n;
						break;
					}
				}

				String[] targetRow = node.data.attributes[i].getValues();

				for (int k=0; k < targetRow.length; k++){

					if (targetRow[k].equals( a_max.getSplitAt() )){
						j = k;
						break;
					}
				}

				VirtualDataSet[] tmp = node.data.partitionByNumericAttribute(i, j);
				node.children = new Node[tmp.length];

				for (int t=0; t < tmp.length; t ++){
					node.children[t] = new Node<VirtualDataSet>(tmp[t]);
				}

				for (int n=0; n < node.children.length; n++) {
					build(node.children[n]);
				}
			}
		}
	}

	@Override
	public String toString() {
		return toString(root, 0);
	}

	private String toString(Node<VirtualDataSet> node, int indentDepth) {
		//1. Edge cases:
		// All the exeptions will be caught before reaching at this step, since
		// this method is private, and "public String toString()" depends on root only.

		StringBuffer buffer = new StringBuffer();
		String indent = createIndent(indentDepth);


		//2. Base cases:
		if(node.children == null){

			buffer.append(createIndent(indentDepth + 1));

			String classAttributeName = node.data.attributes[node.data.numAttributes - 1].getName();
			String[] classAttributeValue = node.data.attributes[node.data.numAttributes - 1].getValues();

			buffer.append(classAttributeName + " = " + classAttributeValue[0]);

			buffer.append("\n");
			
		}


		//3. Recursive case:
		else{

			for (int i = 0; i < node.children.length; i++) {

				String condition = node.children[i].data.getCondition();

				if (i == 0){
					buffer.append(indent);

					buffer.append("if (" + condition + ") {");

					buffer.append("\n");

					buffer.append(toString(node.children[i], indentDepth + 1));

					buffer.append(indent);

					buffer.append("}");

					buffer.append("\n");
				}
				
				else{
					buffer.append(indent);

					buffer.append("else if (" + condition + ") {");

					buffer.append("\n");

					buffer.append(toString(node.children[i], indentDepth + 1));

					buffer.append(indent);

					buffer.append("}");

					buffer.append("\n");
				}
			}
		}
		return buffer.toString();
	}

	private static String createIndent(int indentDepth) {
		if (indentDepth <= 0) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < indentDepth; i++) {
			buffer.append(' ');	
		}
		return buffer.toString();
	}

	public static void main(String[] args) throws Exception {
	
		if (args == null || args.length == 0) {
			System.out.println("Expected a file name as argument!");
			System.out.println("Usage: java DecisionTree <file name>");
			return;
		}

		String strFilename = args[0];

		ActualDataSet data = new ActualDataSet(new CSVReader(strFilename));

		DecisionTree dtree = new DecisionTree(data);

		System.out.println(dtree);
	}
}
