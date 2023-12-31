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
import com.farmwiseai.tniamp.Retrofit.DataClass.BlockData;
import com.farmwiseai.tniamp.Retrofit.DataClass.VillageData;

import java.util.ArrayList;
import java.util.List;

public class VillageAdaapter extends BaseAdapter {
    List<VillageData> data;
    public List<VillageData> SubjectListTemp;
    public List<VillageData> MainList;
    public VillageAdaapter.SubjectDataFilter subjectDataFilter;
    Context context;

    public  VillageAdaapter(Context context, List<VillageData> data) {
        this.context = context;
        this.data = data;
        this.SubjectListTemp = new ArrayList<VillageData>();
        this.SubjectListTemp.addAll(data);
        this.MainList = new ArrayList<VillageData>();
        this.MainList.addAll(data);
    }


    public Filter getFilter() {

        if (subjectDataFilter == null) {

            subjectDataFilter = new VillageAdaapter.SubjectDataFilter();
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
                ArrayList<VillageData> arrayList1 = new ArrayList<VillageData>();

                for (int i = 0, l = MainList.size(); i < l; i++) {
                    VillageData subject = MainList.get(i);

                    String valueOfParet = String.valueOf(subject.getBLOCK_ID());

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

            SubjectListTemp = (ArrayList<VillageData>) filterResults.values;

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
