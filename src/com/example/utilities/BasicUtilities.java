package com.example.utilities;

import java.lang.reflect.Field;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.ViewConfiguration;

public class BasicUtilities {

	/**
	 * @param context
	 * @param action
	 * @return return true if the app can handle the intent
	 */
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	/**
	 * @param context
	 * 
	 *            Enable the overflowMenuItem in the action bar , The three dot
	 *            thingy is displayed NOTE: TO BE CALLED AT THE END OF THE
	 *            ONCREATE
	 */
	public static void overFlowActionItems(Context context) {
		try {
			ViewConfiguration config = ViewConfiguration.get(context);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
			// Ignore
		}
	}

	/**
	 * @param context
	 * 
	 *            Disable the overflowMenuItem in the action bar , The three dot
	 *            thingy is displayed NOTE: TO BE CALLED AT THE END OF THE
	 *            ONCREATE
	 */
	public static void preventOverFlowActionItems(Context context) {
		try {
			ViewConfiguration config = ViewConfiguration.get(context);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, true);
			}
		} catch (Exception ex) {
			// Ignore
		}
	}

	public static void redirectWithClearTop(Activity activity, Class<?> cls) {
		Intent intent = new Intent(activity, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(intent);
		activity.overridePendingTransition(0, 0);
	}

	public static void redirect(Activity activity, Class<?> cls) {
		Intent intent = new Intent(activity, cls);
		activity.startActivity(intent);
		activity.overridePendingTransition(0, 0);
	}

	public static int convertDpToPx(Context context, int dp) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				r.getDisplayMetrics());
		return (int) px;

	}

}
