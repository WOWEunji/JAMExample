//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $id: $
//  $Source: /home/CVS/semix2/jam-060722/uos/ai/jam/examples/TestClass.java,v $
//  
//  File              : TestClass.java
//  Original author(s): Jaeho Lee <jaeho@uos.ac.kr>
//  Created On        : Tue Sep 30 14:22:34 1997
//  Last Modified By  : Jaeho Lee <jaeho@david>
//  Last Modified On  : Mon Sep  6 17:46:34 2004
//  Update Count      : 1
//  
//  Copyright (C) 1997-2004 Jaeho Lee and Marcus J. Huber.
//  
//////////////////////////////////////////////////////////////////////////////
//
//  JAM agent architecture
//
//  Copyright (C) 1997-2004 Jaeho Lee and Marcus J. Huber.
//  
//  Permission is granted to copy and redistribute this software so long
//  as no fee is charged, and so long as the copyright notice above, this
//  grant of permission, and the disclaimer below appear in all copies
//  made.
//  
//  This software is provided as is, without representation as to its
//  fitness for any purpose, and without warranty of any kind, either
//  express or implied, including without limitation the implied
//  warranties of merchantability and fitness for a particular purpose.
//  Jaeho Lee and Marcus J. Huber shall not be liable for any damages,
//  including special, indirect, incidental, or consequential damages,
//  with respect to any claim arising out of or in connection with the
//  use of the software, even if they have been or are hereafter advised
//  of the possibility of such damages.
// 
//////////////////////////////////////////////////////////////////////////////

package test;

public class TestClass {
	public int intMember;

	public double realMember;

	public String stringMember;

	public TestClass() {
		intMember = 1;
		realMember = 3.3;
		stringMember = "Hello there";
	}

	public void setIntMember(int i) {
		intMember = i;
	}

	public void setRealMember(double d) {
		realMember = d;
	}

	public void setStringMember(String s) {
		stringMember = s;
	}

	public void print() {
		System.out.println("TestClass::print: intMember = " + intMember);
		System.out.println("TestClass::print: realMember = " + realMember);
		System.out.println("TestClass::print: stringMember = " + stringMember);
	}

	public int memberFunction1(int a, Boolean b, String c) {
		System.out.println("\nIn method 1: " + a + ", " + b + ", " + c + "\n");
		return 1;
	}

	public String memberFunction2(int a, int b, String c) {
		System.out.println("\nIn method 2: " + a + ", " + b + ", " + c + "\n");
		return "Hello";
	}

	public int memberFunction3(String a, String b, String c) {
		System.out.println("\nIn method 3a: " + a + ", " + b + ", " + c + "\n");
		return 3;
	}

	public int memberFunction3(String a, String b, int c) {
		System.out.println("\nIn method 3b: " + a + ", " + b + ", " + c + "\n");
		return 3;
	}

	public int memberFunction3(String a, String b, double c) {
		System.out.println("\nIn method 3c: " + a + ", " + b + ", " + c + "\n");
		return 3;
	}

	public String memberFunction4(String a, String b, int c) {
		System.out.println("\nIn method 4: " + a + ", " + b + ", " + c + "\n");
		return "4";
	}

	public double memberFunction5(String a, String b, double c) {
		System.out.println("\nIn method 5a: " + a + ", " + b + ", " + c + "\n");
		return 5.0;
	}

	public double memberFunction5(String a, double b, double c) {
		System.out.println("\nIn method 5b: " + a + ", " + b + ", " + c);
		System.out.println("   " + b + " + " + c + " = " + (b + c));
		return b + c;
	}

	public int memberFunction6(TestClass t) {
		System.out.println("\nIn method 6: " + t);
		t.setIntMember(54321);
		t.setRealMember(5.4321);
		t.setStringMember("54321");
		return 1;
	}
}
