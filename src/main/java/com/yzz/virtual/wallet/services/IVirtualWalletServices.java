/**
 * 
 */
package com.yzz.virtual.wallet.services;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.yzz.virtual.wallet.entity.AccountInfo;
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

/**
 * @ClassName VirtualWalletServices
 * @Description TODO
 * @author zhengzhou.yang
 * @date 2015-4-13
 *
 */
public interface IVirtualWalletServices {
	

	/**
	 * 检查账户信息
	 * @param account 虚拟账户
	 * @param froms 来源
	 * @return AccountInfo
	 */
	public AccountInfo checkAccount(String account,int froms);

	/**
	 * 检查账户信息
	 * @param account 虚拟账户
	 * @param froms 来源
	 * @param isCreated 不存在账户时，是否创建。true将自动创建
	 * @return AccountInfo
	 */
	public AccountInfo checkAccount(String account,int froms,boolean isCreated);
	
	/**
	 * 创建虚拟账户
	 * 
	 * @param account 虚拟账户
	 * @param pwd  密码
	 * @param froms 来源
	 * @return 0成功；4系统内部错误
	 */
	public int created_account(String account,String pwd,int froms);

	/**
	 * 激活虚拟账户
	 * @param account
	 * @param pwd 可以为null
	 * @param froms 来源
	 * @return 0成功；4系统错误(失败)
	 */
	public int activation_account(String account,String pwd,int froms);
	
	/**
	 * 修改支付密码
	 * @param account 虚拟账户
	 * @param oldPwd 数据库中原始密码
	 * @param newPwd 新密码
	 * @param froms 来源
	 * @return 0成功；4修改失败
	 */
	public int change_paypwd(String account,String oldPwd, String newPwd, int froms);
	
	/**
	 * 获取账户余额
	 * 
	 * @param account 虚拟账户
	 * @param froms 来源
	 * @return 账户余额（-101系统内部错误）
	 */
	public Integer get_balance(String account, int froms);
	
	/**
	 * 获取虚拟账号信息
	 * @param page 分页对象
	 * @param account 虚拟账号
	 * @param status 状态
	 * @param beginDate 创建时间段（开始时间）
	 * @param endDate 创建时间段（结束时间）
	 * @param froms 来源
	 */
	public void getAccounts(PageUtil<Map, ?> page,String account,AccountStatus status,String beginDate,String endDate,int froms);
	
	/**
	 * 虚拟账户交易
	 * @param bNote
	 * @return 0成功；1失败；2参数错误；4系统错误；8余额不足；25交易记录已经存在
	 */
	public Feedback business(BusinessNote bNote);

	/**
	 * 虚拟账户交易
	 * @param conn
	 * @param bNote
	 * @return 0成功；1失败；2参数错误；4系统错误；8余额不足；25交易记录已经存在
	 */
	public Feedback business(Connection conn,BusinessNote bNote) throws Exception;

	/**
	 * 获得记录过期处理
	 * @return
	 */
	public Feedback overdueHandle();
	
	/**
	 * 
	 * 获取虚拟账户交易记录
	 * @param page 分页对象
	 * @return
	 */
	public void get_business_notes(PageUtil<Map,BusinessNoteWhere> page);
	

	/**
	 * 虚拟账户提现
	 * @param wNote 提现信息
	 * @param froms  来源
	 * @return 0成功；1失败；8余额不足；4系统错误
	 */
	public Feedback withdraw(WithdrawNote wNote,int froms);

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
	 * @param account 虚拟账户
	 * @param uadwid 提现信息编号
	 * @param loseReason 提现信息记录关闭原因
	 * @param froms 来源
	 * @param handler 关闭操作人
	 * @param closeType 关闭类型（1审核；2退票；3非退票的提现失败）
	 * @return 0成功；1失败；26提现记录不存在；4系统错误
	 */
	public int withdrawClose(String account,String uadwid,String loseReason,String handler,NoteCloseType closeType,int froms);
	
	/**
	 * 获取虚拟账户提现记录
	 * @param page 分页对象
	 * @param modeType 获取处理方式
	 */
	public Feedback get_withdraw_notes(PageUtil<Map,WithdrawNoteWhere> page,ModeType modeType);
	

	/**
	 * 获取系统参数值
	 * @param name 名称
	 * @return （返回null时，说明获取失败）
	 */
	public Object getSystemParam(String name);
	

	/**
	 * 获取当前账户提现次数
	 * @return 提现次数值。（返回-100时，说明获取失败）
	 */
	public int getWithdrawCount(String account);
	

	/*********************基础信息 begin********************************/
	
	/**
	 * 获取地区信息
	 * @param parentCode
	 * @return
	 */
	public List<Area> getAreas(String parentCode);
	
	/**
	 * 获取提现银行信息
	 * @return
	 */
	public List<Bank> getBanks();

	/*********************基础信息 end********************************/
	
	/*********************统计信息 begin********************************/
	
	/**
	 * 查询日账单流水数据
	 * @param page
	 * @param beginDate 时间段（开始日期。格式：yyyy-MM-dd） 
	 * @param endDate 时间段（结束日期。格式：yyyy-MM-dd） 
	 */
	public void findStatDays(PageUtil page, String beginDate, String endDate);

	/**
	 * 查询月账单流水数据
	 * @param page
	 * @param beginDate 时间段（开始日期。格式：yyyy-MM-dd） 
	 * @param endDate 时间段（结束日期。格式：yyyy-MM-dd） 
	 */
	public void findStatMonths(PageUtil page, String beginDate, String endDate);
	
	/*********************统计信息 end********************************/
	
}
