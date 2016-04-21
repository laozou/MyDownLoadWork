package test8.mydownloadwork;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mEditText;
    private ProgressBar mProgressBar;
    private TextView mTextView;
    public static String mURL=null;
    public static final String DOWNLOAD="download";
    public static final String DOWNLOADPROGRESS="下载进度：";
    public static final String PERCENT="%";
    public static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.button);
        mEditText = (EditText) findViewById(R.id.edittext);
        mProgressBar=(ProgressBar)findViewById(R.id.downloadprograssbar);
        mTextView=(TextView)findViewById(R.id.textview);


        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mEditText.getText()!=null){
            mURL=mEditText.getText().toString();
            new DownLoadTask().execute(mURL);
        }else {
            Toast.makeText(MainActivity.this,"请输入下载地址",Toast.LENGTH_SHORT).show();
        }
    }

    class DownLoadTask extends AsyncTask<String,Integer,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(DOWNLOADPROGRESS+0+PERCENT);
        }

        @Override
        protected Boolean doInBackground(String... params) {


            try {
                URL url = new URL(params[0]);

                URLConnection urlConnection = url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();

                int contentLength = urlConnection.getContentLength(); // 要下载的文件的大小
                String downloadFoldersName = Environment.getExternalStorageDirectory() + File.separator + DOWNLOAD + File.separator;
                File file = new File(downloadFoldersName);
                if (!file.exists()) {
                    file.mkdir();
                }

                String fileName = downloadFoldersName + "test.apk";
                File apkFile = new File(fileName);
                if (apkFile.exists()) {
                    apkFile.delete();
                }

                int downloadSize = 0;

                byte[] bytes = new byte[1024];
                int length;

                OutputStream outputStream = new FileOutputStream(fileName);

                while ((length = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, length);
                    downloadSize += length;
                    int progress = downloadSize * 100 / contentLength;
                    // update UI
                    publishProgress(progress);

                }

                inputStream.close();
                outputStream.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBar.setProgress(values[0]);
            mTextView.setText(DOWNLOADPROGRESS+values[0]+PERCENT);
            if(values[0] == 100){
                Toast.makeText(MainActivity.this, "download success", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
                mTextView.setVisibility(View.INVISIBLE);
            }
        }
    }

}
