package test;

public class Person {
	private final String		_firstName;
	private final String 		_lastName;
	private final int			_age;
	private final Address		_address;
	
	public Person(String firstName, String lastName, int age, Address address) {
		_firstName 		= firstName;
		_lastName		= lastName;
		_age			= age;
		_address		= address;
	}

	public String getFirstName() {
		return _firstName;
	}
	
	public String getLastName() {
		return _lastName;
	}
	
	public int getAge() {
		return _age;
	}
	
	public Address getAddress() {
		return _address;
	}
	
	public String toString() {
		return _firstName + " " + _lastName;
	}
	
}
