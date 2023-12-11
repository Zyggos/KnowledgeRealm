package com.knowledgerealm.handlers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.knowledgerealm.R;
import com.knowledgerealm.models.User;

import java.util.List;

/**
 * Adapter class for displaying user data in a RecyclerView.
 */
public class UserAdapterHandler extends RecyclerView.Adapter<UserAdapterHandler.UserViewHolder> {
    private List<User> userList;

    /**
     * Constructor for UserAdapterHandler.
     *
     * @param userList the list of User objects to be displayed
     */
    public UserAdapterHandler(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for each user
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    /**
     * ViewHolder class for holding the user item views.
     */
    static class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView rankTextView;
        private TextView nameTextView;
        private TextView scoreTextView;

        /**
         * Constructor for UserViewHolder.
         *
         * @param itemView the inflated item view
         */
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            rankTextView = itemView.findViewById(R.id.rankTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
        }

        /**
         * Binds the user data to the item view.
         *
         * @param user the User object containing the user data
         */
        public void bind(User user) {
            // Set the rank, name, and score in the item view
            rankTextView.setText(String.valueOf(getAdapterPosition() + 4));
            nameTextView.setText(user.getUsername());
            scoreTextView.setText(String.valueOf(user.getScore()));
        }
    }
}
