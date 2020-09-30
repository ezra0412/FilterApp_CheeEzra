package com.example.filterapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.filterapp.adapter.MainTabAdapter;
import com.example.filterapp.classes.MovableFloatingActionButton;
import com.example.filterapp.classes.StaffDetails;
import com.example.filterapp.menu.DrawerAdapter;
import com.example.filterapp.menu.DrawerItem;
import com.example.filterapp.menu.SimpleItem;
import com.example.filterapp.menu.SpaceItem;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.SlidingRootNavLayout;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    TabLayout tabLayout;
    TabItem generateQR, scanQR;
    ViewPager viewPager;
    View view;
    public static boolean verfiedAdmin = false;
    public static int positionCode = 3;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore fs = FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private SlidingRootNav slidingRootNav;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    boolean showPasswordPU = false;
    TextView name, position, branch;
    ImageView pi;
    Dialog loadingDialog, adminDialog;
    public static Dialog loadingDialogAdmin;

    private static final int account = 0;
    private static final int addCustomer = 1;
    private static final int customerDetails = 2;
    private static final int GPS = 3;
    private static final int faq = 4;
    private static final int tnc = 5;
    private static final int logout = 7;

    public static StaffDetails staffDetailsStatic = new StaffDetails();

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tl_main);
        generateQR = findViewById(R.id.tab_item_generateQR_main);
        scanQR = findViewById(R.id.tab_item_scanQR_main);
        viewPager = findViewById(R.id.vp_main);
        loadingDialog = new Dialog(this);
        loadingDialogAdmin = new Dialog(this);
        adminDialog = new Dialog(this);

        DocumentReference staffDB = db.collection("staffDetails").document(mAuth.getCurrentUser().getUid());
        staffDB.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                setPositionCodeDB(documentSnapshot.getString("position"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error, please try again later", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, LoginPage.class));
                finish();
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .withDragDistance(160)
                .withRootViewScale(0.8f)
                .withRootViewElevation(10)
                .withRootViewYTranslation(4)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        SlidingRootNavLayout sideBar = slidingRootNav.getLayout();
        name = sideBar.findViewById(R.id.tv_name_menu);
        branch = sideBar.findViewById(R.id.tv_branch_menu);
        position = sideBar.findViewById(R.id.tv_position_menu);
        pi = sideBar.findViewById(R.id.img_pi_menu);
        DocumentReference userDetails = db.collection("staffDetails").document(mAuth.getCurrentUser().getUid());
        userDetails.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                name.setText(documentSnapshot.getString("fName"));
                branch.setText(documentSnapshot.getString("branch"));
                position.setText(documentSnapshot.getString("position"));
                setPositionCodeDB(documentSnapshot.getString("position"));
                setStaffDetailsStatic(documentSnapshot.toObject(StaffDetails.class));
            }
        });

        pi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 1000);
            }
        });

        StorageReference profileRef = storageReference.child("staff/" + mAuth.getCurrentUser().getUid() + "/profileImage.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(pi);
            }
        });

        position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position.getText().toString().trim().equalsIgnoreCase("admin")) {
                    adminCheck();
                }
            }
        });

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(account),
                createItemFor(addCustomer),
                createItemFor(customerDetails),
                createItemFor(GPS),
                createItemFor(faq),
                createItemFor(tnc),
                new SpaceItem(35),
                createItemFor(logout)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        MovableFloatingActionButton fab = findViewById(R.id.fab_main);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        fab.setCoordinatorLayout(lp);

        MainTabAdapter mainTabAdapter = new
                MainTabAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());


        viewPager.setAdapter(mainTabAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setPositionCodeDB(String position) {

        if (position.equalsIgnoreCase("admin"))
            positionCode = 1;

        else if (position.equalsIgnoreCase("sales"))
            positionCode = 2;
        else
            positionCode = 3;

    }


    @Override
    public void onItemSelected(int position) {
        switch (position) {
            case account:
                Intent accountPage = new Intent(MainActivity.this, AccountDetails.class);
                accountPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(accountPage);
                break;

            case addCustomer:
                if (positionCode != 3) {
                    Intent addCustomerPage = new Intent(MainActivity.this, AddCustomer.class);
                    addCustomerPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    addCustomerPage.putExtra("customerID", "non");
                    startActivity(addCustomerPage);
                } else
                    Toast.makeText(MainActivity.this, "Sorry technician aren't allowed", Toast.LENGTH_LONG).show();

                break;

            case customerDetails:
                if (positionCode != 3) {
                    Intent customerDetailsPage = new Intent(MainActivity.this, Abcd.class);
                    customerDetailsPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(customerDetailsPage);
                } else
                    Toast.makeText(MainActivity.this, "Sorry technician aren't allowed", Toast.LENGTH_LONG).show();

                break;
            case GPS:
                Intent GpsPage = new Intent(MainActivity.this, GPS.class);
                GpsPage.putExtra("lan", "non");
                GpsPage.putExtra("lon", "non");
                GpsPage.putExtra("name", "non");
                GpsPage.putExtra("mobile", "non");
                GpsPage.putExtra("address", "non");
                GpsPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(GpsPage);
                break;

            case faq:
                Intent FAQ = new Intent(MainActivity.this, FAQ.class);
                FAQ.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(FAQ);
                break;

            case tnc:
                Intent tnc = new Intent(MainActivity.this, TermNCondition.class);
                tnc.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tnc);
                break;

            case logout:
                mAuth.signOut();
                mGoogleSignInClient.revokeAccess();
                startActivity(new Intent(MainActivity.this, LoginPage.class));
                finish();
                break;
        }
        slidingRootNav.closeMenu();

    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.light_green))
                .withTextTint(color(R.color.light_green))
                .withSelectedIconTint(color(R.color.light_green))
                .withSelectedTextTint(color(R.color.light_green));
    }


    public void addCustomer(View view) {
        if (positionCode != 3) {
            Intent intent = new Intent(MainActivity.this, AddCustomer.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("customerID", "non");
            startActivity(intent);
        } else
            Toast.makeText(MainActivity.this, "Sorry technician aren't allowed", Toast.LENGTH_LONG).show();

    }


    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.menuScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.menuScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri image = data.getData();
                uploadImageToFirebase(image);
            }
        }
    }

    private void uploadImageToFirebase(Uri image) {
        ProgressBar progressBar;
        Sprite style = new Wave();
        loadingDialog.setContentView(R.layout.pop_up_loading_screen);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar = loadingDialog.findViewById(R.id.sk_loadingPU);
        progressBar.setIndeterminateDrawable(style);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        final StorageReference imageRefence = storageReference.child("staff/" + mAuth.getCurrentUser().getUid() + "/profileImage.jpg");
        imageRefence.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRefence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(pi);
                        loadingDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Failed to change profile picture", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void adminCheck() {
        ProgressBar progressBar;
        Sprite style = new Wave();
        loadingDialogAdmin.setContentView(R.layout.pop_up_loading_screen);
        loadingDialogAdmin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar = loadingDialogAdmin.findViewById(R.id.sk_loadingPU);
        progressBar.setIndeterminateDrawable(style);
        loadingDialogAdmin.setCanceledOnTouchOutside(false);

        final ImageView close, showPassword;
        Button done;
        final EditText etPassword;
        adminDialog.setContentView(R.layout.pop_up_admin_password_check);
        adminDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        close = adminDialog.findViewById(R.id.img_close_adminPU);

        showPassword = adminDialog.findViewById(R.id.img_showPassword_adminPU);

        etPassword = adminDialog.findViewById(R.id.et_enterCode_adminPU);

        done = adminDialog.findViewById(R.id.bt_done_adminPU);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminDialog.dismiss();
            }
        });

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showPasswordPU) {
                    showPassword.setImageResource(R.drawable.hide_password_icon);
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPassword.setSelection(etPassword.getText().length());
                    showPasswordPU = true;
                } else {
                    showPassword.setImageResource(R.drawable.show_password_icon);
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPassword.setSelection(etPassword.getText().length());
                    showPasswordPU = false;
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String password = etPassword.getText().toString().trim();

                if (password.isEmpty()) {
                    etPassword.setError("Cannot leave empty");
                    etPassword.requestFocus();
                    return;
                }

                DocumentReference passwordCheck = db.collection("adminDetails").document("loginDetails");
                passwordCheck.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (password.equals(documentSnapshot.getString("verificationPassword"))) {
                            adminDialog.dismiss();
                            loadingDialogAdmin.show();
                            Toast.makeText(MainActivity.this, "Admin verification successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, AdminPage.class);
                            startActivity(intent);
                            loadingDialogAdmin.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this, "Admin verification failed", Toast.LENGTH_SHORT).show();
                            adminDialog.dismiss();
                        }
                    }
                });
            }
        });
        adminDialog.show();

        adminDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                closeKeyboard();
            }
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    @Override
    public void onResume() {
        super.onResume();

        DocumentReference userDetails = db.collection("staffDetails").document(mAuth.getCurrentUser().getUid());
        userDetails.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                name.setText(documentSnapshot.getString("fName"));
                branch.setText(documentSnapshot.getString("branch"));
                position.setText(documentSnapshot.getString("position"));
                setPositionCodeDB(documentSnapshot.getString("position"));
                setStaffDetailsStatic(documentSnapshot.toObject(StaffDetails.class));
            }
        });


        StorageReference profileRef = storageReference.child("staff/" + mAuth.getCurrentUser().getUid() + "/profileImage.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(pi);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pi.setImageDrawable(getDrawable(R.drawable.profile_picture));
            }
        });

    }

    public static int getPositionCode() {
        return positionCode;
    }

    public static void setPositionCode(int positionCode) {
        MainActivity.positionCode = positionCode;
    }

    public static boolean isVerfiedAdmin() {
        return verfiedAdmin;
    }

    public static void setVerfiedAdmin(boolean verfiedAdmin) {
        MainActivity.verfiedAdmin = verfiedAdmin;
    }

    public static StaffDetails getStaffDetailsStatic() {
        return staffDetailsStatic;
    }

    public static void setStaffDetailsStatic(StaffDetails staffDetailsStatic) {
        MainActivity.staffDetailsStatic = staffDetailsStatic;
    }
}
