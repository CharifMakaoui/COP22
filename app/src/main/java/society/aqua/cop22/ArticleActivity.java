package society.aqua.cop22;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import io.realm.Realm;
import io.realm.RealmResults;
import society.aqua.cop22.Modules.Entry;
import society.aqua.cop22.Utils.KEYS;

public class ArticleActivity extends AppCompatActivity {

    private Realm mRealm;
    String ARTICLE_ID;

    WebView article_content;

    private void init(){
        Realm.init(this);
        mRealm = Realm.getDefaultInstance();

        article_content = (WebView) findViewById(R.id.article_content);

        final Entry article = mRealm.where(Entry.class)
                .equalTo("id", ARTICLE_ID)
                .findFirst();

        String Style = "<style> img{ width: 100%; } div{ direction: rtl; } </style>";

        article_content.getSettings().setJavaScriptEnabled(true);
        article_content.loadDataWithBaseURL("", article.content +" "+ Style, "text/html", "UTF-8", "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        ARTICLE_ID = getIntent().getStringExtra(KEYS.ARTICLE_ID);

        init();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
