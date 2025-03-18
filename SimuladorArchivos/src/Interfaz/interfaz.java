/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Interfaz;

import DataStructures.SimpleList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import simuladorarchivos.Directory;
import simuladorarchivos.Files;
import simuladorarchivos.StorageDevice;

/**
 *
 * @author manch
 */
public class interfaz extends javax.swing.JFrame {

    String storageString;
    int cantidadBloques = 50;
    public int cantidadBloquesDisponibles = cantidadBloques;
    public DefaultTableModel modeloTablaAsignacion;
    StorageDevice sd = new StorageDevice(cantidadBloques);
    SimpleList initStorage = sd.getBloques();
    Directory raiz = new Directory("Raiz");

    SimpleList listaDirectorios = new SimpleList();
    DefaultMutableTreeNode raizNode = new DefaultMutableTreeNode("Raíz");
    DefaultTreeModel modelo = new DefaultTreeModel(raizNode);

    private void actualizarNombreArchivo(Directory root, String nombreActual, String nuevoNombre) {
        if (root != null) {
            if (root.getName().equals(nombreActual)) {
                root.setName(nuevoNombre);
                return;
            }

            for (int i = 0; i < root.getFiles().getSize(); i++) {
                Object obj = root.getFiles().getValor(i); // Usar getValor de SimpleList
                if (obj instanceof Files) {
                    Files file = (Files) obj;
                    if (file.getNombre().equals(nombreActual)) {
                        file.setNombre(nuevoNombre);
                        return;
                    }
                } else if (obj instanceof Directory) {
                    actualizarNombreArchivo((Directory) obj, nombreActual, nuevoNombre); // Recursión
                }
            }
        }
    }

    private void actualizarJTree(DefaultMutableTreeNode root, String nombreActual, String nuevoNombre) {
        if (root.getUserObject().toString().equals(nombreActual)) {
            root.setUserObject(nuevoNombre);
            modelo.reload(root); // Notificar al modelo del cambio
            return;
        }

        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
            actualizarJTree(child, nombreActual, nuevoNombre);
        }
    }

    private Directory buscarDirectorio(String nombre) {
        for (int i = 0; i < listaDirectorios.getSize(); i++) {
            Directory dir = (Directory) listaDirectorios.getValor(i);
            if (dir.getName().equals(nombre)) {
                return dir;
            }
        }
        return null;
    }

    private DefaultMutableTreeNode buscarNodo(DefaultMutableTreeNode root, String nombre) {
        if (root.getUserObject().toString().equals(nombre)) {
            return root;
        }

        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
            DefaultMutableTreeNode result = buscarNodo(child, nombre);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public static boolean validarCampoStringNoVacio(JTextField textField, String nombreCampo) {
        String texto = textField.getText().trim(); // Obtener el texto y eliminar espacios en blanco al inicio y al final

        if (texto.isEmpty()) {
            // Mostrar un mensaje de error o realizar alguna acción (p. ej., cambiar el foco)
            javax.swing.JOptionPane.showMessageDialog(null, "El campo '" + nombreCampo + "' no puede estar vacío.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            textField.requestFocus(); // Opcional: enfocar el campo para que el usuario lo corrija
            return false; // Indica que la validación falló
        }

        return true; // Indica que la validación fue exitosa
    }

    public boolean validarCampoEnteroYBloques(JTextField textField, String nombreCampo) {
        try {
            int bloquesAsignados = Integer.parseInt(textField.getText());

            if (bloquesAsignados > 0 && bloquesAsignados <= cantidadBloquesDisponibles) {
                return true;
            } else if (bloquesAsignados <= 0) {
                JOptionPane.showMessageDialog(null, "El campo: " + nombreCampo + " debe ser un entero positivo mayor que 0", "ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            } else {
                JOptionPane.showMessageDialog(null, "El campo: " + nombreCampo + " no puede ser mayor que " + cantidadBloquesDisponibles, "ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El campo: " + nombreCampo + " debe ser un entero", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void crearArchivo(String nombre, String directorioSeleccionado) {
        if (validarCampoEnteroYBloques(CantidadBloquesTextField, "Cantidad de Bloques del Archivo")
                && validarCampoStringNoVacio(NameArchivoTextField1, "Nombre del archivo/directorio")) {

            int numeroBloques = Integer.parseInt(CantidadBloquesTextField.getText());
            Files file = new Files(nombre, numeroBloques);
            file.agregarBloques(numeroBloques);

            sd.asignarBloques(file.getTamañoBloques(), nombre);
            cantidadBloquesDisponibles = cantidadBloquesDisponibles - numeroBloques;
            storageDevicePanel.setText(sd.imprimir());
            bloquesDisponiblesText.setText(String.valueOf(cantidadBloquesDisponibles));
            añadirTablaAsignacion(modeloTablaAsignacion, file.getNombre(), file.getTamañoBloques(), 0);

            if (!directorioSeleccionado.equals("Raiz")) {
                Directory padre = buscarDirectorio(directorioSeleccionado);
                if (padre != null) {
                    padre.agregar(file);
                    agregarDirectorio(buscarNodo(raizNode, directorioSeleccionado), nombre);
                }
            } else {
                agregarDirectorio(raizNode, nombre);
                raiz.agregar(file);
                listaDirectorios.printListToConsole();
            }
        }
    }

    private void añadirTablaAsignacion(DefaultTableModel modeloTabla, String nombreArchivo, int bloquesArchivo, int bloqueInicial) {
        Object[] nuevaFila = new Object[]{
            nombreArchivo,
            bloquesArchivo,
            bloqueInicial
        };
        modeloTabla.addRow(nuevaFila);
    }

    private void actualizarTablaAsignacion(DefaultTableModel modeloTabla, String nombreArchivo, String nuevoNombreArchivo) {
        for (int i = modeloTabla.getRowCount() - 1; i >= 0; i--) {
            String nombreArchivoActual = modeloTabla.getValueAt(i, 0).toString();

            // Compara el nombre de archivo actual con el nombre de archivo que se va a actualizar.
            if (nombreArchivoActual.equals(nombreArchivo)) {
                // Se encontró la fila. Actualiza el nombre del archivo en la primera columna.
                modeloTabla.setValueAt(nuevoNombreArchivo, i, 0);
                break;
            }
        }
    }

    private void borrarTablaAsignacion(DefaultTableModel modeloTabla, String nombreArchivo) {
        for (int i = modeloTabla.getRowCount() - 1; i >= 0; i--) {
            String nombreArchivoActual = modeloTabla.getValueAt(i, 0).toString();

            // Compara el ID del proceso actual con el ID del proceso que se va a eliminar.
            if (nombreArchivoActual.equals(nombreArchivo)) {
                // Se encontró el proceso. Elimina la fila correspondiente de la tabla.
                modeloTabla.removeRow(i);
                break; // Importante: Salir del bucle después de eliminar la fila.
            }
        }
    }

    private void crearDirectorio(String nombre, String directorioSeleccionado) {
        if (validarCampoStringNoVacio(NameArchivoTextField1, "Nombre del archivo/directorio")) {
            Directory directorio = new Directory(nombre);
            DirectorySelect.addItem(directorio.getName());
            listaDirectorios.insertLast(directorio);

            if (!directorioSeleccionado.equals("Raiz")) {
                Directory padre = buscarDirectorio(directorioSeleccionado);
                if (padre != null) {
                    padre.agregar(directorio);
                    agregarDirectorio(buscarNodo(raizNode, directorioSeleccionado), nombre);
                }
            } else {
                agregarDirectorio(raizNode, nombre);
                raiz.agregar(directorio);
            }
        }
    }

    private void actualizarComboBox(String nombreActual, String nuevoNombre) {
        for (int i = 0; i < ArchivoActualizarSelect.getItemCount(); i++) {
            if (ArchivoActualizarSelect.getItemAt(i).equals(nombreActual)) {
                ArchivoActualizarSelect.removeItemAt(i);
                ArchivoActualizarSelect.insertItemAt(nuevoNombre, i);
                ArchivoABorrarSelect.setSelectedItem(nuevoNombre);
                break;
            }
        }
        for (int i = 0; i < ArchivoABorrarSelect.getItemCount(); i++) {
            if (ArchivoABorrarSelect.getItemAt(i).equals(nombreActual)) {
                ArchivoABorrarSelect.removeItemAt(i);
                ArchivoABorrarSelect.insertItemAt(nuevoNombre, i);
                break;
            }
        }
    }

    private void eliminarNodo(Directory root, String nombreAEliminar) {
        for (int i = 0; i < root.getFiles().getSize(); i++) {
            Object obj = root.getFiles().getValor(i);
            if (obj instanceof Files) {
                Files file = (Files) obj;
                if (file.getNombre().equals(nombreAEliminar)) {
                    sd.removerArchivo(nombreAEliminar);
                    root.getFiles().remove(file);
                    return;
                }
            } else if (obj instanceof Directory) {
                Directory dir = (Directory) obj;
                if (dir.getName().equals(nombreAEliminar)) {
                    eliminarDirectorioRecursivamente(dir);
                    root.getFiles().remove(dir);
                    return;
                } else {
                    eliminarNodo(dir, nombreAEliminar); // Recursión para subdirectorios
                }
            }
        }
    }

    private void eliminarDirectorioRecursivamente(Directory dir) {
        for (int i = 0; i < dir.getFiles().getSize(); i++) {
            Object obj = dir.getFiles().getValor(i);
            if (obj instanceof Files) {
                Files file = (Files) obj;
                sd.removerArchivo(file.getNombre());
            } else if (obj instanceof Directory) {
                eliminarDirectorioRecursivamente((Directory) obj); // Recursión
            }
        }
        listaDirectorios.remove(dir);
    }

    private void eliminarNodoJTree(DefaultMutableTreeNode root, String nombreAEliminar) {
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
            if (child.getUserObject().toString().equals(nombreAEliminar)) {
                modelo.removeNodeFromParent(child);
                return;
            } else {
                eliminarNodoJTree(child, nombreAEliminar); // Recursión
            }
        }
    }

    private void eliminarNodoComboBox(String nombreAEliminar) {
        for (int i = 0; i < ArchivoActualizarSelect.getItemCount(); i++) {
            if (ArchivoActualizarSelect.getItemAt(i).equals(nombreAEliminar)) {
                ArchivoActualizarSelect.removeItemAt(i);
                break;
            }
        }
        for (int i = 0; i < ArchivoABorrarSelect.getItemCount(); i++) {
            if (ArchivoABorrarSelect.getItemAt(i).equals(nombreAEliminar)) {
                ArchivoABorrarSelect.removeItemAt(i);
                break;
            }
        }
    }

    /**
     * Creates new form interfaz
     */
    public interfaz() {
        initComponents();

        modeloTablaAsignacion = (DefaultTableModel) TablaAsignacion.getModel();

        CreateFileButton.setEnabled(false);
        ActualizarButton.setEnabled(false);
        BorrarButton.setEnabled(false);
        AdminButton.setEnabled(true);
        UserButton.setEnabled(false);

        storageString = initStorage.printList();
        storageDevicePanel.setText(storageString);
        bloquesDisponiblesText.setText(String.valueOf(cantidadBloquesDisponibles));
    }

    private void agregarDirectorio(DefaultMutableTreeNode padre, String nombreDirectorio) {
        DefaultMutableTreeNode directorio = new DefaultMutableTreeNode(nombreDirectorio);
        modelo.insertNodeInto(directorio, padre, padre.getChildCount()); // Notificar al modelo
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator8 = new javax.swing.JSeparator();
        CargarButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        storageDevicePanel = new javax.swing.JTextArea();
        bloquesDisponiblesText = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jLabel3 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaAsignacion = new javax.swing.JTable();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        NameArchivoActualizarTextField = new javax.swing.JTextField();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        CantidadBloquesTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        ArchivoABorrarSelect = new javax.swing.JComboBox<>();
        CreateFileButton = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        TipoArchivoSelect1 = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        NameArchivoTextField1 = new javax.swing.JTextField();
        ActualizarButton = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JSeparator();
        ArchivoActualizarSelect = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        BorrarButton = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        DirectorySelect = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        AdminButton = new javax.swing.JButton();
        UserButton = new javax.swing.JButton();
        GuardarConfigButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        CargarButton.setBackground(new java.awt.Color(91, 90, 90));
        CargarButton.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        CargarButton.setForeground(new java.awt.Color(185, 185, 185));
        CargarButton.setText("CARGAR CONFIGURACIÓN");
        CargarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CargarButtonActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(187, 187, 187));

        storageDevicePanel.setEditable(false);
        storageDevicePanel.setColumns(20);
        storageDevicePanel.setRows(5);
        jScrollPane2.setViewportView(storageDevicePanel);

        bloquesDisponiblesText.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        bloquesDisponiblesText.setForeground(new java.awt.Color(0, 0, 0));
        bloquesDisponiblesText.setText("0");

        jTree1.setModel(modelo);
        jScrollPane3.setViewportView(jTree1);

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Storage Device");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bloquesDisponiblesText)))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bloquesDisponiblesText)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jPanel3.setBackground(new java.awt.Color(187, 187, 187));

        TablaAsignacion.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        TablaAsignacion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Bloques", "Direccion 1er Bloque"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TablaAsignacion);
        if (TablaAsignacion.getColumnModel().getColumnCount() > 0) {
            TablaAsignacion.getColumnModel().getColumn(0).setResizable(false);
            TablaAsignacion.getColumnModel().getColumn(1).setResizable(false);
            TablaAsignacion.getColumnModel().getColumn(2).setResizable(false);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jPanel5.setBackground(new java.awt.Color(187, 187, 187));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("CREAR");

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Nombre del archivo / directorio");

        NameArchivoActualizarTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NameArchivoActualizarTextFieldActionPerformed(evt);
            }
        });

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Cantidad de bloques asignados");

        CantidadBloquesTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CantidadBloquesTextFieldActionPerformed(evt);
            }
        });

        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Tipo de archivo");

        ArchivoABorrarSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ArchivoABorrarSelectActionPerformed(evt);
            }
        });

        CreateFileButton.setBackground(new java.awt.Color(0, 139, 252));
        CreateFileButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CreateFileButton.setForeground(new java.awt.Color(0, 0, 0));
        CreateFileButton.setText("CREAR ARCHIVO/DIRECTORIO");
        CreateFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateFileButtonActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("BORRAR");

        TipoArchivoSelect1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Archivo", "Directorio" }));
        TipoArchivoSelect1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TipoArchivoSelect1ActionPerformed(evt);
            }
        });

        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Nombre del archivo / directorio");

        NameArchivoTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NameArchivoTextField1ActionPerformed(evt);
            }
        });

        ActualizarButton.setBackground(new java.awt.Color(0, 139, 252));
        ActualizarButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ActualizarButton.setForeground(new java.awt.Color(0, 0, 0));
        ActualizarButton.setText("ACTUALIZAR ARCHIVO/DIRECTORIO");
        ActualizarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActualizarButtonActionPerformed(evt);
            }
        });

        ArchivoActualizarSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ArchivoActualizarSelectActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("ACTUALIZAR");

        BorrarButton.setBackground(new java.awt.Color(217, 51, 39));
        BorrarButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BorrarButton.setForeground(new java.awt.Color(0, 0, 0));
        BorrarButton.setText("BORRAR ARCHIVO/DIRECTORIO");
        BorrarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BorrarButtonActionPerformed(evt);
            }
        });

        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Directorio");

        DirectorySelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Raiz" }));
        DirectorySelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DirectorySelectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator11)
                            .addComponent(jSeparator9)
                            .addComponent(NameArchivoActualizarTextField)
                            .addComponent(CantidadBloquesTextField)
                            .addComponent(ArchivoABorrarSelect, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jSeparator5)
                            .addComponent(TipoArchivoSelect1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ArchivoActualizarSelect, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(142, 142, 142)
                                .addComponent(jLabel6))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel7))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel12))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel9)))
                        .addGap(0, 139, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(DirectorySelect, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(NameArchivoTextField1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 51, Short.MAX_VALUE)
                        .addComponent(CreateFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59))))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addComponent(jLabel11))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel10))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(jLabel13)))
                .addGap(0, 119, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(BorrarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(ActualizarButton)
                        .addGap(55, 55, 55))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NameArchivoTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CantidadBloquesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TipoArchivoSelect1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(DirectorySelect, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CreateFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ArchivoActualizarSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NameArchivoActualizarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ActualizarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ArchivoABorrarSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BorrarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("PROYECTO #2 - SISTEMAS OPERATIVOS");

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        AdminButton.setBackground(new java.awt.Color(61, 150, 209));
        AdminButton.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        AdminButton.setText("MODO ADMINISTRADOR");
        AdminButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminButtonActionPerformed(evt);
            }
        });

        UserButton.setBackground(new java.awt.Color(150, 150, 150));
        UserButton.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        UserButton.setForeground(new java.awt.Color(28, 28, 28));
        UserButton.setText("MODO USUARIO");
        UserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserButtonActionPerformed(evt);
            }
        });

        GuardarConfigButton.setBackground(new java.awt.Color(38, 171, 75));
        GuardarConfigButton.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        GuardarConfigButton.setForeground(new java.awt.Color(28, 28, 28));
        GuardarConfigButton.setText("GUARDAR CONFIGURACION");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CargarButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(AdminButton, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(UserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(GuardarConfigButton)
                        .addGap(0, 226, Short.MAX_VALUE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 42, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator3)
                    .addComponent(CargarButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AdminButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(UserButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(GuardarConfigButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(720, 720, 720))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(43, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CargarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CargarButtonActionPerformed
        // TODO add your handling code here:
        /**
         * Buscador de archivos Accede todos los documentos del ordenador
         */

    }//GEN-LAST:event_CargarButtonActionPerformed

    private void ActualizarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActualizarButtonActionPerformed
        // TODO add your handling code here:
        if (validarCampoStringNoVacio(NameArchivoActualizarTextField, "Nombre del archivo nuevo")) {
            String nuevoNombre = NameArchivoActualizarTextField.getText();
            String nombreActual = ArchivoActualizarSelect.getSelectedItem().toString();

            // Actualizar el nombre en la estructura de datos
            actualizarNombreArchivo(raiz, nombreActual, nuevoNombre);

            // Actualizar el JTree
            actualizarJTree(raizNode, nombreActual, nuevoNombre);

            // Actualizar el StorageDevice
            sd.actualizarNombreArchivoEnBloques(nombreActual, nuevoNombre);
            storageDevicePanel.setText(sd.imprimir());

            // Actualizar el JComboBox
            actualizarComboBox(nombreActual, nuevoNombre);

            // Actualizar el JTable
            actualizarTablaAsignacion(modeloTablaAsignacion, nombreActual, nuevoNombre);
        }
    }//GEN-LAST:event_ActualizarButtonActionPerformed

    private void ArchivoActualizarSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ArchivoActualizarSelectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ArchivoActualizarSelectActionPerformed

    private void TipoArchivoSelect1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TipoArchivoSelect1ActionPerformed
        // TODO add your handling code here:
        if (TipoArchivoSelect1.getSelectedItem().equals("Directorio")) {
            CantidadBloquesTextField.setEnabled(false);
        } else {
            CantidadBloquesTextField.setEnabled(true);
        }
    }//GEN-LAST:event_TipoArchivoSelect1ActionPerformed

    private void CreateFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateFileButtonActionPerformed
        // TODO add your handling code here:
        String tipoArchivo = TipoArchivoSelect1.getSelectedItem().toString();
        String nombre = NameArchivoTextField1.getText();
        String directorioSeleccionado = DirectorySelect.getSelectedItem().toString();

        ArchivoActualizarSelect.addItem(nombre);
        ArchivoABorrarSelect.addItem(nombre);

        switch (tipoArchivo) {
            case "Archivo":
                crearArchivo(nombre, directorioSeleccionado);
                break;
            case "Directorio":
                crearDirectorio(nombre, directorioSeleccionado);
                break;

        }

    }//GEN-LAST:event_CreateFileButtonActionPerformed

    private void DirectorySelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DirectorySelectActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_DirectorySelectActionPerformed

    private void AdminButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdminButtonActionPerformed
        // TODO add your handling code here:
        CreateFileButton.setEnabled(true);
        ActualizarButton.setEnabled(true);
        BorrarButton.setEnabled(true);
        AdminButton.setEnabled(false);
        UserButton.setEnabled(true);
    }//GEN-LAST:event_AdminButtonActionPerformed

    private void UserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserButtonActionPerformed
        // TODO add your handling code here:
        CreateFileButton.setEnabled(false);
        ActualizarButton.setEnabled(false);
        BorrarButton.setEnabled(false);
        AdminButton.setEnabled(true);
        UserButton.setEnabled(false);
    }//GEN-LAST:event_UserButtonActionPerformed

    private void CantidadBloquesTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CantidadBloquesTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CantidadBloquesTextFieldActionPerformed

    private void NameArchivoTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NameArchivoTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NameArchivoTextField1ActionPerformed

    private void NameArchivoActualizarTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NameArchivoActualizarTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NameArchivoActualizarTextFieldActionPerformed

    private void BorrarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BorrarButtonActionPerformed
        // TODO add your handling code here:

        String nombreAEliminar = ArchivoABorrarSelect.getSelectedItem().toString();

        System.out.println(nombreAEliminar);

        if (nombreAEliminar != null && !nombreAEliminar.isEmpty()) {
            eliminarNodo(raiz, nombreAEliminar);
            eliminarNodoJTree(raizNode, nombreAEliminar);
            eliminarNodoComboBox(nombreAEliminar);
            borrarTablaAsignacion(modeloTablaAsignacion, nombreAEliminar);
            storageDevicePanel.setText(sd.imprimir()); // Actualizar el StorageDevice
        }
    }//GEN-LAST:event_BorrarButtonActionPerformed

    private void ArchivoABorrarSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ArchivoABorrarSelectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ArchivoABorrarSelectActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new interfaz().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ActualizarButton;
    private javax.swing.JButton AdminButton;
    private javax.swing.JComboBox<String> ArchivoABorrarSelect;
    private javax.swing.JComboBox<String> ArchivoActualizarSelect;
    private javax.swing.JButton BorrarButton;
    private javax.swing.JTextField CantidadBloquesTextField;
    private javax.swing.JButton CargarButton;
    private javax.swing.JButton CreateFileButton;
    private javax.swing.JComboBox<String> DirectorySelect;
    private javax.swing.JButton GuardarConfigButton;
    private javax.swing.JTextField NameArchivoActualizarTextField;
    private javax.swing.JTextField NameArchivoTextField1;
    private javax.swing.JTable TablaAsignacion;
    private javax.swing.JComboBox<String> TipoArchivoSelect1;
    private javax.swing.JButton UserButton;
    private javax.swing.JLabel bloquesDisponiblesText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTree jTree1;
    private javax.swing.JTextArea storageDevicePanel;
    // End of variables declaration//GEN-END:variables
}
