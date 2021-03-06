package com.kyluandkylu.android.logiword.FriendList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kyluandkylu.android.logiword.R;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder> {

    private String[] friends;
    private OnListItemClickListener onClickListener;

    public FriendAdapter(String[] friends, OnListItemClickListener onClickListener) {
        this.friends = friends;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public FriendAdapter.FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.friend_item, parent, false);
        return new FriendHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {
        holder.textViewName.setText(friends[position]);
    }

    @Override
    public int getItemCount() {
        return friends.length;
    }


    protected class FriendHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewName;
        ImageButton imageButtonRemoveFriend;

        public FriendHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.friend_view_name);
            imageButtonRemoveFriend = itemView.findViewById(R.id.imageButtonRemoveFriend);
            imageButtonRemoveFriend.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onListItemClick(getAdapterPosition());
        }
    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
