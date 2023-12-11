package com.knowledgerealm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    Element versionElement = new Element();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getView());
    }

    private View getView() {
        setVersion();

        return new AboutPage(this)
                .isRTL(false)

                // About App
                .setDescription(this.getString(R.string.extended_description))
                .setImage(R.drawable.ic_launcher_custom)

                // App Details
                .addGroup(this.getString(R.string.app_details))
                .addItem(versionElement)

                // Contact us
                .addGroup(this.getString(R.string.contact_us))
                .addEmail("xxxx@csd.auth.gr", this.getString(R.string.send_us_an_email))

                // Credits
                .addGroup(this.getString(R.string.credits))
                .addWebsite("https://icons8.com/", this.getString(R.string.icons_by_icons8))
                .addWebsite("https://github.com/medyo/android-about-page", this.getString(R.string.about_page_library))
                .addWebsite("https://freesound.org/", this.getString(R.string.sound_effects_by_freesoundorg))
                .create();
    }

    /**
     * Sets the version of the app in the About Activity
     */
    private void setVersion() {
        versionElement.setTitle(this.getString(R.string.version) + " " + BuildConfig.VERSION_NAME);
    }

}