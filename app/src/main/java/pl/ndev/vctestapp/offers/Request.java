package pl.ndev.vctestapp.offers;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import pl.ndev.vctestapp.R;
import pl.ndev.vctestapp.utils.ConnectionDetector;

public class Request {
    private static final long HTTP_CACHE_SIZE = 10 * 1024 * 1024;

    public static Cache createHttpClientCache(Context context) {
        File cacheDir = context.getDir("service_api_cache", Context.MODE_PRIVATE);

        return new Cache(cacheDir, HTTP_CACHE_SIZE);
    }

    public static void loadOffers(OkHttpClient client, Context context, final ParseTask.TaskCallback callback) {
        com.squareup.okhttp.Request.Builder requestBuilder = new com.squareup.okhttp.Request.Builder()
                .header("Accept", "application/json")
                .url(context.getString(R.string.offers_api));

        if (ConnectionDetector.isInternetAvailable(context)) {
            // refreshing the content in case there are some new offers.
            requestBuilder = requestBuilder.cacheControl(CacheControl.FORCE_NETWORK);
        }

        client.setCache(createHttpClientCache(context));
        client.newCall(requestBuilder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, IOException e) {
                Log.d("OfferListFragment", e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                new ParseTask(callback).execute(response.body().string());
            }
        });
    }
}
