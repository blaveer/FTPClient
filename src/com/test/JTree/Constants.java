package com.test.JTree;

public class Constants {
    /**
     * the name of the OS as given by the Java system property "os.name"
     */
    public final static String osname = System.getProperty("os.name");
    /**
     * true if the program is running on OS X
     */
    public final static boolean isOSX = osname.equalsIgnoreCase("Mac OS X");
    /**
     * true if the program is running on Linux
     */
    public final static boolean isLinux = osname.equalsIgnoreCase("Linux");
    /**
     * true if the program is running on Solaris
     */
    public final static boolean isSolaris = osname.equalsIgnoreCase("SunOS");
    /**
     * true if the program is running on Windows Vista
     */
    public final static boolean isVista = osname.equalsIgnoreCase("Windows Vista");
    /**
     * true if the program is running on Windows
     */
    public final static boolean isWindows = !(isOSX || isLinux || isSolaris);
}