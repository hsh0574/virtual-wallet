package com.yzz.virtual.wallet;

public abstract class AbsWalletExpand {
	/**
	 * 账户信息拓展字段查询内容
	 * @return
	 */
	public String selectAccountFields(){
		return null;
	}
	
	/**
	 * 交易记录拓展字段查询内容
	 * @return
	 */
	public String selectBusinessNoteFields(){
		return null;
	}
	
	/**
	 * 提现记录拓展字段查询内容
	 * @return
	 */
	public String selectWithdrawNoteFields(){
		return null;
	}
	
	/**
	 * 账户是否有效。实时创建账户状态下，当账户不存在时，如果此方法返回true，则创建账户
	 * @param account
	 * @return
	 */
	public boolean validAccount(String account){
		return false;
	}
}
