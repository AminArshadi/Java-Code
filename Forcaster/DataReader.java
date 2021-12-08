
public interface DataReader {

	int getNumberOfColumns();

	int getNumberOfDataRows();

	String[] getAttributeNames();

	String[][] getData();

	String getSourceId();
}
