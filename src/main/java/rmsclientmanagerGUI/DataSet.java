package rmsclientmanagerGUI;


import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultValueDataset;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Facade DataSet Class
 *
 */
public class DataSet {
    private DefaultCategoryDataset datasetCategory;
    private DefaultValueDataset dataSetValue;

    /***
     * Costruction of object dataset.
     * @param type if is 'chartline' crate a dataset for this type chart. Else for other
     */
    public DataSet(String type){
        type=type.toLowerCase();
        if(type.contains("chartline"))
            this.datasetCategory=new DefaultCategoryDataset();
        else
            this.dataSetValue=new DefaultValueDataset();    
    }

    /***
     * Set Method to dataset of value
     * @param value
     */
     public void setDataSetValue(Double value){
         this.dataSetValue.setValue(value);
     }

    /***
     * Set Method to dataset category
     * @param value to ploat
     * @param title of date
     * @param time  time of migration
     */
     public void setDataSetValue(Double value,String title,String time){
         this.datasetCategory.addValue(value, title , time);
     }

    /***
     * Get Method for Data Set Category
     * @return object category
     */
    public DefaultCategoryDataset getDatasetCategory() {
        return datasetCategory;
    }

    /***
     * Get Method for Data Set Value
     * @return
     */
    public DefaultValueDataset getDataSetValue() {
        return dataSetValue;
    }
     
}
