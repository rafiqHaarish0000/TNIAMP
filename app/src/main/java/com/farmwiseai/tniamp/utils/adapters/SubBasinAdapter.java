package com.farmwiseai.tniamp.utils.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.DataClass.ComponentData;
import com.farmwiseai.tniamp.Retrofit.DataClass.Sub_Basin_Data;

import java.util.ArrayList;
import java.util.List;

public class SubBasinAdapter extends BaseAdapter {
    List<Sub_Basin_Data> data;
    public List<Sub_Basin_Data> SubjectListTemp;
    public List<Sub_Basin_Data> MainList;
    public SubBasinAdapter.SubjectDataFilter subjectDataFilter;
    Context context;

    public SubBasinAdapter(Context context, List<Sub_Basin_Data> data) {
        this.context = context;
        this.data = data;
        this.SubjectListTemp = new ArrayList<Sub_Basin_Data>();
        this.SubjectListTemp.addAll(data);
        this.MainList = new ArrayList<Sub_Basin_Data>();
        this.MainList.addAll(data);
    }


    public Filter getFilter() {

        if (subjectDataFilter == null) {

            subjectDataFilter = new SubBasinAdapter.SubjectDataFilter();
        }
        return subjectDataFilter;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class SubjectDataFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charParentID) {

            charParentID = charParentID.toString().toLowerCase();


            FilterResults filterResults = new FilterResults();

            if (charParentID != null && charParentID.toString().length() > 0) {
                ArrayList<Sub_Basin_Data> arrayList1 = new ArrayList<Sub_Basin_Data>();

                for (int i = 0, l = MainList.size(); i < l; i++) {
                    Sub_Basin_Data subject = MainList.get(i);

                    String valueOfParet = String.valueOf(subject.getPhase());

                    if (valueOfParet.equals(charParentID))

                        arrayList1.add(subject);
                }
                filterResults.count = arrayList1.size();

                filterResults.values = arrayList1;
            } else {
                synchronized (this) {
                    filterResults.values = MainList;

                    filterResults.count = MainList.size();
                }
            }
            return filterResults;
        }


        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            SubjectListTemp = (ArrayList<Sub_Basin_Data>) filterResults.values;

            notifyDataSetChanged();

            data.clear();

            for (int i = 0, l = SubjectListTemp.size(); i < l; i++)
                data.add(SubjectListTemp.get(i));

            //subjectDataFilter.notifyDataSetInvalidated();
        }

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater lInflater = (LayoutInflater) context.getSystemService(
                    Activity.LAYOUT_INFLATER_SERVICE);

            view = lInflater.inflate(R.layout.main_list_view, null);
        }
        TextView textView = (TextView) view.findViewById(R.id.data_values);
        textView.setText(data.get(i).getNAME());
//        textView.setVisibility(View.GONE);
        return view;
    }
}