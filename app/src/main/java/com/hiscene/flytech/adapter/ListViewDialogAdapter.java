package com.hiscene.flytech.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hiscene.flytech.R;

import java.util.List;

/**
 * 显示数组
 */
public class ListViewDialogAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mItem ;

    public ListViewDialogAdapter( Context context, List<String> mItem) {
        this.mContext = context;
        this.mItem = mItem;
    }

    @Override
    public int getCount() {
        return mItem.size();
    }

    @Override
    public Object getItem( int position) {
        return mItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        final HoldClass hold;
        if (convertView == null) {
            hold = new HoldClass();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_dialog, null);
            hold.text1= (TextView) convertView.findViewById(R.id.text1);

            convertView.setTag(hold);
        } else {
            hold = (HoldClass) convertView.getTag();
        }
        hold.text1.setText(mItem.get(position));
        return convertView;
    }

    static class HoldClass {
        private TextView text1;
    }
}