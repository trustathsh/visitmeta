package de.hshannover.f4.trust.visitmeta.util;

public class Same {

	public static boolean check(Object one, Object other){
		if (one != null){
			return one.equals(other);
		}else if (other != null) {
			return other.equals(one);
		} else {
			return true;
		}
	}
	
}
