package browser.iclick.com.browser.search;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import browser.iclick.com.browser.locale.Locales;
import browser.iclick.com.browser.utils.IOUtils;

/**
 * Created by bym on 2017/7/31.
 * 根据不同的地域或者系统语言获取不同的搜索引擎 如在美国用google 在中国用Baidu等等
 */

public class SearchEngineManager extends BroadcastReceiver {
    
    private List<SearchEngine> searchEngines;
    
    private boolean loadHasBeenTriggered = false;
    
    private static SearchEngineManager instance = new SearchEngineManager();
    
    private SearchEngineManager() {
        
    }
    
    public static SearchEngineManager getInstance() {
        return instance;
    }
    
    public void init(Context context) {
        context.registerReceiver(this, new IntentFilter(Intent.ACTION_LOCALE_CHANGED));
        loadSearchEngines(context);
    }
    
    private void loadSearchEngines(final Context context) {
        new Thread("SearchEngines-Load") {
            @Override
            public void run() {
                loadFromDisk(context);
            }
        }.start();
    }

    private void loadFromDisk(Context context) {
        loadHasBeenTriggered = true;
        final AssetManager assetManager = context.getAssets();
        final Locale locale = Locale.getDefault();
        final List<SearchEngine> searchEngins = new ArrayList<>();

        try {
            final JSONArray engineNames = loadSearchEngineListForLocale(context);

            final String localePath = "search/" + Locales.getLanguageTag(locale);
            final String languagePath = "search/" + Locales.getLanguage(locale);
            final String defaultPath = "search/default";

            final List<String> localeEngines = Arrays.asList(assetManager.list(localePath));
            final List<String> languageEngines = Arrays.asList(assetManager.list(languagePath));
            final List<String> defualtEngines = Arrays.asList(assetManager.list(defaultPath));

            for(int i = 0; i < engineNames.length(); i++) {
                final String engineName = engineNames.getString(i);
                final String fileName = engineName + ".xml";

                if(localeEngines.contains(fileName)) {

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONArray loadSearchEngineListForLocale(Context context) throws IOException {

        try {
            final Locale locale = Locale.getDefault();
            final JSONObject configuration = IOUtils.readAsset(context, "search/search_configuration.json");

            final String languageTag = Locales.getLanguageTag(locale);
            if(configuration.has(languageTag)) {
                return configuration.getJSONArray(languageTag);
            }

            final String language = Locales.getLanguage(locale);
            if(configuration.has(language)) {
                return configuration.getJSONArray(language);
            }

            return configuration.getJSONArray("default");

        } catch (JSONException e) {
            throw new AssertionError("Reading search configuration failed", e);
        }


    }


    @Override
    public void onReceive(Context context, Intent intent) {
        
    }
}











