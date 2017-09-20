package com.xhj.learningaccumulation.Utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.FileDescriptor;
import java.io.FileInputStream;

/**
 * 与图片操作相关的工具类
 */
public class ImageUtils {

    /**
     * 将资源路径下的图片资源压缩后返回对应的Bitmap对象
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int requiredWidth, int requiredHeight) {
        // 第一次解析将inJustDecodeBounds置为true用以获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用calculateInSampleSize计算inSampleSize的值
        options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight);
        // 将inJustDecodeBounds置为false,再次解析
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 将磁盘上的FileDescriptor代表的图片文件压缩后返回对应的Bitmap对象
     */
    public static Bitmap decodeSampledBitmapFromDisk(FileDescriptor descriptor, int requiredWidth, int requiredHeight) {
        // 第一次解析将inJustDecodeBounds置为true用以获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Rect outPadding = new Rect(0, 0, requiredWidth, requiredHeight);
        BitmapFactory.decodeFileDescriptor(descriptor, outPadding, options);
        // 调用calculateInSampleSize计算inSampleSize的值
        options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight);
        // 将inJustDecodeBounds置为false,再次解析
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(descriptor, outPadding,
                options);
    }

    public static Bitmap decodeSampledBitmap(FileInputStream is, int requiredWidth, int requiredHeight) {
        // 第一次解析将inJustDecodeBounds置为true用以获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        // 调用calculateInSampleSize计算inSampleSize的值
        options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight);
        // 将inJustDecodeBounds置为false,再次解析
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is, null, options);
    }

    /**
     * 将磁盘上的imagePath路径下的图片文件压缩后返回对应的Bitmap对象
     */
    public static Bitmap decodeSampledBitmapFromDisk(String imagePath, int requiredWidth, int requiredHeight) {
        // 第一次解析将inJustDecodeBounds置为true用以获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        // 调用calculateInSampleSize计算inSampleSize的值
        options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight);
        // 将inJustDecodeBounds置为false,再次解析
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }

    /**
     * 根据传入的BitmapFactory.Options对象和图片需要的宽和高计算出压缩比例
     *
     * @param options        BitmapFactory.Options对象
     * @param requiredWidth  需要的图片的宽度
     * @param requiredHeight 需要的图片的高度
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int requiredWidth, int requiredHeight) {
        // 根据BitmapFactory.Options对象获取原图片的高度和宽度
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;
        if (width > requiredWidth || height > requiredHeight) {
            // 计算出实际的宽高比率
            final int widthRatio = Math.round((float) width
                    / (float) requiredWidth);
            final int heightRatio = Math.round((float) height
                    / (float) requiredHeight);
            // 选择宽高比率最小的作为inSampleSize的值,保证最后图片的宽和高大于等于需要的宽和高
            inSampleSize = widthRatio < heightRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) { // api >= 19
            return getRealPathFromUriAboveApi19(context, uri);
        } else { // api < 19
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())){
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * 添加文字到图片，类似水印文字。
     * @param gContext
     * @param gResId
     * @param gText
     * @return
     */
    public static Bitmap drawTextToBitmap(Context gContext, int gResId, String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(61,61,61));
        // text size in pixels
        paint.setTextSize((int) (14 * scale*5));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        //      int x = (bitmap.getWidth() - bounds.width()) / 2;
        //      int y = (bitmap.getHeight() + bounds.height()) / 2;
        //draw  text  to the bottom
        int x = (bitmap.getWidth() - bounds.width())/10*9 ;
        int y = (bitmap.getHeight() + bounds.height())/10*9;
        canvas.drawText(gText, x , y, paint);

        return bitmap;
    }

}