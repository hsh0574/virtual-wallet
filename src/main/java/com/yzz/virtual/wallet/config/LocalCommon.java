package com.yzz.virtual.wallet.config;

import java.util.HashMap;
import java.util.Map;

public class LocalCommon {
	/**
	 * 微信商城错误代码与信息说明
	 */
	public final static Map<Integer,String> WEIXIN_ERROR_CODE_MSG=new HashMap<Integer, String>();
	
	static{
		//系统级错误码 1000 +
		WEIXIN_ERROR_CODE_MSG.put(1001,"method字段未设置");
		WEIXIN_ERROR_CODE_MSG.put(1002,"timestamp字段未设置");
		WEIXIN_ERROR_CODE_MSG.put(1003,"app_key字段未设置");
		WEIXIN_ERROR_CODE_MSG.put(1004,"v字段未设置");
		WEIXIN_ERROR_CODE_MSG.put(1005,"sign字段未设置");
		WEIXIN_ERROR_CODE_MSG.put(1006,"无效的系统级参数");
		WEIXIN_ERROR_CODE_MSG.put(1007,"方法名未定义或未注册");
		WEIXIN_ERROR_CODE_MSG.put(1008,"签名错误");
		WEIXIN_ERROR_CODE_MSG.put(1009,"app_key错误");
		WEIXIN_ERROR_CODE_MSG.put(1010,"时间戳不正确（timestamp超时）");
		WEIXIN_ERROR_CODE_MSG.put(1011,"版本号不正确");
		WEIXIN_ERROR_CODE_MSG.put(1012,"返回数据格式不正确");
		WEIXIN_ERROR_CODE_MSG.put(1013,"签名方法不正确");
		//应用级错误码 0-1000   错误码编号,"错误说明"
		WEIXIN_ERROR_CODE_MSG.put(0,"成功");
		WEIXIN_ERROR_CODE_MSG.put(1,"参数未设置");
		WEIXIN_ERROR_CODE_MSG.put(2,"参数不合法");
		WEIXIN_ERROR_CODE_MSG.put(3,"解析请求时出错");
		WEIXIN_ERROR_CODE_MSG.put(4,"系统内部错误");
		WEIXIN_ERROR_CODE_MSG.put(5,"必须为post请求");
		WEIXIN_ERROR_CODE_MSG.put(6,"账户或密码错误");
		WEIXIN_ERROR_CODE_MSG.put(8,"账户余额不足");
		WEIXIN_ERROR_CODE_MSG.put(15,"目标账户为无效账号");
		WEIXIN_ERROR_CODE_MSG.put(16,"账户已经存在");
		WEIXIN_ERROR_CODE_MSG.put(17,"账户不存在");
		WEIXIN_ERROR_CODE_MSG.put(18,"账号异常");
		WEIXIN_ERROR_CODE_MSG.put(19,"出、入账户不能为同一账户");
		WEIXIN_ERROR_CODE_MSG.put(20,"目标账户不存在");
		WEIXIN_ERROR_CODE_MSG.put(21,"超过每天的提现次数");
		WEIXIN_ERROR_CODE_MSG.put(22,"原始支付密码错误");
		WEIXIN_ERROR_CODE_MSG.put(23,"现在不是提现时间");
		WEIXIN_ERROR_CODE_MSG.put(24,"提现金额小于最低提现限额");
		WEIXIN_ERROR_CODE_MSG.put(25,"交易记录已经存在");
		WEIXIN_ERROR_CODE_MSG.put(26,"没有相关记录存在");
		WEIXIN_ERROR_CODE_MSG.put(27,"记录已经被处理过了");
		WEIXIN_ERROR_CODE_MSG.put(28,"提现手续费有变动");
		
	}
}
