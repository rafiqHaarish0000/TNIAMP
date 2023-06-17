package com.farmwiseai.tniamp.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.farmwiseai.tniamp.R;
import com.farmwiseai.tniamp.Retrofit.Request.ListOfTNAU;
import com.farmwiseai.tniamp.databinding.ListViewBinding;

import java.util.ArrayList;
import java.util.List;

public class UserAdapters extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    List<ListOfTNAU> data;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;
    public UserAdapters.SubjectDataFilter subjectDataFilter ;
    public List<ListOfTNAU> SubjectListTemp;
    public List<ListOfTNAU> MainList;

    public UserAdapters(Context context, List<ListOfTNAU> data) {
        this.context = context;
        this.data = data;
        this.SubjectListTemp = new ArrayList<ListOfTNAU>();
        this.SubjectListTemp.addAll(data);
        this.MainList = new ArrayList<ListOfTNAU>();
        this.MainList.addAll(data);
    }


    public Filter getFilter() {

        if (subjectDataFilter == null){

            subjectDataFilter  = new UserAdapters.SubjectDataFilter();
        }
        return subjectDataFilter;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        ListViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_view, parent, false);
        viewHolder = new DataHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListOfTNAU userData = data.get(position);

        DataHolder dataHolder = (DataHolder) holder;
//        dataHolder.binding.idText.setText(String.valueOf(userData.getID()));
        dataHolder.binding.titleText.setText(userData.getName());
//        dataHolder.binding.bodyText.setText(String.valueOf(userData.getPARENT_ID()));

    }

    @Override
    public int getItemCount() {
        return data == null ? 0: data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == data.size() -1 && isLoadingAdded) ? LOADING:ITEM;
    }




    public static class DataHolder extends RecyclerView.ViewHolder {
        public ListViewBinding binding;

        public DataHolder(ListViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class SubjectDataFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            charSequence = charSequence.toString().toLowerCase();

            FilterResults filterResults = new FilterResults();

            if(charSequence != null && charSequence.toString().length() > 0)
            {
                ArrayList<ListOfTNAU> arrayList1 = new ArrayList<ListOfTNAU>();

                for(int i = 0, l = MainList.size(); i < l; i++)
                {
                    ListOfTNAU subject = MainList.get(i);

                    if(subject.getPARENT_ID()==0)

                        arrayList1.add(subject);
                }
                filterResults.count = arrayList1.size();

                filterResults.values = arrayList1;
            }
            else
            {
                synchronized(this)
                {
                    filterResults.values = MainList;

                    filterResults.count = MainList.size();
                }
            }
            return filterResults;
        }


        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            SubjectListTemp = (ArrayList<ListOfTNAU>)filterResults.values;

            notifyDataSetChanged();

            data.clear();

            for(int i = 0, l = SubjectListTemp.size(); i < l; i++)
                data.add(SubjectListTemp.get(i));

            //subjectDataFilter.notifyDataSetInvalidated();
        }

    }

}
