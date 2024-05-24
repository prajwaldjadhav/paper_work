package com.templatemela.camscanner.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.templatemela.camscanner.R;
import com.templatemela.camscanner.activity.MainActivity;
import com.templatemela.camscanner.main_utils.Constant;
import com.templatemela.camscanner.models.DrawerModel;

import java.util.ArrayList;

public class DrawerItemAdapter extends BaseAdapter {

    public Activity activity;
    private ArrayList<DrawerModel> arrayList;

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    public DrawerItemAdapter(Activity activity2, ArrayList<DrawerModel> arrayList2) {
        activity = activity2;
        arrayList = arrayList2;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    //
    public class ViewHolder {
        private ImageView iv_item_icon;
        private TextView tv_item_name;
        //older version of switch
        private SwitchCompat switchNightMode;

        public ViewHolder() {
        }
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        View inflate = LayoutInflater.from(activity).inflate(R.layout.drawer_list_item, viewGroup, false);


        viewHolder.iv_item_icon = (ImageView) inflate.findViewById(R.id.iv_item_icon);
        viewHolder.tv_item_name = (TextView) inflate.findViewById(R.id.tv_item_name);
        viewHolder.switchNightMode = (SwitchCompat) inflate.findViewById(R.id.switchNightMode);

        //if the list item is dark theme() make the switch visible
        if (arrayList.get(i).getItem_name().equalsIgnoreCase(activity.getResources().getString(R.string.darkTheme))) {
            viewHolder.switchNightMode.setVisibility(View.VISIBLE);
            initTheme(viewHolder.switchNightMode);
        } else {
            viewHolder.switchNightMode.setVisibility(View.GONE);
        }

        viewHolder.iv_item_icon.setImageResource(arrayList.get(i).getItem_icon());
        viewHolder.tv_item_name.setText(arrayList.get(i).getItem_name());

        //setting a onclick listner on list item
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) activity).onDrawerItemSelected(i);
            }
        });

        //adding a listner to the switch
        viewHolder.switchNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.PREFS_NAME, Context.MODE_PRIVATE);
                if (isChecked) {
                    //storing a preference to the shared preference
                    sharedPreferences.edit().putInt(Constant.KEY_THEME, Constant.THEME_DARK).apply();

                    setTheme(AppCompatDelegate.MODE_NIGHT_YES, Constant.THEME_DARK);
                } else {
                    sharedPreferences.edit().putInt(Constant.KEY_THEME, Constant.THEME_LIGHT).apply();
                    setTheme(AppCompatDelegate.MODE_NIGHT_NO, Constant.THEME_LIGHT);
                }
            }
        });
        return inflate;
    }

    //initialize the switch by getting the preference from shared preference
    private void initTheme(SwitchCompat switchNightMode) {
        int savedTheme = getSavedTheme();
        if (savedTheme == Constant.THEME_LIGHT) {
            switchNightMode.setChecked(false);
//            setTheme(AppCompatDelegate.MODE_NIGHT_NO,Constant.THEME_LIGHT);
        } else if (savedTheme == Constant.THEME_DARK) {
            switchNightMode.setChecked(true);
//            setTheme(AppCompatDelegate.MODE_NIGHT_YES,Constant.THEME_DARK);
        }
    }

    //get saved theme
    private int getSavedTheme() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(Constant.KEY_THEME, Constant.THEME_UNDEFINED);
    }


    ///save theme in shared preference
    public void saveTheme(int theme) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.PREFS_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(Constant.KEY_THEME, theme).apply();
    }



    //set theme using AppCompactDelegate
    private void setTheme(int themeMode, int prefsMode) {
        AppCompatDelegate.setDefaultNightMode(themeMode);
        saveTheme(prefsMode);
    }
}