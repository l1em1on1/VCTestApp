package pl.ndev.vctestapp;

import android.app.Activity;
import android.app.Fragment;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pl.ndev.vctestapp.offers.Container;
import pl.ndev.vctestapp.offers.Item;
import pl.ndev.vctestapp.offers.LocationItem;
import pl.ndev.vctestapp.utils.ConnectionDetector;

public class OfferDetailFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    public static final String ARG_ITEM_ID = "item_id";

    private static final long LOCATION_REFRESH_TIME = 60 * 60 * 1000;
    private static final float LOCATION_REFRESH_DISTANCE = 10000;

    private GoogleMap googleMap;

    private Item offerItem;
    private String logoUrlModifier;
    private LocationManager locationManager;

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


        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        locationManager = (LocationManager) activity.getSystemService(Activity.LOCATION_SERVICE);

        String provider = locationManager.getBestProvider(criteria, true);

        if (provider != null) {
            locationManager.requestLocationUpdates(provider, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, this);
        }

        if (ConnectionDetector.isInternetAvailable(activity)) {
            MapFragment mapFragment = (MapFragment) activity.getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
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
            rootView.findViewById(R.id.offer_code_container).setVisibility(offerItem.voucherCode != null && !offerItem.voucherCode.isEmpty() ? View.VISIBLE : View.GONE);

            final TextView codeView = (TextView) rootView.findViewById(R.id.offer_code);
            rootView.findViewById(R.id.offer_code_reveal).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    codeView.setVisibility(View.VISIBLE);
                    codeView.setText(offerItem.voucherCode);
                }
            });

            View showMoreButton = rootView.findViewById(R.id.offer_show_more);
            if (offerItem.featuredImage != null && !offerItem.featuredImage.isEmpty()) {
                showMoreButton.setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.offer_show_more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out, R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                                .replace(R.id.offer_detail_container, OfferDetailReverseFragment.newInstance(getArguments().getString(OfferDetailFragment.ARG_ITEM_ID)))
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }
        }

        return rootView;
    }

    private void updateLocation(Location location) {
        if (location != null) {
            LocationItem offerLocation = offerItem.getNearestOfferLocation(location);
            if (googleMap != null) {
                LatLng centerLatLng = new LatLng(offerLocation.latitude, offerLocation.longitude);
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(centerLatLng).title(offerLocation.toString()));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerLatLng, 10));
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        updateLocation(Item.NEAREST_LOCATION);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location", location.toString());
        if (Item.NEAREST_LOCATION == null || location.distanceTo(Item.NEAREST_LOCATION) > 1000) {
            //only update map if location changed significantly since last check.
            updateLocation(location);
        }

        Item.NEAREST_LOCATION = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //not relevant at the moment
    }

    @Override
    public void onProviderEnabled(String s) {
        //not relevant at the moment
    }

    @Override
    public void onProviderDisabled(String s) {
        if (LocationManager.GPS_PROVIDER.equals(s)) {
            locationManager.removeUpdates(this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, this);
        }
    }
}
