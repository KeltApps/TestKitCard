package com.keltapps.testkitcard.views.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keltapps.testkitcard.R;
import com.keltapps.testkitcard.utils.ScriptDatabase;


public class BikePointCursorAdapter extends CursorRecyclerViewAdapter<BikePointCursorAdapter.ViewHolderBikePoint> {
    private final Context context;

    public BikePointCursorAdapter(Context context) {
        super(null);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(ViewHolderBikePoint viewHolder, Cursor cursor) {
        viewHolder.bindHolder(context, cursor);
    }

    @Override
    public ViewHolderBikePoint onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderBikePoint(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bike_point_list, parent, false));
    }

    public static class ViewHolderBikePoint extends RecyclerView.ViewHolder {
        TextView textViewCommonName;
        TextView textViewInformation;

        public ViewHolderBikePoint(View itemView) {
            super(itemView);
            textViewCommonName = (TextView) itemView.findViewById(R.id.item_bike_point_commonName);
            textViewInformation = (TextView) itemView.findViewById(R.id.item_bike_point_information);
        }

        public void bindHolder(Context context, Cursor cursor) {
            textViewCommonName.setText(cursor.getString(cursor.getColumnIndex(ScriptDatabase.ColumnBikePoint.COMMON_NAME)));
            int availableDocks = cursor.getInt(cursor.getColumnIndex(ScriptDatabase.ColumnBikePoint.AVAILABLE_DOCKS));
            int totalDocks = cursor.getInt(cursor.getColumnIndex(ScriptDatabase.ColumnBikePoint.TOTAL_DOCKS));

            String information = context.getString(R.string.bikePointList_information, availableDocks, totalDocks);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(information);
            spannableStringBuilder.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, information.indexOf(" "), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), information.lastIndexOf(" "), information.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textViewInformation.setText(spannableStringBuilder);
        }

    }
}
