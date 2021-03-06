package com.example.ala.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ala.Inventory.StatusData;
import com.example.ala.Order;
import com.example.ala.OrderAdapter;
import com.example.ala.R;
import com.example.ala.view.dialog.PaymentDialog;
import com.example.ala.view.dialog.SaleDialog;
import com.example.ala.StatusAdapter;
import com.example.ala.controller.OrderActivityController;
import com.example.ala.view.dialog.StornoDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class OrderActivityView extends AppCompatActivity implements OrderAdapter.OnDetailListener, SaleDialog.SaleDialogListener, StornoDialog.StornoDialogListener, PaymentDialog.PaymentDialogListener {

    OrderActivityController controller;

    private Spinner spinner_status;
    private StatusAdapter statAdapter;
    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    public OrderAdapter adapter;
    public ArrayList<Order> list;
    public TextView numberOrder, register_num, txt_date_order, txt_status, txt_type_payment, txt_paid, txt_price, txt_name_customer,txt_email_customer,
            txt_phone_customer, txt_offic_address,txt_office_name,txt_name_product, txt_discount, txt_date_pay, title_date_pay, title_locate,
            txt_locate, txt_date_locate, title_registr_num, txt_register_num, txt_description;
    public ImageView img_status_bar;
    public Button btn_payment, btn_storno, btn_edit_sale;
    public BottomSheetDialog bottomSheetDialog;
    Context context;

    //TODO fce filter orders by word search or status
    //TODO adding count  product

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        controller = new OrderActivityController(this);

        context = this;

        recyclerView = findViewById(R.id.recycler_view2);
        progressBar = findViewById(R.id.progress_bar);
        spinner_status = findViewById(R.id.spinner_filter);
        statAdapter = new StatusAdapter(OrderActivityView.this, StatusData.getStatusList());
        spinner_status.setAdapter(statAdapter);

        //   mAuth = FirebaseAuth.getInstance();

        //   final FirebaseUser office = mAuth.getCurrentUser();
        //  String id = office.getUid();

        //  firebaseDatabase = FirebaseDatabase.getInstance();
        // databaseReference = FirebaseDatabase.getInstance().getReference().child("Order").child("Orders");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new OrderAdapter(this, list, this);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        controller.setRecViewContent();
        progressBar.setVisibility(View.INVISIBLE);


    }

    @Override
    public void onDetailClick(int position)
    {
        controller.getOrderFirebaseResources(controller.getOrderID(position));

        createBottomSheet();


    }


    private void createBottomSheet() {
         bottomSheetDialog = new BottomSheetDialog(OrderActivityView.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.sheet_detail_order,(LinearLayout)findViewById(R.id.sheet_container));
        bottomSheetDialog.setContentView(bottomSheetView);

        numberOrder = bottomSheetDialog.findViewById(R.id.number_order);
        txt_date_order = bottomSheetDialog.findViewById(R.id.txt_date_order);
        txt_status = bottomSheetDialog.findViewById(R.id.txt_status);
        img_status_bar = bottomSheetDialog.findViewById(R.id.img_status_bar);
        txt_type_payment = bottomSheetDialog.findViewById(R.id.txt_type_payment);
        txt_paid = bottomSheetDialog.findViewById(R.id.txt_paid);
        txt_price = bottomSheetDialog.findViewById(R.id.txt_price);
        txt_name_customer = bottomSheetDialog.findViewById(R.id.txt_name_customer);
        txt_email_customer = bottomSheetDialog.findViewById(R.id.txt_email_customer);
        txt_phone_customer = bottomSheetDialog.findViewById(R.id.txt_phone_customer);
        txt_offic_address = bottomSheetDialog.findViewById(R.id.txt_offic_address);
        txt_office_name = bottomSheetDialog.findViewById(R.id.txt_office_name);
        txt_name_product = bottomSheetDialog.findViewById(R.id.txt_name_product);
        txt_discount = bottomSheetDialog.findViewById(R.id.txt_discount);
        txt_date_pay = bottomSheetDialog.findViewById(R.id.txt_date_pay);
        title_date_pay = bottomSheetDialog.findViewById(R.id.title_date_pay);
        title_locate = bottomSheetDialog.findViewById(R.id.title_locate);
        txt_locate = bottomSheetDialog.findViewById(R.id.txt_locate);
        txt_date_locate = bottomSheetDialog.findViewById(R.id.txt_date_locate);
        title_registr_num = bottomSheetDialog.findViewById(R.id.title_registr_num);
        txt_register_num = bottomSheetDialog.findViewById(R.id.txt_register_num);
        btn_payment = bottomSheetDialog.findViewById(R.id.btn_payment);
        btn_storno = bottomSheetDialog.findViewById(R.id.btn_storno);
        btn_edit_sale = bottomSheetDialog.findViewById(R.id.btn_edit_sale);
        txt_description = bottomSheetDialog.findViewById(R.id.txt_waiting_alert);


        bottomSheetDialog.show();

        btn_edit_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSaleDialog();
            }
        });

        btn_storno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStornoDialog();
            }
        });

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // openPaymentDialog();
                controller.setPDF(context);

            }
        });

    }

    private void openPaymentDialog() {
        PaymentDialog paymentDialog = new PaymentDialog();
        paymentDialog.show(getSupportFragmentManager(), "payment dialog");
    }

    private void openStornoDialog() {
        StornoDialog stornoDialog = new StornoDialog();
        stornoDialog.show(getSupportFragmentManager(), "storno dialog");
    }

    public void openSaleDialog() {
        SaleDialog saleDialog = new SaleDialog();
        saleDialog.show(getSupportFragmentManager(),"sale dialog");
    }
    @Override
    public void applyTexts2() {
        controller.setAfterStorno();

    }

    @Override
    public void applyTexts(String sale) {
        controller.setAfterSale(sale);

    }

    @Override
    public void applyTexts3() {
        controller.setAfterPayment();
        bottomSheetDialog.cancel();
        adapter.notifyDataSetChanged();
    }

}