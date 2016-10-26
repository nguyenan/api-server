package com.wut.support.networking;


public class MacAddressHelper {

	public static String getHostMacAddress() {
		StringBuilder sb = new StringBuilder();
		sb.append("1.1.1.1");
		return sb.toString();
	}

	public static void main(String[] args) {
		String macAddr = MacAddressHelper.getHostMacAddress();
		System.out.print("Mac Address: ");
		System.out.println(macAddr);
	}
}