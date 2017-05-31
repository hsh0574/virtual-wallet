package com.yzz.virtual.wallet.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.yzz.virtual.wallet.config.LocalCommon;

public class Feedback implements Serializable{
	private static final long serialVersionUID = -5506352863213202120L;
	
	private int code=0;//0�ɹ���Ĭ��ֵ
	private String message="";
	private Map<String,Object> info=new HashMap<String,Object>();
	
	public Feedback(){}
	public Feedback(int code,String message){
		this.code=code;
		this.message=message;
	}
	public Feedback(int code){
		this.code=code;
		this.message=LocalCommon.WEIXIN_ERROR_CODE_MSG.get(code);
	}
	
	/**
	 * �����������
	 * @return 0�ɹ�
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * �����������
	 * @param code
	 */
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, Object> getInfo() {
		return info;
	}
	public void setInfo(Map<String, Object> info) {
		this.info = info;
	}
	
}
