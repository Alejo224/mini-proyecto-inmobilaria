/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoeventospostgres.controlador;

import com.mycompany.proyectoeventospostgres.modelo.DAO.ArriendoDAO;
import com.mycompany.proyectoeventospostgres.vista.ArriendoView;
import com.mycompany.proyectoeventospostgres.vista.InmuebleView;
import com.mycompany.proyectoeventospostgres.vista.MenuView;
import com.mycompany.proyectoeventospostgres.modelo.InmuebleModel;

import java.awt.event.*;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.swing.*;

/**
 *
 * @author nico
 */
public class InmuebleCtrl implements ActionListener, MouseListener, ItemListener{

    private final InmuebleView inmuebleView;
    private MenuView menuView;
    private InmuebleModel inmuebleModel = new InmuebleModel();
    private ArriendoView arriendoView;
    public InmuebleCtrl(InmuebleView inmuebleView) {
        this.inmuebleView = inmuebleView;
    }
    private ArriendoDAO arriendoDAO;
        
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(inmuebleView.getJbGuardar())){
            System.out.println("Has precionado guardar");

            try{
                JTextField codigo_inmueble = inmuebleView.getTxtCodigoInmueble();
                JTextField descripcion = inmuebleView.getTxtDescripcion();
                JTextField precio_propietario = inmuebleView.getTxtPrecioPropietario();
                JComboBox fk_cedula_propietario = inmuebleView.getComboBoxPropietarios();

                inmuebleModel.agregar(codigo_inmueble, descripcion, precio_propietario, fk_cedula_propietario);
                inmuebleView.limpiarJtexField();
                System.out.println("Has precionado guardar");
                inmuebleModel.mostrar(inmuebleView.getTbListaInmuebles());

            }catch (SQLIntegrityConstraintViolationException repit){
                JOptionPane.showMessageDialog(null, "Datos duplicados. Posibles datos (cedula, email o celular)" +
                        ". Por favor ingrese otra \n " + repit);

            }catch (NumberFormatException n){
                JOptionPane.showMessageDialog(null, "Por favor ingrese la informacion del cliente"
                        + " \n " + n);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error. No se guardo" + ex);
            } finally {
//                conexionBD.ConnectionClosed();
            }
        }
        
        if (e.getSource().equals(inmuebleView.getJbModificar())){
            System.out.println("Boton modificar");

            try{
                JTextField codigo_inmueble = inmuebleView.getTxtCodigoInmueble();
                JTextField descripcion = inmuebleView.getTxtDescripcion();
                JTextField precio_propietario = inmuebleView.getTxtPrecioPropietario();
                JComboBox fk_cedula_propietario = inmuebleView.getComboBoxPropietarios();

                inmuebleModel.modificar(codigo_inmueble,
                        descripcion, precio_propietario, fk_cedula_propietario);

                inmuebleView.limpiarJtexField();
                inmuebleView.getComboBoxPropietarios().setEditable(true);
                inmuebleView.getTbListaInmuebles().clearSelection();
                inmuebleModel.mostrar(inmuebleView.getTbListaInmuebles());
            }catch (SQLIntegrityConstraintViolationException repit){
                JOptionPane.showMessageDialog(null, "Datos duplicados. Posibles datos (cedula, email o celular)" +
                        ". Por favor ingrese otra \n " + repit);

            }catch (NumberFormatException n){
                JOptionPane.showMessageDialog(null, "Por favor ingrese la informacion del agente"
                        + " \n " + n);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error. No se guardo" + ex);

            } finally {
//                conexionBD.ConnectionClosed();
            }
        }
        
        if (e.getSource().equals(inmuebleView.getJbEliminar())){
            System.out.println("Boton eliminar");
            try {
                inmuebleModel.eliminar(inmuebleView.getTxtCodigoInmueble());
                inmuebleModel.mostrar(inmuebleView.getTbListaInmuebles());
                inmuebleView.limpiarJtexField();
                inmuebleView.getTxtCodigoInmueble().setEditable(true);
            }catch (NumberFormatException n){
                JOptionPane.showMessageDialog(null, "Por favor seleccione un inmueble a eliminar"
                        + " \n " + n);
            } catch (SQLException sqle) {
                JOptionPane.showMessageDialog(null, "Error. No se guardo" + sqle);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null,"Error: "+ ex);
            }
        }
        
        if (e.getSource().equals(inmuebleView.getJbSalir())){
            if (menuView == null){
                menuView = new MenuView();
            }
            menuView.setVisible(true);
            inmuebleView.dispose();
        }
        
        if (e.getSource().equals(inmuebleView.getJbCancelar())){

            inmuebleView.limpiarJtexField();
            inmuebleView.getTxtCodigoInmueble().setEditable(true);
            inmuebleView.getJbCancelar().setVisible(false);
        }

        if (e.getSource().equals(inmuebleView.getJbArrendarInmueble())){
            System.out.println("Ingresando Arrendar inmueble");
            String codigoInmueble = inmuebleView.getTxtCodigoInmueble().getText();

            // verificar si ha seleccionado un inmueble para arendar
            if (codigoInmueble.isBlank()){
                JOptionPane.showMessageDialog(inmuebleView, "Por favor seleccione el inmueble que desea arrendar.",
                        "Seleccione un inmueble", JOptionPane.WARNING_MESSAGE);
                return;
            }
            inmuebleView.setVisible(false);

            if (arriendoView == null){
                arriendoView = new ArriendoView();
            }
            arriendoView.setVisible(true);
            if (arriendoDAO == null ) arriendoDAO = new ArriendoDAO();
            //cargar la informacion de la tabla
            arriendoDAO.mostrarArriendos(Integer.parseInt(codigoInmueble), arriendoView.getjTable1());
            arriendoView.getComboBoxInmueble().setSelectedItem(codigoInmueble);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(inmuebleView.getTbListaInmuebles())){
            System.out.println("Has seleccionado una fila");
                JTextField codigo_inmueble = inmuebleView.getTxtCodigoInmueble();
                JTextField descripcion = inmuebleView.getTxtDescripcion();
                JTextField precio_propietario = inmuebleView.getTxtPrecioPropietario();
                JComboBox fk_cedula_propietario = inmuebleView.getComboBoxPropietarios();

            inmuebleModel.seleccionar(inmuebleView.getTbListaInmuebles(),
                    codigo_inmueble, descripcion, precio_propietario, fk_cedula_propietario);
            inmuebleView.getTxtCodigoInmueble().setEnabled(false);
            inmuebleView.getJbCancelar().setVisible(true);

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
        if (e.getSource().equals(inmuebleView.getComboBoxPropietarios())) {
            String seleccionado =(String) inmuebleView.getComboBoxPropietarios().getSelectedItem();
            System.out.println(seleccionado);
        }
    }
}
