package pl.ndev.vctestapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class OfferListActivity extends AppCompatActivity implements OfferListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list);

        if (findViewById(R.id.offer_detail_container) != null) {
            mTwoPane = true;

            ((OfferListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.offer_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(OfferDetailFragment.ARG_ITEM_ID, id);
            OfferDetailFragment fragment = new OfferDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.offer_detail_container, fragment)
                    .commit();
        } else {
            Intent detailIntent = new Intent(this, OfferDetailActivity.class);
            detailIntent.putExtra(OfferDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}