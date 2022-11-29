package Vues;

import Controlers.CtrlGraphique;
import Entities.DatasGraph;
import Tools.ConnexionBDD;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultKeyedValues2DDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class FrmGraphique extends JFrame{
    private JPanel pnlGraph1;
    private JPanel pnlGraph2;
    private JPanel pnlGraph3;
    private JPanel pnlGraph4;
    private JPanel pnlRoot;
    CtrlGraphique ctrlGraphique;

    public FrmGraphique() throws SQLException, ClassNotFoundException {
        this.setTitle("Devoir graphique");
        this.setContentPane(pnlRoot);
        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);

        ConnexionBDD cnx = new ConnexionBDD();
        ctrlGraphique = new CtrlGraphique();

        DefaultCategoryDataset datas = new DefaultCategoryDataset();
        TreeMap<Integer, Float> lesAvgByAge = ctrlGraphique.getAvgSalaireByAge();

        float avgSalaire = 0;
        for (int ageEmp : lesAvgByAge.keySet()){
            avgSalaire = lesAvgByAge.get(ageEmp);
            datas.setValue(Double.parseDouble(String.valueOf(avgSalaire)), "", String.valueOf(ageEmp));
        }
        JFreeChart chart1 = ChartFactory.createLineChart("Moyenne des salaires par âge", "Age", "Salaire moyen", datas);
        ChartPanel graph1 = new ChartPanel(chart1);
        pnlGraph1.add(graph1);
        pnlGraph1.validate();

        DefaultPieDataset datas2 = new DefaultPieDataset<>();
        HashMap<String, Integer> lesPourcentages = ctrlGraphique.getPourcentageBySexe();
        int unPourcentage = 0;
        for (String sexe : lesPourcentages.keySet()){
            unPourcentage = lesPourcentages.get(sexe);
            datas2.setValue(sexe, unPourcentage);
        }
        JFreeChart chart2 = ChartFactory.createRingChart(
                "Pourcentage de femmes et d'hommes",
                datas2,true,false,false);
        RingPlot plot = (RingPlot) chart2.getPlot();
        plot.setBackgroundPaint(new Color(240,240,240));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{1}"));
        ChartPanel graph2 = new ChartPanel(chart2);
        pnlGraph2.add(graph2);
        pnlGraph2.validate();

        DefaultKeyedValues2DDataset data = new DefaultKeyedValues2DDataset ();
        HashMap<String, HashMap<String, Integer>> lesCounts = ctrlGraphique.getCountByTrancheAndSexe();
        int countHomme = 0;
        int countFemme = 0;
        for (String tranche : lesCounts.keySet()){
            for (String sexe : lesCounts.get(tranche).keySet()){
                if (sexe.compareTo("Homme")==0){
                    countHomme = lesCounts.get(tranche).get(sexe) * (-1);
                    data.setValue( countHomme, "Male", tranche);
                }
                countFemme = lesCounts.get(tranche).get(sexe);
                data.setValue( countFemme, "FeMale", tranche);
            }


        }
        JFreeChart chart3 = ChartFactory.createStackedBarChart(
                "Homme/Femmes",
                "Tranche d'âge",     // domain axis label
                "Sexe", // range axis label
                data         // data
                // include legend
                // tooltips
        );
        CategoryPlot plot3 = chart3.getCategoryPlot();
        ChartPanel graph3 = new ChartPanel(chart3);
        pnlGraph3.add(graph3);
        pnlGraph3.validate();


        DefaultCategoryDataset donnees = new DefaultCategoryDataset();
        HashMap<String, HashMap<String, Integer>> lesSemestres = ctrlGraphique.getMontants();
        HashMap<String, Integer> lesMags = new HashMap<>();
        int montant = 0;

        for (String unSemestre : lesSemestres.keySet()){
            lesMags = lesSemestres.get(unSemestre);
            for (String unMagasin : lesMags.keySet()){
                montant = lesSemestres.get(unSemestre).get(unMagasin);
                donnees.setValue(Double.parseDouble(String.valueOf(montant)), unMagasin, unSemestre);
            }
        }

        JFreeChart chart4 = ChartFactory.createBarChart(
                "Les montants par semestres",
                "Semestres",
                "Montant",
                donnees,
                PlotOrientation.VERTICAL,
                true, true, false);
        ChartPanel graph4 = new ChartPanel(chart4);
        pnlGraph4.add(graph4);
        pnlGraph4.validate();
    }

}
