package com.yzz.virtual.wallet.biz;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

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

public interface IVirtualWalletBiz {
	/**
	 * 创建虚拟账户
	 * @param account 虚拟账号
	 * @param pwd 虚拟账号密码
	 * @param froms 来源
	 * @return Feedback
	 */
	public Feedback createdAccount(String account,String pwd,int froms);
	
	/**
	 * 修改虚拟账号支付密码
	 * @param account 虚拟账号
	 * @param oldPwd 原始密码(如果是首次设置密码，则为null)
	 * @param newPwd 新密码
	 * @param froms 来源
	 * @return Feedback
	 */
	public Feedback changePaypwd(String account,String oldPwd,String newPwd,int froms);
	
	

	/**
	 * 获取账户余额
	 * @param account 虚拟账号
	 * @param froms 来源
	 * @return Feedback
	 */
	public Feedback getBalance(String account,int froms);
	
	/**
	 * 获取虚拟账号信息
	 * @param page 分页对象
	 * @param account 虚拟账号
	 * @param status 状态
	 * @param beginDate 创建时间段（开始时间）
	 * @param endDate 创建时间段（结束时间）
	 * @param froms 来源
	 */
	public Feedback getAccounts(PageUtil<Map, ?> page,String account,AccountStatus status,String beginDate,String endDate,int froms);

	/**********************************交易 begin****************************************/
	
	/**
	 * 虚拟账户交易
	 * @param bNote 交易内容对象
	 * @return Feedback
	 */
	public Feedback business(BusinessNote bNote);

	/**
	 * 虚拟账户交易
	 * @param conn
	 * @param bNote
	 * @return code(0成功；1失败；2参数错误；4系统错误；8余额不足；25交易记录已经存在)
	 */
	public Feedback business(Connection conn,BusinessNote bNote) throws Exception;

	/**
	 * 获得记录过期处理
	 * @return
	 */
	public Feedback overdueHandle();

	/**
	 * 获取虚拟交易记录
	 * @param page 分页对象
	 * @return Feedback
	 */
	public Feedback getBusinessNotes(PageUtil<Map,BusinessNoteWhere> page);
	
	/**********************************交易 end****************************************/
	/**********************************提现 begin****************************************/
	
	/**
	 * 虚拟账户提现
	 * @param wNote 提现信息对象
	 * @return Feedback
	 */
	public Feedback withdraw(WithdrawNote wNote);

	/**
	 * 提现初始化信息
	 * @param account 虚拟账号（可为空）
	 * @return  Json数据。如：{"user":{},"counterFee":{"type":2,"counterFee":0.05}。counterFee中(type{1固定值；2百分比值})
	 */
	public JSONObject withdrawInitialInfo(String account);
	
	/**
	 * 虚拟账户提现记录审核通过
	 * @param account 虚拟账户
	 * @param uadwid 提现信息编号
	 * @param status 处理状态
	 * @param reason 处理备注
	 * @param handler 操作人
	 * @param froms 来源
	 * @return
	 */
	public Feedback withdrawAudit(String account,String uadwid,Status status,String reason,String handler,int froms);
	
	/**
	 * 虚拟账户提现记录关闭
	 * @param account 虚拟账号
	 * @param uadwid 提现记录
	 * @param loseReason 关闭原因
	 * @param noteCloseType 关闭类型
	 * @param handler 操作人
	 * @param froms 来源
	 * @return Feedback
	 */
	public Feedback withdrawClose(String account,String uadwid,String loseReason,NoteCloseType noteCloseType,String handler,int froms);

	/**
	 * 获取虚拟账户提现记录
	 * @param page 分页对象
	 * @param modeType 获取处理方式
	 * @return Feedback
	 */
	public Feedback getWithdrawNotes(PageUtil<Map,WithdrawNoteWhere> page,ModeType modeType);

	/**********************************提现 end****************************************/
	/************************************基础信息 begin********************************/
	
	/**
	 * 获取地区信息
	 * @param parentCode 如果为空，则获取省级地区信息
	 * @return
	 */
	public List<Area> getAreas(String parentCode);
	
	/**
	 * 获取提现银行信息
	 * @return
	 */
	public List<Bank> getBanks();
	
	/***********************************基础信息 end********************************/

	/*********************统计信息 begin********************************/
	
	/**
	 * 查询日账单流水数据
	 * @param page
	 * @param beginDate 时间段（开始日期。格式：yyyy-MM-dd） 
	 * @param endDate 时间段（结束日期。格式：yyyy-MM-dd） 
	 */
	public Feedback findStatDays(PageUtil page, String beginDate, String endDate);

	/**
	 * 查询月账单流水数据
	 * @param page
	 * @param beginDate 时间段（开始日期。格式：yyyy-MM-dd） 
	 * @param endDate 时间段（结束日期。格式：yyyy-MM-dd） 
	 */
	public Feedback findStatMonths(PageUtil page, String beginDate, String endDate);
	
	/*********************统计信息 end********************************/
}
