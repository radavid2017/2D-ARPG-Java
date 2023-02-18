package features;

import java.io.File;

public class RenameFolderFiles {
    public static void rename(String folderPath) {
        File myfolder = new File(folderPath);

        File[] file_array = myfolder.listFiles();
        if (file_array == null)
            System.out.println("path: " + folderPath);
        System.out.println("Numar cadre animatie: " + file_array.length);
        for (int i = 0; i < file_array.length; i++)
        {
            if (file_array[i].isFile())
            {
                File myfile = new File(folderPath +
                        "\\" + file_array[i].getName());

                myfile.renameTo(new File(folderPath +
                        "\\" + i + ".png"));
            }
        }
    }
}
