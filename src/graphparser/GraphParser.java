/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package graphparser;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Felice
 */
public class GraphParser {

    public static void main(String[] args) throws IOException {

        File input = new File("index.xgml");
        File output = new File("output.txt");

        FileWriter writer = new FileWriter("output.txt");
        Document doc;
        EdgeClass edge;

        int i = 0;
        int j = 0;
        boolean checkFirst = true;
        int maxWeight = 0;

        try {
            doc = Jsoup.parse(input, "UTF-8");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }

        // parses nodes and edges data
        Elements numberNodes = doc.select("section [name=node]");
        Elements idNodes = numberNodes.select("attribute[key=id]");
        Elements nameNodes = numberNodes.select("attribute[key=label]");
        Elements numberEdges = doc.select("section [name=edge]");
        Elements sourceEdges = numberEdges.select("attribute[key=source]");
        Elements targetEdges = numberEdges.select("attribute[key=target]");

        ArrayList<EdgeClass> edges = new ArrayList();
        ArrayList subNodes = new ArrayList();

        // sets source and target node for each edge
        for (i = 0; i < numberEdges.size(); i++) {

            edge = new EdgeClass();
            edge.setSourceNode(Integer.parseInt(sourceEdges.get(i).text()));
            edge.setTargetNode(Integer.parseInt(targetEdges.get(i).text()));

            edges.add(edge);
        }

        // sets type for each edge
        for (i = 0; i < numberEdges.size(); i++) {

            String type;

            type = String.valueOf(numberEdges.get(i).select("attribute[key=targetArrow]").text());

            if (type.isEmpty()) {

                edges.get(i).setType("edge");
            } else {

                edges.get(i).setType("arco");

            }

            // sets weight for each edge
            String weight;

            weight = String.valueOf(numberEdges.get(i).select("attribute[key=label]").text());

            if (weight.isEmpty()) {

                edges.get(i).setWeight(1);

            } else {

                edges.get(i).setWeight(Integer.parseInt(weight));

            }

            // max weight
            if (weight.length() > maxWeight) {

                maxWeight = weight.length();
            }

        }
        for (i = 0; i < idNodes.size(); i++) {

            subNodes.add(Integer.valueOf(nameNodes.get(i).text().substring(1)));

        }

        // sort source and target nodes
        for (i = 0; i < edges.size(); i++) {
            checkFirst = true;
            for (j = 0; j < nameNodes.size(); j++) {
                if (checkFirst) {
                    if (edges.get(i).getSourceNode() == j) {

                        edges.get(i).setSourceNode((int) subNodes.get(j) - 1);
                        checkFirst = false;
                    }
                }
            }
        }

        for (i = 0; i < edges.size(); i++) {
            checkFirst = true;
            for (j = 0; j < nameNodes.size(); j++) {
                if (checkFirst) {
                    if (edges.get(i).getTargetNode() == j) {

                        edges.get(i).setTargetNode((int) subNodes.get(j) - 1);
                        checkFirst = false;
                    }
                }
            }
        }

        // build the matrix
        int matrice[][] = new int[idNodes.size()][idNodes.size()];

        for (i = 0; i < edges.size(); i++) {

            if (edges.get(i).getWeight() == 0) {

                if (edges.get(i).getType().equals("edge")) {

                    matrice[edges.get(i).getSourceNode()][edges.get(i).getTargetNode()] = 1;
                    matrice[edges.get(i).getTargetNode()][edges.get(i).getSourceNode()] = 1;

                } else {

                    matrice[edges.get(i).getSourceNode()][edges.get(i).getTargetNode()] = 1;

                }
            } else {

                if (edges.get(i).getType().equals("edge")) {

                    matrice[edges.get(i).getSourceNode()][edges.get(i).getTargetNode()] = edges.get(i).getWeight();
                    matrice[edges.get(i).getTargetNode()][edges.get(i).getSourceNode()] = edges.get(i).getWeight();

                } else {

                    matrice[edges.get(i).getSourceNode()][edges.get(i).getTargetNode()] = edges.get(i).getWeight();

                }

            }
        }

        // output
        try {

            writer.write(idNodes.size() + "\n");

            int indentSize;

            if (maxWeight == 0) {

                indentSize = +2;
            } else {

                indentSize = maxWeight + 1;
            }

            for (i = 0; i < matrice.length; i++) {

                for (j = 0; j < matrice[i].length; j++) {

                    String formatted = String.format("%" + indentSize + "d", matrice[i][j]);
                    writer.write(formatted);

                }

                writer.write("\n");

            }

            writer.close();

            System.out.println("Output written in file" + "output.txt");

        } catch (IOException e) {

            System.err.println("Error during file writing: " + e.getMessage());

        }

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(output);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Press ENTER to continue...");
        scanner.nextLine();
        System.out.println("Program ended with exit code 0.");
        System.exit(0);
    }
}
