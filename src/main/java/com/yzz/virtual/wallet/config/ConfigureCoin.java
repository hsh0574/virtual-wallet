package com.yzz.virtual.wallet.config;

import com.yzz.virtual.wallet.AbsWalletConfigure;
import com.yzz.virtual.wallet.AbsWalletExpand;

public class ConfigureCoin extends AbsWalletConfigure {

	private static String walletDBName;
	private static String configDBName;
	private static String tablePrefix="coin_";
	private static boolean sameReturn=true;
	private static AbsWalletExpand walletExpand;
	public ConfigureCoin(){}
	public ConfigureCoin(AbsWalletExpand walletExpand){
		ConfigureCoin.walletExpand=walletExpand;
	}
	public boolean realTimeCreatedAccount(){
		return true;
	}
	public String getWalletDBName() {
		return ConfigureCoin.walletDBName;
	}
	public void setWalletDBName(String walletDBName) {
		ConfigureCoin.walletDBName = walletDBName;
	}
	public String getConfigDBName() {
		return ConfigureCoin.configDBName;
	}
	public void setConfigDBName(String configDBName) {
		ConfigureCoin.configDBName = configDBName;
	}
	public String getTablePrefix() {
		return ConfigureCoin.tablePrefix;
	}
	public void setTablePrefix(String tablePrefix) {
		ConfigureCoin.tablePrefix = tablePrefix;
	}
	public AbsWalletExpand getWalletExpand() {
		return ConfigureCoin.walletExpand;
	}
	public void setWalletExpand(AbsWalletExpand walletExpand) {
		ConfigureCoin.walletExpand = walletExpand;
	}
	public void setSameReturn(Boolean sameReturn) {
		ConfigureCoin.sameReturn = sameReturn;
	}
	public boolean sameReturnForReund_cancel(){
		return ConfigureCoin.sameReturn;
	}
	public boolean hasEffectiveTime(){
		return ConfigureCoin.sameReturn;
	}
}
