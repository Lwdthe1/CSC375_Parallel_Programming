package com.lincolnwdaniel.ltrain.utils;

import android.content.Context;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by lwdthe1 on 1/8/2015.
 */
public class LColor {

    public static int getRColorById(Context context, int colorId) {
        return context.getResources().getColor(colorId);
    }

    public static class Flat {
        //create final hashmap storage of the flatui colors
        private static final Map<String, String> fUIColorsMap;

        static {
            fUIColorsMap = new HashMap<String, String>();
            fUIColorsMap.put("turquoise", "#1ABC9C");
            fUIColorsMap.put("emerald", "#2ECC71");
            fUIColorsMap.put("peter river", "#3498DB");
            fUIColorsMap.put("amethyst", "#9B59B6");
            fUIColorsMap.put("green sea", "#16A085");
            fUIColorsMap.put("nephritis", "#27AE60");
            fUIColorsMap.put("belize hole", "#2980B9");
            fUIColorsMap.put("wisteria", "#8E44AD");
            fUIColorsMap.put("wet asphalt", "#34495E");
            fUIColorsMap.put("sun flower", "#F1C40F");
            fUIColorsMap.put("carrot", "#E67E22");
            fUIColorsMap.put("alizarin", "#E74C3C");
            fUIColorsMap.put("midnight blue", "#2C3E50");
            fUIColorsMap.put("orange", "#F39C12");
            fUIColorsMap.put("pumpkin", "#D35400");
            fUIColorsMap.put("pomegranate", "#C0392B");
            fUIColorsMap.put("clouds", "#ECF0F1");
            fUIColorsMap.put("concrete", "#95A5A6");
            fUIColorsMap.put("silver", "#BDC3C7");
            fUIColorsMap.put("asbestos", "#7F8C8D");
        }

        private static final String[] fUIColorsArray = {
                "#1abc9c",
                "#2ecc71",
                "#3498db",
                "#9b59b6",
                "#34495e",
                "#16a085",
                "#27ae60",
                "#2980b9",
                "#8e44ad",
                "#95a5a6",
                "#2c3e50",
                "#f1c40f",
                "#e67e22",
                "#e74c3c",
                "#f39c12",
                "#d35400",
                "#c0392b",
                "#bdc3c7",
                "#7f8c8d"

        };

        private static final List<String> fUIColorsList = new ArrayList<>(Arrays.asList(
                "#1abc9c",
                "#2ecc71",
                "#3498db",
                "#9b59b6",
                "#34495e",
                "#16a085",
                "#27ae60",
                "#2980b9",
                "#8e44ad",
                "#95a5a6",
                "#2c3e50",
                "#f1c40f",
                "#e67e22",
                "#e74c3c",
                "#f39c12",
                "#d35400",
                "#c0392b",
                "#bdc3c7",
                "#7f8c8d"
        ));

        private static final List<String> fUIColorsBrightList = new ArrayList<String>(Arrays.asList(
                "#1abc9c",
                "#2ecc71",
                "#3498db",
                "#9b59b6",
                "#16a085",
                "#27ae60",
                "#2980b9",
                "#8e44ad",
                "#f1c40f",
                "#e67e22",
                "#e74c3c",
                "#f39c12",
                "#d35400",
                "#c0392b"
        ));


        //public methods to retrieve colors
        public static int getColorI(String color) {
            color = color.toLowerCase();
            if (fUIColorsMap.containsKey(color)) {
                String fUIColor = fUIColorsMap.get(color);
                return Color.parseColor(fUIColor);
            } else {
                return 0;
            }
        }

        public static String getColorS(String color) {
            color = color.toLowerCase();
            if (fUIColorsMap.containsKey(color)) {
                String fUIColor = fUIColorsMap.get(color);
                return fUIColor;
            } else {
                return "";
            }
        }

        public static int getRandomColor() {
            Random randGen = new Random();
            int randInt = randGen.nextInt(fUIColorsList.size());

            return Color.parseColor(fUIColorsList.get(randInt));
        }

        public static int getRandomBrightColor() {
            Random randGen = new Random();
            int randInt = randGen.nextInt(fUIColorsBrightList.size());

            return Color.parseColor(fUIColorsBrightList.get(randInt));
        }

        public static String[] fUIColorsArray() {
            return fUIColorsArray;
        }

        public static List<String> fUIColorsList() {
            return fUIColorsList;
        }
    }
}

