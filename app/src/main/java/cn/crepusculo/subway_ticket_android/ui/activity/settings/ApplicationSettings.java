package cn.crepusculo.subway_ticket_android.ui.activity.settings;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import cn.crepusculo.subway_ticket_android.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class ApplicationSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        View settingsView = new AboutPage(this)
                .isRTL(false)
                .setDescription("地铁自助购票软件 \n Android 客户端")
                .setImage(R.drawable.ic_subway_white_24dp)
                .addGroup("Pc version")
                .addWebsite("http://101.200.144.204:16080/subway-ticket-web")
                .addGroup("Connect with us")
                .addGitHub("Crepusculo")
                .addEmail("airfree1452@gmail.com")
                .addItem(new Element().setTitle("Version 1.0.0"))
                .addItem(getCopyRightsElement())
                .create();
        setContentView(settingsView);
    }

    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIcon(R.drawable.ic_copy_right);
        copyRightsElement.setColor(ContextCompat.getColor(this, mehdi.sakout.aboutpage.R.color.about_item_icon_color));
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ApplicationSettings.this, copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }
}
