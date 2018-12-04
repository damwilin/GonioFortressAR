package com.lionapps.wili.goniofortressar;

import android.util.Log;

import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;

public class CustomArFragment extends ArFragment {
    @Override
    protected Config getSessionConfiguration(Session session) {
        //Switching off hand on the start
        getPlaneDiscoveryController().hide();
        getPlaneDiscoveryController().setInstructionView(null);
        Config config = new Config(session);
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
        session.configure(config);
        getArSceneView().setupSession(session);
        disablePlaneRender();
        if (((MainActivity) getActivity()).setupAugmentedImagesDb(config, session)){
            Log.d("SetupAugImgDb", "Success");
        } else
            Log.e("SetupAugImgDb", "Fail");
        return config;
    }

    private void disablePlaneRender(){
        getArSceneView().getPlaneRenderer().setEnabled(false);
    }
}
