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
	private String uaddid;//���׼�¼���
	private String account;//����Ǯ���ʺ�
	private int types=0;//���ͣ�1֧����2���롣0��ʾȫ����
	private SubTypes subTypes;//������(���ֶ�ֻ�����������ݣ�������չʾ)
	private int froms=1;//�̻���Դ��1��Ĭ��1��
	private String outTradeNo;//�̻���ˮ�ţ�ͬһ�̻���ˮ�Ų����ظ���
	private Integer outType;//�̻����ͱ�ʶ
	private String beginDate;//����ʱ��Σ���ʼʱ�䣬yyyy-MM-dd HH:mm:ss��
	private String endDate;//����ʱ���(����ʱ�䣬yyyy-MM-dd HH:mm:ss)
	private String targetAccount;//Ŀ��Ǯ���˺ţ�ת�˽��ף�
	private int onlyNote=-1;//�Ƿ�����Ǽ�¼������ʾ��0����[��������ʾ]��1ֻ�Ǽ�¼[�����в���ʾ]����-1��ѯȫ��
	private boolean isManager=false;//�Ƿ��̨����
	
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
