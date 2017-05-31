/**
 * 
 */
package com.yzz.virtual.wallet.entity;

import com.yzz.virtual.wallet.entity.WithdrawNote.NoteCloseType;
import com.yzz.virtual.wallet.entity.WithdrawNote.Status;

/**
 * @ClassName WithdrawNoteWhere
 * @Description TODO
 * @author zhengzhou.yang
 * @date 2015-4-13
 *
 */
public class WithdrawNoteWhere {
	private String uadwid;//提现记录编号
	private String account;//所属钱包帐号
	private NoteCloseType noteCloseType;//关闭类型
	private Status status;//状态
	private int froms=1;//商户来源（1，默认1）
	private String batch;//批次号
	private String beginDate;//交易时间段（开始时间，yyyy-MM-dd HH:mm:ss）
	private String endDate;//交易时间段(结束时间，yyyy-MM-dd HH:mm:ss)
	private boolean isManager=false;//是否后台操作
	private String handler;//后台操作人
	
	public WithdrawNoteWhere(){}
	public WithdrawNoteWhere(String account){
		this.account=account;
	}
	public WithdrawNoteWhere(boolean isManager){
		this.isManager=isManager;
	}
	public String getUadwid() {
		return uadwid;
	}
	public void setUadwid(String uadwid) {
		this.uadwid = uadwid;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public NoteCloseType getNoteCloseType() {
		return noteCloseType;
	}
	public void setNoteCloseType(NoteCloseType noteCloseType) {
		this.noteCloseType = noteCloseType;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public int getFroms() {
		return froms;
	}
	public void setFroms(int froms) {
		this.froms = froms;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
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
	public boolean isManager() {
		return isManager;
	}
	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}
	public String getHandler() {
		return handler;
	}
	public void setHandler(String handler) {
		this.handler = handler;
	}

	public enum ModeType{
		/**
		 * 查询
		 */
		SELECT(1,"查询"),
		/**
		 * 导出
		 */
		EXPORT(2,"提现导出"),
		/**
		 * 生成批次并导出
		 */
		EXPORT_NEW_BATCH(3,"提现生成批次并导出");
		
		private int value = 0;
		private String name="";
		private ModeType(int value,String name) {
			this.value = value;
			this.name = name;
		}
		public int getValue(){
			return this.value;
		}
		public String getName(){
			return this.name;
		}
	}
}
