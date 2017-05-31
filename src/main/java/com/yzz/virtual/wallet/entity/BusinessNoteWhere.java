/**
 * 
 */
package com.yzz.virtual.wallet.entity;

import com.yzz.virtual.wallet.entity.BusinessNote.SubTypes;

/**
 * @ClassName BusinessNote
 * @Description TODO
 * @author zhengzhou.yang
 * @date 2015-4-13
 *
 */
public class BusinessNoteWhere {
	private String uaddid;//交易记录编号
	private String account;//所属钱包帐号
	private int types=0;//类型（1支出、2收入。0表示全部）
	private SubTypes subTypes;//子类型(该字段只用于区分数据，不用于展示)
	private int froms=1;//商户来源（1，默认1）
	private String outTradeNo;//商户流水号（同一商户流水号不能重复）
	private Integer outType;//商户类型标识
	private String beginDate;//交易时间段（开始时间，yyyy-MM-dd HH:mm:ss）
	private String endDate;//交易时间段(结束时间，yyyy-MM-dd HH:mm:ss)
	private String targetAccount;//目标钱包账号（转账交易）
	private int onlyNote=-1;//是否仅仅是记录即不显示（0不是[功能中显示]；1只是记录[功能中不显示]）。-1查询全部
	private boolean isManager=false;//是否后台操作
	
	public BusinessNoteWhere(){}
	public BusinessNoteWhere(String account){
		this.account=account;
	}
	public BusinessNoteWhere(boolean isManager){
		this.isManager=isManager;
	}
	public String getUaddid() {
		return uaddid;
	}
	public void setUaddid(String uaddid) {
		this.uaddid = uaddid;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public int getTypes() {
		return types;
	}
	public void setTypes(int types) {
		this.types = types;
	}
	public SubTypes getSubTypes() {
		return subTypes;
	}
	public void setSubTypes(SubTypes subTypes) {
		this.subTypes = subTypes;
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
	public Integer getOutType() {
		return outType;
	}
	public void setOutType(Integer outType) {
		this.outType = outType;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getTargetAccount() {
		return targetAccount;
	}
	public void setTargetAccount(String targetAccount) {
		this.targetAccount = targetAccount;
	}
	public int getOnlyNote() {
		return onlyNote;
	}
	public void setOnlyNote(int onlyNote) {
		this.onlyNote = onlyNote;
	}
	public boolean isManager() {
		return isManager;
	}
	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}
	
}
