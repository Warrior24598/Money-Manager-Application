package com.example.accounts;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionPageAdapter extends FragmentPagerAdapter
{
    List<Fragment> pageList = new ArrayList<>();
    List<String> pageTitles = new ArrayList<>();

    public SectionPageAdapter(FragmentManager fm)
    {
        super(fm);
    }

    public void addPage(Fragment page, String title)
    {
        pageList.add(page);
        pageTitles.add(title);
    }

    @Override
    public Fragment getItem(int position)
    {
        return pageList.get(position);
    }

    @Override
    public int getCount()
    {
        return pageList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return pageTitles.get(position);
    }


}
