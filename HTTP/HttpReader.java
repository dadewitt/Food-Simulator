package HTTP;

public class HttpReader {
	
	public static HeaderDictionary parseRequest(String data) {
		return new HeaderDictionary(true, data);
	}
}
