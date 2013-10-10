package com.example.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.example.icloset.R;

public class PhotoUtilities {

	public static final String CAMERA_DIR = "/DCIM/";
	public static final String JPEG_FILE_PREFIX = "IMG_";
	public static final String JPEG_FILE_SUFFIX = ".jpg";
	public static final String TAG = PhotoUtilities.class.getSimpleName();

	private static File getAlbumDir(Context context) {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			storageDir = getAlbumStorageDir(getAlbumName(context));

			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}
		} else {
			Log.e(context.getString(R.string.app_name),
					"External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}

	private static File getAlbumStorageDir(String albumName) {
		return new File(Environment.getExternalStorageDirectory() + CAMERA_DIR
				+ albumName);
	}

	public static File setUpPhotoFile(Context context) throws IOException {

		File f = createImageFile(context);
		return f;
	}

	@SuppressLint("SimpleDateFormat")
	private static File createImageFile(Context context) throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir(context);
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX,
				albumF);
		return imageF;
	}

	/* Photo album for this application */
	private static String getAlbumName(Context context) {
		return context.getString(R.string.album_name);
	}

	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	/**
	 * @param mImageView
	 * @param photoPath
	 *            Pass the imageView and the image URL as a string
	 */
	public static void setPic(ImageView mImageView, String photoPath,
			int reqWidth, int reqHeight) {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */
		/* Get the size of the ImageView */
		/* Get the size of the image */

		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photoPath, bmOptions);

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;

		bmOptions.inSampleSize = calculateInSampleSize(bmOptions, reqWidth,
				reqHeight);
		bmOptions.inPurgeable = true;
		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
		/* Associate the Bitmap to the ImageView */
		mImageView.setImageBitmap(bitmap);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			// This is because sometimes you might need to set the imageView as
			// fill_parent for the width

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * @param context
	 * @param mCurrentPhotoPath
	 * 
	 *            Use this if the images have to appear in the gallery of the
	 *            android phone so that other applications can also use it
	 */
	public static void galleryAddPic(Context context, String mCurrentPhotoPath) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		context.sendBroadcast(mediaScanIntent);
	}

}
