package net.topafrica.Annuaire.adapter;

import android.content.Context;
import android.location.Address;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import net.topafrica.Annuaire.GeoSearchModel;
import net.topafrica.Annuaire.R;
import net.topafrica.Annuaire.modal.category.Businesse;
import net.topafrica.Annuaire.rx.AddressToStringFunc;
import net.topafrica.Annuaire.rx.DisplayTextOnViewAction;
import net.topafrica.Annuaire.rx.ErrorHandler;
import net.topafrica.Annuaire.rx.FallbackReverseGeocodeObservable;
import net.topafrica.Annuaire.rx.ReverseGeocodeObservable;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by ericbasendra on 02/12/15.
 */
public class CategorySearchAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Context mContext;
    public List<T> dataSet;
    private static MyClickListener myClickListener;

    public CategorySearchAdapter(List<T> productLists, Context mContext) {
        this.mContext = mContext;
        this.dataSet = productLists;
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void animateTo(List<T> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }


    private void applyAndAnimateRemovals(List<T> newModels) {
        for (int i = dataSet.size() - 1; i >= 0; i--) {
            final T model = dataSet.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }


    private void applyAndAnimateAdditions(List<T> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final T model = newModels.get(i);
            if (!dataSet.contains(model)) {
                addItem(i, model);
            }
        }
    }


    private void applyAndAnimateMovedItems(List<T> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final T model = newModels.get(toPosition);
            final int fromPosition = dataSet.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public void addItem(T item) {
        if (!dataSet.contains(item)) {
            dataSet.add(item);
            notifyItemInserted(dataSet.size() - 1);
        }
    }

    public void addItem(int position, T model) {
        dataSet.add(position, model);
        notifyItemInserted(position);
    }

    public void removeItem(T item) {
        int indexOfItem = dataSet.indexOf(item);
        if (indexOfItem != -1) {
            this.dataSet.remove(indexOfItem);
            notifyItemRemoved(indexOfItem);
        }
    }

    public T removeItem(int position) {
        final T model = dataSet.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void clearItem(){
        if (dataSet != null)
            dataSet.clear();
    }

    public void moveItem(int fromPosition, int toPosition) {
        final T model = dataSet.remove(fromPosition);
        dataSet.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public T getItem(int index) {
        if (dataSet != null && dataSet.get(index) != null) {
            return dataSet.get(index);
        } else {
            throw new IllegalArgumentException("Item with index " + index + " doesn't exist, dataSet is " + dataSet);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position)!=null? VIEW_ITEM: VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_ITEM){
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.cat_row, parent, false);
            vh = new EventViewHolder(itemView);
        }
        else if(viewType == VIEW_PROG){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);
            vh = new ProgressViewHolder(v);
        }else {
            throw new IllegalStateException("Invalid type, this type ot items " + viewType + " can't be handled");
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof EventViewHolder){
//            setFadeAnimation(holder.itemView);
            T dataItem = dataSet.get(position);
            ((EventViewHolder) holder).textView_business_name.setText(((Businesse)dataItem).getName().toString());
            String[] geoArray = ((Businesse)dataItem).getOfficeLocation().trim().split(",");
            double lat = Double.valueOf(geoArray[0]);
            double lng = Double.valueOf(geoArray[1]);
//            ((EventViewHolder) holder).textView_business_address.setText(GeoSearchModel.addressByLocation(new LatLng(lat,lng),mContext));
            getStringAdress(lat,lng,((EventViewHolder) holder).textView_business_address);
            ((EventViewHolder) holder).textView_business_category.setText(((Businesse)dataItem).getCategory().toString());
            Picasso.with(mContext)
                    .load(((Businesse)dataItem).getLogo())
                    .resize(75,75)
                    .centerInside()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.no_image_available)
                    .into(((EventViewHolder) holder).imageView_business_image);
        }else{
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        if (dataSet!=null)
            return dataSet.size();
        else
            return 0;
    }


    private void setFadeAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
          @Nullable
          @Bind(R.id.id_card_business_name)
          TextView textView_business_name;

          @Nullable
          @Bind(R.id.id_card_business_category)
          TextView textView_business_category;

          @Nullable
          @Bind(R.id.id_card_business_address)
          TextView textView_business_address;

          @Nullable
          @Bind(R.id.id_card_business_image)
          ImageView imageView_business_image;


          TextView textView_business_text_open;
        public EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try{
                if(null != myClickListener){
                  myClickListener.onItemClick(getLayoutPosition(), view);
                }else{
                    Toast.makeText(view.getContext(),"Click Event Null", Toast.LENGTH_SHORT).show();
                }
            }catch(NullPointerException e){
                Toast.makeText(view.getContext(),"Click Event Null Ex", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar)v.findViewById(R.id.progress_bar);
        }
    }


    /**
     * y Custom Item Listener
     */

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    private void getStringAdress(double lat, double lng, TextView targetView){
        FallbackReverseGeocodeObservable.createObservable(Locale.getDefault(),lat,lng,1)
                .map(new Func1<List<Address>, Address>() {
                    @Override
                    public Address call(List<Address> addresses) {
                        return addresses != null && !addresses.isEmpty() ? addresses.get(0) : null;
                    }
                })
                .map(new AddressToStringFunc())
                .subscribeOn(Schedulers.io())               // use I/O thread to query for addresses
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new DisplayTextOnViewAction(targetView),new ErrorHandler(mContext));
    }


}
