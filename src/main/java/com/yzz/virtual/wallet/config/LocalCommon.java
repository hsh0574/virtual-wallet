package com.yzz.virtual.wallet.config;

import java.util.HashMap;
import java.util.Map;

public class LocalCommon {
	/**
	 * ΢���̳Ǵ����������Ϣ˵��
	 */
	public final static Map<Integer,String> WEIXIN_ERROR_CODE_MSG=new HashMap<Integer, String>();
	
	static{
		//ϵͳ�������� 1000 +
		WEIXIN_ERROR_CODE_MSG.put(1001,"method�ֶ�δ����");
		WEIXIN_ERROR_CODE_MSG.put(1002,"timestamp�ֶ�δ����");
		WEIXIN_ERROR_CODE_MSG.put(1003,"app_key�ֶ�δ����");
		WEIXIN_ERROR_CODE_MSG.put(1004,"v�ֶ�δ����");
		WEIXIN_ERROR_CODE_MSG.put(1005,"sign�ֶ�δ����");
		WEIXIN_ERROR_CODE_MSG.put(1006,"��Ч��ϵͳ������");
		WEIXIN_ERROR_CODE_MSG.put(1007,"������δ�����δע��");
		WEIXIN_ERROR_CODE_MSG.put(1008,"ǩ������");
		WEIXIN_ERROR_CODE_MSG.put(1009,"app_key����");
		WEIXIN_ERROR_CODE_MSG.put(1010,"ʱ�������ȷ��timestamp��ʱ��");
		WEIXIN_ERROR_CODE_MSG.put(1011,"�汾�Ų���ȷ");
		WEIXIN_ERROR_CODE_MSG.put(1012,"�������ݸ�ʽ����ȷ");
		WEIXIN_ERROR_CODE_MSG.put(1013,"ǩ����������ȷ");
		//Ӧ�ü������� 0-1000   ��������,"����˵��"
		WEIXIN_ERROR_CODE_MSG.put(0,"�ɹ�");
		WEIXIN_ERROR_CODE_MSG.put(1,"����δ����");
		WEIXIN_ERROR_CODE_MSG.put(2,"�������Ϸ�");
		WEIXIN_ERROR_CODE_MSG.put(3,"��������ʱ����");
		WEIXIN_ERROR_CODE_MSG.put(4,"ϵͳ�ڲ�����");
		WEIXIN_ERROR_CODE_MSG.put(5,"����Ϊpost����");
		WEIXIN_ERROR_CODE_MSG.put(6,"�˻����������");
		WEIXIN_ERROR_CODE_MSG.put(8,"�˻�����");
		WEIXIN_ERROR_CODE_MSG.put(15,"Ŀ���˻�Ϊ��Ч�˺�");
		WEIXIN_ERROR_CODE_MSG.put(16,"�˻��Ѿ�����");
		WEIXIN_ERROR_CODE_MSG.put(17,"�˻�������");
		WEIXIN_ERROR_CODE_MSG.put(18,"�˺��쳣");
		WEIXIN_ERROR_CODE_MSG.put(19,"�������˻�����Ϊͬһ�˻�");
		WEIXIN_ERROR_CODE_MSG.put(20,"Ŀ���˻�������");
		WEIXIN_ERROR_CODE_MSG.put(21,"����ÿ������ִ���");
		WEIXIN_ERROR_CODE_MSG.put(22,"ԭʼ֧���������");
		WEIXIN_ERROR_CODE_MSG.put(23,"���ڲ�������ʱ��");
		WEIXIN_ERROR_CODE_MSG.put(24,"���ֽ��С����������޶�");
		WEIXIN_ERROR_CODE_MSG.put(25,"���׼�¼�Ѿ�����");
		WEIXIN_ERROR_CODE_MSG.put(26,"û����ؼ�¼����");
		WEIXIN_ERROR_CODE_MSG.put(27,"��¼�Ѿ����������");
		WEIXIN_ERROR_CODE_MSG.put(28,"�����������б䶯");
		
	}
}
