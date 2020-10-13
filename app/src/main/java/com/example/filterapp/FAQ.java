package com.example.filterapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterapp.adapter.FAQAdapter;
import com.example.filterapp.classes.FAQitem;

import java.util.LinkedList;
import java.util.List;

public class FAQ extends AppCompatActivity {
    RecyclerView faq;
    RecyclerView.LayoutManager layoutManager;
    List<FAQitem> faqItems = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_a_q);
        faq = findViewById(R.id.rv_faq);
        faq.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        faq.setLayoutManager(layoutManager);

        FAQitem item = new FAQitem("1. Forget password", "You can reset your password by pressing the forget password text at the Login page or My account page which then you can enter your email to receive a secure link to reset your password.", true);
        faqItems.add(item);

        FAQitem item2 = new FAQitem("2. Position", "Sales position will be to access all functions, but for technician position, you will only be able to record serviced filter details, navigating to the customer's house and also checking work history.", true);
        faqItems.add(item2);

        FAQitem item3 = new FAQitem("3. Update Details ", "You can update your details by pressing the side bar then chose My Account page.", true);
        faqItems.add(item3);

        FAQitem item4 = new FAQitem("4. Changing Position ", "Inorder to change your position, you can either ask the admin to do it for you or you can request for a permission code from the admin, then change it at the My Account page.", true);
        faqItems.add(item4);

        FAQitem item5 = new FAQitem("5. Changing Profile Picture ", "You will be able to change your profile picture by pressing you profile picture at the side bar or through the My Account page.", true);
        faqItems.add(item5);

        FAQitem item6 = new FAQitem("6. Remove Profile Picture", "You will be able to remove your profile picture by pressing the remove button below your profile picture at My Account page.", true);
        faqItems.add(item6);

        FAQitem item7 = new FAQitem("7. Register New Customer ", "You can register new customer by either pressing the floating + sign button at main page or you can add it by choosing the Add Customer page at the side bar.", true);
        faqItems.add(item7);

        FAQitem item8 = new FAQitem("8. Check All Customer's Details", "You will be able to check all registered customer by pressing the side bar then chose the Customer Details page.", true);
        faqItems.add(item8);

        FAQitem item9 = new FAQitem("9. Terminated Customer", "When an customer is being terminated, you won't be able to edit theirs detail, add new filter nor service theirs filter anymore.", true);
        faqItems.add(item9);

        FAQitem item10 = new FAQitem("10. Generate QR code", "After an customer brought the filter, you can first register the customer (If it's a new Customer), then fill up all the needed details at the Main page then the system will auto generate you with an QR code.", true);
        faqItems.add(item10);

        FAQitem item11 = new FAQitem("11. Generated Qr code", "You can chose to save the Qr code by pressing the top right icon or you can chose to share the QR code by pressing the share button.", true);
        faqItems.add(item11);

        FAQitem item12 = new FAQitem("12. Lost/Damaged Qr code", "You can regenerate an QR code by navigating to Customer Purchase history by press the customer details and navigate to the filter's owner page, then press on the Purcase History button at the top right corner." +
                "Choose the filter which the QR code is being damaged, after you are being redirect to the filter details page you can press on the Invoice number and you will be able to generate a new QR code.", true);
        faqItems.add(item12);

        FAQitem item13 = new FAQitem("13. Record Filter Serviced Details", "You can either scan the QR code which is being stick on the filter through the scanner at the Main Page or you can navigate to the Filter Detail page through the Service List page or Purchase History page.", true);
        faqItems.add(item13);

        FAQitem item14 = new FAQitem("14. Scanner Function", "The top left icon is for turning on and off Auto Focus, the top right button is for turning flash light.", true);
        faqItems.add(item14);

        FAQitem item15 = new FAQitem("15. Check Service List", "You will be able to check all the filter which is being sold, by pressing the side bar then press the service list.", true);
        faqItems.add(item15);

        FAQitem item16 = new FAQitem("16. Navigation", "In order to navigate to an customer's house, press on the side bar then chose the GPS page. Then you will just need to enter the customer's first name and mobile, after that you can chose either to navigate by Google Map or Waze. Don't worry if you don't have these app, Waze will be able to navigate through any browser.", true);
        faqItems.add(item16);

        FAQitem item17 = new FAQitem("17. Work History", "You can check all of your sales or service history, by pressing the side bar then chose the Work History page.", true);
        faqItems.add(item17);

        FAQAdapter adapter = new FAQAdapter(faqItems);
        faq.setAdapter(adapter);
    }

    public void back(View view) {
        super.onBackPressed();
    }
}
