package com.example.andrewspc.connectv6;

import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewspc.connectv6.Chat.ChatObject;
import com.example.andrewspc.connectv6.Utils.BottomNavigationViewHelper;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import android.support.v7.widget.SearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostedServices extends AppCompatActivity{

    SearchView searchView;

    private boolean match = true;
    //String global;
    //String global2;

    private List<Slide> slideList = new ArrayList<>();
    private ViewPager viewPager;
    private CustomSwipeAdapter adapter;
    private LinearLayout mDotLayout;
    private TextView[] mDots;

    private CustomSwipeAdapter sliderAdapter;

    private Uri filePath;
    private StorageReference storageReference;

    //Database reference
    private DatabaseReference mDatabaseDesign;
    private DatabaseReference mDatabaseRef2;
    private FirebaseUser mCurrentUser;

    //Image Uri Storage variable
    private String imageLink;
    private String userName;

    private LinearLayout category1Layout;
    private LinearLayout category2Layout;
    private LinearLayout category3Layout;
    private LinearLayout category4Layout;
    private LinearLayout category5Layout;
    private LinearLayout category6Layout;
    private LinearLayout category7Layout;
    private LinearLayout category8Layout;
    private LinearLayout category9Layout;
    private LinearLayout category10Layout;
    private LinearLayout category11Layout;
    private LinearLayout category12Layout;
    private LinearLayout category13Layout;

    //////////////////////////////////////////////////////////////////////////////////// OLD CODE ////////////////////////////////////////////////////////////////////////////////////
    //Youtube Link : https://www.youtube.com/watch?v=vpObpZ5MYSE&t=450s
    //Stopped at 13:19 minutes

    DatabaseReference reference;
    DatabaseReference searchNameRef;
    RecyclerView recyclerView;
    ArrayList<postedServicesModelClass> list;
    ArrayList<postedServicesModelClass> searchField;
    ThirdAdapter adapterThree;

    ArrayList<postedServicesModelClass> profileList;

    private EditText searchBox;
    private Button searchBtnClick;
    private Button newPostBtn;
    private Button sellButton;

    private static final int GALLERY_REQUEST = 1;

    //variables
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private List<String> fileDoneList;
    private UploadListAdapter uploadListAdapter;

    String category;

    private Button loginOutBtn;

    private long backTimePressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_services);

        //Loading of category Images

        loginOutBtn = findViewById(R.id.loginOutBtn);

        category1Layout = findViewById(R.id.Cat1);
        category2Layout = findViewById(R.id.Cat2);
        category3Layout = findViewById(R.id.Cat3);
        category4Layout = findViewById(R.id.Cat4);
        category5Layout = findViewById(R.id.Cat5);
        category6Layout = findViewById(R.id.Cat6);
        category7Layout = findViewById(R.id.Cat7);
        category8Layout = findViewById(R.id.Cat8);
        category9Layout = findViewById(R.id.Cat9);
        category10Layout = findViewById(R.id.Cat10);
        category11Layout = findViewById(R.id.Cat11);
        category12Layout = findViewById(R.id.Cat12);
        category13Layout = findViewById(R.id.Cat13);

        searchView = findViewById(R.id.searchBtn);

        fileDoneList = new ArrayList<>();

        // getImages();

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        sliderAdapter = new CustomSwipeAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        viewPager.addOnPageChangeListener(viewListener);

        sellButton = findViewById(R.id.sellBtn);

        //Storage Reference for uploading of images
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Users").child(current_uid).child("Photos").push();

        mDatabaseDesign = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Photos").push();

        loginOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent loginIntent = new Intent(PostedServices.this, StartActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY_REQUEST);

                /*
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
                */

            }
        });

        setupBottomNavigatonView();

        //For the top app bar of the app
        /*
        Toolbar toolbar = findViewById(R.id.toolbar);
        searchTextField = (EditText) toolbar.findViewById(R.id.action_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Services");
        */

        recyclerView = (RecyclerView) findViewById(R.id.mRecyclerView3);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

        // Youtube : https://www.youtube.com/watch?v=SD2t75T5RdY&t=716s
        // 3 item grid view is this below line of code
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        list = new ArrayList<postedServicesModelClass>();
        searchField = new ArrayList<postedServicesModelClass>();

        reference = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Photos");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    postedServicesModelClass p = dataSnapshot1.getValue(postedServicesModelClass.class);
                    list.add(p);
                }

                Collections.reverse(list);
                adapterThree = new ThirdAdapter(PostedServices.this, list, profileList);
                recyclerView.setAdapter(adapterThree);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(PostedServices.this, "Error something is not right", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting up the search function
        // Youtube Tutorial : https://www.youtube.com/watch?v=PmqYd-AdmC0
        if (searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }

        category1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterThree = new ThirdAdapter(PostedServices.this, list, profileList);
                recyclerView.setAdapter(adapterThree);
            }
        });

        category2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClick("24h Services");
            }
        });

        category3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClick("Education");
            }
        });

        category4Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClick("Relax");
            }
        });

        category5Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClick("Facial");
            }
        });

        category6Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClick("Games");
            }
        });

        category7Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClick("Medical");
            }
        });

        category8Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClick("Exercise");
            }
        });

        category9Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClick("Delivery");
            }
        });

        category10Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClick("Travel");
            }
        });

        category11Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClick("Advertising");
            }
        });

        category12Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClick("Property");
            }
        });

        category13Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClick("Focus");
            }
        });
    }

    @Override
    public void onBackPressed() {


        if (backTimePressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(PostedServices.this, "Please Back Again To Exit The Application", Toast.LENGTH_SHORT).show();
        }
        backTimePressed = System.currentTimeMillis();

        /*
        Intent i = new Intent(PostedServices.this, PostedServices.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        */
    }

    private void categoryClick(final String clickItem) {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    if (dataSnapshot1.hasChild("Category")){
                            String name = dataSnapshot1.child("Category").getValue().toString();
                            if (name.equals(clickItem)) {

                                final ArrayList<postedServicesModelClass> myCategoryList = new ArrayList<>();
                                for (postedServicesModelClass obj : list) {
                                    if (obj.getCategory().equals(clickItem)) {
                                        myCategoryList.add(obj);
                                    }
                                }

                                Collections.reverse(list);
                                ThirdAdapter thirdAdapter = new ThirdAdapter(PostedServices.this, myCategoryList, profileList);
                                recyclerView.setAdapter(thirdAdapter);
                            }
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(PostedServices.this, "Error something is not right", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Setting up the search function
    // Youtube Tutorial : https://www.youtube.com/watch?v=PmqYd-AdmC0
    public void search(String str) {

        ArrayList<postedServicesModelClass> myCurrentList = new ArrayList<>();
        for (postedServicesModelClass obj : list)
        {
            if (obj.getTitle().toLowerCase().contains(str.toLowerCase())) {
                myCurrentList.add(obj);
            }
        }
        ThirdAdapter thirdAdapter = new ThirdAdapter(PostedServices.this, myCurrentList, profileList);
        recyclerView.setAdapter(thirdAdapter);
    }

    // Displaying Images to the image view
    /*
    private void getImages(){

        // General Services
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/uploads%2FCategories%20Images%2Fwhiteoutlineninesmallboxes.png?alt=media&token=b694748c-e61b-4414-abbc-10a12f218a4f");
        mNames.add("All Services");

        // 24 Hours Image
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/uploads%2FCategories%20Images%2Fawhiteoutlinehours.png?alt=media&token=fedd1338-5931-41b3-a095-1fe1b4245368");
        mNames.add("24h Services");

        // Eduation Image
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/uploads%2FCategories%20Images%2Fwhiteoutlinemortarboard.png?alt=media&token=6650ec78-85e3-4e25-bd43-498b6173a109");
        mNames.add("Education");

        // Facial Image
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/uploads%2FCategories%20Images%2Fwhiteoutlinemakeup.png?alt=media&token=301928bb-fe56-44f2-aad8-bcd4f71435e9");
        mNames.add("Facial");

        // Game Controller
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/uploads%2FCategories%20Images%2Fwhiteoutlineconsole.png?alt=media&token=dfaaef8c-9276-42b4-9516-3006e8d7ba74");
        mNames.add("Games");

        // Health Services
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/uploads%2FCategories%20Images%2Fwhiteoutlineheart.png?alt=media&token=4ed6bb8c-3b27-4d1f-946e-ef1ee2bbd2a5");
        mNames.add("Medical");

        // Exercise
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/uploads%2FCategories%20Images%2Fwhiteoutlineweightlifter.png?alt=media&token=c96fc107-eaee-416a-9a61-b03f9a8b08c6");
        mNames.add("Exercise");

        // Delivery
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/uploads%2FCategories%20Images%2Fwhiteoutlinetruck.png?alt=media&token=656036f7-ad74-4dc5-bf6a-5e47dd4c47d4");
        mNames.add("Delivery");

        // Travelling
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/uploads%2FCategories%20Images%2Fwhiteoutlineaeroplane.png?alt=media&token=5253f839-7646-4ddf-8d62-f34c27c45fc1");
        mNames.add("Travel");

        // Advertising
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/uploads%2FCategories%20Images%2Fwhiteoutlinead.png?alt=media&token=cda399db-8f6a-4081-8389-af2b71ac1435");
        mNames.add("Advertising");

        // Property
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/uploads%2FCategories%20Images%2Fhomewhiteoutline.png?alt=media&token=f00a6b79-f34e-4e5e-8c43-cbc006d1a994");
        mNames.add("Property");

        // Relax
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/uploads%2Farelaxservices.png?alt=media&token=9ec8bbcc-1ab5-4d9e-ab28-96aa9edede97");
        mNames.add("Relax");

        // Yoga
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/uploads%2FCategories%20Images%2Fwhiteoutlineyogaposition.png?alt=media&token=29e64d1e-c890-4708-907b-75103dfe6984");
        mNames.add("Focus");

        initRecyclerView();
    }
    */

    private void initRecyclerView(){
        /*
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        CategoryAdapter adapter = new CategoryAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
        */
    }

    //Image Cropper and posting code
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            filePath = data.getData();
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .setMinCropResultSize(350, 350)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                final Intent intent = new Intent(PostedServices.this, HomeScreen.class);
                intent.putExtra("imageSelected", resultUri);
                startActivity(intent);

                //postImage.setImageURI(resultUri);
                //filePath = resultUri;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


        /*
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            if (data.getClipData() != null){

                int totalItemsSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemsSelected; i++){

                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    String fileName = getFileName(fileUri);
                    fileDoneList.add(fileName);

                    Toast.makeText(PostedServices.this, fileDoneList.toString(), Toast.LENGTH_SHORT).show();
                }

            }else if (data.getData() != null){

                Toast.makeText(PostedServices.this, "Selected Single File", Toast.LENGTH_SHORT).show();

            }
        }
        */
    }

    public String getFileName(Uri uri){
        String result = null;
        if (uri.getScheme().equals("contents")){
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if(result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

        /*
        Bottom Navigation View Setup
        */
    private void setupBottomNavigatonView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(PostedServices.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }

    //// For the top app bar of the app
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    //// For the top app bar of the app
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.item3:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent loginIntent = new Intent(PostedServices.this, StartActivity.class);
                startActivity(loginIntent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addDotsIndicator(int position) {

        mDots = new TextView[4];
        mDotLayout.removeAllViews();

        for(int i = 0; i < mDots.length; i++){

            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.lightGrey));

            mDotLayout.addView(mDots[i]);

        }

        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.black));

        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}