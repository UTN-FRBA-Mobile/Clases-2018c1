package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ModelViewHolder>{

    private List<Model> items;

    public ModelAdapter(List<Model> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ModelAdapter.ModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ModelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(ModelAdapter.ModelViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public class ModelViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private ImageView imageView;

        public ModelViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image_view);
            this.textView = itemView.findViewById(R.id.text_view);
        }

        public void bind(Model model) {
            textView.setText(model.getTextId());
            imageView.setImageResource(model.getImageId());
        }
    }

}
