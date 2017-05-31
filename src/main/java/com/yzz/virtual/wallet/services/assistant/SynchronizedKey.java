package com.yzz.virtual.wallet.services.assistant;

import java.util.HashMap;
import java.util.Map;

public class SynchronizedKey {

	private static Map<String, String> accountKeys=new HashMap<String, String>(); 
	private static Map<String, Integer> accountKeySigns=new HashMap<String, Integer>(); 
	
	public static synchronized SyncKeyResult getAccountKey(String account){
		if(accountKeys.get(account)==null){
			accountKeys.put(account, account);
			accountKeySigns.put(account, 1);
		}else accountKeySigns.put(account, accountKeySigns.get(account)+1);
		return new SyncKeyResult(accountKeys.get(account),accountKeySigns.get(account));
	}
	
	public static synchronized void deleteAccountKey(SyncKeyResult ret){
		if(accountKeySigns.get(ret.getKey())==ret.getSign()){
			accountKeys.remove(ret.getKey());
			accountKeySigns.remove(ret.getKey());
		}
	}
	
}
