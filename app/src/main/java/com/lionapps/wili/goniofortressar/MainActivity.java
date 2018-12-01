package com.lionapps.wili.goniofortressar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.FrameTime;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private CustomArFragment arFragment;
    private boolean isModelAdded = false;
    private static final String JPG_FILE_NAME = "mosaic.jpeg";
    private static final String FILE_NAME = "mosaic";
    private static final String SFB_FILE_NAME = "mosaic.sfb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);
        initializeSceneView();
    }

    public boolean setupAugmentedImagesDb(Config config, Session session) {
        AugmentedImageDatabase augmentedImageDatabase;
        Bitmap augmentedImageBitmap = loadAugmentedImage();
        if (augmentedImageBitmap == null)
            return false;

        augmentedImageDatabase = new AugmentedImageDatabase(session);
        augmentedImageDatabase.addImage(FILE_NAME, augmentedImageBitmap);
        config.setAugmentedImageDatabase(augmentedImageDatabase);
        return true;
    }

    private Bitmap loadAugmentedImage() {
        try {
            InputStream is = getAssets().open(JPG_FILE_NAME);
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            Log.e(TAG, "IO exception loading augmented image bitmap", e);
        }
        return null;
    }

    private void onUpdateFrame(FrameTime frameTime) {
        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<AugmentedImage> updatedAugmentedImages = frame.getUpdatedTrackables(AugmentedImage.class);

        for (AugmentedImage augmentedImage : updatedAugmentedImages) {
            if (augmentedImage.getTrackingState() == TrackingState.TRACKING && !isModelAdded) {
                if (augmentedImage.getName().equals(FILE_NAME)) {
                    AugmentedImageNode node = new AugmentedImageNode(this, SFB_FILE_NAME);
                    node.setImage(augmentedImage);
                    arFragment.getArSceneView().getScene().addChild(node);
                    isModelAdded = true;
                }
            }
        }
    }

    private void initializeSceneView() {
        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);
    }
}



