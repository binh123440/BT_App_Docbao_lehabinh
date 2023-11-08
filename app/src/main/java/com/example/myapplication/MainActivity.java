package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<String> arrayTitle, arrayLink;
    ArrayAdapter adapterTitle,adapterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.lvItem);
        arrayTitle = new ArrayList<>();
        arrayLink = new ArrayList<>();
        adapterTitle = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayTitle);
        lv.setAdapter(adapterTitle);

        //Đặt đường dẫn nhớ phải xin quyền
        new ReadRss().execute("https://vnexpress.net/rss/cuoi.rss");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, NewsItemActivity.class);
                intent.putExtra("link",arrayLink.get(i));
                startActivity(intent);
            }
        });

    }

    private class ReadRss extends AsyncTask<String ,Void , String> {
        @Override
        protected String doInBackground(String... strings) {

            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStreamReader input = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader buffer = new BufferedReader(input);

                String line = "";
                //Kiểm tra còn dọc được dòng là != null
                while ( (line = buffer.readLine()) != null ){
                    content.append(line);
                }

                buffer.close();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }

            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            //Danh sách chứa từng item: nodeList
            NodeList nodeList = document.getElementsByTagName("item");

            String title = "";
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                title = parser.getValue(element , "title");
                arrayTitle.add(title);
                arrayLink.add(parser.getValue(element, "link"));
            }
            //Cập nhật lại dữ liệu thay đổi
            adapterTitle.notifyDataSetChanged();

        }
    }
}