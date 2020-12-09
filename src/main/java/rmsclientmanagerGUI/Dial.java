/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmsclientmanagerGUI;


import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultValueDataset;


/**
 *
 * @author L.RUSSO62
 */
public class Dial {
     private ChartPanel chartPanel;
     private DialPlot plot;
     private DataSet dataset;
     public Dial(int width,int height,String titleChart,int minval,int maxval,int gap,DataSet dataset){
         this.dataset=dataset;
        DialPlot plot=new DialPlot(this.dataset.getDataSetValue());
        plot.setDialFrame(new StandardDialFrame());
        plot.addLayer(new DialValueIndicator(0));
        plot.addLayer(new DialPointer.Pointer());
        StandardDialScale scale = new StandardDialScale(minval,
            maxval, -120, -300, gap, gap - 1);
        scale.setTickRadius(0.88);
        scale.setTickLabelOffset(0.20);
        plot.addScale(0, scale);
         JFreeChart freechart=new JFreeChart(plot);
         this.chartPanel = new ChartPanel(new JFreeChart(plot));
         chartPanel.setPreferredSize( new java.awt.Dimension(200,200));
         chartPanel.setVisible(true);
     }

    public DataSet getDataset() {
        return dataset;
    }

    public void setDataset(DataSet dataset) {
        this.dataset = dataset;
        this.plot.setDataset(dataset.getDataSetValue());
    }
    
     public ChartPanel getChartPannel(){
         return this.chartPanel;
     }
    
}
