package lanselota.nicolas.editor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Marco marco = new Marco();
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setVisible(true);

    }
}

class Marco extends JFrame {
    public Marco() {
        setBounds(300, 300, 600, 600);
        setTitle("Editor de texto creado por Nico");


        add(new Panel(this));
    }
}

class Panel extends JPanel {
    public Panel(JFrame marco) {
        setLayout(new BorderLayout());




        //Menu************
        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new BorderLayout());
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
        creaItem("Deshacer", "editar", "deshacer");
        creaItem("Rehacer", "editar", "rehacer");
        editar.addSeparator();
        creaItem("Cortar", "editar", "cortar");
        creaItem("Copiar", "editar", "copiar");
        creaItem("Pegar", "editar", "pegar");
//*****************************************************

//********Elementos del menu Seleccionar***********************
        creaItem("Seleccionar Todo", "seleccion", "seleccionarTodo");
        //***************************************

//***********************Elementos del menu Ver**********
        creaItem("Numeración", "ver", "numeración");
        ver.add(apariencia);
        creaItem("Normal", "apariencia", "normal");
        creaItem("Dark", "apariencia", "dark");
//*********************************


        panelMenu.add(menu, BorderLayout.NORTH);
        //************************


//**************Area de texto**********
        tPane = new JTabbedPane();

        listFile = new ArrayList<File>();
        listAreaTexto = new ArrayList<JTextPane>();
        listScroll = new ArrayList<JScrollPane>();
        listManager = new ArrayList<>();

//**************************************
        /////-----------Barra de herramientas-----------

        herramientas = new JToolBar(JToolBar.VERTICAL);
        url = Main.class.getResource("/lanselota/nicolas/img/circle-xmark-solid.png");
        Utilidades.addButton(url,herramientas,"Cerrar pestaña actual").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int seleccion = tPane.getSelectedIndex();
                if (seleccion!= -1){
                    //si existen pestañas abiertas eliminamos la pestaña que tenemos seleccionadas
                    listFile.remove(seleccion);
                    listAreaTexto.remove(seleccion);
                    listScroll.get(tPane.getSelectedIndex()).setRowHeaderView(null);
                    tPane.remove(seleccion);
                    listManager.remove(seleccion);

                    contadorPanel--;
                    if (tPane.getSelectedIndex() == -1){
                        existePanel = false;//Si tPane retorna -1 no existe paneles
                    }


                }
            }
        });

        url=Main.class.getResource("/lanselota/nicolas/img/circle-plus-solid.png");
        Utilidades.addButton(url,herramientas,"Nuevo Archivo").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                creaPanel();
            }
        });

        //-------------------------------------------------
        //----------panel extra----------------------------
        panelExtra = new JPanel();
        panelExtra.setLayout(new BorderLayout());

        JPanel panelIzquierdo = new JPanel();
        labelAlfiler = new JLabel();
        url= Main.class.getResource("/lanselota/nicolas/img/thumbtack-solid.png");
        labelAlfiler.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(20,30,Image.SCALE_SMOOTH)));
        labelAlfiler.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                estadoAlfiler = !estadoAlfiler;
                marco.setAlwaysOnTop(estadoAlfiler);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                url = Main.class.getResource("/lanselota/nicolas/img/thumbtack-solid.png");
                labelAlfiler.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(20,30, Image.SCALE_SMOOTH)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (estadoAlfiler){
                    url = Main.class.getResource("/lanselota/nicolas/img/thumbtack-solid.png");
                    labelAlfiler.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(15,20, Image.SCALE_SMOOTH)));
                }else{
                    url = Main.class.getResource("/lanselota/nicolas/img/thumbtack-solid.png");
                    labelAlfiler.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(20,30, Image.SCALE_SMOOTH)));
                }
            }
        });
        panelIzquierdo.add(labelAlfiler);
        JPanel panelCentro = new JPanel();
        slider = new JSlider( 8,38,14);
        slider.setMajorTickSpacing(12);// La separación va a ser de 12 en 12
        slider.setMinorTickSpacing(2);//barras chicas de 2 en 2.

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Utilidades.tamTexto(slider.getValue(),contadorPanel,listAreaTexto);
            }
        });


        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        panelCentro.add(slider);

        panelExtra.add(panelIzquierdo, BorderLayout.WEST);
        panelExtra.add(panelCentro, BorderLayout.CENTER);
        //-------------------------------------------------

        //-----MENU EMERGENTE---------------
        menuEmergente = new JPopupMenu();

        JMenuItem cortar = new JMenuItem("Cortar");
        JMenuItem copiar = new JMenuItem("Copiar");
        JMenuItem pegar = new JMenuItem("Pegar");

        cortar.addActionListener(new DefaultEditorKit.CutAction());
        copiar.addActionListener(new DefaultEditorKit.CopyAction());
        pegar.addActionListener(new DefaultEditorKit.PasteAction());

        menuEmergente.add(cortar);
        menuEmergente.add(copiar);
        menuEmergente.add(pegar);


        //-----------------------------------


        add(panelMenu, BorderLayout.NORTH);
        add(tPane, BorderLayout.CENTER);
        add(herramientas, BorderLayout.WEST);
        add(panelExtra, BorderLayout.SOUTH);
    }

    public void creaItem(String rotulo, String menu, String accion) {
        elementoItem = new JMenuItem(rotulo);

        if (menu.equals("archivo")) {
            archivo.add(elementoItem);
            if (accion.equals("nuevo")) {
                elementoItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        creaPanel();
                    }
                });
            }
            else if (accion.equals("abrir")) {
                elementoItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        creaPanel();
                        JFileChooser selectorArchivo = new JFileChooser();
                        selectorArchivo.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                        int resultado = selectorArchivo.showOpenDialog(listAreaTexto.get(tPane.getSelectedIndex()));

                        if (resultado == JFileChooser.APPROVE_OPTION) {
                            boolean existsPath = false;
                            for (int i = 0; i < tPane.getTabCount(); i++) {
                                File f = new File("");
                                f = selectorArchivo.getSelectedFile();
                                if (listFile.get(i).getPath().equals(f.getPath())) existsPath = true;
                            }
                            if (!existsPath) {
                                File archivo = selectorArchivo.getSelectedFile();
                                listFile.set(tPane.getSelectedIndex(), archivo);
                                try {
                                    FileReader entrada = new FileReader(
                                            listFile.get(tPane.getSelectedIndex()).getPath());
                                    BufferedReader miBuffer = new BufferedReader(entrada);
                                    String linea = "";
                                    String titulo = listFile.get(tPane.getSelectedIndex()).getName();
                                    //El título se le agrega a la pestaña del panel que se crea, donde se encuentra
                                    //nuestra area de texto, lugar donde ira el texto del archivo que el usuario ha seleccionado
                                    tPane.setTitleAt(tPane.getSelectedIndex(), titulo);

                                    while (linea != null) {
                                        linea = miBuffer.readLine(); //lee todas las lineas y la almacena en el string

                                        if (linea != null)
                                            Utilidades.append(linea + "\n", listAreaTexto.get(tPane.getSelectedIndex()));
                                    }
                                    Utilidades.aFondo(contadorPanel,tipoFondo,slider.getValue(),listAreaTexto);

                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            } else {
                                //si la ruta del fichero existe vamos a recorrer todas las pestañas para ver cual
                                //es la que tiene ese path del fichero y seleccionar ese fichero y ese panel
                                for (int i = 0; i < tPane.getTabCount(); i++) {
                                    File f = selectorArchivo.getSelectedFile();
                                    if (listFile.get(i).getPath().equals(f.getPath())) {
                                        //seleccionamos el panel que ya tiene el archivo abierto
                                        tPane.setSelectedIndex(i);//le pasamos por parametro la posicion del panel que tiene el path

                                        listAreaTexto.remove(tPane.getTabCount() - 1);
                                        listScroll.remove(tPane.getTabCount() - 1);
                                        listFile.remove(tPane.getTabCount() - 1);
                                        tPane.remove(tPane.getTabCount() - 1);
                                        contadorPanel--;
                                        break;
                                    }
                                }
                            }

                        } else {
                            //si se oprime cancelar se elimina el panel que se crea por defecto
                            int seleccion = tPane.getSelectedIndex();
                            if (seleccion != -1) {
                                listAreaTexto.remove(tPane.getTabCount() - 1);
                                listScroll.remove(tPane.getTabCount() - 1);
                                listFile.remove(tPane.getTabCount() - 1);
                                tPane.remove(tPane.getTabCount() - 1);
                                contadorPanel--;
                            }
                        }


                    }
                });
            }
            else if (accion.equals("guardar")) {
                elementoItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //guardar como si el archivo no existe
                        if (listFile.get(tPane.getSelectedIndex()).getPath().equals("")){
                            JFileChooser guardarArchivo = new JFileChooser();
                            int opc = guardarArchivo.showSaveDialog(null);

                            if (opc == JFileChooser.APPROVE_OPTION){
                                File archivo = guardarArchivo.getSelectedFile();
                                listFile.set(tPane.getSelectedIndex(),archivo);
                                tPane.setTitleAt(tPane.getSelectedIndex(), archivo.getName());

                                try {
                                    FileWriter fw= new FileWriter(listFile.get(tPane.getSelectedIndex()).getPath());
                                    String texto = listAreaTexto.get(tPane.getSelectedIndex()).getText();

                                    for (int i = 0; i < texto.length(); i++){
                                        fw.write(texto.charAt(i));
                                    }
                                    fw.close();

                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }


                            }
                        }
                        else{
                            try {
                                FileWriter fw= new FileWriter(listFile.get(tPane.getSelectedIndex()).getPath());
                                String texto = listAreaTexto.get(tPane.getSelectedIndex()).getText();

                                for (int i = 0; i < texto.length(); i++){
                                    fw.write(texto.charAt(i));
                                }
                                fw.close();

                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                        }
                    }
                });

            }
            else if (accion.equals("guardarC")) {
                elementoItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //guardar como si el archivo no existe
                            JFileChooser guardarArchivo = new JFileChooser();
                            int opc = guardarArchivo.showSaveDialog(null);

                            if (opc == JFileChooser.APPROVE_OPTION) {
                                File archivo = guardarArchivo.getSelectedFile();
                                listFile.set(tPane.getSelectedIndex(), archivo);
                                tPane.setTitleAt(tPane.getSelectedIndex(), archivo.getName());

                                try {
                                    FileWriter fw = new FileWriter(listFile.get(tPane.getSelectedIndex()).getPath());
                                    String texto = listAreaTexto.get(tPane.getSelectedIndex()).getText();

                                    for (int i = 0; i < texto.length(); i++) {
                                        fw.write(texto.charAt(i));
                                    }
                                    fw.close();

                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }

                    }
                });
            }
        }
        else if (menu.equals("editar")) {
            editar.add(elementoItem);
            if(accion.equals("deshacer")){
                elementoItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(listManager.get(tPane.getSelectedIndex()).canUndo()){
                            listManager.get(tPane.getSelectedIndex()).undo();
                        }
                    }
                });
            }
            else if (accion.equals("rehacer")){
                elementoItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (listManager.get(tPane.getSelectedIndex()).canRedo()){
                        listManager.get(tPane.getSelectedIndex()).redo();
                    }
                }
            });
                
            }
            else if (accion.equals("cortar")) {
                elementoItem.addActionListener(new DefaultEditorKit.CutAction());
            }
            else if (accion.equals("copiar")) {
                elementoItem.addActionListener(new DefaultEditorKit.CopyAction());
            }
            else if (accion.equals("pegar")){
                elementoItem.addActionListener(new DefaultEditorKit.PasteAction());
            }
        }
        else if (menu.equals("seleccion")) {
            seleccion.add(elementoItem);
            if (accion.equals("seleccionarTodo")) {
                elementoItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        listAreaTexto.get(tPane.getSelectedIndex()).selectAll();
                    }
                });
            }
        }
        else if (menu.equals("ver")) {
            ver.add(elementoItem);
            if (accion.equals("numeración")){
                elementoItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        numeracion = !numeracion;
                        Utilidades.viewNumeracion(contadorPanel,numeracion,listAreaTexto,listScroll );

                    }
                });
            }
        }
        else if (menu.equals("apariencia")) {
            apariencia.add(elementoItem);

            if (accion.equals("normal")){
                elementoItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tipoFondo="w";

                        if (tPane.getTabCount() > 0){
                            Utilidades.aFondo(contadorPanel,tipoFondo,slider.getValue(),listAreaTexto);
                        }
                    }
                });
            }
            else if (accion.equals("dark")) {
                elementoItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tipoFondo="d";

                        if (tPane.getTabCount()>0){
                            Utilidades.aFondo(contadorPanel,tipoFondo,slider.getValue(),listAreaTexto);
                        }
                    }
                });
            }
        }
    }

    public void creaPanel() {
        ventana = new JPanel();
        ventana.setLayout(new BorderLayout());
        listFile.add(new File(""));
        listAreaTexto.add(new JTextPane());
        listScroll.add(new JScrollPane(listAreaTexto.get(contadorPanel)));
        listManager.add(new UndoManager());//nos sirve para rastrear los cambios del area de texto
        listAreaTexto.get(contadorPanel).getDocument().addUndoableEditListener(listManager.get(contadorPanel));

        listAreaTexto.get(contadorPanel).setComponentPopupMenu(menuEmergente);

        ventana.add(listScroll.get(contadorPanel), BorderLayout.CENTER);

        tPane.addTab("title", ventana);

        Utilidades.viewNumeracionInicio(numeracion, listAreaTexto.get(contadorPanel) ,listScroll.get(contadorPanel));
        tPane.setSelectedIndex(contadorPanel);
        contadorPanel++;
        Utilidades.aFondo(contadorPanel,tipoFondo,slider.getValue(),listAreaTexto);
        existePanel=true;
    }
    private boolean numeracion = false;
    private int contadorPanel = 0; //Nos va contar cuantos paneles se han creado
    private boolean existePanel = false; //nos dice si inicialmente existe un panel creado.

    //elementos visuales
    private String tipoFondo = "d";
    private JTabbedPane tPane;
    private JPanel ventana;
    private JPanel panelExtra;
    //private JTextArea areaTexto;
    private ArrayList<JTextPane> listAreaTexto;
    private ArrayList<File>listFile;
    private ArrayList<JScrollPane> listScroll;
    private ArrayList<UndoManager> listManager;
    private JMenuBar menu;
    private JMenu archivo, editar, seleccion, ver, apariencia;
    private JMenuItem elementoItem;
    private JToolBar herramientas;
    private URL url;


    private boolean estadoAlfiler = false;
    private JLabel labelAlfiler;
    private JSlider slider;

    private JPopupMenu menuEmergente;

}