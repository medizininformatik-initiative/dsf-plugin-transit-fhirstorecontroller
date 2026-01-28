package de.fraunhofer.isst.health.store.utils;


import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringSubstitutor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Renderer {

    public static final String CHARTADDITIONS = "/templates/AdditionChart.txt";
    public static final String INSTALL = "/templates/Install.txt";
    public static final String CHART = "/templates/Chart.txt";
    public static final String VALUES = "/templates/values.txt";

    public static String renderInstall(String dupId) throws IOException {
        try {
            return getString(dupId, 0, INSTALL);
        } catch (IOException e) {
            throw new IOException("Install.txt template for FileStorage not found at " + INSTALL);
        }
    }

    public static String renderChartAdditions(String dupId) throws IOException {
        try {
            return getString(dupId, 0, CHARTADDITIONS);
        } catch (IOException e) {
            throw new IOException("AdditionChart.txt template for FileStorage not found at " + CHARTADDITIONS);
        }
    }
    public static String renderChart(String dupId) throws IOException {
        try {
            return getString(dupId, 0, CHART);
        } catch (IOException e) {
            throw new IOException("Chart.txt template for FileStorage not found at " + CHART);
        }
    }
    public static String renderValues(String dupId, double size) throws IOException {
        try {
            return getString(dupId, size, VALUES);
        } catch (IOException e) {
            throw new IOException("values.txt template for FileStorage not found at " + VALUES);
        }
    }
    private static String getString(String dupId, double size, String charts) throws IOException {
        String template = IOUtils.resourceToString(charts, StandardCharsets.UTF_8);
        HashMap<String, String> context = new HashMap<>();
        context.put("dupId", dupId);
        context.put("size", String.valueOf(size));
        StringSubstitutor substitutor = new StringSubstitutor(context);
        return substitutor.replace(template);
    }

    public static String renderManifest(String template, HashMap<String, String> context){
        StringSubstitutor substitutor = new StringSubstitutor(context);
        return substitutor.replace(template);
    }
}
