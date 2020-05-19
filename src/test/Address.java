package test;

public class Address {
	private final String		_zipcode;
	private final String		_detail;
	
	public Address(String zipcode, String detail) {
		_zipcode	= zipcode;
		_detail		= detail;
	}
	
	public String getZipCode() {
		return _zipcode;
	}
	
	public String getDetail() {
		return _detail;
	}
	
	public String toString() {
		return _detail + " (" + _zipcode + ")";
	}
}
