package com.example.ipoet;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    Context context;
    List<String> names;
    String category;
   public MainAdapter(Context context, List<String> names,String category) {
       this.context = context;
       this.names=names;
       this.category=category;
   }

    @NonNull
    @Override
    public MainAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.item,viewGroup,false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.MainViewHolder mainViewHolder, int i) {
        mainViewHolder.title.setText(names.get(i));

    }

    @Override
    public int getItemCount()
    {
        if(names!=null)
        return names.size();
        else return  0;
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        public MainViewHolder(@NonNull final View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.main_text);

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    Intent intent=new Intent(context, PoemActivity.class);
                    intent.putExtra(context.getString(R.string.CATEGORY_KEY),category);
                    intent.putExtra(context.getString(R.string.POS_KEY),position);
                    intent.putExtra(context.getString(R.string.TITLE_KEY),names.get(position));
                    context.startActivity(intent);
                }
            });
        }
    }
}
