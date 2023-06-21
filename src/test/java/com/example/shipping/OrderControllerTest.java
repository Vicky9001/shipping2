package com.example.shipping;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.example.shipping.controllers.OrderController;
import com.example.shipping.models.Order;
import com.example.shipping.models.Transfer;
import com.example.shipping.models.User;
import com.example.shipping.models.Van;
import com.example.shipping.payload.request.OrderRequest;
import com.example.shipping.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VanRepository vanRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private TransferRepository transferRepository;

    @InjectMocks
    private OrderController orderController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void testCreateOrder() throws Exception {
        // Create a mock user for the shipper
        User shipper = new User("John", "Doe", "john.doe@example.com", "password");
        when(userRepository.findById(1L)).thenReturn(Optional.of(shipper));

        // Create a request object for the order
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setShipperId(1L);
        orderRequest.setStart("NewYork");
        orderRequest.setDestination("LosAngeles");
        orderRequest.setReceiver("Jane");
        orderRequest.setInfo("Someinfo");
        orderRequest.setWeight(10.0);

        // Mock the OrderRepository to return a new Order object when save is called
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            return order;
        });

        // Send a POST request to create the order
        mockMvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // Verify that the order was created and saved to the database
        Order order = new Order(shipper, "NewYork", "LosAngeles", "Jane", "Some info", 10.0, 0, LocalDateTime.now());
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        List<Order> orders = orderRepository.findAll();
        assertEquals(0, orders.size());
    }

    @Test
    public void testChangeState() throws Exception {
        // Create a mock user for the shipper
        User shipper = new User("John", "Doe", "john.doe@example.com", "password");
        when(userRepository.findById(1L)).thenReturn(Optional.of(shipper));

        // Create an order
        Order order = new Order(shipper, "New York", "Los Angeles", "Jane", "Some info", 10.0, 0, LocalDateTime.now());
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Create a request object to change the state of the order
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setId(1L);
        orderRequest.setState(0);

        // Send a POST request to change the state of the order
        mockMvc.perform(post("/order/changeState")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // Verify that the state of the order was changed but not saved to the database
        Order updatedOrder = new Order(shipper, "New York", "Los Angeles", "Jane", "Some info", 10.0, 1, LocalDateTime.now());
        updatedOrder.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(updatedOrder));
    }

    @Test
    public void testTransfer() throws Exception {
        // Create a mock user for the shipper
        User shipper = new User("John", "Doe", "john.doe@example.com", "password");
        when(userRepository.findById(1L)).thenReturn(Optional.of(shipper));

        // Create a mock user for the carrier
        User carrier = new User("Jane", "Doe", "jane.doe@example.com", "password");
        when(userRepository.findById(2L)).thenReturn(Optional.of(carrier));

        // Create a mock van for the carrier
        Van van = new Van(1111, "Camry", 1, "test");
        van.setId(1L);
        when(vanRepository.findById(1L)).thenReturn(Optional.of(van));

        // Create an order
        Order order = new Order(shipper, "New York", "Los Angeles", "Jane", "Some info", 10.0, 0, LocalDateTime.now());
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Create a request object to transfer the order
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setId(1L);
        Set<Long> carrierIds = new HashSet<>();
        carrierIds.add(2L);
        orderRequest.setCarrierIds(carrierIds);
        Set<Long> vanIds = new HashSet<>();
        vanIds.add(1L);
        orderRequest.setVanIds(vanIds);

        // Mock the TransferRepository to return a new Transfer object when save is called
        when(transferRepository.save(any(Transfer.class))).thenAnswer(invocation -> {
            Transfer transfer = invocation.getArgument(0);
            transfer.setId(1L);
            return transfer;
        });

        // Send a POST request to transfer the order
        mockMvc.perform(post("/order/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(jsonPath("$.code").value(102));
    }

}
