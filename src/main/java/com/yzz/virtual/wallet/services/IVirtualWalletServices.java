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
	 * ����˻���Ϣ
	 * @param account �����˻�
	 * @param froms ��Դ
	 * @return AccountInfo
	 */
	public AccountInfo checkAccount(String account,int froms);

	/**
	 * ����˻���Ϣ
	 * @param account �����˻�
	 * @param froms ��Դ
	 * @param isCreated �������˻�ʱ���Ƿ񴴽���true���Զ�����
	 * @return AccountInfo
	 */
	public AccountInfo checkAccount(String account,int froms,boolean isCreated);
	
	/**
	 * ���������˻�
	 * 
	 * @param account �����˻�
	 * @param pwd  ����
	 * @param froms ��Դ
	 * @return 0�ɹ���4ϵͳ�ڲ�����
	 */
	public int created_account(String account,String pwd,int froms);

	/**
	 * ���������˻�
	 * @param account
	 * @param pwd ����Ϊnull
	 * @param froms ��Դ
	 * @return 0�ɹ���4ϵͳ����(ʧ��)
	 */
	public int activation_account(String account,String pwd,int froms);
	
	/**
	 * �޸�֧������
	 * @param account �����˻�
	 * @param oldPwd ���ݿ���ԭʼ����
	 * @param newPwd ������
	 * @param froms ��Դ
	 * @return 0�ɹ���4�޸�ʧ��
	 */
	public int change_paypwd(String account,String oldPwd, String newPwd, int froms);
	
	/**
	 * ��ȡ�˻����
	 * 
	 * @param account �����˻�
	 * @param froms ��Դ
	 * @return �˻���-101ϵͳ�ڲ�����
	 */
	public Integer get_balance(String account, int froms);
	
	/**
	 * ��ȡ�����˺���Ϣ
	 * @param page ��ҳ����
	 * @param account �����˺�
	 * @param status ״̬
	 * @param beginDate ����ʱ��Σ���ʼʱ�䣩
	 * @param endDate ����ʱ��Σ�����ʱ�䣩
	 * @param froms ��Դ
	 */
	public void getAccounts(PageUtil<Map, ?> page,String account,AccountStatus status,String beginDate,String endDate,int froms);
	
	/**
	 * �����˻�����
	 * @param bNote
	 * @return 0�ɹ���1ʧ�ܣ�2��������4ϵͳ����8���㣻25���׼�¼�Ѿ�����
	 */
	public Feedback business(BusinessNote bNote);

	/**
	 * �����˻�����
	 * @param conn
	 * @param bNote
	 * @return 0�ɹ���1ʧ�ܣ�2��������4ϵͳ����8���㣻25���׼�¼�Ѿ�����
	 */
	public Feedback business(Connection conn,BusinessNote bNote) throws Exception;

	/**
	 * ��ü�¼���ڴ���
	 * @return
	 */
	public Feedback overdueHandle();
	
	/**
	 * 
	 * ��ȡ�����˻����׼�¼
	 * @param page ��ҳ����
	 * @return
	 */
	public void get_business_notes(PageUtil<Map,BusinessNoteWhere> page);
	

	/**
	 * �����˻�����
	 * @param wNote ������Ϣ
	 * @param froms  ��Դ
	 * @return 0�ɹ���1ʧ�ܣ�8���㣻4ϵͳ����
	 */
	public Feedback withdraw(WithdrawNote wNote,int froms);

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
	 * @param account �����˻�
	 * @param uadwid ������Ϣ���
	 * @param loseReason ������Ϣ��¼�ر�ԭ��
	 * @param froms ��Դ
	 * @param handler �رղ�����
	 * @param closeType �ر����ͣ�1��ˣ�2��Ʊ��3����Ʊ������ʧ�ܣ�
	 * @return 0�ɹ���1ʧ�ܣ�26���ּ�¼�����ڣ�4ϵͳ����
	 */
	public int withdrawClose(String account,String uadwid,String loseReason,String handler,NoteCloseType closeType,int froms);
	
	/**
	 * ��ȡ�����˻����ּ�¼
	 * @param page ��ҳ����
	 * @param modeType ��ȡ����ʽ
	 */
	public Feedback get_withdraw_notes(PageUtil<Map,WithdrawNoteWhere> page,ModeType modeType);
	

	/**
	 * ��ȡϵͳ����ֵ
	 * @param name ����
	 * @return ������nullʱ��˵����ȡʧ�ܣ�
	 */
	public Object getSystemParam(String name);
	

	/**
	 * ��ȡ��ǰ�˻����ִ���
	 * @return ���ִ���ֵ��������-100ʱ��˵����ȡʧ�ܣ�
	 */
	public int getWithdrawCount(String account);
	

	/*********************������Ϣ begin********************************/
	
	/**
	 * ��ȡ������Ϣ
	 * @param parentCode
	 * @return
	 */
	public List<Area> getAreas(String parentCode);
	
	/**
	 * ��ȡ����������Ϣ
	 * @return
	 */
	public List<Bank> getBanks();

	/*********************������Ϣ end********************************/
	
	/*********************ͳ����Ϣ begin********************************/
	
	/**
	 * ��ѯ���˵���ˮ����
	 * @param page
	 * @param beginDate ʱ��Σ���ʼ���ڡ���ʽ��yyyy-MM-dd�� 
	 * @param endDate ʱ��Σ��������ڡ���ʽ��yyyy-MM-dd�� 
	 */
	public void findStatDays(PageUtil page, String beginDate, String endDate);

	/**
	 * ��ѯ���˵���ˮ����
	 * @param page
	 * @param beginDate ʱ��Σ���ʼ���ڡ���ʽ��yyyy-MM-dd�� 
	 * @param endDate ʱ��Σ��������ڡ���ʽ��yyyy-MM-dd�� 
	 */
	public void findStatMonths(PageUtil page, String beginDate, String endDate);
	
	/*********************ͳ����Ϣ end********************************/
	
}
