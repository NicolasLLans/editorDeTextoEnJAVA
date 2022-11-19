package lanselota.nicolas.editor;

import javax.swing.*;
import java.awt.event.*;

public class Main {
    public static void main(String[] args) {
        Marco marco = new Marco();
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setVisible(true);

    }
}

class Marco extends JFrame {
    public Marco() {
        setBounds(300, 300, 600, 300);
        setTitle("Editor de texto de Nico");


        add(new Panel());
    }
}

class Panel extends JPanel {
    public Panel() {
        //Menu************
        JPanel panelMenu = new JPanel();

        menu = new JMenuBar();
        archivo = new JMenu("Archivo");
        editar = new JMenu("Editar");
        seleccion = new JMenu("Selección");
        ver = new JMenu("Ver");
        apariencia = new JMenu("Apariencia");

        menu.add(archivo);
        menu.add(editar);
        menu.add(seleccion);
        menu.add(ver);

//***********Elementos del menu archivo**********

        creaItem("Nuevo Archivo", "archivo", "nuevo");
        creaItem("Abrir Archivo", "archivo", "abrir");
        archivo.addSeparator();
        creaItem("Guardar", "archivo", "guardar");
        creaItem("Guardar Como", "archivo", "guardarC");
//****************************************************

//************Elementos del menu editar**************
        creaItem("Deshacer", "editar", "");
        creaItem("Rehacer", "editar", "");
        editar.addSeparator();
        creaItem("Cortar", "editar", "");
        creaItem("Copiar", "editar", "");
        creaItem("Pegar", "editar", "");
//*****************************************************

//********Elementos del menu Seleccionar***********************
        creaItem("Seleccionar Todo", "seleccion", "");
        //***************************************

//***********************Elementos del menu Ver**********
        creaItem("Numeración", "ver", "");
        ver.add(apariencia);
        creaItem("Normal", "apariencia", "");
        creaItem("Dark", "apariencia", "");
//*********************************


        panelMenu.add(menu);
        //************************


//**************Area de texto**********
        tPane = new JTabbedPane();
//**************************************


        add(panelMenu);
        add(tPane);
    }

    public void creaItem(String rotulo, String menu, String accion) {
        elementoItem = new JMenuItem(rotulo);

        if (menu.equals("archivo")) {
            archivo.add(elementoItem);
            if(accion.equals("nuevo")){
                elementoItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        creaPanel();
                    }
                });
            } else if (accion.equals("abrir")) {
                elementoItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
            }
        } else if (menu.equals("editar")) {
            editar.add(elementoItem);
        } else if (menu.equals("seleccion")) {
            seleccion.add(elementoItem);
        } else if (menu.equals("ver")) {
            ver.add(elementoItem);
        } else if (menu.equals("apariencia")) {
            apariencia.add(elementoItem);
        }
    }

    public void creaPanel() {
        ventana = new JPanel();
        areaTexto = new JTextArea();

        ventana.add(areaTexto);
        tPane.addTab("title", ventana);
    }

    private JTabbedPane tPane;
    private JPanel ventana;
    private JTextArea areaTexto;
    private JMenuBar menu;
    private JMenu archivo, editar, seleccion, ver, apariencia;
    private JMenuItem elementoItem;
}

