package ir.iau.exchange.service;

import ir.iau.exchange.dto.requestes.CreateUserRequestDto;
import ir.iau.exchange.dto.requestes.SubmitOrderRequestDto;
import ir.iau.exchange.entity.Order;
import ir.iau.exchange.entity.UserAsset;
import ir.iau.exchange.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceIntegrationTests {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    private Authentication auth1;
    private Authentication auth2;

    @BeforeEach
    public void beforeEach() {
        CreateUserRequestDto dto1 = new CreateUserRequestDto();
        dto1.setUsername("junit-test-1");
        dto1.setPassword("123456");
        userService.createUser(dto1);

        CreateUserRequestDto dto2 = new CreateUserRequestDto();
        dto2.setUsername("junit-test-2");
        dto2.setPassword("123456");
        userService.createUser(dto2);

        UserDetails userDetails1 = userDetailsService.loadUserByUsername(dto1.getUsername());
        auth1 = new UsernamePasswordAuthenticationToken(userDetails1, dto1.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        UserDetails userDetails2 = userDetailsService.loadUserByUsername(dto2.getUsername());
        auth2 = new UsernamePasswordAuthenticationToken(userDetails2, dto2.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void matchOrders() {

        // set authenticating to auth1
        auth1();

        // increase balance for auth1
        userService.increaseCurrentUserAsset("A", BigDecimal.ONE);

        // submit auth1's order
        SubmitOrderRequestDto submitOrderRequestDto = new SubmitOrderRequestDto();
        submitOrderRequestDto.setDestinationCode("B");
        submitOrderRequestDto.setDestinationAmount(BigDecimal.TEN);
        submitOrderRequestDto.setSourceCode("A");
        submitOrderRequestDto.setSourceAmount(BigDecimal.ONE); // it means that 1 A is equal 10 B
        orderService.submitForCurrentUser(submitOrderRequestDto);

        // check auth1 asset
        UserAsset auth1AAsset = userService.getCurrentUserAsset("A");
        assertEquals(BigDecimal.ZERO, auth1AAsset.getBalance());

        // check auth1 orders
        checkOneOpenOrder();

        // nothing should change
        orderService.matchOrders();

        // recheck user orders
        checkOneOpenOrder();

        // change user
        auth2();

        // increase balance for auth2
        userService.increaseCurrentUserAsset("B", BigDecimal.TEN);

        // submit order matching to auth1's order by auth2
        submitOrderRequestDto = new SubmitOrderRequestDto();
        submitOrderRequestDto.setDestinationCode("A");
        submitOrderRequestDto.setDestinationAmount(BigDecimal.ONE);
        submitOrderRequestDto.setSourceCode("B");
        submitOrderRequestDto.setSourceAmount(BigDecimal.TEN); // it means that 1 A is equal 10 B
        orderService.submitForCurrentUser(submitOrderRequestDto);

        // check auth2 assets
        UserAsset auth2BAsset = userService.getCurrentUserAsset("B");
        assertEquals(BigDecimal.ZERO, auth2BAsset.getBalance());

        // check auth2 orders
        checkOneOpenOrder();

        // orders should match and seal the deal
        orderService.matchOrders();

        // check change in auth2's orders
        checkOneClosedOrder();

        // check auth2 assets
        UserAsset auth2AAsset = userService.getCurrentUserAsset("A");
        assertEquals(BigDecimal.ONE, auth2AAsset.getBalance());

        // change user
        auth1();

        // check change in auth1's orders
        checkOneClosedOrder();

        // check auth1 assets
        UserAsset auth1BAsset = userService.getCurrentUserAsset("B");
        assertEquals(BigDecimal.TEN, auth1BAsset.getBalance());
    }

    @Test
    void notMatchOrdersInAmount() {

        // set authenticating
        auth1();

        // increase balance for auth1
        userService.increaseCurrentUserAsset("A", BigDecimal.ONE);

        // submit auth1's order
        SubmitOrderRequestDto submitOrderRequestDto = new SubmitOrderRequestDto();
        submitOrderRequestDto.setDestinationCode("B");
        submitOrderRequestDto.setDestinationAmount(BigDecimal.TEN);
        submitOrderRequestDto.setSourceCode("A");
        submitOrderRequestDto.setSourceAmount(BigDecimal.ONE); // it means that 1 A is equal 10 B
        orderService.submitForCurrentUser(submitOrderRequestDto);

        // check auth1 orders
        checkOneOpenOrder();

        // nothing should change
        orderService.matchOrders();

        // recheck user orders
        checkOneOpenOrder();

        // change user
        auth2();

        // increase balance for auth1
        userService.increaseCurrentUserAsset("B", BigDecimal.ONE);

        // submit order not matching to auth1's order by auth2
        submitOrderRequestDto = new SubmitOrderRequestDto();
        submitOrderRequestDto.setDestinationCode("A");
        submitOrderRequestDto.setDestinationAmount(BigDecimal.ONE); // ***** here is the difference *****
        submitOrderRequestDto.setSourceCode("B");
        submitOrderRequestDto.setSourceAmount(BigDecimal.ONE); // it means that 1 A is equal 1 B
        orderService.submitForCurrentUser(submitOrderRequestDto);

        // check auth2 orders
        checkOneOpenOrder();

        // orders should not match to seal the deal
        orderService.matchOrders();

        // check nothing changes in auth2's orders
        checkOneOpenOrder();

        // change user
        auth1();

        // check nothing changes in auth1's orders
        checkOneOpenOrder();
    }

    private void auth1() {
        SecurityContextHolder.getContext().setAuthentication(auth1);
    }

    private void auth2() {
        SecurityContextHolder.getContext().setAuthentication(auth2);
    }

    private void checkOneOpenOrder() {
        List<Order> openOrdersForCurrentUser = orderService.getOpenOrdersForCurrentUser();
        assertNotNull(openOrdersForCurrentUser);
        assertEquals(1, openOrdersForCurrentUser.size());

        List<Order> closedOrdersForCurrentUser = orderService.getClosedOrdersForCurrentUser();
        assertNotNull(closedOrdersForCurrentUser);
        assertEquals(0, closedOrdersForCurrentUser.size());
    }

    private void checkOneClosedOrder() {
        List<Order> openOrdersForCurrentUser = orderService.getOpenOrdersForCurrentUser();
        assertNotNull(openOrdersForCurrentUser);
        assertEquals(0, openOrdersForCurrentUser.size());

        List<Order> closedOrdersForCurrentUser = orderService.getClosedOrdersForCurrentUser();
        assertNotNull(closedOrdersForCurrentUser);
        assertEquals(1, closedOrdersForCurrentUser.size());
    }
}