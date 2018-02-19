package hr.from.kkf.kingictapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.teamName) EditText teamName;
    @BindView(R.id.teamPassword) EditText teamPassword;

    @BindView(R.id.name1) EditText name1;
    @BindView(R.id.surname1) EditText surname1;
    @BindView(R.id.email1) EditText email1;

    @BindView(R.id.name2) EditText name2;
    @BindView(R.id.surname2) EditText surname2;
    @BindView(R.id.email2) EditText email2;

    @BindView(R.id.name3) EditText name3;
    @BindView(R.id.surname3) EditText surname3;
    @BindView(R.id.email3) EditText email3;

    @BindView(R.id.name4) EditText name4;
    @BindView(R.id.surname4) EditText surname4;
    @BindView(R.id.email4) EditText email4;

    @BindView(R.id.registerResultText) TextView resultText;
    @BindView(R.id.registerResultBody) TextView resultBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.autofillDataButton)
    public void fillData(){
        teamName.setText("KKF");
        teamPassword.setText("od1do10");

        name1.setText("Frano");
        surname1.setText("Ćaleta");
        email1.setText("frano.caleta@fer.hr");

        name2.setText("Marin");
        surname2.setText("Krešo");
        email2.setText("marin.kreso@fer.hr");

        name3.setText("Matteo");
        surname3.setText("Miloš");
        email3.setText("matteo.milos@fer.hr");

        name4.setText("Ivan");
        surname4.setText("Rezić");
        email4.setText("ivan.rezic@fer.hr");
    }

    @OnClick(R.id.registerButton)
    public void sendPostRequest(){
        List<Member> members = new ArrayList<>();
        members.add(new Member(name1.getText().toString(), surname1.getText().toString(), email1.getText().toString()));
        members.add(new Member(name2.getText().toString(), surname2.getText().toString(), email2.getText().toString()));
        members.add(new Member(name3.getText().toString(), surname3.getText().toString(), email3.getText().toString()));
        members.add(new Member(name4.getText().toString(), surname4.getText().toString(), email4.getText().toString()));

        TeamData team = new TeamData(teamName.getText().toString(), teamPassword.getText().toString(), members);

        Gson gson = new Gson();

        String json = gson.toJson(team);

        try {
            AndroidNetworking.post("http://52.233.158.172/change/api/hr/account/register")
                    .addJSONObjectBody(new JSONObject(json)) // posting json
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            try {
                                JSONObject object = new JSONObject(response.getString("Result"));
                                String nextLink = object.getString("NextApi");

                                resultText.setVisibility(View.VISIBLE);
                                resultBody.setText(nextLink);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError error) {
                            Toast.makeText(RegisterActivity.this, "Team already registered", Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class TeamData{
        private String Teamname;
        private String Password;
        private List<Member> Members = new ArrayList<>();

        public TeamData(String teamName, String teamPassword, List<Member> members) {
            this.Teamname = teamName;
            this.Password = teamPassword;
            this.Members = members;
        }
    }

    private class Member {
        private String name;
        private String surname;
        private String mail;

        public Member(String name, String surname, String email) {
            this.name = name;
            this.surname = surname;
            this.mail = email;
        }
    }
}
