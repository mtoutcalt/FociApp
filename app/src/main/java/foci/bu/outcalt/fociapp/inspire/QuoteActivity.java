package foci.bu.outcalt.fociapp.inspire;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import foci.bu.outcalt.fociapp.R;
import foci.bu.outcalt.fociapp.calm.BreatheActivity;
import foci.bu.outcalt.fociapp.creative.BrainstormTopicActivity;
import foci.bu.outcalt.fociapp.habit.HabitActivity;
import foci.bu.outcalt.fociapp.home.HomeActivity;
import foci.bu.outcalt.fociapp.tab.TabLayoutActivity;
import foci.bu.outcalt.fociapp.timer.TimerActivity;
import foci.bu.outcalt.fociapp.timer.TimerSessionActivity;
import foci.bu.outcalt.fociapp.todo.ToDoActivity;

public class QuoteActivity extends AppCompatActivity {

    //async task that will call quote REST service
    //uses spring restTemplate to form http call and jackson to map JSON response to Java objects
    private class HttpRequestTask extends AsyncTask<String, Void, QuoteParent> {

        public HttpRequestTask() {
            super();
        }

        @Override
        protected QuoteParent doInBackground(String... params) {
            try {
//                final String url = "http://quotes.rest/qod.json?category=life";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ResponseEntity<QuoteParent> quoteParent = restTemplate.getForEntity(params[0], QuoteParent.class);
                return quoteParent.getBody();
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(QuoteParent quoteParent) {
            TextView nameText = (TextView) findViewById(R.id.textView);
            TextView quoteText = (TextView) findViewById(R.id.textView2);
            nameText.setText("\"" + quoteParent.getContents().getQuotes()[0].getQuote() + "\"");
            quoteText.setText(quoteParent.getContents().getQuotes()[0].getAuthor());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quote_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new QuoteFragment())
                    .commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void getInspireQuote(View view) {
        final String url = "http://quotes.rest/qod.json?category=inspire";
        new HttpRequestTask().execute(url);
    }

    public void getManagementQuote(View view) {
        final String url = "http://quotes.rest/qod.json?category=management";
        new HttpRequestTask().execute(url);
    }

    public void getFunnyQuote(View view) {
        final String url = "http://quotes.rest/qod.json?category=funny";
        new HttpRequestTask().execute(url);
    }

    public static class QuoteFragment extends Fragment {

        public QuoteFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.quote_fragment, container, false);
            return rootView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.foci_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_home:
                intent = new Intent(this, HomeActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_todo:
                intent = new Intent(this, ToDoActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_habit:
                intent = new Intent(this, HabitActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_timer:
                intent = new Intent(this, TimerActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_breathe:
                intent = new Intent(this, BreatheActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_info:
                intent = new Intent(this, TabLayoutActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_quote:
                intent = new Intent(this, QuoteActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_brainstorm:
                intent = new Intent(this, BrainstormTopicActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_session_history:
                intent = new Intent(this, TimerSessionActivity.class);
                this.startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}
