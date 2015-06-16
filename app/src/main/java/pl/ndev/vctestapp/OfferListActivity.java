package pl.ndev.vctestapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class OfferListActivity extends AppCompatActivity implements OfferListFragment.Callbacks {

    private boolean mTwoPane;
    private View rightContainer;
    private String offerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list);

        if (findViewById(R.id.offer_detail_container) != null) {
            mTwoPane = true;

            rightContainer = findViewById(R.id.right_container);

            ((OfferListFragment) getFragmentManager()
                    .findFragmentById(R.id.offer_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(String offerId) {
        if (mTwoPane) {
            this.offerId = offerId;
            rightContainer.setVisibility(View.VISIBLE);
            getFragmentManager().beginTransaction()
                    .replace(R.id.offer_detail_container, OfferDetailFragment.newInstance(offerId))
                    .commit();
        } else {
            Intent detailIntent = new Intent(this, OfferDetailActivity.class);
            detailIntent.putExtra(OfferDetailFragment.ARG_ITEM_ID, offerId);
            startActivity(detailIntent);
        }
    }

    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (offerId != null && rightContainer != null) {
            rightContainer.setVisibility(View.VISIBLE);
        }
    }
}
