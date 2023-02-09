package com.tm2batch.faces;

import com.tm2batch.util.MessageFactory;
import com.tm2batch.global.STException;
import java.io.Serializable;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.FacesMessage.Severity;
import jakarta.faces.context.FacesContext;

// @ManagedBean
public class FacesUtils implements Serializable
{

    public void setErrorMessage( String key, Object[] params )
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // create a FacesMessage
        FacesMessage fm = MessageFactory.getMessage( key, params );

        fm.setSeverity( FacesMessage.SEVERITY_ERROR );

        // place in FacesContext
        facesContext.addMessage( null, fm );
    }

    public void setInfoMessage( String key, Object[] params )
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // create a FacesMessage
        FacesMessage fm = MessageFactory.getMessage( key, params );

        fm.setSeverity( FacesMessage.SEVERITY_INFO );

        // place in FacesContext
        facesContext.addMessage( null, fm );
    }

    public void setStringInfoMessage( String message )
    {
        String[] params;

        params = new String[1];

        params[0] = message;

        setInfoMessage( "g.PassThru", params );
    }

    public void setStringErrorMessage( String message )
    {
        String[] params;

        params = new String[1];

        params[0] = message;

        setErrorMessage( "g.PassThru", params );
    }

    public void setMessage( Exception e )
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // create a FacesMessage
        FacesMessage fm;

        if( e instanceof STException )
        {
            fm = MessageFactory.getMessage( ( (STException) e ).getKey(), ( (STException) e ).getParams() );
        }

        else
        {
            Object[] params = new Object[1];

            params[0] = e.toString();

            // create a FacesMessage
            fm = MessageFactory.getMessage( "g.SystemError", params );
        }

        fm.setSeverity( FacesMessage.SEVERITY_ERROR );

        // place in FacesContext
        facesContext.addMessage( null, fm );
    }

    public void setMessage( String key, Object[] params, Severity severity )
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // create a FacesMessage
        FacesMessage fm = MessageFactory.getMessage( key, params );

        fm.setSeverity( severity );

        // place in FacesContext
        facesContext.addMessage( null, fm );
    }


}
