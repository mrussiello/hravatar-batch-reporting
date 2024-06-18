/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.user;

import com.tm2batch.entity.user.User;
import com.tm2batch.global.RuntimeConstants;
import java.io.Serializable;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

/**
 *
 * @author Mike
 */
@Named
@SessionScoped
public class UserBean implements Serializable
{
    private User user;
    private long logonHistoryId = 0;

    //private int failedLogonAttempts=0;


    /** Creates a new instance of UserBean */
    public UserBean()
    {
    }

    public static UserBean getInstance()
    {
        FacesContext fc = FacesContext.getCurrentInstance();

        return (UserBean) fc.getApplication().getELResolver().getValue( fc.getELContext(), null, "userBean" );
    }

    public void clear()
    {
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBaseLogoUrl()
    {
        return RuntimeConstants.getStringValue("baselogourl");
    }

    public String getBaseIconUrl()
    {
        return RuntimeConstants.getStringValue("baseiconurl");
    }
    
    
    public boolean getUserLoggedOnAsAdmin()
    {
        return user != null && user.getUserId()>0 && ( user.getRoleId() >= RoleType.ADMIN.getRoleTypeId() );
    }

    public long getUserId()
    {
        if( user == null )
            return 0;

        return user.getUserId();
    }

    public long getLogonHistoryId() {
        return logonHistoryId;
    }

    public void setLogonHistoryId(long logonHistoryId) {
        this.logonHistoryId = logonHistoryId;
    }


}
