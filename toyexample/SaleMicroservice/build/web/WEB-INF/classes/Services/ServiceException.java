/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

/**
 *
 * @author Elder
 */
public class ServiceException extends Exception {

    public ServiceException() {
        super("An internal service error occured");
    }
}
