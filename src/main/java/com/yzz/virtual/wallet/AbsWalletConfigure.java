package com.yzz.virtual.wallet;

public abstract class AbsWalletConfigure {
	/**
	 * �Ƿ�ʵʱ�����˻���Ĭ��Ϊ��������false��
	 * trueʱ���������˻������ڣ��򴴽�
	 * @return
	 */
	public boolean realTimeCreatedAccount(){
		return false;
	}
	
	
	/**
	 * �����˻�����Դ����
	 * @return
	 */
	public abstract String getWalletDBName();
	/**
	 * ϵͳ����Դ����
	 * @return
	 */
	public abstract String getConfigDBName() ;
	/**
	 * �����˻������ݱ���ǰ׺
	 * @return
	 */
	public abstract String getTablePrefix();
	
	/**
	 * �����˻�������չ�ӿ�
	 * @return
	 */
	public abstract AbsWalletExpand getWalletExpand();
	
	/**
	 * �˿��ȡ�����Ƿ�ԭ·���ء�Ĭ�ϲ���ԭ·���أ�false��
	 * ���Ϊtrue������ʱ��Ҫ��¼��������Щ��ü�¼�����Ϊfalse���򲻴����ü�¼��ֻ��ע�ܶ�
	 * @return
	 */
	public boolean sameReturnForReund_cancel(){
		return false;
	}
	

	/**
	 * �Ƿ�����Ч�ڡ�Ĭ��Ϊ��false��
	 * ���Ϊtrue����ü�¼����Ч�ڡ����Ϊfalse����ü�¼����Ч�ڣ�ֻ��ע�ܶ�
	 * @return
	 */
	public boolean hasEffectiveTime(){
		return false;
	}
}
