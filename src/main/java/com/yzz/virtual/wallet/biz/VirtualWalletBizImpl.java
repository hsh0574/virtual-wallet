package com.yzz.virtual.wallet.biz;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.yzz.virtual.wallet.AbsWalletConfigure;
import com.yzz.virtual.wallet.entity.AccountInfo;
import com.yzz.virtual.wallet.entity.AccountStatus;
import com.yzz.virtual.wallet.entity.Area;
import com.yzz.virtual.wallet.entity.Bank;
import com.yzz.virtual.wallet.entity.BusinessNote;
import com.yzz.virtual.wallet.entity.BusinessNote.SubTypes;
import com.yzz.virtual.wallet.entity.BusinessNoteWhere;
import com.yzz.virtual.wallet.entity.Feedback;
import com.yzz.virtual.wallet.entity.WithdrawNote;
import com.yzz.virtual.wallet.entity.WithdrawNote.NoteCloseType;
import com.yzz.virtual.wallet.entity.WithdrawNote.Status;
import com.yzz.virtual.wallet.entity.WithdrawNoteWhere;
import com.yzz.virtual.wallet.entity.WithdrawNoteWhere.ModeType;
import com.yzz.virtual.wallet.services.IVirtualWalletServices;
import com.yzz.virtual.wallet.services.VirtualWalletServicesImpl;
import com.yzz.virtual.wallet.services.assistant.SyncKeyResult;
import com.yzz.virtual.wallet.services.assistant.SynchronizedKey;

import cn.com.yofogo.frame.assistant.PageUtil;
import cn.com.yofogo.frame.dao.DBModes.ResultMode;
import cn.com.yofogo.tools.util.CipherUtil;
import cn.com.yofogo.tools.util.DateTimeUtil;
import cn.com.yofogo.tools.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class VirtualWalletBizImpl implements IVirtualWalletBiz {
	private AbsWalletConfigure config;
	private IVirtualWalletServices wallet;
	public VirtualWalletBizImpl(AbsWalletConfigure config){
		this.config=config;
		wallet=new VirtualWalletServicesImpl(config);
	}
	

	/**
	 * 创建虚拟账户
	 * @param account 虚拟账号
	 * @param pwd 虚拟账号密码
	 * @param froms 来源
	 * @return Feedback
	 */
	public Feedback createdAccount(String account,String pwd,int froms){
		// account 有问题
		if (account == null || "".equals(account.trim())) return new Feedback(1, "account参数未设置");
		else if ("null".equals(account.trim())) return new Feedback(2, "account参数不合法");
		if (!vilidateSystemFroms(froms)) return new Feedback(2, "froms参数不合法");
		// 业务处理
		Feedback fb;
		SyncKeyResult sKey=SynchronizedKey.getAccountKey(account);
		synchronized (sKey.getKey()) {
			int ret=wallet.checkAccount(account,froms).getRet();
			if(ret==17) ret=wallet.created_account(account, pwd, froms);
			else if(ret==18) ret=wallet.activation_account(account, pwd, froms);//异常
			else if(ret==1) ret=16;
			fb = new Feedback(ret);
		}
		SynchronizedKey.deleteAccountKey(sKey);
		return fb;
	}
	
	/**
	 * 修改虚拟账号支付密码
	 * @param account 虚拟账号
	 * @param oldPwd 原始密码(如果是首次设置密码，则为null)
	 * @param newPwd 新密码
	 * @param froms 来源
	 * @return Feedback
	 */
	public Feedback changePaypwd(String account,String oldPwd,String newPwd,int froms){
		// account 有问题
		if(account == null || "".equals(account.trim())) return new Feedback(1, "account参数未设置");
		if(!vilidateSystemFroms(froms)) return new Feedback(2, "froms参数不合法");
		if(newPwd==null || "".equals(newPwd.trim())) return new Feedback(1, "newPwd参数未设置");

		// 业务处理
		AccountInfo aInfo=wallet.checkAccount(account,froms);
		int ret=aInfo.getRet();
		if(ret==1){
			if(aInfo.getPayPwd()==null){
				if(oldPwd!=null) return new Feedback(22);
			} else if(!CipherUtil.validatePassword(aInfo.getPayPwd(), oldPwd==null?"":oldPwd)){
				 return new Feedback(22);
			}
			ret=wallet.change_paypwd(account,aInfo.getPayPwd(), newPwd, froms);//0成功；4修改失败
			return new Feedback(ret);
		} else return new Feedback(ret);
	}
	
	

	/**
	 * 获取账户余额
	 * @param account 虚拟账号
	 * @param froms 来源
	 * @return Feedback (info.balance为余额)
	 */
	public Feedback getBalance(String account,int froms){
		if(account == null || "".equals(account.trim())) return new Feedback(1, "account参数未设置");
		if(!vilidateSystemFroms(froms)) return new Feedback(2, "froms参数不合法");
		AccountInfo ai=wallet.checkAccount(account,froms);
		int ret=ai.getRet();
		if(ret==1){
			Feedback fb = new Feedback();
			fb.getInfo().put("balance", ai.getBalance());
			return fb;
		}else return new Feedback(ret);
	}
	
	//获取虚拟账号信息
	@Override
	public Feedback getAccounts(PageUtil<Map, ?> page,String account,AccountStatus status,String beginDate,String endDate,int froms){
		if(!vilidateSystemFroms(froms)) return new Feedback(2, "froms参数不合法");
		wallet.getAccounts(page, account, status, beginDate, endDate, froms);
		return new Feedback(0);
	}

	/**********************************交易 begin****************************************/
	
	//虚拟账户交易
	@Override
	public Feedback business(BusinessNote bNote){
		try {
			return _business(null, bNote);
		} catch (Exception e) {
			e.printStackTrace();
			 return new Feedback(4, "系统异常");
		}
	}

	//虚拟账户交易
	@Override
	public Feedback business(Connection conn,BusinessNote bNote) throws Exception{
		if(conn==null) return new Feedback(2, "conn参数不合法");
		return _business(conn, bNote);
	}
	
	//虚拟账户交易处理
	private Feedback _business(Connection conn,BusinessNote bNote) throws Exception{
		if(bNote.getAccount() == null || "".equals(bNote.getAccount().trim())) return new Feedback(1, "account参数未设置");
		if(!vilidateSystemFroms(bNote.getFroms())) return new Feedback(2, "froms参数不合法");
		if(bNote.getMoney()<=0) return new Feedback(2, "money参数不合法");
		//商户流水号（同一商户流水号不能重复，不能为空）
		if(bNote.getOutTradeNo() == null || "".equals(bNote.getOutTradeNo().trim())) return new Feedback(1, "outTradeNo参数未设置");
		if(bNote.getSubTypes() == null) return new Feedback(1, "subTypes参数未设置");
		else if(bNote.getSubTypes()==SubTypes.TURN_OUT){
			if(bNote.getTargetAccount() == null || "".equals(bNote.getTargetAccount().trim())) return new Feedback(1, "targetAccount参数未设置");
			if(bNote.getAccount().equals(bNote.getTargetAccount())) return new Feedback(19); 
		} else if(bNote.getSubTypes()==SubTypes.TURN_IN) return new Feedback(2, "subTypes参数不合法");
		if(bNote.getReqIP() == null) return new Feedback(1, "reqIP参数未设置");
		if (config.hasEffectiveTime() && bNote.getSubTypes().getValue()>0){
			if((config.sameReturnForReund_cancel() && bNote.getSubTypes()!=SubTypes.CANCEL && bNote.getSubTypes()!=SubTypes.REFUND) || !config.sameReturnForReund_cancel()){//此类型不需要有效时间段
				if(bNote.getUseBegin()==null || bNote.getUseEnd()==null) return new Feedback(2,"有效时间参数(useBegin、useEnd)未设置");
			}
		}
		Feedback fb;
		SyncKeyResult sKey=SynchronizedKey.getAccountKey(bNote.getAccount());
		synchronized (sKey.getKey()) {
			AccountInfo aInfo=wallet.checkAccount(bNote.getAccount(),bNote.getFroms(),config.realTimeCreatedAccount());
			if(aInfo.getRet()==1){//具体处理交易业务
				if(conn==null) fb=wallet.business(bNote);
				else fb=wallet.business(conn,bNote);//具体处理交易业务
			} else fb = new Feedback(aInfo.getRet());
		}
		SynchronizedKey.deleteAccountKey(sKey);
		return fb;
	}

	//获得记录过期处理
	@Override
	public Feedback overdueHandle(){
		return wallet.overdueHandle();
	}
	
	//获取虚拟账户交易记录
	@Override
	public Feedback getBusinessNotes(PageUtil<Map,BusinessNoteWhere> page){
		BusinessNoteWhere where = page.getConditions();
		if(!vilidateSystemFroms(where.getFroms())) return new Feedback(2, "froms参数不合法");
		if(!where.isManager()){
			if(where.getAccount() == null || "".equals(where.getAccount().trim())) return new Feedback(1, "account参数未设置");
			int ret=wallet.checkAccount(where.getAccount(),where.getFroms()).getRet();
			if(ret!=1) return new Feedback(ret);
		}
		wallet.get_business_notes(page);
		return  new Feedback(page.getList()==null?4:0);
	}
	
	/**********************************交易 end****************************************/
	
	/**********************************提现 begin****************************************/
	
	/**
	 * 虚拟账户提现
	 * @param wNote 提现信息对象
	 * @return Feedback
	 */
	public Feedback withdraw(WithdrawNote wNote){
		//提现时间验证
		{
			//{"type":2,"range":["1<=d<=5","d==7"]}
			Object obj=wallet.getSystemParam(config.getTablePrefix()+"withdraw_date_range");
			if(obj==null) return new Feedback(4);
			JSONObject json=JSONObject.fromObject(obj);
			int type=json.getInt("type");
			if(type!=0){
				int d=0;
				Calendar c=Calendar.getInstance();
				if(type==1){//时间（小时）
					d=c.get(Calendar.HOUR_OF_DAY);
				}else if(type==2){//周（几）
					d=c.get(Calendar.DAY_OF_WEEK)-1;
					if(d==0) d=7;
				}else if(type==3){//月（几号）
					d=c.get(Calendar.DAY_OF_MONTH);
				}
				boolean hasDate=false;
				JSONArray arrs=json.getJSONArray("range");
				for(int i=0;i<arrs.size();i++){
					try {
						Object bl = new ScriptEngineManager().getEngineByName("JavaScript").eval(arrs.getString(i).replace("d", d+""));
						if(hasDate=(Boolean)bl) break;
					} catch (ScriptException e) {
						e.printStackTrace();
						System.err.println("提现时间配置错误（系统参数“"+config.getTablePrefix()+"withdraw_date_range”）");
						return null;
					}
				}
				if(!hasDate) return new Feedback(23);
			}
		}
		if (wNote.getAccount() == null || "".equals(wNote.getAccount().trim())) return new Feedback(1, "account参数未设置");
		if(!vilidateSystemFroms(wNote.getFroms())) return new Feedback(2, "froms参数不合法");
		if(wNote.getFees()<=0) return new Feedback(2, "fees参数不合法");
		if (wNote.getBankCode() == null || "".equals(wNote.getBankCode().trim())) return new Feedback(1, "bankCode参数未设置");
		if (wNote.getBankName() == null || "".equals(wNote.getBankName().trim())) return new Feedback(1, "bankName参数未设置");
		if (wNote.getBankNo() == null || "".equals(wNote.getBankNo().trim())) return new Feedback(1, "bankNo参数未设置");
		if (wNote.getBankUserName() == null || "".equals(wNote.getBankUserName().trim())) return new Feedback(1, "bankUserName参数未设置");
		if (wNote.getProvinceCode() == null || "".equals(wNote.getProvinceCode().trim())) return new Feedback(1, "provinceCode参数未设置");
		if (wNote.getCityCode() == null || "".equals(wNote.getCityCode().trim())) return new Feedback(1, "cityCode参数未设置");
		if (wNote.getSubBankName() == null || "".equals(wNote.getSubBankName().trim())) return new Feedback(1, "subBankName参数未设置");
		if(wNote.getMode()!=2) return new Feedback(2, "mode参数不合法");
		//验证最小提现金额
		Object obj=wallet.getSystemParam(config.getTablePrefix()+"withdraw_min");
		if(obj==null) return new Feedback(4);
		double minMoney=Double.parseDouble(obj.toString());
		if(wNote.getFees()<minMoney){
			Feedback fb=new Feedback(24);
			fb.getInfo().put("minMoney",minMoney);
			return fb;
		}
		/*if(payType==2){
			try {
				wNote.setBankNo(new DES3Util(payPwd).decrypt(wNote.getBankNo()));
			} catch (Exception e) {
				e.printStackTrace();
				return new Feedback(2, "bankNo参数不合法");
			}
		}else wNote.setBankNo(bankNo);*/
		Feedback fb;
		SyncKeyResult sKey=SynchronizedKey.getAccountKey(wNote.getAccount());
		synchronized (sKey.getKey()) {
			fb=_withdraw(wNote);
		}
		SynchronizedKey.deleteAccountKey(sKey);
		return fb;
	}
	
	private Feedback _withdraw(WithdrawNote wNote){
		AccountInfo aInfo=wallet.checkAccount(wNote.getAccount(),wNote.getFroms());
		if(aInfo.getRet()==1){
			//验证提现次数
			Object obj=wallet.getSystemParam(config.getTablePrefix()+"withdraw_day_count");
			if(obj==null) return new Feedback(4);
			int allowCount=(Integer)obj;
			int myCount=wallet.getWithdrawCount(wNote.getAccount());
			if(myCount==-100) return new Feedback(4);
			else if(myCount>=allowCount){
				Feedback fb=new Feedback(21);
				fb.getInfo().put("allowCount",allowCount);
				return fb;
			}
			//具体处理提现业务【0成功；1失败；8账户余额不足；4系统错误】
			return wallet.withdraw(wNote,wNote.getFroms());
		} else return new Feedback(aInfo.getRet());
	}
	
	// 提现初始化信息
	@Override
	public JSONObject withdrawInitialInfo(String account){
		return wallet.withdrawInitialInfo(account);
	}
	
	
	//虚拟账户提现记录审核通过
	@Override
	public Feedback withdrawAudit(String account,String uadwid,Status status,String reason,String handler,int froms){
		if(status==null || (status!=Status.SUCCESS && status!=Status.AUDIT)) return new Feedback(2, "status参数不合法");
		if(account == null || "".equals(account.trim())) return new Feedback(1, "account参数未设置");
		if(!vilidateSystemFroms(froms)) return new Feedback(2, "froms参数不合法");
		if(uadwid == null || "".equals(uadwid.trim())) return new Feedback(1, "uadwid参数未设置");
		if(handler == null || "".equals(handler.trim())) return new Feedback(1, "handler参数未设置");
		Feedback fb;
		SyncKeyResult sKey=SynchronizedKey.getAccountKey(account);
		synchronized (sKey.getKey()) {
			AccountInfo aInfo=wallet.checkAccount(account,froms);
			if(aInfo.getRet()==1){
				//关闭提现记录
				fb=wallet.withdrawAudit(account, uadwid,status, reason, handler, froms);
			} else fb=new Feedback(aInfo.getRet());
		}
		SynchronizedKey.deleteAccountKey(sKey);
		return fb;
	}
	
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
	public Feedback withdrawClose(String account,String uadwid,String loseReason,NoteCloseType noteCloseType,String handler,int froms){
		if(account == null || "".equals(account.trim())) return new Feedback(1, "account参数未设置");
		if(!vilidateSystemFroms(froms)) return new Feedback(2, "froms参数不合法");
		if(uadwid == null || "".equals(uadwid.trim())) return new Feedback(1, "uadwid参数未设置");
		if(loseReason == null || "".equals(loseReason.trim())) return new Feedback(1, "loseReason参数未设置");
		if(handler == null || "".equals(handler.trim())) return new Feedback(1, "handler参数未设置");
		if(noteCloseType == null) return new Feedback(1, "noteCloseType参数未设置");
		Feedback fb;
		SyncKeyResult sKey=SynchronizedKey.getAccountKey(account);
		synchronized (sKey.getKey()) {
			AccountInfo aInfo=wallet.checkAccount(account,froms);
			if(aInfo.getRet()==1 || aInfo.getRet()==18){
				//关闭提现记录
				int ret=wallet.withdrawClose(account, uadwid, loseReason, handler,noteCloseType, froms);
				if(ret==1) ret=4;
				fb=new Feedback(ret);
			} else fb=new Feedback(aInfo.getRet());
		}
		SynchronizedKey.deleteAccountKey(sKey);
		return fb;
	}

	/**
	 * 获取虚拟账户提现记录
	 * @param page 分页对象
	 * @param modeType 获取处理方式
	 * @return Feedback
	 */
	public Feedback getWithdrawNotes(PageUtil<Map,WithdrawNoteWhere> page,ModeType modeType){
		WithdrawNoteWhere where=page.getConditions();
		if(!vilidateSystemFroms(where.getFroms())) return new Feedback(2, "froms参数不合法");
		if(!where.isManager()){
			if(where.getAccount() == null || "".equals(where.getAccount().trim())) return new Feedback(1, "account参数未设置");
			int ret=wallet.checkAccount(where.getAccount(),where.getFroms()).getRet();
			if(ret!=1) return new Feedback(ret);
		}
		if(modeType==modeType.EXPORT){
			if(where.getBatch()==null || "".equals(where.getBatch())) return new Feedback(1, "batch参数未设置");
		} else if(modeType==modeType.EXPORT_NEW_BATCH){
			if(where.getBatch()!=null && !"".equals(where.getBatch())) return new Feedback(2, "batch参数不合法");
			where.setBatch(null);
		}
		return wallet.get_withdraw_notes(page,modeType);
	}
		

	/**********************************提现 end****************************************/
	/************************************基础信息 begin********************************/
	
	//获取地区信息
	@Override
	public List<Area> getAreas(String parentCode){
		if(parentCode==null || "".equals(parentCode.trim())) parentCode="china";
		return wallet.getAreas(parentCode);
	}
	
	//获取提现银行信息
	@Override
	public List<Bank> getBanks(){
		return wallet.getBanks();
	}
	
	/***********************************基础信息 end********************************/
	/*********************统计信息 begin********************************/
	
	//查询日账单流水数据
	@Override
	public Feedback findStatDays(PageUtil page, String beginDate, String endDate) {
		if(StringUtil.isNullOrEmpty(beginDate)) return new Feedback(1,"未设置查询时间段的开始日期");
		if(StringUtil.isNullOrEmpty(endDate)) return new Feedback(1,"未设置查询时间段的结束日期");
		wallet.findStatDays(page, beginDate, endDate);
		if(page.getList()==null) return new Feedback(4);
		return new Feedback();
	}

	//查询月账单流水数据
	@Override
	public Feedback findStatMonths(PageUtil page, String beginDate, String endDate) {
		if(StringUtil.isNullOrEmpty(beginDate)) return new Feedback(1,"未设置查询时间段的开始日期");
		//结束时间未设置，则使用今天
		if(StringUtil.isNullOrEmpty(endDate)) endDate=DateTimeUtil.dateTimeToString(Calendar.getInstance().getTime(), "yyyy-MM-dd");
		wallet.findStatMonths(page, beginDate, endDate);
		if(page.getList()==null) return new Feedback(4);
		return new Feedback();
	}
	
	/*********************统计信息 end********************************/
	
	
	
	
	//来源验证
	private final static boolean vilidateSystemFroms(int froms){
		switch (froms) {//来源
		case 1://1目前只有1
			return true;
		default:
			return false;
		}
	}
	
	
	
}
