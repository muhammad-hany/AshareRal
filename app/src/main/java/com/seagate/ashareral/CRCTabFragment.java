package com.seagate.ashareral;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CRCTabFragment extends Fragment {

    boolean upComming;
    ArrayList<CRC> crcs;

    NavController navController;
    private ProgressBar progressBar;

    String adminAction;

    public CRCTabFragment() {
        // Required empty public constructor
    }

    public CRCTabFragment(boolean upComming,String adminAction){
        this.upComming=upComming;
        this.adminAction=adminAction;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crc_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        crcs=new ArrayList<>();
        navController= Navigation.findNavController(view);

        progressBar=getParentFragment().getView().findViewById(R.id.progress);
        RecyclerView recyclerView=view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL,true);
        recyclerView.setLayoutManager(layoutManager);
        CRCAdapter adapter=new CRCAdapter(crcs,v -> {
            CRCAdapter.CRCViewHolder holder= (CRCAdapter.CRCViewHolder) v.getTag();
            recyclerOnclick(holder.getAdapterPosition());
        });
        recyclerView.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child(Utils.CRC_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    CRC crc=data.getValue(CRC.class);
                    Date crcDate=getDate(crc.getDate());
                    crcDate.setTime(crcDate.getTime()+1000*60*60*24*crc.getPerioud());
                    Date now =new Date();
                    if (upComming) {
                        if (now.before(crcDate)) {
                            crcs.add(crc);
                        }
                    }else {
                        if (now.after(crcDate)){
                            crcs.add(crc);
                        }
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void recyclerOnclick(int position) {
        if (adminAction.equals(Utils.ACTION_EDIT)){
            Bundle bundle=new Bundle();
            bundle.putSerializable(Utils.CRC_KEY,crcs.get(position));
            bundle.putString(Utils.ADMIN_ACTION_KEY,Utils.ACTION_EDIT);
            navController.navigate(R.id.toCRCAdminFragment,bundle);
        }else if (adminAction.equals(Utils.ACTION_DELETE)){
            FirebaseDatabase.getInstance().getReference().child(Utils.CRC_KEY).child(String.valueOf(crcs.get(position).getTimestamp())).removeValue().addOnSuccessListener(aVoid -> {
                Toast.makeText(getContext(),"CRC deleted successfully",Toast.LENGTH_SHORT).show();
                navController.navigateUp();
            });
        }else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Utils.CRC_KEY, crcs.get(position));
            bundle.getString(Utils.ADMIN_ACTION_KEY, Utils.ACTION_VIEW);
            navController.navigate(R.id.toCRCFragment, bundle);
        }
    }



    public static Date getDate(String stringDate) {

        DateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {

            Date date = dateFormat.parse(stringDate);

            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;


    }
}
