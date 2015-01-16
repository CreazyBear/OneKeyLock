package com.crazybear.onekeylock;

import com.crazybear.onekeylock.R;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;

public class MainActivity extends Activity
{
	private DevicePolicyManager policyManager = null;
	private ComponentName componentName = null;
	private static final int MY_REQUEST_CODE = 1590;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		
		// 获取设备管理服务
		policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		componentName = new ComponentName(this, AdminReceiver.class);

		
		// 判断是否有锁屏权限，若有则立即锁屏并结束自己，若没有则获取权限
		if (policyManager.isAdminActive(componentName))
		{
			policyManager.lockNow();
			finish();
		}
		else
		{
			activeManage();
		}
		setContentView(R.layout.activity_main); // 把这句放在最后，这样锁屏的时候就不会跳出来（闪一下）
	}

	// 获取权限
	private void activeManage()
	{
		// 启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

		// 权限列表
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);

		// 描述(additional explanation)
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "CrazyBear");

		startActivityForResult(intent, MY_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// 获取权限成功，立即锁屏并finish自己，否则继续获取权限
		if (requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK)
		{
			policyManager.lockNow();
			finish();
		}
		else
		{
			finish();//当不不给予权限时，关闭应用
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	
}
