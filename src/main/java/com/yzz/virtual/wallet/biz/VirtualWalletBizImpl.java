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
	 * ���������˻�
	 * @param account �����˺�
	 * @param pwd �����˺�����
	 * @param froms ��Դ
	 * @return Feedback
	 */
	public Feedback createdAccount(String account,String pwd,int froms){
		// account ������
		if (account == null || "".equals(account.trim())) return new Feedback(1, "account����δ����");
		else if ("null".equals(account.trim())) return new Feedback(2, "account�������Ϸ�");
		if (!vilidateSystemFroms(froms)) return new Feedback(2, "froms�������Ϸ�");
		// ҵ����
		Feedback fb;
		SyncKeyResult sKey=SynchronizedKey.getAccountKey(account);
		synchronized (sKey.getKey()) {
			int ret=wallet.checkAccount(account,froms).getRet();
			if(ret==17) ret=wallet.created_account(account, pwd, froms);
			else if(ret==18) ret=wallet.activation_account(account, pwd, froms);//�쳣
			else if(ret==1) ret=16;
			fb = new Feedback(ret);
		}
		SynchronizedKey.deleteAccountKey(sKey);
		return fb;
	}
	
	/**
	 * �޸������˺�֧������
	 * @param account �����˺�
	 * @param oldPwd ԭʼ����(������״��������룬��Ϊnull)
	 * @param newPwd ������
	 * @param froms ��Դ
	 * @return Feedback
	 */
	public Feedback changePaypwd(String account,String oldPwd,String newPwd,int froms){
		// account ������
		if(account == null || "".equals(account.trim())) return new Feedback(1, "account����δ����");
		if(!vilidateSystemFroms(froms)) return new Feedback(2, "froms�������Ϸ�");
		if(newPwd==null || "".equals(newPwd.trim())) return new Feedback(1, "newPwd����δ����");

		// ҵ����
		AccountInfo aInfo=wallet.checkAccount(account,froms);
		int ret=aInfo.getRet();
		if(ret==1){
			if(aInfo.getPayPwd()==null){
				if(oldPwd!=null) return new Feedback(22);
			} else if(!CipherUtil.validatePassword(aInfo.getPayPwd(), oldPwd==null?"":oldPwd)){
				 return new Feedback(22);
			}
			ret=wallet.change_paypwd(account,aInfo.getPayPwd(), newPwd, froms);//0�ɹ���4�޸�ʧ��
			return new Feedback(ret);
		} else return new Feedback(ret);
	}
	
	

	/**
	 * ��ȡ�˻����
	 * @param account �����˺�
	 * @param froms ��Դ
	 * @return Feedback (info.balanceΪ���)
	 */
	public Feedback getBalance(String account,int froms){
		if(account == null || "".equals(account.trim())) return new Feedback(1, "account����δ����");
		if(!vilidateSystemFroms(froms)) return new Feedback(2, "froms�������Ϸ�");
		AccountInfo ai=wallet.checkAccount(account,froms);
		int ret=ai.getRet();
		if(ret==1){
			Feedback fb = new Feedback();
			fb.getInfo().put("balance", ai.getBalance());
			return fb;
		}else return new Feedback(ret);
	}
	
	//��ȡ�����˺���Ϣ
	@Override
	public Feedback getAccounts(PageUtil<Map, ?> page,String account,AccountStatus status,String beginDate,String endDate,int froms){
		if(!vilidateSystemFroms(froms)) return new Feedback(2, "froms�������Ϸ�");
		wallet.getAccounts(page, account, status, beginDate, endDate, froms);
		return new Feedback(0);
	}

	/**********************************���� begin****************************************/
	
	//�����˻�����
	@Override
	public Feedback business(BusinessNote bNote){
		try {
			return _business(null, bNote);
		} catch (Exception e) {
			e.printStackTrace();
			 return new Feedback(4, "ϵͳ�쳣");
		}
	}

	//�����˻�����
	@Override
	public Feedback business(Connection conn,BusinessNote bNote) throws Exception{
		if(conn==null) return new Feedback(2, "conn�������Ϸ�");
		return _business(conn, bNote);
	}
	
	//�����˻����״���
	private Feedback _business(Connection conn,BusinessNote bNote) throws Exception{
		if(bNote.getAccount() == null || "".equals(bNote.getAccount().trim())) return new Feedback(1, "account����δ����");
		if(!vilidateSystemFroms(bNote.getFroms())) return new Feedback(2, "froms�������Ϸ�");
		if(bNote.getMoney()<=0) return new Feedback(2, "money�������Ϸ�");
		//�̻���ˮ�ţ�ͬһ�̻���ˮ�Ų����ظ�������Ϊ�գ�
		if(bNote.getOutTradeNo() == null || "".equals(bNote.getOutTradeNo().trim())) return new Feedback(1, "outTradeNo����δ����");
		if(bNote.getSubTypes() == null) return new Feedback(1, "subTypes����δ����");
		else if(bNote.getSubTypes()==SubTypes.TURN_OUT){
			if(bNote.getTargetAccount() == null || "".equals(bNote.getTargetAccount().trim())) return new Feedback(1, "targetAccount����δ����");
			if(bNote.getAccount().equals(bNote.getTargetAccount())) return new Feedback(19); 
		} else if(bNote.getSubTypes()==SubTypes.TURN_IN) return new Feedback(2, "subTypes�������Ϸ�");
		if(bNote.getReqIP() == null) return new Feedback(1, "reqIP����δ����");
		if (config.hasEffectiveTime() && bNote.getSubTypes().getValue()>0){
			if((config.sameReturnForReund_cancel() && bNote.getSubTypes()!=SubTypes.CANCEL && bNote.getSubTypes()!=SubTypes.REFUND) || !config.sameReturnForReund_cancel()){//�����Ͳ���Ҫ��Чʱ���
				if(bNote.getUseBegin()==null || bNote.getUseEnd()==null) return new Feedback(2,"��Чʱ�����(useBegin��useEnd)δ����");
			}
		}
		Feedback fb;
		SyncKeyResult sKey=SynchronizedKey.getAccountKey(bNote.getAccount());
		synchronized (sKey.getKey()) {
			AccountInfo aInfo=wallet.checkAccount(bNote.getAccount(),bNote.getFroms(),config.realTimeCreatedAccount());
			if(aInfo.getRet()==1){//���崦����ҵ��
				if(conn==null) fb=wallet.business(bNote);
				else fb=wallet.business(conn,bNote);//���崦����ҵ��
			} else fb = new Feedback(aInfo.getRet());
		}
		SynchronizedKey.deleteAccountKey(sKey);
		return fb;
	}

	//��ü�¼���ڴ���
	@Override
	public Feedback overdueHandle(){
		return wallet.overdueHandle();
	}
	
	//��ȡ�����˻����׼�¼
	@Override
	public Feedback getBusinessNotes(PageUtil<Map,BusinessNoteWhere> page){
		BusinessNoteWhere where = page.getConditions();
		if(!vilidateSystemFroms(where.getFroms())) return new Feedback(2, "froms�������Ϸ�");
		if(!where.isManager()){
			if(where.getAccount() == null || "".equals(where.getAccount().trim())) return new Feedback(1, "account����δ����");
			int ret=wallet.checkAccount(where.getAccount(),where.getFroms()).getRet();
			if(ret!=1) return new Feedback(ret);
		}
		wallet.get_business_notes(page);
		return  new Feedback(page.getList()==null?4:0);
	}
	
	/**********************************���� end****************************************/
	
	/**********************************���� begin****************************************/
	
	/**
	 * �����˻�����
	 * @param wNote ������Ϣ����
	 * @return Feedback
	 */
	public Feedback withdraw(WithdrawNote wNote){
		//����ʱ����֤
		{
			//{"type":2,"range":["1<=d<=5","d==7"]}
			Object obj=wallet.getSystemParam(config.getTablePrefix()+"withdraw_date_range");
			if(obj==null) return new Feedback(4);
			JSONObject json=JSONObject.fromObject(obj);
			int type=json.getInt("type");
			if(type!=0){
				int d=0;
				Calendar c=Calendar.getInstance();
				if(type==1){//ʱ�䣨Сʱ��
					d=c.get(Calendar.HOUR_OF_DAY);
				}else if(type==2){//�ܣ�����
					d=c.get(Calendar.DAY_OF_WEEK)-1;
					if(d==0) d=7;
				}else if(type==3){//�£����ţ�
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
						System.err.println("����ʱ�����ô���ϵͳ������"+config.getTablePrefix()+"withdraw_date_range����");
						return null;
					}
				}
				if(!hasDate) return new Feedback(23);
			}
		}
		if (wNote.getAccount() == null || "".equals(wNote.getAccount().trim())) return new Feedback(1, "account����δ����");
		if(!vilidateSystemFroms(wNote.getFroms())) return new Feedback(2, "froms�������Ϸ�");
		if(wNote.getFees()<=0) return new Feedback(2, "fees�������Ϸ�");
		if (wNote.getBankCode() == null || "".equals(wNote.getBankCode().trim())) return new Feedback(1, "bankCode����δ����");
		if (wNote.getBankName() == null || "".equals(wNote.getBankName().trim())) return new Feedback(1, "bankName����δ����");
		if (wNote.getBankNo() == null || "".equals(wNote.getBankNo().trim())) return new Feedback(1, "bankNo����δ����");
		if (wNote.getBankUserName() == null || "".equals(wNote.getBankUserName().trim())) return new Feedback(1, "bankUserName����δ����");
		if (wNote.getProvinceCode() == null || "".equals(wNote.getProvinceCode().trim())) return new Feedback(1, "provinceCode����δ����");
		if (wNote.getCityCode() == null || "".equals(wNote.getCityCode().trim())) return new Feedback(1, "cityCode����δ����");
		if (wNote.getSubBankName() == null || "".equals(wNote.getSubBankName().trim())) return new Feedback(1, "subBankName����δ����");
		if(wNote.getMode()!=2) return new Feedback(2, "mode�������Ϸ�");
		//��֤��С���ֽ��
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
				return new Feedback(2, "bankNo�������Ϸ�");
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
			//��֤���ִ���
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
			//���崦������ҵ��0�ɹ���1ʧ�ܣ�8�˻����㣻4ϵͳ����
			return wallet.withdraw(wNote,wNote.getFroms());
		} else return new Feedback(aInfo.getRet());
	}
	
	// ���ֳ�ʼ����Ϣ
	@Override
	public JSONObject withdrawInitialInfo(String account){
		return wallet.withdrawInitialInfo(account);
	}
	
	
	//�����˻����ּ�¼���ͨ��
	@Override
	public Feedback withdrawAudit(String account,String uadwid,Status status,String reason,String handler,int froms){
		if(status==null || (status!=Status.SUCCESS && status!=Status.AUDIT)) return new Feedback(2, "status�������Ϸ�");
		if(account == null || "".equals(account.trim())) return new Feedback(1, "account����δ����");
		if(!vilidateSystemFroms(froms)) return new Feedback(2, "froms�������Ϸ�");
		if(uadwid == null || "".equals(uadwid.trim())) return new Feedback(1, "uadwid����δ����");
		if(handler == null || "".equals(handler.trim())) return new Feedback(1, "handler����δ����");
		Feedback fb;
		SyncKeyResult sKey=SynchronizedKey.getAccountKey(account);
		synchronized (sKey.getKey()) {
			AccountInfo aInfo=wallet.checkAccount(account,froms);
			if(aInfo.getRet()==1){
				//�ر����ּ�¼
				fb=wallet.withdrawAudit(account, uadwid,status, reason, handler, froms);
			} else fb=new Feedback(aInfo.getRet());
		}
		SynchronizedKey.deleteAccountKey(sKey);
		return fb;
	}
	
	/**
	 * �����˻����ּ�¼�ر�
	 * @param account �����˺�
	 * @param uadwid ���ּ�¼
	 * @param loseReason �ر�ԭ��
	 * @param noteCloseType �ر�����
	 * @param handler ������
	 * @param froms ��Դ
	 * @return Feedback
	 */
	public Feedback withdrawClose(String account,String uadwid,String loseReason,NoteCloseType noteCloseType,String handler,int froms){
		if(account == null || "".equals(account.trim())) return new Feedback(1, "account����δ����");
		if(!vilidateSystemFroms(froms)) return new Feedback(2, "froms�������Ϸ�");
		if(uadwid == null || "".equals(uadwid.trim())) return new Feedback(1, "uadwid����δ����");
		if(loseReason == null || "".equals(loseReason.trim())) return new Feedback(1, "loseReason����δ����");
		if(handler == null || "".equals(handler.trim())) return new Feedback(1, "handler����δ����");
		if(noteCloseType == null) return new Feedback(1, "noteCloseType����δ����");
		Feedback fb;
		SyncKeyResult sKey=SynchronizedKey.getAccountKey(account);
		synchronized (sKey.getKey()) {
			AccountInfo aInfo=wallet.checkAccount(account,froms);
			if(aInfo.getRet()==1 || aInfo.getRet()==18){
				//�ر����ּ�¼
				int ret=wallet.withdrawClose(account, uadwid, loseReason, handler,noteCloseType, froms);
				if(ret==1) ret=4;
				fb=new Feedback(ret);
			} else fb=new Feedback(aInfo.getRet());
		}
		SynchronizedKey.deleteAccountKey(sKey);
		return fb;
	}

	/**
	 * ��ȡ�����˻����ּ�¼
	 * @param page ��ҳ����
	 * @param modeType ��ȡ����ʽ
	 * @return Feedback
	 */
	public Feedback getWithdrawNotes(PageUtil<Map,WithdrawNoteWhere> page,ModeType modeType){
		WithdrawNoteWhere where=page.getConditions();
		if(!vilidateSystemFroms(where.getFroms())) return new Feedback(2, "froms�������Ϸ�");
		if(!where.isManager()){
			if(where.getAccount() == null || "".equals(where.getAccount().trim())) return new Feedback(1, "account����δ����");
			int ret=wallet.checkAccount(where.getAccount(),where.getFroms()).getRet();
			if(ret!=1) return new Feedback(ret);
		}
		if(modeType==modeType.EXPORT){
			if(where.getBatch()==null || "".equals(where.getBatch())) return new Feedback(1, "batch����δ����");
		} else if(modeType==modeType.EXPORT_NEW_BATCH){
			if(where.getBatch()!=null && !"".equals(where.getBatch())) return new Feedback(2, "batch�������Ϸ�");
			where.setBatch(null);
		}
		return wallet.get_withdraw_notes(page,modeType);
	}
		

	/**********************************���� end****************************************/
	/************************************������Ϣ begin********************************/
	
	//��ȡ������Ϣ
	@Override
	public List<Area> getAreas(String parentCode){
		if(parentCode==null || "".equals(parentCode.trim())) parentCode="china";
		return wallet.getAreas(parentCode);
	}
	
	//��ȡ����������Ϣ
	@Override
	public List<Bank> getBanks(){
		return wallet.getBanks();
	}
	
	/***********************************������Ϣ end********************************/
	/*********************ͳ����Ϣ begin********************************/
	
	//��ѯ���˵���ˮ����
	@Override
	public Feedback findStatDays(PageUtil page, String beginDate, String endDate) {
		if(StringUtil.isNullOrEmpty(beginDate)) return new Feedback(1,"δ���ò�ѯʱ��εĿ�ʼ����");
		if(StringUtil.isNullOrEmpty(endDate)) return new Feedback(1,"δ���ò�ѯʱ��εĽ�������");
		wallet.findStatDays(page, beginDate, endDate);
		if(page.getList()==null) return new Feedback(4);
		return new Feedback();
	}

	//��ѯ���˵���ˮ����
	@Override
	public Feedback findStatMonths(PageUtil page, String beginDate, String endDate) {
		if(StringUtil.isNullOrEmpty(beginDate)) return new Feedback(1,"δ���ò�ѯʱ��εĿ�ʼ����");
		//����ʱ��δ���ã���ʹ�ý���
		if(StringUtil.isNullOrEmpty(endDate)) endDate=DateTimeUtil.dateTimeToString(Calendar.getInstance().getTime(), "yyyy-MM-dd");
		wallet.findStatMonths(page, beginDate, endDate);
		if(page.getList()==null) return new Feedback(4);
		return new Feedback();
	}
	
	/*********************ͳ����Ϣ end********************************/
	
	
	
	
	//��Դ��֤
	private final static boolean vilidateSystemFroms(int froms){
		switch (froms) {//��Դ
		case 1://1Ŀǰֻ��1
			return true;
		default:
			return false;
		}
	}
	
	
	
}
