package pl.ndev.vctestapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Type;
import java.util.List;

import pl.ndev.vctestapp.offers.Container;
import pl.ndev.vctestapp.offers.Item;
import pl.ndev.vctestapp.offers.ListAdapter;
import pl.ndev.vctestapp.offers.ParseTask;
import pl.ndev.vctestapp.offers.Request;

public class OfferListFragment extends ListFragment implements ParseTask.TaskCallback {



    private Callbacks mCallbacks;

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private int mActivatedPosition = ListView.INVALID_POSITION;

    public OfferListFragment() {}

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Container.ITEMS.size() == 0) {
            Request.loadOffers(client, this.getActivity(), this);
        } else {
            this.onTaskFinished(null, Container.ITEMS);
        }
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
            mCallbacks.onItemSelected(Container.mapItem(position).getId());
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
