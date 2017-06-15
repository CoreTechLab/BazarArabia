package bazararabia.com.and.util;

import android.graphics.Bitmap.Config;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import bazararabia.com.and.R;


public class ImageUtil {

	public static void displayImage(ImageView view, String path, int defaultImage , ImageLoadingListener listener) {
		DisplayImageOptions.Builder displayOptionsBuilder= new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.displayer(new FadeInBitmapDisplayer(300, true, false, false))
				.showImageForEmptyUri(defaultImage)
				.showImageOnLoading(defaultImage)
				.showImageOnFail(defaultImage).cacheOnDisk(true)
				.cacheInMemory(true).bitmapConfig(Config.ARGB_8888);

		DisplayImageOptions displayOption = displayOptionsBuilder.build();

		ImageLoader loader = ImageLoader.getInstance();
		try {
			if(path.startsWith("/"))//local file
				loader.displayImage("file://"+path, view, displayOption, listener);
			else
				loader.displayImage(path, view, displayOption, listener);

			view.invalidate();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			loader.clearMemoryCache();
		}
	}

	public static void displayImage(ImageView view, String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();
		try {
			if(path.startsWith("/"))//local file
				loader.displayImage("file://"+path, view, DEFAULT_DISPLAY_IMAGE_OPTIONS, listener);
			else
				loader.displayImage(path, view, DEFAULT_DISPLAY_IMAGE_OPTIONS, listener);

            view.invalidate();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			loader.clearMemoryCache();
		}
	}
	
	public static void displayRoundImage(ImageView view, String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();

		try {
			loader.displayImage(path, view, ROUND_DISPLAY_IMAGE_OPTIONS, listener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			loader.clearMemoryCache();
		}
	}
	
	public static void loadImage(String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();
		try {
			loader.loadImage(path, DEFAULT_DISPLAY_IMAGE_OPTIONS, listener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
	}
	
	//TODO Change default image
	private static final DisplayImageOptions.Builder DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER = new DisplayImageOptions.Builder()
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.displayer(new FadeInBitmapDisplayer(300, true, false, false))
			.showImageForEmptyUri(R.drawable.default_image)
			.showImageOnLoading(R.drawable.default_image)
			.showImageOnFail(R.drawable.default_image).cacheOnDisk(true)
			.cacheInMemory(true).bitmapConfig(Config.ARGB_8888);

	private static final DisplayImageOptions DEFAULT_DISPLAY_IMAGE_OPTIONS = DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER
			.build();
	private static final DisplayImageOptions ROUND_DISPLAY_IMAGE_OPTIONS = DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER
			.displayer(new RoundedBitmapDisplayer(500)).build();
}
