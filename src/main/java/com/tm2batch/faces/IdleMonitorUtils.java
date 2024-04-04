package com.tm2batch.faces;


import com.tm2batch.global.Constants;
import com.tm2batch.user.UserBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@RequestScoped
public class IdleMonitorUtils
{

    @Inject 
    UserBean userBean; 
      
    public int getSessionTimeoutMilliseconds()
    {
        return Constants.IDLE_SESSION_TIMEOUT_MINS*60*1000;
    }
    
        /*
    public void onIdle() 
    {
        try
        {
            LogService.logIt( "IdleMonitorUtils.onIdle() TM2Batch loggedOnAccount=" + (userBean==null ? "NULL" : userBean.getUserLoggedOnAsAdmin()) );
            // if logged on, logout.
            if( userBean!=null && userBean.getUserLoggedOnAsAdmin() )
            {
                UserUtils userUtils = UserUtils.getInstance();                
                userUtils.processUserLogOff();
            }

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().invalidateSession();
            fc.getExternalContext().redirect( "/td/index.xhtml" ); 
        }
        catch( Exception e )
        {
            LogService.logIt( "IdleMonitorUtils.onIdle() " + userBean.getUserLoggedOnAsAdmin() );
        }
    }
    */
 
    

}
