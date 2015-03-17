package kr.hyosang.android.logviewer;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView mLogView;
    
    private Button mBtnExecute;
    private Button mBtnClear;
    private EditText mArgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mLogView = (TextView) findViewById(R.id.tv_log);
        
        mBtnExecute = (Button) findViewById(R.id.btn_execute);
        mBtnClear = (Button) findViewById(R.id.btn_clear);
        mArgs = (EditText) findViewById(R.id.et_arguments);
        
        mBtnExecute.setOnClickListener(mBtnClicked);
        mBtnClear.setOnClickListener(mBtnClicked);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    private String executeLogcat(String args) {
        try {
            Process p = Runtime.getRuntime().exec("logcat " + args);
            
            byte [] buf = new byte[512];
            InputStream is = p.getInputStream();
            StringBuffer sb = new StringBuffer();
            int read;
            
            while((read = is.read(buf)) > 0) {
                sb.append(new String(buf, 0, read));
            }
            
            return sb.toString();
        }catch(IOException e) {
            return Log.getStackTraceString(e);
        }
    }
    
    private OnClickListener mBtnClicked = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btn_execute) {
                //키보드 숨김
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                
                String args = mArgs.getText().toString();
                args = "-d " + args;
                String log = executeLogcat(args);
                mLogView.setText(log);
            }else if(v.getId() == R.id.btn_clear) {
                executeLogcat("-c");
                mLogView.setText("");
            }
        }
    };

}
