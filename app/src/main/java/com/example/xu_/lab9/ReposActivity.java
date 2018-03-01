package com.example.xu_.lab9;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xu_.lab9.adapter.CardAdapter;
import com.example.xu_.lab9.adapter.ViewHolder;
import com.example.xu_.lab9.factory.ServiceFactory;
import com.example.xu_.lab9.model.Repos;
import com.example.xu_.lab9.service.GithubService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xu_ on 2017/12/25.
 */

public class ReposActivity extends AppCompatActivity {
    RecyclerView reposList;
    ProgressBar reposProgress;
    List<Map<String, Object>> list = new ArrayList<>();
    CardAdapter cardAdapter;
    private GithubService githubService;
    String username = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        reposList = (RecyclerView)findViewById(R.id.detail_recyclerview);
        reposProgress = (ProgressBar)findViewById(R.id.detail_progressBar);
        Retrofit GithubRetrofit = ServiceFactory.createRetrofit("https://api.github.com/");
        githubService = GithubRetrofit.create(GithubService.class);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) username = bundle.getString("name");

        reposList.setLayoutManager(new LinearLayoutManager(this));
        cardAdapter = new CardAdapter(this, R.layout.item_detail, list) {
            @Override
            public void convert(ViewHolder holder, Map<String, Object> data) {
                TextView name = holder.getView(R.id.item_title);
                name.setText(data.get("name").toString());
                TextView language = holder.getView(R.id.item_language);
                language.setText(data.get("language").toString());
                TextView description = holder.getView(R.id.item_brief);
                description.setText(data.get("description").toString());
            }
        };

        reposList.setAdapter(cardAdapter);
        cardAdapter.setOnItemClickListener(new CardAdapter.MyOnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(cardAdapter.getData(position, "url"));
                intent.setData(content_url);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position){

            }
        });

        githubService.getUserRepos(username)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repos>>() {
                    @Override
                    public void onCompleted() {
                        reposProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ReposActivity.this, e.getMessage()+"请确认您搜索的用户名存在", Toast.LENGTH_LONG).show();
                        reposProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(List<Repos> reposes) {
                        for(int i=0; i<reposes.size(); i++){
                            cardAdapter.add(reposes.get(i));
                        }
                    }
                });
    }
}
