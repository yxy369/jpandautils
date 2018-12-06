package net.jpanda.util.primarytype;

import java.nio.ByteBuffer;

/**
 * byte与其他数据类型互转工具
 * @author yxy
 *
 */
public class ByteUtil {

	private static ByteBuffer buffer=ByteBuffer.allocate(8);
	
	/**
	 * 将byte[]前4字节转为int
	 * @param bytes
	 * @return
	 * @throws InvalidBytesException
	 */
	public static int bytesToInt(byte[] bytes){
		return bytesToInt(bytes,0);
	}
	
	/**
	 * 将一个byte数组，从index位置开始的4字节转为int
	 * @param bytes
	 * @param index
	 * @return
	 * @throws InvalidBytesException
	 */
	public static int bytesToInt(byte[] bytes, int index){
		
		return bytes[index+3] & 0xFF | 
			   (bytes[index+2] & 0xFF)<<8 | 
			   (bytes[index+1] & 0xFF)<<16 | 
			   (bytes[index+0] & 0xFF)<<24;
	}
	
	/**
	 * 将int转为长度为4的byte[]
	 * @param i
	 * @return
	 */
	public static byte[] intToBytes(int i){
		return new byte[] {
				(byte)((i>>24) & 0xFF),
				(byte)((i>>16) & 0xFF),
				(byte)((i>>8) & 0xFF),
				(byte)(i & 0xFF)
		};
	}
	
	/**
	 * 将short转为长度为2的byte[]
	 * @param s
	 * @return
	 */
	public static byte[] shortToBytes(short s){
		return new byte[] {
				(byte)((s>>8) & 0xFF),
				(byte)((s>>0) & 0xFF)
		};
	}
	
	/**
	 * 将byte[]前2字节转为short
	 * @param bytes
	 * @return
	 * @throws InvalidBytesException
	 */
	public static short bytesToShort(byte[] bytes){
		return bytesToShort(bytes,0);
	}
	
	/**
	 * 将一个byte数组，从index位置开始的2字节转为int
	 * @param bytes
	 * @param index
	 * @return
	 * @throws InvalidBytesException
	 */
	public static short bytesToShort(byte[] bytes, int index){
		
		return (short)(((bytes[index+0]<<8) | bytes[index+1] & 0xFF));
	}
	
	/**
	 * 从byte[]中提取新的byte[]
	 * @param source - 源byte[]
	 * @param start - 提取开始位置
	 * @param end - 提取结束位置
	 * @return 新的byte[]
	 */
	public static byte[] extractBytes(byte[] source, int start, int end){
		byte[] result=new byte[end-start+1];
		for(int i=0;(start+i)<=end;i++){
			result[i]=source[start+i];
		}
		return result;
		
	}
	
	/**
	 * 合并两个byte[]
	 * @param byte1
	 * @param byte2
	 * @return byte[]
	 */
	public static byte[] mergeBytes(byte[] byte1, byte[] byte2){
		byte[] result=new byte[byte1.length+byte2.length];
		replaceBytes(result,byte1,0);
		replaceBytes(result,byte2,byte1.length);
		return result;
	}
	
	/**
	 * 从index位置开始，替换目标的byte[]
	 * @param source - 目标byte[]
	 * @param bytes - 替换byte[]
	 * @param index - 要替换的开始位置
	 */
	public static void replaceBytes(byte[] source, byte[] bytes, int index){
		for(int i=0;i<bytes.length;i++){
			source[index+i]=bytes[i];
		}
		
	}
	
	/**
	 * 将byte[]中的某一位置1
	 * @param source - 源byte[]
	 * @param pos - 要置1的位置，取值范围0-source.length*8-1
	 */
	public static void set(byte[] source, int pos){
		int s=pos/8;
		int y=pos%8;
		byte tmp=set(source[s], y);
		source[s]=tmp;
		
	}
	
	/**
	 * 将byte中的某一位置1
	 * @param source - 源byte
	 * @param pos - 要置1的位置，取值范围0-7
	 * @return byte - 返回设置后的byte
	 */
	public static byte set(byte source, int pos){
		int moveCount=8-pos-1;
		return (byte)(source|(byte)(1<<moveCount));

	}
	
	/**
	 * 将byte[]中某一范围置0
	 * @param source - 源byte[]
	 * @param startPos - 要置0的起始位置
	 * @param endPos - 要置0的结束位置
	 */
	public static void clear(byte[] source, int startPos, int endPos){
		for(int i=0;i<=endPos-startPos;i++){
			clear(source,startPos+i);
		}
	}
	
	/**
	 * 将byte[]中的某一位置0
	 * @param source - 源byte[]
	 * @param pos - 要置0的位置，取值范围0-source.length-1
	 */
	public static void clear(byte[] source, int pos){
		int s=pos/8;
		int y=pos%8;
		byte tmp=clear(source[s], y);
		source[s]=tmp;
	}
	
	/**
	 * 将byte中的某一位置0
	 * @param source - 源byte
	 * @param pos - 要置0的位置，取值范围0-7
	 * @return byte - 返回设置后的byte
	 */
	public static byte clear(byte source, int pos) {
		int moveCount=8-pos-1;
		return (byte)(source & (byte)(~(1<<moveCount)));
	}
	
	/**
	 * 获取byte中某一bit的值
	 * @param source - 源byte
	 * @param pos - 位置(0-7)
	 * @return int - 参数pos指定位的bit值（0或1）
	 */
	public static int getBitFromByte(byte source, int pos){
		int result=0;
		if(pos<0 || pos>7)  //pos参数错误
			return 0;
		return (source & 1<<(7-pos)) >>> (7-pos);
	}
}
