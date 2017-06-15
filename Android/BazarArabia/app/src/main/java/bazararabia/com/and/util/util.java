package bazararabia.com.and.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import bazararabia.com.and.R;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class util {

    public static final String PHOTO_FOLDER_NAME = "CardPhotos";
    public static final String APP_FOLDER_NAME = "enRICH";
    public static final String AUDIO_FOLDER_NAME = "Audio";
    public static final String SCANNERS_FOLDER_NAME = "Scanners";
    public static final String APP_TEMPFOLDER_NAME = "temp";



    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span)
    {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                // Do something with span.getURL() to handle the link click...
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    public static void setTextViewHTML(TextView text, String html)
    {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static String saveImageToExternalStorage(Bitmap image , String fileName) {
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+APP_FOLDER_NAME;
        try
        {
            File dir = new File(fullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fullPath = fullPath+"/"+APP_TEMPFOLDER_NAME;
            File tempDir = new File(fullPath);
            if(!tempDir.exists())
            {
                tempDir.mkdirs();
            }

            OutputStream fOut = null;
            File file = new File(fullPath, fileName);
            if(file.exists())
                file.delete();
            file.createNewFile();
            fOut = new FileOutputStream(file);
            // 100 means no compression, the lower you go, the stronger the compression
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            image.recycle();
            image = null;
            return file.getAbsolutePath();
        }
        catch (Exception e)
        {
            Log.e("saveToExternalStorage()", e.getMessage());
            return "";
        }
    }

    public static Bitmap decodeFile(File f,int WIDTH,int HEIGHT){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //landscape
            int desired_width = WIDTH;
            int desired_height = HEIGHT;
            if(o.outWidth<o.outHeight) {
                desired_height = WIDTH;
                desired_width = HEIGHT;
            }


            //The new size we want to scale to
            final int REQUIRED_WIDTH=desired_width;
            final int REQUIRED_HIGHT=desired_height;
            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        catch (Exception e)
        {e.printStackTrace();}
        return null;
    }

    public static String saveImageInExternalCacheDir(Context context, Bitmap bitmap, String myfileName) {
        SimpleDateFormat format = new SimpleDateFormat("MM dd yyyy HH:mm:ss");
        String strTime = format.format(Calendar.getInstance().getTime());

        String fileName = myfileName + strTime.replace(' ', '_').replace(":", "_");
        String filePath = (context.getExternalCacheDir()).toString() + "/" + fileName + ".jpg";
        try {
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return filePath;
    }



    public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.setRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    public static void showCardImageViewWithPicasso(Context context, String filePathOrUri , ImageView imageView , int defaultDrawable)
    {
        if(filePathOrUri.equalsIgnoreCase(""))
        {
            imageView.setImageResource(defaultDrawable);
            imageView.invalidate();
        }
        else {
            //local file
            String path = filePathOrUri;
            if (filePathOrUri.startsWith("/")) {
                path = "file://"+filePathOrUri;
            }
            Picasso.with(context).load(path).placeholder(defaultDrawable).into(imageView);
        }
    }

    public static boolean isAudioFileExist(String fileName)
    {
        try {
            // todo change the file location/name according to your needs
            String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APP_FOLDER_NAME;

            File appFolder = new File(fullPath);
            if (!appFolder.exists()) {
                appFolder.mkdirs();
            }
            fullPath = fullPath + "/" + "Audio";
            File audioDir = new File(fullPath);
            if (!audioDir.exists()) {
                audioDir.mkdirs();
            }
            fullPath = fullPath + "/" + fileName;
            File audioFile = new File(fullPath);
            if(audioFile.exists())
                return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    public static String getScannedCardImageFilePath(String fileName)
    {
        try {
            // todo change the file location/name according to your needs
            String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APP_FOLDER_NAME;

            File appFolder = new File(fullPath);
            if (!appFolder.exists()) {
                appFolder.mkdirs();
            }
            fullPath = fullPath + "/" + "Scanners";
            File scanDir = new File(fullPath);
            if (!scanDir.exists()) {
                scanDir.mkdirs();
            }
            fullPath = fullPath + "/" + fileName;
            return fullPath;
        }catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getAppUsingFilePath(String folderName , String fileName)
    {
        try {
            // todo change the file location/name according to your needs
            String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APP_FOLDER_NAME;

            File appFolder = new File(fullPath);
            if (!appFolder.exists()) {
                appFolder.mkdirs();
            }
            fullPath = fullPath + "/" + folderName;
            File audioDir = new File(fullPath);
            if (!audioDir.exists()) {
                audioDir.mkdirs();
            }
            fullPath = fullPath + "/" + fileName;
            return fullPath;
        }catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getVoiceRecordAudioFilePath(String fileName)
    {
        try {
            // todo change the file location/name according to your needs
            String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APP_FOLDER_NAME;

            File appFolder = new File(fullPath);
            if (!appFolder.exists()) {
                appFolder.mkdirs();
            }
            fullPath = fullPath + "/" + "Record";
            File audioDir = new File(fullPath);
            if (!audioDir.exists()) {
                audioDir.mkdirs();
            }
            fullPath = fullPath + "/" + fileName;
            return fullPath;
        }catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

//    public static boolean writeResponseBodyToDisk(Response<ResponseBody> response , String folderName , String fileName) {
//        try {
            // todo change the file location/name according to your needs
//            String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+APP_FOLDER_NAME;
//
//            File appFolder = new File(fullPath);
//            if (!appFolder.exists()) {
//                appFolder.mkdirs();
//            }
//            fullPath = fullPath + "/" + folderName;
//            File audioDir = new File(fullPath);
//            if(!audioDir.exists()) {
//                audioDir.mkdirs();
//            }
//            fullPath = fullPath + "/" + fileName;
//            File outputFile = new File(fullPath);
//
//            InputStream inputStream = null;
//            OutputStream outputStream = null;
//
//            ResponseBody body = response.body();
//
//            try {
//                byte[] fileReader = new byte[4096];
//
//                long fileSize = body.contentLength();
//                long fileSizeDownloaded = 0;
//
//                inputStream = body.byteStream();
//                outputStream = new FileOutputStream(outputFile);
//
//                while (true) {
//                    int read = inputStream.read(fileReader);
//
//                    if (read == -1) {
//                        break;
//                    }
//
//                    outputStream.write(fileReader, 0, read);
//
//                    fileSizeDownloaded += read;
//
//                    Log.d("File Dwonloading", "file download: " + fileSizeDownloaded + " of " + fileSize);
//                }
//
//                outputStream.flush();
//
//                return true;
//            } catch (IOException e) {
//                return false;
//            } finally {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//            }
//        } catch (IOException e) {
//            return false;
//        }
//    }

    /*
        * Gets the file path of the given Uri.
        */
    @SuppressLint("NewApi")
    public static String getPath(Context context ,Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[] {
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getApplicationContext().getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
