package com.example.googleplay;

import android.Manifest;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Toast;

import com.example.googleplay.fragment.BaseFragment;
import com.example.googleplay.fragment.FragmentFactory;
import com.example.googleplay.service.NetStateService;
import com.example.googleplay.utils.UIUtils;

public class MainActivity extends AppCompatActivity {
    /*toolbar*/
    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ViewPager mPagerContent;
    private TabLayout mTabs;

    private String[] mTabList;

    public MyServiceConnection connection;
    private CoordinatorLayout content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        MyApplication.addActivity(this);
        UIUtils.getPreferences("appConfig").edit().clear().commit();
        //初始化标题与侧滑菜单
        initToolBarOrDrawer();
        //初始化view
        initView();
        requsetPermission();
        //初始化数据
        //initData();
        Intent intent = new Intent();
        intent.setClass(this, NetStateService.class);
        startService(intent);
        connection = new MyServiceConnection();
        bindService(intent, connection, BIND_AUTO_CREATE);

    }

    /**
     * 初始化ToolBar于侧滑菜单
     */
    private void initToolBarOrDrawer() {
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_Sliding);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);//设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_black_24dp);

        //设置监听侧滑菜单的打开于关闭
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);//将ActionBarDrawerToggle设置给侧滑菜单
        content = (CoordinatorLayout) findViewById(R.id.content);
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigationView);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Snackbar.make(content, "Pressed" + item.getTitle(), Snackbar.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.i("MainActivity","ToolBat高度:"+toolbar.getHeight()+";"+toolbar.getMeasuredHeight());
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;
        float density = dm.density;
        int dpi = dm.densityDpi;
        Log.w("MainActivity","分辨率:"+heightPixels+"x"+widthPixels+";屏幕密度:"+density+";Dpi:"+dpi);

    }

    /**
     * 初始化View控件
     */
    private void initView() {
        mPagerContent = (ViewPager) findViewById(R.id.pager_Content);
        mTabs = (TabLayout) findViewById(R.id.tab_Pager);
        mTabs.setTabMode(TabLayout.MODE_FIXED);
        mTabs.setTabTextColors(UIUtils.getColorSelector(R.color.tab_color_selector));

    }

    private void initData() {
        /**Tab信息*/
        mTabList = getResources().getStringArray(R.array.tab_list);

        //  mPagerContent.setOffscreenPageLimit(5);
        mPagerContent.setAdapter(new PagerContentAdapter(getSupportFragmentManager()));
        mTabs.setupWithViewPager(mPagerContent);

        mPagerContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                BaseFragment fragment = FragmentFactory.createFragment(position);
                Bundle bundle = fragment.getArguments();
                /*防止来回滑动页面,重复清除数据*/
                if (bundle != null) {
                    int pageType = bundle.getInt("pageType");
                    boolean isFirstLoadData = UIUtils.getPreferences("appConfig").getBoolean("isFirstLoadData" + pageType, false);
                    Log.i("onPageSelected", "isFirstLoadData" + pageType + ":" + isFirstLoadData);
                    if (!isFirstLoadData) {
                        fragment.loadData();//请求数据
                        UIUtils.getPreferences("appConfig").edit().putBoolean("isFirstLoadData" + pageType, true).commit();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        //返回此id菜单项
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(search);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);

//        MenuItemCompat.setOnActionExpandListener(search, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                return false;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                return false;
//            }
//        });

//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //设置点击菜单,打开抽屉
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    /**
     * ViewPager 页卡适配器
     */
    class PagerContentAdapter extends FragmentPagerAdapter {
        public PagerContentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            BaseFragment fragment = FragmentFactory.createFragment(position);

            return fragment;
        }

        @Override
        public int getCount() {
            return mTabList.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            Log.e("main", mTabList.toString());

            return mTabList[position];
        }
    }

    /**
     * 绑定服务,实现实时监听网络逻辑
     */
    class MyServiceConnection implements ServiceConnection {

        private Snackbar snackbar;

        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            //获取binder对象
            NetStateService.MyBinder binder = (NetStateService.MyBinder) service;
            //使用binder里面方法获取service实例 设置监听
            binder.getService().setOnNetStateListener(new NetStateService.OnNetStateListener() {
                @Override
                public void onNetState(boolean state) {
                    if (!state) {
                        snackbar = Snackbar.make(content, "网络连接不可用!", Snackbar.LENGTH_INDEFINITE);
                        Log.i("MainActivity", "网络连接不可用");
                        snackbar.setAction("关闭", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        });
                        snackbar.show();

                    } else {
                        Log.i("MainActivity", "网络连接可用");
                    }
                }
            });

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            exitTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
        } else {
            MyApplication.removeActivity();
            //System.exit(0);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, NetStateService.class);
        stopService(intent);
        unbindService(connection);
        Log.e("onDestroy", "程序销毁了");

        /*将保存的第一次请求数据标识全部清除,避免下次启动没有请求数据*/
 /*       for (int i = 0; i < mTabList.length; i++) {
            UIUtils.getPreferences("appConfig").edit().remove("isFirstLoadData" + i).commit();
            Log.e("onDestroy", "isFirstLoadData" + i+":被清除");
        }*/

    }


    /**
     * 请求权限
     */
    private void requsetPermission() {

/** 判断应用是否已经获取了权限*/

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) { //判断当前Activity是否已经获取了权限,如果没有获取,就请求权限

            //如果app的权限曾经被用户拒绝过,就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(this, "本次操作需要您授予权限!", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new
                                String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

            } else {//如果没有获取权限,就进行权限请求

//进行权限请求
                ActivityCompat.requestPermissions(this, new
                                String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }

        } else {//如果已经获取权限,就读取文件
            initData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1:
                initData();
                Log.i("MainActivity", "权限请求回来:" + permissions[0] + ";" + permissions.length + ";grantResults" + grantResults[0]);
                break;

        }

    }
}
