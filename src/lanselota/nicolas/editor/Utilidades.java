package lanselota.nicolas.editor;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;

public class Utilidades {
    //----------- Agrega texto al final-------------
    public static void append(String linea, JTextPane areaTexto){
        try{
            Document doc = areaTexto.getDocument();
            doc.insertString(doc.getLength(), linea,null);
        }catch (BadLocationException exc){
            exc.printStackTrace();
        }
    }
    //----------------------------------------------------

    //------------Método de mostrar numeración--------------
    public static void viewNumeracionInicio(boolean numeracion, JTextPane textArea, JScrollPane scroll){
        if (numeracion){
            scroll.setRowHeaderView(new TextLineNumber(textArea));
        }else {
            scroll.setRowHeaderView(null);
        }
    }

    public static void viewNumeracion(int contador, boolean numeracion, ArrayList<JTextPane> textArea, ArrayList<JScrollPane> scroll){
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

    //--------------sección apariencia--------------
    public static void aFondo(int contador, String tipo, ArrayList<JTextPane> list){
        if (tipo.equals("w")){
            for (int i = 0; i < contador;i++){
                StyleContext sc = StyleContext.getDefaultStyleContext();

                //para el color del texto-----------------------------
                AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,StyleConstants.Foreground, new Color (255,255,255));

                //Para el tipo de texto -------------------------------
                aset= sc.addAttribute(aset,StyleConstants.FontFamily,"Arial");

                list.get(i).setCharacterAttributes(aset, false);
                list.get(i).setBackground(Color.WHITE);
            }
        } else if (tipo.equals("d")) {
            for (int i = 0; i < contador;i++){
                StyleContext sc = StyleContext.getDefaultStyleContext();

                //para el color del texto-----------------------------
                AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,StyleConstants.Foreground, Color.lightGray);

                //Para el tipo de texto -------------------------------
                aset= sc.addAttribute(aset,StyleConstants.FontFamily,"Arial");

                list.get(i).setCharacterAttributes(aset, false);
                list.get(i).setBackground(Color.DARK_GRAY);
            }
        }
    }
    //----------------------------
}
