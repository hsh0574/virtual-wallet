/**
 * 
 */
package com.yzz.virtual.wallet.entity;

import java.util.Date;

/**
 * @ClassName BusinessNote
 * @Description TODO
 * @author zhengzhou.yang
 * @date 2015-4-13
 *
 */
public class BusinessNote {
	private String account;//所属钱包帐号
	private String accountName;//所属钱包帐号名称
	private SubTypes subTypes;//子类型(该字段只用于区分数据，不用于展示)
	private int money;//金额（收入为正数、支出为负数）
	private String remarks;//备注，展示给用户
	private int froms=1;//商户来源（1，默认1）
	private String outTradeNo;//商户流水号（同一商户流水号不能重复）
	private int outType=0;//商户类型标识
	private String outTypeName;//商户类型名称
	private String outOther;//商户其他信息
	private String reqIP;//请求IP地址
	private String targetAccount;//目标钱包账号（转账交易）
	private String targetAccountName;//目标钱包账号名称（转账交易）
	private Date useBegin;//开始可用时间（主要用于原路返回及过期）
	private Date useEnd;//过期时间（主要用于原路返回及过期）
	
	//其他字段
	private int useMoney;
	private int useStatus;
	private Date upDate;
	private String useTarget;//原路返回时，原支付单号
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public SubTypes getSubTypes() {
		return subTypes;
	}
	public void setSubTypes(SubTypes subTypes) {
		this.subTypes = subTypes;
	}
	public int getMoney() {
		return money;
	}
	/**
	 * 如果是人民币，需要转换为单位（分）
	 * @param money
	 */
	public void setMoney(int money) {
		this.money = money;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public int getFroms() {
		return froms;
	}
	/*public void setFroms(int froms) {
		this.froms = froms;
	}*/
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public int getOutType() {
		return outType;
	}
	public void setOutType(int outType) {
		this.outType = outType;
	}
	public String getOutTypeName() {
		return outTypeName;
	}
	public void setOutTypeName(String outTypeName) {
		this.outTypeName = outTypeName;
	}
	public String getOutOther() {
		return outOther;
	}
	public void setOutOther(String outOther) {
		this.outOther = outOther;
	}
	public String getReqIP() {
		return reqIP;
	}
	public void setReqIP(String reqIP) {
		this.reqIP = reqIP;
	}
	public String getTargetAccount() {
		return targetAccount;
	}
	public void setTargetAccount(String targetAccount) {
		this.targetAccount = targetAccount;
	}
	public String getTargetAccountName() {
		return targetAccountName;
	}
	public void setTargetAccountName(String targetAccountName) {
		this.targetAccountName = targetAccountName;
	}
	public Date getUseBegin() {
		return useBegin;
	}
	/**
	 * 开始可用时间（主要用于原路返回及过期）
	 * @param useBegin
	 */
	public void setUseBegin(Date useBegin) {
		this.useBegin = useBegin;
	}
	public Date getUseEnd() {
		return useEnd;
	}
	/**
	 * 过期时间（主要用于原路返回及过期）
	 * @param useEnd
	 */
	public void setUseEnd(Date useEnd) {
		this.useEnd = useEnd;
	}
	public int getUseMoney() {
		return useMoney;
	}
	public void setUseMoney(int useMoney) {
		this.useMoney = useMoney;
	}
	/**
	 * 使用状态
	 * @return 1正常；2已用完；3已过期
	 */
	public int getUseStatus() {
		return useStatus;
	}
	public void setUseStatus(int useStatus) {
		this.useStatus = useStatus;
	}
	public Date getUpDate() {
		return upDate;
	}
	public void setUpDate(Date upDate) {
		this.upDate = upDate;
	}
	/**
	 * 原路返回时，原支付单号
	 * @return
	 */
	public String getUseTarget() {
		return useTarget;
	}
	/**
	 * 原路返回时，原支付单号
	 * @param useTarget
	 */
	public void setUseTarget(String useTarget) {
		this.useTarget = useTarget;
	}

	public enum SubTypes{
		//1收益、2转账(入)、3充值、4退款、5订单取消
		//-1购买、-2转账（出）、-3提现
		/**
		 * 收益收入（即奖金。进账）
		 */
		INCOME(1,"奖金"),
		/**
		 * 转账收入（进账）
		 */
		TURN_IN(2,"转账（进）"),
		/**
		 * 充值（进账）
		 */
		RECHARGE(3,"充值"),
		/**
		 * 退款（进账）
		 */
		REFUND(4,"退款"),
		/**
		 * 订单取消（进账）
		 */
		CANCEL(5,"订单取消"),
		/**
		 * 外部来源
		 */
		OUTSIDE(11,"转入"),
		/**
		 * 购买支付（出账）
		 */
		BUY(-1,"购物"),
		/**
		 * 转账支出（出账）
		 */
		TURN_OUT(-2,"转账（出）"),
		/**
		 * 提现（出账）
		 */
		TAKE_OUT(-3,"提现");
		private int value = 0;
		private String name="";
		private SubTypes(int value,String name) {
			this.value = value;
			this.name = name;
		}
		public int getValue(){
			return this.value;
		}
		public String getName(){
			return this.name;
		}
		public static SubTypes valueOf(int value){
			switch (value) {
				case 1: return INCOME;
				case 2: return TURN_IN;
				case 3: return RECHARGE;
				case 4: return REFUND;
				case -1: return BUY;
				case -2: return TURN_OUT;
				case -3: return TAKE_OUT;
				default: return null;
			}
		}
	}
}
