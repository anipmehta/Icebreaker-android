package in.icebreakerapp.icebreaker.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.icebreakerapp.icebreaker.R;
import in.icebreakerapp.icebreaker.helpers.MessageDb;
import in.icebreakerapp.icebreaker.models.HomeChat;

/**
 * Created by anip on 28/08/16.
 */
public class HomeChatAdapter extends RecyclerView.Adapter<HomeChatAdapter.ViewHolder> {
private String[] mDataset;
private ArrayList<HomeChat> chats;
private MessageDb db;
private Activity context;

public HomeChatAdapter(FragmentActivity activity, ArrayList<HomeChat> chats, Activity context) {
        this.chats = chats;
        this.context = context;
        db = new MessageDb(this.context);

        }

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public static class ViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public TextView mTextView;
    public TextView message;
    public ViewHolder(View v) {
        super(v);
        mTextView = (TextView) v.findViewById(R.id.enroll);
        message = (TextView) v.findViewById(R.id.last);
//            mTextView = v;

    }
}
    // Provide a suitable constructor (depends on the kind of dataset)
    public HomeChatAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HomeChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_chat, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(chats.get(position).getEnroll());
        holder.message.setText(db.lastMessage(chats.get(position).getChat_id()));

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return chats.size();
    }
}