package com.example.mytraining;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Applied_Adapter extends RecyclerView.Adapter<Applied_Adapter.MyViewHolder>{

    float rateValue;
    String orgID;
    ArrayList<TrainingOffer> list;
    private RecycleViewClickListener listener;
    String name;

    DatabaseReference DB = FirebaseDatabase.getInstance().getReference().child("Reviews");
    DatabaseReference tr_DB = FirebaseDatabase.getInstance().getReference().child("TrainingOffers");


    String id_app;


    public Applied_Adapter(ArrayList<TrainingOffer>list, RecycleViewClickListener listener,String id, String name)
    {
        this.list = list;
        this.listener = listener;
        this.id_app = id;
        this.name = name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applied_offers_for_applicant_ch,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.offerName.setText(list.get(position).getOfferName());
        Glide.with(holder.offer_img.getContext()).load(list.get(position).getImageUrl()).placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop().error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.offer_img);

        holder.rate.setVisibility(View.INVISIBLE);

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");// 05/06/2022
        String Date = sdf.format(new Date());
        System.out.println("****************************************** Current Date "+Date);

        String mm = Date.substring(0,2);
        int int_month = Integer.parseInt(mm);

        String dd = Date.substring(3,5);
        int int_day = Integer.parseInt(dd);

        String yyyy = Date.substring(6,10);

        //05-09-2022
        //0123456789

        System.out.println("******************************* current mm "+mm);
        System.out.println("******************************* current month integer "+int_month);

        System.out.println("******************************* current dd "+dd);
        System.out.println("******************************* current day integer "+int_day);

        System.out.println("******************************* current yyyy "+yyyy);

        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ Break ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        String end_Date = list.get(position).getEndDate();

        String end_Month = end_Date.substring(0,2);
        int int_endMonth = Integer.parseInt(end_Month);

        String end_Day = end_Date.substring(3,5);
        int int_endDay = Integer.parseInt(end_Day);

        String end_Year = end_Date.substring(6,10);


        System.out.println("-----------------------           end_Month           --------------- "+end_Month);
        System.out.println("-----------------------             integer end_Month           --------------- "+int_endMonth);

        System.out.println("--------------          end_Day          ------------------ "+end_Day);
        System.out.println("-----------------------        integer    end day           --------------- "+int_endDay);

        System.out.println("--------------------        end_Year         -------------------- "+end_Year);

        if (yyyy.equals(end_Year)){
            System.out.println("-------------------------- Year Matched -------------------------");
              //      8             5
            if (int_month >= int_endMonth){
                System.out.println("-------------------------- Month Matched -------------------------");

                if(int_day >= int_endDay){
                    holder.rate.setVisibility(View.VISIBLE);
                    System.out.println("-------------------------- Open Rate -------------------------");
                    holder.rate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            final DialogPlus dialogPlus = DialogPlus.newDialog(holder.offerName.getContext())
                                    .setContentHolder(new ViewHolder(R.layout.rate_card_holder))
                                    .setExpanded(true,1600)
                                    .create();

                            Calendar calendar = Calendar.getInstance();
                            String currentDate = DateFormat.getDateInstance(DateFormat.MONTH_FIELD).format(calendar.getTime());


                            View view1 = dialogPlus.getHolderView();

                            TextView rateCount = view1.findViewById(R.id.tx);
                            EditText review = view1.findViewById(R.id.reviewBtn);
                            RatingBar ratingBar = view1.findViewById(R.id.ratingBar);
                            CheckBox check = view1.findViewById(R.id.check);

                            Button submit = view1.findViewById(R.id.submit);
                            Button cancel = view1.findViewById(R.id.cancelBtn);
                            dialogPlus.show();

                            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                @Override
                                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                                    rateValue = ratingBar.getRating();

                                    if(rateValue <= 1 && rateValue >0)
                                        rateCount.setText("Bad " + rateValue +"/5");

                                    else if(rateValue <= 2 && rateValue >1)
                                        rateCount.setText("OK " + rateValue +"/5");

                                    else if(rateValue <= 3 && rateValue >2)
                                        rateCount.setText("Good " + rateValue +"/5");

                                    else if(rateValue <= 4 && rateValue >3)
                                        rateCount.setText("Very Good " + rateValue +"/5");

                                    else if(rateValue <= 5 && rateValue >4)
                                        rateCount.setText("Best " + rateValue +"/5");
                                }
                            });

                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String rev = review.getText().toString();
                                    String rate_bar = String.valueOf(rateValue);

                                    tr_DB.child(list.get(position).getId()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            orgID = snapshot.child("organizationID").getValue().toString();

                                            //String id, applicantID, trainingID, applicantName, review, ratingStar,orgID, rate_date;

                                            if(check.isChecked()){
                                                // show name of applicant
                                                String key = DB.push().getKey();
                                                Review r = new Review(key,id_app,list.get(position).getId(),name,rev,rate_bar,orgID,currentDate);
                                                DB.child(key).setValue(r);
                                            }
                                            else{
                                                // Don't name of applicant
                                                String key = DB.push().getKey();
                                                Review r = new Review(key,id_app,list.get(position).getId(),"Anonymous",rev,rate_bar,orgID,currentDate);
                                                DB.child(key).setValue(r);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    Toast.makeText(holder.offerName.getContext(),"Thanks for response", Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                }
                            });

                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(holder.offerName.getContext(),"Rating is canceled", Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                }
                            });
                        }
                    });
                }
                else{
                    System.out.println("-------------------------- Day not yet -------------------------");

                }

            }
            else{
                System.out.println("-------------------------- Month Not yet  -------------------------");
            }

        }
        else{
            System.out.println("-------------------------- Year Not Yet -------------------------");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface RecycleViewClickListener{
        void onClick(View v, int position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView offerName;
        private ImageView offer_img;
        private Button rate;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            offerName = itemView.findViewById(R.id.training_name);
            offer_img = itemView.findViewById(R.id.image_offer);
            rate = itemView.findViewById(R.id.rate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }

    }


}
