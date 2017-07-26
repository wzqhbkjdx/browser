package browser.iclick.com.browser.autocomplete;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import browser.iclick.com.browser.locale.Locales;
import browser.iclick.com.browser.utils.LogUtils;
import browser.iclick.com.browser.widget.InlineAutocompleteEditText;

/**
 * Created by bym on 2017/7/25.
 */

public class UrlAutoCompleteFilter implements InlineAutocompleteEditText.OnFilterListener {
    private static final String LOG_TAG = "UrlAutoCompleteFilter";

    private Set<String> domains;

    private static String prepareAutoCompleteResult(final String rawSearchText, final String lowerCaseResult) {
        return rawSearchText + lowerCaseResult.substring(rawSearchText.length());
    }


    @Override
    public void onFilter(String rawSearchText, InlineAutocompleteEditText view) {
        if(domains == null || view == null) {
            return;
        }

        final String searchText = rawSearchText.toLowerCase(Locale.US);

        for(final String domain : domains) {
            final String wwwDomain = "www." + domain;
            if(wwwDomain.startsWith(searchText)) {
                view.onAutocomplete(prepareAutoCompleteResult(rawSearchText, wwwDomain));
                return;
            }

            if(domain.startsWith(searchText)) {
                view.onAutocomplete(prepareAutoCompleteResult(rawSearchText, wwwDomain));
                return;
            }

        }
    }

    @VisibleForTesting
    void onDomainsLoaded(Set<String> domains) {
        this.domains = domains;
    }

    public void loadDomainsInBackground(final Context context) {

        new AsyncTask<Resources, Void, Set<String>>() {

            @Override
            protected Set<String> doInBackground(Resources... params) {
                final Set<String> domains = new LinkedHashSet<String>();
                final Set<String> availableLists = getAvailableDomainList(context);

                for (final String country : Locales.getCountriesInDefaultLocaleList()) {
                    if(availableLists.contains(country)) {
                        loadDomainsFromLanguage(context, domains, country);
                    }
                }

                loadDomainsFromLanguage(context, domains, "global");

                return domains;
            }
        }.execute(context.getResources());

    }

    private Set<String> getAvailableDomainList(Context context) {

        final Set<String> availableDomains = new HashSet<>();
        final AssetManager assetManager = context.getAssets();

        try {
            Collections.addAll(availableDomains, assetManager.list("domains"));
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.v(LOG_TAG, "could not get domains from assets");
        }

        return availableDomains;
    }

    private void loadDomainsFromLanguage(Context context, Set<String> domains, String country) {

        final AssetManager assetManager = context.getAssets();

        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open("domains/" + country), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                domains.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.v(LOG_TAG, "could not get domains from assets by language");
        }

    }




}










