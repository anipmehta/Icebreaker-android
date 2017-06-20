package in.icebreakerapp.icebreaker.adapters;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.icebreakerapp.icebreaker.R;
import in.icebreakerapp.icebreaker.models.Contact;
import in.icebreakerapp.icebreaker.models.HomeChat;
import in.icebreakerapp.icebreaker.util.CircleTransform;

/**
 * Created by anip on 05/09/16.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private String[] mDataset;
    private ArrayList<Contact> contacts;
    private Activity context;


    public ContactsAdapter(FragmentActivity activity, ArrayList<Contact> contacts, Activity context) {
        this.context =context;
        this.contacts = contacts;

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView enroll;
        public ImageView profile;
        public TextView status;
        public ViewHolder(View v) {
            super(v);
            enroll = (TextView) v.findViewById(R.id.enroll);
            profile = (ImageView) v.findViewById(R.id.profile);
            status = (TextView) v.findViewById(R.id.status);
//            mTextView = v;

        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactsAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(contacts.get(position).getNick_name()!=null) {
            Log.i("hell_name",contacts.get(position).getNick_name());
            holder.enroll.setText(contacts.get(position).getNick_name());
        }
        else
            holder.enroll.setText(contacts.get(position).getEnroll());
        holder.status.setText(contacts.get(position).getStatus());
        Log.i("hell_url","http://anip.xyz:8080/image/"+contacts.get(position).getEnroll()+"/");
        Picasso.with(context)
                .load("http://anip.xyz:8080/image/"+contacts.get(position).getEnroll()+"/")
                .resize(200, 200)
//                .centerCrop()
//                .fit()
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
        return contacts.size();
    }
}