package com.yzz.virtual.wallet.entity;

public enum AccountStatus {
	/**
	 * ��Ч
	 */
	INVALID(0,"��Ч"),
	/**
	 * ����
	 */
	NORMAL(1,"����"),
	/**
	 * ����
	 */
	DISABLE(2,"����");
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
