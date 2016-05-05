package com.example.a.finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class kdaGraph extends AppCompatActivity{

    double averageKDA = 0;
    double totalKDA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kda_graph);

        Button back = (Button) findViewById(R.id.back);

        //Gets the match list from the Stats class
        Intent intent = getIntent();
        Match[] matches = (Match[]) getIntent().getSerializableExtra("matches");
        double[] graphContent = new double[matches.length];

        //Gathers relevant information from the Match[] array
        for(int i = 0; i < matches.length; i++)
        {
            Match match = matches[i];
            graphContent[i] = match.getKDA();
            totalKDA += match.getKDA();
        }
        averageKDA = totalKDA / matches.length;
        double lastKDA = graphContent[graphContent.length - 1];
        double[] futurePoints = predictGraph(graphContent, lastKDA);

        //
        // Begin creating the graph points
        //

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Matches");
        graph.getGridLabelRenderer().setVerticalAxisTitle("KDA by match");
        graph.getGridLabelRenderer().setPadding(1);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(1,graphContent[0]),
                new DataPoint(2,graphContent[1]),
                new DataPoint(3,graphContent[2]),
                new DataPoint(4,graphContent[3]),
                new DataPoint(5,graphContent[4]),
                new DataPoint(6,graphContent[5]),
                new DataPoint(7,graphContent[6]),
                new DataPoint(8,graphContent[7]),
                new DataPoint(9,graphContent[8]),
                new DataPoint(10,graphContent[9])
        });
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);
        series.setColor(Color.RED);
        graph.addSeries(series);

        LineGraphSeries<DataPoint> future = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(10, graphContent[9]),
                new DataPoint(11,futurePoints[0]),
                new DataPoint(12,futurePoints[1]),
                new DataPoint(13,futurePoints[2]),
                new DataPoint(14,futurePoints[3]),
                new DataPoint(15,futurePoints[4]),
        });
        future.setDrawDataPoints(true);
        future.setDataPointsRadius(10);
        future.setThickness(5);
        future.setColor(Color.BLUE);
        graph.addSeries(future);

        //Returns to the Stats class
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //
    // End creating graph points
    //

    //A makeshift prediction algorithm for the next 5 games.
    public double[] predictGraph(double[] matches, double lastKDA)
    {
        double[] points = new double[5];
        int index = 0;
        int i = 0;
        while(i < matches.length - 1)
        {
            double kda1 = matches[i];
            System.out.println("kda1 = "+kda1);
            double kda2 = matches[i+1];
            System.out.println("kda2 = "+kda2);

            double netKDA = kda2-kda1;
            double nextKDA = netKDA + lastKDA;
            double dampeningFactor = 1/averageKDA;
            dampeningFactor = 1 - dampeningFactor;
            nextKDA = nextKDA*dampeningFactor;
            points[index] = Math.abs(nextKDA);
            index++;
            i+=2;
        }
        return points;
    }
}