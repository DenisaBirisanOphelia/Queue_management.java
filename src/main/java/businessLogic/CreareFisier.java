package businessLogic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class CreareFisier {

    private FileWriter Scriitor;

    public void creeazaFisier(String numeFisier) {
        try {
            File fisier = new File(numeFisier);//creeaza un obiect de tip fisier
            if (!fisier.exists()) //daca nu exista, ilcreeaza doar
                fisier.createNewFile();
            Scriitor = new FileWriter(fisier); //creeaza un obiect de tipul "Scriitor" care imi va putea opera cu fisierul
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void scrie(String linieNoua)
    {
        try {
            Scriitor.write(linieNoua + "\n");
            Scriitor.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
