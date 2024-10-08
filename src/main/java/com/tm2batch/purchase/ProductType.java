package com.tm2batch.purchase;

import com.tm2batch.util.MessageFactory;
import java.util.Locale;


public enum ProductType
{    
    CREDITS(30, "pt.HostedCredits", 1000000, false, false, true,true, true),
    RESULT_CREDITS(31, "pt.UnlimitedSubscription", 0, false, false, true, true, true),
    SIM(40, "pt.Sim", 0, false, false, true,false, false),
    IFRAMETEST(41, "pt.Test", 0, false, false, false,false, false),
    FINDLY(42, "pt.Findly", 0, false, false, true, false,false),
    IVR(43, "pt.IVR", 0, false, false, false, false,false),
    VOT(44, "pt.Vot", 0, false, false, false, false,false ),
    PEOPLETEST(45, "pt.PeopleTest", 0, false, false, false, false,false ),
    CT5DIRECTTEST(46, "pt.Ct5DirectTest", 0, false, false, true, true , false),
    BATTERY(50, "pt.Battery", 0, false, false, false,false, false),
    CORPBATTERY(51, "pt.CorpBattery", 0, false, false, false,false, false),
    ONETIMEBATTERY(52, "pt.OneTimeBattery", 0, false, false, false,false, false),
    PRODUCT_CHOICE(60, "pt.ProductChoice", 0, false, false, false,false, false),
    INVOICE_PAYMENT(61, "pt.InvoicePmt", 0, false, false, false,false, false),
    LIVE_VIDEO_INTERVIEW(100, "pt.LiveVideoInterview", 0, false, true, true,false, false),
    LIVE_VIDEO_INTERVIEW_SUBSCRIPTION(120, "pt.LiveVideoInterviewSubsc", 0, true, false, true,true, false ),
    DATA_CONNECTOR(900, "pt.DataConnector", 0, false, false, false, false,false),
    RESOURCE(1001, "pt.Resource", 0, false, false, false,false,false);

    private final int productTypeId;

    private String key;

    private int maxQuantity = 0;

    private boolean onlineStart = false;

    private boolean downloadable = false;

    private boolean electronicFulfill = false;

    private boolean sendConfirm = false;

    private boolean directSale = false;

    private ProductType( int p,
                         String key,
                         int maxQuantity,
                         boolean onlineStart,
                         boolean downloadable,
                         boolean electronicFulfill,
                         boolean sendConfirm,
                         boolean directSale )
    {
        this.productTypeId = p;

        this.key = key;

        this.maxQuantity = maxQuantity;

        this.onlineStart = onlineStart;

        this.downloadable = downloadable;

        this.electronicFulfill = electronicFulfill;

        this.sendConfirm = sendConfirm;

        this.directSale = directSale;
    }

    public int getProductTypeId()
    {
        return this.productTypeId;
    }

    public boolean getIsBattery()
    {
        return equals(BATTERY) || equals(CORPBATTERY);
    }

    public boolean getIsFindlyOrSim()
    {
        return equals(FINDLY) || equals(SIM);
    }

    public boolean getIsFindly()
    {
        return equals(FINDLY);
    }

    public boolean getIsSimOrCt5Direct()
    {
        return equals(SIM) || equals(CT5DIRECTTEST);
    }

    public boolean getIsIvr()
    {
        return equals(IVR);
    }

    public boolean getIsSim()
    {
        return equals(SIM);
    }

    public boolean getIsIFrameTest()
    {
        return equals(IFRAMETEST);
    }

    public boolean getIsVot()
    {
        return equals(VOT);
    }

    public boolean getIsIvrOrVot()
    {
        return equals(IVR) || equals(VOT);
    }

    public boolean getIsAnyBattery()
    {
        return equals( BATTERY ) || equals( CORPBATTERY );
    }


    public String getName( Locale locale )
    {
        return MessageFactory.getStringMessage( locale, key, null );
    }

    public static ProductType getType( int typeId )
    {
        return getValue( typeId );
    }


    public String getKey()
    {
        return key;
    }

    public String getName()
    {
        return getName( Locale.US );
    }

    public static ProductType getValue( int id )
    {
        ProductType[] vals = ProductType.values();

        for( int i = 0; i < vals.length; i++ )
        {
            if( vals[i].getProductTypeId() == id )
                return vals[i];
        }

        return null;
    }



}
