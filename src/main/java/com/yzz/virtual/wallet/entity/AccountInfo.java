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
	 * 构造方法
	 * @param ret 1存在且正常；4系统内部错误；17不存在；18电子钱包账号异常
	 */
	public AccountInfo(int ret){
		this.ret=ret;
	}
	private int ret;
	private int status;
	private String payPwd;
	private Integer balance;
	/**
	 * 账户检查结果：1存在且正常；4系统内部错误；17不存在；18电子钱包账号异常
	 * @return
	 */
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	/**
	 * 账户状态：0未创建；1正常；2禁用；3转移；4转移中
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
