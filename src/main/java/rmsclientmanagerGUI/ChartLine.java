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
 * Facade class of jFreeChart Library
 *
 */
public class ChartLine {
    
     private ChartPanel chartPanel;
     private DataSet dataset;

    /**
     * Constructor of plot's object
     * @param width
     * @param height
     * @param titleChart
     * @param dataset
     */
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

    /***
     * Get method for DataSet used in a plot
     * @return
     */
    public DefaultCategoryDataset getDataset() {
        return dataset.getDatasetCategory();
    }

    /***
     * Set Method to dataset in a plot
     * @param dataset Dataset to insert
     */
    public void setDataset(DataSet dataset) {
        this.dataset = dataset;
    }

    /***
     * Get Method for Chart Panel
     * @return
     */
     public ChartPanel getChartPanel(){
         return this.chartPanel;
     }
}
