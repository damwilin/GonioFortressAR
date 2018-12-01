package com.lionapps.wili.goniofortressar;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.util.concurrent.CompletableFuture;

public class AugmentedImageNode extends AnchorNode {
    private static final String TAG = AugmentedImageNode.class.getSimpleName();
    private static CompletableFuture<ModelRenderable> modelFuture;
    private AugmentedImage image;

    public AugmentedImageNode(Context context, String filename){
        if (modelFuture == null){
            modelFuture = ModelRenderable.builder().setRegistryId("modelFuture")
                    .setSource(context, Uri.parse(filename))
                    .build();
        }
    }

    public void setImage(AugmentedImage image){
        this.image = image;

        if (!modelFuture.isDone()){
            CompletableFuture.allOf(modelFuture).thenAccept((Void aVoid) -> {
                setImage(image);
            }).exceptionally(throwable -> {
                Log.e(TAG, "Exception loading", throwable);
                return null;
            });
        }
        setAnchor(image.createAnchor(image.getCenterPose()));
        Node node = new Node();

        //Pose pose = Pose.makeTranslation(0f,0f,0f);
        //node.setLocalPosition(new Vector3(pose.tx(), pose.ty(), pose.tz()));
        //node.setLocalRotation(new Quaternion(pose.qx(), pose.qy(), pose.qz(), pose.qw()));

        node.setParent(this);
        node.setRenderable(modelFuture.getNow(null));
    }

    public AugmentedImage getImage(){
        return image;
    }

}
