package foci.bu.outcalt.fociapp.habit;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import foci.bu.outcalt.fociapp.R;

public class BreakChainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "foci.bu.outcalt.fociapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_break_chain_activity);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        String duration = extras.getString(EXTRA_MESSAGE);
        System.out.println("DURATION PASSED IS: " + duration);
        createTheChain(Integer.parseInt(duration));
    }

    private void createTheChain(int days) {
        int counter = 1;
        // get a reference for the TableLayout
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        int rows;

        if (days < 4) {
            rows = 1;
        } else {
            rows = days / 4;
            if (days % 4 != 0) {
                rows++;
            }
        }
        for (int i = 0; i < rows; i++) {
            TableRow row1 = new TableRow(this);
            for (int j = 0; j < 4; j++) {
                if (days == 0) {
                    break;
                }
                    // create a new TextView
                    final Button button = new Button(this);
                    button.setBackgroundColor(Color.BLUE);
                    button.setTextColor(Color.WHITE);
                    button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Button button1 = (Button) v;
                        button1.setBackgroundColor(Color.GREEN);
                        button1.setTextColor(Color.BLACK);
                    }
                });
                    button.setText("DAY " + counter);
                    // add the TextView to the new TableRow
                    row1.addView(button);
                    days--;
                    ++counter;
            }
            table.addView(row1, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }



}
