package lanselota.nicolas.editor;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;
import java.awt.event.*;
import java.io.*;
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


        panelMenu.add(menu);
        //************************


//**************Area de texto**********
        tPane = new JTabbedPane();

        listFile = new ArrayList<File>();
        listAreaTexto = new ArrayList<JTextPane>();
        listScroll = new ArrayList<JScrollPane>();
        listManager = new ArrayList<>();

//**************************************


        add(panelMenu);
        add(tPane);
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
                                    Utilidades.aFondo(contadorPanel,tipoFondo,listAreaTexto);

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
                            Utilidades.aFondo(contadorPanel,tipoFondo,listAreaTexto);
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
                            Utilidades.aFondo(contadorPanel,tipoFondo,listAreaTexto);
                        }
                    }
                });
            }
        }
    }

    public void creaPanel() {
        ventana = new JPanel();
        listFile.add(new File(""));
        listAreaTexto.add(new JTextPane());
        listScroll.add(new JScrollPane(listAreaTexto.get(contadorPanel)));
        listManager.add(new UndoManager());//nos sirve para rastrear los cambios del area de texto
        listAreaTexto.get(contadorPanel).getDocument().addUndoableEditListener(listManager.get(contadorPanel));

        ventana.add(listScroll.get(contadorPanel));

        tPane.addTab("title", ventana);

        Utilidades.viewNumeracionInicio(numeracion, listAreaTexto.get(contadorPanel) ,listScroll.get(contadorPanel));
        tPane.setSelectedIndex(contadorPanel);
        contadorPanel++;
        Utilidades.aFondo(contadorPanel,tipoFondo,listAreaTexto);
        existePanel=true;
    }
    private boolean numeracion = false;
    private int contadorPanel = 0; //Nos va contar cuantos paneles se han creado
    private boolean existePanel = false; //nos dice si inicialmente existe un panel creado.


    //elementos visuales

    private String tipoFondo = "d";
    private JTabbedPane tPane;
    private JPanel ventana;
    //private JTextArea areaTexto;
    private ArrayList<JTextPane> listAreaTexto;
    private ArrayList<File>listFile;
    private ArrayList<JScrollPane> listScroll;
    private ArrayList<UndoManager> listManager;
    private JMenuBar menu;
    private JMenu archivo, editar, seleccion, ver, apariencia;
    private JMenuItem elementoItem;
}

