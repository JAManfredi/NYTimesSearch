package nytimessearch.jm.com.nytimessearch.adapters;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import nytimessearch.jm.com.nytimessearch.R;
import nytimessearch.jm.com.nytimessearch.models.Article;
import nytimessearch.jm.com.nytimessearch.models.Multimedia;

/**
 * Created by Jared12 on 3/18/17.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private List<Article> mArticles;
    private Context mContext;

    public ArticleAdapter(Context context, List<Article> articles) {
        mArticles = articles;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivArticleImage;
        public TextView tvArticleTitle;
        public TextView tvArticleSnippet;
        private Context context;

        public ViewHolder(Context context, View itemView) {
            super(itemView);

            this.ivArticleImage = (ImageView) itemView.findViewById(R.id.ivArticleImage);
            this.tvArticleTitle = (TextView) itemView.findViewById(R.id.tvArticleTitle);
            this.tvArticleSnippet = (TextView) itemView.findViewById(R.id.tvArticleSnippet);
            this.context = context;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Article article = mArticles.get(position);

                // Use custom chrome tabs to view article
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(this.context, R.color.colorPrimary));
                builder.setSecondaryToolbarColor(ContextCompat.getColor(this.context, R.color.colorAccent));
                builder.addDefaultShareMenuItem(); // Add share action

                Bitmap bitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ic_action_name);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, article.getWebUrl());

                int requestCode = 100;
                PendingIntent pendingIntent = PendingIntent.getActivity(this.context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this.context, Uri.parse(article.getWebUrl()));
            }
        }
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.ivArticleImage);
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View articleView = inflater.inflate(R.layout.item_article, parent, false);

        ViewHolder viewHolder = new ViewHolder(context, articleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder viewHolder, int position) {
        Article article = mArticles.get(position);

        String headline = (article.getHeadline().getMain() != null)
                ? article.getHeadline().getMain() : article.getHeadline().getPrintHeadline();
        viewHolder.tvArticleTitle.setText(headline);
        viewHolder.tvArticleSnippet.setText(article.getSnippet());
        viewHolder.ivArticleImage.setVisibility(View.VISIBLE);

        String imageUrl = null;
        for (Multimedia multimedia : article.getMultimedia()) {
            if (multimedia.getSubtype().equals("thumbnail")) {
                imageUrl = multimedia.getUrl();
            }
        }

        if (imageUrl != null) {
            Glide.with(getContext())
                    .load(imageUrl)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.ivArticleImage);
        } else {
            viewHolder.ivArticleImage.setVisibility(View.GONE);
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mArticles.size();
    }
}
