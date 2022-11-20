package lanselota.nicolas.editor;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class Utilidades {
    public static void append(String linea, JTextArea areaTexto){
        try{
            Document doc = areaTexto.getDocument();
            doc.insertString(doc.getLength(), linea,null);
        }catch (BadLocationException exc){
            exc.printStackTrace();
        }
    }
}
