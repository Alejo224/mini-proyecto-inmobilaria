
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectoeventospostgres.modelo;

import javax.swing.*;
import java.sql.*;

/**
 *
 * @author alejo224
 */
public class ConexionBD {


    Connection connection = null;
    String user = "root";
    String password = "admin";
    String bd = "inmobilaria";
    String ip = "localhost";
    String puerto = "3306";

    String cadena = "jdbc:mysql://"+ip+":"+puerto+"/"+bd;

    public Connection establecerConnetion(){
        System.out.println("Conectando...");
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(cadena, user, password);

            System.out.println("se conectó correctamente a la base de datos");

        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error" + e);
        }

        return connection;
    }

    public void ConnectionClosed(){

        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("La conexión fue cerrada");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "La conexión no fue cerrada" +
                    e);
        }
    }
}
