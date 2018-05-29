/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Insumo;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author chrs
 */
public class InsumosHelper {
    
    Session session = null;
    private String errorMessage = "";
    public InsumosHelper() {
        session = LoginController.serviceInit();
    }
    
    public void close(){
        session.close();
    }
    
    public String getErrorMessage(){
        return errorMessage;
    }
    
    public Long saveInsumo(Insumo newInsumo) {
        Long id = null;
        try {
            Transaction tx = session.beginTransaction();
            session.save(newInsumo);
            if(newInsumo.getId() != null) {
                id = newInsumo.getId();
            }
            tx.commit();            
            
        } catch (Exception e) {
            session.getTransaction().rollback();
            this.errorMessage = e.getMessage();
        }
        return id;
    }
    public ArrayList<Insumo> getInsumos() {
        ArrayList<Insumo> insumos = null;
        Query query = null;
        try {
            query = session.createQuery("from Insumo");
            if(!query.list().isEmpty()){
                insumos = (ArrayList<Insumo>)(query.list());
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        return insumos;
    }
}
