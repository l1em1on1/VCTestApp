package pl.ndev.vctestapp.offers;

import android.location.Location;

/**
 * Created by l1em1on1 on 14/06/2015.
 */
public class LocationItem {
    public String name;
    public String address;
    public String postcode;

    public double latitude;
    public double longitude;

    public double voucherCode;
    public double featuredImage;

    public Location getLocation() {
        return null;
    }

    public String toString() {
        return String.format("%s\n%s\n%s", name, address, postcode);
    }
}

/*
    "name":         "Covent Garden",
    "address":      "34 Wellington Street, Covent Garden, London",
    "postcode":     "WC2E 7BD",
    "latitude":     51.5124853557,
    "longitude":    -0.120828744429
 */