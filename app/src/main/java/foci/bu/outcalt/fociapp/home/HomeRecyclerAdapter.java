package foci.bu.outcalt.fociapp.home;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import foci.bu.outcalt.fociapp.R;


public class HomeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemDetail = (TextView)itemView.findViewById(R.id.item_detail);

            itemTitle.setText(titles[0]);
            itemDetail.setText(details[0]);
            itemImage.setImageResource(images[0]);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v)
                {
                    int position = getAdapterPosition();
                    Snackbar.make(v, "Click detected on item " + position, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            });
        }
    }

    private String[] titles = {"Chapter One",
            "Chapter Two", "Chapter Three",
            "Chapter Four", "Chapter Five",
            "Chapter Six", "Chapter Seven",
            "Chapter Eight"};

    private String[] details = {"Item one details", "Item two details",
            "Item three details", "Item four details",
            "Item file details", "Item six details",
            "Item seven details", "Item eight details"};

    private int[] images = {
            R.drawable.chart
    };

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_content_card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public int getItemCount() {
        return titles.length;
    }


}
