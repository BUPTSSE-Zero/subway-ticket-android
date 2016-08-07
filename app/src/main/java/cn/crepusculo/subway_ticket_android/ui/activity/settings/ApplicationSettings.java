package cn.crepusculo.subway_ticket_android.ui.activity.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;

public class ApplicationSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View settingsView = new AboutPage(this)
                .isRTL(false)
//                .setImage()
                .addWebsite("http://101.200.144.204:16080/subway-ticket-web")
                .addGroup("Connect with us")
                .addGitHub("Crepusculo")
                .addEmail("airfree1452@gmail.com")
                .create();
        setContentView(settingsView);
    }


}
