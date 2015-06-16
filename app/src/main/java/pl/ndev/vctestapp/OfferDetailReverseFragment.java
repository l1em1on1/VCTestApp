package pl.ndev.vctestapp;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import pl.ndev.vctestapp.offers.Container;
import pl.ndev.vctestapp.offers.Item;

public class OfferDetailReverseFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private Item offerItem;

    public static OfferDetailReverseFragment newInstance(String offerId) {
        OfferDetailReverseFragment fragment = new OfferDetailReverseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ITEM_ID, offerId);
        fragment.setArguments(args);

        return fragment;
    }

    public OfferDetailReverseFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            offerItem = Container.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_offer_detail_reverse, container, false);

        if (offerItem != null) {
            Glide.with(getActivity())
                    .load(offerItem.featuredImage)
                    .dontAnimate().fitCenter().into((ImageView) rootView.findViewById(R.id.offer_item_featured_image));
        }

        return rootView;
    }

}
