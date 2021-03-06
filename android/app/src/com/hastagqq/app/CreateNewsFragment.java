package com.hastagqq.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hastagqq.app.api.NewsApiClient;
import com.hastagqq.app.model.News;

import org.apache.commons.lang3.StringUtils;

/**
 * @author avendael
 */
public class CreateNewsFragment extends Fragment {
    public static final String EXTRAS_LOCATION = "CreateNewsFragment.extras_location";
    private EditText mEtTitle;
    private EditText mEtContent;
    private String mLocation;
    private NewsApiClient.CreateCallback mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof NewsApiClient.CreateCallback)) {
            throw new IllegalArgumentException(
                    "Activity must implement NewsApiClient.CreateCallback");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_item, container, false);
        Bundle args = getArguments();
        mEtTitle = (EditText) view.findViewById(R.id.et_news_title);
        mEtContent = (EditText) view.findViewById(R.id.et_news_content);
        String defaultLocation = getString(R.string.default_news_location);
        mLocation = args != null
                ? StringUtils.isNotEmpty(args.getString(EXTRAS_LOCATION, defaultLocation))
                ? args.getString(EXTRAS_LOCATION, defaultLocation)
                : defaultLocation : defaultLocation;
        mCallback = (NewsApiClient.CreateCallback) getActivity();

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.create_news_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create_news:
                submitNews();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void submitNews() {
        String defaultTitle = getString(R.string.default_news_title);
        String title = mEtTitle.getText() != null
                ? StringUtils.isNotEmpty(mEtTitle.getText().toString())
                ? mEtTitle.getText().toString()
                : defaultTitle : defaultTitle;
        String defaultContent = getString(R.string.default_news_content);
        String content = mEtContent.getText() != null
                ? StringUtils.isNotEmpty(mEtContent.getText().toString())
                ? mEtContent.getText().toString()
                : defaultContent : defaultContent;
        News news = new News(title, content, mLocation, "");
        NewsApiClient.createNews(news, mCallback);
    }
}
