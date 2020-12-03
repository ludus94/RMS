package GUI;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainCategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class graphicGUI extends JFrame {
    private final ChartPanel chartPanel;
    public graphicGUI() {
        JFreeChart jFreeChart=this.createChart();
        chartPanel = new ChartPanel(this.createChart());
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    public CategoryDataset createDataset1() {

        final DefaultCategoryDataset result = new DefaultCategoryDataset();
        // row keys...
        final String series1 = "First";
        // column keys...
        final String type1 = "Type 1";
        final String type2 = "Type 2";
        result.addValue(1.0, series1, type1);
        result.addValue(4.0, series1, type2);

        return result;
    }

    private JFreeChart createChart() {

        final CategoryDataset dataset1 = createDataset1();
        final NumberAxis rangeAxis1 = new NumberAxis("Value");
        rangeAxis1.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        final LineAndShapeRenderer renderer1 = new LineAndShapeRenderer();
        final CategoryPlot subplot1 = new CategoryPlot(dataset1, null, rangeAxis1, renderer1);
        subplot1.setDomainGridlinesVisible(true);

        final CategoryAxis domainAxis = new CategoryAxis("Category");
        final CombinedDomainCategoryPlot plot = new CombinedDomainCategoryPlot(domainAxis);
        plot.add(subplot1, 2);

        final JFreeChart result = new JFreeChart("Combined Domain Category Plot Demo", new Font("SansSerif", Font.BOLD, 12), plot,true);
        return result;

    }
}
