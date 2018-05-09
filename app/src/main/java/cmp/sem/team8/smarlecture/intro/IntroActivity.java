package cmp.sem.team8.smarlecture.intro;

import android.Manifest;
import android.os.Bundle;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import cmp.sem.team8.smarlecture.R;


public class IntroActivity extends com.heinrichreimersoftware.materialintro.app.IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullscreen(true);
        super.onCreate(savedInstanceState);
        addSlide(new SimpleSlide.Builder()
                .title("Welcome To Smart Session")
                .description("Save your time and organize your groups")
                .image(R.drawable.logo_big_burned)
                .background(R.color.blueback)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Manage your groups")
                .description("Make group and add members to it. Invite them and start sessions in the group")
                .image(R.drawable.intro_manage_big)
                .background(R.color.blueback)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Make sessions")
                .description("Make any number of sessions in your group. open the session and enjoy its interactions")
                .image(R.drawable.intro_meeting)
                .background(R.color.blueback)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Enjoy session interactions")
                .description("Trusted attendance system, session questions, notes about members and more")
                .image(R.drawable.intro_interaction_big)
                .background(R.color.blueback)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Export your data")
                .description("Visualize the group data with all sessions in excel format")
                .image(R.drawable.intro_export)
                .background(R.color.blueback)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .build());
    }
}
