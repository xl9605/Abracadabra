package ac.ict.humanmotion.abracadabra.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * Project AndroidCA.
 * Created by 旭 on 2017/5/27.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter<T>.BaseViewHolder> {

    protected Context context;

    public T getMyDataAt(int position) {
        return myData.get(position);
    }

    protected List<T> myData;

    public void setItemOnclickListener(ItemOnclickListener itemOnclickListener) {
        this.itemOnclickListener = itemOnclickListener;
    }

    private ItemOnclickListener itemOnclickListener;

    BaseAdapter(Context context) {
        this.context = context;
    }

    public void setMyData(List<T> myData) {
        this.myData = myData;
        notifyDataSetChanged();
    }

    public void updateMyData(List<T> myData) {
        this.myData.addAll(myData);
        notifyDataSetChanged();
    }

    public interface ItemOnclickListener {
        void onItemClick(View v, int pos);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return myData == null ? 0 : myData.size();
    }

    public abstract int getLayoutId();

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> mViewMap;

        BaseViewHolder(View itemView) {
            super(itemView);
            mViewMap = new SparseArray<>();
            //itemView.setOnClickListener(v -> itemOnclickListener.onItemClick(v, getLayoutPosition()));
        }

        View getView(int id) {
            View view = mViewMap.get(id);
            if (view == null) {
                view = itemView.findViewById(id);
                mViewMap.put(id, view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemOnclickListener.onItemClick(v, getLayoutPosition());
                    }
                });
            }
            return view;
        }
    }
}
