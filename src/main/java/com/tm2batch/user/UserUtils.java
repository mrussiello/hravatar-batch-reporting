/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.user;

import com.tm2batch.entity.user.LogonHistory;
import com.tm2batch.entity.user.User;
import com.tm2batch.global.Constants;
import com.tm2batch.global.STException;
import com.tm2batch.service.LogService;
import com.tm2batch.service.Tracker;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author Mike
 */
@Named
@RequestScoped
public class UserUtils
{
    private String logonName;

    private String logonKey;

    private UserFacade userFacade;

    private UserBean userBean;


    public static UserUtils getInstance()
    {
        FacesContext fc = FacesContext.getCurrentInstance();

        return (UserUtils) fc.getApplication().getELResolver().getValue( fc.getELContext(), null, "userUtils" );
    }

    public UserBean getUserBean()
    {
        FacesContext fc = FacesContext.getCurrentInstance();

        if( userBean == null )
            userBean = (UserBean) fc.getApplication().getELResolver().getValue( fc.getELContext(), null, "userBean" );

        return userBean;
    }





    public String processLogonAttempt()
    {
        try
        {
            getUserBean();
            
            if( PasswordUtils.hasTooManyFailedLogons(logonName) )
            {
                // getHttpSession().setMaxInactiveInterval(30*60);
                throw new STException( "g.TooManyFailedLogonAttempts" );
            }

            HttpServletRequest req = FacesContext.getCurrentInstance()==null ? null : (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            
            String ipAddress = req==null ? null : req.getRemoteAddr();
            if( PasswordUtils.hasTooManyFailedLogons4Ip(ipAddress) )
                throw new STException( "g.TooManyFailedLogonAttempts" );
            
            
            
            boolean lockout = false;
            

            if( logonName == null || logonName.isEmpty() )
                return null;

            if( logonKey == null || logonKey.isEmpty() )
                return null;

            if( userFacade == null ) 
                userFacade = UserFacade.getInstance();

            // find user by info
            User u = userFacade.getUserByLogonInfo( logonName, logonKey );

            if( u!=null && u.getLockoutDate()!=null )
            {
                Calendar cal = new GregorianCalendar();
                cal.add( Calendar.MINUTE, -1*Constants.LOGON_LOCKOUT_MINUTES );
                
                if( cal.getTime().before( u.getLockoutDate() ) )
                {
                    lockout=true;
                    u=null;
                }
                
                else
                {
                    u.setLockoutDate(null);
                    
                    if( userFacade == null )
                        userFacade = UserFacade.getInstance();

                    userFacade.saveUser( u );
                }
            }
            
            
            else if( u==null )
            {
                // increment.
                // PasswordUtils.addFailedLogon( logonName );
                // userBean.setFailedLogonAttempts( userBean.getFailedLogonAttempts() + 1 );
                
                // lockout this user for 30 if too many attempts.
                if( PasswordUtils.hasTooManyFailedLogons(logonName) ) // userBean.getFailedLogonAttempts()>=Constants.MAX_FAILED_LOGON_ATTEMPTS )

                {
                    if( userFacade == null )
                        userFacade = UserFacade.getInstance();

                    User u2 = userFacade.getUserByUsername(logonName);

                    if( u2!=null )
                    {
                        lockout=true;
                        u2.setLockoutDate( new Date() );
                        userFacade.saveUser(u2);
                    }
                }                
            }
            
            if( u!=null )
            {
                ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).changeSessionId();
                
                Calendar cal = new GregorianCalendar();                
                cal.add( Calendar.MONTH, -1*Constants.MAX_PASSWORD_AGE_MONTHS );

                if( u.getResetPwd() != Constants.YES && u.getPasswordStartDate().before( cal.getTime() ) )
                {
                    u.setResetPwd( Constants.YES );                    
                    if( userFacade==null )
                        userFacade=UserFacade.getInstance();
                    userFacade.saveUser(u);
                }  
                
                if( u.getResetPwd()== Constants.YES )
                {
                    setStringInfoMessage( "Your password has expired. Please change your password before trying again." );
                    u=null;                    
                }
            }
            
            
            
            logonName = null;

            logonKey = null;

            if( u!=null && u.getUserId()>0 )
                Tracker.addLogon();
            
            if( u != null && u.getRoleType().getIsAdmin() )
            {
                userBean.setUser(  u );
                PasswordUtils.clearFailedLogons( u.getUsername() );  
                
                // HttpServletRequest req = FacesContext.getCurrentInstance()==null ? null : (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                
                String ua = null;
                String ip = null;
                
                if( req!=null )
                {
                    ua = req.getHeader( "User-Agent" );
                    ip = req.getRemoteAddr();
                }
                
                LogonHistory logonHistory = userFacade.addLogonHistory(u, LogonType.USER.getLogonTypeId(), ua, ip );
                userBean.setLogonHistoryId( logonHistory.getLogonHistoryId() );      
                
                // set the message on last login
                Date lastLogin = userFacade.getLastLogonDate( u.getUserId(), userBean.getLogonHistoryId() );

                if( lastLogin != null )
                {
                   LogService.logIt( "UserUtils.completeLogon: lastLogin=" + lastLogin.toString() );

                   Calendar cal = new GregorianCalendar();
                   cal.setTime( u.getPasswordStartDate()==null ? new Date() : u.getPasswordStartDate() );
                   cal.add( Calendar.MONTH, Constants.MAX_PASSWORD_AGE_MONTHS );
                   Date pwdExpDate = cal.getTime();
                   setStringInfoMessage( "Last login: " + lastLogin.toString() + ", password expires: " + pwdExpDate.toString() );
                }                                
            }
            
            else
            {
                PasswordUtils.addFailedLogon(logonName);
                PasswordUtils.addFailedLogon4Ip(ipAddress);
                
                if( lockout )
                    this.setStringErrorMessage( "Logons are temporarily disabled. Please wait 30 minutes and try again.");
                else
                    this.setStringErrorMessage( "Logon info invalid." );
            }


            return null;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "UserUtils.processLogonAttempt()" );

            setMessage( e );
        }

        return null;
    }


    public String processUserLogOff()
    {
        try
        {
            getUserBean();

            if( userFacade==null )
                userFacade = UserFacade.getInstance();

            if( userBean.getUserLoggedOnAsAdmin() )
                LogService.logIt( "UserUtils.processUserLogOff() Logging Off User: " + userBean.getUser().getUserId() );
            
            if( userBean.getUserLoggedOnAsAdmin()&& userBean.getLogonHistoryId() > 0 )
               userFacade.addUserLogout( userBean.getLogonHistoryId(), LogoffType.USER.getLogoffTypeId() );

            processLogout( LogoffType.USER.getLogoffTypeId() );
            
            FacesContext fc = FacesContext.getCurrentInstance();
            
            if( fc==null )
                throw new Exception( "FacesContext is null." );
            
            fc.getExternalContext().invalidateSession();

            fc.getExternalContext().redirect("/td/index.xhtml");
            
            return "/index.xhtml";
            
        }

        catch( Exception e )
        {
            LogService.logIt( e, "processUserLogOff()" );

            setMessage( e );
        }

        return null;
    }


    public void processLogout( int logoffTypeId ) throws Exception
    {
        try
        {
            getUserBean();

            if( userBean.getUserLoggedOnAsAdmin()&& userBean.getLogonHistoryId() > 0 )
               userFacade.addUserLogout( userBean.getLogonHistoryId(), logoffTypeId );

            userBean.setLogonHistoryId( 0 );

            Tracker.addLogout();

            userBean.setUser(null);

            userBean.clear();

            FacesContext fc = FacesContext.getCurrentInstance();

            if( fc==null )
                throw new Exception( "FacesContext is null." );

            fc.getExternalContext().invalidateSession();
        }
        catch( Exception e )
        {
            LogService.logIt(e, "UserUtils.processLogout() " );
            throw e;
        }
    }
    
    


    public void setStringErrorMessage( String message )
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // create a FacesMessage
        FacesMessage fm = new FacesMessage( "ERROR: " + message );

        fm.setSeverity( FacesMessage.SEVERITY_ERROR );

        // place in FacesContext
        facesContext.addMessage( null, fm );
    }

    public void setStringInfoMessage( String message )
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // create a FacesMessage
        FacesMessage fm = new FacesMessage( "INFO: " + message );

        fm.setSeverity( FacesMessage.SEVERITY_INFO );

        // place in FacesContext
        facesContext.addMessage( null, fm );
    }



    public void setMessage( Exception e )
    {
        setStringErrorMessage( e.toString() );
    }




    public String getLogonKey() {
        return logonKey;
    }

    public void setLogonKey(String logonKey) {
        this.logonKey = logonKey;
    }

    public String getLogonName() {
        return logonName;
    }

    public void setLogonName(String logonName) {
        this.logonName = logonName;
    }
}
