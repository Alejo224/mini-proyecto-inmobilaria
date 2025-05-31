package com.mycompany.proyectoeventospostgres.controlador;

import com.mycompany.proyectoeventospostgres.modelo.AgenteModel;
import com.mycompany.proyectoeventospostgres.modelo.ClienteModel;
import com.mycompany.proyectoeventospostgres.modelo.ConexionBD;
import com.mycompany.proyectoeventospostgres.vista.ClienteView;
import com.mycompany.proyectoeventospostgres.vista.MenuView;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class ClienteCtrl implements ActionListener, MouseListener, ItemListener {
    private final ClienteView clienteView;
    private MenuView menuView;
    private final ClienteModel clienteModel = new ClienteModel();
    private final ConexionBD conexionBD = new ConexionBD();


    public ClienteCtrl(ClienteView clienteView){
        this.clienteView = clienteView;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(clienteView.getJbGuardar())){
            System.out.println("Has precionado guardar");

            try{
                JTextField cedula = clienteView.getTxtCedula();
                JTextField nombre = clienteView.getTxtNombreCompleto();
                JTextField direccion = clienteView.getTxtDireccion();
                JTextField telefono = clienteView.getTxtCelular();
                JTextField email = clienteView.getTxtEmail();
                JComboBox cedulaAgente = clienteView.getComboBoxAgentes();

                clienteModel.agregar(cedula, nombre, direccion, telefono,email, cedulaAgente);
                clienteView.limpiarJtexField();
                clienteModel.mostrar(clienteView.getTbListaAgentes());

            }catch (SQLIntegrityConstraintViolationException repit){
                JOptionPane.showMessageDialog(null, "Datos duplicados. Posibles datos (cedula, email o celular)" +
                        ". Por favor ingrese otra \n " + repit);

            }catch (NumberFormatException n){
                JOptionPane.showMessageDialog(null, "Por favor ingrese la informacion del cliente"
                        + " \n " + n);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error. No se guardo" + ex);
            } finally {
                conexionBD.ConnectionClosed();
            }
        }

        if (e.getSource().equals(clienteView.getJbModificar())){
            System.out.println("Boton modificar");

            try{
                JTextField cedula = clienteView.getTxtCedula();
                JTextField nombre = clienteView.getTxtNombreCompleto();
                JTextField direccion = clienteView.getTxtDireccion();
                JTextField telefono = clienteView.getTxtCelular();
                JTextField email = clienteView.getTxtEmail();
                JComboBox cedulaAgente = clienteView.getComboBoxAgentes();

                clienteModel.modificar(cedula,
                        nombre, direccion, telefono, email, cedulaAgente );

                clienteView.limpiarJtexField();
                clienteView.getTxtCedula().setEditable(true);
                clienteView.getTbListaAgentes().clearSelection();
                clienteModel.mostrar(clienteView.getTbListaAgentes());
            }catch (SQLIntegrityConstraintViolationException repit){
                JOptionPane.showMessageDialog(null, "Datos duplicados. Posibles datos (cedula, email o celular)" +
                        ". Por favor ingrese otra \n " + repit);

            }catch (NumberFormatException n){
                JOptionPane.showMessageDialog(null, "Por favor ingrese la informacion del agente"
                        + " \n " + n);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error. No se guardo" + ex);

            } finally {
                conexionBD.ConnectionClosed();
            }
        }

        if (e.getSource().equals(clienteView.getJbEliminar())){
            System.out.println("Boton eliminar");
            try {
                clienteModel.eliminar(clienteView.getTxtCedula());
                clienteModel.mostrar(clienteView.getTbListaAgentes());
                clienteView.limpiarJtexField();
                clienteView.getTxtCedula().setEditable(true);
            }catch (NumberFormatException n){
                JOptionPane.showMessageDialog(null, "Por favor seleccione un agente a eliminar"
                        + " \n " + n);
            } catch (SQLException sqle) {
                JOptionPane.showMessageDialog(null, "Error. No se guardo" + sqle);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null,"Error: "+ ex);
            }
        }
        if (e.getSource().equals(clienteView.getJbSalir())){
            System.out.println("Boton salir");

            if (menuView == null){
                menuView = new MenuView();
            }
            menuView.setVisible(true);
            clienteView.setVisible(false);
        }

        if (e.getSource().equals(clienteView.getJbCancelar())){

            clienteView.limpiarJtexField();
            clienteView.getTxtCedula().setEditable(true);
            clienteView.getJbCancelar().setVisible(false);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(clienteView.getTbListaAgentes())){
            System.out.println("Has seleccionado una fila");
            JTextField cedula = clienteView.getTxtCedula();
            JTextField nombre = clienteView.getTxtNombreCompleto();
            JTextField direccion = clienteView.getTxtDireccion();
            JTextField telefono = clienteView.getTxtCelular();
            JTextField email = clienteView.getTxtEmail();
            JComboBox cedulaAgente = clienteView.getComboBoxAgentes();

            clienteModel.seleccionar(clienteView.getTbListaAgentes(),
                    cedula, nombre, direccion, telefono, email, cedulaAgente);
            clienteView.getTxtCedula().setEditable(false);
            clienteView.getJbCancelar().setVisible(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource().equals(clienteView.getComboBoxAgentes())) {
            String seleccionado =(String) clienteView.getComboBoxAgentes().getSelectedItem();
            System.out.println(seleccionado);
        }
    }
}

