package society.aqua.cop22;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import society.aqua.cop22.Modules.Feed;
import society.aqua.cop22.Rests.ApiClient;
import society.aqua.cop22.Services.Articles;

public class MainActivity extends AppCompatActivity {

    private String LOG_TAG = "Home_Page";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Articles articles = ApiClient.getClient().create(Articles.class);
        Call<Feed> feed = articles.getArticles();

        feed.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                Log.d(LOG_TAG,"Get data in home page");
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {

            }
        });
    }
}
