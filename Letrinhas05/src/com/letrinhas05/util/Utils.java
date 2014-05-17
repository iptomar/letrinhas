package com.letrinhas05.util;

import android.os.Environment;

import java.io.*;

/**
 * Created by Alex on 06/05/2014.
 */
public class Utils {

    /**
     * Guarda um determinado ficheiro no sistema de pastas do cartao de memoria
     * @param pasta Sub Pasta onde pertence o ficheiro (exp: Students, Schools..)
     * @param nome Nome do ficheiro (exp: IMG1.JPG)
     * @param body Ficheiro em byte[]
     */
    public static void saveFileSD(String pasta, String nome ,byte[] body ) {
        FileOutputStream fos = null;
        try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/School-Data/" + pasta + "/");

            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File myFile = new File(dir, nome);

            if (!myFile.exists()) {
                myFile.createNewFile();
            }

            fos = new FileOutputStream(myFile);
            fos.write(body);
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * Buscar um determinado ficheiro no sistema de pastas do cartao de memoria
     * @param pasta Sub Pasta onde pertence o ficheiro (exp: Students, Schools..)
     * @param nome Nome do ficheiro (exp: IMG1.JPG)
     * @return returna o ficheiro em Byte[]
     */
    public static byte[] getFileSD(String pasta, String nome) {
        try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/School-Data/" + pasta + "/");
            final File myFile = new File(dir, nome);
            InputStream is = new BufferedInputStream(new FileInputStream(myFile));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while (is.available() > 0) {
                bos.write(is.read());
            }
            is.close();
            return bos.toByteArray();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }



    /**
     * Buscar um determinado ficheiro no sistema de pastas do cartao de memoria
     * @param caminho Sub Pasta onde pertence o ficheiro (exp: Students, Schools..)
     * @param nome Nome do ficheiro (exp: IMG1.JPG)
     * @return returna o ficheiro em Byte[]
     */
    public static byte[] getFileSDTest(String caminho, String nome) {
        try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + caminho);
            final File myFile = new File(dir, nome);
            InputStream is = new BufferedInputStream(new FileInputStream(myFile));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while (is.available() > 0) {
                bos.write(is.read());
            }
            is.close();
            return bos.toByteArray();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Apaga todos os ficheiros dentro de uma pasta passada por parametro
     * @param pasta pasta ao qual se pretende apagar toda a informacao (exmp: Students)
     */
    public static void deleteAllFileFolder(String pasta) {
        try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/School-Data/" + pasta + "/");
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }




}
