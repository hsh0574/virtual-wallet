package com.yzz.virtual.wallet;

public abstract class AbsWalletExpand {
	/**
	 * �˻���Ϣ��չ�ֶβ�ѯ����
	 * @return
	 */
	public String selectAccountFields(){
		return null;
	}
	
	/**
	 * ���׼�¼��չ�ֶβ�ѯ����
	 * @return
	 */
	public String selectBusinessNoteFields(){
		return null;
	}
	
	/**
	 * ���ּ�¼��չ�ֶβ�ѯ����
	 * @return
	 */
	public String selectWithdrawNoteFields(){
		return null;
	}
	
	/**
	 * �˻��Ƿ���Ч��ʵʱ�����˻�״̬�£����˻�������ʱ������˷�������true���򴴽��˻�
	 * @param account
	 * @return
	 */
	public boolean validAccount(String account){
		return false;
	}
}
