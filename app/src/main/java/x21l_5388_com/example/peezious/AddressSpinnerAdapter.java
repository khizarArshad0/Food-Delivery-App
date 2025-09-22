package x21l_5388_com.example.peezious;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import android.widget.ArrayAdapter;

public class AddressSpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> addresses;

    public AddressSpinnerAdapter(@NonNull Context context, @NonNull List<String> addresses) {
        super(context, android.R.layout.simple_spinner_item, addresses);
        this.context = context;
        this.addresses = addresses;
    }

    @Override
    public int getCount() {
        return addresses != null ? addresses.size() : 0;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return addresses != null && position < addresses.size() ? addresses.get(position) : null;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(getItem(position));
        textView.setTextSize(16); // Adjust text size
        textView.setPadding(8, 8, 8, 8);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(getItem(position));
        textView.setTextSize(14); // Adjust text size for dropdown items
        textView.setPadding(8, 8, 8, 8);

        return convertView;
    }
}
