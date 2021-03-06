package br.alesanfer.meuimei;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private TextView tIMEI, tInfos;
    private String infos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("IMEI-Cia");


        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            tIMEI = (TextView) findViewById(R.id.txtIMEI);
            tIMEI.setText("IMEI : "+ getIMEI(this));


            infos = "Model: " + android.os.Build.MODEL+"\n"+
                    "IMSI: "+ getIMSI(this)+ "\n"+
                    "Software Version: " + getSoftwareVersion(this)+ "\n"+
                    "Serial number: " + getSimSerialNumber(this)+ "\n"+
                    "Operadora: " + getNetworkOperatorName(this);

            tInfos = (TextView) findViewById(R.id.txtOthers);
            tInfos.setText(infos);


            Button btEnviar = (Button) findViewById(R.id.btEnviar);

            btEnviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView tvImei = (TextView) findViewById(R.id.txtIMEI);

                    Bitmap bm = takeScreenShot(MainActivity.this);
                    File file = saveBitmap(bm, "imei.jpeg");
                    Log.i("chase", "filepath: "+file.getAbsolutePath());
                    Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "MeuIMEI App \n " + tvImei.getText().toString()+ "\n" + infos );
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.setType("image/*");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "Compartilhar via"));
                }
            });



        }else{
            // Solicita as permissões
            String[] permissoes = new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };

            if ( PermissionUtils.validate(this, 0, permissoes) ) {

                tIMEI = (TextView) findViewById(R.id.txtIMEI);
                tIMEI.setText("IMEI : "+ getIMEI(this));


                infos = "Model: " + android.os.Build.MODEL+"\n"+
                        "IMSI: "+ getIMSI(this)+ "\n"+
                        "Software Version: " + getSoftwareVersion(this)+ "\n"+
                        "Serial number: " + getSimSerialNumber(this)+ "\n"+
                        "Operadora: " + getNetworkOperatorName(this);

                tInfos = (TextView) findViewById(R.id.txtOthers);
                tInfos.setText(infos);


                Button btEnviar = (Button) findViewById(R.id.btEnviar);

                btEnviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        TextView tvImei = (TextView) findViewById(R.id.txtIMEI);

                        Bitmap bm = takeScreenShot(MainActivity.this);
                        File file = saveBitmap(bm, "imei.jpeg");
                        Log.i("chase", "filepath: "+file.getAbsolutePath());
                        Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "MeuIMEI App \n " + tvImei.getText().toString()+ "\n" + infos );
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent.setType("image/*");
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(shareIntent, "Compartilhar via"));
                    }
                });

            }
        }





    }




    private static Bitmap takeScreenShot(Activity activity)
    {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height  - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }



    private static File saveBitmap(Bitmap bm, String fileName){
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                alertAndFinish();
                return;
            }
        }
            tIMEI = (TextView) findViewById(R.id.txtIMEI);
            tIMEI.setText("IMEI : "+ getIMEI(this));


            infos = "Model: " + android.os.Build.MODEL+"\n"+
                    "IMSI: "+ getIMSI(this)+ "\n"+
                    "Software Version: " + getSoftwareVersion(this)+ "\n"+
                    "Serial number: " + getSimSerialNumber(this)+ "\n"+
                    "Operadora: " + getNetworkOperatorName(this);

            tInfos = (TextView) findViewById(R.id.txtOthers);
            tInfos.setText(infos);

        Button btEnviar = (Button) findViewById(R.id.btEnviar);

        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView tvImei = (TextView) findViewById(R.id.txtIMEI);

                Bitmap bm = takeScreenShot(MainActivity.this);
                File file = saveBitmap(bm, "imei.jpeg");
                Log.i("chase", "filepath: "+file.getAbsolutePath());
                Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "MeuIMEI App \n " + tvImei.getText().toString()+ "\n" + infos );
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "Compartilhar via"));
            }
        });

    }

    private void alertAndFinish() {
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name).setMessage("Para utilizar este aplicativo, você precisa aceitar as permissões.");

            // Add the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            android.support.v7.app.AlertDialog dialog = builder.create();
            dialog.show();

        }
    }


    public String getIMEI(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public String getIMSI(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSubscriberId();
    }



    public String getSoftwareVersion(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceSoftwareVersion();
    }


    public String getSimSerialNumber(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSimSerialNumber();
    }


    public String getVoiceMailNumber(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getVoiceMailNumber();
    }


    public String getSimOperatorName(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSimOperatorName();
    }

    public String getLine1Number(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }


    public String getNetworkOperator(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getNetworkOperator();
    }


    public String getNetworkOperatorName(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getNetworkOperatorName();
    }



}
