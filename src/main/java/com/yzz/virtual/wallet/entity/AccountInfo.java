/**
 * 
 */
package com.yzz.virtual.wallet.entity;

/**
 * @ClassName AccountInfo
 * @Description TODO
 * @author zhengzhou.yang
 * @date 2015-4-13
 *
 */
public class AccountInfo {
	public AccountInfo(){};
	/**
	 * ���췽��
	 * @param ret 1������������4ϵͳ�ڲ�����17�����ڣ�18����Ǯ���˺��쳣
	 */
	public AccountInfo(int ret){
		this.ret=ret;
	}
	private int ret;
	private int status;
	private String payPwd;
	private Integer balance;
	/**
	 * �˻��������1������������4ϵͳ�ڲ�����17�����ڣ�18����Ǯ���˺��쳣
	 * @return
	 */
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	/**
	 * �˻�״̬��0δ������1������2���ã�3ת�ƣ�4ת����
	 * @return
	 */
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getPayPwd() {
		return payPwd;
	}
	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}
	public Integer getBalance() {
		return balance;
	}
	public void setBalance(Integer balance) {
		this.balance = balance;
	}
}
