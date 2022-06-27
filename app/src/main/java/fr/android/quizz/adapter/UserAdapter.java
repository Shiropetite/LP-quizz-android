package fr.android.quizz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import fr.android.quizz.R;
import fr.android.quizz.model.User;

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(@NonNull Context context, @NonNull ArrayList<User> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }

        // Lookup view for data population
        TextView username = (TextView) convertView.findViewById(R.id.user_username);
        TextView score = (TextView) convertView.findViewById(R.id.user_score);
        ImageView avatar = convertView.findViewById(R.id.quizz_image);

        // Populate the data into the template view using the data object
        username.setText(user.getUsername());
        score.setText(user.getScore() + " points");
        Glide.with(convertView).load(user.getAvatar()).into(avatar);

        // Return the completed view to render on screen
        return convertView;
    }

}
