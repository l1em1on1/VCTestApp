package pl.ndev.vctestapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import pl.ndev.vctestapp.offers.Container;
import pl.ndev.vctestapp.offers.Item;
import pl.ndev.vctestapp.offers.ListAdapter;
import pl.ndev.vctestapp.offers.ParseTask;
import pl.ndev.vctestapp.utils.ConnectionDetector;

public class OfferListFragment extends ListFragment implements ParseTask.TaskCallback {

    private static final long HTTP_CACHE_SIZE = 10 * 1024 * 1024;

    private Callbacks mCallbacks;

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private int mActivatedPosition = ListView.INVALID_POSITION;

    public OfferListFragment() {}

    private final OkHttpClient client = new OkHttpClient();

    public static Cache createHttpClientCache(Context context) {
        File cacheDir = context.getDir("service_api_cache", Context.MODE_PRIVATE);

        return new Cache(cacheDir, HTTP_CACHE_SIZE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Request.Builder requestBuilder = new Request.Builder()
                .header("Accept", "application/json")
                .url(getString(R.string.offers_api));

        if (ConnectionDetector.isInternetAvailable(this.getActivity())) {
            // refreshing the content in case there are some new offers.
            requestBuilder = requestBuilder.cacheControl(CacheControl.FORCE_NETWORK);
        }

        client.setCache(createHttpClientCache(this.getActivity()));
        client.newCall(requestBuilder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("OfferListFragment", e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                new ParseTask(OfferListFragment.this).execute(response.body().string());
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = null;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        if (mCallbacks != null) {
            mCallbacks.onItemSelected(Container.ITEMS.get(position).id);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @Override
    public void onTaskFinished(Type taskType, List<Item> result) {
        setListAdapter(new ListAdapter(this.getActivity()));
    }

    public interface Callbacks {
        public void onItemSelected(String id);
    }
}
