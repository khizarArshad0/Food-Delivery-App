package x21l_5388_com.example.peezious;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private Context context;
    private List<String> addressList;

    public AddressAdapter(Context context, List<String> addressList) {
        this.context = context;
        this.addressList = addressList;
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_card, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {
        String address = addressList.get(position);
        holder.addressTextView.setText(address);
        DBHelper dbHelper = new DBHelper(context);
        holder.deleteAddress.setOnClickListener(v -> {
            dbHelper.removeAddress(address);
            addressList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Address deleted", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public void updateAddressList(List<String> newAddressList) {
        this.addressList = newAddressList;
        notifyDataSetChanged(); // Refresh RecyclerView
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView addressTextView;
        ImageButton deleteAddress;

        public AddressViewHolder(View itemView) {
            super(itemView);
            addressTextView = itemView.findViewById(R.id.address_title);
            deleteAddress = itemView.findViewById(R.id.delete_address);
        }
    }
}
