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
	private String uadwid;//���ּ�¼���
	private String account;//����Ǯ���ʺ�
	private NoteCloseType noteCloseType;//�ر�����
	private Status status;//״̬
	private int froms=1;//�̻���Դ��1��Ĭ��1��
	private String batch;//���κ�
	private String beginDate;//����ʱ��Σ���ʼʱ�䣬yyyy-MM-dd HH:mm:ss��
	private String endDate;//����ʱ���(����ʱ�䣬yyyy-MM-dd HH:mm:ss)
	private boolean isManager=false;//�Ƿ��̨����
	private String handler;//��̨������
	
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
		 * ��ѯ
		 */
		SELECT(1,"��ѯ"),
		/**
		 * ����
		 */
		EXPORT(2,"���ֵ���"),
		/**
		 * �������β�����
		 */
		EXPORT_NEW_BATCH(3,"�����������β�����");
		
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
