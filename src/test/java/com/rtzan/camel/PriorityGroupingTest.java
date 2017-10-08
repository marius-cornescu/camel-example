/**
 * Free
 */
package com.rtzan.camel;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.AllTests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 */
@RunWith(Arquillian.class)
public class PriorityGroupingTest {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Instance fields 
    //~ ----------------------------------------------------------------------------------------------------------------

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Deployment
    public static JavaArchive deployment() {
        JavaArchive archive =
        ShrinkWrap.create(JavaArchive.class)
                // Camel CDI
                .addPackage(org.apache.camel.cdi.CdiCamelExtension.class.getPackage())
                // Test classes
                .addPackages(true, MyRouteBuilder.class.getPackage())
                // Bean archive deployment descriptor
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
                ;


        //archive.addClass(Account.class);
        //archive.addAsResource("org/jboss/fuse/persistence/jpa/jpa-camel-context.xml", "META-INF/camel-context.xml");
        return archive;
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testPriorityGrouping() throws Exception {
    }


}
