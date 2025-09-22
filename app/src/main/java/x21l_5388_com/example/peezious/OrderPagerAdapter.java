package x21l_5388_com.example.peezious;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrderPagerAdapter extends FragmentStateAdapter {
    public OrderPagerAdapter(@NonNull FragmentActivity activity1){
        super(activity1);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position){
        if (position ==0){
            return new PizzaFragment();
        }
        else if (position ==1){
            return new BurgerFragment();
        }
        else if (position ==2){
            return new PastaFragment();
        }
        else{
            return new OthersFragment();
        }

    }

    @Override
    public int getItemCount(){
        return 4;
    }


}
