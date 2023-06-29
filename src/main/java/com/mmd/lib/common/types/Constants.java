package com.mmd.lib.common.types;

import java.nio.file.Path;
import java.util.Locale;

public class Constants {
    public static class stringPathBits {
        public static String basePath = "mmdlib-data";
        public static String assetsPath = "assets";
        public static String materialsPath = LocationType.MATERIAL.getMyTypeName().toLowerCase(Locale.ENGLISH);
        public static String itemsPath = LocationType.ITEM.getMyTypeName().toLowerCase(Locale.ENGLISH);
        public static String blocksPath = LocationType.BLOCK.getMyTypeName().toLowerCase(Locale.ENGLISH);
    };

    public static class PathBits {
        public static String materials = String.format("%s/%s/%s", stringPathBits.assetsPath, stringPathBits.basePath, stringPathBits.materialsPath);
        public static String items = String.format("%s/%s/%s", stringPathBits.assetsPath, stringPathBits.basePath, stringPathBits.itemsPath);
        public static String blocks = String.format("%s/%s/%s", stringPathBits.assetsPath, stringPathBits.basePath, stringPathBits.blocksPath);
    };

}
