package net.jpanda.util.net;

public class PortTool {

	/**
	 * 判断一个端口号是否合法
	 * @param port
	 * @return
	 */
	public static boolean isValidPort(int port){
		return (port>=1 && port<=65535)?true:false;
	}
}
