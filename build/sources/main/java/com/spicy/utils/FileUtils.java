package com.spicy.utils;

import net.minecraft.util.Tuple;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static File getConfigDir() {
        File file = new File(System.getenv("APPDATA"), ".spicy");
        if (!file.exists())
            file.mkdir();
        return file;
    }

    public static Tuple<Boolean, File> getConfigFile(String name) {
        File file = new File(getConfigDir(), String.format("%s.sxf", name));
        if (!file.exists())
            try {
                file.createNewFile();
                return new Tuple<>(true, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return new Tuple<>(false, file);
    }

    public static List<String> read(File inputFile) {
        List<String> readContent = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8));
            String str;
            while ((str = in.readLine()) != null)
                readContent.add(str);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readContent;
    }

    public static void write(File outputFile, List<String> writeContent, boolean overrideContent) {
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8));
            for (String outputLine : writeContent)
                out.write(outputLine + System.getProperty("line.separator"));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}