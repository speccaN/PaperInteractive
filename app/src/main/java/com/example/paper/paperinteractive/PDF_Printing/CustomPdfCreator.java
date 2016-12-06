package com.example.paper.paperinteractive.PDF_Printing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomPdfCreator {

    public static void CreatePDF(Context context, String name, String[] exercises) {
        /*                CustomPdfPrinter pdfPrinter = new CustomPdfPrinter(this);
                pdfPrinter.Start();*/

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        //PDF Creation
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        // Draw the title
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        canvas.drawText(name,
                54,
                72,
                paint);

        // Drawing the Date under Title
        Rect bounds = new Rect();
        paint.getTextBounds(name, 0, name.length(), bounds);
        paint.setTextSize(14);
        canvas.drawText(date, 54, bounds.height()*3 - 25, paint);

        // Draw subtitle
        paint.setTextSize(30);
        paint.setFlags(Paint.UNDERLINE_TEXT_FLAG);
        canvas.drawText("Övningar:",
                54,
                72 + bounds.height() + 70,
                paint);

        // Draw all the Exercises
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(14);
        textPaint.setColor(Color.BLACK);
        String allExercises = "";
        for (int i = 0; i < exercises.length; i++) {
            allExercises += (exercises[i] + "\n");
        }
        StaticLayout staticLayout = new StaticLayout(allExercises, textPaint, canvas.getWidth() / 4,
                Layout.Alignment.ALIGN_NORMAL, 1, 0.25f, false);
        canvas.save();
        canvas.translate(54, 72 + bounds.height() + 80);
        staticLayout.draw(canvas);
        canvas.restore();

        document.finishPage(page);

        //TODO Inte ersätta PDF med samma namn
        FileOutputStream outputStream;
        File pdf;
        File dir;
        if (isExternalStorageWritable()) {
            try {
                name = Normalizer.normalize(name,
                        Normalizer.Form.NFKD).replaceAll("\\p{M}", "") + " " + date +".pdf";
                dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString());
                if(!dir.exists()){
                    dir.mkdirs();
                }
                pdf = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), name);
                outputStream = new FileOutputStream(pdf);
                document.writeTo(outputStream);
                document.close();
            } catch (IOException e) {
                Log.e("Error: ", e.toString());
            }
        }
    }

    /* Checks if external storage is available for read and write */
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}
