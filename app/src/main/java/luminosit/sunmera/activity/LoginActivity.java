package luminosit.sunmera.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import luminosit.sunmera.R;
import luminosit.sunmera.ui.IconTitleAdapter;
import luminosit.sunmera.util.UserDatabase;


public class LoginActivity extends ActionBarActivity {

    //extras' keys


    //ui
    ListView listView;

    //default username
    private static final String DEFAULT_USERNAME = "Default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //for one-account version
        orJustUseDefaultAccount();
    }

    public void onResume(){
        super.onResume();
        //doLogin();
        finish();
    }

    public void doLogin(){
        UserDatabase userDatabase = new UserDatabase(this);

        //set up the adapter
        final String[] userList = userDatabase.getUserList();
        listView = (ListView)findViewById(R.id.login_listView);
        listView.setAdapter(new IconTitleAdapter(this, userList, userList));

        //listener, pass the clicked user name to the main activity
        final Intent loginIntent = new Intent(this, MainActivity.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loginIntent.putExtra(MainActivity.EXTRA_USER_NAME, userList[position]);
                startActivity(loginIntent);
            }
        });
    }

    public void orJustUseDefaultAccount(){
        Intent loginIntent = new Intent(this, MainActivity.class);
        loginIntent.putExtra(MainActivity.EXTRA_USER_NAME, DEFAULT_USERNAME);
        startActivity(loginIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.action_test:
                startActivity(new Intent(this, TestActivity.class));
                return true;
            case R.id.action_add_user:

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
