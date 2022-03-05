package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.Plane;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.Sun;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseTransformableNode;
import com.google.ar.sceneform.ux.TransformableNode;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import at.markushi.ui.CircleButton;

public class MainActivity extends AppCompatActivity implements ItemTabListenerRec, ColorPickerDialogListener{

    private static final String TAG ="MainActivity";
    private static final int DIALOG_ID =0;
    ArFragment arFragment;
    RecyclerView recyclerView;
    ModelRenderable sofaRenderable,
            bedRenderable,
            tableRenderable,
            dresserRenderable,
            sideLampRenderable,
            renderable;
    Anchor anchor;
    AnchorNode anchorNode;
    CircleButton deleteBtn;
    CircleButton resetBtn;
    CircleButton saveBtn;
    SwitchCompat deleteSwitch;
    ChipNavigationBar chipnav;
    ImageView selectedColor;
    private int mWidth;
    private int mHeight;
    private  boolean capturePicture = false;
    private int fCount;


    boolean deleteOnTap = false;
    private LinearLayout bottomsheetlayout;
    BottomSheetBehavior sheetBehavior;
    String[] objectName;
    int[] objectImg;
    private File videoDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragmented);
        recyclerView = findViewById(R.id.recycleView);
        deleteSwitch = findViewById(R.id.deleteswitch);
        bottomsheetlayout = findViewById(R.id.bottomsheetlayout);
        chipnav = findViewById(R.id.chipnav);


        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        objectName = new String[]{"Sofa", "Bed", "Table", "Dresser","Side Lamp"};
        objectImg = new int[]{R.drawable.sofa, R.drawable.bed, R.drawable.table, R.drawable.dresser,R.drawable.sidelamp};

        recyclerView.setAdapter(new RecycleAdapter(MainActivity.this, objectName, objectImg, MainActivity.this));

        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdate);

        deleteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deleteOnTap = isChecked;
            }
        });

        chipnav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                switch (id) {
                    case R.id.bedRoom:

                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));

                        objectName = new String[]{"Sofa", "Bed", "Table", "Dresser","Side Lamp"};
                        objectImg = new int[]{R.drawable.sofa, R.drawable.bed, R.drawable.table, R.drawable.dresser,R.drawable.sidelamp};

                        recyclerView.setAdapter(new RecycleAdapter(MainActivity.this, objectName, objectImg, MainActivity.this));


                        break;
                    case R.id.lounge:


                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));

                        objectName = new String[]{"Single Sofa", "Two Seater", "Combed Sofa", "Corner Sofa","Dinning Table","Dinning Set","Lamp"};
                        objectImg = new int[]{R.drawable.singlesofa, R.drawable.twoseater, R.drawable.sofacombed, R.drawable.cornersofa,R.drawable.dinningtable,R.drawable.dinningset,R.drawable.lamp};

                        recyclerView.setAdapter(new RecycleAdapter(MainActivity.this, objectName, objectImg, MainActivity.this));


                        break;
                    case R.id.kitchen:
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));

                        objectName = new String[]{"Fridge", "Modern Fridge", "Cooking Range", "Cabinet","Dish Washer","Dish Cabinet"};
                        objectImg = new int[]{R.drawable.fridge, R.drawable.modernfridge, R.drawable.cookingrange, R.drawable.cabinet,R.drawable.dishwasher,R.drawable.dishcabinet};

                        recyclerView.setAdapter(new RecycleAdapter(MainActivity.this, objectName, objectImg, MainActivity.this));



                        break;
                    case R.id.bathroom:
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));

                        objectName = new String[]{"Sink", "Rounded Sink", "Toilet", "Modern Toilet","Bathtub","Dust Bin"};
                        objectImg = new int[]{R.drawable.sink, R.drawable.roundsink, R.drawable.toilet, R.drawable.moderntoilet,R.drawable.bathtub,R.drawable.dustbin};

                        recyclerView.setAdapter(new RecycleAdapter(MainActivity.this, objectName, objectImg, MainActivity.this));



                        break;


                }
            }
        });
        chipnav.setItemSelected(R.id.bedRoom,true);

        arFragment.getArSceneView().getScene()
                .addOnPeekTouchListener(new Scene.OnPeekTouchListener() {


                    @Override
                    public void onPeekTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
                        arFragment.onPeekTouch(hitTestResult, motionEvent);

                        // Check for touching a Sceneform node
                        if (hitTestResult.getNode() != null) {

                            Node node = hitTestResult.getNode();
                            if (deleteOnTap) {
                                deleteItemOnTab(node);
                            }
//                            if (node.getRenderable() == sofaRenderable) {
//
//                                selectedenderable = sofaRenderable;
//                                System.out.println("detectSofa");
//                            }
//                            if (node.getRenderable() == bedRenderable) {
//                                deleteItemOnTab(node);
//
//                                System.out.println("detectBed");
//                            }
//                            if (node.getRenderable() == tableRenderable) {
//                                deleteItemOnTab(node);
//
//                                System.out.println("detectTable");
//                            }
//                            if (node.getRenderable() == dresserRenderable) {
//                                deleteItemOnTab(node);
//
//                                System.out.println("detectDresser");
//                            }

                        }
                    }
                });

        deleteBtn = findViewById(R.id.deleteBtn);
        resetBtn = findViewById(R.id.resetBtn);
        saveBtn = findViewById(R.id.saveBtn);


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeAnchorNode(anchorNode);


            }
        });
        selectedColor=findViewById(R.id.colorChange);
        selectedColor.setOnClickListener(v -> changeColor());



        sheetBehavior = BottomSheetBehavior.from(bottomsheetlayout);

        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetScene();

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();

            }

        });




    }

    private void onUpdate(FrameTime frameTime) {

        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<Plane> planes = frame.getUpdatedTrackables(Plane.class);

        for (Plane plane : planes) {

            if (plane.getTrackingState() == TrackingState.TRACKING) {

                System.out.println("checking" + plane.getTrackingState());
                anchor = plane.createAnchor(plane.getCenterPose());

                break;
            }


        }
    }

    @Override
    public void getitemNameonTabListener(String name) {
        if (name.equals("Sofa")) {

            placeSofa(anchor);
        }
        if (name.equals("Bed")) {
            placeBed(anchor);
        }
        if (name.equals("Table")) {
            placeTable(anchor);
        }
        if (name.equals("Dresser")) {
            placeDressing(anchor);
        }
        if (name.equals("Side Lamp")){
            placeSideLamp(anchor);
        }
        if (name.equals("Single Sofa")){
            placeSingleSofa(anchor);
        }
        if (name.equals("Two Seater")){
            placeTwoSeater(anchor);
        }
        if (name.equals("Combed Sofa")){
            placeCombedSofa(anchor);
        }
        if (name.equals("Corner Sofa")){
            placeCornerSofa(anchor);
        }
        if (name.equals("Dinning Table")){
            placeDinningTable(anchor);
        }
        if (name.equals("Dinning Set")){
            placeDinningSet(anchor);
        }
        if (name.equals("Lamp")){
            placeLamp(anchor);
        }
        if (name.equals("Fridge")){
            placeFridge(anchor);
        }
        if (name.equals("Modern Fridge")){
            placeModernFridge(anchor);
        }
        if (name.equals("Cooking Range")){
            placeCookingRange(anchor);
        }
        if (name.equals("Cabinet")){
            placeCabinet(anchor);
        }
        if (name.equals("Dish Washer")){
            placeDishWasher(anchor);
        }
        if (name.equals("Dish Cabinet")){
            placeDishCabinet(anchor);
        }
        if (name.equals("Sink")){
            placeSink(anchor);
        }
        if (name.equals("Rounded Sink")){
            placeRoundedSink(anchor);
        }
        if (name.equals("Toilet")){
            placeToilet(anchor);
        }
        if (name.equals("Modern Toilet")){
            placeModernToilet(anchor);
        }
        if (name.equals("Bathtub")){
            placeBathtub(anchor);
        }
        if (name.equals("Dust Bin")){
            placeDustBin(anchor);
        }




        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
    }

    public void placeSofa(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.scene)
                .build()
                .thenAccept(modelRenderable -> {
                    sofaRenderable = modelRenderable;

                    placeModel(anchor, sofaRenderable);
                });


    }


    public void placeBed(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.bed)
                .build()
                .thenAccept(modelRenderable -> {
                    bedRenderable = modelRenderable;

                    placeModel(anchor, bedRenderable);
                });


    }

    public void placeTable(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.table)
                .build()
                .thenAccept(modelRenderable -> {
                    tableRenderable = modelRenderable;

                    placeModel(anchor, tableRenderable);
                });


    }

    public void placeDressing(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.dressing)
                .build()
                .thenAccept(modelRenderable -> {
                    dresserRenderable = modelRenderable;

                    placeModel(anchor, dresserRenderable);
                });


    }
    public void placeSideLamp(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.sidelamp)
                .build()
                .thenAccept(modelRenderable -> {
                    sideLampRenderable = modelRenderable;

                    placeModel(anchor,  sideLampRenderable);
                });


    }
    public void placeSingleSofa(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.singlesofa)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeTwoSeater(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.twoseater)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeCombedSofa(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.combedsofa)
                .build()
                .thenAccept(modelRenderable -> {
                 renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeCornerSofa(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.cornersofa)
                .build()
                .thenAccept(modelRenderable -> {
                   renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeDinningTable(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.dinningtable)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeDinningSet(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.dinningset)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeLamp(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.lamp)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeFridge(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.fridge)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeModernFridge(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.modernfridge)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeCookingRange(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.cookingrange)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeCabinet(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.cabinet)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeDishCabinet(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.dishcabinet)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeDishWasher(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.dishwasher)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeSink(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.sink)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeRoundedSink(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.roundedsink)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeToilet(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.ctoilet)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeModernToilet(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.moderntoilet)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeBathtub(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.bathtub)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;
                    placeModel(anchor,  renderable);

                });


    }
    public void placeDustBin(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.dustbin1)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;
                    placeModel(anchor,  renderable);

                });


    }

    private void placeModel(Anchor anchor, ModelRenderable renderable) {
        anchorNode = new AnchorNode(anchor);
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(renderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();


    }

    public void removeAnchorNode(AnchorNode nodeToremove) {

        if (nodeToremove != null) {
            arFragment.getArSceneView().getScene().removeChild(nodeToremove);
            nodeToremove.getAnchor().detach();
            nodeToremove.setParent(null);
            nodeToremove = null;


        }

    }

    public void resetScene() {

        List<Node> children = new ArrayList<>(arFragment.getArSceneView().getScene().getChildren());
        for (Node node : children) {
            if (node instanceof AnchorNode) {
                if (((AnchorNode) node).getAnchor() != null) {
                    ((AnchorNode) node).getAnchor().detach();
                }
            }
            if (!(node instanceof Camera) && !(node instanceof Sun)) {
                node.setParent(null);
            }
        }


    }

    public void addObjectToScene(Anchor anchor, ModelRenderable modelRenderablein, int resource) {

        ModelRenderable
                .builder()
                .setSource(this, resource)
                .build()
                .thenAccept(modelRenderable -> {
                    //  modelRenderablein = modelRenderable;

                    placeModel(anchor, modelRenderable);
                });


    }

    public void deleteItemOnTab(Node node) {
        if (node instanceof AnchorNode) {
            if (((AnchorNode) node).getAnchor() != null) {
                ((AnchorNode) node).getAnchor().detach();
            }
        }
        if (!(node instanceof Camera) && !(node instanceof Sun)) {
            node.setParent(null);
        }

    }

    @Override
    protected void onResume() {

        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions
                    (this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);


    }

//    private void takeScreenshot() {
//
//
//        try {
//            // image naming and path  to include sd card  appending name you choose for file
//
//
//            String mPath = Environment.getExternalStorageDirectory().toString() + "/Download" ;
//            System.out.println("fhjsdfhskdf" +mPath);
//
//    // create bitmap screen capture
//            View v1 = getWindow().getDecorView().getRootView();
//            v1.setDrawingCacheEnabled(true);
//            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
//            v1.setDrawingCacheEnabled(false);
//
//            File imageFile = new File(mPath+"/"+System.currentTimeMillis()+".jpg");
//
//            System.out.println("fhjsdfhskdf 0 " +imageFile.toString());
//
//            FileOutputStream outputStream = new FileOutputStream(imageFile);
//            int quality = 80;
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
//            outputStream.flush();
//            outputStream.close();
//
//            //openScreenshot(imageFile);
//        } catch (Exception e) {
//            // Several error may come out with file handling or DOM
//            e.printStackTrace();
//        }
//    }
//    private void openScreenshot(File imageFile) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        Uri uri = MyFileProvider.getUriForFile(MainActivity.this,"com.example.myproject.provider",imageFile);
//        intent.setDataAndType(uri, "image/*");
//        startActivity(intent);
//    }
    private void changeColor() {

    ColorPickerDialog.newBuilder()
            .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
            .setAllowPresets(false)
            .setDialogId(DIALOG_ID)
            .setColor(android.graphics.Color.TRANSPARENT)
            .setShowAlphaSlider(true)
            .show(this);




}


    @Override public void onColorSelected(int dialogId, int color) {
        Log.d(TAG, "onColorSelected() called with: dialogId = [" + dialogId + "], color = [" + color + "]");
        switch (dialogId) {
            case DIALOG_ID:

                // We got result from the dialog that is shown when clicking on the icon in the action bar.

                BaseTransformableNode node = arFragment.getTransformationSystem().getSelectedNode();
                if (node != null) {


                    node.getRenderable().getMaterial().setFloat3("baseColorTint", new Color(color));
                }
                Toast.makeText(MainActivity.this, "Selected Color: #" + Integer.toHexString(color), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override public void onDialogDismissed(int dialogId) {
        Log.d(TAG, "onDialogDismissed() called with: dialogId = [" + dialogId + "]");
    }

    public void onSavePicture(View view) {
        // Here just a set a flag so we can copy
        // the image from the onDrawFrame() method.
        if (capturePicture) {
            capturePicture = false;
            try {
                SavePicture();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // This is required for OpenGL so we are on the rendering thread.
        this.capturePicture = true;
    }

    /**
     * Call from the GLThread to save a picture of the current frame.
     */
    public void SavePicture() throws IOException {
        int pixelData[] = new int[mWidth * mHeight];

        // Read the pixels from the current GL frame.
        IntBuffer buf = IntBuffer.wrap(pixelData);
        buf.position(0);
        GLES20.glReadPixels(0, 0, mWidth, mHeight,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buf);

        // Create a file in the Pictures/HelloAR album.
        final File out = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + "/HelloAR", "Img" +
                Long.toHexString(System.currentTimeMillis()) + ".png");

        // Make sure the directory exists
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }

        // Convert the pixel data from RGBA to what Android wants, ARGB.
        int bitmapData[] = new int[pixelData.length];
        for (int i = 0; i < mHeight; i++) {
            for (int j = 0; j < mWidth; j++) {
                int p = pixelData[i * mWidth + j];
                int b = (p & 0x00ff0000) >> 16;
                int r = (p & 0x000000ff) << 16;
                int ga = p & 0xff00ff00;
                bitmapData[(mHeight - i - 1) * mWidth + j] = ga | r | b;
            }
        }
        // Create a bitmap.
        Bitmap bmp = Bitmap.createBitmap(bitmapData,
                mWidth, mHeight, Bitmap.Config.ARGB_8888);

        // Write it to disk.
        FileOutputStream fos = new FileOutputStream(out);
        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showSnackbarMessage("Wrote " + out.getName(), false);
            }
        });
    }

    private void showSnackbarMessage(String s, boolean b) {


    }
    private void takePhoto() {
        ArSceneView view = arFragment.getArSceneView();

        // Create a bitmap the size of the scene view.
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        // Make the request to copy.
        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                try {
                    saveBitmapToDisk(bitmap,"screenshot"+(fCount++)+".jpg");
                } catch (IOException e) {
//                    Toast toast = Toast.makeText(this, "error"+e.toString(),
//                            Toast.LENGTH_LONG);
//                    toast.show();
                    return;
                }
                Toast.makeText(this, "Screenshot saved in /Pictures/Screenshots", Toast.LENGTH_SHORT).show();
                //SnackbarUtility.showSnackbarTypeLong(settingsButton, "Screenshot saved in /Pictures/Screenshots");




            } else {

                Toast.makeText(this, "Failed to take screenshot", Toast.LENGTH_SHORT).show();
                //SnackbarUtility.showSnackbarTypeLong(settingsButton, "Failed to take screenshot");

            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
    }


    public void saveBitmapToDisk(Bitmap bitmap,String filename) throws IOException {



        String path = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(path+File.separator+"/saved_images");
        myDir.mkdirs();

        String fname = filename;
        File file = new File (myDir, fname);

//        if (file.exists ())
//        {
//            file.createNewFile();
//        };
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(this, "Success to take screenshot", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Failed to take screenshot", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//
//        if (videoDirectory == null) {
//            videoDirectory =
//                    new File();
//        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        String formattedDate = df.format(c.getTime());

        File mediaFile = new File(videoDirectory, "FieldVisualizer"+formattedDate+".jpeg");

        FileOutputStream fileOutputStream = new FileOutputStream(mediaFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
