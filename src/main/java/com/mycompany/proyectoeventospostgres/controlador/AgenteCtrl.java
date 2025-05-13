package com.mycompany.proyectoeventospostgres.controlador;

import com.mycompany.proyectoeventospostgres.modelo.ConexionBD;
import com.mycompany.proyectoeventospostgres.vista.AgenteView;
import com.mycompany.proyectoeventospostgres.vista.MenuView;
import com.mycompany.proyectoeventospostgres.modelo.AgenteModel;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;


public class AgenteCtrl implements ActionListener, MouseListener {

    private final AgenteView agenteView;
    private MenuView menuView;
    private final AgenteModel agenteModel = new AgenteModel();
    private final ConexionBD conexionBD = new ConexionBD();


    public AgenteCtrl(AgenteView agenteView){
        this.agenteView = agenteView;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(agenteView.getJbGuardar())){
            System.out.println("Has precionado guardar");

            try{
                JTextField cedula = agenteView.getTxtCedula();
                JTextField nombre = agenteView.getTxtNombreCompleto();
                JTextField direccion = agenteView.getTxtDireccion();
                JTextField telefono = agenteView.getTxtCelular();
                JTextField email = agenteView.getTxtEmail();

                agenteModel.agregar(cedula, nombre, direccion, telefono,email);
                agenteView.limpiarJtexField();
                agenteModel.mostrarAgentesComerciales(agenteView.getTbListaAgentes());

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

        if (e.getSource().equals(agenteView.getJbModificar())){
            System.out.println("Boton modificar");

            try{
                JTextField cedula = agenteView.getTxtCedula();
                JTextField nombre = agenteView.getTxtNombreCompleto();
                JTextField direccion = agenteView.getTxtDireccion();
                JTextField telefono = agenteView.getTxtCelular();
                JTextField email = agenteView.getTxtEmail();

                agenteModel.modificarAgenteComercial(cedula,
                        nombre, direccion, telefono, email );

                agenteView.limpiarJtexField();
                agenteView.getTxtCedula().setEditable(true);
                agenteView.getTbListaAgentes().clearSelection();
                agenteModel.mostrarAgentesComerciales(agenteView.getTbListaAgentes());
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

        if (e.getSource().equals(agenteView.getJbEliminar())){
            System.out.println("Boton eliminar");
            try {
                agenteModel.eliminarAgenteComercial(agenteView.getTxtCedula());
                agenteModel.mostrarAgentesComerciales(agenteView.getTbListaAgentes());
                agenteView.limpiarJtexField();
                agenteView.getTxtCedula().setEditable(true);
            }catch (NumberFormatException n){
                JOptionPane.showMessageDialog(null, "Por favor seleccione un agente a eliminar"
                        + " \n " + n);
            } catch (SQLException sqle) {
                JOptionPane.showMessageDialog(null, "Error. No se guardo" + sqle);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null,"Error: "+ ex);
            }
        }
        if (e.getSource().equals(agenteView.getJbSalir())){
            System.out.println("Boton salir");

            if (menuView == null){
                menuView = new MenuView();
            }
            menuView.setVisible(true);
            agenteView.setVisible(false);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(agenteView.getTbListaAgentes())){
            System.out.println("Has seleccionado una fila");
            JTextField cedula = agenteView.getTxtCedula();
            JTextField nombre = agenteView.getTxtNombreCompleto();
            JTextField direccion = agenteView.getTxtDireccion();
            JTextField telefono = agenteView.getTxtCelular();
            JTextField email = agenteView.getTxtEmail();

            agenteModel.seleccionar(agenteView.getTbListaAgentes(),
                    cedula, nombre, direccion, telefono, email);
            agenteView.getTxtCedula().setEditable(false);
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
}
