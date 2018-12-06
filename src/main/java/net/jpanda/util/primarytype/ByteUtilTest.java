package net.jpanda.util.primarytype;

public class ByteUtilTest {

	public static void main(String[] args){
		
		byte source=-110;
		
		System.out.println(Integer.toBinaryString(source & 0xFF));
		
		for(int i=0;i<=7;i++){
			System.out.println(ByteUtil.getBitFromByte(source, i));
		}
		
	}
}
