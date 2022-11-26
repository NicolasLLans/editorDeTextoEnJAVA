package lanselota.nicolas.editor;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.util.ArrayList;

public class Utilidades {
    //----------- Agrega texto al final-------------
    public static void append(String linea, JTextArea areaTexto){
        try{
            Document doc = areaTexto.getDocument();
            doc.insertString(doc.getLength(), linea,null);
        }catch (BadLocationException exc){
            exc.printStackTrace();
        }
    }
    //----------------------------------------------------

    //------------Método de mostrar numeración--------------
    public static void viewNumeracionInicio(boolean numeracion, JTextArea textArea, JScrollPane scroll){
        if (numeracion){
            scroll.setRowHeaderView(new TextLineNumber(textArea));
        }else {
            scroll.setRowHeaderView(null);
        }
    }

    public static void viewNumeracion(int contador, boolean numeracion, ArrayList<JTextArea> textArea, ArrayList<JScrollPane> scroll){
        if (numeracion){
           for (int i = 0; i < contador; i++){
               scroll.get(i).setRowHeaderView(new TextLineNumber(textArea.get(i)));
           }
        }else {
            for (int i =0; i<contador; i++){
                scroll.get(i).setRowHeaderView(null);
            }
        }
    }
}
