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
	private String account;//����Ǯ���ʺ�
	private String accountName;//����Ǯ���ʺ�����
	private SubTypes subTypes;//������(���ֶ�ֻ�����������ݣ�������չʾ)
	private int money;//������Ϊ������֧��Ϊ������
	private String remarks;//��ע��չʾ���û�
	private int froms=1;//�̻���Դ��1��Ĭ��1��
	private String outTradeNo;//�̻���ˮ�ţ�ͬһ�̻���ˮ�Ų����ظ���
	private int outType=0;//�̻����ͱ�ʶ
	private String outTypeName;//�̻���������
	private String outOther;//�̻�������Ϣ
	private String reqIP;//����IP��ַ
	private String targetAccount;//Ŀ��Ǯ���˺ţ�ת�˽��ף�
	private String targetAccountName;//Ŀ��Ǯ���˺����ƣ�ת�˽��ף�
	private Date useBegin;//��ʼ����ʱ�䣨��Ҫ����ԭ·���ؼ����ڣ�
	private Date useEnd;//����ʱ�䣨��Ҫ����ԭ·���ؼ����ڣ�
	
	//�����ֶ�
	private int useMoney;
	private int useStatus;
	private Date upDate;
	private String useTarget;//ԭ·����ʱ��ԭ֧������
	
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
	 * ���������ң���Ҫת��Ϊ��λ���֣�
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
	 * ��ʼ����ʱ�䣨��Ҫ����ԭ·���ؼ����ڣ�
	 * @param useBegin
	 */
	public void setUseBegin(Date useBegin) {
		this.useBegin = useBegin;
	}
	public Date getUseEnd() {
		return useEnd;
	}
	/**
	 * ����ʱ�䣨��Ҫ����ԭ·���ؼ����ڣ�
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
	 * ʹ��״̬
	 * @return 1������2�����ꣻ3�ѹ���
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
	 * ԭ·����ʱ��ԭ֧������
	 * @return
	 */
	public String getUseTarget() {
		return useTarget;
	}
	/**
	 * ԭ·����ʱ��ԭ֧������
	 * @param useTarget
	 */
	public void setUseTarget(String useTarget) {
		this.useTarget = useTarget;
	}

	public enum SubTypes{
		//1���桢2ת��(��)��3��ֵ��4�˿5����ȡ��
		//-1����-2ת�ˣ�������-3����
		/**
		 * �������루�����𡣽��ˣ�
		 */
		INCOME(1,"����"),
		/**
		 * ת�����루���ˣ�
		 */
		TURN_IN(2,"ת�ˣ�����"),
		/**
		 * ��ֵ�����ˣ�
		 */
		RECHARGE(3,"��ֵ"),
		/**
		 * �˿���ˣ�
		 */
		REFUND(4,"�˿�"),
		/**
		 * ����ȡ�������ˣ�
		 */
		CANCEL(5,"����ȡ��"),
		/**
		 * �ⲿ��Դ
		 */
		OUTSIDE(11,"ת��"),
		/**
		 * ����֧�������ˣ�
		 */
		BUY(-1,"����"),
		/**
		 * ת��֧�������ˣ�
		 */
		TURN_OUT(-2,"ת�ˣ�����"),
		/**
		 * ���֣����ˣ�
		 */
		TAKE_OUT(-3,"����");
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
