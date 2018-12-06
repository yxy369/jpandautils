package net.jpanda.util.net;

public class InvalidIpAddressException extends Exception {

	public InvalidIpAddressException(){
		super();
	}
	
	public InvalidIpAddressException(String message){
		super(message);
	}
}
