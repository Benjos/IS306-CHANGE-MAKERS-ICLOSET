package com.example.icloset;

import android.app.Application;

public class IclosetApplication extends Application {

	public static final String DEFAULT_DISK_CACHE_DIR = "Icloset";

	// public static Application application;
	// public Activity currentActivity = null;

	@Override
	public void onCreate() {
		super.onCreate();
		// application = (Application) this;
		// defaultCacheParams = new ImageCacheParams(getApplicationContext(),
		// DEFAULT_DISK_CACHE_DIR);
		// defaultCacheParams.setMemCacheSizePercent(0.25f); // Set memory cache
		// to
		// // 25% of

	}

}