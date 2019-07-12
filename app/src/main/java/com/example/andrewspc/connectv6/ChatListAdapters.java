package com.example.andrewspc.connectv6;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewspc.connectv6.Chat.ChatActivity;
import com.example.andrewspc.connectv6.Chat.ChatObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapters extends RecyclerView.Adapter<ChatListAdapters.MyViewHolder> {

    ArrayList<ChatObject> ChatListHelp;
    Context context;

    public ChatListAdapters(Context c, ArrayList<ChatObject> p)
    {
        context = c;
        ChatListHelp = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int position) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.title.setText(ChatListHelp.get(position).getUsername());
        Picasso.get().load(ChatListHelp.get(position).getImageOfUser()).fit().into(holder.imgProfilePic);
        holder.specificChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                /// HERE RETRIEVE THE KEY THAT CREATED THE CHAT ////

                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("chatIDD", ChatListHelp.get(holder.getAdapterPosition()).getUserUniqueID());
                bundle.putString("chatImageOfUser", ChatListHelp.get(holder.getAdapterPosition()).getImageOfUser());
                bundle.putString("chatUsername", ChatListHelp.get(holder.getAdapterPosition()).getUsername());
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);

                /*
                String keyChatId = ChatListHelp.get(position).getUserUniqueID();

                Toast.makeText(view.getContext(), keyChatId, Toast.LENGTH_SHORT).show();


                /*final Intent intent = new Intent(context, ChatActivity.class);
                context.startActivity(intent);
                */

            }
        });

    }


    @Override
    public int getItemCount() {
        return ChatListHelp.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private CardView specificChat;
        private TextView title;
        private CircleImageView imgProfilePic;


        public MyViewHolder(View itemView){
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            specificChat = (CardView) itemView.findViewById(R.id.ChatsOfUser);
            imgProfilePic = (CircleImageView) itemView.findViewById(R.id.chatProfilePic);

        }

    }

}
