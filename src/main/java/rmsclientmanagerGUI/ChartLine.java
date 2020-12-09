/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmsclientmanagerGUI;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author L.RUSSO62
 */
public class ChartLine {
    
     private ChartPanel chartPanel;
     private DataSet dataset;
     
     public ChartLine(int width,int height,String titleChart,DataSet dataset){
         this.dataset=dataset;
         JFreeChart lineChartTemperature = ChartFactory.createLineChart(
         titleChart,
         null,null,
         this.dataset.getDatasetCategory(),
         PlotOrientation.VERTICAL,
         false,true,false);
         this.chartPanel = new ChartPanel( lineChartTemperature );
         chartPanel.setPreferredSize( new java.awt.Dimension(width ,height));
         chartPanel.setVisible(true);
     }

    public DefaultCategoryDataset getDataset() {
        return dataset.getDatasetCategory();
    }

    public void setDataset(DataSet dataset) {
        this.dataset = dataset;
    }
     
     public ChartPanel getChartPannel(){
         return this.chartPanel;
     }
}
