package pku.zy.iword.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pku.zy.iword.R;

public class WordListAdapter extends ArrayAdapter<String> {
    private int resourceId;
    public WordListAdapter(Context context, int textViewResourceId, List<String> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;

    }
    public View getView(int position, View convertView, ViewGroup parent){
        String str=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView wordInfo=(TextView) view.findViewById(R.id.tv_word);
        wordInfo.setText(str);
        return view;
    }
}
