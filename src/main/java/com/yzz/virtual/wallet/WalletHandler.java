package com.yzz.virtual.wallet;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.yzz.virtual.wallet.biz.IVirtualWalletBiz;
import com.yzz.virtual.wallet.biz.VirtualWalletBizImpl;
import com.yzz.virtual.wallet.config.ConfigureDzqb;
import com.yzz.virtual.wallet.entity.AccountStatus;
import com.yzz.virtual.wallet.entity.Area;
import com.yzz.virtual.wallet.entity.Bank;
import com.yzz.virtual.wallet.entity.BusinessNote;
import com.yzz.virtual.wallet.entity.BusinessNoteWhere;
import com.yzz.virtual.wallet.entity.Feedback;
import com.yzz.virtual.wallet.entity.WithdrawNote;
import com.yzz.virtual.wallet.entity.WithdrawNote.NoteCloseType;
import com.yzz.virtual.wallet.entity.WithdrawNote.Status;
import com.yzz.virtual.wallet.entity.WithdrawNoteWhere;
import com.yzz.virtual.wallet.entity.WithdrawNoteWhere.ModeType;

import cn.com.yofogo.frame.assistant.PageUtil;
import net.sf.json.JSONObject;

public class WalletHandler {

	private static IVirtualWalletBiz vWallet=new VirtualWalletBizImpl(new ConfigureDzqb());
	
	
	/**
	 * 创建电子钱包账户
	 * @param account 钱包账号
	 * @param pwd 钱包账号密码
	 * @param froms 来源
	 * @return Feedback
	 */
	public static Feedback createdAccount(String account,String pwd){
		return vWallet.createdAccount(account, pwd, 1);
	}
	
	/**
	 * 修改电子钱包账号支付密码
	 * @param account 钱包账号
	 * @param froms 来源
	 * @param oldPwd 原始密码(如果是首次设置密码，则为null)
	 * @param newPwd 新密码
	 * @return Feedback
	 */
	public static Feedback changePaypwd(String account,String oldPwd,String newPwd){
		return vWallet.changePaypwd(account, oldPwd, newPwd, 1);
	}
	
	

	/**
	 * 获取账户余额
	 * @param account 钱包账号
	 * @return Feedback
	 */
	public static Feedback getBalance(String account){
		return vWallet.getBalance(account, 1);
	}
	

	/**
	 * 获取钱包账号信息
	 * @param page 分页对象
	 * @param account 钱包账号
	 * @param status 状态
	 * @param beginDate 创建时间段（开始时间）
	 * @param endDate 创建时间段（结束时间）
	 */
	public static Feedback getAccounts(PageUtil<Map, ?> page,String account,AccountStatus status,String beginDate,String endDate){
		return vWallet.getAccounts(page, account, status, beginDate, endDate, 1);
	}
	
	/**
	 * 电子钱包交易
	 * @param bNote 交易内容对象
	 * @return Feedback
	 */
	public static Feedback business(BusinessNote bNote){
		return vWallet.business(bNote);
	}

	/**
	 * 虚拟账户交易
	 * @param conn
	 * @param bNote
	 * @return code(0成功；1失败；2参数错误；4系统错误；8余额不足；25交易记录已经存在)
	 */
	public static Feedback business(Connection conn,BusinessNote bNote) throws Exception{
		return vWallet.business(conn, bNote);
	}

	/**
	 * 获得记录过期处理
	 * @return
	 */
	public static Feedback overdueHandle(){
		return vWallet.overdueHandle();
	}

	/**
	 * 获取钱包交易记录
	 * @param page 分页对象
	 * @param where 查询条件对象
	 * @return Feedback
	 */
	public static Feedback getBusinessNotes(PageUtil<Map,BusinessNoteWhere> page){
		return vWallet.getBusinessNotes(page);
	}
	

	/**
	 * 钱包提现
	 * @param wNote 提现信息对象
	 * @return Feedback
	 */
	public static Feedback withdraw(WithdrawNote wNote){
		return vWallet.withdraw(wNote);
	}
	

	/**
	 * 提现初始化信息
	 * @param account 钱包账号（可为空）
	 * @return  Json数据。如：{"user":{},"counterFee":{"type":2,"counterFee":0.05}。counterFee中(type{1固定值；2百分比值})
	 */
	public static JSONObject withdrawInitialInfo(String account){
		return vWallet.withdrawInitialInfo(account);
	}
	
	/**
	 * 电子钱包提现记录审核通过
	 * @param account 电子钱包账户
	 * @param uadwid 提现信息编号
	 * @param status 处理状态
	 * @param reason 处理备注
	 * @param handler 操作人
	 * @return
	 */
	public static Feedback withdrawAudit(String account,String uadwid,Status status,String reason,String handler){
		return vWallet.withdrawAudit(account, uadwid, status,reason, handler, 1);
	}

	/**
	 * 钱包提现记录关闭
	 * @param account 钱包账号
	 * @param uadwid 提现记录
	 * @param loseReason 关闭原因
	 * @param noteCloseType 关闭类型
	 * @param handler 操作人
	 * @return Feedback
	 */
	public static Feedback withdrawClose(String account,String uadwid,String loseReason,NoteCloseType noteCloseType,String handler){
		return vWallet.withdrawClose(account, uadwid, loseReason, noteCloseType, handler, 1);
	}

	/**
	 * 获取钱包提现记录
	 * @param page 分页对象
	 * @param where 查询条件对象
	 * @return Feedback
	 */
	public static Feedback getWithdrawNotes(PageUtil<Map,WithdrawNoteWhere> page){
		return vWallet.getWithdrawNotes(page,ModeType.SELECT);
	}
	
	/**
	 * 获取钱包提现记录
	 * @param page 分页对象
	 * @param where 查询条件对象
	 * @return Feedback
	 */
	public static Feedback getWithdrawNotes(PageUtil<Map,WithdrawNoteWhere> page,ModeType modeType){
		return vWallet.getWithdrawNotes(page,modeType);
	}
	
	/**
	 * 获取地区信息
	 * @param parentCode 如果为空，则获取省级地区信息
	 * @return
	 */
	public static List<Area> getAreas(String parentCode){
		return vWallet.getAreas(parentCode);
	}
	
	/**
	 * 获取提现银行信息
	 * @return
	 */
	public static List<Bank> getBanks(){
		return vWallet.getBanks();
	}
	
	/**
	 * 查询日账单流水数据
	 * @param page
	 * @param beginDate 时间段（开始日期。格式：yyyy-MM-dd） 
	 * @param endDate 时间段（结束日期。格式：yyyy-MM-dd） 
	 */
	public static Feedback findStatDays(PageUtil page, String beginDate, String endDate){
		return vWallet.findStatDays(page, beginDate, endDate);
	}

	/**
	 * 查询月账单流水数据
	 * @param page
	 * @param beginDate 时间段（开始日期。格式：yyyy-MM-dd） 
	 * @param endDate 时间段（结束日期。格式：yyyy-MM-dd） 
	 */
	public static Feedback findStatMonths(PageUtil page, String beginDate, String endDate){
		return vWallet.findStatMonths(page, beginDate, endDate);
	}
}
