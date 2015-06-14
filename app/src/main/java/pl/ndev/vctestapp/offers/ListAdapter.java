package pl.ndev.vctestapp.offers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import pl.ndev.vctestapp.R;

public class ListAdapter extends BaseAdapter
{

    private Context context;

    public ListAdapter(Context context) {
        this.context = context;
    }

    static class ViewHolder
    {
        TextView merchantNameView;
        TextView offerTitleView;

        ImageView featuredImageView;
        ImageView merchantLogoView;

        ImageView exclusiveView;
    }

    @Override
    public int getCount() {
        return Container.ITEMS.size();
    }

    @Override
    public Object getItem(int position) {
        return Container.ITEMS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_offer, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.merchantNameView = (TextView) convertView.findViewById(R.id.offer_item_merchant_name);
            viewHolder.offerTitleView = (TextView) convertView.findViewById(R.id.offer_item_title);
            viewHolder.featuredImageView = (ImageView) convertView.findViewById(R.id.offer_item_featured_image);
            viewHolder.merchantLogoView = (ImageView) convertView.findViewById(R.id.offer_item_merchant_logo);
            viewHolder.exclusiveView = (ImageView) convertView.findViewById(R.id.offer_item_exclusive);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Item offer = Container.ITEMS.get(position);

        if (offer != null) {
            viewHolder.merchantNameView.setText(offer.merchantName);
            viewHolder.offerTitleView.setText(offer.offerTitle);

            Glide.with(context).load(offer.getMerchantLogo(context)).dontAnimate().fitCenter().into(viewHolder.merchantLogoView);

            viewHolder.featuredImageView.setVisibility(View.VISIBLE);
            if (offer.featuredImage != null) {
                viewHolder.featuredImageView.setVisibility(View.GONE);
                Glide.with(context).load(offer.featuredImage).dontAnimate().centerCrop().into(viewHolder.featuredImageView);
            }

            viewHolder.exclusiveView.setVisibility(offer.isExclusive ? View.VISIBLE : View.GONE);
        }

        return convertView;
    }
}