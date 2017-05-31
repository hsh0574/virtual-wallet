package com.yzz.virtual.wallet;

public abstract class AbsWalletConfigure {
	/**
	 * 是否实时创建账户。默认为不创建（false）
	 * true时，当处理账户不存在，则创建
	 * @return
	 */
	public boolean realTimeCreatedAccount(){
		return false;
	}
	
	
	/**
	 * 虚拟账户数据源名称
	 * @return
	 */
	public abstract String getWalletDBName();
	/**
	 * 系统配置源名称
	 * @return
	 */
	public abstract String getConfigDBName() ;
	/**
	 * 虚拟账户主数据表名前缀
	 * @return
	 */
	public abstract String getTablePrefix();
	
	/**
	 * 虚拟账户处理拓展接口
	 * @return
	 */
	public abstract AbsWalletExpand getWalletExpand();
	
	/**
	 * 退款或取消，是否原路返回。默认不按原路返回（false）
	 * 如果为true，消费时需要记录消费了哪些获得记录。如果为false，则不处理获得记录，只关注总额
	 * @return
	 */
	public boolean sameReturnForReund_cancel(){
		return false;
	}
	

	/**
	 * 是否有有效期。默认为（false）
	 * 如果为true，获得记录有有效期。如果为false，获得记录无有效期，只关注总额
	 * @return
	 */
	public boolean hasEffectiveTime(){
		return false;
	}
}
