/**
 * 
 */
package com.yzz.virtual.wallet.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.yzz.virtual.wallet.AbsWalletConfigure;
import com.yzz.virtual.wallet.AbsWalletExpand;
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

import cn.com.yofogo.frame.assistant.PageUtil;
import cn.com.yofogo.frame.dao.DBModes.DBTarget;
import cn.com.yofogo.frame.dao.DBModes.ResultMode;
import cn.com.yofogo.frame.dao.perdure.BaseDao;
import cn.com.yofogo.frame.dao.perdure.conn.BaseDaoImpl;
import cn.com.yofogo.frame.tools.NumberCreator;
import cn.com.yofogo.tools.util.CipherUtil;
import cn.com.yofogo.tools.util.DateTimeUtil;
import cn.com.yofogo.tools.util.MyMath;
import cn.com.yofogo.tools.util.StringUtil;
import net.sf.json.JSONObject;

/**
 * @ClassName VirtualWalletServicesImpl
 * @Description TODO
 * @author zhengzhou.yang
 * @date 2015-4-13
 *
 */
public class VirtualWalletServicesImpl implements IVirtualWalletServices {
	private AbsWalletConfigure config;
	private BaseDao dbHelp, dbHelpConfig ;
	public VirtualWalletServicesImpl(AbsWalletConfigure WalletConfigure){
		this.config=WalletConfigure;
		dbHelp = BaseDaoImpl.getInstance(config.getWalletDBName());
		dbHelpConfig = BaseDaoImpl.getInstance(config.getConfigDBName());
	}

	//检查账户信息
	@Override
	public AccountInfo checkAccount(String account,int froms){
		return checkAccount(account, froms ,false);
	}
	//检查账户信息
	@Override
	public AccountInfo checkAccount(String account,int froms,boolean isCreated){
		String sql="SELECT COUNT(0),status,pay_sign,balance FROM "+config.getTablePrefix()+"account WHERE uadid=?";
		Object[] objs=dbHelp.queryOneArray(sql,DBTarget.writeDB, account);
		if(objs==null || objs[0]==null) return new AccountInfo(4);
		int count=0,status=-100;
		Integer balance=0;
		try {
			count=Integer.parseInt(objs[0].toString());
			if(count==0){
				if(isCreated){
					int ret=created_account(account, null, froms);
					if(ret==0){
						AccountInfo aInfo=new AccountInfo(1);
						aInfo.setStatus(1);
						aInfo.setBalance(0);
						return aInfo;
					} else return new AccountInfo(4);
				}
				return new AccountInfo(17);
			}
			status=Integer.parseInt(objs[1].toString());
			balance=Integer.parseInt(objs[3].toString());
		} catch (Exception e) {
			return new AccountInfo(4);
		}
		if(count==1){
			AccountInfo aInfo=new AccountInfo(1);
			if(status!=1){
				/*if(isCreated){
					int ret=activation_account(account, null, froms);
					if(ret==0){
						aInfo.setStatus(1);
						aInfo.setBalance(balance);
						return aInfo;
					} else return new AccountInfo(4);
				}*/
				aInfo.setRet(18);
			}
			aInfo.setStatus(status);
			aInfo.setBalance(balance);
			if(objs[2]!=null) aInfo.setPayPwd(objs[2].toString());
			return aInfo;
		} else return new AccountInfo(4);
	}
	
	//创建虚拟账户账户
	@Override
	public int created_account(String account,String pwd,int froms){
		return _created_account(account, pwd, froms, true);
	}
	private int _created_account(String account,String pwd,int froms,boolean isCheck){
		if(isCheck){
			String sql="SELECT COUNT(0) FROM "+config.getTablePrefix()+"account WHERE uadid=?";
			Object obj=dbHelp.queryObject(sql,DBTarget.writeDB, account);
			if(obj==null) return 4;
			int count=0;
			try {
				count=Integer.parseInt(obj.toString());
			} catch (Exception e) {
				return 4;
			}
			if(count>0) return 16;
		}
		if(pwd!=null) pwd=CipherUtil.generatePassword(pwd);
		String sql="INSERT INTO "+config.getTablePrefix()+"account(uadid,pay_sign,status,created,balance,froms) VALUES(?,?,1,NOW(),0,?)";
		int rows=dbHelp.executeSql(sql, account,pwd,froms);
		if(rows==1)return 0;
		else return 4;
	}
	
	
	//激活虚拟账户账户
	@Override
	public int activation_account(String account,String pwd,int froms){
		String sql="SELECT COUNT(0),status,pay_sign,move_target FROM "+config.getTablePrefix()+"account WHERE uadid=?";
		Object[] obj=dbHelp.queryOneArray(sql,DBTarget.writeDB, account);
		if(obj==null || obj[0]==null) return 4;
		int count=0;
		try {
			count=Integer.parseInt(obj[0].toString());
		} catch (Exception e) {
			return 4;
		}
		if(count==0) return 4;
		int status=Integer.parseInt(obj[1].toString());//状态（0未创建；1正常；2禁用；3转移；4转移中。默认为1）
		if(status==3 || status==0){
			//无记录信息
			Object objCount=dbHelp.queryObject("select count(0) from "+config.getTablePrefix()+"account_detail where uadid=?",DBTarget.writeDB, account);
			if(objCount==null || Integer.parseInt(objCount.toString())>0) return 4;
			if(pwd!=null) pwd=CipherUtil.generatePassword(pwd);
			sql="UPDATE "+config.getTablePrefix()+"account SET pay_sign=?,status=1,move_target=null WHERE status=? AND balance=0 AND froms=? AND uadid=?";
			int rows=dbHelp.executeSql(sql,pwd,status,froms,account);
			if(rows==1){
				//日志记录
				sql="INSERT INTO "+config.getTablePrefix()+"handle_logs(table_name,key_val,types,new_data,old_data,handler,created,remark)"
					+" VALUES('user_account_dzqb',?,2,?,?,?,NOW(),'激活账户')";
				dbHelp.executeSql(sql,account,"{\"newPwd\":\""+pwd+"\",\"status\":1}"
						,"{\"oldPwd\":\""+obj[2]+"\",\"status\":"+status+",\"move_target\":\""+obj[3]+"\"}",account );
				return 0;
			}
		}
		return 4;
	}
	
	//修改支付密码
	@Override
	public int change_paypwd(String account,String oldPwd, String newPwd, int froms){
		newPwd=CipherUtil.generatePassword(newPwd);
		String sql="UPDATE "+config.getTablePrefix()+"account SET pay_sign=? WHERE uadid=? AND froms=?";
		int rows=dbHelp.executeSql(sql,newPwd, account,froms);
		if(rows>0){
			//日志记录
			sql="INSERT INTO "+config.getTablePrefix()+"handle_logs(table_name,key_val,types,new_data,old_data,handler,created,remark)"
				+" VALUES('user_account_dzqb',?,2,?,?,?,NOW(),'修改支付密码')";
			dbHelp.executeSql(sql,account,newPwd,oldPwd,account );
			return 0;
		}
		return 4;
	}
	 
	//获取账户余额
	@Override
	public Integer get_balance(String account, int froms){
		String sql="SELECT balance FROM "+config.getTablePrefix()+"account WHERE uadid=?";
		Object obj=dbHelp.queryObject(sql,DBTarget.writeDB, account);
		if(obj==null) return -100;
		try {
			return Integer.parseInt(obj.toString());
		} catch (Exception e) {
			return -100;
		}
	}

	//钱包账号信息
	@Override
	public void getAccounts(PageUtil<Map, ?> page,String account,AccountStatus status,String beginDate,String endDate,int froms){
		List params=new ArrayList();
		String sql="SELECT uadid,status,balance,froms,set_status AS setStatus,retain_fees AS retainFees,created,update_date AS updateDate"
				+buildExpandFields(config.getTablePrefix()+"account","a",1)
				+" FROM "+config.getTablePrefix()+"account a WHERE froms=?";
		params.add(froms);
		if(account!=null && !"".equals(account.trim())){
			sql +=" AND uadid=?";
			params.add(account);
		}
		if(status!=null){
			sql +=" AND status=?";
			params.add(status.getValue());
		}
		if(beginDate!=null && !"".equals(beginDate.trim())){
			if(endDate!=null && !"".equals(endDate.trim())){
				sql +=" AND created BETWEEN ? AND ?";
				params.add(beginDate);
				params.add(endDate);
			} else {
				sql +=" AND created >= ?";
				params.add(beginDate);
			}
		} else if(endDate!=null && !"".equals(endDate)){
			sql +=" AND created <= ?";
			params.add(endDate);
		} 
		sql +=" ORDER BY created DESC";
		dbHelp.queryForPage(page,sql,ResultMode.isMap,params.toArray());
	}

	//钱包交易
	@Override
	public Feedback business(BusinessNote bNote){
		Connection conn=dbHelp.getConnection(DBTarget.writeDB);
		try {
			conn.setAutoCommit(false);
			Feedback ret=business(conn,bNote);
			if(ret.getCode()==0) conn.commit();
			else conn.rollback();
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if(!conn.getAutoCommit()) conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return new Feedback(4);
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//钱包交易
	@Override
	public Feedback business(Connection conn,BusinessNote bNote) throws Exception {
		int types=0;//类型（1支出、2收入）
		double fees=bNote.getMoney();
		if(bNote.getSubTypes().getValue()>0){//收入
			types=2;
		}else if(bNote.getSubTypes().getValue()<0){//支出
			types=1;
			fees=0-fees;
		} else return new Feedback(2);
		{//检查交易记录是否存在
			Object obj=dbHelp.queryObject("SELECT COUNT(0) FROM "+config.getTablePrefix()+"account_detail WHERE froms=? AND out_trade_no=?"
					,DBTarget.writeDB,bNote.getFroms(),bNote.getOutTradeNo());
			if(obj==null) return new Feedback(4);
			if(Integer.parseInt(obj.toString())>0) return new Feedback(25);
		}
		double balance=0;
		if(types==1){//支出时，需要判断余额是否充足
			String sql="SELECT balance FROM "+config.getTablePrefix()+"account WHERE uadid=?";
			Object obj=dbHelp.queryObject(sql,DBTarget.writeDB, bNote.getAccount());
			if(obj==null) return  new Feedback(4);
			balance=Double.parseDouble(obj.toString());
			if(balance<bNote.getMoney()) return new Feedback(8);
		}
		String remarks;
		if(bNote.getSubTypes()==SubTypes.TURN_OUT) {
			remarks="转账到"+bNote.getTargetAccountName();
			if(bNote.getRemarks()!=null) remarks+="；"+bNote.getRemarks();
			//检查目标账户是否存在
			String sql="SELECT count(0) FROM "+config.getTablePrefix()+"account WHERE uadid=?";
			Object obj=dbHelp.queryObject(sql,DBTarget.writeDB, bNote.getTargetAccount());
			if(obj==null) return new Feedback(4,"目标账户获取失败");
			if(Integer.parseInt(obj.toString())==0){
				if(config.realTimeCreatedAccount()){
					if(config.getWalletExpand().validAccount(bNote.getTargetAccount())){
						int ret=_created_account(bNote.getTargetAccount(), null, bNote.getFroms(), false);
						if(ret!=0) return new Feedback(4,"目标账户创建失败");
					} else return new Feedback(15);
				} else return new Feedback(20);
			}
		} else remarks=bNote.getRemarks();
		//onlyNote=1不显示，仅仅记录；0提供展示
		int onlyNote=(config.hasEffectiveTime() && config.sameReturnForReund_cancel()) && (bNote.getSubTypes()==SubTypes.CANCEL || bNote.getSubTypes()==SubTypes.REFUND)?1:0;
		//记录交易记录
		String uaddid=NumberCreator.getNumber(dbHelpConfig,config.getTablePrefix()+"account_detail");
		List params=new ArrayList();
		params.addAll(Arrays.asList(uaddid,bNote.getAccount(),fees,types,bNote.getSubTypes().getValue() ,bNote.getFroms()
				,bNote.getOutTradeNo(),bNote.getOutType(),bNote.getOutTypeName(),bNote.getOutOther(),remarks,bNote.getReqIP()
				,onlyNote));
		String sql="INSERT INTO "+config.getTablePrefix()+"account_detail(uaddid,uadid,money,types,sub_types,created,froms,out_trade_no,out_type,out_type_name,out_other,remarks,req_ip,only_note";
		boolean firstNoteDate=(config.hasEffectiveTime() && types==2 
				&& ((config.sameReturnForReund_cancel() && bNote.getSubTypes()!=SubTypes.CANCEL && bNote.getSubTypes()!=SubTypes.REFUND) || !config.sameReturnForReund_cancel()));
		if(firstNoteDate){
			sql+=",use_begin,use_end,use_status,up_date";
		}
		sql+=") VALUES(?,?,?,?,?,NOW(),?,?,?,?,?,?,?,?";
		if (firstNoteDate){
			sql+=",?,?,1,NOW()";
			params.add(bNote.getUseBegin());
			params.add(bNote.getUseEnd());			
		}
		sql+=")";
		int rows=dbHelp.executeSql(conn, sql,params.toArray());
		if(rows<1){
			return new Feedback(4,"处理记录失败");
		}
		int minusMoney=0;//账户总额处理时，需要减去的数量(一般退回时过期有值)
		if(config.sameReturnForReund_cancel()){//退款或取消需要原路返回处理
			Feedback fb = businessSameReturn(conn, bNote, uaddid, types);
			if(fb.getCode()!=0) return fb;
			try {
				minusMoney=Integer.parseInt(fb.getInfo().get("minusMoney")+"");
			} catch (Exception e) {
				e.printStackTrace();
				return new Feedback(4,"账户总额数据处理失败");
			}
		}
		
		//处理账户总金额
		if(types==1){//支出
			rows=dbHelp.executeSql(conn, "UPDATE "+config.getTablePrefix()+"account SET balance=balance-? WHERE uadid=? AND balance>=?"//AND balance=?
				,bNote.getMoney(),bNote.getAccount(), bNote.getMoney());//balance
		}else if(types==2){//收入
			rows=dbHelp.executeSql(conn, "UPDATE "+config.getTablePrefix()+"account SET balance=balance+?-? WHERE uadid=?"
					,bNote.getMoney(),minusMoney,bNote.getAccount());
		}
		if(rows<1){
			return new Feedback(4,"处理账户余额失败");
		}
		//转账时，需要更新目标账号信息
		if(bNote.getSubTypes()==SubTypes.TURN_OUT){
			//记录交易记录
			String targetUaddid=NumberCreator.getNumber(dbHelpConfig,config.getTablePrefix()+"account_detail");
			params=new ArrayList();
			params.addAll(Arrays.asList( targetUaddid,bNote.getTargetAccount(),bNote.getMoney(),2,SubTypes.TURN_IN.getValue() ,bNote.getFroms()
					,bNote.getAccount()+"_"+bNote.getOutTradeNo(),bNote.getOutType(),"转账收入","来自"+bNote.getAccountName()+"的转账",bNote.getReqIP()));
			String targetSql="INSERT INTO "+config.getTablePrefix()+"account_detail(uaddid,uadid,money,types,sub_types,created,froms,out_trade_no,out_type,out_type_name,remarks,req_ip";
			if(config.hasEffectiveTime()){
				targetSql+=",use_begin,use_end,use_status,up_date";//开始时间为当前时间、结束使用时间为当前时间加xx月
			}
			targetSql+=") VALUES(?,?,?,?,?,NOW(),?,?,?,?,?,?";
			if (config.hasEffectiveTime()){
				targetSql+=",NOW(),DATE_ADD(NOW(),INTERVAL ? MONTH),1,NOW()";
				params.add(getSystemParam(config.getTablePrefix()+"valid_moths"));
			}
			targetSql+=")";
			rows=dbHelp.executeSql(conn,targetSql,params.toArray());
			if(rows<1){
				return new Feedback(4,"处理转账记录失败");
			}
			rows=dbHelp.executeSql(conn
					, "INSERT INTO "+config.getTablePrefix()+"account_transfer_note(out_adid,out_account,in_adid,in_account)  VALUES(?,?,?,?)"
					,uaddid,bNote.getAccount(), targetUaddid,bNote.getTargetAccount());
			if(rows!=1){
				return new Feedback(4,"处理转账记录关联信息失败");
			}
			
			//处理账户总金额
			rows=dbHelp.executeSql(conn, "UPDATE "+config.getTablePrefix()+"account SET balance=balance+? WHERE uadid=?"
					,bNote.getMoney(),bNote.getTargetAccount());
			if(rows<1){
				return new Feedback(4,"处理转账目标账户余额失败");
			}
		}
		return new Feedback();
	}

	/**
	 * 退款或取消需要原路返回处理。相关记录需要做关联
	 * @param conn
	 * @param bNote
	 * @param uaddid
	 * @param types
	 * @return
	 * @throws Exception
	 */
	private Feedback businessSameReturn(Connection conn, BusinessNote bNote, String uaddid, int types) throws Exception {
		int minusMoney=0;//账户总额处理时，需要减去的数量(一般退回时有值)
		if(types==1){//支出处理 (逐条使用获得记录，按结束使用时间升序，即最先过期的先使用)
			List notes=dbHelp.query("SELECT uaddid,money,use_money FROM "+config.getTablePrefix()+"account_detail WHERE use_status=1 AND NOW() BETWEEN use_begin AND use_end AND types=2 AND uadid=? ORDER BY use_end"
					,DBTarget.writeDB, ResultMode.isMap, bNote.getAccount());
			if(notes==null || notes.size()==0) return new Feedback(4,"获取获得记录失败");
			Map note;
			String noteId;
			int money,useMoney,myMoney=0,useStatus,tempMoney=bNote.getMoney();
			for(Object obj : notes){
				if(tempMoney==0) break;
				else if(tempMoney<0) return new Feedback(4,"处理获得记录失败");
				note=(Map)obj;
				noteId=note.get("uaddid").toString();
				money=Integer.parseInt(note.get("money")+"");
				useMoney=Integer.parseInt(note.get("use_money")+"");
				myMoney=money-useMoney;
				if(myMoney<=tempMoney){
					tempMoney -= myMoney;
					useStatus=2;
				} else {
					myMoney=tempMoney;
					tempMoney=0;
					useStatus=1;
				}
				int rows=dbHelp.executeSql(conn, "UPDATE "+config.getTablePrefix()+"account_detail SET use_money=use_money+?,use_status=?,up_date=NOW() WHERE money-use_money>=? AND use_status=1 AND types=2 AND uaddid=?"
						,myMoney,useStatus,myMoney, noteId);
				if(rows!=1){
					return new Feedback(4,"使用时处理获得记录失败");
				}
				rows=dbHelp.executeSql(conn, "INSERT "+config.getTablePrefix()+"use_for_get(ufgid,unid,gnid,money,status,up_date) VALUES(?,?,?,?,1,NOW())"
						,UUID.randomUUID().toString(),uaddid,noteId,myMoney);
				if(rows!=1){
					return new Feedback(4,"使用时处理获得记录关联信息失败");
				}
			}
		} else if(SubTypes.REFUND==bNote.getSubTypes() || SubTypes.CANCEL==bNote.getSubTypes()){//退回处理
			String noteSql="SELECT g.uaddid,ufg.ufgid,ufg.money,ufg.re_money,UNIX_TIMESTAMP(g.use_end)-UNIX_TIMESTAMP(NOW()) AS times"//g.use_end-now()
					+",g.money-g.use_money AS gqMoney,g.use_status"
					+" FROM "+config.getTablePrefix()+"account_detail u,"+config.getTablePrefix()+"use_for_get ufg ,"+config.getTablePrefix()+"account_detail g"
					+" WHERE u.uaddid=ufg.unid AND g.uaddid=ufg.gnid AND u.types=1 AND ufg.money>ufg.re_money AND g.uadid=? AND u.uadid=? AND u.out_trade_no=? ORDER BY g.use_end DESC";
			List notes=dbHelp.query(noteSql,DBTarget.writeDB, ResultMode.isMap, bNote.getAccount(), bNote.getAccount(),bNote.getUseTarget());
			if(notes==null || notes.size()==0) return new Feedback(4,"获取使用记录失败");
			Map note;
			String noteId,ufgid;
			int money,reMoney,myMoney=0,tempMoney=bNote.getMoney(),times,useStatus;
			for(Object obj : notes){
				if(tempMoney<=0) break;
				note=(Map)obj;
				noteId=note.get("uaddid").toString();
				ufgid=note.get("ufgid").toString();
				money=Integer.parseInt(note.get("money")+"");
				reMoney=Integer.parseInt(note.get("re_money")+"");
				times=Integer.parseInt(note.get("times")+"");//结束使用时间已经过当前时间，则为过期记录
				useStatus=Integer.parseInt(note.get("use_status")+"");//使用状态（0无状态；1正常；2已用完；3已过期）
				myMoney=money-reMoney;
				if(myMoney<=tempMoney){
					tempMoney -= myMoney;
				} else {
					myMoney=tempMoney;
					tempMoney=0;
				}
				int currUseStatus=(useStatus==3 ? 3 : (times>0)?1:3);
				int rows=dbHelp.executeSql(conn, "UPDATE "+config.getTablePrefix()+"account_detail SET use_money=use_money-?,use_status=?,up_date=NOW() WHERE use_money>=? AND use_status=? AND types=2 AND uaddid=?"
						,myMoney,currUseStatus,myMoney,useStatus, noteId);
				if(useStatus==3) minusMoney+=myMoney;
				else if(currUseStatus==3) minusMoney+=myMoney+Integer.parseInt(note.get("gqMoney")+"");
				
				
				if(rows!=1){
					return new Feedback(4,"使用时处理获得记录失败");
				}
				rows=dbHelp.executeSql(conn, "UPDATE "+config.getTablePrefix()+"use_for_get SET re_money=re_money+?,up_date=NOW() WHERE money-re_money>=? AND ufgid=?"
						,myMoney,myMoney,ufgid);
				if(rows!=1){
					return new Feedback(4,"使用时处理获得记录关联信息失败");
				}
			}
		} 
		Feedback fb = new Feedback();
		fb.getInfo().put("minusMoney", minusMoney);
		return fb;
	}

	//获得记录过期处理
	@Override
	public Feedback overdueHandle(){
		String sql="SELECT uaddid,uadid,money-use_money AS myMoney,use_money FROM "+config.getTablePrefix()+"account_detail WHERE use_status=1 AND types=2 AND use_end<=NOW() ORDER BY use_end";
		PageUtil page = new PageUtil();
		page.setPageSize(500);
		do{
			dbHelp.queryForPage(page, sql, ResultMode.isMap);
			List notes=page.getList();
			if(notes!=null && notes.size()>0){
				Map note;
				String uaddid,account;
				int myMoney,useMoney;
				for(Object item : notes){
					note=(Map)item;
					uaddid=note.get("uaddid").toString();
					account=note.get("uadid").toString();
					myMoney=Integer.parseInt(note.get("myMoney")+"");
					useMoney=Integer.parseInt(note.get("use_money")+"");
					Connection conn=dbHelp.getConnection(DBTarget.writeDB);
					try {
						conn.setAutoCommit(false);
						int rows=dbHelp.executeSql(conn, "UPDATE "+config.getTablePrefix()+"account_detail SET use_status=3,up_date=NOW() WHERE use_money=? AND use_status=1 AND use_status!=3 AND types=2 AND uaddid=?"
								,useMoney, uaddid);
						if(rows!=1){
							conn.rollback();
							return new Feedback(4,"处理获得记录失败");
						}
						rows=dbHelp.executeSql(conn, "UPDATE "+config.getTablePrefix()+"account SET balance=balance-? WHERE balance>=? AND uadid=?"
								,myMoney,myMoney,account );
						if(rows!=1){
							conn.rollback();
							return new Feedback(4,"处理账户余额失败");
						}
						conn.commit();
					} catch (Exception e) {
						try {
							conn.rollback();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					} finally {
						try {
							conn.close();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}while(page.getPageTotal()>1);
		return new Feedback();
	}

	//获取虚拟账户交易记录
	@Override
	public void get_business_notes(PageUtil<Map,BusinessNoteWhere> page){
		BusinessNoteWhere where=page.getConditions();
		List params=new ArrayList();
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT d.uaddid,d.uadid AS account,d.types,d.sub_types AS subTypes,d.froms,d.`money`,d.remarks,d.created")
			.append(",d.out_trade_no AS outTradeNo,d.out_type AS outType,d.out_type_name AS outTypeName,d.out_other AS outOther");
		//childTypeName：1奖金、2转账(入)、3充值、4退款、5订单取消 		-1购买、-2转账(出)、-3提现
		sql.append(",CASE ");
		for (SubTypes item : SubTypes.values()) {
			sql.append(" WHEN d.sub_types="+item.getValue()+" THEN '"+item.getName()+"'");
		}
		sql.append(" END AS childTypeName");
		sql.append(",d.use_money AS useMoney,d.use_status AS useStatus,d.use_begin AS useBegin,d.use_end AS useEnd,d.up_date AS `upDate`");
		if(config.hasEffectiveTime() && config.sameReturnForReund_cancel()){
			sql.append(",CASE WHEN d.types=1 THEN (SELECT SUM(re_money) FROM coin_use_for_get WHERE unid=d.uaddid) ELSE 0 END AS reMoney");
		} else sql.append(",0 AS reMoney");
		sql.append(buildExpandFields(config.getTablePrefix()+"account_detail","d",2))
			.append(" FROM "+config.getTablePrefix()+"account_detail d");
		if(where.getTargetAccount()!=null && !"".equals(where.getTargetAccount().trim())){
			sql.append(","+config.getTablePrefix()+"account_transfer_note dtn WHERE d.uaddid=dtn.out_adid AND dtn.in_account=? AND froms=?");
			params.add(where.getTargetAccount());
		} else sql.append(" WHERE froms=?");
		params.add(where.getFroms());
		if(where.isManager()){
			if(where.getOnlyNote()>-1){
				sql.append(" AND only_note=?");
				params.add(where.getOnlyNote());
			}
		} else sql.append(" AND only_note=0");
		if(where.getUaddid()!=null && !"".equals(where.getUaddid().trim())){
			sql.append(" AND uaddid=?");
			params.add(where.getUaddid());
			page.setPage(false);
		}
		if(where.getTypes()==1 || where.getTypes()==2){
			sql.append(" AND types=?");
			params.add(where.getTypes());
		}
		if(where.getSubTypes()!=null){
			sql.append(" AND sub_types=?");
			params.add(where.getSubTypes().getValue());
		}
		if(where.getOutTradeNo()!=null && !"".equals(where.getOutTradeNo().trim())){
			sql.append(" AND out_trade_no=?");
			params.add(where.getOutTradeNo());
		}
		if(where.getOutType()!=null){
			sql.append(" AND out_type=?");
			params.add(where.getOutType());
		}
		if(!where.isManager() || (where.getAccount()!=null && !"".equals(where.getAccount().trim()))){
			sql.append(" AND uadid=?");
			params.add(where.getAccount());
		}
		if(where.getBeginDate()!=null && !"".equals(where.getBeginDate().trim())){
			if(where.getEndDate()!=null && !"".equals(where.getEndDate().trim())){
				sql.append(" AND created BETWEEN ? AND ?");
				params.add(where.getBeginDate());
				params.add(where.getEndDate());
			} else {
				sql.append(" AND created >= ?");
				params.add(where.getBeginDate());
			}
		} else if(where.getEndDate()!=null && !"".equals(where.getEndDate().trim())){
			sql.append(" AND created <= ?");
			params.add(where.getEndDate());
		} 
		sql.append(" ORDER BY created DESC");
		dbHelp.queryForPage(page,sql.toString(),ResultMode.isMap,params.toArray());
	}
	
	
	// 钱包提现
	@Override
	public Feedback withdraw(WithdrawNote wNote,int froms){
		String sql="SELECT balance FROM "+config.getTablePrefix()+"account WHERE uadid=?";
		Object obj=dbHelp.queryObject(sql,DBTarget.writeDB, wNote.getAccount());
		if(obj==null) return new Feedback(4);
		double balance=Double.parseDouble(obj.toString());
		//提现单笔手续费//【{"type":2,"counterFee":0.05}】(type{1固定值；2百分比值})
		String counterFeeConfig =_withdrawCounterFeeConfig();
		double counterFee;
		if(counterFeeConfig==null) return new Feedback(4);
		else {
			JSONObject json=JSONObject.fromObject(counterFeeConfig);
			int type=json.getInt("type");
			if(type==1) counterFee=json.getDouble("counterFee");
			else if(type==2){
				double rate=json.getDouble("counterFee");
				counterFee=MyMath.mul(2,wNote.getFees(), rate);
			} else return new Feedback(4);
		}
		double factFee=MyMath.minus(wNote.getFees(), counterFee);
		if(factFee<=0) return new Feedback(1);
		if(balance<wNote.getFees()) return new Feedback(8);
		if(counterFee!=wNote.getCounterFee() || factFee!=wNote.getFactFee()) return new Feedback(28);
		wNote.setUadwid(NumberCreator.getNumber(dbHelpConfig,config.getTablePrefix()+"account_withdraw"));
		Connection conn=dbHelp.getConnection(DBTarget.writeDB);
		try {
			conn.setAutoCommit(false);
			//扣除账户总金额
			int rows=dbHelp.executeSql(conn, "UPDATE "+config.getTablePrefix()+"account SET balance=balance-? WHERE uadid=? AND balance=? AND balance>=?"
					,wNote.getFees(),wNote.getAccount(), balance,wNote.getFees());
			if(rows<1){
				conn.rollback();
				return new Feedback(1);
			}	
			//增加交易记录
			String uaddid=NumberCreator.getNumber(dbHelpConfig,config.getTablePrefix()+"account_detail");
			sql="INSERT INTO "+config.getTablePrefix()+"account_detail(uaddid,uadid,money,types,sub_types,created,froms,out_trade_no,remarks) VALUES(?,?,?,1,"+SubTypes.TAKE_OUT.getValue()+",NOW(),?,?,?)";
			rows=dbHelp.executeSql(conn, sql,uaddid,wNote.getAccount(),0-wNote.getFees(),froms,wNote.getUadwid(),"提现");
			if(rows<1){
				conn.rollback();
				return new Feedback(1);
			}	
			wNote.setSerialNo(uaddid);
			//记录提现记录【目前直接为1[提现中]】
			sql = "INSERT INTO `"+config.getTablePrefix()+"account_withdraw` (uadwid, `dzqb_no`, `fees`,`fact_fee`,`counter_fee`, `bank_code`, `bank_name`, `bank_no`, `bank_user_name`, `province_code`,`city_code`,`sub_bank_name`,`mode`,serial_no,remark, `status`, `created`,update_date) " +
				"VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,0, now(),now())";
			rows = dbHelp.executeSql(conn,sql, wNote.getUadwid(),wNote.getAccount(),wNote.getFees(),factFee,counterFee,wNote.getBankCode(),wNote.getBankName()
					,wNote.getBankNo(),wNote.getBankUserName(),wNote.getProvinceCode(),wNote.getCityCode(),wNote.getSubBankName(),wNote.getMode(),wNote.getSerialNo(),wNote.getRemark());
			if(rows<1){
				conn.rollback();
				return new Feedback(1);
			}
			conn.commit();
			//调用银行接口提现
			//暂无
			
			return new Feedback();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return new Feedback(4);
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	//提现初始化信息
	@Override
	public JSONObject withdrawInitialInfo(String account){
		Map<String,Object> info=new HashMap();
		if(account!=null && !"".equals(account.trim())){
			String sql="SELECT bank_code AS bankCode,bank_name AS bankName,bank_no AS bankNo,bank_user_name AS bankUserName,sub_bank_name AS subBankName,province_code AS provinceCode,city_code AS cityCode"
					+",(SELECT names FROM base_area WHERE code=w.province_code) AS provinceName"
					+",(SELECT names FROM base_area WHERE code=w.city_code) AS cityName"
				+" FROM "+config.getTablePrefix()+"account_withdraw w WHERE dzqb_no=? ORDER BY created DESC LIMIT 1";
			info.put("user",dbHelp.queryMap(sql,ResultMode.isOnlyMap,account));
		}
		info.put("counterFee",JSONObject.fromObject(_withdrawCounterFeeConfig()));
		return JSONObject.fromObject(info);
	}
	
	private String _withdrawCounterFeeConfig(){
		return (String)getSystemParam(config.getTablePrefix()+"withdraw_counter_fee");
	}
	
	//虚拟账户提现记录审核通过
	@Override
	public Feedback withdrawAudit(String account,String uadwid,Status status,String reason,String handler,int froms){
		//检查记录是否存在
		Map withdraw=dbHelp.queryMap("SELECT uadwid,status,fees,fact_fee FROM "+config.getTablePrefix()+"account_withdraw WHERE dzqb_no=? AND uadwid=?",ResultMode.isOnlyMap ,account, uadwid);
		if(withdraw==null || withdraw.isEmpty()) return new Feedback(26);
		//状态（0用户申请[待财审]、1提现中[提现接口请求成功]、2已关闭[最终态，申请失败或提现失败]、3提现成功[最终态]、10财审通过。默认为0）
		int oldStatus=Integer.parseInt(withdraw.get("status")+"");
		if((status==Status.AUDIT && oldStatus!=0) || (status==Status.SUCCESS && oldStatus!=10 && oldStatus!=1)) return new Feedback(27);
		List params=new ArrayList();
		String sql="UPDATE "+config.getTablePrefix()+"account_withdraw SET";
		if(status==Status.AUDIT) sql +=" status=10,";
		else if(status==Status.SUCCESS)  sql +=" status=3,";
		else return new Feedback(2, "status参数不合法");
		sql+=" close_reason=?,update_date=NOW(),handler=?";
		params.add(reason);
		params.add(handler);
		if(Status.AUDIT==status) {
			sql+=",audit_date=NOW(),audit_user=?";
			params.add(handler);
		}
		sql +=" WHERE ";
		if(status==Status.AUDIT) sql +="status=0";
		else if(status==Status.SUCCESS)  sql +="status in(1,10)";
		sql+=" AND dzqb_no=? AND uadwid=?";
		params.add(account);
		params.add(uadwid);
		int rows=dbHelp.executeSql(sql,params.toArray());
		if(rows==1) return new Feedback();
		else return new Feedback(4);
	}
	
	//虚拟账户提现记录关闭
	@Override
	public int withdrawClose(String account,String uadwid,String loseReason,String handler,NoteCloseType closeType,int froms){
		//检查记录是否存在
		Map withdraw=dbHelp.queryMap("SELECT uadwid,status,fees,fact_fee FROM "+config.getTablePrefix()+"account_withdraw WHERE dzqb_no=? AND uadwid=?",ResultMode.isOnlyMap ,account, uadwid);
		if(withdraw==null || withdraw.isEmpty()) return 26;
		//状态必须是 用户申请[待财审] 时，才可以关闭
		int oldStatus=0;
		if((oldStatus=Integer.parseInt(withdraw.get("status")+""))!=0){
			if(closeType.getValue()==1) return 1;
			else if(oldStatus!=1 && oldStatus!=10) return 4;
		}
		double fees=Double.parseDouble(withdraw.get("fees")+"");
		double factFee=0;
		List params=new ArrayList();
		String upStatusSql="UPDATE "+config.getTablePrefix()+"account_withdraw SET status=2,close_type=?,close_reason=?,update_date=now(),handler=?";
		params.add(closeType.getValue());
		params.add(loseReason);
		params.add(handler);
		if(closeType.getValue()==1){
			upStatusSql+=",audit_date=now(),audit_user=?";
			params.add(handler);
		}
		if(closeType.getValue()==2){
			upStatusSql+=",is_ret_ticket=1"; 
			factFee = Double.parseDouble(withdraw.get("fact_fee")+"");
		} else {
			upStatusSql+=",counter_fee=0,fact_fee=fees";
			factFee=fees;
		}
		upStatusSql+=" WHERE status=? AND fees=? AND dzqb_no=? AND uadwid=?";
		params.add(oldStatus);
		params.add(fees);
		params.add(account);
		params.add(uadwid);

		if(factFee<0) return 4;
		String detailsSsql="INSERT INTO "+config.getTablePrefix()+"account_detail(uaddid,uadid,money,types,sub_types,created,froms,out_trade_no,remarks,out_type,out_type_name) VALUES(?,?,?,2,"+SubTypes.RECHARGE.getValue()+",NOW(),?,?,?,33,'提现失败退回')";
		
		Connection conn=dbHelp.getConnection(DBTarget.writeDB);
		try {
			conn.setAutoCommit(false);
			//修改提现记录状态为关闭
			int rows=dbHelp.executeSql(conn,upStatusSql, params.toArray());
			if(rows!=1){
				conn.rollback();
				return 1;
			}
			//向账户充值同等金额
			rows=dbHelp.executeSql(conn, "UPDATE "+config.getTablePrefix()+"account SET balance=balance+? WHERE uadid=?" ,factFee,account);
			if(rows!=1){
				conn.rollback();
				return 1;
			}
			String uaddid=NumberCreator.getNumber(dbHelpConfig,config.getTablePrefix()+"account_detail");
			rows=dbHelp.executeSql(conn, detailsSsql,uaddid,account,factFee,froms,uadwid+"_CZ","提现失败退回");
			if(rows!=1){
				conn.rollback();
				return 1;
			}
			conn.commit();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return 4;
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	

	//获取虚拟账户提现记录
	@Override
	public Feedback get_withdraw_notes(PageUtil<Map,WithdrawNoteWhere> page,ModeType modeType){
		List whereParams=new ArrayList();
		String whereSql="";
		WithdrawNoteWhere where=page.getConditions();
		if(where.getUadwid()!=null && !"".equals(where.getUadwid().trim())){
			whereSql +=" AND uadwid=?";
			whereParams.add(where.getUadwid());
			page.setPage(false);
		}
		if(where.getNoteCloseType()!=null){
			whereSql +=" AND close_type=?";
			whereParams.add(where.getNoteCloseType().getValue());
		}
		if(!where.isManager() || (where.getAccount()!=null && !"".equals(where.getAccount().trim()))){
			whereSql +=" AND dzqb_no=?";
			whereParams.add(where.getAccount());
		}
		if(where.getBeginDate()!=null && !"".equals(where.getBeginDate().trim())){
			if(where.getEndDate()!=null && !"".equals(where.getEndDate().trim())){
				whereSql +=" AND created BETWEEN ? AND ?";
				whereParams.add(where.getBeginDate());
				whereParams.add(where.getEndDate());
			} else {
				whereSql +=" AND created >= ?";
				whereParams.add(where.getBeginDate());
			}
		} else if(where.getEndDate()!=null && !"".equals(where.getEndDate().trim())){
			whereSql +=" AND created <= ?";
			whereParams.add(where.getEndDate());
		} 
		if(modeType==ModeType.EXPORT_NEW_BATCH){//更新满足条件的数据的批次号
			String batch=NumberCreator.getNumber(dbHelpConfig, "dzqb_account_withdraw_batch");
			Connection confConn=dbHelpConfig.getConnection(DBTarget.writeDB);
			Connection conn=dbHelp.getConnection(DBTarget.writeDB);
			try {
				conn.setAutoCommit(false);
				confConn.setAutoCommit(false);
				int rows=dbHelp.executeSql(conn,"UPDATE "+config.getTablePrefix()+"account_withdraw SET status=1,batch='"+batch+"',batch_date=NOW() WHERE status=10 "+whereSql, whereParams.toArray());
				if(rows==0){
					conn.rollback();
					confConn.rollback();
					return new Feedback(26);
				}
				rows=dbHelpConfig.executeSql(confConn,"INSERT INTO order_send_log(batch_number,batch_date,admin_user,types) VALUES (?,NOW(),?,2)",batch,where.getHandler());
				if(rows==0){
					conn.rollback();
					confConn.rollback();
					return new Feedback(4);
				}
				conn.commit();
				confConn.commit();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				try {
					confConn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				return new Feedback(4);
			}finally{
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					confConn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			where.setBatch(batch);
			where.setStatus(Status.WITHDRAW);
			page.setPage(false);
		} if(modeType==ModeType.EXPORT){
			where.setStatus(Status.WITHDRAW);
			page.setPage(false);
		}
		if(where.getStatus()!=null){
			whereSql +=" AND status=?";
			whereParams.add(where.getStatus().getValue());
		}
		if(where.getBatch()!=null && !"".equals(where.getBatch().trim())){
			whereSql +=" AND batch=?";
			whereParams.add(where.getBatch());
		}
		String sql="SELECT uadwid,dzqb_no AS account,fees,fact_fee AS factFee,counter_fee AS counterFee"
			+",bank_code,bank_name AS bankName,bank_no AS bankNo,bank_user_name AS bankUserName,sub_bank_name AS subBankName,province_code,city_code"
				+",(SELECT names FROM base_area WHERE code=w.province_code) AS provinceName"
				+",(SELECT names FROM base_area WHERE code=w.city_code) AS cityName"
			+",created,status,close_reason AS loseReason,remark,batch"
			+",batch_date AS batchDate,audit_date AS auditDate,audit_user AS auditUser,update_date AS updateDate"
			+buildExpandFields(config.getTablePrefix()+"account_withdraw","w",3)
			+" FROM "+config.getTablePrefix()+"account_withdraw w";
		if(!"".equals(whereSql)){
			sql += " WHERE 1=1" + whereSql;
		} 
		sql +=" ORDER BY created DESC";
		dbHelp.queryForPage(page,sql,ResultMode.isMap,whereParams.toArray());
		return new Feedback();
	}
	
	//type(1账户信息；2交易记录；3提现记录)
	private String buildExpandFields(String talbeName,String tableShortName,int types){
		AbsWalletExpand expand= config.getWalletExpand();
		if(expand==null) return "";
		String expandFields=null;
		if(types==1) expandFields=expand.selectAccountFields();
		else if(types==2) expandFields=expand.selectBusinessNoteFields();
		else if(types==3) expandFields=expand.selectWithdrawNoteFields();
		if(expandFields==null) return "";
		String sql="";
		if(!"".equals(expandFields=expandFields.trim())){
			if(!expandFields.startsWith(",")) sql+=",";
			expandFields=expandFields.replace(talbeName+".", tableShortName+".");
			sql+=expandFields;
		}
		return sql;
	}
	
	//获取系统参数值
	@Override
	public Object getSystemParam(String name){
		//类型（1整型；2字符；3时间；4浮点型）
		Map map = dbHelpConfig.queryMap("SELECT types,ints,str,dates,doubles FROM base_system_params WHERE name=?",ResultMode.isOnlyMap, name);
		int type=(Integer)map.get("types");
		switch(type){
			case  1 : return map.get("ints");
			case  2 : return map.get("str");
			case  3 : return map.get("dates");
			case  4 : return map.get("doubles");
			default : return null;
		}
	}
	

	/**
	 * 获取当前账户提现次数
	 * @return 提现次数值。（返回-100时，说明获取失败）
	 */
	public int getWithdrawCount(String account){
		String myDate=DateTimeUtil.getCurrentDatetime("yyyy-MM-dd");
		Object obj=dbHelp.queryObject("SELECT COUNT(0) FROM "+config.getTablePrefix()+"account_withdraw WHERE created BETWEEN ? AND ? AND dzqb_no=?"
				,DBTarget.writeDB,myDate+" 00:00:00",myDate+" 23:59:59", account);
		if(obj==null) return -100;
		return Integer.parseInt(obj.toString());
	}
	
	/*********************基础信息 begin********************************/
	
	//获取地区信息
	@Override
	public List<Area> getAreas(String parentCode){
		return dbHelp.queryEntity(Area.class, "SELECT * FROM base_area WHERE parent=?", parentCode);
	}
	
	//获取提现银行信息
	@Override
	public List<Bank> getBanks(){
		return dbHelp.queryEntity(Bank.class, "SELECT * FROM base_bank WHERE status=1");
	}

	/*********************基础信息 end********************************/

	/*********************统计信息 begin********************************/
	
	//查询日账单流水数据
	@Override
	public void findStatDays(PageUtil page, String beginDate, String endDate) {
		dbHelp.queryForPage(page, "SELECT * FROM "+config.getTablePrefix()+"stat_days WHERE days BETWEEN ? AND ? ORDER BY days DESC"
				, ResultMode.isMap,beginDate, endDate);
	}

	//查询月账单流水数据
	@Override
	public void findStatMonths(PageUtil page, String beginDate, String endDate) {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT t.*\t")
			.append(",(SELECT befor_fees FROM "+config.getTablePrefix()+"stat_days WHERE days=t.onDay) AS beforFees\t")
			.append(",(SELECT befor_fees FROM "+config.getTablePrefix()+"stat_days WHERE days=t.lastDay) AS  balance\t")
			.append(" FROM (\t")
			.append("  SELECT date_format(days,'%Y-%m') AS months,SUM(make_fees) AS makeFees,SUM(use_fees) AS useFees,MIN(days) AS onDay,MAX(days) lastDay\t")
			.append("  FROM "+config.getTablePrefix()+"stat_days WHERE days BETWEEN ? AND ? GROUP BY date_format(days,'%Y-%m')\t")
			.append(") t ORDER BY months DESC \t");
		dbHelp.queryForPage(page, sql.toString(), ResultMode.isMap,beginDate,endDate);
	}
	
	/*********************统计信息 end********************************/
}
