package test;

public class Test {
	private int			_value;
	
	public Test(int value) {
		_value = value;
	}
	
	public int getValue() {
		return _value;
	}
	
	public void setValue(int value) {
		_value = value;
	}
	
	public void print() {
		System.out.println("Test.print()");
	}
	
	public void print(String msg) {
		System.out.println("Test.print(msg): " + msg);
	}
	
	public int getSum(int a, int b) {
		return (a + b);
	}
	
	public boolean getTrue() {
		return true;
	}
	
	public boolean getFalse() {
		return false;
	}
	
	public static void staticPrint() {
		System.out.println("Test.staticPrint()");	
	}
	
	public String toString() {
		return "Test(" + _value + ")";
	}
}
