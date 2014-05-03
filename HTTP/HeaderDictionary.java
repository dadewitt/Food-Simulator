package HTTP;

import java.util.Scanner;

public class HeaderDictionary {
	private String fileAndQuery;
	private String[] requestKeys = {
		"GET", "Accept", "Accept-Charset", "Accept-Encoding", "Accept-Language", "Accept-Datetime", "Authorization",
		"Cache-Control", "Connection", "Cookie", "Content-Length", "Content-MD5", "Content-Type", "Date", 
		"Expect", "From", "Host", "If-Match", "If-Modified-Since", "If-None-Match", "If-Range", "If-Unmodified-Since",
		"Max-Forwards", "Origin", "Pragma", "Proxy-Authorization", "Range", "Referer", "TE", "User-Agent", "Upgrade",
		"Via", "Warning", "X-Requested-With", "DNT", "X-Forwarded-For","X-Forwarded-Proto", "Front-End-Https",
		"X-ATT-DeviceId", "X-Wap-Profile", "Proxy-Connection"
	};
	private String[] responseKeys = {
		"Access-Control-Allow-Origin", "Accept-Ranges", "Age", "Allow", "Cache-Control", "Connection", "Content-Encoding",
		"Content-Language", "Content-Length", "Content-Location", "Content-MD5", "Content-Disposition", "Content-Range", 
		"Content-Type", "Date", "ETag", "Expires", "Last-Modified", "Link", "Location", "P3P", "Pragma", "Proxy-Authenticate",
		"Refresh", "Retry-After", "Server", "Set-Cookie", "Status", "Strict-Transport-Security", "Trailer", "Transfer-Encoding",
		"Upgrade", "Vary", "Via", "Warning", "WWW-Authenticate", "X-Frame-Options", "Pulic-Key-Pins", "X-XSS-Protection", 
		"Content-Security-Policy", "X-Content-Type-Options", "X-Powered-By", "X-UA-Compatible"
	};
	private String[] responseValues;
	private String[] requestValues;
	private boolean isRequest;
	
	private int getKeyIndex(String key) {
		String[] array = (isRequest ? requestKeys : responseKeys);
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(key))
				return i;
		}
		return -1;
	}
	
	public HeaderDictionary(boolean isRequest, String data) {
		this.isRequest = isRequest;
		responseValues = new String[responseKeys.length];
		for (int i = 0; i < responseValues.length; i++)
			responseValues[i] = null;
		requestValues = new String[requestKeys.length];
		for (int i = 0; i < requestValues.length; i++)
			responseValues[i] = null;
		String[] keys = (isRequest ? requestValues : responseValues);
		Scanner in = new Scanner(data);
		if (in.hasNext()) {
			in.next();
			fileAndQuery = in.next();
			in.nextLine();
			//System.out.println("file: " + fileAndQuery);
			while (in.hasNext()) {
				String key = in.next().replace(":", "");
				//System.out.println("key: " + key);
				String value = in.nextLine();
				//System.out.println("Value: " + value);
				keys[getKeyIndex(key)] = value;
			}
		}
	}
	
	public String getField(String fieldName) {
		String[] array = (isRequest ? requestValues : responseValues);
		int ind = getKeyIndex(fieldName);
		return array[ind];
		
	}
	
	public void print() {
		String[] array = (isRequest ? requestKeys : responseKeys);
		String[] valList = (isRequest ? requestValues : responseValues);
		for (int i = 0; i < array.length; i++) {
			String key = array[i];
			String val = valList[i];
			if (val != null)
				System.out.println(key + ": " + val);
		}
	}
}