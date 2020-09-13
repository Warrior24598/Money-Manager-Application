package com.example.accounts.recyclerviewadapters;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounts.R;
import com.example.accounts.SystemSingleTon;
import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.databaseService.IExpenseLimitService;
import com.example.accounts.models.ExpenseConfig;

import java.util.ArrayList;
import java.util.List;

public class AdapterExpenseLimitConfig extends RecyclerView.Adapter<AdapterExpenseLimitConfig.ViewHolder>
{
    private static final String TAG = "AdapterExpenseLimitConf";
    Context context;
    Boolean edit;

    SQLiteOpenHelper dbHandler;
    IExpenseLimitService expenseLimitService;

    List<ExpenseConfig> expenseConfigs;
    List<ViewHolder> viewHolders = new ArrayList<>();
    public AdapterExpenseLimitConfig(Context context)
    {
        this.context = context;
        this.edit = false;

        dbHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(context);
        expenseLimitService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createExpenseLimitService();
        this.expenseConfigs = expenseLimitService.getExpenseLimit();
    }

    public void setEditMode(boolean edit)
    {
        this.edit = edit;
        notifyDataSetChanged();
    }

    public void save()
    {
        for(ViewHolder holder: viewHolders)
        {
            int index = viewHolders.indexOf(holder);
            String limitString = holder.editLimit.getText().toString();

            if(limitString.equals(""))
            {
                continue;
            }
            ExpenseConfig config = expenseConfigs.get(index);
            config.setLimit(Float.parseFloat(limitString));

            expenseLimitService.updateExpenseLimit(config);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_expense_limit_config,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if(viewHolders.size()==expenseConfigs.size())
        {
            viewHolders.clear();
        }

        viewHolders.add(holder);

        ExpenseConfig config = expenseConfigs.get(position);

        holder.txtCategory.setText(config.getCategory().getName());
        holder.editLimit.setText(String.valueOf(config.getLimit()));
        holder.editLimit.setEnabled(this.edit);
    }

    @Override
    public int getItemCount()
    {
        return expenseConfigs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtCategory;
        EditText editLimit;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            editLimit = itemView.findViewById(R.id.editLimit);
        }
    }
}
