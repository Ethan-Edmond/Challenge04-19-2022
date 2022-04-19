package com.ethanedmond;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildOrderResolver {
    public static String[] resolve(String[] projects, List<String[]> dependencies) throws Exception {
        String[] buildOrder = new String[projects.length];
        for (int i = 0; i < projects.length; i++) {
            buildOrder[i] = projects[i];
        }


        Map<String, Integer> comparatorMap = new HashMap<>();
        for (String project : projects) {
            comparatorMap.put(project, 1);
        }

        // enforcing that children have higher values than parents in the comparatorMap
        boolean resolvingDeps = true;
        while (resolvingDeps) {
            resolvingDeps = false;
            for (String[] dependency : dependencies) {
                Integer parentVal = comparatorMap.get(dependency[0]);
                Integer childVal = comparatorMap.get(dependency[1]);
                if (parentVal >= childVal) {
                    if (parentVal > projects.length) {
                        throw new Exception("Circular dependency detected");
                    }
                    comparatorMap.put(dependency[1], parentVal + 1);
                    resolvingDeps = true;
                }
            }
        }

        // bubblesort by comparatorMap value
        for (int i = buildOrder.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (comparatorMap.get(buildOrder[j]) > comparatorMap.get(buildOrder[j + 1])) {
                    String temp = buildOrder[j];
                    buildOrder[j] = buildOrder[j + 1];
                    buildOrder[j + 1] = temp;
                }
            }
        }

        return buildOrder;
    }
}
