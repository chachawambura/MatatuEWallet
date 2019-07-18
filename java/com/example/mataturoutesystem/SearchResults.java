package com.example.mataturoutesystem;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mataturoutesystem.Models.Matatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResults extends AppCompatActivity {
    String searchItem = StaticStrings.staticSearchItem;
    String searchURL = "https://www.fuelpap.co.ke/mataturoutesystem/matatupostsearched.php";
    String searchJSONObject;
    String TAG = "seearch";
    List <Matatus>searchedMatatusList;
    RecyclerView matatuRV;
    Context c;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listofitems);
        searchedMatatusList = new ArrayList<Matatus>();
        matatuRV = findViewById(R.id.searchRV);
        postAndGetSearchItems();
    }

    public void postAndGetSearchItems(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, searchURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                searchJSONObject = response;
                Log.d(TAG, searchJSONObject);
                new ParseJSONDataClass(SearchResults.this).execute();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchResults.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map <String, String> params = new HashMap<>();
                params.put("search", searchItem);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SearchResults.this);
        requestQueue.add(stringRequest);


    }

    public class ParseJSONDataClass extends AsyncTask <Void, Void, Void> {
        public Context context;
        public ParseJSONDataClass(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(searchJSONObject != null){
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(searchJSONObject);
                    JSONObject jsonObject;
                    Matatus matatus;
                    for (int i = 0; i < jsonArray.length(); i++){
                        matatus = new Matatus();
                        jsonObject = jsonArray.getJSONObject(i);
                        matatus.setRegNumber(jsonObject.getString("regNum"));
                        matatus.setPrice(jsonObject.getString("price"));
                        searchedMatatusList.add(matatus);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            if(searchedMatatusList != null){
                Log.d(TAG, "not null");
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
                matatuRV.setLayoutManager(linearLayoutManager);
                matatuRV.setHasFixedSize(true);
                MatatusItemsAdapter matatusItemsAdapter = new MatatusItemsAdapter(c);
                matatuRV.setAdapter(matatusItemsAdapter);
            }

        }
    }

    public class MatatusItemsAdapter extends RecyclerView.Adapter<MatatusItemsAdapter.VersionViewHolder>{
        private List<Matatus> searchMatatusList;
        private Context context;

        public MatatusItemsAdapter(Context context){
            this.context = context;
            searchMatatusList = searchedMatatusList;
        }



        @NonNull
        @Override
        public MatatusItemsAdapter.VersionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_recycler_items, viewGroup, false);
            VersionViewHolder viewHolder = new VersionViewHolder(view);
            Log.d(TAG, String.valueOf(searchMatatusList.size())+"neat");
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MatatusItemsAdapter.VersionViewHolder versionViewHolder, int i) {
            versionViewHolder.regName.setText(searchMatatusList.get(i).getRegNumber());
            versionViewHolder.price.setText(searchMatatusList.get(i).getPrice());
            Log.d(TAG, String.valueOf(searchMatatusList.size())+"gfdsdfghgjhgft");
//            versionViewHolder.add.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(getContext(), FragPayment.class));
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return searchMatatusList == null ? 0 :searchMatatusList.size();
        }

        class VersionViewHolder extends RecyclerView.ViewHolder {
            TextView regName, price;
            Button add;
            public VersionViewHolder(@NonNull View itemView) {
                super(itemView);
                regName = itemView.findViewById(R.id.registrationNum);
                price = itemView.findViewById(R.id.price);
            }
        }
    }
}
