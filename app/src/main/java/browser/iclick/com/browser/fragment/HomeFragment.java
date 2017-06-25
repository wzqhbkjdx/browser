package browser.iclick.com.browser.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import browser.iclick.com.browser.R;

/**
 * Created by bym on 2017/6/24.
 */

public class HomeFragment extends LocaleAwareFragment
        implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    public static final String FRAGMENT_TAG = "home";


    public static HomeFragment create() {
        return new HomeFragment();
    }

    private View fakeUrlBarView;

    @Override
    public void applyLocale() {
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, HomeFragment.create(), HomeFragment.FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final FragmentActivity activity = getActivity();

        if(activity != null && Intent.ACTION_VIEW.equals(activity.getIntent().getAction())) {
            activity.setIntent(new Intent(Intent.ACTION_MAIN));
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        fakeUrlBarView  = view.findViewById(R.id.fake_urlbar);
        fakeUrlBarView.setOnClickListener(this);

        view.findViewById(R.id.menu).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fake_urlbar:

                break;
            case R.id.menu:

                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

}
