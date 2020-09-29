package com.epam.rd.java.basic.practice8;

        import com.epam.rd.java.basic.practice8.db.DBManager;
        import org.junit.Assert;
        import org.junit.Test;

        import java.util.logging.Level;
        import java.util.logging.Logger;

public class Part1StudentTest {
    public static final Logger logger = Logger.getLogger( Part1StudentTest .class.getName());
    @Test
    public void first() {
        DBManager dbManager = null;
        try {
            dbManager = DBManager.getInstance();
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Exception message",e);
        }
        Assert.assertEquals( true, dbManager!=null);
    }

    @Test
    public void sec() {
        DBManager dbManager = null;
        try {
            dbManager = DBManager.getInstance();
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Exception message",e);
        }
        Assert.assertEquals( true, dbManager!=null);
    }


}
