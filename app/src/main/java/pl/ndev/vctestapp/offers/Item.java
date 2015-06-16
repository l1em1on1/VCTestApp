package pl.ndev.vctestapp.offers;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import pl.ndev.vctestapp.utils.Formatter;

/**
 * Created by l1em1on1 on 14/06/2015.
 */
public class Item {
    public static Location NEAREST_LOCATION = null;

    public String id;

    public Boolean isExclusive = false;

    public List<LocationItem> locations = new ArrayList<LocationItem>();
    public String merchantName;
    public String merchantLogo;
    public String offerTitle;
    public String voucherCode;
    public String featuredImage;

    public String getId() {
        if (id == null) {
            id = Formatter.formatAsMd5(String.format("%s %s", merchantName, offerTitle));
        }

        return id;
    }

    public String getMerchantLogo(String urlModifier) {
        // ­android #MDPI
        // ­android@1.5x #HDPI
        // ­android@2x #XHDPI
        return urlModifier != null ? merchantLogo.replaceAll("(.+)(\\.[^\\.]+)$", String.format("$1%s$2", urlModifier)) : merchantLogo;
    }

    public LocationItem getNearestOfferLocation(Location location) {
        LocationItem nearestLocationItem = null;
        double distance = -1;

        for (LocationItem locationItem : locations) {
            float [] lastDistance = new float[1];
            Location.distanceBetween(locationItem.latitude, locationItem.longitude, location.getLatitude(), location.getLongitude(), lastDistance);

            if (distance >= 0 && lastDistance[0] > distance)
                continue;

            distance = lastDistance[0];
            nearestLocationItem = locationItem;
        }

        return nearestLocationItem;
    }

    @Override
    public String toString() {
        return offerTitle;
    }
}

/*
    "merchantName": "PizzaExpress",
    "merchantLogo": "http://hosted.vouchercodes.co.uk/careers/tests/mobile/assets/pizzaexpress.png",
    "offerTitle":   "2 for 1 on Main Courses",
    "voucherCode":  "TEST2",
    "featuredImage": "http://hosted.vouchercodes.co.uk/careers/tests/mobile/assets/featured-pizzaexpress.png", (optional)
    "isExclusive":   true, (optional)
    "locations": []
 */