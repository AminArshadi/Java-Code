
public class EntropyEvaluator {

	public static double evaluate(DataSet[] partitions) {

		if (partitions == null || partitions.length == 0)
			throw new IllegalArgumentException("Argument --partitions-- in method --evaluate-- cannot be null and should contain at least one partition.");

		// All partitions will have the same attributes, so we simply take the first:
		DataSet p = partitions[0];

		// Assume that "class" is the last attribute.
		int classIndex = p.getNumberOfAttributes() - 1;

		double[] entropyValues = new double[partitions.length];
		int totalCount = 0;

		for (int i = 0; i < partitions.length; i++) {
			String[] classes = partitions[i].getUniqueAttributeValues(classIndex);
			int currentPartitionSize = partitions[i].getNumberOfDatapoints();

			if (currentPartitionSize == 0)
				continue;

			totalCount += currentPartitionSize;

			int[] classCounts = new int[classes.length];

			for (int j = 0; j < partitions[i].getNumberOfDatapoints(); j++) {
				String cls = partitions[i].getValueAt(j, classIndex);
				for (int k = 0; k < classes.length; k++) {
					if (cls.equals(classes[k])) {
						classCounts[k]++;
						break;
					}
				}
			}

			for (int k = 0; k < classes.length; k++) {
				if (classCounts[k] == 0)
					continue;
				entropyValues[i] += -1 * (((double) classCounts[k]) / currentPartitionSize)
						* log2(((double) classCounts[k]) / currentPartitionSize);
			}

		}

		if (totalCount == 0)
			return 0;

		double average = 0;

		for (int i = 0; i < partitions.length; i++) {
			average += (((double) partitions[i].getNumberOfDatapoints()) / totalCount) * entropyValues[i];
		}

		return average;
	}

	public static double log2(double x) {
		if (x <= 0){
			throw new IllegalArgumentException("Argument --x-- in method --log2-- cannot be less than or equal to zero.");
		}
		return (Math.log(x) / Math.log(2));
	}
}
