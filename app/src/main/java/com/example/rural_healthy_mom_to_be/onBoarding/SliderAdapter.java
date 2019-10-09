package com.example.rural_healthy_mom_to_be.onBoarding;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rural_healthy_mom_to_be.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class SliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

//    private TextView slideHeading, slideDescription;
//    private ImageView slide_imageView;


    public SliderAdapter(Context context) {

        this.context = context;
    }

    // img Array
    public int[] image_slide ={
            R.drawable.weighttrack,
            R.drawable.report,
            R.drawable.fooddiary
    };

    // heading Array
    public String[] heading_slide ={
            "Track Weight",
            "Generate Report",
            "Food Diary"
    };

    // description Array
    public String[] description_slide ={
            "You can track your weight and maintain weight log during different stages of pregnancy and compare your weight gain with recommended maximum and minimum  weight gain with help of a graph.",
            "You can generate PDF report containing your weight information before pregnancy, weight log during maternity and weight gain graph during all the weeks of pregnancy.",
            "This application lets you maintain a log of food item and quantity that you ate at your selected time and count calories and other nutrients intake during pregnancy."
    };




    @Override
    public int getCount() {

        return heading_slide.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container,false);
        container.addView(view);

//        ImageView slide_imageView = view.findViewById(R.id.imageView1);
        CircleImageView slide_imageView = view.findViewById(R.id.imageView1);

        TextView slideHeading = view.findViewById(R.id.tvHeading);
        TextView  slideDescription = view.findViewById(R.id.tvDescription);

        slide_imageView.setImageResource(image_slide[position]);
        slideHeading.setText(heading_slide[position]);
        slideDescription.setAllCaps(false);
        slideDescription.setText(description_slide[position]);
        slideDescription.setTextColor(Color.parseColor("#000000"));
        slideDescription.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        slideHeading.setTextColor(Color.parseColor("#000000"));

        return view;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }

//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        View view = (View) object;
//        container.removeView(view);
//    }

}
