package com.example.myappforsitekit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huawei.hms.site.api.SearchResultListener;
import com.huawei.hms.site.api.SearchService;
import com.huawei.hms.site.api.SearchServiceFactory;
import com.huawei.hms.site.api.model.AddressDetail;
import com.huawei.hms.site.api.model.SearchStatus;
import com.huawei.hms.site.api.model.Site;
import com.huawei.hms.site.api.model.TextSearchRequest;
import com.huawei.hms.site.api.model.TextSearchResponse;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SearchService searchService;
    TextView resultTextView;
    EditText queryInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_layout);

        searchService = SearchServiceFactory.create(this, "CV7h53mNfW86LsdIENFugL8L+PRioKm1BJ4BgV45nAk5c/EvHV85W6Tt9kjwEb/RPIj3zCdHX2aJxU9l2irZlyhMVnNM");

        queryInput = findViewById(R.id.edit_text_text_search_query);
        resultTextView = findViewById(R.id.response_text_search);

        TextSearchRequest textSearchRequest = new TextSearchRequest();
        textSearchRequest.setQuery(queryInput.getText().toString());
    }

    public void search(View view) {
        TextSearchRequest textSearchRequest = new TextSearchRequest();
        textSearchRequest.setQuery(queryInput.getText().toString());
        searchService.textSearch(textSearchRequest, new SearchResultListener<TextSearchResponse>() {
            @Override
            public void onSearchResult(TextSearchResponse textSearchResponse) {

                StringBuilder response = new StringBuilder("\n");
                response.append("success\n");
                int count = 1;
                AddressDetail addressDetail;
                for (Site site : textSearchResponse.getSites()) {
                    addressDetail = site.getAddress();
                    response.append(String.format(
                            "[%s]  name: %s, formatAddress: %s, country: %s, countryCode: %s \r\n",
                            "" + (count++), site.getName(), site.getFormatAddress(),
                            (addressDetail == null ? "" : addressDetail.getCountry()),
                            (addressDetail == null ? "" : addressDetail.getCountryCode())));
                }
                Log.d(TAG, "search result is : " + response);
                resultTextView.setText(response.toString());
            }

            @Override
            public void onSearchError(SearchStatus searchStatus) {
                Log.e(TAG, "onSearchError is: " + searchStatus.getErrorCode());
            }
        });
    }
}
