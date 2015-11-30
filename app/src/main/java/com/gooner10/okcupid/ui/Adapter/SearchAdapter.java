package com.gooner10.okcupid.ui.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gooner10.okcupid.EventBus.OnItemClickEvent;
import com.gooner10.okcupid.Model.DataModel;
import com.gooner10.okcupid.R;
import com.gooner10.okcupid.ui.Activity.UserDetail;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolderData> {
    public LayoutInflater layoutInflater;
    public Context mContext;
    public ArrayList<DataModel> mCupidData;

    public SearchAdapter(Context mContext, ArrayList<DataModel> mCupidData) {
        layoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mCupidData = mCupidData;
    }

    @Override
    public ViewHolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.single_search_row, parent, false);
        ViewHolderData viewHolderData = new ViewHolderData(view);
        return viewHolderData;
    }

    @Override
    public void onBindViewHolder(final ViewHolderData holder, final int position) {
        Glide.with(mContext)
                .load(mCupidData.get(position).getPhoto_medium())
                .override(120, 120)
                .centerCrop()
                .crossFade(30)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.test_profile)
                .into(holder.mImageView);

        holder.mUserName.setText(mCupidData.get(position).getUser_name());

        String age = String.valueOf(mCupidData.get(position).getAge());
        String city = mCupidData.get(position).getLocation().getmCityName();
        String stateCode = mCupidData.get(position).getLocation().getmStateCode();
        holder.mLocation.setText(age + "-" + city + "," + stateCode);

        String match = Integer.parseInt(mCupidData.get(position).getMatch()) / 100 + "%";
        holder.mMatch.setText(match);

        holder.mMatchText.setText("Match");


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, UserDetail.class);

                DataModel mDataModel = mCupidData.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("cupid_detail", mDataModel);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context,
                                holder.mImageView, // Starting view
                                "profileImage"); // The Shared Transition

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    context.startActivity(intent, options.toBundle());
                } else {
                    context.startActivity(intent);
                }
                // Get and Post the event
                EventBus.getDefault().postSticky(new OnItemClickEvent(bundle));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCupidData.size();
    }

    public class ViewHolderData extends RecyclerView.ViewHolder {
        public final View mView;

        @Bind(R.id.profileImageView)
        ImageView mImageView;

        @Bind(R.id.LocationTextView)
        TextView mLocation;

        @Bind(R.id.MatchTextView)
        TextView mMatch;

        @Bind(R.id.UserNameTextView)
        TextView mUserName;

        @Bind(R.id.MatchText)
        TextView mMatchText;


        public ViewHolderData(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
