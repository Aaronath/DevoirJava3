package Controlers;

import Entities.DatasGraph;
import Tools.ConnexionBDD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CtrlGraphique
{
    private Connection cnx;
    private PreparedStatement ps;
    private ResultSet rs;

    public CtrlGraphique() {
        cnx = ConnexionBDD.getCnx();
    }

    public TreeMap<Integer, Float> getAvgSalaireByAge() throws SQLException {
        TreeMap<Integer, Float> lesAvgByAge = new TreeMap<>();

        ps = cnx.prepareStatement("SELECT AVG(salaireEmp) AS avgSalaire, ageEmp FROM employe GROUP BY ageEmp;");
        rs = ps.executeQuery();
        while (rs.next()){
            lesAvgByAge.put(rs.getInt("ageEmp"), rs.getFloat("avgSalaire"));
        }
        return lesAvgByAge;
    }

    public HashMap<String, Integer> getPourcentageBySexe() throws SQLException {
        HashMap<String, Integer> lesPourcentages = new HashMap<>();

        ps = cnx.prepareStatement("SELECT COUNT(numEmp) / 200 * 100 AS pourcentage, sexe FROM employe GROUP BY sexe;");
        rs = ps.executeQuery();
        while(rs.next()){
            lesPourcentages.put(rs.getString("sexe"), rs.getInt("pourcentage"));
        }
        return lesPourcentages;
    }

    public HashMap<String, HashMap<String,Integer>> getCountByTrancheAndSexe() throws SQLException {
        HashMap<String, HashMap<String,Integer>> lesCounts = new HashMap<>();
        HashMap<String, Integer> lesSexes = new HashMap<>();

        ps = cnx.prepareStatement("SELECT COUNT(numEmp) as \"10-19\", sexe FROM employe WHERE ageEmp BETWEEN 10 and 19 GROUP BY sexe;");
        rs = ps.executeQuery();
        while (rs.next()){
            lesSexes.put(rs.getString(2), rs.getInt(1));
            lesCounts.put("10-19", lesSexes);        }
        ps.close();
        rs.close();

        ps = cnx.prepareStatement("SELECT COUNT(numEmp) as \"20-29\", sexe FROM employe WHERE ageEmp BETWEEN 20 and 29 GROUP BY sexe;");
        rs = ps.executeQuery();
        while (rs.next()){
            lesSexes.put(rs.getString(2), rs.getInt(1));
            lesCounts.put("20-29", lesSexes);
        }
        ps.close();
        rs.close();

        ps = cnx.prepareStatement("SELECT COUNT(numEmp) as \"30-39\", sexe FROM employe WHERE ageEmp BETWEEN 30 and 39 GROUP BY sexe;");
        rs = ps.executeQuery();
        while (rs.next()){
            lesSexes.put(rs.getString(2), rs.getInt(1));
            lesCounts.put("30-39", lesSexes);        }
        ps.close();
        rs.close();

        ps = cnx.prepareStatement("SELECT COUNT(numEmp) as \"40-49\", sexe FROM employe WHERE ageEmp BETWEEN 40 and 49 GROUP BY sexe;");
        rs = ps.executeQuery();
        while (rs.next()){
            lesSexes.put(rs.getString(2), rs.getInt(1));
            lesCounts.put("40-49", lesSexes);        }
        ps.close();
        rs.close();

        ps = cnx.prepareStatement("SELECT COUNT(numEmp) as \"50+\", sexe FROM employe WHERE ageEmp > 50 GROUP BY sexe;");
        rs = ps.executeQuery();
        while (rs.next()){
            lesSexes.put(rs.getString(2), rs.getInt(1));
            lesCounts.put("50+", lesSexes);        }
        ps.close();
        rs.close();

        return lesCounts;
    }

    public HashMap<String, HashMap<String, Integer>> getMontants() throws SQLException {
        HashMap<String, HashMap<String, Integer>> lesSemestres = new HashMap<>();
        HashMap<String, Integer> lesMontants = new HashMap<>();

        ps = cnx.prepareStatement("SELECT SUM(montant), nomMagasin, nomSemestre FROM vente GROUP BY nomSemestre, nomMagasin;");
        rs = ps.executeQuery();
        while (rs.next()){
            lesMontants.put(rs.getString(2), rs.getInt(1));
            if (!lesSemestres.containsKey(rs.getString(3))){
                lesSemestres.put(rs.getString(3), lesMontants);

            }
        }

        return lesSemestres;
    }
}
