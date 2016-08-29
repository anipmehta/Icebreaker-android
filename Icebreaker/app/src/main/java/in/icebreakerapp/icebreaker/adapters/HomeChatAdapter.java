package in.icebreakerapp.icebreaker.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.icebreakerapp.icebreaker.R;
import in.icebreakerapp.icebreaker.models.HomeChat;

/**
 * Created by anip on 28/08/16.
 */
public class HomeChatAdapter extends BaseAdapter {
    private LayoutInflater lf;

    ArrayList<HomeChat> mChats = new ArrayList<HomeChat>();
    public HomeChatAdapter(ArrayList arr, Context c){
        this.mChats = arr;
        lf = LayoutInflater.from(c);
    }
    class  ViewHolder {
        TextView title;
        TextView lastMessage;
    }
    @Override
    public int getCount() {
        return mChats.size();
    }

    @Override
    public Object getItem(int i) {
        return mChats.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        Log.i("hell","entered adapter");
        if(view == null){
            vh = new ViewHolder();
            view = lf.inflate(R.layout.item_home_chat,null);
            vh.title = (TextView) view.findViewById(R.id.title);
            vh.lastMessage = (TextView) view.findViewById(R.id.last);
            view.setTag(vh);
        }
        else{
            vh = (ViewHolder) view.getTag();
        }
        HomeChat item = mChats.get(i);
        vh.title.setText(item.getTitle());

        return view;
    }
}
