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
	 * ��������Ǯ���˻�
	 * @param account Ǯ���˺�
	 * @param pwd Ǯ���˺�����
	 * @param froms ��Դ
	 * @return Feedback
	 */
	public static Feedback createdAccount(String account,String pwd){
		return vWallet.createdAccount(account, pwd, 1);
	}
	
	/**
	 * �޸ĵ���Ǯ���˺�֧������
	 * @param account Ǯ���˺�
	 * @param froms ��Դ
	 * @param oldPwd ԭʼ����(������״��������룬��Ϊnull)
	 * @param newPwd ������
	 * @return Feedback
	 */
	public static Feedback changePaypwd(String account,String oldPwd,String newPwd){
		return vWallet.changePaypwd(account, oldPwd, newPwd, 1);
	}
	
	

	/**
	 * ��ȡ�˻����
	 * @param account Ǯ���˺�
	 * @return Feedback
	 */
	public static Feedback getBalance(String account){
		return vWallet.getBalance(account, 1);
	}
	

	/**
	 * ��ȡǮ���˺���Ϣ
	 * @param page ��ҳ����
	 * @param account Ǯ���˺�
	 * @param status ״̬
	 * @param beginDate ����ʱ��Σ���ʼʱ�䣩
	 * @param endDate ����ʱ��Σ�����ʱ�䣩
	 */
	public static Feedback getAccounts(PageUtil<Map, ?> page,String account,AccountStatus status,String beginDate,String endDate){
		return vWallet.getAccounts(page, account, status, beginDate, endDate, 1);
	}
	
	/**
	 * ����Ǯ������
	 * @param bNote �������ݶ���
	 * @return Feedback
	 */
	public static Feedback business(BusinessNote bNote){
		return vWallet.business(bNote);
	}

	/**
	 * �����˻�����
	 * @param conn
	 * @param bNote
	 * @return code(0�ɹ���1ʧ�ܣ�2��������4ϵͳ����8���㣻25���׼�¼�Ѿ�����)
	 */
	public static Feedback business(Connection conn,BusinessNote bNote) throws Exception{
		return vWallet.business(conn, bNote);
	}

	/**
	 * ��ü�¼���ڴ���
	 * @return
	 */
	public static Feedback overdueHandle(){
		return vWallet.overdueHandle();
	}

	/**
	 * ��ȡǮ�����׼�¼
	 * @param page ��ҳ����
	 * @param where ��ѯ��������
	 * @return Feedback
	 */
	public static Feedback getBusinessNotes(PageUtil<Map,BusinessNoteWhere> page){
		return vWallet.getBusinessNotes(page);
	}
	

	/**
	 * Ǯ������
	 * @param wNote ������Ϣ����
	 * @return Feedback
	 */
	public static Feedback withdraw(WithdrawNote wNote){
		return vWallet.withdraw(wNote);
	}
	

	/**
	 * ���ֳ�ʼ����Ϣ
	 * @param account Ǯ���˺ţ���Ϊ�գ�
	 * @return  Json���ݡ��磺{"user":{},"counterFee":{"type":2,"counterFee":0.05}��counterFee��(type{1�̶�ֵ��2�ٷֱ�ֵ})
	 */
	public static JSONObject withdrawInitialInfo(String account){
		return vWallet.withdrawInitialInfo(account);
	}
	
	/**
	 * ����Ǯ�����ּ�¼���ͨ��
	 * @param account ����Ǯ���˻�
	 * @param uadwid ������Ϣ���
	 * @param status ����״̬
	 * @param reason ����ע
	 * @param handler ������
	 * @return
	 */
	public static Feedback withdrawAudit(String account,String uadwid,Status status,String reason,String handler){
		return vWallet.withdrawAudit(account, uadwid, status,reason, handler, 1);
	}

	/**
	 * Ǯ�����ּ�¼�ر�
	 * @param account Ǯ���˺�
	 * @param uadwid ���ּ�¼
	 * @param loseReason �ر�ԭ��
	 * @param noteCloseType �ر�����
	 * @param handler ������
	 * @return Feedback
	 */
	public static Feedback withdrawClose(String account,String uadwid,String loseReason,NoteCloseType noteCloseType,String handler){
		return vWallet.withdrawClose(account, uadwid, loseReason, noteCloseType, handler, 1);
	}

	/**
	 * ��ȡǮ�����ּ�¼
	 * @param page ��ҳ����
	 * @param where ��ѯ��������
	 * @return Feedback
	 */
	public static Feedback getWithdrawNotes(PageUtil<Map,WithdrawNoteWhere> page){
		return vWallet.getWithdrawNotes(page,ModeType.SELECT);
	}
	
	/**
	 * ��ȡǮ�����ּ�¼
	 * @param page ��ҳ����
	 * @param where ��ѯ��������
	 * @return Feedback
	 */
	public static Feedback getWithdrawNotes(PageUtil<Map,WithdrawNoteWhere> page,ModeType modeType){
		return vWallet.getWithdrawNotes(page,modeType);
	}
	
	/**
	 * ��ȡ������Ϣ
	 * @param parentCode ���Ϊ�գ����ȡʡ��������Ϣ
	 * @return
	 */
	public static List<Area> getAreas(String parentCode){
		return vWallet.getAreas(parentCode);
	}
	
	/**
	 * ��ȡ����������Ϣ
	 * @return
	 */
	public static List<Bank> getBanks(){
		return vWallet.getBanks();
	}
	
	/**
	 * ��ѯ���˵���ˮ����
	 * @param page
	 * @param beginDate ʱ��Σ���ʼ���ڡ���ʽ��yyyy-MM-dd�� 
	 * @param endDate ʱ��Σ��������ڡ���ʽ��yyyy-MM-dd�� 
	 */
	public static Feedback findStatDays(PageUtil page, String beginDate, String endDate){
		return vWallet.findStatDays(page, beginDate, endDate);
	}

	/**
	 * ��ѯ���˵���ˮ����
	 * @param page
	 * @param beginDate ʱ��Σ���ʼ���ڡ���ʽ��yyyy-MM-dd�� 
	 * @param endDate ʱ��Σ��������ڡ���ʽ��yyyy-MM-dd�� 
	 */
	public static Feedback findStatMonths(PageUtil page, String beginDate, String endDate){
		return vWallet.findStatMonths(page, beginDate, endDate);
	}
}
