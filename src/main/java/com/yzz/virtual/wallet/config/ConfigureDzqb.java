package com.yzz.virtual.wallet.config;

import com.yzz.virtual.wallet.AbsWalletConfigure;
import com.yzz.virtual.wallet.AbsWalletExpand;

public class ConfigureDzqb extends AbsWalletConfigure {

	private static String walletDBName;
	private static String configDBName;
	private static String tablePrefix="dzqb_";
	private AbsWalletExpand walletExpand;
	public ConfigureDzqb(){}
	public ConfigureDzqb(AbsWalletExpand walletExpand){
		this.walletExpand=walletExpand;
	}
	public String getWalletDBName() {
		return ConfigureDzqb.walletDBName;
	}
	public void setWalletDBName(String walletDBName) {
		ConfigureDzqb.walletDBName = walletDBName;
	}
	public String getConfigDBName() {
		return ConfigureDzqb.configDBName;
	}
	public void setConfigDBName(String configDBName) {
		ConfigureDzqb.configDBName = configDBName;
	}
	public String getTablePrefix() {
		return ConfigureDzqb.tablePrefix;
	}
	public void setTablePrefix(String tablePrefix) {
		ConfigureDzqb.tablePrefix = tablePrefix;
	}
	public AbsWalletExpand getWalletExpand() {
		return walletExpand;
	}
	public void setWalletExpand(AbsWalletExpand walletExpand) {
		this.walletExpand = walletExpand;
	}
}
