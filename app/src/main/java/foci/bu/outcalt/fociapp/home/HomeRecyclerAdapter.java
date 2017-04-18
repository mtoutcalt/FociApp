package foci.bu.outcalt.fociapp.home;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import foci.bu.outcalt.fociapp.R;
import foci.bu.outcalt.fociapp.calm.BreatheActivity;
import foci.bu.outcalt.fociapp.habit.HabitActivity;
import foci.bu.outcalt.fociapp.inspire.QuoteActivity;
import foci.bu.outcalt.fociapp.tab.TabLayoutActivity;
import foci.bu.outcalt.fociapp.timer.TimerActivity;
import foci.bu.outcalt.fociapp.timer.TimerSessionActivity;
import foci.bu.outcalt.fociapp.todo.ToDoActivity;


public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {


    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemDetail = (TextView)itemView.findViewById(R.id.item_detail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v)
                {
                    Context context = v.getContext();
                    Intent intent;
                    int position = getAdapterPosition();

                    switch (position) {
                        case 0:
                            intent = new Intent(context, TimerActivity.class);
                            context.startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(context, TimerSessionActivity.class);
                            context.startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(context, ToDoActivity.class);
                            context.startActivity(intent);
                            break;
                        case 3:
                            intent = new Intent(context, HabitActivity.class);
                            context.startActivity(intent);
                            break;
                        case 4:
                            intent = new Intent(context, BreatheActivity.class);
                            context.startActivity(intent);
                            break;
                        case 5:
                            intent = new Intent(context, QuoteActivity.class);
                            context.startActivity(intent);
                            break;
                        case 6:
                            intent = new Intent(context, TabLayoutActivity.class);
                            context.startActivity(intent);
                            break;
                        default: break;
                    }
                }
            });
        }
    }

    private String[] titles = {
            "Timer",
            "Timer Session History",
            "TODO",
            "Habit",
            "Breathe",
            "Quotes",
            "Info"};

    private String[] details = {"" +
            "Pomodoro Timer",
            "History of Pomodoro Sessions with the number of interruptions",
            "Todo List",
            "Don't Break the Chain",
            "Follow your breathe to relax",
            "Get quotes of the day to get you inspired",
            "Information about the app"
            };

    private int[] images = {
            R.drawable.pomodoro,
            R.drawable.ic_assignment_black_24dp,
            R.drawable.ic_done_all_black_24dp,
            R.drawable.ic_event_black_24dp,
            R.drawable.ic_menu_gallery,
            R.drawable.ic_face_black_24dp,
            android.R.drawable.ic_dialog_info,
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_content_card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.itemTitle.setText(titles[position]);
        viewHolder.itemDetail.setText(details[position]);
        viewHolder.itemImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }


}
