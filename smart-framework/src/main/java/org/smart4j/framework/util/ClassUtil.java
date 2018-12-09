package org.smart4j.framework.util;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author: YANGXUAN223
 * @date: 2018/11/28.
 */
public final class ClassUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> loadClass(String clazzName) {
        return loadClass(clazzName, true);
    }

    public static Class<?> loadClass(String clazzName, boolean isInitialized) {
        try {
            return Class.forName(clazzName, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class failure.exception:", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取包名下所有的文件和目录并遍历出所有的class文件
     *
     * @param packageName 报名
     * @return 类合集
     */
    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> classes = Sets.newLinkedHashSet();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                String protocol = url.getProtocol();
                switch (protocol) {
                    case "file":
                        String packagePath = url.getPath().replaceAll("%20", " ");
                        addClass(classes, packagePath, packageName);
                        break;
                    case "jar":
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if (jarURLConnection == null) {
                            break;
                        }
                        JarFile jarFile = jarURLConnection.getJarFile();
                        if (jarFile == null) {
                            break;
                        }
                        Enumeration<JarEntry> jarEntries = jarFile.entries();
                        while (jarEntries.hasMoreElements()) {
                            JarEntry entry = jarEntries.nextElement();
                            String entryName = entry.getName();
                            if (entryName.endsWith(".class")) {
                                String className = entryName.substring(0, entryName.lastIndexOf(".")).replaceAll("/", ".");
                                addToClasses(classes, className);
                            }
                        }
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            LOGGER.error("get class failure.exception:", e);
            throw new RuntimeException(e);
        }
        return classes;
    }

    /**
     * 将包路径下的 class文件全部载入 classes内
     *
     * @param classes     classes集合
     * @param packagePath 包路径
     * @param packageName 报名
     */
    private static void addClass(Set<Class<?>> classes, String packagePath, String packageName) {
        // 只处理class文件或者目录
        File[] files = new File(packagePath).listFiles(file -> {
            if (file.isFile() && file.getName().endsWith("class")) {
                return true;
            }
            return file.isDirectory();
        });
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                // 文件处理
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringUtils.isNoneBlank(packageName)) {
                    className = packageName + "." + className;
                }
                addToClasses(classes, className);
            } else {
                // 目录处理
                String subPackagePath = fileName;
                String subPackageName = fileName;
                if (StringUtils.isNoneBlank(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                if (StringUtils.isNoneBlank(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classes, subPackagePath, subPackageName);
            }
        }
    }

    private static void addToClasses(Set<Class<?>> classes, String className) {
        Class<?> clazz = loadClass(className, false);
        classes.add(clazz);
    }
}
