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
	private String uadwid;//记录编号，主键
	@ColumnAnnotation(name="dzqb_no")
	private String account;//提现电子钱包账号
	@ColumnAnnotation(name="fees")
	private double fees;//提现金额
	@ColumnAnnotation(name="fact_fee")
	private double factFee;//实际提现到账金额
	@ColumnAnnotation(name="counter_fee")
	private double counterFee;//提现手续费
	@ColumnAnnotation(name="bank_code")
	private String bankCode;//提现银行代码
	@ColumnAnnotation(name="bank_name")
	private String bankName;//提现银行名称
	@ColumnAnnotation(name="province_code")
	private String provinceCode;//开户省代码
	@ColumnAnnotation(name="city_code")
	private String cityCode;//开户市代码
	@ColumnAnnotation(name="sub_bank_name")
	private String subBankName;//支行信息
	@ColumnAnnotation(name="mode")
	private int mode=2;//提现方式（1微信钱包；2银行卡。默认为2）
	@ColumnAnnotation(name="bank_no")
	private String bankNo;//提现银行账号
	@ColumnAnnotation(name="bank_user_name")
	private String bankUserName;//提现银行用户姓名
	private String remark;//用户填写备注说明
	private int status;//状态（状态：0发起中、1提现中、2已关闭（申请失败或提现失败）、3提现成功。默认为0）
	@ColumnAnnotation(name="serial_no")
	private String serialNo;//申请提现流水号。这里为电子钱包明细表的主键uaddid
	@ColumnAnnotation(name="lose_reason")
	private String loseReason;//关闭原因
	@ColumnAnnotation(name="created",insert=true)
	private Date created;//申请时间
	private int froms=1;//来源
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
		//1审核；2退票；3非退票的提现失败
		/**
		 * 审核
		 */
		AUDIT(1,"审核"),
		/**
		 * 退票
		 */
		FAIL_REFUND_TICKET(2,"退票"),
		/**
		 * 非退票的提现失败
		 */
		FAIL_OTHER(3,"提现失败");
		
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
		//状态（0用户申请[待财审]、1提现中[提现接口请求成功]、2已关闭[最终态，申请失败或提现失败]、
		//3提现成功[最终态]、10财审通过。默认为0）

		/**
		 * 申请中
		 */
		APPLY(0,"申请中"),
		/**
		 * 提现中
		 */
		WITHDRAW(1,"提现中"),
		/**
		 * 财审通过
		 */
		AUDIT(10,"财审通过"),
		/**
		 * 关闭
		 */
		CLOSE(2,"关闭"),
		/**
		 * 成功
		 */
		SUCCESS(3,"成功");
		
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
