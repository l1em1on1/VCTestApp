package pl.ndev.vctestapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import pl.ndev.vctestapp.offers.Container;
import pl.ndev.vctestapp.offers.Item;

public class OfferDetailFragment extends Fragment implements OnMapReadyCallback {

    public static final String ARG_ITEM_ID = "item_id";

    private Item offerItem;
    private String logoUrlModifier;

    public static OfferDetailFragment newInstance(String offerId) {
        OfferDetailFragment fragment = new OfferDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ITEM_ID, offerId);
        fragment.setArguments(args);

        return fragment;
    }

    public OfferDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            offerItem = Container.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.logoUrlModifier = activity.getString(R.string.logo_url_modifier);

        MapFragment mapFragment = (MapFragment) activity.getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_offer_detail, container, false);

        if (offerItem != null) {
            ((TextView) rootView.findViewById(R.id.offer_item_merchant_name)).setText(offerItem.merchantName);
            ((TextView) rootView.findViewById(R.id.offer_item_title)).setText(offerItem.offerTitle);

            Glide.with(getActivity())
                    .load(offerItem.getMerchantLogo(logoUrlModifier))
                    .dontAnimate().fitCenter().into((ImageView) rootView.findViewById(R.id.offer_item_merchant_logo));

            rootView.findViewById(R.id.offer_item_exclusive).setVisibility(offerItem.isExclusive ? View.VISIBLE : View.GONE);
        }

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
