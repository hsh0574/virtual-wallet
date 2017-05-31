package com.yzz.virtual.wallet.services.assistant;

public class SyncKeyResult {
	private String key;
	private int sign;
	public SyncKeyResult(String key,int sign){
		this.key=key;
		this.sign=sign;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getSign() {
		return sign;
	}
	public void setSign(int sign) {
		this.sign = sign;
	}
}
