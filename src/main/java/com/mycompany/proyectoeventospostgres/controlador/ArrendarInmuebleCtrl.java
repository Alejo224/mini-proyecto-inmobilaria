/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoeventospostgres.controlador;

import com.mycompany.proyectoeventospostgres.modelo.ConexionBD;
import com.mycompany.proyectoeventospostgres.modelo.InmuebleModel;
import com.mycompany.proyectoeventospostgres.vista.ArrendarInmuebleView;
import com.mycompany.proyectoeventospostgres.vista.InmuebleView;
import com.mycompany.proyectoeventospostgres.vista.MenuView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author nico
 */
public class ArrendarInmuebleCtrl implements ActionListener {
    private final ArrendarInmuebleView arrendarInmuebleView ;
    private final InmuebleModel inmuebleModel = new InmuebleModel();
    private InmuebleView inmuebleView;
    private final ConexionBD conexionBD = new ConexionBD();
    
    public ArrendarInmuebleCtrl(ArrendarInmuebleView arrendarInmuebleView){
        this.arrendarInmuebleView = arrendarInmuebleView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(arrendarInmuebleView.getJbArrendar())){
            System.out.println("Boton arrendar");

            try{
                JTextField codigo_inmueble = arrendarInmuebleView.getTxtCodigoInmueble();
                JTextField precio_cliente = arrendarInmuebleView.getTxtPrecioCliente();
                JTextField cedula_cliente = arrendarInmuebleView.getTxtCedulaCliente();

                inmuebleModel.arrendarInmueble(codigo_inmueble, cedula_cliente,
                        precio_cliente);

                arrendarInmuebleView.limpiarJtexField();
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
        if (e.getSource().equals(arrendarInmuebleView.getJbSalir())){
            if (inmuebleView == null){
                inmuebleView = new InmuebleView();
            }
            inmuebleView.setVisible(true);
            arrendarInmuebleView.dispose();
        }
        
    }
}
