package comq.mostafa.fci.pets.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import comq.mostafa.fci.pets.R;

public class PetsAdapter extends ArrayAdapter<Pet> {


    public PetsAdapter(@NonNull Context context, ArrayList<Pet> pets) {
        super(context, R.layout.item_pet, pets);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_pet, parent, false);
        }

        final Pet pet = getItem(position);
        TextView nameTextView  = listItemView.findViewById(R.id.name_textView);
        TextView breedTextView = listItemView.findViewById(R.id.breed_textView);

        nameTextView.setText(pet.getName());
        breedTextView.setText(pet.getBreed());

        return listItemView;
    }
}
