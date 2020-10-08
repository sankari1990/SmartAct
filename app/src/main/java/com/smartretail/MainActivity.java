package com.smartretail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smartretail.printer.PDFWriter;
import com.smartretail.printer.PaperSize;
import com.smartretail.printer.PdfDocumentAdapter;
import com.smartretail.printer.StandardFonts;
import com.smartretail.printer.Transformation;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Logger;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MainActivity extends AppCompatActivity {
Button masters,transactions,reports,tools,exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        masters = findViewById(R.id.btn_master);
        transactions = findViewById(R.id.btn_transaction);
        reports = findViewById(R.id.btn_reports);
        tools = findViewById(R.id.btn_tools);
        exit = findViewById(R.id.btn_exit);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }



    }

    public void onMasterClick(View v){
        Intent masterActivity = new Intent(MainActivity.this,MasterActivity.class);
        startActivity(masterActivity);
    }

    public void onTransactionClick(View v){
        Intent masterActivity = new Intent(MainActivity.this,TransactionActivity.class);
        startActivity(masterActivity);
    }

    public void onReportClick(View v){
        Intent masterActivity = new Intent(MainActivity.this,ReportActivity.class);
        startActivity(masterActivity);
    }

    public void onExitClick(View v){
        //finish();
        File myFile = new File("");
        try {
            File root = Environment.getExternalStorageDirectory();
            myFile = new File(root +"/textfile.pdf");
            if(myFile.exists()){
                myFile.delete();
            }
            myFile.createNewFile();

            System.out.println(myFile.getAbsolutePath());

            String pdfcontent = generateHelloWorldPDF();
            outputToFile(myFile, pdfcontent, "ISO-8859-1");

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();

        }


        PrintManager printManager=(PrintManager) getSystemService(Context.PRINT_SERVICE);
        try
        {
            PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(getApplicationContext(),myFile.getAbsolutePath() );
            //PrintDocumentAdapter printAdapter = new
            printManager.print("Document", printAdapter,new PrintAttributes.Builder().build());

        }

    catch (Exception e)
    {
e.printStackTrace();
    }
    }

    private String generateHelloWorldPDF() {
        PDFWriter mPDFWriter = new PDFWriter(PaperSize.FOLIO_WIDTH, PaperSize.FOLIO_HEIGHT);

        AssetManager mngr = getAssets();


        //mPDFWriter.newPage();
        mPDFWriter.addRawContent("[] 0 d\n");
        mPDFWriter.addRawContent("1 w\n");
        mPDFWriter.addRawContent("0 0 1 RG\n");
        mPDFWriter.addRawContent("0 1 0 rg\n");
        mPDFWriter.addRectangle(40, 50, 280, 50);
        mPDFWriter.addText(85, 75, 18, "Sample Message");

        int pageCount = mPDFWriter.getPageCount();
        for (int i = 0; i < pageCount; i++) {
            mPDFWriter.setCurrentPage(i);
            mPDFWriter.addText(10, 10, 8, Integer.toString(i + 1) + " / " + Integer.toString(pageCount));
        }

        String s = mPDFWriter.asString();
        return s;
    }


    private void outputToFile(File newFile, String pdfContent, String encoding) {

        Log.i("PDF", "Writing file to " + newFile);

        try {
            newFile.createNewFile();
            try {
                FileOutputStream pdfFile = new FileOutputStream(newFile);
                pdfFile.write(pdfContent.getBytes(encoding));
                pdfFile.close();
            } catch (FileNotFoundException e) {
                Log.w("PDF", e);
            }
        } catch (IOException e) {
            Log.w("PDF", e);
        }
    }
}
