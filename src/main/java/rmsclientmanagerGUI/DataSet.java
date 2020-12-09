package rmsclientmanagerGUI;


import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultValueDataset;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author L.RUSSO62
 */
public class DataSet {
    private DefaultCategoryDataset datasetCategory;
    private DefaultValueDataset dataSetValue;
    
    public DataSet(String type){
        type=type.toLowerCase();
        if(type.contains("chartline"))
            this.datasetCategory=new DefaultCategoryDataset();
        else
            this.dataSetValue=new DefaultValueDataset();    
    }
     public void setDataSetVale(Double value){
         this.dataSetValue.setValue(value);
     }
     public void setDataSetValue(Double value,String title,String time){
         this.datasetCategory.addValue(value, title , time);
     }

    public DefaultCategoryDataset getDatasetCategory() {
        return datasetCategory;
    }

    public DefaultValueDataset getDataSetValue() {
        return dataSetValue;
    }
     
}
