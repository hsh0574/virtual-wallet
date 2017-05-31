package com.yzz.virtual.wallet;

import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.yzz.virtual.wallet.config.ConfigureCoin;
import com.yzz.virtual.wallet.entity.BusinessNote;
import com.yzz.virtual.wallet.entity.BusinessNote.SubTypes;
import com.yzz.virtual.wallet.entity.BusinessNoteWhere;
import com.yzz.virtual.wallet.entity.Feedback;

import cn.com.yofogo.frame.assistant.PageUtil;


public class WalletTest {
	
	@Before
	public void testBefor(){
		ConfigureCoin config=new ConfigureCoin();
		config.setConfigDBName("ssweixin");
		config.setWalletDBName("virtual");
		config.setTablePrefix("coin_");
	}

	//创建账户
	//@Test
	public void createdAccount(){
		System.out.println(CoinHandler.createdAccount("-10000",null));

	}

	//修改支付密码
	//@Test
	public void changePaypwd(){
		System.out.println(CoinHandler.changePaypwd("15381901840", null, "1234567"));
	}
	
	
	//是否有支付密码
	/*@Test
	public void hasPaypwd(){
		//System.out.println(CoinHandler.hasPaypwd("wdianU201507241"));
	}*/
	
	//获取账户余额
	@Test
	public void getBalance(){
		Feedback b=CoinHandler.getBalance("00009");
		System.out.println("结果:::::"+b.getCode()+"::::"+b.getMessage());
		if(b.getCode()==0) System.out.println("balance=========="+new java.text.DecimalFormat("#.00").format(b.getInfo().get("balance")));
	}
	
	//验证支付密码
	/*@Test
	public void checkPaypwd(){
		System.out.println(CoinHandler.checkPaypwd("15381901840", "123456"));
	}*/

	//交易
	//@Test
	public void getBusiness(){
		BusinessNote note=new BusinessNote();
		note.setAccount("00009");
		note.setAccountName("00009");
		note.setTargetAccount("00008");
		note.setTargetAccountName("00008");
		note.setMoney(10);
		note.setSubTypes(SubTypes.TURN_OUT);
		note.setOutTradeNo(UUID.randomUUID().toString());
		note.setOutType(61);
		note.setOutTypeName("外加数据测试");
		note.setRemarks("外加数据测试");
		note.setReqIP("172.16.19.161");
		Calendar c=Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		note.setUseBegin(c.getTime());
		c.add(Calendar.MONTH, 6);
		note.setUseEnd(c.getTime());
		Feedback fb = CoinHandler.business(note);
		System.out.println(fb.getCode()+"\t"+fb.getMessage());
		System.out.println(fb.getInfo());
	}
	//交易记录
	@Test
	public void getBusinessNotes(){
		BusinessNoteWhere where =new BusinessNoteWhere();
		where.setTargetAccount("00008");
		where.setAccount("00009");
		where.setManager(true);
		PageUtil<Map, BusinessNoteWhere> pageB=new PageUtil();
		pageB.setConditions(where);
		pageB.setPageNo(1);
		pageB.setPageSize(3);			
		System.out.println(CoinHandler.getBusinessNotes(pageB));
		System.out.println(pageB.getRecTotal()+"\t"+pageB.getList().size());
	}
	
	
	//提现
	//@Test
	public void withdraw(){
		/*WithdrawNote wNote =new WithdrawNote();
		wNote.setAccount("wdian15381901826");
		wNote.setPayPwd("123456");
		wNote.setFees(10);
		wNote.setBankCode("1001");
		wNote.setBankName("招商银行");
		wNote.setBankNo("0000000000000");
		wNote.setBankUserName("杨政洲");
		wNote.setProvinceCode("1");
		wNote.setCityCode("10");
		wNote.setSubBankName("招商银行支行");
		wNote.setMode(2);
		wNote.setRemark("洞彻");
		WithdrawNoteResult ret=CoinHandler.withdraw(wNote);
		System.out.println(ret.getStatus()+"\t"+ret.getMsg()+"\t"+ret.getMinMoney()+"\t"+ret.getAllowCount());
	*/
		
	}
	
	//关闭提现记录
	//Test
	public void withdrawCloseForAudit(){
	/*
	{
		System.out.println(CoinHandler.withdrawCloseForAudit("yzz_test_001", "TX2015052741", "审核不通过", "system"));
		System.out.println(CoinHandler.withdrawCloseForAudit("yzz_test_002", "TX2015052742", "审核不通过", "system"));
		System.out.println(CoinHandler.withdrawCloseForAudit("yzz_test_002", "TX2015052743", "审核不通过", "system"));
		System.out.println(CoinHandler.withdrawCloseForAudit("yzz_test_002", "TX2015052744", "审核不通过", "system"));
		System.out.println(CoinHandler.withdrawCloseForAudit("yzz_test_002", "TX2015052745", "审核不通过", "system"));
	}*/
	}
	
	
	//提现记录
	//@Test
	public void getWithdrawNotes(){
		/*PageUtil page=new PageUtil();
		page.setPageNo(1);
		page.setPageSize(2);
		CoinHandler.getWithdrawNotes(page, "15381901828");
		System.out.println(page.getRecTotal()+"\t"+page.getList().size());
		*/
	}
	
	
	//转移账户
	//@Test
	public void transfer(){
		//System.out.println(CoinHandler.transfer("wdian15381901826", "15381901840", false));
	}
}
