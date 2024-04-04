/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.faces;

import com.tm2batch.service.LogService;
import com.tm2batch.user.LogoffType;
import com.tm2batch.user.UserBean;
import com.tm2batch.user.UserUtils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;


/**
 *
 * @author Dad
 */
@Named
@RequestScoped
public class GeneralEntry
{
    @Inject 
    UserBean userBean;
    
     
    public void doIdleReset( ComponentSystemEvent ev )
    {
        try
        {
            FacesContext fc = FacesContext.getCurrentInstance();
            
            // If there is someone logged on, log them off (will redirect t
            if( userBean.getUserLoggedOnAsAdmin())
            {
                UserUtils userUtils = UserUtils.getInstance();                
                userUtils.processLogout(LogoffType.USER.getLogoffTypeId());
            }

            try
            {
                fc.getExternalContext().invalidateSession();
            }
            catch(IllegalStateException e )
            {
                LogService.logIt( "GeneralEntry.doLogonReset() " + e.toString() + " while invalidating session.");
            }

            fc.getExternalContext().redirect( "/td/index.xhtml" );
        }

        catch( Exception e )
        {
            LogService.logIt( e, "GeneralEntry.doLogonReset() " );
        }
    }
     


}