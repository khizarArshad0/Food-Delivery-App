package x21l_5388_com.example.peezious;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.userId.setText("User ID: " + order.getUserId());
        holder.phone.setText("Phone: " + order.getPhone());
        holder.address.setText("Address: " + order.getAddress());
        holder.status.setText("Status: " + order.getStatus());
        holder.orderDetails.setText("Details: " + order.getOrderDetails());
        holder.totalPrice.setText("Total Price: Rs. " + order.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        public TextView userId, phone, address, status, orderDetails, totalPrice;

        public OrderViewHolder(View view) {
            super(view);
            userId = view.findViewById(R.id.cart_item_user_id);
            phone = view.findViewById(R.id.cart_item_phone);
            address = view.findViewById(R.id.cart_item_address);
            status = view.findViewById(R.id.cart_item_status);
            orderDetails = view.findViewById(R.id.cart_item_order_details);
            totalPrice = view.findViewById(R.id.cart_item_total_price);
        }
    }
}
