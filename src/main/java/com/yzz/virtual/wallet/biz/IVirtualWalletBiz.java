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
	 * ���������˻�
	 * @param account �����˺�
	 * @param pwd �����˺�����
	 * @param froms ��Դ
	 * @return Feedback
	 */
	public Feedback createdAccount(String account,String pwd,int froms);
	
	/**
	 * �޸������˺�֧������
	 * @param account �����˺�
	 * @param oldPwd ԭʼ����(������״��������룬��Ϊnull)
	 * @param newPwd ������
	 * @param froms ��Դ
	 * @return Feedback
	 */
	public Feedback changePaypwd(String account,String oldPwd,String newPwd,int froms);
	
	

	/**
	 * ��ȡ�˻����
	 * @param account �����˺�
	 * @param froms ��Դ
	 * @return Feedback
	 */
	public Feedback getBalance(String account,int froms);
	
	/**
	 * ��ȡ�����˺���Ϣ
	 * @param page ��ҳ����
	 * @param account �����˺�
	 * @param status ״̬
	 * @param beginDate ����ʱ��Σ���ʼʱ�䣩
	 * @param endDate ����ʱ��Σ�����ʱ�䣩
	 * @param froms ��Դ
	 */
	public Feedback getAccounts(PageUtil<Map, ?> page,String account,AccountStatus status,String beginDate,String endDate,int froms);

	/**********************************���� begin****************************************/
	
	/**
	 * �����˻�����
	 * @param bNote �������ݶ���
	 * @return Feedback
	 */
	public Feedback business(BusinessNote bNote);

	/**
	 * �����˻�����
	 * @param conn
	 * @param bNote
	 * @return code(0�ɹ���1ʧ�ܣ�2��������4ϵͳ����8���㣻25���׼�¼�Ѿ�����)
	 */
	public Feedback business(Connection conn,BusinessNote bNote) throws Exception;

	/**
	 * ��ü�¼���ڴ���
	 * @return
	 */
	public Feedback overdueHandle();

	/**
	 * ��ȡ���⽻�׼�¼
	 * @param page ��ҳ����
	 * @return Feedback
	 */
	public Feedback getBusinessNotes(PageUtil<Map,BusinessNoteWhere> page);
	
	/**********************************���� end****************************************/
	/**********************************���� begin****************************************/
	
	/**
	 * �����˻�����
	 * @param wNote ������Ϣ����
	 * @return Feedback
	 */
	public Feedback withdraw(WithdrawNote wNote);

	/**
	 * ���ֳ�ʼ����Ϣ
	 * @param account �����˺ţ���Ϊ�գ�
	 * @return  Json���ݡ��磺{"user":{},"counterFee":{"type":2,"counterFee":0.05}��counterFee��(type{1�̶�ֵ��2�ٷֱ�ֵ})
	 */
	public JSONObject withdrawInitialInfo(String account);
	
	/**
	 * �����˻����ּ�¼���ͨ��
	 * @param account �����˻�
	 * @param uadwid ������Ϣ���
	 * @param status ����״̬
	 * @param reason ����ע
	 * @param handler ������
	 * @param froms ��Դ
	 * @return
	 */
	public Feedback withdrawAudit(String account,String uadwid,Status status,String reason,String handler,int froms);
	
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
	public Feedback withdrawClose(String account,String uadwid,String loseReason,NoteCloseType noteCloseType,String handler,int froms);

	/**
	 * ��ȡ�����˻����ּ�¼
	 * @param page ��ҳ����
	 * @param modeType ��ȡ����ʽ
	 * @return Feedback
	 */
	public Feedback getWithdrawNotes(PageUtil<Map,WithdrawNoteWhere> page,ModeType modeType);

	/**********************************���� end****************************************/
	/************************************������Ϣ begin********************************/
	
	/**
	 * ��ȡ������Ϣ
	 * @param parentCode ���Ϊ�գ����ȡʡ��������Ϣ
	 * @return
	 */
	public List<Area> getAreas(String parentCode);
	
	/**
	 * ��ȡ����������Ϣ
	 * @return
	 */
	public List<Bank> getBanks();
	
	/***********************************������Ϣ end********************************/

	/*********************ͳ����Ϣ begin********************************/
	
	/**
	 * ��ѯ���˵���ˮ����
	 * @param page
	 * @param beginDate ʱ��Σ���ʼ���ڡ���ʽ��yyyy-MM-dd�� 
	 * @param endDate ʱ��Σ��������ڡ���ʽ��yyyy-MM-dd�� 
	 */
	public Feedback findStatDays(PageUtil page, String beginDate, String endDate);

	/**
	 * ��ѯ���˵���ˮ����
	 * @param page
	 * @param beginDate ʱ��Σ���ʼ���ڡ���ʽ��yyyy-MM-dd�� 
	 * @param endDate ʱ��Σ��������ڡ���ʽ��yyyy-MM-dd�� 
	 */
	public Feedback findStatMonths(PageUtil page, String beginDate, String endDate);
	
	/*********************ͳ����Ϣ end********************************/
}
