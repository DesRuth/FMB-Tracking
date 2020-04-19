package com.pkvv.fmb_tracking.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.pkvv.fmb_tracking.R;
import com.pkvv.fmb_tracking.models.CluserMarker;

public class MyClusterManagerRenderer extends DefaultClusterRenderer<CluserMarker>  {
 private final IconGenerator iconGenerator;
 private final ImageView imageView;
 private final int markerWidth;
 private final int markerHeight;

    public MyClusterManagerRenderer(Context context, GoogleMap map, ClusterManager<CluserMarker> clusterManager ) {
        super(context, map, clusterManager);


        iconGenerator =new IconGenerator(context.getApplicationContext());
        imageView = new ImageView(context.getApplicationContext());
       // markerWidth = (int) context.getResources().getDimension(android.R.dimen.thumbnail_width);
       // markerHeight = (int) context.getResources().getDimension(android.R.dimen.thumbnail_height);
        markerWidth=150;
        markerHeight=150;
        imageView.setLayoutParams(new ViewGroup.LayoutParams( markerWidth,markerHeight));
        imageView.setPadding(1,1,1,1);
        iconGenerator.setContentView(imageView);


    }

    @Override
    protected void onBeforeClusterItemRendered(CluserMarker item, MarkerOptions markerOptions) {
        imageView.setImageResource(item.getIconPicture());
        Bitmap icon = iconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.getTitle());
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<CluserMarker> cluster) {
        return false;
    }

    /**
     * Update the GPS coordinate of a ClusterItem
     * @param clusterMarker
     */
    public void setUpdateMarker(CluserMarker clusterMarker) {
        Marker marker = getMarker(clusterMarker);
        if (marker != null) {
            marker.setPosition(clusterMarker.getPosition());
        }
    }
}
