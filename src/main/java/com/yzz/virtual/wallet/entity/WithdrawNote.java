/**
 * 
 */
package com.yzz.virtual.wallet.entity;

import java.util.Date;

import com.yzz.virtual.wallet.entity.BusinessNote.SubTypes;

import cn.com.yofogo.frame.dao.annotation.ColumnAnnotation;
import cn.com.yofogo.frame.dao.annotation.TableAnnotation;

/**
 * @ClassName WithdrawNote
 * @Description TODO
 * @author zhengzhou.yang
 * @date 2015-4-13
 *
 */
@TableAnnotation(name="user_account_dzqb_withdraw")
public class WithdrawNote {
	@ColumnAnnotation(name="uadwid",primaryKey=true)
	private String uadwid;//��¼��ţ�����
	@ColumnAnnotation(name="dzqb_no")
	private String account;//���ֵ���Ǯ���˺�
	@ColumnAnnotation(name="fees")
	private double fees;//���ֽ��
	@ColumnAnnotation(name="fact_fee")
	private double factFee;//ʵ�����ֵ��˽��
	@ColumnAnnotation(name="counter_fee")
	private double counterFee;//����������
	@ColumnAnnotation(name="bank_code")
	private String bankCode;//�������д���
	@ColumnAnnotation(name="bank_name")
	private String bankName;//������������
	@ColumnAnnotation(name="province_code")
	private String provinceCode;//����ʡ����
	@ColumnAnnotation(name="city_code")
	private String cityCode;//�����д���
	@ColumnAnnotation(name="sub_bank_name")
	private String subBankName;//֧����Ϣ
	@ColumnAnnotation(name="mode")
	private int mode=2;//���ַ�ʽ��1΢��Ǯ����2���п���Ĭ��Ϊ2��
	@ColumnAnnotation(name="bank_no")
	private String bankNo;//���������˺�
	@ColumnAnnotation(name="bank_user_name")
	private String bankUserName;//���������û�����
	private String remark;//�û���д��ע˵��
	private int status;//״̬��״̬��0�����С�1�����С�2�ѹرգ�����ʧ�ܻ�����ʧ�ܣ���3���ֳɹ���Ĭ��Ϊ0��
	@ColumnAnnotation(name="serial_no")
	private String serialNo;//����������ˮ�š�����Ϊ����Ǯ����ϸ�������uaddid
	@ColumnAnnotation(name="lose_reason")
	private String loseReason;//�ر�ԭ��
	@ColumnAnnotation(name="created",insert=true)
	private Date created;//����ʱ��
	private int froms=1;//��Դ
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
	public double getFees() {
		return fees;
	}
	public void setFees(double fees) {
		this.fees = fees;
	}
	public double getFactFee() {
		return factFee;
	}
	public void setFactFee(double factFee) {
		this.factFee = factFee;
	}
	public double getCounterFee() {
		return counterFee;
	}
	public void setCounterFee(double counterFee) {
		this.counterFee = counterFee;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getSubBankName() {
		return subBankName;
	}
	public void setSubBankName(String subBankName) {
		this.subBankName = subBankName;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getBankUserName() {
		return bankUserName;
	}
	public void setBankUserName(String bankUserName) {
		this.bankUserName = bankUserName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getLoseReason() {
		return loseReason;
	}
	public void setLoseReason(String loseReason) {
		this.loseReason = loseReason;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public int getFroms() {
		return froms;
	}
	public void setFroms(int froms) {
		this.froms = froms;
	}
	
	public enum NoteCloseType{
		//1��ˣ�2��Ʊ��3����Ʊ������ʧ��
		/**
		 * ���
		 */
		AUDIT(1,"���"),
		/**
		 * ��Ʊ
		 */
		FAIL_REFUND_TICKET(2,"��Ʊ"),
		/**
		 * ����Ʊ������ʧ��
		 */
		FAIL_OTHER(3,"����ʧ��");
		
		private int value = 0;
		private String name="";
		private NoteCloseType(int value,String name) {
			this.value = value;
			this.name = name;
		}
		public int getValue(){
			return this.value;
		}
		public String getName(){
			return this.name;
		}
		public static NoteCloseType valueOf(int value){
			switch (value) {
				case 1: return AUDIT;
				case 2: return FAIL_REFUND_TICKET;
				case 3: return FAIL_OTHER;
				default: return null;
			}
		}
	}
	
	public enum Status{
		//״̬��0�û�����[������]��1������[���ֽӿ�����ɹ�]��2�ѹر�[����̬������ʧ�ܻ�����ʧ��]��
		//3���ֳɹ�[����̬]��10����ͨ����Ĭ��Ϊ0��

		/**
		 * ������
		 */
		APPLY(0,"������"),
		/**
		 * ������
		 */
		WITHDRAW(1,"������"),
		/**
		 * ����ͨ��
		 */
		AUDIT(10,"����ͨ��"),
		/**
		 * �ر�
		 */
		CLOSE(2,"�ر�"),
		/**
		 * �ɹ�
		 */
		SUCCESS(3,"�ɹ�");
		
		private int value = 0;
		private String name="";
		private Status(int value,String name) {
			this.value = value;
			this.name = name;
		}
		public int getValue(){
			return this.value;
		}
		public String getName(){
			return this.name;
		}
		public static Status valueOf(int value){
			switch (value) {
				case 0: return APPLY;
				case 1: return WITHDRAW;
				case 2: return CLOSE;
				case 3: return SUCCESS;
				case 10: return AUDIT;
				default: return null;
			}
		}
	}
}
