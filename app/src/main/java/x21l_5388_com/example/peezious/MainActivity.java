package x21l_5388_com.example.peezious;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    TabLayout tabLayout;
    private ViewPager2 carouselViewPager;
    private CarouselAdapter carouselAdapter;
    private List<Integer> imageList = new ArrayList<>();
    private Handler handler = new Handler();
    private int currentItem = 0;
    ImageButton searchButton, cartButton , accountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        viewPager2 = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);
        viewPager2.setAdapter(new OrderPagerAdapter(this));
        TabLayoutMediator tlm = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                    {
                        tab.setText("Pizza");
                        break;
                    }
                    case 1:
                    {
                        tab.setText("Burger");
                        break;
                    }
                    case 2:
                    {
                        tab.setText("Pasta");
                        break;
                    }
                    case 3:
                    {
                        tab.setText("Others");
                        break;
                    }
                }
            }
        });
        tlm.attach();
        imageList.add(R.drawable.image1);
        imageList.add(R.drawable.image2);
        imageList.add(R.drawable.image3);
        carouselViewPager = findViewById(R.id.carouselViewPager);
        carouselAdapter = new CarouselAdapter(this, imageList);
        carouselViewPager.setAdapter(carouselAdapter);
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (currentItem == imageList.size()) {
                    currentItem = 0;
                }
                carouselViewPager.setCurrentItem(currentItem++, true);
                handler.postDelayed(this, 3000); // Slide every 3 seconds
            }
        };
        handler.postDelayed(runnable, 3000); // Start after 3 seconds

        searchButton = findViewById(R.id.searchButton);
        cartButton = findViewById(R.id.cartButton);
        accountButton = findViewById(R.id.accountButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(MainActivity.this, SearchView.class);
                startActivity(I);
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(MainActivity.this, CartActivity.class);
                startActivity(I);
            }
        });

        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(MainActivity.this, AccountSettings.class);
                startActivity(I);
            }
        });


    }
}