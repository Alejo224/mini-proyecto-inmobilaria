package com.mycompany.proyectoeventospostgres.controlador;

import com.mycompany.proyectoeventospostgres.modelo.ConexionBD;
import com.mycompany.proyectoeventospostgres.modelo.PropietarioModel;
import com.mycompany.proyectoeventospostgres.vista.MenuView;
import com.mycompany.proyectoeventospostgres.vista.PropietarioView;

import javax.swing.*;
import java.awt.event.*;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class PropietarioCtrl implements ActionListener, MouseListener, ItemListener {
    private final PropietarioView propietarioView;
    private MenuView menuView;
    private final PropietarioModel propietarioModel = new PropietarioModel();
    private final ConexionBD conexionBD = new ConexionBD();


    public PropietarioCtrl(PropietarioView propietarioView){
        this.propietarioView = propietarioView;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(propietarioView.getJbGuardar())){
            System.out.println("Has precionado guardar");

            try{
                JTextField cedula = propietarioView.getTxtCedula();
                JTextField nombre = propietarioView.getTxtNombreCompleto();
                JTextField direccion = propietarioView.getTxtDireccion();
                JTextField telefono = propietarioView.getTxtCelular();
                JTextField email = propietarioView.getTxtEmail();
                JComboBox cedulaAgente = propietarioView.getComboBoxAgentes();

                propietarioModel.agregar(cedula, nombre, direccion, telefono,email, cedulaAgente);
                propietarioView.limpiarJtexField();
                propietarioModel.mostrar(propietarioView.getTbLista());

            }catch (SQLIntegrityConstraintViolationException repit){
                JOptionPane.showMessageDialog(null, "Datos duplicados. Posibles datos (cedula, email o celular)" +
                        ". Por favor ingrese otra \n " + repit);

            }catch (NumberFormatException n){
                JOptionPane.showMessageDialog(null, "Por favor ingrese la informacion del propietario"
                        + " \n " + n);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error. No se guardo" + ex);
            } finally {
                conexionBD.ConnectionClosed();
            }
        }

        if (e.getSource().equals(propietarioView.getJbModificar())){
            System.out.println("Boton modificar");

            try{
                JTextField cedula = propietarioView.getTxtCedula();
                JTextField nombre = propietarioView.getTxtNombreCompleto();
                JTextField direccion = propietarioView.getTxtDireccion();
                JTextField telefono = propietarioView.getTxtCelular();
                JTextField email = propietarioView.getTxtEmail();
                JComboBox cedulaAgente = propietarioView.getComboBoxAgentes();

                propietarioModel.modificar(cedula,
                        nombre, direccion, telefono, email, cedulaAgente );

                propietarioView.limpiarJtexField();
                propietarioView.getTxtCedula().setEditable(true);
                propietarioView.getTbLista().clearSelection();
                propietarioModel.mostrar(propietarioView.getTbLista());
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

        if (e.getSource().equals(propietarioView.getJbEliminar())){
            System.out.println("Boton eliminar");
            try {
                propietarioModel.eliminar(propietarioView.getTxtCedula());
                propietarioModel.mostrar(propietarioView.getTbLista());
                propietarioView.limpiarJtexField();
                propietarioView.getTxtCedula().setEditable(true);
            }catch (NumberFormatException n){
                JOptionPane.showMessageDialog(null, "Por favor seleccione un agente a eliminar"
                        + " \n " + n);
            } catch (SQLException sqle) {
                JOptionPane.showMessageDialog(null, "Error. No se guardo" + sqle);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null,"Error: "+ ex);
            }
        }
        if (e.getSource().equals(propietarioView.getJbSalir())){
            System.out.println("Boton salir");

            if (menuView == null){
                menuView = new MenuView();
            }
            menuView.setVisible(true);
            propietarioView.setVisible(false);
        }

        if (e.getSource().equals(propietarioView.getJbCancelar())){

            propietarioView.limpiarJtexField();
            propietarioView.getTxtCedula().setEditable(true);
            propietarioView.getJbCancelar().setVisible(false);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(propietarioView.getTbLista())){
            System.out.println("Has seleccionado una fila");
            JTextField cedula = propietarioView.getTxtCedula();
            JTextField nombre = propietarioView.getTxtNombreCompleto();
            JTextField direccion = propietarioView.getTxtDireccion();
            JTextField telefono = propietarioView.getTxtCelular();
            JTextField email = propietarioView.getTxtEmail();
            JComboBox cedulaAgente = propietarioView.getComboBoxAgentes();

            propietarioModel.seleccionar(propietarioView.getTbLista(),
                    cedula, nombre, direccion, telefono, email, cedulaAgente);
            propietarioView.getTxtCedula().setEditable(false);
            propietarioView.getJbCancelar().setVisible(true);
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
        if (e.getSource().equals(propietarioView.getComboBoxAgentes())) {
            String seleccionado =(String) propietarioView.getComboBoxAgentes().getSelectedItem();
            System.out.println(seleccionado);
        }
    }
}


