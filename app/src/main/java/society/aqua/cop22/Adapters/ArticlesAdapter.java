package society.aqua.cop22.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import society.aqua.cop22.Modules.Entry;
import society.aqua.cop22.R;

/**
 * Created by MrCharif on 27/10/2016.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {

    private Realm realm;
    private List<Entry> articleList;
    private Context context;
    private final String LOG_TAG = "Article_Adapter";

    class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView article_title;
        ImageView article_thumbnail;
        MaterialFavoriteButton favoriteButton;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            article_title = (TextView) itemView.findViewById(R.id.article_title);
            article_thumbnail = (ImageView) itemView.findViewById(R.id.article_thumbnail);
            favoriteButton = (MaterialFavoriteButton) itemView.findViewById(R.id.favoritButton);
        }
    }


    public ArticlesAdapter(List<Entry> articles, Context context, Realm realm) {
        this.articleList = articles;
        this.context = context;
        this.realm = realm;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_card, parent, false);
        ArticleViewHolder viewHolder = new ArticleViewHolder(row);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, final int position) {
        final Entry article = this.articleList.get(position);
        RealmResults<Entry> result = realm.where(Entry.class)
                .equalTo("id", article.id)
                .findAll();

        Log.d(LOG_TAG, "Binding article From Realm: " + result.get(0).id);

        holder.article_title.setText(article.title);
        // loading album cover using Glide library
        Glide.with(context).load(article.imgsrc).into(holder.article_thumbnail);

        holder.favoriteButton.setFavorite(result.get(0).isFav, true);

        holder.favoriteButton.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        article.isFav = favorite;
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(article);
                        realm.commitTransaction();
                        Log.d(LOG_TAG, "Favorite Article : "+ position + " IsFav : "+ favorite);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return this.articleList.size();
    }

    public void update(List<Entry> data) {
        this.articleList = data;
        notifyDataSetChanged();
    }
}
