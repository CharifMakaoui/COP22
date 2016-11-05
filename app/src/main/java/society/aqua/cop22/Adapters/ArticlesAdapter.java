package society.aqua.cop22.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import society.aqua.cop22.Modules.Entry;
import society.aqua.cop22.R;

import static com.google.ads.AdRequest.TEST_EMULATOR;

/**
 * Created by MrCharif on 27/10/2016.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Realm realm;
    private List<Entry> articleList;
    private Context context;
    private final String LOG_TAG = "Article_Adapter";

    class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView article_title;
        ImageView article_thumbnail;
        Button articleFullView;
        MaterialFavoriteButton favoriteButton;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            article_title = (TextView) itemView.findViewById(R.id.article_title);
            article_thumbnail = (ImageView) itemView.findViewById(R.id.article_thumbnail);
            articleFullView = (Button) itemView.findViewById(R.id.articleFullView);
            favoriteButton = (MaterialFavoriteButton) itemView.findViewById(R.id.favoritButton);
        }
    }

    class AdsHolder extends RecyclerView.ViewHolder{

        NativeExpressAdView adView;
        public AdsHolder(View itemView) {
            super(itemView);
            adView = (NativeExpressAdView) itemView.findViewById(R.id.adView);
        }
    }


    public ArticlesAdapter(List<Entry> articles, Context context, Realm realm) {
        this.articleList = articles;
        this.context = context;
        this.realm = realm;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 9988998){
            return new AdsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.native_ads, parent, false));
        }
        else{
            return new ArticleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_card, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        if(viewType == 9988998){
            AdsHolder adsHolder = (AdsHolder) holder;
            AdRequest request = new AdRequest.Builder()
                    .addTestDevice(TEST_EMULATOR)
                    .build();
            adsHolder.adView.loadAd(request);
        }
        else{
            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
            final Entry article = this.articleList.get(position);
            RealmResults<Entry> result = realm.where(Entry.class)
                    .equalTo("id", article.id)
                    .findAll();


            articleViewHolder.article_title.setText(article.title);
            // loading album cover using Glide library
            Glide.with(context).load(article.imgsrc).into(articleViewHolder.article_thumbnail);

            articleViewHolder.articleFullView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG,"Click on Show Article position "+ position);
                }
            });

            articleViewHolder.favoriteButton.setFavorite(result.get(0).isFav, true);
            articleViewHolder.favoriteButton.setOnFavoriteChangeListener(
                    new MaterialFavoriteButton.OnFavoriteChangeListener() {
                        @Override
                        public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                            article.isFav = favorite;
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(article);
                            realm.commitTransaction();
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return this.articleList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if((position % 10) == 0){
            return 9988998;
        }
        else{
            return super.getItemViewType(position);
        }
    }

    public void update(List<Entry> data) {
        this.articleList = data;
        notifyDataSetChanged();
    }
}
