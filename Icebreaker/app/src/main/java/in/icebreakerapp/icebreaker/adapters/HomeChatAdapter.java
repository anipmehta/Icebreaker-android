package in.icebreakerapp.icebreaker.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.icebreakerapp.icebreaker.R;
import in.icebreakerapp.icebreaker.helpers.MessageDb;
import in.icebreakerapp.icebreaker.models.HomeChat;
import in.icebreakerapp.icebreaker.models.IcebreakerNotification;
import in.icebreakerapp.icebreaker.util.CircleTransform;

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
    private ImageView profile;
    private TextView time;
    private TextView count;
    public ViewHolder(View v) {
        super(v);
        mTextView = (TextView) v.findViewById(R.id.enroll);
        message = (TextView) v.findViewById(R.id.last);
        profile = (ImageView) v.findViewById(R.id.profile);
        time = (TextView) v.findViewById(R.id.time);
        count = (TextView) v.findViewById(R.id.count);

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
        int count=0;
        IcebreakerNotification message = db.lastMessage(chats.get(position).getChat_id());


            holder.mTextView.setText(chats.get(position).getEnroll());
        if (message != null) {
            holder.message.setText(message.getMessage());
            if (compareDate(message.getTime()))
                holder.time.setText(convertDate(message.getTime(), "hh:mm a"));
            else
                holder.time.setText(convertDate(message.getTime(), "dd/MM/yy "));
            if ((count = db.unreadTitle(chats.get(position).getChat_id())) >= 1) {
                holder.count.setVisibility(View.VISIBLE);
                holder.count.setText(String.valueOf(count));
            } else
                holder.count.setVisibility(View.GONE);
        }
        else {
            holder.count.setVisibility(View.GONE);
            holder.time.setVisibility(View.GONE);
            holder.message.setVisibility(View.GONE);
        }

        Log.i("hell_url","http://anip.xyz:8080/image/"+chats.get(position).getEnroll()+"/");
        Picasso.with(context)
                .load("http://anip.xyz:8080/image/"+chats.get(position).getEnroll()+"/")
//                .resize(50, 50)
//                .centerCrop()
                .fit()
                .centerCrop()
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .transform(new CircleTransform())
                .placeholder(R.drawable.icebreaker)
                .error(R.drawable.icebreaker)
                .into(holder.profile);

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return chats.size();
    }
    public String convertDate(long dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, dateInMilliseconds).toString();
    }
    public boolean compareDate(long date){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date).equals(fmt.format(System.currentTimeMillis()));
    }

}