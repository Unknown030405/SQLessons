package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.valera.R;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;

import domain.Friend;
import domain.User;
import interactor.UserInteractor;
import repository.DatabaseRepository;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private ArrayList<Friend> friends;
    private Context context;
    private User user;

    public Adapter(ArrayList<Friend> friends, Context context, User user) {
        this.friends = friends;
        this.context = context;
        this.user = user;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder vh;
        vh = new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_layout, null), context, user, this);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        holder.bind(friends.get(position));
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private User user;
        private TextView name;
        private Button button;
        private Context context;
        private UserInteractor UI;
        private Adapter adapter;

        public ViewHolder(@NonNull View itemView, Context context, User user, Adapter adapter) {
            super(itemView);
            name = itemView.findViewById(R.id.users_name);
            button = itemView.findViewById(R.id.add_to_friends_button);
            this.context = context;
            this.user = user;
            this.adapter = adapter;
            UI = new UserInteractor(context);
        }

        public void bind(final Friend friend){
            name.setText(friend.name);
            switch(friend.type){
                case 0:
                    button.setText("Waiting...");
                    button.setBackgroundColor(context.getResources().getColor(R.color.colorYellow));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "Already sent a request", Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "Waiting for your friend's accept", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case 1:
                    button.setText("Delete");
                    button.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UI.deleteFromFriends(user.id, friend.id);
                            Toast.makeText(context, "Deleted from friends", Toast.LENGTH_SHORT).show();
                            adapter.setFriends(UI.GetFriends(user));
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2:
                    button.setText("send request");
                    button.setBackgroundColor(context.getResources().getColor(R.color.colorGrey));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UI.sendRequest(user.id, friend.id);
                            Toast.makeText(context, "Request sent", Toast.LENGTH_SHORT).show();
                            adapter.setFriends(UI.GetFriends(user));
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 3:
                    button.setText("add to friends");
                    button.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UI.acceptRequest(user.id, friend.id);
                            Toast.makeText(context, friend.name + " is now your friend", Toast.LENGTH_SHORT).show();
                            adapter.setFriends(UI.GetFriends(user));
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
            }
        }
    }
}
