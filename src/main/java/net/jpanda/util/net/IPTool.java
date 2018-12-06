package net.jpanda.util.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 本类专用于IP地址的各类计算
 * @author yxy
 *
 */
public class IPTool {

	/**
	 * 所有可能的ip地址掩码
	 */
	private final static String[] ipMask={"0.0.0.0","128.0.0.0","192.0.0.0","224.0.0.0","240.0.0.0","248.0.0.0","252.0.0.0","254.0.0.0","255.0.0.0",
									"255.128.0.0","255.192.0.0","255.224.0.0","255.240.0.0","255.248.0.0","255.252.0.0","255.254.0.0","255.255.0.0",
									"255.255.128.0","255.255.192.0","255.255.224.0","255.255.240.0","255.255.248.0","255.255.252.0","255.255.254.0","255.255.255.0",
									"255.255.255.128","255.255.255.192","255.255.255.224","255.255.255.240","255.255.255.248","255.255.255.252","255.255.255.254","255.255.255.255"};
	
	/**
	 * 判断输入的ip地址是否合法
	 * @param ip-ip地址，isAssignable-该地址是否可实际分配给电脑使用（如：172.17.0.0，如果设置
	 *                  isAssignable为true，则方法返回false。如果设置isAssignable为false，则方法返回true）
	 * @return
	 */
	public static boolean isValidIp(String ip,boolean isAssignable){
		if(ip==null)
			return false;
		String[] ips=ip.split("\\.");
		if(ips.length!=4)
			return false;
		int temp=0;
		/*if(ips[0].equals("0") && ips[1].equals("0") && ips[2].equals("0") && ips[3].equals("0"))
			return true;*/
		for(int i=0;i<ips.length;i++){
			try{
				temp=Integer.parseInt(ips[i]);
			}catch(Exception e){
				return false;
			}
			if(i==0 && (temp==0 || temp==255) && isAssignable)
				return false;
			if(i==3 && (temp==0 || temp==255) && isAssignable)
				return false;
			if(temp<0 || temp>255)
				return false;			
		}
		return true;		
	}
	
	/**
	 * 子网掩码格式转换。如果参数是24，则返回值为：255.255.255.0。如果参数是255.255.255.0，则返回值为：24
	 * @param mask 待转换的子网掩码
	 * @return 转换格式后的子网掩码
	 * @throws InvalidIpMaskException
	 */
	public static String maskStyleChange(String mask) throws InvalidIpMaskException{
		if(!isValidMask(mask)){
			throw new InvalidIpMaskException();
		}
		String result="";
		if(mask.indexOf(".")==-1){
			result=ipMask[Integer.parseInt(mask)];
		}else{
			for(int i=0;i<ipMask.length;i++){
				if(ipMask[i].equals(mask)){
					result=i+"";
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * 判断一个输入的子网掩码是否合法，子网掩码可以是十进制或点分十进制格式（如：24或255.255.255.0）
	 * 合法的子网掩码包括0——32
	 * @param mask
	 * @return
	 */
	public static boolean isValidMask(String mask){
		if(mask.indexOf(".")==-1){
			try{
				int maskInt=Integer.parseInt(mask);
				if(maskInt>32 || maskInt<0)
					return false;
				else
					return true;
			}catch(Exception e){
				return false;
			}
		}else{
			for(String temp:ipMask){
				if(temp.equals(mask))
					return true;
			}
			return false;
		}
	}
	
	/**
	 * 返回所有可能的子网掩码
	 * @return
	 */
	public static String[] getAllPossibleMask(){
		return ipMask;
	}
	
	/**
	 * 返回ip地址的类别
	 * @param ip
	 * @return 0-回环地址;1-A类地址;2-B类地址;3-C类地址;4-D类（组播）地址;5-E类（保留）地址
	 * @throws InvalidIpAddressException
	 */
	public static int getIpClass(String ip) throws InvalidIpAddressException{
		int[] ipArray=null;
		try{
			ipArray=getIpArray(ip);
		}catch(InvalidIpAddressException e){
			throw new InvalidIpAddressException();
		}
		int first=ipArray[0];
		if(first==127)
			return 0;
		else if(first<127)
			return 1;
		else if(first<192)
			return 2;
		else if(first<224)
			return 3;
		else if(first<240)
			return 4;
		else if(first<255)
			return 5;
		else
			return -1;
	}
	
	/**
	 * 根据输入的ip地址和子网掩码判断网络号
	 * 
	 * @param ip String 格式为点分十进制
	 * @param mask String 格式为点分十进制或掩码位数
	 * @return
	 * @throws InvalidIpAddressException
	 * @throws InvalidIpMaskException
	 */
	public static String getNetworkNumber(String ip,String mask) throws InvalidIpAddressException,InvalidIpMaskException{
		String result="";
		try{
			int[] ipArray=getIpArray(ip);
			int[] maskArray=getMaskArray(mask);
			for(int i=0;i<ipArray.length;i++){
				result=result.concat(Integer.toString(ipArray[i] & maskArray[i]));
				if(i<3)
					result=result.concat(".");
			}
		}catch(InvalidIpAddressException e){
			throw new InvalidIpAddressException();
		}catch(InvalidIpMaskException e){
			throw new InvalidIpMaskException();
		}
		return result;
	}
	
	/**
	 * 根据输入的ip地址和子网掩码判断广播地址
	 * @param ip String 格式为点分十进制
	 * @param mask String 格式为点分十进制或掩码位数
	 * @return
	 * @throws InvalidIpAddressException
	 * @throws InvalidIpMaskException
	 */
	public static String getBroadcastNumber(String ip,String mask)throws InvalidIpAddressException,InvalidIpMaskException{
		String result="";
		try{
			int[] ipArray=getIpArray(ip);
			int[] maskArray=getMaskArray(mask);
			for(int i=0;i<ipArray.length;i++){
				result=result.concat(Integer.toString(~maskArray[i]+256 | ipArray[i]));
				if(i<3)
					result=result.concat(".");
			}
		}catch(InvalidIpAddressException e){
			throw new InvalidIpAddressException();
		}catch(InvalidIpMaskException e){
			throw new InvalidIpMaskException();
		}
		return result;
	}
	
	/**
	 * 根据输入的ip地址和子网掩码，返回该网段可分配的开始IP地址和结束IP地址
	 * @param ip
	 * @param mask
	 * @return 返回包含两个元素的字符串数组，下标为0的数组（[0]）为开始IP地址、null表示没有开始IP地址，下标为1的数组（[1]）为结束IP地址、null表示没有结束IP地址
	 * @throws InvalidIpAddressException
	 * @throws InvalidIpMaskException
	 */
	public static String[] getStartAndEndIpInSubnet(String ip,String mask) throws InvalidIpAddressException,InvalidIpMaskException{
		String[] result=new String[2];
		try{
			String startIp="";
			String endIp="";
			int[] ipArray=getIpArray(ip);
			int[] maskArray=getMaskArray(mask);
			for(int i=0;i<ipArray.length;i++){
				if(i<ipArray.length-1){
					startIp = startIp.concat(Integer.toString(ipArray[i] & maskArray[i])).concat(".");
	                endIp = endIp.concat(Integer.toString(~maskArray[i]+256 | ipArray[i])).concat(".");
				}else if(i==ipArray.length-1){
					if(maskArray[i] != 255 && maskArray[i] != 254) {
	                    startIp = startIp.concat(Integer.toString((ipArray[i] & maskArray[i]) + 1));
	                    endIp = endIp.concat(Integer.toString((~maskArray[i]+256 | ipArray[i]) - 1));
	                }
	                else {
	                    startIp = null;
	                    endIp = null;
	                }
				}
			}
			result[0]=startIp;
			result[1]=endIp;
			
		}catch(InvalidIpAddressException e){
			throw new InvalidIpAddressException();
		}catch(InvalidIpMaskException e){
			throw new InvalidIpMaskException();
		}
		return result;
	}
	
	/**
	 * 根据开始IP和结束IP，获取所有可用的IP地址
	 * @param startIp 开始IP
	 * @param endIp 结束IP
	 * @return 
	 * @throws InvalidIpAddressException
	 */
	public static List<String> getAllValidIpBetweenStartAndEndIp(String startIp,String endIp) throws InvalidIpAddressException{
		List<String> result=new ArrayList<String>(); //存放结果
		
		//计算开始ip和结束ip的整型数组，已备后面的计算。
		long sIp=ipToLong(startIp);
		long eIp=ipToLong(endIp);
		long width=eIp-sIp;
		
		//开始计算
		for(long i=0;i<=eIp-sIp;i++){
			long temp=sIp+i;
			String ipTemp=longToIp(temp);
			if(isValidIp(ipTemp,true))
				result.add(ipTemp);
		}
		
		return result;
	}
	
	/**
	 * 判断ip地址是公网地址还是私网地址
	 * @param ip
	 * @return
	 * @throws InvalidIpAddressException
	 */
	public static boolean isInnerIP(String ip) throws InvalidIpAddressException{
		boolean result=false;
		long ipLong=ipToLong(ip);
		if ((ipLong >> 24 == 0xa) || (ipLong >> 16 == 0xc0a8) || (ipLong >> 22 == 0x2b0)){   
			result = true;   
		} 
		return result;
	}
	
	/**
	 * 将输入的ip地址（x.x.x.x形式)转换成整形数组
	 * @param ip
	 * @return
	 * @throws InvalidIpAddressException
	 */
	private static int[] getIpArray(String ip) throws InvalidIpAddressException{
		if(!isValidIp(ip,false))
			throw new InvalidIpAddressException();
		String[] ips=ip.split("\\.");
		int[] ipArray=new int[ips.length];
		for(int i=0;i<ips.length;i++){
			ipArray[i]=Integer.parseInt(ips[i]);
		}
		return ipArray;
	}
	
	/**
	 * 将输入的子网掩码（x.x.x.x形式或xx形式）转换成整形数组
	 * @param mask
	 * @return
	 * @throws InvalidIpMaskException
	 */
	private static int[] getMaskArray(String mask) throws InvalidIpMaskException{
		if(!isValidMask(mask))
			throw new InvalidIpMaskException();
		String[] maskTemp=mask.split("\\.");
		if(maskTemp.length==1){
			maskTemp=ipMask[Integer.parseInt(maskTemp[0])].split("\\.");
		}
		int[] maskArray=new int[maskTemp.length];
		for(int i=0;i<maskArray.length;i++){
			maskArray[i]=Integer.parseInt(maskTemp[i]);
		}
		
		return maskArray;
	}
	
	/**
	 * 将点分十进制的ip地址转换为整型形式。如ip地址为10.0.3.193，转换后的结果为167773121
	 * @param ip
	 * @return
	 * @throws InvalidIpAddressException
	 */
	private static long ipToLong(String ip) throws InvalidIpAddressException{
		if(!isValidIp(ip,false))
			throw new InvalidIpAddressException();
		long result=0;
		StringTokenizer token=new StringTokenizer(ip,".");
		result+=Long.parseLong(token.nextToken())<<24;
		result+=Long.parseLong(token.nextToken())<<16;
		result+=Long.parseLong(token.nextToken())<<8;
		result+=Long.parseLong(token.nextToken());
		return result;
	}
	
	/**
	 * 将整型ip地址转换为点分十进制形式。如将167773121转换为ip地址10.0.3.193。
	 * @param ip
	 * @return
	 */
	private static String longToIp(Long ip){
		String result="";
		if(ip<0)
			return null;
		StringBuilder sb=new StringBuilder();
		sb.append(ip>>>24);
		sb.append(".");
		sb.append(String.valueOf((ip&0x00FFFFFF)>>>16));
		sb.append(".");
		sb.append(String.valueOf((ip&0x0000FFFF)>>>8));
		sb.append(".");
		sb.append(String.valueOf(ip&0x000000FF));
		return sb.toString();
	}
	
	
	public static void main(String[] args) throws Exception{
		try {
			InetAddress in=InetAddress.getLocalHost();
			InetAddress[] all = InetAddress.getAllByName(in.getHostName());   
			for(InetAddress address:all){
				String tmp=address.getHostAddress().toString();
				System.out.print("IP="+tmp);
				if(isInnerIP(tmp))
					System.out.print("内网IP"+"\n");
				else
					System.out.print("外网IP"+"\n");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}
	
}
