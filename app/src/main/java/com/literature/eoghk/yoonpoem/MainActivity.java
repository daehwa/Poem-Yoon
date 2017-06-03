package com.literature.eoghk.yoonpoem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    static String[] POEM_LIST = null;
    EditText title;
    EditText writer;
    EditText content ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (EditText)findViewById(R.id.mytitle);
        writer = (EditText)findViewById(R.id.mywriter);
        content = (EditText)findViewById(R.id.mypoem);
        getSupportActionBar().hide();

        File files = new File(getFilesDir().getAbsolutePath());
        String[] strTemp=new String[files.listFiles().length];
        if(files.listFiles().length>0){
            int i =0;
            int bad=0;
            for(File data : files.listFiles()){
                strTemp[i] = data.getName();
                if (strTemp[i].toLowerCase().endsWith(".xsd")||strTemp[i].toLowerCase().endsWith(".txt")) bad++;
                else i++;
            }
            if (i==0) {
                POEM_LIST = new String[1];
                POEM_LIST[0]="아직 시가 없습니다 *' '*";
            }
            else{
                POEM_LIST=new String[files.listFiles().length-bad];
                for (int j=0 ; j < POEM_LIST.length; j++){
                    POEM_LIST[j]=strTemp[j];
                }
            }
        }else{
            POEM_LIST=new String [1];
            POEM_LIST[0]="아직 시가 없습니다 *' '*";
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, POEM_LIST) ;

        ListView listview = (ListView) findViewById(R.id.poemlist) ;
        listview.setAdapter(adapter) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String strText = (String) parent.getItemAtPosition(position) ;
                String poemText="";
                try {
                    InputStream is = openFileInput(strText);
                    BufferedReader bIn=new BufferedReader(new InputStreamReader(is));

                    int current =0;
                    String strTemp="";
                    while((strTemp=bIn.readLine())!=null) {
                        if(current==0) {
                            title.setText(strTemp);
                            current++;
                        }
                        else if(current==1){
                            writer.setText(strTemp);
                            current++;
                        }
                        else    poemText += strTemp + "\r\n";
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                content.setText(poemText);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View arg1, final int position, long id) {
                String strTemp=(String) parent.getItemAtPosition(position) ;
                if (strTemp.equals("아직 시가 없습니다 *' '*")) return false;
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("\'"+strTemp+"\'"+"을(를) 삭제 하시겠습니까?")

                        .setPositiveButton("YES", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                File dir = new File(getFilesDir().getAbsolutePath());
                                if (dir.isDirectory())
                                {
                                    String[] children = dir.list();
                                    new File(dir, children[position]).delete();
                                }
                                Toast.makeText(getApplicationContext(),"삭제되었습니다",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .show();

                return false;
            }
        });



    }
    public void savebtn(View v){
        if (title.getText().toString().equals("")){
            Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (writer.getText().toString().equals("")){
            Toast.makeText(this, "작가을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (content.getText().toString().equals("")){
            Toast.makeText(this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        OutputStream os= null;
        try {
            os = openFileOutput(title.getText().toString(), Context.MODE_PRIVATE);
            BufferedWriter bOs=new BufferedWriter(new OutputStreamWriter(os));
            bOs.write(title.getText().toString()+"\n");
            bOs.write(writer.getText().toString()+"\n");
            bOs.write(content.getText().toString());
            bOs.close();

            Toast.makeText(this, "'"+title.getText().toString()+"' "+"저장 완료", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();

            title.setText("");
            writer.setText("");
            content.setText("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
