package com.github.malkovarseny.anychartandroidvr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.EnumsAlign;
import com.anychart.anychart.LegendLayout;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;
import com.anychart.anychart.chart.common.Event;
import com.anychart.anychart.chart.common.ListenersInterface;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private ViewRenderable anyChartRenderable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ar_fragment);

        ViewRenderable.builder()
                .setView(this, R.layout.view_anychart)
                .build()
                .thenAccept(viewRenderable -> anyChartRenderable = viewRenderable);

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (anyChartRenderable == null) {
                        return;
                    }

//                    if (plane.getType() != Plane.Type.HORIZONTAL_UPWARD_FACING) {
//                        return;
//                    }

                    Pie pie = AnyChart.pie();

                    List<DataEntry> data = new ArrayList<>();
                    data.add(new ValueDataEntry("Apples", 6371664));
                    data.add(new ValueDataEntry("Pears", 789622));
                    data.add(new ValueDataEntry("Bananas", 7216301));
                    data.add(new ValueDataEntry("Grapes", 1486621));
                    data.add(new ValueDataEntry("Oranges", 1200000));

                    pie.setData(data);

                    pie.setTitle("Fruits imported in 2015 (in kg)");

                    ((AnyChartView) anyChartRenderable.getView()).setChart(pie);

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(anyChartRenderable);
                    andy.select();
                });
    }
}
