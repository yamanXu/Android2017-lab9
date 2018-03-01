package com.example.xu_.lab9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xu_.lab9.adapter.CardAdapter;
import com.example.xu_.lab9.adapter.ViewHolder;
import com.example.xu_.lab9.factory.ServiceFactory;
import com.example.xu_.lab9.model.Github;
import com.example.xu_.lab9.service.GithubService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    EditText ET_search;
    Button BT_clear;
    Button BT_fetch;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    CardAdapter cardAdapter;
    GithubService githubService;
    List<Map<String, Object>> GithubList = new ArrayList<>();
    //SharedPreferences sharedPreferences;
    int usercnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
//        if(sharedPreferences.getString("usercnt", "").equals("")){
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("usercnt", Integer.toString(0));
//            editor.commit();
//        }
//        else{
//            usercnt = Integer.parseInt(sharedPreferences.getString("usercnt", "0"));
//        }

        ET_search = (EditText)findViewById(R.id.search);
        BT_clear = (Button)findViewById(R.id.clear);
        BT_fetch = (Button)findViewById(R.id.fetch);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        progressBar = (ProgressBar)findViewById(R.id.main_progressBar);
        Retrofit GithubRetrofit = ServiceFactory.createRetrofit("https://api.github.com/");
        githubService = GithubRetrofit.create(GithubService.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardAdapter = new CardAdapter(this, R.layout.item_main, GithubList){
            @Override
            public void convert(ViewHolder holder, Map<String, Object> data) {
                TextView name = holder.getView(R.id.username);
                name.setText(data.get("name").toString());
                TextView id = holder.getView(R.id.id);
                id.setText(data.get("id").toString());
                TextView blog = holder.getView(R.id.blog);
                blog.setText(data.get("blog").toString());
            }
        };
        recyclerView.setAdapter(cardAdapter);
        cardAdapter.setOnItemClickListener(new CardAdapter.MyOnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, ReposActivity.class);
                intent.putExtra("name", cardAdapter.getData(position,"name"));
                startActivity(intent);
            }
            @Override
            public void onLongClick(int position) {
                cardAdapter.delete(position);
            }
        });

       // init();
        setClick();
    }

    public void init(){
        for(int i=0; i<usercnt; i++){
            String user = ET_search.getText().toString();
        //    String user = sharedPreferences.getString(Integer.toString(i), "");
            progressBar.setVisibility(View.VISIBLE);
            githubService.getUser(user)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Github>() {
                        @Override
                        public void onCompleted() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(MainActivity.this, e.getMessage()+"请确认您搜索的用户名存在", Toast.LENGTH_SHORT).show();
                            progressBar.setProgress(View.GONE);
                        }

                        @Override
                        public void onNext(Github github) {
                            cardAdapter.add(github);
                        }
                    });
        }
    }
    public void setClick(){
        BT_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardAdapter.clear();
                ET_search.setText("");
          //      SharedPreferences.Editor editor = sharedPreferences.edit();
                usercnt = 0;
          //      editor.clear();
          //      editor.commit();
            }
        });
        BT_fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = ET_search.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                githubService.getUser(user)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Github>() {
                            @Override
                            public void onCompleted() {
                                progressBar.setVisibility(View.GONE);
                            }
                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(MainActivity.this, e.getMessage()+"请确认您搜索的用户名存在", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                            @Override
                            public void onNext(Github github) {
                                cardAdapter.add(github);
                             //   SharedPreferences.Editor editor = sharedPreferences.edit();
                             //   editor.putString(Integer.toString(usercnt), github.getLogin());
                                usercnt++;
                            //    editor.putString("usercnt", Integer.toString(usercnt));
                             //   editor.commit();
                            }
                        });
            }
        });
    }
}
