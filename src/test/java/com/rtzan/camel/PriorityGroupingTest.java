/** Free */
package com.rtzan.camel;

import javax.inject.Inject;

import com.rtzan.model.Customer;
import com.rtzan.model.Product;
import org.apache.camel.cdi.CdiCamelExtension;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 */
@RunWith(Arquillian.class)
public class PriorityGroupingTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private TestSetup testSetup;
    
    @Inject
    private GroupByCustomerFinalProcessor finalProcessor;

    @Deployment
    public static JavaArchive deployment() {
        //J--
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
            // Camel CDI
            .addPackage(CdiCamelExtension.class.getPackage())
            // Test classes
            .addPackages(true, 
                    Filters.exclude(".*Test.*"), GroupByCustomerRouteBuilder.class.getPackage())
            // mock components
            .addClass(TestSetup.class)
            // Bean archive deployment descriptor
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
        ;
        //J++

        //archive.addClass(Account.class);
        return archive;
    }

    @Before
    public void setUp() throws Exception {
        testSetup.setupContext();
    }

    @Test
    public void testPriorityGrouping() throws Exception {
        
        
        Customer customer01 = new Customer("ana");
        Customer customer02 = new Customer("mihai");
        Customer customer03 = new Customer("jeean");
        Customer customer04 = new Customer("bob");

        Product book1 = new Product("book", 10);
        Product book2 = new Product("big_book", 15);
        Product book3 = new Product("book", 10);
        Product book4 = new Product("big_book", 15);

        Product alcohol1 = new Product("wine", 50);
        Product alcohol2 = new Product("vodka", 50);

        Product milk1 = new Product("milk", 5);

        book1.setCustomer(customer01);
        book2.setCustomer(customer01);

        book3.setCustomer(customer02);
        milk1.setCustomer(customer02);
        alcohol1.setCustomer(customer02);

        book4.setCustomer(customer03);

        alcohol2.setCustomer(customer04);

        final List<Product> products = Arrays.asList(book1, book2, book3, book4, milk1, alcohol1, alcohol2);
        
        for (Product product : products) {
            testSetup.getInputEndpoint().sendBody(product);
        }
        
        Thread.sleep(20 * 1000L);
        
        assertEquals(2, finalProcessor.getHistory());
    }

}
