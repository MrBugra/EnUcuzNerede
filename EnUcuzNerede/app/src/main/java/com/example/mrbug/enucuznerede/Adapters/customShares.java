package com.example.mrbug.enucuznerede.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mrbug.enucuznerede.Classes.AppShares;
import com.example.mrbug.enucuznerede.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrBug on 18.10.2017.
 */

public class customShares extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<AppShares> listShaa;

    public customShares(Activity activity, List<AppShares> appss) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        listShaa = appss;
    }

    @Override
    public int getCount() {
        return listShaa.size();
    }

    @Override
    public AppShares getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return listShaa.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View satirView;

        satirView = mInflater.inflate(R.layout.acc_listview_pattern, null);
        TextView textView =
                (TextView) satirView.findViewById(R.id.titleTxT);
        ImageView imageView =
                (ImageView) satirView.findViewById(R.id.imageIcon);
        TextView aciklama= (TextView) satirView.findViewById(R.id.Despriction);
        AppShares apppsss = listShaa.get(position);


        TextView adress=(TextView) satirView.findViewById(R.id.bos);
        adress.setText(apppsss.getLongAdress());



        textView.setText(apppsss.getTitle());
        aciklama.setText("ilk fiyat ="+apppsss.getPrice()+"  iken indirimli fiyatı"+apppsss.getDiscounted());

            imageView.setImageResource( whichPng(apppsss.getId()));

        return satirView;
    }
    public Integer whichPng(Integer categoryid){
        if(categoryid==1) return R.drawable.market;
        if(categoryid==2) return R.drawable.yiyecek;
        if(categoryid==3) return R.drawable.icecek;
        if(categoryid==4) return R.drawable.giyim;
        if(categoryid==5) return R.drawable.teknoloji;
        if(categoryid==6) return R.drawable.konaklama;
        if(categoryid==7) return R.drawable.ulasim;
        else return R.drawable.market;
    };
}
