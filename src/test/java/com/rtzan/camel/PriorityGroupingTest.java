/**
 * Free
 */
package com.rtzan.camel;

import com.rtzan.model.Customer;
import com.rtzan.model.Product;
import com.rtzan.model.rule.Criteria;
import com.rtzan.model.rule.Rule;
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

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
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
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
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
        Rule rule = createBasicRule();
        
        List<Product> products = createBasicProducts();

        for (Product product : products) {
            testSetup.getInputEndpoint().sendBodyAndHeader(product, "SELECTION_RULE", rule);
        }

        Thread.sleep(20 * 1000L);

        assertEquals(3, finalProcessor.getHistory().size());
    }

    @Test
    public void testLargeVolumePriorityGrouping() throws Exception {
        int customerCnt = 100;
        int productsPerCustomerCnt = 100;
        
        Rule rule = createBasicRule();
        
        List<Customer> customers = createCustomers("customer_", 100);
        List<Product> products = createProductsForCustomers(customers, 100);
        
        Collections.shuffle(products);

        for (Product product : products) {
            testSetup.getInputEndpoint().sendBodyAndHeader(product, "SELECTION_RULE", rule);
        }

        Thread.sleep(20 * 1000L);

        assertEquals(customerCnt, finalProcessor.getHistory().size());
        assertEquals(productsPerCustomerCnt, finalProcessor.getHistory().get(0).getCartItems().size());
    }
    
    private Rule createBasicRule() {
        Criteria criteria1 = new Criteria(1, "first", Arrays.asList("book", "milk"));
        Criteria criteria2 = new Criteria(2, "second", Arrays.asList("big_book"));
        
        return new Rule("basic_rule", Arrays.asList(criteria1, criteria2));
    }
    
    private List<Product> createBasicProducts() {
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

        return products;
    }

    private List<Customer> createCustomers(String customerPrefix, int customerCount) {
        List<Customer> customers = new ArrayList<>();
        
        final int totalCharCount = String.valueOf(customerCount).length();
        final String customerName = customerPrefix + "%1$0" + totalCharCount + "d";

        for (int i = 0; i < customerCount; i++) {
            customers.add(new Customer(String.format(customerName, i)));
        }
        
        return customers;
    }

    private List<Product> createProductsForCustomers(List<Customer> customers, int productCount) {
        List<Product> products = new ArrayList<>();
        
        List<String> productLabels = Arrays.asList("book", "big_book", "car", "pencil", "wine", "vodka", "milk");

        for (Customer customer : customers) {
            products.addAll(createProductsForCustomer(customer, productLabels, productCount));
        }
        
        return products;
    }

    private List<Product> createProductsForCustomer(Customer customer, List<String> productLabels, int productCount) {
        List<Product> products = new ArrayList<>();
        
        int labelsIndex = 0;

        for (int i = 0; i < productCount; i++) {
            if (labelsIndex >= productLabels.size()) {
                labelsIndex = 0;
            }
            
            Product product = new Product(productLabels.get(labelsIndex++), 10);
            product.setCustomer(customer);
            products.add(product);
        }
        
        return products;
    }

}
