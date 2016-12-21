package com.example.paper.paperinteractive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfActivity extends AppCompatActivity {

    private GridView gridView;
    private File dir;
    private File[] pdfs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new CustomGridViewAdapter(this));

        //TODO Göra en LongPress funktion där man får välja flera PDFer att skicka via Mail
        //TODO Ändra vanlig Press att öppna app som kan läsa PDF

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("vnd.android.cursor.dir/email");
                String[] to = {"asd@gmail.com"};
                Uri fileUri = Uri.fromFile(pdfs[position]);
                sendIntent.putExtra(Intent.EXTRA_EMAIL, to);
                sendIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Sissss");
                startActivity(Intent.createChooser(sendIntent, "Send email..."));
            }
        });
    }

    private class CustomGridViewAdapter extends BaseAdapter {

        private Context mContext;

        CustomGridViewAdapter(Context context) {
            mContext = context;

            dir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString());
            pdfs = dir.listFiles();
        }

        @Override
        public int getCount() {
            if (pdfs != null)
                return pdfs.length;
            else
                return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = convertView;
            ViewHolder vh;

            if (row == null){
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                row = inflater.inflate(R.layout.pdf_gridview, parent, false);
                vh = new ViewHolder(row);
                vh.imageView = (ImageView) row.findViewById(R.id.imagePDF);
                vh.mTextView = (TextView) row.findViewById(R.id.textPDF);
                row.setTag(vh);
            } else {
                vh = (ViewHolder) row.getTag();
            }

            vh.mTextView.setText(pdfs[position].getName());


            return row;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

         TextView mTextView;
         ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
