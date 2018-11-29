package com.saptra.sieron.myapplication.Utils;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.mbms.FileInfo;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Globals {
    //Globals
    public static String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    }; //TODO You can Add multiple permissions here.
    public static final int REQUIRED_PERMISSIONS_CODE = 800;
    private Context context;
    public static Globals instance;

    //Constructor
    public Globals(){}

    static {
        instance = new Globals();
    }

    //Get Instance
    public static Globals getInstance(){
        return Globals.instance;
    }

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getShortDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public Date StringToDate(String date){
        Date d = null;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            d = sdf.parse(date);
        }catch(Exception ex){
            Log.e("StringToDate", "Error:" +ex.getLocalizedMessage());
        }
        return  d;
    }

    //Valida si disposiivo está conectado a internet
    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    //Convert file to base64
    public String FileToBase64(Context c, String path){
        String base64File = "";
        try{
            byte[] bFile;
            Bitmap mImageBitmap = GetResizedBitmap(c, Uri.parse(path).getPath());
            if(mImageBitmap != null) {
                bFile = BitmapToByte(mImageBitmap);
                base64File = ByteArrayToB64Sring(bFile);
            }
        }catch (Exception ex){
            Log.e("FileToBase64", "Error:"+ex.getLocalizedMessage());
            Toast.makeText(c,
                    "FileToBase64 Error:"+ex.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
        return base64File;
    }

    //Convert Base64 to file
    public String GetPathFromBase64(Context c, String base64Img, String pathfile){
        String path = "";
        try{
            String [] _file = pathfile.split("/");
            String filename = _file[_file.length-1];
            byte[] bFile = Base64.decode(base64Img, Base64.DEFAULT);
            File storageDir = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            File image = new File(storageDir.getAbsolutePath()+File.separator+filename);

            if (!image.exists()) {
                //write the bytes in file
                FileOutputStream fo = new FileOutputStream(image);
                //image.createNewFile();
                fo.write(bFile);
                // remember close de FileOutput
                fo.close();
            }
            path = "file:" + image.getAbsolutePath();
        }
        catch (Exception ex){
            Log.e("GetPathFromBase64", "Error:"+ex.getLocalizedMessage());
            Toast.makeText(c,
                    "GetPathFromBase64 Error:"+ex.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
        return  path;
    }

    //Drop exist file
    public boolean DropFile(String path){
        boolean result = false;
        try{
            path = path.replace("file:","");
            File file = new File(path);
            if(file.exists()) {
                file.delete();
                result = true;
            }
        }
        catch (Exception ex){
            Log.e("DropFile", "Error:"+ex.getLocalizedMessage());
            result = false;
        }
        return result;
    }

    //Drop All Files from current Session
    public void DropAllFile(Context c){
        try{
            int dropedFiles = 0;
            File storageDir = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if(storageDir.exists()){
                String [] files = storageDir.list();
                for(String file : files){
                    if(file.toLowerCase().contains(".jpeg")) {
                        new File(file).delete();
                        dropedFiles++;
                    }
                }
                Log.e("DropAllFile", "DropedFiles:"+dropedFiles);
            }
        }
        catch (Exception ex){
            Log.e("DropAllFile", "Error:"+ex.getLocalizedMessage());
        }
    }

    //Convert Bitmap to Byte[]
    // Bitmap to byte[]
    public byte[] BitmapToByte(Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //bitmap to byte[] stream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] x = stream.toByteArray();
            //close stream to save memory
            stream.close();
            return x;
        } catch (IOException e) {
            Log.e("bitmapToByte", e.getLocalizedMessage());
        }
        return null;
    }

    public static String GetCurrentDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public Bitmap getBitmap(byte[] bitmap) {
        return BitmapFactory.decodeByteArray(bitmap , 0, bitmap.length);
    }

    public String ByteArrayToB64Sring( byte[] b){
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap Base64ToBitmap(String b64){
        byte[] decodedString = Base64.decode(b64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public Bitmap GetResizedBitmap(Context c, String path) {

        Log.e("GetResizedBitmap", "uri:"+path);
        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = c.getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inPreferredConfig = Bitmap.Config.ARGB_8888;
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = c.getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                //Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            //Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
            b.getHeight();
            return b;
        } catch (IOException e) {
            //Log.e("", e.getMessage(), e);
            return null;
        }
    }
}
