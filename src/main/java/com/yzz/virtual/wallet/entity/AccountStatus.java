package com.yzz.virtual.wallet.entity;

public enum AccountStatus {
	/**
	 * 无效
	 */
	INVALID(0,"无效"),
	/**
	 * 正常
	 */
	NORMAL(1,"正常"),
	/**
	 * 禁用
	 */
	DISABLE(2,"禁用");
	private int value = 0;
	private String name="";
	private AccountStatus(int value,String name) {
		this.value = value;
		this.name = name;
	}
	public int getValue(){
		return this.value;
	}
	public String getName(){
		return this.name;
	}
	public static AccountStatus valueOf(int value){
		switch (value) {
			case 0: return INVALID;
			case 1: return NORMAL;
			case 2: return DISABLE;
			default: return null;
		}
	}
}
