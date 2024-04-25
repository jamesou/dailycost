package com.jamesou.dailycost;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.jamesou.dailycost.adapter.AccountAdapter;
import com.jamesou.dailycost.db.AccountBean;
import com.jamesou.dailycost.db.DBManager;
import com.jamesou.dailycost.dialog.BudgetDialog;
import com.jamesou.dailycost.utils.FormatNumberUtil;
import com.jamesou.dailycost.utils.PromptMsgUtil;

/**
 * @Author: Jamesou
 * @Date: 7/04/24 01:44
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView todayLv;
    ImageView searchIv;
    Button btn_add;
    ImageButton btn_more;
    List<AccountBean> mDatas;
    AccountAdapter adapter;
    int year, month, day;

    SharedPreferences preferences;
    int MENU_UPDATE = 0;
    int MENU_DELETE = 1;
    DrawerLayout drawerLayout;
    LinearLayout recordlayout,analysislayout,settinglayout,helplayout,aboutlayout;

    // main top elements
    View headerView = null;
    TextView topExpenseTv, topIncomeTv, topBudgetTv, topBudgetTextTv,topDailyTv,topAnalysisTv;
    ImageView topHideIv;

    String PROMPT_MSG="Feature development in progress, stay tuned!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        // create a preferences to store share data
        preferences = getSharedPreferences("budget" , Context.MODE_PRIVATE);
        addLVHeaderView();
        mDatas = new ArrayList<>();
        // add adapter to store the data showing on the list
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);
    }

    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        btn_add = findViewById(R.id.main_btn_add);
        btn_more = findViewById(R.id.main_btn_more);
        searchIv = findViewById(R.id.main_iv_search);

        //left menu
        recordlayout = findViewById(R.id.dialog_more_btn_record);
        analysislayout = findViewById(R.id.dialog_more_btn_ana);
        settinglayout = findViewById(R.id.dialog_more_btn_setting);
        helplayout = findViewById(R.id.dialog_more_btn_help);
        aboutlayout = findViewById(R.id.dialog_more_btn_about);

        recordlayout.setOnClickListener(this);
        analysislayout.setOnClickListener(this);
        settinglayout.setOnClickListener(this);
        helplayout.setOnClickListener(this);
        aboutlayout.setOnClickListener(this);

        btn_more.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        //long press list view will trigger onCreateContextMenu function
        registerForContextMenu(todayLv);
        //set the finger scroll screen
        setScrollListener();

        //define main layout as drawerLayout for implementing left menu sliding effect
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    //implement the back button function
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, MENU_UPDATE, 0, "Modify Record");
        menu.add(0, MENU_DELETE, 0, "Delete Record");
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo cmi=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        //selected item
        int posMenu=cmi.position;
        //getItem from adapter list
        AccountBean bean=(AccountBean) adapter.getItem(posMenu-1);
        switch(item.getItemId()){
            case 0:
                ArrayList<String> stringList = new ArrayList<>();
                stringList.add(bean.getId()+"");
                stringList.add(bean.getCategoryName()+"");
                stringList.add(bean.getsImageId()+"");
                stringList.add(bean.getComment()==null?"":bean.getComment());
                stringList.add(bean.getMoney()+"");
                stringList.add(bean.getTime()+"");
                stringList.add(bean.getYear()+"");
                stringList.add(bean.getMonth()+"");
                stringList.add(bean.getDay()+"");
                stringList.add(bean.getKind()+"");
                stringList.add("recode");   //the jump direction of page
                Intent intent = new Intent();
                intent.setClass(this, NewEntryActivity.class);
                intent.putStringArrayListExtra("RecordInfo", stringList);
                startActivity(intent);
                break;
            case 1:
                showDeleteItemDialog(bean);
                break;
        }
        return super.onContextItemSelected(item);
    }


    private void setScrollListener() {
        //scroll list view
        todayLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // rapidly scroll screen
                    case SCROLL_STATE_FLING:
                        btn_add.setVisibility(View.INVISIBLE);
                        break;
                    //stop scroll, the new entry button visible
                    case SCROLL_STATE_IDLE:
                        btn_add.setVisibility(View.VISIBLE);
                        break;
                    //touch screen to scroll
                    case SCROLL_STATE_TOUCH_SCROLL:
                        btn_add.setVisibility(View.INVISIBLE);
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void showDeleteItemDialog(final AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message").setMessage("Confirm deletion?")
                .setNegativeButton("Cancel" , null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int click_id = clickBean.getId();
                        //Delete record in local database
                        DBManager.deleteItemFromAccounttbById(click_id);
                        mDatas.remove(clickBean);
                        adapter.notifyDataSetChanged(); // refresh list view
                        setTopTvShow(); // refresh display amount shown on the top view
                    }
                });
        builder.create().show();
    }


    private void addLVHeaderView() {
        //add a view, which is on the top of list view
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);
        // init top view
        topExpenseTv = headerView.findViewById(R.id.item_mainlv_top_tv_expense_value);
        topIncomeTv = headerView.findViewById(R.id.item_mainlv_top_tv_income_value);
        topBudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget_value);
        topBudgetTextTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget_text);

        topDailyTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topHideIv = headerView.findViewById(R.id.item_mainlv_top_iv_hide);
        topAnalysisTv = headerView.findViewById(R.id.item_mainlv_top_analysis_text);

        //no need to register the whole view instead of registering item_mainlv_top_analysis_text
//        headerView.setOnClickListener(this);
        topBudgetTextTv.setOnClickListener(this);
        topAnalysisTv.setOnClickListener(this);
        topBudgetTv.setOnClickListener(this);
        topHideIv.setOnClickListener(this);
    }

    /**
     * Get current date
     */
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    // main activity is activated ,refresh data
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }

    /*Set the top view data*/
    private void setTopTvShow() {
        float incomeOneDay = DBManager.setSumMoneyOneDay(year, month, day, 1);
        float outcomeOneDay = DBManager.setSumMoneyOneDay(year, month, day, 0);
        String dailycost = "Today's expense: $" + FormatNumberUtil.formatFloat(outcomeOneDay) + " income: $" + FormatNumberUtil.formatFloat(incomeOneDay);
        topDailyTv.setText(dailycost);
        // monthly cost
        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        topIncomeTv.setText("$" + FormatNumberUtil.formatFloat(incomeOneMonth));
        topExpenseTv.setText("$" + FormatNumberUtil.formatFloat(outcomeOneMonth));
        // display budget
        float bmoney = preferences.getFloat("bmoney", 0);
        if(bmoney == 0){
            topBudgetTv.setText("$ " + 0);
        }else{
            float syMoney = bmoney - outcomeOneMonth;
            topBudgetTv.setText("$" + FormatNumberUtil.formatFloat(syMoney));
        }

    }

    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneMonthFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();;
        switch (v.getId()) {
            case R.id.main_iv_search:
//                intent = new Intent(this, SearchActivity.class);
//                startActivity(intent);
                PromptMsgUtil.promptMsg(getApplicationContext(),PROMPT_MSG);
                break;
            case R.id.main_btn_add:
                //@todo  add scan button
                intent = new Intent(this, NewEntryActivity.class);
                startActivity(intent);
                break;
            case R.id.main_btn_more:
                //show left menu
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.item_mainlv_top_tv_budget_value:
                showBudgetDialog();
                break;
            case R.id.item_mainlv_top_tv_budget_text:
                showBudgetDialog();
                break;
            case R.id.item_mainlv_top_iv_hide:
                // set the money visible or invisible
                toggleShow();
                break;
            case R.id.dialog_more_btn_record:
                PromptMsgUtil.promptMsg(getApplicationContext(),PROMPT_MSG);
                break;
            case R.id.item_mainlv_top_analysis_text:
                PromptMsgUtil.promptMsg(getApplicationContext(),PROMPT_MSG);
                break;
            case R.id.dialog_more_btn_setting:
                PromptMsgUtil.promptMsg(getApplicationContext(),PROMPT_MSG);
                break;
            case R.id.dialog_more_btn_help:
                PromptMsgUtil.promptMsg(getApplicationContext(),PROMPT_MSG);
                break;
            case R.id.dialog_more_btn_about:
                PromptMsgUtil.promptMsg(getApplicationContext(),PROMPT_MSG);
                break;
        }
    }


    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                SharedPreferences.Editor edit = preferences.edit();
                edit.putFloat("bmoney" , money);
                edit.commit();
                //calculate the balance , use budget minus monthly expense
                float monthlyExpense = DBManager.getSumMoneyOneMonth(year, month, 0);
                float balance = money - monthlyExpense;
                topBudgetTv.setText("$ " + balance);
            }
        });
    }

    /**
     * set the money visible or invisible
     */
    boolean isShow = true;

    private void toggleShow() {
        if (isShow) {
            PasswordTransformationMethod transformationMethod = PasswordTransformationMethod.getInstance();
            topExpenseTv.setTransformationMethod(transformationMethod);
            topIncomeTv.setTransformationMethod(transformationMethod);
            topBudgetTv.setTransformationMethod(transformationMethod);
            topHideIv.setImageResource(R.mipmap.ih_hide);
            isShow = false;
        } else {

            HideReturnsTransformationMethod returnsTransformationMethod = HideReturnsTransformationMethod.getInstance();
            topExpenseTv.setTransformationMethod(returnsTransformationMethod);
            topIncomeTv.setTransformationMethod(returnsTransformationMethod);
            topBudgetTv.setTransformationMethod(returnsTransformationMethod);
            topHideIv.setImageResource(R.mipmap.ih_show);
            isShow = true;
        }
    }
}
