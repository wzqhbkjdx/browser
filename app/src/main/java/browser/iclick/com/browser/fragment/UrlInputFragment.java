package browser.iclick.com.browser.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import browser.iclick.com.browser.R;
import browser.iclick.com.browser.autocomplete.UrlAutoCompleteFilter;
import browser.iclick.com.browser.utils.ThreadUtils;
import browser.iclick.com.browser.utils.UrlUtils;
import browser.iclick.com.browser.utils.ViewUtils;
import browser.iclick.com.browser.widget.HintFrameLayout;
import browser.iclick.com.browser.widget.InlineAutocompleteEditText;

import static browser.iclick.com.browser.R.id.dismiss;

/**
 * Created by bym on 2017/6/25.
 */

public class UrlInputFragment extends Fragment implements View.OnClickListener,
        InlineAutocompleteEditText.OnCommitListener, InlineAutocompleteEditText.OnFilterListener {

    public static final String FRAGMENT_TAG = "url_input";

    private static final String ARGUMENT_URL = "url";
    private static final String ARGUMENT_ANIMATION = "animation";
    private static final String ARGUMENT_X = "x";
    private static final String ARGUMENT_Y = "y";
    private static final String ARGUMENT_WIDTH = "width";
    private static final String ARGUMENT_HEIGHT = "height";

    private static final String ANIMATION_HOME_SCREEN = "home_screen";
    private static final String ANIMATION_BROWSER_SCREEN = "browser_screen";

    private static final int ANIMATION_DURATION = 200;


    public static UrlInputFragment createWithHomeScreenAnimation(View fakeUrlBarView) {
        int[] screenLocation = new int[2];
        fakeUrlBarView.getLocationOnScreen(screenLocation);

        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_ANIMATION, ANIMATION_HOME_SCREEN);
        arguments.putInt(ARGUMENT_X, screenLocation[0]);
        arguments.putInt(ARGUMENT_Y, screenLocation[1]);
        arguments.putInt(ARGUMENT_WIDTH, fakeUrlBarView.getWidth());
        arguments.putInt(ARGUMENT_HEIGHT, fakeUrlBarView.getHeight());

        UrlInputFragment fragment = new UrlInputFragment();

        fragment.setArguments(arguments);

        return fragment;
    }

    public static UrlInputFragment createWithBrowserScreenAnimation(String url, View urlView) {
        final Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_ANIMATION, ANIMATION_BROWSER_SCREEN);
        arguments.putString(ARGUMENT_URL, url);

        int[] screenLocation = new int[2];
        urlView.getLocationOnScreen(screenLocation);

        arguments.putInt(ARGUMENT_X, screenLocation[0]);
        arguments.putInt(ARGUMENT_Y, screenLocation[1]);
        arguments.putInt(ARGUMENT_WIDTH, urlView.getWidth());
        arguments.putInt(ARGUMENT_HEIGHT, urlView.getHeight());

        final UrlInputFragment fragment = new UrlInputFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    private View dismissView;
    private View clearView;
    private View searchViewContainer;
    private TextView searchView;
    private UrlAutoCompleteFilter urlAutoCompleteFilter;

    private InlineAutocompleteEditText urlView;
    private volatile boolean isAnimating;
    private View toolbarBackgroundView;
    private View urlInputBackgroundView;
    private HintFrameLayout urlInputContainerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_urlinput, container, false);

        dismissView = view.findViewById(dismiss);
        dismissView.setOnClickListener(this);

        clearView = view.findViewById(R.id.clear);
        clearView.setOnClickListener(this);

        searchViewContainer = view.findViewById(R.id.search_hint_container);

        searchView = (TextView) view.findViewById(R.id.search_hint);
        searchView.setOnClickListener(this);

        urlAutoCompleteFilter = new UrlAutoCompleteFilter();
        urlAutoCompleteFilter.loadDomainsInBackground(getContext().getApplicationContext());

        urlView = (InlineAutocompleteEditText) view.findViewById(R.id.url_edit);
        urlView.setOnFilterListener(this);
        urlView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus && !isAnimating) {
                    ViewUtils.showKeyboard(urlView);
                }

            }
        });

        toolbarBackgroundView = view.findViewById(R.id.toolbar_background);
        urlInputBackgroundView = view.findViewById(R.id.url_input_background);

        urlInputContainerView = (HintFrameLayout) view.findViewById(R.id.url_input_container);
        urlInputContainerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                urlInputContainerView.getViewTreeObserver().removeOnPreDrawListener(this);

                animateFirstDraw();

                return true;
            }
        });


        urlView.setOnCommitListener(this);

        if(getArguments().containsKey(ARGUMENT_URL)) {
            urlView.setText(getArguments().getString(ARGUMENT_URL));
            clearView.setVisibility(View.VISIBLE);
        }

        return view;
    }


    public boolean onBackPressed() {
        animateAndDismiss();
        return true;
    }

    private void animateFirstDraw() {
        final String animation = getArguments().getString(ARGUMENT_ANIMATION);

        if (ANIMATION_HOME_SCREEN.equals(animation)) {
            playHomeScreenAnimation(false);
        } else if (ANIMATION_BROWSER_SCREEN.equals(animation)) {
            playBrowserScreenAnimation(false);
        }
    }

    private void animateAndDismiss() {
        ThreadUtils.assertOnUiThread();

        if(isAnimating) {
            return;
        }

        dismissView.setClickable(false);

        final String animation = getArguments().getString(ARGUMENT_ANIMATION);

        if(ANIMATION_HOME_SCREEN.equals(animation)) {
            playHomeScreenAnimation(true);
        } else if(ANIMATION_BROWSER_SCREEN.equals(animation)) {
            playBrowserScreenAnimation(true);
        } else {
            dismiss();
        }

    }

    private void playBrowserScreenAnimation(final boolean reverse) {
        
        if(isAnimating) {
            return;
        }
        
        isAnimating = true;
        
        {
            
            float containerMargin = ((FrameLayout.LayoutParams)urlInputContainerView.getLayoutParams()).bottomMargin;
            
            float width = urlInputBackgroundView.getWidth();
            float height = urlInputBackgroundView.getHeight();
            
            float widthScale = (width + (2 * containerMargin)) / width;
            float heightScale = (height + (2 * containerMargin)) / height;
            
            if(!reverse) {

                urlInputBackgroundView.setPivotX(0);
                urlInputBackgroundView.setPivotY(0);
                urlInputBackgroundView.setScaleX(widthScale);
                urlInputBackgroundView.setScaleY(heightScale);
                urlInputBackgroundView.setTranslationX(-containerMargin);
                urlInputBackgroundView.setTranslationY(-containerMargin);
                urlInputContainerView.setAnimationOffset(0f);
                
                clearView.setAlpha(0);
            }

            urlInputBackgroundView.animate()
                    .setDuration(ANIMATION_DURATION)
                    .scaleX(reverse ? widthScale : 1)
                    .scaleY(reverse ? heightScale : 1)
                    .alpha(reverse ? 0 : 1)
                    .translationX(reverse ? -containerMargin : 0)
                    .translationY(reverse ? -containerMargin : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            if (reverse) {
                                clearView.setAlpha(0);
                            }
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (reverse) {
                                dismiss();
                            } else {
                                clearView.setAlpha(1);
                            }

                            isAnimating = false;
                        }
                    });
            
            
        }

        {
            int[] screenLocation = new int[2];
            urlView.getLocationOnScreen(screenLocation);

            int leftDelta = getArguments().getInt(ARGUMENT_X) - screenLocation[0] - urlView.getPaddingLeft();

            if (!reverse) {
                urlView.setPivotX(0);
                urlView.setPivotY(0);
                urlView.setTranslationX(leftDelta);
            }

            // The URL moves from the right (at least if the lock is visible) to it's actual position
            urlView.animate()
                    .setDuration(ANIMATION_DURATION)
                    .translationX(reverse ? leftDelta : 0);
        }

        if (!reverse) {
            toolbarBackgroundView.setAlpha(0);
            clearView.setAlpha(0);
        }

        // The darker background appears with an alpha animation
        toolbarBackgroundView.animate()
                .setDuration(ANIMATION_DURATION)
                .alpha(reverse ? 0 : 1);
        
    }

    private void playHomeScreenAnimation(final boolean reverse) {
        if(isAnimating) {
            return;
        }

        isAnimating = true;

        int[] screenLocation = new int[2];
        urlInputContainerView.getLocationOnScreen(screenLocation);

        int leftDelta = getArguments().getInt(ARGUMENT_X) - screenLocation[0];
        int topDelta = getArguments().getInt(ARGUMENT_Y) - screenLocation[1];

        float widthScale = getArguments().getInt(ARGUMENT_WIDTH) / urlInputContainerView.getWidth();
        float heightScale = getArguments().getInt(ARGUMENT_HEIGHT) / urlInputContainerView.getHeight();

        if(!reverse) {

            urlInputContainerView.setAlpha(0);
            urlInputContainerView.setPivotX(0);
            urlInputContainerView.setPivotY(0);
            urlInputContainerView.setScaleX(widthScale);
            urlInputContainerView.setScaleY(heightScale);
            urlInputContainerView.setTranslationX(leftDelta);
            urlInputContainerView.setTranslationY(topDelta);
            urlInputContainerView.setAnimationOffset(1.0f);

            toolbarBackgroundView.setAlpha(0);
            dismissView.setAlpha(0);

        }

        urlInputContainerView.animate()
                .setDuration(ANIMATION_DURATION)
                .scaleX(reverse ? widthScale : 1)
                .scaleY(reverse ? heightScale : 1)
                .translationX(reverse ? leftDelta : 0)
                .translationY(reverse ? topDelta : 0)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        ViewUtils.updateAlphaIfViewExists(getActivity(), R.id.fake_urlbar, 0f);

                        urlInputContainerView.setAlpha(1);

                        if(reverse) {
                            urlView.setText("");
                            urlView.setCursorVisible(false);
                            urlView.clearFocus();
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(reverse) {
                            urlInputContainerView.setAlpha(0f);

                            ViewUtils.updateAlphaIfViewExists(getActivity(), R.id.fake_urlbar, 1f);
                            dismiss();
                        } else {
                            urlView.setCursorVisible(true);
                        }

                        isAnimating = false;
                    }

                });


        final ObjectAnimator hintAnimator = ObjectAnimator.ofFloat(urlInputContainerView, "animationOffset", reverse ? 0f : 1f, reverse ? 1f : 0f);

        hintAnimator.setDuration(ANIMATION_DURATION);
        hintAnimator.start();

        toolbarBackgroundView.animate()
                .alpha(reverse ? 0 : 1)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(new DecelerateInterpolator());

        dismissView.animate()
                .alpha(reverse ? 0 : 1)
                .setDuration(ANIMATION_DURATION);

    }

    private void dismiss() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(this).commitAllowingStateLoss();
    }

    @Override
    public void onStart() {
        super.onStart();
        urlView.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case dismiss:

                break;

            case R.id.clear:

                break;

            case R.id.search_hint:
                onSearch();
                break;

            default:
                break;
        }
    }


    @Override
    public void onCommit() {
        final String input = urlView.getText().toString();
        if(!input.trim().isEmpty()) {
            ViewUtils.hideKeyboard(urlView);
            final boolean isUrl = UrlUtils.isUrl(input);

            // TODO: 2017/7/31

        }
    }

    private void openUrl() {

    }


    private void onSearch() {

    }

    @Override
    public void onFilter(String searchText, InlineAutocompleteEditText view) {
        if(!isVisible()) {
            return;
        }

        urlAutoCompleteFilter.onFilter(searchText, view);

        if(searchText.trim().isEmpty()) {

        }


    }





}











