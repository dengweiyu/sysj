package com.li.videoapplication.wxapi;


import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "WXPayEntryActivity";

	public final static String TYPE = "type";
	public final static String WX_PAY = "wx_pay";                  //intent 携带数据的key 表示是微信支付
	public final static String RESULT_NAME = "wx_pay_result";      //intent 携带数据的key 支付结果

	private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(this, "wx27677b3c673d2ade");
		api.handleIntent(getIntent(),this);
		Log.e(TAG,"onCreate");
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(getIntent(),this);
	}

	@Override
	public void onReq(BaseReq baseReq) {
		Log.e(TAG,"onReq");
	}

	@Override
	public void onResp(BaseResp baseResp) {
		Log.e(TAG,"onResp");
		onResponse(baseResp,this);
	}
	/**
	 *WXPayEntryActivity onResp()中调用
	 */
	private void onResponse(BaseResp response,Context context){
		if (response.getType() == ConstantsAPI.COMMAND_PAY_BY_WX){
			try {
				Class paymentActivity = Class.forName("com.li.videoapplication.mvp.mall.view.PaymentWayActivity");
				Intent intent = new Intent(context,paymentActivity);
				intent.putExtra(TYPE,WX_PAY);
				intent.putExtra(RESULT_NAME,response.errCode);
				context.startActivity(intent);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}