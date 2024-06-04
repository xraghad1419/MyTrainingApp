package com.example.mytraining;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

public class View_Offers_Adapter extends RecyclerView.Adapter<View_Offers_Adapter.MyViewHolder>{

    private ArrayList<TrainingOffer> list;
    private RecycleViewClickListener listener;

    DatabaseReference DB = FirebaseDatabase.getInstance().getReference().child("TrainingOffers");

    public View_Offers_Adapter(ArrayList<TrainingOffer>list, RecycleViewClickListener listener)
    {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_offers_card_holder,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.offer_name.setText(list.get(position).getOfferName());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.offer_name.getContext())
                        .setContentHolder(new ViewHolder(R.layout.edit_training_offer))
                        .setExpanded(true,1500)
                        .create();



                View view1 = dialogPlus.getHolderView();

                EditText Name = view1.findViewById(R.id.etFullName);
                EditText SD = view1.findViewById(R.id.editTextStartDate);
                EditText ED = view1.findViewById(R.id.editTextEndDate);
                EditText City = view1.findViewById(R.id.etCityOffer);
                EditText GPA = view1.findViewById(R.id.etGPA_Offer);
                EditText Major = view1.findViewById(R.id.etMajor);
                EditText Nationality = view1.findViewById(R.id.etNationality);
                EditText Desc = view1.findViewById(R.id.etDes);
                EditText Elig = view1.findViewById(R.id.etEligibility);
                EditText Ben = view1.findViewById(R.id.etBenefits);
                EditText Link = view1.findViewById(R.id.etlinkoffer);

                Button update = view1.findViewById(R.id.btUpdateeOffer);
                Button cancel = view1.findViewById(R.id.btCancelOffer);


                Name.setText(list.get(position).getOfferName());
                SD.setText(list.get(position).getStartDate());
                ED.setText(list.get(position).getEndDate());
                City.setText(list.get(position).getCity());
                GPA.setText(list.get(position).getRequiredGPA());
                Major.setText(list.get(position).getMajor());
                Nationality.setText(list.get(position).getRequiredNationality());
                Desc.setText(list.get(position).getOfferDescription());
                Elig.setText(list.get(position).getEligibility());
                Ben.setText(list.get(position).getBenefits());
                Link.setText(list.get(position).getLink());


                dialogPlus.show();

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DB.child(list.get(position).getId()).child("offerName").setValue(Name.getText().toString());
                        DB.child(list.get(position).getId()).child("startDate").setValue(SD.getText().toString());
                        DB.child(list.get(position).getId()).child("endDate").setValue(ED.getText().toString());
                        DB.child(list.get(position).getId()).child("city").setValue(City.getText().toString());
                        DB.child(list.get(position).getId()).child("requiredGPA").setValue(GPA.getText().toString());
                        DB.child(list.get(position).getId()).child("major").setValue(Major.getText().toString());
                        DB.child(list.get(position).getId()).child("requiredNationality").setValue(Nationality.getText().toString());
                        DB.child(list.get(position).getId()).child("offerDescription").setValue(Desc.getText().toString());
                        DB.child(list.get(position).getId()).child("eligibility").setValue(Elig.getText().toString());
                        DB.child(list.get(position).getId()).child("benefits").setValue(Ben.getText().toString());
                        DB.child(list.get(position).getId()).child("link").setValue(Link.getText().toString());

                        list.get(position).setOfferName(Name.getText().toString());
                        list.get(position).setStartDate(SD.getText().toString());
                        list.get(position).setEndDate(ED.getText().toString());
                        list.get(position).setCity(City.getText().toString());
                        list.get(position).setRequiredGPA(GPA.getText().toString());
                        list.get(position).setMajor(Major.getText().toString());
                        list.get(position).setRequiredNationality(Nationality.getText().toString());
                        list.get(position).setOfferDescription(Desc.getText().toString());
                        list.get(position).setEligibility(Elig.getText().toString());
                        list.get(position).setBenefits(Ben.getText().toString());
                        list.get(position).setLink(Link.getText().toString());

                        Toast.makeText(holder.offer_name.getContext(),"Data Updated successfully", Toast.LENGTH_SHORT).show();
                        dialogPlus.dismiss();

                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(holder.offer_name.getContext(),"No changes", Toast.LENGTH_SHORT).show();
                        dialogPlus.dismiss();
                    }
                });
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.offer_name.getContext());
                builder.setMessage("Are sure you want delete this offer?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DB.child(list.get(position).getId()).removeValue();
                        Toast.makeText(holder.offer_name.getContext(),"Delete it successfully", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.offer_name.getContext(),"No changes", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        holder.applicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Applied_Applicants.class);
                intent.putExtra("offer_ID",list.get(position).getId());
                view.getContext().startActivity(intent);
            }
        });

        holder.reviews_of_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), View_Offers_Reviews.class);
                intent.putExtra("offer_ID",list.get(position).getId());
                view.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface RecycleViewClickListener{
        void onClick(View v, int position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView offer_name;
        private ImageButton delete, edit, applicants, reviews_of_offers;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            offer_name = itemView.findViewById(R.id.training_offer_name);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
            applicants = itemView.findViewById(R.id.view_app);
            reviews_of_offers = itemView.findViewById(R.id.rev_offers);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }


    }


}
