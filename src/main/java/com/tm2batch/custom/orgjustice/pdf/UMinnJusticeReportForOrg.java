 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm2batch.custom.orgjustice.pdf;

import com.tm2batch.global.STException;
import com.tm2batch.pdf.ReportTemplate;
import com.tm2batch.service.LogService;



/**
 * CoreTest Selection Report.
 *
 * @author Mike
 */
public class UMinnJusticeReportForOrg extends BaseUMinnJusticeReportTemplate implements ReportTemplate
{

    public UMinnJusticeReportForOrg()
    {
        super();
    }


    @Override
    public byte[] generateReport() throws Exception
    {
        try
        {
            addCoverPage();

            addNewPage();
            
            addPreparationNotesSection();

            closeDoc();

            return getDocumentBytes();
        }

        catch( STException e )
        {
            throw e;
        }

        catch( Exception e )
        {
            LogService.logIt( e, "UMinnJusticeReportForOrg.generateReport() " );

            throw new STException( e );
        }
    }

    @Override
    public void initForSource()
    {
        // Use all default. Nothing to do here.
    }
    

}
