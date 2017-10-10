/** Free */
package com.rtzan.camel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rtzan.model.Cart;
import com.rtzan.model.CartItem;
import com.rtzan.model.Customer;
import com.rtzan.model.Product;
import com.rtzan.model.Receipt;
import com.rtzan.model.rule.Criteria;
import com.rtzan.model.rule.Rule;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomerSelectionProcessor implements Processor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void process(Exchange exchange) throws Exception {
        Rule rule = exchange.getIn().getHeader("SELECTION_RULE", Rule.class);
        Cart cart = exchange.getIn().getBody(Cart.class);

        List<Product> deductibleProducts = new ArrayList<>();
        Receipt receipt = null;

        logger.info("### Ruling on [{}] ###", cart.getCustomer());

        for (Criteria criteria : rule.getCriterias()) {
            boolean matchRule = ruleOnCriteria(criteria, cart.getCartItems(), deductibleProducts);
            logger.info("Criteria [{}] on customer [{}] with products [{}] had [{}] match", criteria, cart.getCustomer(), cart.getCartItems(), matchRule);

            if (matchRule) {
                receipt = buildReceipt(cart.getCustomer(), deductibleProducts);
                break;
            }
        }

        if (Optional.ofNullable(receipt).isPresent()) {
            exchange.getIn().setBody(receipt);
        }
    }

    private boolean ruleOnCriteria(Criteria criteria, List<CartItem> cartItems, List<Product> deductibleProducts) {
        List<Product> cartProducts = cartItems.stream().map(CartItem::getProduct).collect(Collectors.toList());

        List<Product> potentialDeductibleProducts = new ArrayList<>();
        List<String> productNames = criteria.getProductNames();
        int[] productNameMatches = new int[productNames.size()];
        boolean criteriaMatchAll = false;

        for (Product product : cartProducts) {
            int index = productNames.indexOf(product.getName());
            if (index >= 0) {
                potentialDeductibleProducts.add(product);
                productNameMatches[index] = 1;
            }
            if (!Arrays.toString(productNameMatches).contains("0")) {
                criteriaMatchAll = true;
                deductibleProducts.addAll(potentialDeductibleProducts);
                break;
            }
        }

        return criteriaMatchAll;
    }

    private Receipt buildReceipt(Customer customer, List<Product> products) {
        Receipt receipt = new Receipt(customer);

        for (Product product : products) {
            receipt.addCombo(product.getName(), product.getPrice());
        }

        return receipt;
    }

}
