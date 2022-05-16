package ps.demo.util;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyFileUtil {

    public static final String DIR_SEPERATOR = "/";
    private static Integer BUFFER_SIZE = 1024 * 1024 * 10;

    private static int FILE_NAME_LENGTH = 128;

    public static MessageDigest MD5 = null;

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ne) {
            ne.printStackTrace();
        }
    }

    public static String toValidFileName(String input) {
        return StringUtils.abbreviate(input.replaceAll("[:\\\\/*\"?|<>']",
                "-"), FILE_NAME_LENGTH);
    }

    public static String getUserHomeDir() {
        return Paths.get(System.getProperty("user.home")).toAbsolutePath()
                .toString();
    }

    public static String getCurrentDir() {
        return Paths.get(System.getProperty("user.dir")).toAbsolutePath()
                .toString();
    }

    public static File getFileInHomeDir(String fileName) {
        return new File(getUserHomeDir() + DIR_SEPERATOR + toValidFileName(fileName));
    }

    public static File getFileTsInHomeDir(String key) {
        return new File(getUserHomeDir() + DIR_SEPERATOR + MyTimeUtil.getNowStr() + "-" + toValidFileName(key));
    }

    public static File getFileDateDirInHomeDir(String key) {
        File dir = new File(getUserHomeDir() + DIR_SEPERATOR
                + MyTimeUtil.getNowStr("yyyy-MM-dd") + DIR_SEPERATOR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir.getPath() + DIR_SEPERATOR + MyTimeUtil.getNowStr("HHmmss") + "-" + toValidFileName(key));
    }

    public static void setFullPermission(File file) {
        if (file != null) {
            file.setReadable(true, false);
            file.setExecutable(true, false);
            file.setWritable(true, false);
        }
    }

    public static void createDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f);
            }
        } else {
            file.delete();
        }
    }


    /**
     * 创建文件支持多级目录
     *
     * @param file 需要创建的文件
     * @return 是否成功, 如果存在则返回成功
     */
    public final static boolean createFiles(File file) {
        if (file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            if (!file.exists()) {
                return file.mkdirs();
            }
        } else {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    try {
                        return file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    return file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 创建文件支持多级目录
     *
     * @param file 需要创建的文件
     * @return 是否成功, 如果存在则返回成功
     * @para isReNew 存在的时候是否重新创建
     */
    public final static boolean createFiles(File file, boolean isReNew) {
        if (file.exists()) {
            if (isReNew) {
                if (file.delete()) {
                    try {
                        return file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }
        if (file.isDirectory()) {
            if (!file.exists()) {
                return file.mkdirs();
            }
        } else {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    try {
                        return file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    return file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 创建文件支持多级目录
     *
     * @param path 需要创建的文件
     * @return 是否成功
     */
    public static boolean createFile(String path) {
        if (path != null && path.length() > 0) {
            try {
                File file = new File(path);
                if (!file.getParentFile().exists() && file.getParentFile().mkdirs()) {
                    return file.createNewFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    /**
     * 获取文件后缀名
     *
     * @param file file
     * @return file's suffix
     */

    public static String suffix(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.indexOf(".") + 1);
    }

    /**
     * 获取文件的hash
     *
     * @param file     file
     * @param HashTyle MD5,SHA-1,SHA-256
     * @return
     */
    public static String fileHash(File file, String HashTyle) {
        try (InputStream fis = new FileInputStream(file)) {
            MessageDigest md = MessageDigest.getInstance(HashTyle);
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            byte[] md5Bytes = md.digest();
            StringBuilder hexValue = new StringBuilder();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public final static boolean cleanFile(File file) {
        try (
                FileWriter fw = new FileWriter(file)
        ) {
            fw.write("");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public final static String mimeType(String file) throws IOException {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(file);
    }

    public final static Date modifyTime(File file) {
        return new Date(file.lastModified());
    }

    /**
     * 复制文件
     *
     * @param resourcePath 源文件
     * @param targetPath   目标文件
     * @return 是否成功
     */
    public final static boolean copy(String resourcePath, String targetPath) {
        File file = new File(resourcePath);
        return copy(file, targetPath);
    }

    /**
     * 复制文件
     * 通过该方式复制文件文件越大速度越是明显
     *
     * @param file       需要处理的文件
     * @param targetFile 目标文件
     * @return 是否成功
     */
    public final static boolean copy(File file, String targetFile) {
        try (
                FileInputStream fin = new FileInputStream(file);
                FileOutputStream fout = new FileOutputStream(new File(targetFile))
        ) {
            FileChannel in = fin.getChannel();
            FileChannel out = fout.getChannel();
            //设定缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while (in.read(buffer) != -1) {
                //准备写入，防止其他读取，锁住文件
                buffer.flip();
                out.write(buffer);
                //准备读取。将缓冲区清理完毕，移动文件内部指针
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String fileMD5(File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new BigInteger(1, MD5.digest()).toString(16);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> searchInFile(File file, String regex) {
        List<String> findings = new ArrayList<>();
        Pattern pt = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        String content = null;
        try {
            content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Matcher mt = pt.matcher(content);
        while (mt.find()) {
            findings.add(content.substring(mt.start(), mt.end()));
        }
        return findings;
    }


    public static void copy(File from, File to, int bufsize) {

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(from);
            fos = new FileOutputStream(to);
            byte[] buf = new byte[bufsize];
            int index;
            Random rd = new Random();
            while (-1 != (index = fis.read(buf, 0, buf.length))) {
                fos.write(buf, 0, index);
                try {
                    Thread.sleep(100 + rd.nextInt(900));
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeSilently(fis);
            closeSilently(fos);
        }
    }

    public static void closeSilently(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Exception e) {

            }
        }
    }

}
